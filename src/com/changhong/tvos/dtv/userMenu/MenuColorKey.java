package com.changhong.tvos.dtv.userMenu;

import com.changhong.tvos.dtv.DtvRoot;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;

import android.app.Activity;
import android.app.Dialog;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MenuColorKey extends Dialog {

	enum dialogType {
		audio_mode, audio_track
	};
	private static final String TAG = "MenuColorKey";
	private DtvChannelManager mChannelManager =DtvChannelManager.getInstance();
	int mShowTime = 5000;
	TextView title;
	TextView valueText;
	dialogType curType;
	String[] valueArray;
	int curIndex;
	int valCount;
	Handler mHandler;
	Runnable mRunnable;
	Context mContext; 
	//NavIntegration navImp = NavIntegration.getInstance(null);
	public MenuColorKey(Context context, int  keyCode) {
		super(context,R.style.Theme_DialogFactory);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.menu_colorkey);
		mContext =context;
		title = (TextView) findViewById(R.id.title);
		valueText = (TextView) findViewById(R.id.value);

		mHandler =new Handler();
		mRunnable =new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				if(mContext!=null&&(mContext instanceof Activity)&&(!((Activity)mContext).isFinishing())){
					Log.i(TAG,"LL mContext = " + mContext + "isFinish = " + ((Activity)mContext).isFinishing());
					dismiss();
				}
			}
		};
		mHandler.postDelayed(mRunnable, mShowTime);
		menuInit(keyCode);

		setPositon(0,(int) (250*mContext.getResources().getDisplayMetrics().scaledDensity));
	}
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mRunnable);
		super.dismiss();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("tv","LL MenuColorKey>>onKeyDown>>keyCode = " + keyCode);
		DtvRoot.handleScreenSaverMessages();
		switch (keyCode) {
		case KeyEvent.KEYCODE_YELLOW:
			if(curType ==dialogType.audio_mode){
				curIndex =(curIndex+1)%valCount;
				mChannelManager.setAudioMode(curIndex);
				valueText.setText(valueArray[curIndex]);
				saveCurValue();
				mHandler.removeCallbacks(mRunnable);
				mHandler.postDelayed(mRunnable, mShowTime);	
			}else{
				menuInit(keyCode);
			}
			break;	
		case KeyEvent.KEYCODE_GREEN:
			{
				if(curType ==dialogType.audio_track){
					curIndex =(curIndex+1)%valCount;
					valueText.setText(valueArray[curIndex]);
					if(valCount>1){
						mChannelManager.setAudioTrack(curIndex);
					 }
					saveCurValue();
					mHandler.removeCallbacks(mRunnable);
					mHandler.postDelayed(mRunnable, mShowTime);	
				}else{
					menuInit(keyCode);
				}		
			}

			break;	
		default:
			dismiss();
			break;
		}

		return false;
	}
	
	public void menuInit(int keyCode){

		switch (keyCode) {
		case KeyEvent.KEYCODE_YELLOW:{
				curType =dialogType.audio_mode;
				title.setText(R.string.menu_audio_mode);
				valueArray = mContext.getResources().getStringArray(R.array.menu_audio_mode);;
				curIndex = mChannelManager.getAudioModeSel();
				valCount = valueArray.length;
			}
			break;
		case KeyEvent.KEYCODE_GREEN:{
		        String language[] =mContext.getResources().getStringArray(R.array.menu_audio_track);
				valueArray = mChannelManager.getAudioTrack(language);
				curType =dialogType.audio_track;
				title.setText(R.string.menu_audio_track);
				curIndex = mChannelManager.getAudioTrackSelIndex();
				valCount = valueArray.length;
			}
			break;
		default:
			break;
		}
		valueText.setText(valueArray[curIndex]);
	}
	
	private void saveCurValue(){
		
	}

	public void setDurationTime(int durationTime){
		mShowTime =durationTime;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mRunnable);
		super.onStop();
	}

	public void setPositon(int xoff, int yoff) {
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.x = xoff;
		lp.y = yoff;
		window.setAttributes(lp);
		
	}
}