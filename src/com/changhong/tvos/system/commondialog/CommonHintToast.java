package com.changhong.tvos.system.commondialog;

import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**设计原理：在Toast显示消失之前，再次调用Toast.show()进行接力。*/
public class CommonHintToast {

	private static String TAG = "CommonHintToast";
	private Context mContext;
	private Toast mToast = null;
	private TextView mMessageTextView;
	private String mMessageText="";
	private LinearLayout mLayout;
	private int mDuration=5;
	private int showCount = 1;
	private Handler mHandler = null;
	private Runnable mToastThread = new Runnable() {
		public void run() {
			// 递增的count明显地表明是不断运行新的Toast.show()的结果
			if (mToast != null) {
				if (mMessageTextView != null) {
					// mToast.setText(String.valueOf(showCount++) + "CustomToast");
					Log.i(TAG, "setText "+showCount+++"times"+"	text="+getMessage());
					mMessageTextView.setText(getMessage());
				}
			} else {
				//	toast = Toast.makeText(context, "这是Toast默认用法", Toast.LENGTH_LONG); 默认Toast
				Log.i(TAG, "mToast is null recreat view");
				initView();
			}
			
			Log.i(TAG, "Toast show at" + System.currentTimeMillis());
			mToast.show();
			// 默认Toast设置为3.3s重新刷新一遍；自定义Toast设置为2s重新刷新一遍
			mHandler.postDelayed(mToastThread, 1800);
		}
	};

	
	/**
	 * 构造方法
	 * @param context
	 */
	public CommonHintToast(Context context) {
		this.mContext = context;
		
		initView();
		
		mHandler = new Handler(this.mContext.getMainLooper());
	}
	
	/**
	 * 初始化视图
	 */
	public void initView() {
		mToast = new Toast(mContext);
		LayoutInflater toastLayout = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View toastView = toastLayout.inflate(R.layout.vch_common_toast_dialog, null);
		mLayout = (LinearLayout) toastView.findViewById(R.id.vch_common_toast_layout);
		mLayout.setBackgroundResource(R.drawable.epg_prompt_bg);
		mMessageTextView = (TextView) (toastView.findViewById(R.id.vch_common_toast_hintmessage));
		mMessageTextView.setText(getMessage());
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setView(toastView);
	}
	
	/**
	 * 自定义Toast显示
	 * @param length
	 */
	public void show() {
		mHandler.post(mToastThread);
		Log.i(TAG, "Handler post at:" + System.currentTimeMillis());
		Thread timeThread = new Thread() {
			public void run() {
				Log.i(TAG, "TimeThread start at:" + System.currentTimeMillis());
				try {
					Thread.sleep(mDuration * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				CommonHintToast.this.cancel();
				Log.i(TAG, "Toast canceled at:" + System.currentTimeMillis());
			}
		};
		timeThread.start();
	}

	
	/**
	 * 设置显示时间
	 * @param duration
	 */
	public void setDuration(int duration) {
		// TODO Auto-generated method stub
		this.mDuration=duration;
	}
	
	/**
	 * 设置显示信息
	 * @param message
	 */
	//设置String类型的消息
	public void setMessage(String message) {
		this.mMessageText = message;
		if (mMessageTextView != null) {
			mMessageTextView.setText(message);
		}
	}
	
	//设置int类型的消息
	public void setMessage(int mesId) {
		if(mesId > 0 && mContext != null)
			setMessage(mContext.getString(mesId));
		else
			setMessage("");
	}
	
	//取得消息
	public String getMessage() {
		return mMessageText;
	}
	
	//设置消息的大小
	public void setMessageSize(int size) {
		if(mMessageTextView != null)
			mMessageTextView.setTextSize(size);
	}
	
	//设置消息的颜色
	public void setMessageColor(int color) {
		if(mMessageTextView != null)
			mMessageTextView.setTextColor(color);
	}
	
	/**
	 * 设置图片背景
	 * @param backgroundResource
	 */
	public void setBackground(int backgroundResource){
		if (mLayout != null) {
			mLayout.setBackgroundResource(backgroundResource);
		}
	}
	
	/**
	 * 设置显示位置
	 * @param gravity		显示相对位置
	 * @param xOffset		设置x坐标偏移
	 * @param yOffset		设置y坐标偏移
	 */
	public void setGravity(int gravity, int xOffset, int yOffset){
		if (mToast != null) {
			mToast.setGravity(gravity, xOffset, yOffset);
		}
	}
	
	
	/**
	 * 取消显示Toast
	 */
	public void cancel() {
		// 删除Handler队列中的仍处理等待的消息元素删除
		if (mHandler != null && mToastThread != null) {
			mHandler.removeCallbacks(mToastThread);
		}
		// 撤掉仍在显示的Toast  
		if (mToast != null) {
			mToast.cancel();
		}
	}
}