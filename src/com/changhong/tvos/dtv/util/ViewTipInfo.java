package com.changhong.tvos.dtv.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ViewTipInfo extends View {
	public static final int SOFTWARE_INFORMATION = 0;
	private static final String TAG = "ViewTipInfo";
	private String titleStr, tip1, tip2, tip3, tip4,tip5;
	private Paint titlepaint, tippaint;
	private int tiptype = SOFTWARE_INFORMATION;//0:software information

	public ViewTipInfo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initPaint();
	}

	public ViewTipInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initPaint();
	}

	private void initPaint() {
		Log.i(TAG,"LL ViewTipInfo>>initPaint()***");
		titlepaint = new Paint();
		titlepaint.setTextSize(30);
		titlepaint.setColor(Color.WHITE);
		titlepaint.setAntiAlias(true);
		titlepaint.setTextAlign(Align.CENTER);

		tippaint = new Paint();
		tippaint.setTextSize(20);
		tippaint.setColor(Color.WHITE);
		tippaint.setAntiAlias(true);
	}

	public void SetSoftWareInfoStr(String title, String tip1, String tip2,
			String tip3,String tip4 ,String tip5) {
		Log.i(TAG,"LL ViewTipInfo>>SetSoftWareInfoStr>>title = " + title);
		this.titleStr = title;
		this.tip1 = tip1;
		this.tip2 = tip2;
		this.tip3 = tip3;
		this.tip4 = tip4;
		this.tip5 = tip5;
	}
	
	public void SetTipType(int type) {
		Log.i(TAG,"LL ViewTipInfo>>SetTipType>>type = " + type);
		this.tiptype = type;
	}

	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Log.i(TAG,"LL ViewTipInfo>>onDraw>>tiptype = " + tiptype);
		switch (tiptype) {
		
		case SOFTWARE_INFORMATION:// soft detect info

			if (titleStr != null){
				canvas.drawText(titleStr, this.getPaddingLeft() + 300,this.getPaddingTop() + 50, titlepaint);
			}
			if (tip1 != null){
				canvas.drawText(tip1, this.getPaddingLeft() + 40,this.getPaddingTop() + 120, tippaint);
			}
			if (tip2 != null){
				canvas.drawText(tip2,this.getPaddingLeft() + 40, this.getPaddingTop() + 160,tippaint);
			}
			if (tip3 != null){
				canvas.drawText(tip3,this.getPaddingLeft() + 40, this.getPaddingTop() + 200,tippaint);
			}
			if (tip4 != null){
				canvas.drawText(tip4,this.getPaddingLeft() + 40, this.getPaddingTop() + 240,tippaint);
			}
			
			break;
			default:
				break;
		
		}
	}
}
