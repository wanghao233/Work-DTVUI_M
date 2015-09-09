package com.changhong.tvos.dtv.util;

import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewPromptInfo extends RelativeLayout{
	protected static final String TAG = "ViewPromptInfo";
	RelativeLayout mBgLayout =null;
	TextView mInfoText =null;
	String mPromptInfo =null;
	Handler mHandler =null;
	private static final int DELAY_MESSAGE = 0;
	private static final int IMMEDIATE_MESSAGE = 1;
	
	public ViewPromptInfo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public ViewPromptInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	private void init(Context context){
		mBgLayout =(RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_prompt_info, null);
		mInfoText =(TextView)mBgLayout.findViewById(R.id.PromptInfo);
		mInfoText.getPaint().setFakeBoldText(true);
		this.addView(mBgLayout);
		mHandler =new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Log.i(TAG, "LL handleMessage>>msg:" + msg.what);

				switch (msg.what) {
				case ViewPromptInfo.DELAY_MESSAGE:
					if((ViewPromptInfo.this.mPromptInfo != null)&&(ViewPromptInfo.this.mPromptInfo.trim().length()>0)&&(!ViewPromptInfo.this.mPromptInfo.trim().equals("null"))){
						Log.i(TAG,"LL Set PromptBgLayout To Be Visible In handleMessage(DELAY_MESSAGE)>>mPromptInfo = " + ViewPromptInfo.this.mPromptInfo);
						mBgLayout.setVisibility(View.VISIBLE);
					}
					break;
				case ViewPromptInfo.IMMEDIATE_MESSAGE:
					if((ViewPromptInfo.this.mPromptInfo != null)&&(ViewPromptInfo.this.mPromptInfo.trim().length()>0)&&(!ViewPromptInfo.this.mPromptInfo.trim().equals("null"))){
						Log.i(TAG,"LL Set PromptBgLayout To Be Visible In handleMessage(IMMEDIATE_MESSAGE)>>mPromptInfo = " + ViewPromptInfo.this.mPromptInfo);
						mHandler.removeMessages(ViewPromptInfo.DELAY_MESSAGE);
						mBgLayout.setVisibility(View.VISIBLE);
					}
					break;
				default:
					break;
				}
			};
		};
		Log.i(TAG,"LL Set PromptBgLayout To Be Gone In init()");
		mBgLayout.setVisibility(View.GONE);
	}

	public void setPromptInfo(String promptInfo){
		mPromptInfo =promptInfo;
		if(mPromptInfo ==null){
			Log.i(TAG,"LL Set PromptBgLayout To Be Gone In setPromptInfo()");
			mBgLayout.setVisibility(View.GONE);
		}else{
			mInfoText.setText(mPromptInfo);
			Log.i(TAG,"LL Set PromptBgLayout To Be Visible In setPromptInfo()>>hasMessage = " + mHandler.hasMessages(ViewPromptInfo.DELAY_MESSAGE) + ",mPromptInfo = " + mPromptInfo);
			if(!mHandler.hasMessages(ViewPromptInfo.DELAY_MESSAGE)){		
				mHandler.sendEmptyMessage(ViewPromptInfo.IMMEDIATE_MESSAGE);
			}
		}
	}
	
	public void updatePromptInfo(String promptInfo) {
		mPromptInfo = promptInfo;
		if (mPromptInfo != null) {
			mInfoText.setText(mPromptInfo);
		}
		mHandler.removeMessages(ViewPromptInfo.DELAY_MESSAGE);
		Log.i(TAG, "LL Set PromptBgLayout To Be Gone In updatePromptInfo()>>mPromptInfo = " + mPromptInfo);
		mBgLayout.setVisibility(View.GONE);
	}
	
	public void hideDelay(int times){
		mHandler.removeMessages(ViewPromptInfo.DELAY_MESSAGE);
		mBgLayout.setVisibility(View.GONE);
		mHandler.sendEmptyMessageDelayed(ViewPromptInfo.DELAY_MESSAGE, times);
		Log.i(TAG, "LL Set PromptBgLayout To Be ViSible Delayed In hideDelay()>>hasMessage = " + mHandler.hasMessages(ViewPromptInfo.DELAY_MESSAGE));
	}
	
	public void show(){
//		java.util.Map<Thread, StackTraceElement[]> ts = Thread.getAllStackTraces();  
//		StackTraceElement[] ste = ts.get(Thread.currentThread());  
//		for (StackTraceElement s : ste) {  
//			Log.e(TAG, s.toString()); //这个是android自带的，如果没有，用其他的打印函数一样   
//		}
		Log.i(TAG,"LL Set PromptBgLayout To Be Visible In show()");
		mHandler.sendEmptyMessage(ViewPromptInfo.IMMEDIATE_MESSAGE);
	}
	
	public void hide(){
		Log.i(TAG,"LL Set PromptBgLayout To Be Gone In hide()");
		mHandler.removeMessages(ViewPromptInfo.DELAY_MESSAGE);
		mBgLayout.setVisibility(View.GONE);
	}
}