package com.changhong.menuView;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public abstract class TimeManager {
	String TAG = "TimeManager";

	private Context mContext;
	private int mShowTime;
	private Handler mHandler = new Handler();
	private Runnable mRunnable = new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			onTimer();
		}
	};

	public TimeManager(Context context, int showTime) {
		mContext = context;
		mShowTime = showTime;
	}

	public void start() {
		mHandler.removeCallbacks(mRunnable);
		if (mShowTime >= 0) {
			mHandler.postDelayed(mRunnable, mShowTime);
		}
	}

	public void reStart() {
		start();
	}

	public void pause() {
		Log.v(TAG, "pause");
		mHandler.removeCallbacks(mRunnable);
	}

	public void setShowTime(int showTime) {
		// TODO Auto-generated method stub
		pause();
		mShowTime = showTime;
		start();
	}

	public abstract void onTimer();

	public void end() {
		// TODO Auto-generated method stub
		pause();
		onTimer();
	}
}