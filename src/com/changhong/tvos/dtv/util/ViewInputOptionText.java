package com.changhong.tvos.dtv.util;

import java.util.List;
import java.util.regex.Pattern;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class ViewInputOptionText extends TextView {
	List<String> list;
	int currentIndex = 0;
	int curValue;
	int listsize = 0;
	int minValue =0;
	int maxValue =10000;
	boolean inputEnable;
	private Context mContext;
//	private AdjustCHSoftKeyboardManager mAdjustCHSKM = null;

	public ViewInputOptionText(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		mContext = context;
		setFocusable(true);
		inputEnable =true;
		
		setTextColor(Color.WHITE);
		setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					setTextColor(Color.BLACK);
				}else{
					setTextColor(Color.WHITE);
				}
			}
		});
	}

	public ViewInputOptionText(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
		mContext = context;
		setFocusable(true);
		inputEnable =true;
		setTextColor(Color.WHITE);
		setOnFocusChangeListener(new OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					setTextColor(Color.BLACK);
				}else{
					setTextColor(Color.WHITE);
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v("onKeyDown", "LL EditSelectText>>keyCode = " + keyCode);
		if (listsize != 0)
			switch (event.getScanCode()) {
			case 231://keyboard Menu
			case 233://keyboard Channel Up
			case 234://keyboard Channel Down
				break;
			case 232://keyboard Source 
				return true;
			case 235://keyboard Volume Down
				if (isEnabled()) {
					currentIndex = (currentIndex == 0) ? listsize - 1
							: currentIndex - 1;
					String str =list.get(currentIndex);
					setText(str);
//					curValue =Integer.parseInt(str);
					curValue =this.convertString2Integer(str);
				}
				return true;
			case 236://keyboard Volume Up
				if (isEnabled()) {
					currentIndex = (currentIndex + 1) % listsize;
					String str =list.get(currentIndex);
					setText(str);
//					curValue =Integer.parseInt(str);
					curValue =this.convertString2Integer(str);
				}
				return true;
			default:
				break;
			}
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if (isEnabled()) {
					currentIndex = (currentIndex == 0) ? listsize - 1
							: currentIndex - 1;
					String str =list.get(currentIndex);
					setText(str);
//					curValue =Integer.parseInt(str);
					curValue =this.convertString2Integer(str);
				}
				return true;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if (isEnabled()) {
					currentIndex = (currentIndex + 1) % listsize;
					String str =list.get(currentIndex);
					setText(str);
//					curValue =Integer.parseInt(str);
					curValue =this.convertString2Integer(str);
				}
				return true;
			case KeyEvent.KEYCODE_MENU:
				return false;
			case KeyEvent.KEYCODE_DEL:
				if(inputEnable){
					curValue =curValue/10;
					currentIndex =findCurIndex(curValue);
					setText(""+curValue);
				}
				break;
//			case KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD: //KEYCODE_CHANGHONGIR_SOFTKEYBOARD 软键盘
//				if (mAdjustCHSKM  == null) {
//					mAdjustCHSKM  = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(mContext);		
//				}
//				if(null != mAdjustCHSKM){
//					mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD, CHSoftKeyboardManager.POS_BOTTOM_CENTER);
//					return true;
//				}
//				break;
			default:

					if (keyCode >= KeyEvent.KEYCODE_0&& keyCode <= KeyEvent.KEYCODE_9 && inputEnable) {
						curValue =curValue*10+keyCode-KeyEvent.KEYCODE_0;
						if(curValue >maxValue){
							curValue = keyCode-KeyEvent.KEYCODE_0;
						}
						currentIndex =findCurIndex(curValue);
						setText(""+curValue);
						//setCursorVisible(true);
						}
					break;
			    }
		return super.onKeyDown(keyCode, event);
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
		setText(list.get(currentIndex));
	}

	public int getCurrentIndex() {
		return currentIndex;
	}
	public void setInputEnable(boolean enble) {
		inputEnable =enble;
	}
	int findCurIndex(int curValue){
		int index =0;
		int value1;
		int value2;
		if(/*curValue<Integer.parseInt(list.get(0))*/curValue <this.convertString2Integer(list.get(0))){
			return 0;
		}
		for(int i = 0; i<list.size()-1; i++){
//			value1 =Integer.parseInt(list.get(i));
			value1 = this.convertString2Integer(list.get(i));
//			value2 =Integer.parseInt(list.get(i+1));
			value2 = this.convertString2Integer(list.get(i+1));
			index =i;
			if(curValue>=value1&& curValue<=value2){
				if((value2 -curValue) <=(curValue -value1)){
					index =i+1;
				}
				break;
			}
		}
		return index;
	}
	private int convertString2Integer(String str){
		int target = 0;
		String regEx="[^0-9]";   
		Pattern p = Pattern.compile(regEx);   

		if(str!=null){
			target = Integer.parseInt(p.matcher(str).replaceAll("").trim());
		}
		return target;
	} 
	public void setList(List<String> list, int index ,int minValue,int maxValue) {
		this.list = list;
		this.minValue = minValue;
		this.maxValue = maxValue;
		currentIndex = index;
		listsize = list.size();
		setText(list.get(index));
	}
	public void setList(int curValue ,List<String> list ,int minValue,int maxValue) {
		this.list = list;
		this.minValue = minValue;
		this.maxValue = maxValue;
		currentIndex = findCurIndex(curValue);
		listsize = list.size();
		setText(list.get(currentIndex));
	}
	
	public void setCurValue(int value){
		curValue = value;
	}
}