package com.changhong.tvos.dtv.util;

import java.util.List;
import java.util.regex.Pattern;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstDemodType;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

/**
 * 主要思路： 不改变输入的值 ，以及传的参数，只在显示的基础上变成MHz。 所以实际的频率单位仍是KHz
 * @author Administrator
 *
 */
public class ViewFreqInputText extends ViewInputOptionText {

	private static final String TAG = "ViewFreqInputText";
	private int demodType;
	public ViewFreqInputText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ViewFreqInputText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		demodType = DtvSourceManager.getInstance().getCurDemodeType(); 
	}	
	
	public void setFreqText(int value) {
		// TODO Auto-generated method stub
		//52500 235000
		Log.i(TAG, "setFreqText the value is " + value);
		int tem = value;
		if(ConstDemodType.DVB_C == demodType){
			tem = (tem < 1000 ) ? tem : tem / 1000;
			setText(String.valueOf(tem));
		}else {
			
			tem = (tem < 10000) ? tem : tem/100;
			String show = null;
			if(tem < 10){
				show = "000."+ tem % 10;
			}else if(tem < 100){
				show= "00" + tem / 10 + "." + tem % 10;
			}else if (tem < 1000) {
				show= "0" + tem / 10 + "." + tem % 10;
			}else{
				show= tem / 10 + "." + tem % 10;
			}
		
			setText(show);
		}
		
	}

	public CharSequence getFreqText() {
		// TODO Auto-generated method stub
		String text = getText().toString().replace(".", "");
		int tem = this.convertString2Integer(text) ;
		if(ConstDemodType.DVB_C == demodType){
			tem = (tem < 1000 ) ? tem * 1000 : tem;
			
		}else {
			tem = (tem < 10000) ? tem * 100 : tem;
		}
		return String.valueOf(tem);
	}

	private int convertString2Integer(String str){
		int target = 0;
		String regEx="[^0-9]";   
		Pattern p = Pattern.compile(regEx);   

		if(str!=null && str!=""){
			try {
				
				target = Integer.parseInt(p.matcher(str).replaceAll("").trim());
			} catch (NumberFormatException  e) {
				// TODO: handle exception
				Log.e(TAG,"LL convertString2Integer()>>NumberFormatException***" +str);
				target = 0;
			}
		}else {
			Log.e(TAG,"LL convertString2Integer()>>input is null" );
		}
		return target;
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
					setFreqText(convertString2Integer(str));
//					curValue =Integer.parseInt(str);
					curValue =this.convertString2Integer(str);
				}
				return true;
			case 236://keyboard Volume Up
				if (isEnabled()) {
					currentIndex = (currentIndex + 1) % listsize;
					String str =list.get(currentIndex);
					setFreqText(convertString2Integer(str));
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
					setFreqText(convertString2Integer(str));
//					curValue =Integer.parseInt(str);
					curValue =this.convertString2Integer(str);
				}
				return true;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if (isEnabled()) {
					currentIndex = (currentIndex + 1) % listsize;
					String str =list.get(currentIndex);
					setFreqText(convertString2Integer(str));
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
					setFreqText(curValue);
				}
				return true;
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
						if(curValue > maxValue ){
							curValue = keyCode-KeyEvent.KEYCODE_0;
						}
						
						currentIndex =findCurIndex(curValue);
						setFreqText(curValue);
						return true;
						//setCursorVisible(true);
						}
					break;
			    }
		return super.onKeyDown(keyCode, event);
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
		setFreqText(convertString2Integer(list.get(currentIndex)));
	}
	
	public void setList(List<String> list, int index ,int minValue,int maxValue) {
		this.list = list;
		this.minValue = minValue;
		this.maxValue = maxValue;
		currentIndex = index;
		listsize = list.size();
		setFreqText(convertString2Integer(list.get(index)));
	}
	public void setList(int curValue ,List<String> list ,int minValue,int maxValue) {
		this.list = list;
		this.minValue = minValue;
		this.maxValue = maxValue;
		currentIndex = findCurIndex(curValue);
		listsize = list.size();
		setFreqText(convertString2Integer(list.get(currentIndex)));
	}
}
