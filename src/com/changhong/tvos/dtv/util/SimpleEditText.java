package com.changhong.tvos.dtv.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class SimpleEditText extends EditText{
	OnInnerKeyDownListener mOnInnerKeyDownListener = null;
	public interface OnInnerKeyDownListener{
		public void onInnerKeyDownListener();
	}
	public SimpleEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}
	public SimpleEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public SimpleEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void setOnInnerKeyDownListener(OnInnerKeyDownListener listener){
		mOnInnerKeyDownListener =listener;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(mOnInnerKeyDownListener!=null){
			mOnInnerKeyDownListener.onInnerKeyDownListener();
		}
		return super.onKeyDown(keyCode, event);
	}
}