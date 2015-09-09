package com.changhong.tvos.dtv.util;

import com.changhong.tvos.dtv.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewActionInfo extends LinearLayout{
	private static final String TAG = "ViewActionInfo";
	private OnPlayScheduleListener mOnPlayScheduleListener = null;
	private Context mContext = null;
	private LinearLayout mBgLayout;
	private Handler mHandler =null;
	private Runnable mShowRun =null;
	private int mShowTime =1000*60;
	private TextView mTextTitle,mTextTitle2,mTextMessage;
	private Button mButtonOK,mButtonCancel;
	private View.OnKeyListener mOnKeyListenerOK = new View.OnKeyListener() {
		
		@Override
		public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
			// TODO Auto-generated method stub
			if(arg2.getAction() != KeyEvent.ACTION_DOWN){
				
				return false;
			}
			Log.i(TAG,"LL ViewActionInfo>>mOnKeyListenerOK>>onKey***");
			switch (arg1) {
			case KeyEvent.KEYCODE_BACK:
				hide();
				break;
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_ENTER:
				hide();
				mOnPlayScheduleListener.onPlaySchedule();
				break;
			default:
				startTimeTask();
				break;
			}
			return false;
		}
	};
	
	private View.OnKeyListener mOnKeyListenerCancel = new View.OnKeyListener() {
		
		@Override
		public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
			// TODO Auto-generated method stub
			if(arg2.getAction() != KeyEvent.ACTION_DOWN){
				
				return false;
			}
			Log.i(TAG,"LL ViewActionInfo>>mOnKeyListenerCancel>>onKey***");
			switch (arg1) {
			case KeyEvent.KEYCODE_BACK:
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_ENTER:
				hide();
				break;

			default:
				startTimeTask();
				break;
			}
			return false;
		}
	};

	public ViewActionInfo(Context context) {
		super(context);
		this.mContext =context;
		this.init();
	}

	public ViewActionInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext =context;
		this.init();
	}

	public ViewActionInfo(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext =context;
		this.init();
	}
	private void init(){
		mBgLayout =(LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.view_action_info, null);
		mTextTitle =(TextView)mBgLayout.findViewById(R.id.view_action_info_title);
		mTextTitle2 =(TextView)mBgLayout.findViewById(R.id.view_action_info_title_2);
		mTextMessage =(TextView)mBgLayout.findViewById(R.id.view_action_info_hintmessage);
		mButtonOK =(Button)mBgLayout.findViewById(R.id.view_action_info_btn_ok);
		mButtonCancel =(Button)mBgLayout.findViewById(R.id.view_action_info_btn_cancel);
		this.addView(mBgLayout);
		mButtonOK.requestFocus();
		mButtonOK.setFocusable(true);
		mButtonOK.setFocusableInTouchMode(true);
		mHandler =new Handler();
		mShowRun =new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				mOnPlayScheduleListener.onPlaySchedule();
				hide();
			}
		};
		mButtonOK.setOnKeyListener(mOnKeyListenerOK);
		mButtonCancel.setOnKeyListener(mOnKeyListenerCancel);
		hide();
	}
	public void setDisplayInfo(String title,String title2,String message,String buttonOK,String buttonCancel){
		mTextTitle.setText(title);
		mTextTitle2.setText(title2);
		mTextMessage.setText(message);
		mButtonOK.setText(buttonOK);
		mButtonCancel.setText(buttonCancel);
		show();
	}
	private void startTimeTask(){
		mHandler.removeCallbacks(mShowRun);
		mHandler.postDelayed(mShowRun, mShowTime);
	}
	private void show(){
		setVisibility(View.VISIBLE);
		startTimeTask();
	}
	private void hide(){
		mHandler.removeCallbacks(mShowRun);
		setVisibility(View.GONE);
	}

	public void setOnPlayScheduleListener(OnPlayScheduleListener arg0){
		mOnPlayScheduleListener = arg0;
	}
	public interface OnPlayScheduleListener{
		public void onPlaySchedule();
	} 
}
