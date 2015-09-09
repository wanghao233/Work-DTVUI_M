package com.changhong.tvos.dtv.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public  class ViewScrollText extends TextView{
	public final static String TAG = ViewScrollText.class.getSimpleName();

	private float textLength = 0f;
	private float viewWidth = 1280f;
	private float step = 0f;
	private float y = 0f;
	private float temp_view_plus_text_length = 0.0f;
	public boolean isStarting = false;
	private Paint paint = null;
	private String text = "";
	private Context mContext;
	private int scrollTimes =1;
	public ViewScrollText(Context context) {
		super(context);
		this.mContext =context;
		this.viewWidth *= mContext.getResources().getDisplayMetrics().scaledDensity;
	}

	public ViewScrollText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext =context;
		this.viewWidth *= mContext.getResources().getDisplayMetrics().scaledDensity;
	}

	public ViewScrollText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext =context;
		this.viewWidth *= mContext.getResources().getDisplayMetrics().scaledDensity;
	}



	public void init() {
		paint = getPaint();
		paint.setColor(Color.WHITE);
		text = getText().toString();
		textLength = paint.measureText(text);
		step = 0;
		temp_view_plus_text_length = viewWidth + textLength;
		y = getTextSize() + getPaddingTop();
	}

	public void startScroll() {
		isStarting = true;
		if(scrollTimes >0){
			scrollTimes--;
		}
		init();
		invalidate();
	}

	public void stopScroll() {
		isStarting = false;
		invalidate();
	}
	public boolean isStopScroll(){
		return !isStarting;
	}
	@Override
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}
	public void setScrollTimes(int times) {
		scrollTimes =times;
	}
	public void onDraw(Canvas canvas) {
		if(canvas!=null&&paint!=null)
		{
			canvas.drawText(text, viewWidth - step, y, paint);
		
			if (!isStarting) {
				return; 
			}
			step += 2;
	
			if (step < temp_view_plus_text_length) {
				invalidate();
			}else if(scrollTimes ==0){
				isStarting = false;
			}else if(scrollTimes ==-1){
				startScroll();
			}else if(scrollTimes >0){
				startScroll();
			}
		}else{
			super.onDraw(canvas);
		}

	}

}
