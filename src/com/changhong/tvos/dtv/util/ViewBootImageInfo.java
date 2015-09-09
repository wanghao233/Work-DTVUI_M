package com.changhong.tvos.dtv.util;

import java.io.File;
import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ViewBootImageInfo extends RelativeLayout {
	protected static final String TAG = "ViewBootImageInfo";
	RelativeLayout mBgLayout =null;
	ImageView mImageView =null;
	String mStrURL =null;
	
	public ViewBootImageInfo(Context context,Rect rect) {
		super(context);
		// TODO Auto-generated constructor stub
		this.init(context, rect);
	}
	public ViewBootImageInfo(Context context,AttributeSet attrs,Rect rect){
		super(context, attrs);
		this.init(context, rect);
	}
	
	public ViewBootImageInfo(Context context,AttributeSet attrs,int defStyle,Rect rect){
		super(context, attrs, defStyle);
		this.init(context, rect);
	}
	
	private void init(Context context,Rect rect){
		LayoutParams params = null;
	    params =new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    if(null == rect||0 == rect.width()&&0 == rect.height()){
	    	params.topMargin = 0;
		    params.leftMargin = 0;
		    params.rightMargin = 0;
		    params.bottomMargin = 0;
	    }else{
	    	params.topMargin = rect.top;
		    params.leftMargin = rect.left;
		    params.rightMargin = rect.right;
		    params.bottomMargin = rect.bottom;
	    }
		mBgLayout =(RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_boot_image_info, null);
		mImageView =(ImageView)mBgLayout.findViewById(R.id.boot_image);
		this.addView(mBgLayout);
		Log.i(TAG,"LL Set PromptBgLayout To Be Gone In init()");
		mBgLayout.setVisibility(View.GONE);
		this.setLayoutParams(params);
	}
	
	public void setLayoutParams(Rect rect){
		LayoutParams params = null;
	    params =new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    if(null == rect||0 == rect.width()&&0 == rect.height()){
	    	params.topMargin = 0;
		    params.leftMargin = 0;
		    params.rightMargin = 0;
		    params.bottomMargin = 0;
	    }else{
	    	params.topMargin = rect.top;
		    params.leftMargin = rect.left;
		    params.rightMargin = rect.right;
		    params.bottomMargin = rect.bottom;
	    }
		this.setLayoutParams(params);
	}
	public void setImageInfo(String strURL){
		mStrURL =strURL;
		if(mStrURL ==null){
			Log.i(TAG,"LL Set PromptBgLayout To Be Gone In setPromptInfo()");
			mBgLayout.setVisibility(View.GONE);
		}else{
			Uri uri = Uri.fromFile(new File(mStrURL));
			Log.i(TAG,"LL setImageInfo>>uri = " + uri);
			if(null != uri){
				
				mImageView.setImageURI(uri);
				mBgLayout.setVisibility(View.VISIBLE);
				this.setFocusable(true);
				this.requestFocus();
				
			}
		}
		
	}
	public void show(){
		if(this.mStrURL != null){
			mBgLayout.setVisibility(View.VISIBLE);
			this.setFocusable(true);
			this.requestFocus();
		}
	}
	public void hide(){
		mBgLayout.setVisibility(View.GONE);
	}	
}
