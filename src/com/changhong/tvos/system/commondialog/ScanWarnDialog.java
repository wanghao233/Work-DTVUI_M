package com.changhong.tvos.system.commondialog;

import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvDialogManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ScanWarnDialog extends Dialog {
	private static final String TAG = "ScanWarnDialog";
	public static final int FOCUS_BUTTON_YES = 0;
	public static final int FOCUS_BUTTON_NO = 1;
	private LinearLayout mLinearLayout;
	private TextView mTitleTextView;
	private TextView mContentTextView;
	private Button mYesButton;
	private Button mNoButton;
	private Context mContext;
	private int mDefaultFocusButton = FOCUS_BUTTON_NO;
	private Handler mHandler = new Handler();
	private int mDuration = 5;
	private boolean isShowTV = false;
	private View.OnClickListener mListenerYes = new View.OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			ScanWarnDialog.this.dismiss();
		}
	};
	private View.OnClickListener mListenerNo = new View.OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.i(TAG,"LL Start Respond Click the ButtonNo***");
			ScanWarnDialog.this.dismiss();
			Log.i(TAG,"LL End Respond Click the ButtonNo***");
		}
	};

	public ScanWarnDialog(Context context) {
		super(context,R.style.Theme_ScanWarnDialog);
		mContext = context;
		setContentView(R.layout.scan_warn_dialog);
		mLinearLayout = (LinearLayout)findViewById(R.id.scan_warn_dialog);
		mTitleTextView = (TextView)findViewById(R.id.scan_warn_dialog_title);
		Log.i(TAG,"LL mTitleTextView = " + mTitleTextView);
		mContentTextView = (TextView)findViewById(R.id.scan_warn_dialog_text);
		mYesButton = (Button)findViewById(R.id.scan_warn_dialog_buttonyes);
		mNoButton = (Button)findViewById(R.id.scan_warn_dialog_buttonno);
		mYesButton.requestFocus();
		mYesButton.setSelected(true);
		setWindowPosition();
		setWindowType(2003);//Ìá¸ßDialogÏÔÊ¾²ã¼¶£¬ºÍ¹ùèª¿Ø¼þµÄÏÔÊ¾²ã¼¶Ò»ÑùÁË
		Log.i(TAG,"LL mListenerNo = " + mListenerNo);
		mYesButton.setOnClickListener(mListenerYes);
		mNoButton.setOnClickListener(mListenerNo);
	}

	public ScanWarnDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
		setContentView(R.layout.scan_warn_dialog);
		mLinearLayout = (LinearLayout)findViewById(R.id.scan_warn_dialog);
		mTitleTextView = (TextView)findViewById(R.id.scan_warn_dialog_title);
		Log.i(TAG,"LL mTitleTextView = " + mTitleTextView);
		mContentTextView = (TextView)findViewById(R.id.scan_warn_dialog_text);
		mYesButton = (Button)findViewById(R.id.scan_warn_dialog_buttonyes);
		mNoButton = (Button)findViewById(R.id.scan_warn_dialog_buttonno);
		mYesButton.requestFocus();
		mYesButton.setSelected(true);
		setWindowPosition();
		setWindowType(2003);//Ìá¸ßDialogÏÔÊ¾²ã¼¶£¬ºÍ¹ùèª¿Ø¼þµÄÏÔÊ¾²ã¼¶Ò»ÑùÁË
		Log.i(TAG,"LL mListenerNo = " + mListenerNo);
		mYesButton.setOnClickListener(mListenerYes);
		mNoButton.setOnClickListener(mListenerNo);
	}
	
	public void setDisplayString(String title,String content,int textColor,int background){
		mTitleTextView.setText(title);
		mTitleTextView.setTextColor(textColor);
		mContentTextView.setText(content);
		mContentTextView.setTextColor(textColor);
		mYesButton.setTextColor(textColor);
		mNoButton.setTextColor(textColor);
		mLinearLayout.setBackgroundDrawable(mContext.getResources().getDrawable(background));
	}
	
	public void setDisplayString(String title,String content){
		mTitleTextView.setText(title);
		mContentTextView.setText(content);
	}
	
	public TextView getmTitleTextView() {
		return mTitleTextView;
	}

	public TextView getmContentTextView() {
		return mContentTextView;
	}

	public Button getmYesButton() {
		return mYesButton;
	}

	public Button getmNoButton() {
		return mNoButton;
	}

	public int getDuration()
	{
		return mDuration;
	}
	public void setDuration(int duration)
	{
		mDuration = duration;
		if(mDuration < 0)
		{
			mDuration = 0;
		}
	}
	public int getDefaultFocusButton() {
		return mDefaultFocusButton;
	}

	public void setDefaultFocusButton(int mDefaultFocusButton) {
		this.mDefaultFocusButton = mDefaultFocusButton;
	}
	
	
	private void setWindowPosition() {
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
//		lp.x += -230;
//		lp.y += -170;
		lp.x += 0;
		lp.y += 0;
		window.setAttributes(lp);
	}

	private void setWindowType(int type) {
		Window window = getWindow();
		window.setType(type);
	}

	public void setDialogMeasurement(int width, int height,int x,int y) {
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = width;
		lp.height = height;
		lp.x = x;
		lp.y = y;
		window.setAttributes(lp);
		LayoutParams layoutParams=new LayoutParams(width, height);
		mLinearLayout.setLayoutParams(layoutParams);
	}

	public void setWindowPosition(int x, int y) {
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.x = x;
		lp.y = y;
		window.setAttributes(lp);
	}

	private void initListener()
	{
		this.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() != KeyEvent.ACTION_DOWN){
					
					return false;
				}
				startTimeTask();
				return false;
			}
		});
	}
	private Runnable mTimeoutRunnable = new Runnable() {
		
		public void run() {
			if(mContext!=null&&(mContext instanceof Activity)&&(!((Activity)mContext).isFinishing())){
				Log.i(TAG,"LL mContext = " + mContext + "isFinish = " + ((Activity)mContext).isFinishing());
				ScanWarnDialog.this.dismiss();
			}
		}
	};
	private void startTimeTask() {
		cancelTimeoutRunnable();
		if(getDuration() > 0)
		{
			mHandler.postDelayed(mTimeoutRunnable, getDuration() * 1000L);
		}
	}
	private void cancelTimeoutRunnable()
	{
		mHandler.removeCallbacks(mTimeoutRunnable);
	}
	private void setFocusButton()
	{
		Button btn = mYesButton;
		if(mDefaultFocusButton == FOCUS_BUTTON_NO)
		{
			btn = mNoButton;
		}
		btn.setFocusable(true);
		btn.setFocusableInTouchMode(true);
		btn.requestFocus();
	}
	public void show() {
		this.initListener();
		super.show();
		this.setFocusButton();
		this.startTimeTask();
		DtvDialogManager.AddShowDialog(this);
	}
	@Override
	public void dismiss() {
		cancelTimeoutRunnable();
		super.dismiss();
		DtvDialogManager.RemoveDialog(this);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG,"LL enter ScanWarningDialog onKeyDown>>keyCode = " + keyCode);
		switch (event.getScanCode()) {
		case 231://keyboard Menu
			this.dismiss();
			return true;
		case 233://keyboard Channel Up
		case 234://keyboard Channel Down
			break;
		case 232://keyboard Source 
			if(mYesButton.isFocused()){
				mYesButton.callOnClick();
			}else{
				
				mNoButton.callOnClick();
			}
			return true;
			
		case 235://keyboard Volume Down
			if(mYesButton.isFocused()){
				mNoButton.requestFocus();
			}else{
				
				mYesButton.requestFocus();
			}
			return true;
		case 236://keyboard Volume Up
			if(mNoButton.isFocused()){
				mYesButton.requestFocus();
			}else{
				
				mNoButton.requestFocus();
			}
			return true;
		default:
			break;
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if(mYesButton.isFocused()){
				mYesButton.callOnClick();
			}else{
				
				mNoButton.callOnClick();
			}
			return true;
		case KeyEvent.KEYCODE_SOURCE:
//			case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBAR:
			case 4126://KEYCODE_CHANGHONGIR_TOOLBAR
			case 170://KEYCODE_CHANGHONGIR_TV
			case 4135:
				setShowTV(true);
				dismiss();
				break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void setShowTV(boolean isShowTV) {
		this.isShowTV = isShowTV;
	}
	public boolean isShowTV() {
		return isShowTV;
	}
}