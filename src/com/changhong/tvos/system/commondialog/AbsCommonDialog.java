package com.changhong.tvos.system.commondialog;

import com.changhong.tvos.dtv.tvap.DtvDialogManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
/**
 * 基类对话框
 *
 * @author jinwei.yang 2011-10-27
 */
public abstract class AbsCommonDialog extends Dialog implements CommonDialogInterface {
	private static final String TAG = "AbsCommonDialog";
	protected AbsCommonDialog mDialog = this;
	protected Context mContext;

	/** 显示时长 **/
	protected int mDuration = 5;
	/** 标题与子标题 **/
	protected String mTitleText = "";
	protected TextView mTitleView;
	/** 消息 **/
	protected String mMessageText = "";
	protected TextView mMessageView;
	/** 可否取消 **/
	protected boolean mCancelable2;
	/** Handler */
	protected Handler mHandler = new Handler();
	/** TimeOutListener */
	protected CommonDialogInterface.OnTimeoutListener mTimeoutListener;

	
	
	/** 构造方法 **/
	public AbsCommonDialog(Context context) {
		this(context, 0);
	}

	public AbsCommonDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
	}

	public AbsCommonDialog(Context context, int theme, int titleId, int mesId, boolean cancelAble, int duration) {
		this(context, theme);
		if (titleId > 0)
			mTitleText = mContext.getString(titleId);
		if (mesId > 0)
			mMessageText = mContext.getString(mesId);
		mCancelable2 = cancelAble;
		mDuration = duration;
	}

	
	
	/** 计时器开始计时 **/
	protected void initListener() {
		mDialog.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() != KeyEvent.ACTION_DOWN)
					return false;
				startTimeTask();
				return false;
			}
		});
	}
	
	protected void startTimeTask() {
		cancelTimeoutRunnable();
		if (getDuration() > 0) {
			mHandler.postDelayed(mTimeoutRunnable, getDuration() * 1000L);
		}
	}
	
	private Runnable mTimeoutRunnable = new Runnable() {

		public void run() {
			// mDialog.cancel();
			if (mContext != null && (mContext instanceof Activity) && (!((Activity) mContext).isFinishing())) {
				Log.i(TAG, "LL mContext = " + mContext + "isFinish = " + ((Activity) mContext).isFinishing());
				mDialog.dismiss();
			}

			if (mTimeoutListener != null) {
				mTimeoutListener.onTimeout(mDialog);
			}
		}
	};
	
	public void setOnTimeoutListener(CommonDialogInterface.OnTimeoutListener listener) {
		mTimeoutListener = listener;
	}
	
	protected void cancelTimeoutRunnable() {
		mHandler.removeCallbacks(mTimeoutRunnable);
	}
	
	@Override
	public void cancel() {
		cancelTimeoutRunnable();
		super.cancel();
	}
	
	
	
	/** 设置cancelable **/
	@Override
	public void setOnCancelListener(OnCancelListener listener) {
		super.setOnCancelListener(listener);
	}
	@Override
	public void setCancelable(boolean flag) {
		mCancelable2 = flag;
		super.setCancelable(mCancelable2);
	}
	

	
	/** get/set方法 **/
	//--------Duration-------
	public int getDuration() {
		return mDuration;
	}
	public void setDuration(int duration) {
		mDuration = duration;
		if (mDuration < 0) {
			mDuration = 0;
		}
	}

	//--------Title-------
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

	//--------Message-------
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

	
	
	/** show/dismiss方法 **/
	@Override
	public void show() {
		initListener();
		super.show();

		startTimeTask();
		DtvDialogManager.AddShowDialog(this);
	}

	@Override
	public void dismiss() {
		cancelTimeoutRunnable();
		super.dismiss();
		DtvDialogManager.RemoveDialog(this);
	}
}