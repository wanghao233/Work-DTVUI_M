package com.changhong.tvos.dtv.epg.normal;

import java.util.List;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvScheduleManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvScheduleEvent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class OrderListFragment extends Fragment {

	private EpgListView mListView;
	private TextView mNoTimerView;
	private Context mContext;
	private OrderListAdpter mOrderAdapter;
	private boolean isExit = false;
	//	private boolean mListViewflag = false;
	//	private boolean mNoTimerViewflag = false;

	public static boolean hasOrderList = false;
	private List<DtvScheduleEvent> mOrderList = null;

	//	private List<DtvScheduleEvent> mOrderTempList = null;

	//	private MenuManager mMenuManager = MenuManager.getInstance();
	//	private DtvScheduleManager mscheduleManager =DtvScheduleManager.getInstance();  //2015-3-23

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//		View view = inflater.inflate(R.layout.epg_order_list_layout, container, false);
		View view = LayoutInflater.from(mContext).inflate(R.layout.epg_normal_order_list_layout, null);

		mListView = (EpgListView) view.findViewById(R.id.orderlist);

		mNoTimerView = (TextView) view.findViewById(R.id.notimer);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mContext = EpgActivity.getEpgActivity();//change by YangLiu 2015-2-12

		isExit = false;
		hasOrderList = false;

		//解决手动排序后预约节目图标错位问题||解决过滤掉的节目预约后再次显示且没有图标 	2014-12-30	YangLiu
		mOrderList = DtvScheduleManager.getInstance().getChangedScheduleEvents(MenuManager.getInstance().getWatchedChannelList());
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		isExit = true;
		super.onStop();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {//获取预约数据并显示
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getDataThread();
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public PopupWindow mOrderPopueWindow(Context context, final View parent,//点击预约节目，弹出“播放”和“取消预约”窗口
			final int poisition) {

		Button play, cancel;

		LayoutInflater lay = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = lay.inflate(R.layout.epg_normal_order_popue_layout, null);

		play = (Button) v.findViewById(R.id.play);

		cancel = (Button) v.findViewById(R.id.cancel);

		final PopupWindow mOrderWindow = new PopupWindow(v,//mOrderWindow弹出框
				parent.getWidth() / 3, parent.getHeight());
		mOrderWindow.setFocusable(true);
		mOrderWindow.setOutsideTouchable(true);
		mOrderWindow.update();
		mOrderWindow.setBackgroundDrawable(new BitmapDrawable());
		mOrderWindow.showAtLocation(parent, Gravity.NO_GRAVITY, parent.getRight(), parent.getTop() + dip2px(context, 120));

		play.setOnKeyListener(new OnKeyListener() {//点击向左按键，弹出的确定框消失
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent event) {
				// TODO Auto-generated method stub

				//在”取消预约“上点击菜单键后一次性退出所有EPG菜单		YangLiu 2014-12-1
				if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {
					if (mOrderWindow != null && mOrderWindow.isShowing()) {
						mOrderWindow.dismiss();
					}
					getActivity().finish();
					/*try {
						this.finalize();
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				}

				if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) {
					if (mOrderWindow != null && mOrderWindow.isShowing()) {
						mOrderWindow.dismiss();
					}
				}
				return false;
			}
		});

		play.setOnClickListener(new View.OnClickListener() {//点击播放时节目跳转
			@Override
			public void onClick(View v) {

				MenuManager.getInstance().changeChannelByProgramServiceIndex(mOrderList.get(poisition).getProgramServiceIndex());
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {//点击取消时从预约节目中移除
			@Override
			public void onClick(View v) {

				MenuManager.getInstance().delScheduleEvent(mOrderList.get(poisition));
				mOrderWindow.dismiss();
				mOrderList.remove(poisition);

				display();

				Log.i("cancel....", "setSelection=" + poisition);
				if (poisition > 0) {//(poisition< mOrderList.size()))
					if (poisition < mOrderList.size()) {
						mListView.setSelection(poisition);
					} else {
						mListView.setSelection(poisition - 1);
					}
				}
			}
		});

		cancel.setOnKeyListener(new OnKeyListener() {//点击向左按键，弹出的确定框消失
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent event) {
				// TODO Auto-generated method stub

				//在”取消预约“上点击菜单键后一次性退出所有EPG菜单 	   YangLiu 2014-12-1
				if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {
					if (mOrderWindow != null && mOrderWindow.isShowing()) {
						mOrderWindow.dismiss();
					}
					getActivity().finish();
				}

				if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) {
					if (mOrderWindow != null && mOrderWindow.isShowing()) {
						mOrderWindow.dismiss();
					}
				}
				return false;
			}
		});

		mOrderWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				OrderListAdpter mAAdapter = (OrderListAdpter) mListView.getAdapter();
				mAAdapter.changListState(true, mListView.getSelectedItemPosition());//设置预约节目列表的显示状态
			}
		});
		return mOrderWindow;
	}

	private void display() {
		Log.i("OrderList", "display...." + hasOrderList);

		//		if(hasOrderList)
		//		{
		mOrderAdapter = new OrderListAdpter(getActivity(), mOrderList, mListView);
		mListView.setAdapter(mOrderAdapter);
		mListView.setCacheColorHint(Color.TRANSPARENT);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

				mOrderPopueWindow(mContext, arg1, arg2);//点击弹出窗口

				OrderListAdpter mAdapter = (OrderListAdpter) mListView.getAdapter();
				mAdapter.changListState(false, arg2);//并设置状态
			}
		});

		mListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (mOrderAdapter != null)
					mOrderAdapter.notifyDataSetChanged();
				/////////////////////////////////////////// add By YangLiu
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		mListView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				/////////////////////////////////////////////
			}
		});
		//		}
		////////////////fengy 2014-10-10/////////	
		if (hasOrderList) {
			mNoTimerView.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
		} else {
			mNoTimerView.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}
	}

	private void getDataThread() {
		new Thread() {
			public void run() {
				try {
					//					mOrderList = mMenuManager.getScheduleEvents();
					//					mOrderList = mscheduleManager.getScheduleEvents();	
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (!isExit) {
					if (mOrderList != null && mOrderList.size() > 0) {
						hasOrderList = true;
					} else {
						hasOrderList = false;
					}
					Message message = mHandler.obtainMessage(0);
					mHandler.sendMessage(message);
				}
			}
		}.start();
	}

	Handler mHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case 0:
				Log.i("OrderList", "disp Data....");
				display();
				break;
			}
		}
	};

	public int dip2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public int px2dip(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("OrderListFragment", "OrderListFragment>onDestroy");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		mListView.setFocusable(true);
		mListView.requestFocus();
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
}