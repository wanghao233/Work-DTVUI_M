package com.changhong.tvos.dtv.util;

import com.changhong.tvos.dtv.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewPageItem extends RelativeLayout{
	RelativeLayout layout;
	RelativeLayout layoutTop;
	RelativeLayout top1;
	RelativeLayout top2;
	LinearLayout layoutPageNum;
	LinearLayout layoutGray;
	TextView curNumText;
	TextView pageCountText;
	int pageCount =1;
	int curPageNum =1;
	boolean showPageNum =true;
	boolean show =true;
	LayoutParams param;
	android.widget.LinearLayout.LayoutParams layoutBlueParam;
	public ViewPageItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		menuInit(context);
		
	}
	public ViewPageItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		menuInit(context);
	}
	public void setCurPageNum(int num){
		if(num<=0){
			return;
		}
		curPageNum =num;
		if(showPageNum){
		    layoutBlueParam.height =(getHeight()/pageCount)*(curPageNum-1);
		}else{
			layoutBlueParam.height =(getHeight()/pageCount)*(curPageNum);
		}

		Log.v("pageItem","pageItem getLayoutParams().height ="+getLayoutParams().height);
		Log.v("pageItem","pageItem getHeight() ="+getHeight());
		Log.v("pageItem","pageItem layoutBlueParam.height ="+layoutBlueParam.height);
		Log.v("pageItem","pageItem pageCount ="+pageCount);
		Log.v("pageItem","pageItem curPageNum ="+curPageNum);
		if (showPageNum){
			if(layoutBlueParam.height>layoutPageNum.getHeight()){
				layoutBlueParam.height =layoutBlueParam.height -layoutPageNum.getHeight();
			}else{
				layoutBlueParam.height =0;
			}
		}
		layoutTop.setLayoutParams(layoutBlueParam);
		curNumText.setText(Integer.toString(curPageNum));
		
	}
	public void initData(int pageCount,boolean showPageNum){
		if(pageCount >0){
			this.pageCount =pageCount;
		}
		this.showPageNum =showPageNum;
		pageCountText.setText(""+this.pageCount);
		setCurPageNum(curPageNum);
		if (showPageNum) {
			layoutPageNum.setVisibility(View.VISIBLE);
		} else {
			layoutPageNum.setVisibility(View.GONE);
		}			
	}
	public void initData(int pageCount){
		initData(pageCount,showPageNum);
	}	
	public void initData(int pageCount ,int curPageNum ){
		initData(pageCount,showPageNum);	
		setCurPageNum(curPageNum);
	}
	public void showPageItem(boolean show) {
	if (show) {
		setVisibility(View.VISIBLE);
	} else {
		setVisibility(View.INVISIBLE);
	}
}	
	private void menuInit(Context context){
		layout =(RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_page_item, null);
		layoutTop =(RelativeLayout) layout.findViewById(R.id.blueBar);
		top1 =(RelativeLayout) layout.findViewById(R.id.blue1);
		top2 =(RelativeLayout) layout.findViewById(R.id.blue2);
		layoutPageNum =(LinearLayout) layout.findViewById(R.id.pageNum);
		curNumText = (TextView) layout.findViewById(R.id.curPageNum);
		pageCountText = (TextView) layout.findViewById(R.id.pageCount);
		layoutBlueParam = (android.widget.LinearLayout.LayoutParams) layoutTop.getLayoutParams();
		layoutGray = (LinearLayout) layout.findViewById(R.id.gray);
		//layout.findViewById(R.id.gray).setVisibility(View.INVISIBLE);
		
		this.addView(layout);
	}
	public void setTopStyle(int resid){
		layoutTop.setBackgroundResource(resid);
		top1.setBackgroundResource(0);
		top2.setBackgroundResource(0);
	}
	public void setGrayStyle(int resid){
		layoutGray.setBackgroundResource(resid);
	}
	}