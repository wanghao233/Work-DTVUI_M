package com.changhong.tvos.dtv.epg.minify;

import java.util.List;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class ChannelListFragment extends Fragment {

	protected static final String TAG = "ChannelListFragment";

	public static boolean isChannelListFragmentFocuse = true;//add by YangLiu
	//	private EpgListView mListView;
	public static EpgListView mListView;//change by YangLiu
	private Context mContext;
	private TextView mTitleView;
	private List<DtvProgram> mChannelList;
	private DtvProgram mCurChannel;//add By YangLiu
	//	private ChannelListAdpter mChanneldapter;
	public static ChannelListAdpter mChanneldapter = null;//add By YangLiu

	public static int mlastItemSelectPosition = 0;//add By YangLiu

	private boolean isExit = false;

	ChannelListSelectedListener mChannelListSelected;

	public interface ChannelListSelectedListener {
		public void onChannelListSelected(DtvProgram data);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.epg_minify_channel_list_layout, container, false);
		mListView = (EpgListView) view.findViewById(R.id.channellist);
		mTitleView = (TextView) view.findViewById(R.id.title);
		return view;//Fragment中必须被返回
	}

	@Override
	//当fragment添加到activity中时，会调用fragment的方法onAttach()，这个方法中适合检查activity是否实现了OnArticleSelectedListener接口，检查方法就是对传入的activity的实例进行类型转换
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mChannelListSelected = (ChannelListSelectedListener) activity;//得到监听器对象实例
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString());
		}
	}

	@Override
	//1.
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mChannelList = MenuManager.getInstance().getChannelListByAllchFlag();//change by YangLiu 2015-1-4
		//		Log.i(TAG, "mChannelList.size="+mChannelList.size());

		mCurChannel = DtvChannelManager.getInstance().getCurProgram();//获取当前频道信息    				
		//		Log.i(TAG, "EPG————————————————>当前节目号："+mCurChannel.getProgramNum());

		mContext = getActivity();
		isExit = false;
	}

	@Override
	//2.
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getDataThread();//初始化显示频道列表信息
	}

	//取得频道列表
	/**
	 * 解决：关闭全部节目然后手动排序后，再次打开全部节目，频道列表只显示过滤后的列表的问题
	 * 				add By YangLiu 2014-12-1
	 */
	/*private List<DtvProgram> getChannelListSmartly(){
		if (DtvChannelManager.getInstance().GetAllchFlag()) {
			MenuManager.getInstance().init(MenuManager.listState.channel_List);
			mChannelList = DtvChannelManager.getInstance().getChannelList();
		}else {
			mChannelList =MenuManager.getInstance().getWatchedChannelList();//取得搜索观看过的频道列表
		}				
		return mChannelList;
	}*/

	private void display() {//3.
		mChanneldapter = new ChannelListAdpter(getActivity(), mChannelList, mListView);
		mListView.setAdapter(mChanneldapter);
		mListView.setCacheColorHint(Color.TRANSPARENT);

		//选中频道列表的条目，加载频道列表数据
		mListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				///////////////////add By YangLiu 2014-11-12//////////////
				if (mChannelList.get(arg2).mProgramNum >= 0) {

					mChannelListSelected.onChannelListSelected(mChannelList.get(arg2));//调用EpgActivity中的接口实现方法

					mChanneldapter.notifyDataSetChanged();

					mlastItemSelectPosition = arg2;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		//点击频道列表的条目，触发频道跳转
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				/*if (mPosition!=-1) {
					arg0.getChildAt(mPosition).setBackgroundColor(Color.WHITE);
				}			
				arg0.getChildAt(arg2).findViewById(R.id.mListView).setBackground(getResources().getDrawable(R.color.focused));
				mPosition = arg2;*/

				MenuManager manager = MenuManager.getInstance();
				manager.changeChannelByProgramServiceIndex(mChannelList.get(arg2).getProgramServiceIndex());//更换频道
			}
		});

		//频道列表的条目是否有焦点，触发字体大小改变
		mListView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				isChannelListFragmentFocuse = hasFocus; //add By YangLiu

				ChannelListAdpter mAdapter = (ChannelListAdpter) mListView.getAdapter();
				mAdapter.changListState(hasFocus, mListView.getSelectedItemPosition());

				if (hasFocus) {//有焦点时，标题布局大，字体大，透明度高
					/*RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					layout.setMargins(dip2px(mContext, 70),dip2px(mContext, 30), 0, 0);
					mTitleView.setLayoutParams(layout);
					mTitleView.setGravity(Gravity.CENTER_HORIZONTAL);
					mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
					mTitleView.setAlpha(1);*/

					mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
					mTitleView.setAlpha(1);

				} else {//没焦点时，标题布局小，字体小，透明度低
					/*RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					layout.setMargins(dip2px(mContext, 82),
							dip2px(mContext, 60), 0, 0);
					mTitleView.setLayoutParams(layout);
					mTitleView.setGravity(Gravity.CENTER_HORIZONTAL);
					mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
					mTitleView.setAlpha((float) 0.4);*/

					mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
					mTitleView.setAlpha((float) 0.4);
				}
			}
		});

		//保存视图位置为当前频道
		if (mChannelList != null && mChannelList.size() >= 2) {
			selsectCurChannel();
		}

		//change By YangLiu 2014-12-18
		mChanneldapter.notifyDataSetChanged();
	}

	//获取当前正在播放的频道
	private void selsectCurChannel() {

		int num = mCurChannel.getProgramNum();
		//		Log.i(TAG,"num000= "+num);//下标从0开始，当前前面的频道序号

		for (int i = 0; i < mChannelList.size(); i++) {
			if (mChannelList.get(i).getProgramNum() == num) {
				mListView.setSelection(i);
				break;
			}
		}
	}

	//2.获取频道业务数据
	private void getDataThread() {
		new Thread() {
			public void run() {
				/*	try {
						//DtvChannelManager.getInstance().getChannelList();
						mChannelList =MenuManager.getInstance().getWatchedChannelList();
					} catch (Exception e) {
						// TODO: handle exception
					}*/

				if (!isExit) {
					if (mChannelList != null && mChannelList.size() > 0) {
						Message message = mHandler.obtainMessage(0);
						mHandler.sendMessage(message);//调用显示频道列表信息
						//						Log.i(TAG, "直接发送消息，已有频道数为:"+mChannelList.size());
					} else {
						DtvProgram mDtvProgram = new DtvProgram();
						mDtvProgram.setProgramNum(0);
						mDtvProgram.setProgramName("暂无数据");
						mChannelList.add(mDtvProgram);//加载所有频道信息

						Message message = mHandler.obtainMessage(0);
						mHandler.sendMessage(message);//调用显示频道列表信息
						//						Log.i(TAG, "先加载频道列表在发送消息,已有频道数为:"+mChannelList.size());
					}
				}
			}
		}.start();
	}

	//3.
	@SuppressLint("HandlerLeak")
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
	public void onResume() {
		// TODO Auto-generated method stub
		mListView.setFocusable(true);
		mListView.requestFocus();
		super.onResume();
	}

	//根据手机的分辨率从 DP 的单位 转成为 PX(像素)
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