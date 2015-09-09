package com.changhong.tvos.dtv.util;

import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

public class ViewBreathingLight extends RelativeLayout {
	protected static final String TAG = ViewBreathingLight.class.getSimpleName();
	Paint mPaint = null;
	int currIndex = 0;// 当前播放图片的ID
	int[] bitmapId;// 图片编号ID
	Handler mHandler = null;
	Runnable mCycleRun = null;

	public ViewBreathingLight(Context context) {
		super(context);
		init(context);
	}

	public ViewBreathingLight(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		bitmapId = new int[] { R.drawable.channel_edit_swap_item_select_1,
				R.drawable.channel_edit_swap_item_select_2,
				R.drawable.channel_edit_swap_item_select_3 };
		mHandler = new Handler();
		mCycleRun = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				currIndex = (currIndex + 1) % bitmapId.length;// 更改图片的ID
				Log.i(TAG,"LL mCycleRun>>currIndex = " + currIndex);
				ViewBreathingLight.this.setBackgroundResource(bitmapId[currIndex]);
				mHandler.postDelayed(mCycleRun, 100);
			}
		};
	}

	private void reset() {
		currIndex = 0;
	}

	public void startFlicker() {
		reset();
		mHandler.removeCallbacks(mCycleRun);
		mHandler.post(mCycleRun);
	}

	public void stopFlicker() {
		mHandler.removeCallbacks(mCycleRun);
	}

	public void setLocation(int[] location){
		this.setX(Float.valueOf(location[0]));
		this.setY(Float.valueOf(location[1]));
	}
}
