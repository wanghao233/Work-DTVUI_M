package com.changhong.tvos.dtv.util;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class ViewInputCodeText extends TextView {

	private Context mContext;
	private boolean inputEnable;
	private int curValue;
	private int maxValue = 9999;
	private String TAG = "ViewInputCodeText";
	private boolean isInputEncode;

	public ViewInputCodeText(Context context) {
		super(context);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	public ViewInputCodeText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setFocusable(true);
		setOnFocusChangeListener(new View.OnFocusChangeListener(){

			@Override
			public void onFocusChange(View arg0, boolean focus) {
				// TODO Auto-generated method stub
				if(focus){
					setTextColor(Color.BLACK);
				}else {
					setTextColor(Color.WHITE);
				}
			}
			
		});
		inputEnable = true;
		setTextColor(Color.WHITE);
		reset();
	}

	public ViewInputCodeText(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG, "ViewInputCodeText onKeyDown keycode-->" + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_DEL:
			curValue = curValue / 10;
			if (curValue > 0) {
				showText(curValue);
			} else {
				showText(-1);
			}
			break;
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_1:
		case KeyEvent.KEYCODE_2:
		case KeyEvent.KEYCODE_3:
		case KeyEvent.KEYCODE_4:
		case KeyEvent.KEYCODE_5:
		case KeyEvent.KEYCODE_6:
		case KeyEvent.KEYCODE_7:
		case KeyEvent.KEYCODE_8:
		case KeyEvent.KEYCODE_9:
			if(inputEnable){
				curValue = curValue * 10 + keyCode - KeyEvent.KEYCODE_0;
				if (curValue > maxValue) {
					curValue = keyCode - KeyEvent.KEYCODE_0;
				}
				showText(curValue);
			}
			break;
		default:
			break;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void reset() {
		curValue = 0;
		setText("");
	}

	public String getCodeString() {
		if(isInputEncode){
			return String.valueOf(curValue);
		}else {
			return getText().toString();
		}
		
	}

	public void showText(int cur) {
		String curString = "";

		if (cur < 0) {
			setText(curString);
			return;
		}  else {
			String tem = String.valueOf(cur);
			for(int i =tem.length(); i >0 ; i--){
				curString += "*";
			}
		}
		
		
		if (isInputEncode) {
			setText(curString);
		} else {
			setText(String.valueOf(cur));
		}

	}

	public void setInputEncode(boolean isInputEncode) {
		this.isInputEncode = isInputEncode;
	}

	public boolean isInputEncode() {
		return isInputEncode;
	}
}
