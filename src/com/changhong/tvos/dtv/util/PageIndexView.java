package com.changhong.tvos.dtv.util;

import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PageIndexView extends RelativeLayout{
	private static final String TAG  = PageIndexView.class.getSimpleName();
	private LinearLayout mDotL;
	private LinearLayout mDotM;
	private LinearLayout mDotR;
	private RelativeLayout layout;
	
	public PageIndexView(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		this.init();
	}

	public PageIndexView(Context context,AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context,attrs);
		this.init();
	}
	
	private void initDot(int curIndex,int dotCount){
		mDotL.removeAllViews();
		mDotR.removeAllViews();
		for(int i=0;i<curIndex;i++){
			RelativeLayout layout =new RelativeLayout(mContext);
			layout.setBackgroundResource(R.drawable.dot_unsel);
			mDotL.addView(layout);
		}
		for(int i=curIndex+1;i<dotCount;i++){
			RelativeLayout layout =new RelativeLayout(mContext);
			layout.setBackgroundResource(R.drawable.dot_unsel);
			mDotR.addView(layout);
		}
	}
	
	public void update(int pageIndex,int pageCount) {
		// TODO Auto-generated method stub
		initDot(pageIndex ,pageCount);
	}
	
	private void init() {
		// TODO Auto-generated method stub
		layout =(RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.view_quick_channel_page_index, null);
		this.addView(layout);
		mDotL = (LinearLayout) layout.findViewById(R.id.DotL);
		mDotM = (LinearLayout) layout.findViewById(R.id.DotM);
		mDotR = (LinearLayout) layout.findViewById(R.id.DotR);
	}
}