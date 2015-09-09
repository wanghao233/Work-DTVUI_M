package com.changhong.tvos.dtv.util;

import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewBootTextInfo extends RelativeLayout {
	protected static final String TAG = "ViewBootInfo";
	RelativeLayout mBgLayout =null;
	TextView mInfoText =null;
	String mPromptInfo =null;
	
	public ViewBootTextInfo(Context context,Rect rect) {
		super(context);
		// TODO Auto-generated constructor stub
		this.init(context, rect);
	}
	public ViewBootTextInfo(Context context,AttributeSet attrs,Rect rect){
		super(context, attrs);
		this.init(context, rect);
	}
	
	public ViewBootTextInfo(Context context,AttributeSet attrs,int defStyle,Rect rect){
		super(context, attrs, defStyle);
		this.init(context, rect);
	}
	
	private void init(Context context,Rect rect){
		LayoutParams params = null;
	    params =new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    if(null != rect){
	    	params.topMargin = rect.top;
		    params.leftMargin = rect.left;
		    params.rightMargin = rect.right;
		    params.bottomMargin = rect.bottom;
	    }
		mBgLayout =(RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_boot_text_info, null);
		mInfoText =(TextView)mBgLayout.findViewById(R.id.boot_text);
		mInfoText.getPaint().setFakeBoldText(true);
		mBgLayout.setVisibility(View.GONE);
		this.setGravity(Gravity.CENTER);
		this.addView(mBgLayout);
		setFocusable(false);
		Log.i(TAG,"LL Set PromptBgLayout To Be Gone In init()");
//		this.setLayoutParams(params);
	}
	
	public void setLayoutParams(Rect rect){
		LayoutParams params = null;
	    params =new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    if(null != rect){
	    	params.topMargin = rect.top;
		    params.leftMargin = rect.left;
		    params.width = rect.width();
		    params.height = rect.height();
	    }
		this.setLayoutParams(params);
	}
	public void setPromptInfo(String promptInfo){
		mPromptInfo =promptInfo;
		if(mPromptInfo ==null){
			Log.i(TAG,"LL Set PromptBgLayout To Be Gone In setPromptInfo()");
			mBgLayout.setVisibility(View.GONE);
		}else{
			mInfoText.setText(mPromptInfo);
			Log.i(TAG,"LL Set PromptBgLayout To Be VISIBLE In setPromptInfo()");
			mBgLayout.setVisibility(View.VISIBLE);
			this.setFocusable(true);
			this.requestFocus();
		}
		
	}
	public void show(){
		Log.i(TAG,"LL ViewBootTextInfo>>show>>mPromptInfo = " + mPromptInfo);
		if((this.mPromptInfo != null)&&(this.mPromptInfo.trim().length()>0)&&(!this.mPromptInfo.trim().equals("null"))){
			mBgLayout.setVisibility(View.VISIBLE);
			this.setFocusable(true);
			this.requestFocus();
		}
	}
	public void hide(){
		mBgLayout.setVisibility(View.GONE);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG,"LL ViewBootTextInfo>>onKeyDown>>keyCode = " + keyCode);
		return true;
	}
}
