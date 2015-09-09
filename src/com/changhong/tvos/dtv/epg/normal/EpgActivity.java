package com.changhong.tvos.dtv.epg.normal;

import java.util.Calendar;
import java.util.Date;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.epg.normal.ChannelListFragment.ChannelListSelectedListener;
import com.changhong.tvos.dtv.epg.normal.ProgramListFragment.ProgramListSelectedListener;
import com.changhong.tvos.dtv.epg.normal.TypeListFragment.TypeListSelectedListener;
import com.changhong.tvos.dtv.epg.normal.WeekListFragment.WeekListSelectedListener;
import com.changhong.tvos.dtv.tvap.DtvScheduleManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;

/**
 * FragmentManager能够实现管理activity中fragment.
 * 通过调用activity的getFragmentManager()取得它的实例
 * @author YangLiu
 */
public class EpgActivity extends FragmentActivity implements TypeListSelectedListener, ChannelListSelectedListener, ProgramListSelectedListener, WeekListSelectedListener {
	private static final String TAG = "EpgActivity";

	private ChannelListFragment channel;// 频道列表
	private OrderListFragment order;// 预约列表
	private ProgramListFragment program;// 节目列表
	private WeekListFragment week;// 周信息
	private TypeListFragment type;

	// public static DtvProgram mCurChannel;//当前的频道 delete By YangLiu
	private DtvProgram mSelectedChannel;// 选择的频道
	private int mDayIndex = 0;
	private String mType = "hot";
	//	private int mTheSameLastKeyCodeCounts = 0; // add By YangLiu
	//	private int mTheSameFirstKeyCodeCounts = 0; // add By YangLiu
	private static EpgActivity mEpgActivity = null;
	private Context mContext;

	private View mView1, mView2, mView3;
	public static int todayOfWeek;
	private Calendar mCalendar = Calendar.getInstance();
	private DtvScheduleManager mscheduleManager = DtvScheduleManager.getInstance();// 预约管理
	// private DtvProgram mCurProgram;//当前的节目 delete by YangLiu
	private Date curDate;// 当前的日期

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			unregisterReceiver(this);
			onStop();
		}
	};

	// 2.
	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction("TV_LongPressMenuKey");
		this.registerReceiver(this.broadcastReceiver, filter);
	}

	@Override
	// 8.
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	// 1.
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.epg_normal_layout);
		mContext = this;
		mEpgActivity = this;

		mView1 = (View) findViewById(R.id.right1);// 节目展开图标
		mView2 = (View) findViewById(R.id.right2);// 星期展开图标
		mView3 = (View) findViewById(R.id.right3);// 星期展开图标

		type = new TypeListFragment();
		channel = new ChannelListFragment();
		order = new OrderListFragment();
		program = new ProgramListFragment();
		week = new WeekListFragment();// 初始化频道，节目，星期，顺序信息

		curDate = mscheduleManager.getCurrentDate();
		mCalendar.setTime(curDate);
		todayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;// 获取当前星期信息

		super.onCreate(savedInstanceState);//2015-3-2 从第一行移动至此 父类方法调用子类数据导致的异常

		// mCurChannel = DtvChannelManager.getInstance().getCurProgram();
		// ////////////////////////delete By YangLiu

		// FragmentTransaction（事务）对fragment进行添加,移除,替换,以及执行其他动作。
		// 每一个事务都是同时要执行的一套变化.可以在一个给定的事务中设置你想执行的所有变化,使用诸如 add(),remove(),和
		// replace().然后,要给activity应用事务,必须调用 commit().
		// 在调用commit()之前,你可能想调用 addToBackStack(),将事务添加到一个fragment事务的back stack.
		// 这个back stack由activity管理, 并允许用户通过按下 BACK 按键返回到前一个fragment状态.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		ft.add(R.id.type, type);
		ft.add(R.id.channel, channel);
		ft.add(R.id.program, program);
		ft.add(R.id.week, week);
		ft.hide(channel);
		ft.hide(program);
		ft.hide(week);
		ft.commit();
	}

	@Override
	// 7.
	protected void onStop() {
		try {
			this.unregisterReceiver(broadcastReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}

		super.onStop();
		finish();
	}

	/**
	 * 解决频道列表不能循环切换问题（解决方案：重新捕捉按键，切换首尾位置）
	 */
	/*@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (!mType.equals("hot")) {
				if (channel.isChannelListFragmentFocuse
						&& channel.mlastItemSelectPosition == channel.mChanneldapter.getCount() - 1) {
					mTheSameLastKeyCodeCounts++;
				
					if (mTheSameLastKeyCodeCounts == 2) {
						mTheSameLastKeyCodeCounts = 0;
						channel.mListView.setSelection(0);
					}
				}
			}
			return super.dispatchKeyEvent(event);
		} else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
			if (!mType.equals("hot")) {
				if (channel.isChannelListFragmentFocuse
						&& channel.mlastItemSelectPosition == 0) {
					mTheSameFirstKeyCodeCounts++;// ChannelListFragment.isChannelListFragmentFocuse

					if (mTheSameFirstKeyCodeCounts == 2) {
						mTheSameFirstKeyCodeCounts = 0;
						channel.mListView.setSelection(channel.mChanneldapter.getCount() - 1);
					}
				}
			}
			return super.dispatchKeyEvent(event);
		} else {
			return super.dispatchKeyEvent(event);
		}
	}*/

	// ////////////////////////////////////////add by YangLiu

	// 4.
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_CHANGHONGKB_MENU:
		case KeyEvent.KEYCODE_MENU:
			finish();
			break;

		case KeyEvent.KEYCODE_BACK:// 按下返回键时
			channel.isCanFocuse(false);
			if (week.isVisible()) {// 当前显示为week信息，则将它隐藏，并将节目信息显示，设置星期信息图标可以显示
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.hide(week);
				ft.commit();
				mView3.setVisibility(View.VISIBLE);
				Log.i(TAG, "EPG(back)——————————week——>program");
				break;
			} else if (program.isVisible()) {// 当前显示为program信息，则将它隐藏，设置星期信息图标不显示，将节目信息图标显示
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				/**
				 * 解决切换频道后记录上次星期的节目，改为每次都初始显示当天的节目信息
				 */
				ft.remove(week);
				week = new WeekListFragment();
				ft.add(R.id.week, week);
				ft.hide(week);

				ft.hide(program);

				/**
				 * 当频道改变后再回到节目列表使其焦点为第一个
				 */
				ft.remove(program);
				program = new ProgramListFragment();
				ft.add(R.id.program, program);
				ft.hide(program);

				ft.commit();

				program.getDataThread(mSelectedChannel, 0, false);
				// //////////add By YangLiu 2014-12-03/////////////

				channel.mListView.setFocusable(true);
				// ////////add By YangLiu 2014-12-04////////////

				mView3.setVisibility(View.GONE);
				mView2.setVisibility(View.VISIBLE);
				Log.i(TAG, "EPG(back)——————————program——>channel");
				break;
			} else if (channel.isVisible()) {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.hide(channel);
				ft.commit();

				mView2.setVisibility(View.GONE);
				mView1.setVisibility(View.VISIBLE);
				break;
			} else if (order.isAdded()) {// 当预约列表被加载时，将频道列表信息将其替换

				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.type, type);
				// newFragment(channel)
				// 替换了当前layout容器中的由R.id.channel标识的fragment.通过调用
				// addToBackStack(), replace事务被保存到back stack,
				// 因此用户可以回退事务,并通过按下BACK按键带回前一个fragment
				ft.commit();

				channel.isChannelListFragmentFocuse = true;// add By YangLiu

				Log.i(TAG, "EPG(back)——————————order——>channel");
				break;
			} else {
				finish();
			}
			break;

		case KeyEvent.KEYCODE_DPAD_RIGHT:// 按下右键时

			if (type.isAdded() && channel.isHidden()) {
				Log.i("right", "channel");
				/**
				 * 	**********************************各个分类数据的显示控制***************************
				 */
				// 不是热门数据的显示控制：!"hot".equals(mType)
				if (!"hot".equals(mType)) {
					if ("common".equals(mType)) {
						if (ChannelListFragment.mCommonList == null || ChannelListFragment.mCommonList.size() == 0) {
							DtvScheduleManager.showHintToast(mContext, getResources().getString(R.string.menu_epg_no_channel), 1);//没有此分类数据
							Log.i(TAG, "mCommonList is null or mCommonList's size is 0, don't display");
							break;
						}
					} else {
						if (channel.getTypeList() == null || channel.getTypeList().size() == 0) {
							DtvScheduleManager.showHintToast(mContext, getResources().getString(R.string.menu_epg_no_channel), 1);//没有此分类数据
							Log.i(TAG, "mTypeList is null or mTypeList's size is 0, don't display");
							break;
						}
					}
					// 热门数据的显示控制："hot".equals(mType)
				} else {
					mView2.setVisibility(View.GONE);

					if (ChannelListFragment.mBWiKiList == null || ChannelListFragment.mBWiKiList.size() <= 0) {
						Log.i(TAG, "mHotList is null or mHotList's size is 0, don't display");
						break;
					} else {
						mView2.setVisibility(View.VISIBLE);
					}
				}

				channel.isCanFocuse(true);
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.show(channel);
				ft.commit();
				channel.mListView.setFocusable(true);

				mView1.setVisibility(View.GONE);

				/*if (!mType.equals("hot")) {
					mView2.setVisibility(View.VISIBLE);
				} else {
					mView2.setVisibility(View.GONE);
				}
				mView1.setVisibility(View.GONE);*/

				Log.i(TAG, "EPG(right)——————————type——>channel");

				break;
			} else if (type.isAdded() && channel.isVisible() && program.isHidden()) {// 当频道列表被加载和节目列表隐藏时（当前显示频道信息），显示节目列表信息，星期图标可见，节目信息图标不可见			
				if (!mType.equals("hot")) {
					channel.isCanFocuse(false);
					Log.i("KEYCODE_DPAD_RIGHT", "KEYCODE_DPAD_RIGHT---  00000" + todayOfWeek);

					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
					ft.show(program);

					ft.commit();

					channel.mListView.setFocusable(false);
					// ////////add By YangLiu 2014-12-04//////////////

					mView3.setVisibility(View.VISIBLE);
					mView2.setVisibility(View.GONE);

					Log.i(TAG, "EPG(right)——————————channel——>program");
				}
				break;
			} else if (type.isAdded() && program.isVisible() && week.isHidden()) {// 当频道列表被加载和星期隐藏时（当前显示节目信息），显示星期信息，星期信息图标不可见
				if (!mType.equals("hot")) {
					Log.i("KEYCODE_DPAD_RIGHT", "KEYCODE_DPAD_RIGHT---  111111");
					channel.isCanFocuse(false);
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
					/**
					 * 当星期改变后再回到节目列表使其焦点为第一个
					 */
					ft.remove(program);
					program = new ProgramListFragment();
					ft.add(R.id.program, program);
					ft.show(week);

					ft.commit();

					program.getDataThread(mSelectedChannel, mDayIndex, false);
					// /////////// add By YangLiu 2014-12-03//////////////

					mView3.setVisibility(View.GONE);
					Log.i(TAG, "EPG(right)——————————program——>week");
				}
				break;
			} else if (order.isAdded()) {// 当预约列表被加载时，将频道列表信息将其替换

				Log.i("KEYCODE_DPAD_RIGHT", "KEYCODE_DPAD_RIGHT---  2222222");

				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.type, type);

				ft.commit();

				channel.isChannelListFragmentFocuse = true;// add By YangLiu

				Log.i(TAG, "EPG(right)——————————order——>type");
			}
			Log.i("KEYCODE_DPAD_RIGHT", "KEYCODE_DPAD_RIGHT---  333333");

			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:// 按下左键
			channel.isCanFocuse(false);
			if (week.isVisible()) {// 当前显示week信息，隐藏星期信息，显示节目列表信息，星期信息图标可见
				Log.i("KEYCODE_DPAD_LEFT", "KEYCODE_DPAD_LEFT---  00000");

				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.hide(week);
				ft.commit();
				mView3.setVisibility(View.VISIBLE);
				Log.i(TAG, "EPG(left)——————————week——>program");
				break;
			} else if (program.isVisible()) {// 当前显示program信息，隐藏节目信息，显示频道列表信息，节目信息图标可见

				Log.i("KEYCODE_DPAD_LEFT", "KEYCODE_DPAD_LEFT---  11111");

				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				/**
				 * 解决切换频道后记录上次星期的节目，改为每次都初始显示当天的节目信息
				 */
				ft.remove(week);
				week = new WeekListFragment();
				ft.add(R.id.week, week);
				ft.hide(week);

				ft.hide(program);

				/**
				 * 当频道改变后再回到节目列表使其焦点为第一个
				 */
				ft.remove(program);
				program = new ProgramListFragment();
				ft.add(R.id.program, program);
				ft.hide(program);

				ft.commit();

				program.getDataThread(mSelectedChannel, 0, false);
				// //////////add By YangLiu 2014-12-03/////////////

				channel.mListView.setFocusable(true);
				// ////////add By YangLiu 2014-12-04////////////

				mView3.setVisibility(View.GONE);
				mView2.setVisibility(View.VISIBLE);
				Log.i(TAG, "EPG(left)——————————program——>channel");
				break;
			} else if (channel.isVisible()) {

				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.hide(channel);
				ft.commit();
				mView2.setVisibility(View.GONE);
				mView1.setVisibility(View.VISIBLE);
				break;
			} else if (type.isAdded()) {// 当频道列表被加载时，用预约列表信息将其替换
				Log.i("KEYCODE_DPAD_LEFT", "KEYCODE_DPAD_LEFT---  22222");
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.type, order);
				ft.commit();

				channel.isChannelListFragmentFocuse = false;// add ByYangLiu

				Log.i(TAG, "EPG(left)——————————channel——>order");
				break;
			}
			Log.i("KEYCODE_DPAD_LEFT", "KEYCODE_DPAD_LEFT---  33333");
			break;

		default:
			break;
		}
		return false;
	}

	// 3.	实现ChannelListFragment中的接口
	@Override
	public void onChannelListSelected(DtvProgram data) {// 频道列表被选中
		// TODO Auto-generated method stub
		mSelectedChannel = data;
		// program.getDataThread(mSelectedChannel, mDayIndex, false);//
		// 根据选择的频道更新节目事务
		// //// change by YangLiu 2014-12-03///////////
		program.getDataThread(mSelectedChannel, 0, false);// 根据选择的频道更新节目事务
	}

	// 6.
	@Override
	// 实现WeekListFragment中的接口
	public void onWeekListSelected(int week) {// 周列表被选中
		// TODO Auto-generated method stub
		mDayIndex = week;
		program.getDataThread(mSelectedChannel, mDayIndex, false);// 根据选择的星期更新节目事务
	}

	// 5.
	@Override
	// 实现ProgramListFragment中的接口
	public void onProgramListSelected(int data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTypeListSelected(ChannelType data) {
		// TODO Auto-generated method stub
		mType = data.getType();
		channel.getDataThread(data);
	}

	/*@Override	2015-3-19  在ChannelListFragment 里的回调接口
	public void setIsHasHDChannels(boolean isHasHD) {
		// TODO Auto-generated method stub
		type.setIsHasHD(isHasHD);
	}*/

	/**
	 * add By YangLiu 2015-1-27
	 * @return
	 */
	public static EpgActivity getEpgActivity() {
		return mEpgActivity;
	}

	public static void finishEPG() {
		if (mEpgActivity != null) {
			mEpgActivity.finish();
		}
	}
}