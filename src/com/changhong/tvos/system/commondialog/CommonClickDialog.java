package com.changhong.tvos.system.commondialog;

import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvDialogManager;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

public class CommonClickDialog extends Dialog {

	private static final String TAG = CommonClickDialog.class.getSimpleName();
//	private static final String TAG = "YangLiu";
	protected CommonClickDialog mDialog = this;
	protected Context mContext;

	private TimeCount timer = null;
	private String mTitleText = "";
	private TextView mTitleView;
	private String mMessageText = "";
	private TextView mMessageView;
	private int mDuration = 5*1000;
	private boolean mCancelable;
	private boolean isShowTV = false;
	
	/** Handler */
	
	/** 构造方法 **/
	public CommonClickDialog(Context context) {
		super(context);
		mContext = context;
		
		initView();
		
		creatTimerTask();//创建定时器
	}
	
	public CommonClickDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
		
		creatTimerTask();//创建定时器
	}

	public CommonClickDialog(Context context, int theme, int titleId, int mesId, boolean cancelAble, int duration) {
		this(context, theme);
		mContext = context;
		
		if (titleId > 0)
			mTitleText = mContext.getString(titleId);
		if (mesId > 0)
			mMessageText = mContext.getString(mesId);
		mCancelable = cancelAble;
		mDuration = duration;
		
		creatTimerTask();//创建定时器
	}
	
	private void initView(){
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.common_click_dialog);
		mMessageView = (TextView) mDialog.findViewById(R.id.commonClick_dialog_message);
	}
	
	
	/** 设置Title **/
	@Override
	public void setTitle(CharSequence title) {
		mTitleText = title.toString();
		if (mTitleView != null) {
			mTitleView.setText(mTitleText);
		}
	}
	@Override
	public void setTitle(int titleId) {
		if (titleId > 0)
			setTitle(mContext.getText(titleId));
		else
			setTitle("");
	}
	public void setTitle(String titleString) {
		mTitleText = titleString;
		if (mTitleView!=null) {
			mTitleView.setText(mTitleText);
		}
	}
	
	/** 设置message **/
	public void setMessage(CharSequence message) {
		mMessageText = message.toString();
		if (mMessageView != null) {
			mMessageView.setText(mMessageText);
		}
	}
	public void setMessage(int messageId) {
		if (messageId > 0)
			setMessage(mContext.getText(messageId));
		else
			setMessage("");
	}
	public void setMessageText(String messageText) {
		mMessageText = messageText;
		if (mMessageView!=null) {
			mMessageView.setText(mMessageText);
		}
	}
	public void setMessageTextSize(int messageSize) {
		if (mMessageView!=null) {
			mMessageView.setTextSize(messageSize);
		}
	}

	/** 设置显示时长 **/
	public int getDuration() {
		return mDuration;
	}
	public void setDuration(int duration) {
		this.mDuration = duration*1000;
		if (this.mDuration<0) {
			this.mDuration = 0;
		}
	}
		
	/** 设置cancelAble **/
	@Override
	public void setOnCancelListener(OnCancelListener listener) {
		super.setOnCancelListener(listener);
	}
	@Override
	public void setCancelable(boolean flag) {
		mCancelable = flag;
		super.setCancelable(mCancelable);
	}
	
	
	/** 监听按键事件 **/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG, "LL enter ScanWarningDialog onKeyDown>>keyCode = " + keyCode);
		switch (event.getScanCode()) {
		case 231:// keyboard Menu
			this.dismiss();
			return true;
		case 233:// keyboard Channel Up
		case 234:// keyboard Channel Down
			break;
		case 232:// keyboard Source
		case 235:// keyboard Volume Down
		case 236:// keyboard Volume Up
			return false;
		default:
			break;
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			Log.i(TAG, "YL enter KEYCODE_DPAD_CENTER or KEYCODE_ENTER，dismiss hint dialog");
			mDialog.dismiss();
			break;
//			return true;
		case KeyEvent.KEYCODE_SOURCE:
			// case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBAR:
		case 4126:// KEYCODE_CHANGHONGIR_TOOLBAR
		case 170:// KEYCODE_CHANGHONGIR_TV
		case 4135:
			setShowTV(true);
			mDialog.dismiss();
			break;
		case KeyEvent.KEYCODE_BACK:
			Log.i(TAG, "YL enter KEYCODE_BACK，dismiss hint dialog");
			mDialog.dismiss();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	
	/** 设置TV的显示 **/
	public void setShowTV(boolean isShowTV) {
		this.isShowTV = isShowTV;
	}
	public boolean isShowTV() {
		return isShowTV;
	}
	
	/** 创建timer定时器 **/
	private void creatTimerTask(){
		if (getDuration()>0) {
			if (timer == null) {
				timer = new TimeCount(getDuration(), 1000);
			}
		}
	}
	
	
	/**  Dialog的show/dismiss方法 **/
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		
		if (timer != null) {
			timer.start();
		}else {
			creatTimerTask();
			timer.start();
		}
		DtvDialogManager.AddShowDialog(this);
	}
	
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		
		if (timer!=null) {
			timer.cancel();
		}
		DtvDialogManager.RemoveDialog(this);
	}
	
	
	
	/**
	 * 定时器，到一定时间消失弹出框
	 * @author YangLiu
	 *
	 */
	public class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			mDialog.dismiss();
			Log.i(TAG, "TimeCount-----timeUp----Dialog dismissed");
			//定时器到点后执行
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			//在此显示定时时间的更新
		}
	}
}