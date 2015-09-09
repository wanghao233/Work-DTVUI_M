package com.changhong.tvos.dtv.channel_list;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.channel_list.MyChannelListView.KeyDownActionListener;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.util.ViewChannelInfo;

public class MenuChannelList extends Dialog {
	private static String TAG ="MenuChannelList";
	private Context mContext =null;
	
	private boolean isShowTv = false;
	private MyChannelListView mmChannelListView = null;
	private MenuManager.listState mListState =null;
	private long mShowTime = 30000;
	private Handler mHandler = new Handler();
	private Runnable mRunShow = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(mContext!=null&&(mContext instanceof Activity)&&(!((Activity)mContext).isFinishing())){
				Log.i(TAG,"LL mContext = " + mContext + "isFinish = " + ((Activity)mContext).isFinishing());
				dismiss();
			}
		}
	};
	
	public MenuChannelList(Context context) {
		super(context,R.style.Theme_ActivityTransparent);

		mContext =context;
		
		Log.v("my view", "****try to channel MenuChannelList");
		mmChannelListView = new MyChannelListView(mContext);
		
		setContentView(mmChannelListView);
		
		
		this.mListState =MenuManager.listState.channel_List;
		mmChannelListView.setKeyDownActionListener(new KeyDownActionListener() {
			
			@Override
			public void updateShowTime() {
				
				mHandler.removeCallbacks(mRunShow);
				mHandler.postDelayed(mRunShow, mShowTime);
			}
		});
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		ViewChannelInfo.isNeedShow = false;
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
		
		mmChannelListView.listview.setIsChangeState(false);
		mmChannelListView.listview.setSelector(R.drawable.setting_picture_sel);
		mmChannelListView.channel_Type_Layout.setBackgroundResource(R.drawable.translucent_background);
		mmChannelListView.listview.requestFocus();
		mmChannelListView.initMenu(mListState);
		this.setShowTv(false);
		super.show();
	}
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mRunShow);
//		mmChannelListView.listview.changeToCurChooseItemDtv(mmChannelListView.listview.getSelectedItemPosition());
		ViewChannelInfo.isNeedShow = true;
		super.dismiss();
	}
	public void update(){
		mmChannelListView.updateCurViews();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v(TAG,"LL MenuChannelList>>onKeyDown>>keyCode = " + keyCode);
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:	
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_DOWN:
			mmChannelListView.listview.requestFocus();
			break;	
		case KeyEvent.KEYCODE_SOURCE:
//		case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBAR:
		case 4126://KEYCODE_CHANGHONGIR_TOOLBAR
		case 170://KEYCODE_CHANGHONGIR_TV
		case 4135:
			setShowTv(true);
			dismiss();
			break;
			
		case KeyEvent.KEYCODE_MENU:
			setShowTv(false);
			dismiss();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void setShowTv(boolean isShowTv) {
		this.isShowTv = isShowTv;
	}

	public boolean isShowTv() {
		return isShowTv;
	}
	
	
}