package com.changhong.tvos.dtv.epg.minify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvEpgManager;
import com.changhong.tvos.dtv.tvap.DtvScheduleManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvEvent;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.DtvScheduleEvent;
import com.changhong.tvos.system.commondialog.CommonAcionDialog;
import com.changhong.tvos.system.commondialog.VchCommonToastDialog;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class ProgramListFragment extends Fragment {

	//	private EpgListView mListView;
	public static EpgListView mListView;//change by YangLiu
	private Context mContext;
	private ProgramListAdpter mProgramAdapter;
	//	private View mView;
	private TextView mTitleView;
	private boolean isExit = false;
	private boolean isFocuse = false;
	private Date mcurDate;
	private boolean hasNoEvent = false;
	public static boolean isProgramListFragmentHasFocus = false;

	private List<DtvEvent> mProgramList;
	private DtvProgram mSelectedChannel;

	private MenuManager mMenuManager = MenuManager.getInstance();
	private DtvEpgManager mEpgManager = DtvEpgManager.getInstance();
	public static DtvScheduleManager mscheduleManager = DtvScheduleManager.getInstance();

	ProgramListSelectedListener mProgramListSelected;
	VchCommonToastDialog mToastDialog = null;

	DtvEvent mSelectEvent;

	public interface ProgramListSelectedListener {
		public void onProgramListSelected(int data);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.epg_minify_program_list_layout, container, false);
		mListView = (EpgListView) view.findViewById(R.id.programlist);
		mTitleView = (TextView) view.findViewById(R.id.title);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		try {
			mProgramListSelected = (ProgramListSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();

		mMenuManager = MenuManager.getInstance();
		//		Date curDate = EpgActivity.curDate;//MenuManager.getInstance().getCurrentDate();		
		mcurDate = mMenuManager.getCurrentDate();//EpgActivity.curDate;

		isExit = false;
		hasNoEvent = false;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	private void display() {
		mProgramAdapter = new ProgramListAdpter(getActivity(), mProgramList, mListView, isFocuse, mSelectedChannel);
		mListView.setAdapter(mProgramAdapter);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		if (isFocuse) {//取得节目列表焦点
			getFocuse();
		}

		//更新节目图标
		mListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				/**
				 * 解决频道列表图标不同步问题（解决方案：及时更新显示）
				 *        YangLiu 2014-10-25
				 */
				mProgramAdapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		//节目条目点击事件，完成节目预约和跳转
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				mcurDate = mMenuManager.getCurrentDate();

				if (hasNoEvent) {
					ShowDialog(R.string.menu_epg_program_noEPGdata);//无节目数据,不能预约!

				} else if (mProgramList.get(arg2).getEndTime().before(mcurDate)) {
					ShowDialog(R.string.menu_epg_program_outdated);//该节目已过期,不能预约!	

				} else if (mProgramList.get(arg2).getStartTime().before(mcurDate) && mProgramList.get(arg2).getEndTime().after(mcurDate)) {//该节目正在播放
					mMenuManager.changeChannelByProgramServiceIndex(mSelectedChannel.getProgramServiceIndex());

				} else if (mProgramList.get(arg2).getStartTime().after(mcurDate)) {//该节目还没播放，可以预约

					//////////////////////fengy////2014-10-08/////////////
					mSelectEvent = mProgramList.get(arg2);

					List<DtvScheduleEvent> scheEventlist = mscheduleManager.getScheduleEvents();
					if (scheEventlist != null && scheEventlist.size() >= 36 && !mscheduleManager.isOrderEvent(mSelectEvent, mSelectedChannel)) {
						/*mToastDialog = new VchCommonToastDialog(mContext);
						mToastDialog.setMessage(R.string.menu_epg_schedule_count);
						mToastDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
						mToastDialog.getWindow().setType(2003);
						mToastDialog.setDuration(3);
						mToastDialog.show();
						*/
						ShowDialog(R.string.menu_epg_schedule_count);//已预约36个节目，不能再预约!

					} else {
						DtvScheduleEvent oldScheduleEvent = mscheduleManager.getConflictEvent(mSelectEvent, mSelectedChannel);
						if (oldScheduleEvent != null) {
							/* mToastDialog = new VchCommonToastDialog(mContext);
							 * mToastDialog.setMessage(R.string.menu_epg_schedule_conflict_title);
							 * mToastDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
							 * mToastDialog.getWindow().setType(2003);
							 * mToastDialog.setDuration(3); mToastDialog.show();
							 */
							//	ShowDialog(R.string.menu_epg_schedule_conflict_title);// 预约时间冲突，不能再预约!						
							showReplaceConflictEventDialog(oldScheduleEvent);//替换已有冲突预约节目

						} else {
							if (mscheduleManager.isOrderEvent(mSelectEvent, mSelectedChannel)) {

								mscheduleManager.delScheduleEvent(mSelectEvent, mSelectedChannel);//如果已经是预约节目则删除预约节目
							} else {

								mscheduleManager.addScheduleEvent(mSelectEvent, mSelectedChannel);//如果还不是预约节目则添加预约节目
							}
						}
					}
					mProgramAdapter.notifyDataSetChanged();//add by YangLiu
				}
			}
		});

		//当节目列表焦点变化时，改变字体大小
		mListView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				// TODO Auto-generated method stub

				ProgramListAdpter mAdapter = (ProgramListAdpter) mListView.getAdapter();

				mAdapter.changListState(hasFocus, mListView.getSelectedItemPosition());

				isProgramListFragmentHasFocus = hasFocus;//////add By YangLiu 2014-12-15
				if (hasFocus) {
					/*RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					layout.setMargins(dip2px(mContext, 50),
							dip2px(mContext, 40), 0, 0);
					mTitleView.setLayoutParams(layout);*///2015-4-23 YangLiu

					mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
					mTitleView.setAlpha(1);
				} else {
					/*RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					layout.setMargins(dip2px(mContext, 56),
							dip2px(mContext, 56), 0, 0);
					mTitleView.setLayoutParams(layout);*///2015-4-23 YangLiu

					mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
					mTitleView.setAlpha((float) 0.4);
				}
			}
		});

		//保存视图位置为起始项
		if (mProgramList != null && mProgramList.size() >= 2) {
			changSelected(0);
		}

		//	add By YangLiu 2014-12-18 	
		mProgramAdapter.notifyDataSetChanged();
	}

	//显示提示对话框
	private void ShowDialog(int mesId)//该节目已过期，不能预约！
	{
		mToastDialog = new VchCommonToastDialog(mContext);
		//mToastDialog.setMessage(R.string.menu_epg_schedule_conflict_title);
		mToastDialog.setMessage(mesId);
		mToastDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
		mToastDialog.getWindow().setType(2003);
		mToastDialog.setDuration(3);
		mToastDialog.show();
	}

	/**
	 * 解决预约时间冲突时，将新的节目预约替换掉老的节目预约
	 * 		add By YangLiu 2014-11-27
	 */
	private void showReplaceConflictEventDialog(DtvScheduleEvent conflictScheduleEvent) {

		final DtvScheduleEvent oldScheduleEvent = conflictScheduleEvent;
		final CommonAcionDialog confirmDialog = new CommonAcionDialog(mContext, R.string.no_string, 0, 0, 30);
		confirmDialog.getWindow().setType(2003);
		//	confirmDialog.setTitle("预约冲突，是否替换");
		confirmDialog.setTitle(R.string.menu_epg_schedule_conflict_or_cancle);
		@SuppressWarnings("deprecation")
		int hours = oldScheduleEvent.getStartTime().getHours();
		@SuppressWarnings("deprecation")
		int minutes = oldScheduleEvent.getStartTime().getMinutes();
		if (minutes == 0) {
			//	confirmDialog.setSubTitleText("已预约 "+hours + ":00");
			confirmDialog.setSubTitleText(mContext.getString(R.string.menu_epg_alreades_schedule) + hours + ":00");
		} else {
			//	confirmDialog.setSubTitleText("已预约 "+hours + ":" + minutes);
			confirmDialog.setSubTitleText(mContext.getString(R.string.menu_epg_alreades_schedule) + hours + ":" + minutes);
		}
		confirmDialog.setSubTitleTextSize(17);
		confirmDialog.setMessage(oldScheduleEvent.getTitle());
		confirmDialog.setCancelButtonListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				confirmDialog.dismiss();
			}
		});

		confirmDialog.setOKButtonListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 替换冲突的预约节目  2014-12-30  YangLiu
				mscheduleManager.replaceConflictEvent(oldScheduleEvent, new DtvScheduleEvent(mSelectEvent, mSelectedChannel));
				confirmDialog.dismiss();
			}
		});

		confirmDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
			}
		});

		confirmDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
			}
		});
		confirmDialog.setCancelable(true);
		confirmDialog.setDefaultFocusButton(CommonAcionDialog.FOCUS_BUTTON_OK);
		confirmDialog.setDuration(30);
		confirmDialog.setOkButtonText(R.string.yes);
		confirmDialog.setCancelButtonText(R.string.no);
		confirmDialog.show();
	}

	//改变选中节目条目的位置
	public void changSelected(int position) {
		mListView.setSelection(position);
		/*ProgramListAdpter mAdapter = (ProgramListAdpter) mListView.getAdapter();
		mAdapter.changListState(false, mListView.getSelectedItemPosition());//更新操作*/
	}

	//获取节目事务数据
	public void getDataThread(final DtvProgram program, final int dayIndex, boolean isNeedFocuse) {
		isFocuse = isNeedFocuse;
		mSelectedChannel = program;
		mProgramList = new ArrayList<DtvEvent>();
		new Thread() {
			public void run() {
				try {
					if (mProgramList == null || mProgramList.size() == 0) {
						//获取当前时间
						mcurDate = mMenuManager.getCurrentDate();
						//获取所有节目列表
						mProgramList = mEpgManager.getEventsByTime(program.getProgramServiceIndex(), dayIndex, 0, 24);
						//解决取得的节目从早上8点开始，掩盖了当前时段的节目     2014-12-30 Add By YangLiu
						//获取当前时间之后的节目列表
						mProgramList = mEpgManager.getEventsFromNowOn(mcurDate, mProgramList);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (!isExit) {//添加节目事务信息，即每个时间段的节目
					if (mProgramList != null && mProgramList.size() > 0) {
						hasNoEvent = false;
					} else {
						if (!(mProgramList.size() > 0)) {/////////////// add By YangLiu
							mcurDate = mMenuManager.getCurrentDate();
							DtvEvent mDtvEvent = new DtvEvent();
							mDtvEvent.setStartTime(mcurDate);
							mDtvEvent.setEndTime(mcurDate);
							mDtvEvent.setTitle(getActivity().getString(R.string.menu_epg_program_null));//"无当前节目信息！" YangLiu 2015-7-24
							mProgramList.add(mDtvEvent);
							hasNoEvent = true;
						}
					}
					Message message = mHandler.obtainMessage(0);
					mHandler.sendMessage(message);
				}
			}
		}.start();
	}

	public void initEpgList(List<?> dtvEvents) {//初始化EPG列表前清空节目列表信息

		if (mProgramList != null) {
			mProgramList.clear();
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case 0:
				display();
				break;
			}
		}
	};

	@Override
	public void onHiddenChanged(boolean hidden) {//隐藏焦点

		// TODO Auto-generated method stub
		if (!hidden) {
			mListView.setFocusable(true);
			mListView.requestFocus();
		}
		super.onHiddenChanged(hidden);
	}

	public void getFocuse() {//取得焦点
		mListView.setFocusable(true);
		mListView.requestFocus();
	}

	public int dip2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		isExit = true;
		super.onStop();
	}
}