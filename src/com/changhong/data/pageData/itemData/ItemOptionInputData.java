package com.changhong.data.pageData.itemData;

import com.changhong.data.pageData.itemData.util.EnumDataType;
import com.changhong.data.pageData.itemData.util.ItemData;
import android.util.Log;
import android.view.KeyEvent;

public abstract class ItemOptionInputData extends ItemData{
	private static final String TAG= ItemOptionInputData.class.getSimpleName();
	private int mValueInput;
    private int mValueIndex; 
    protected int mValue;
    protected String[] mOptionValue;
    protected int mMinValue = -1; 
    protected int mMaxValue = -1;
    protected boolean isShieldInput = false;

    public ItemOptionInputData(int mImage, String mName, int mhelpId, int mhelpImage) {
		super(mImage, mName, mhelpId, mhelpImage);
		// TODO Auto-generated constructor stub
		mType =EnumDataType.EN_OPTIONINPUTVIEW;
		IsRealTimeData =true;
	}	
	public abstract  void onValueChange(int Value);

	public int getCurValue(){
		return mValue;
	}
	
	public void setMinMaxValue(int min,int max){
		mMinValue = min;
		mMaxValue = max;
	}
	
	public void setCurValue(int value){
		mValue = value;
		mValueIndex = findCurIndex(value);
		Log.i("tv","LL mValueIndex = " + mValueIndex);
	}
	public void setShieldInput(boolean input){
		this.isShieldInput = input;
	}
	public void setOptionValues(String[] mOptionValue){
		this.mOptionValue =mOptionValue;
	}
	
	public  boolean onkeyDown(int keyCode ,KeyEvent event){
		if(mOptionValue ==null ||mOptionValue.length ==0){
			return false;
		}
		if(isOnlyShow){
			if(keyCode ==KeyEvent.KEYCODE_DPAD_LEFT||
					keyCode ==KeyEvent.KEYCODE_DPAD_RIGHT ||
					keyCode ==KeyEvent.KEYCODE_DPAD_CENTER||
					keyCode == KeyEvent.KEYCODE_ENTER){
				if(mOnNextPageListener !=null){
					if(mOnlyShowView!=null &&mOnlyShowView.isSelect()){
						
					}else{
						for(OnNextPageListener listener:mOnNextPageListener)
							listener.onNextPage(mChild);
						return true;	 
					}	 
				}
			}
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			mValueIndex = (mValueIndex + mOptionValue.length - 1) % mOptionValue.length;
			mValue = Integer.parseInt(mOptionValue[mValueIndex]);
			update();
			onValueChange(mValue);
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			mValueIndex = (mValueIndex + 1) % mOptionValue.length;
			mValue = Integer.parseInt(mOptionValue[mValueIndex]);
			update();
			onValueChange(mValue);
			return true;
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
			if(isShieldInput == false){
				mValueInput =mValueInput*10+(keyCode-KeyEvent.KEYCODE_0);
				if(mValueInput >mMaxValue){
					mValueInput =(keyCode-KeyEvent.KEYCODE_0);
				}
				mValueIndex =findCurIndex(mValueInput);
				mValue = mValueInput;
				update();
				onValueChange(mValue);
			}			
			break;
		case KeyEvent.KEYCODE_DEL:
			if(isShieldInput == false){
				mValueInput =mValueInput/10;
				mValueIndex =findCurIndex(mValueInput);
				mValue = mValueInput;
				update();
				onValueChange(mValue);
			}			
			break;
		default:			
			break;
		}
		return false;	
	}

	private int findCurIndex(int inputNum){
		int index =0;
		int value1;
		int value2;
		if(null == mOptionValue||inputNum<Integer.parseInt(mOptionValue[0])){
			return 0;
		}
		for(int i = 0; i<mOptionValue.length-1; i++){
			value1 =Integer.parseInt(mOptionValue[i]);
			value2 =Integer.parseInt(mOptionValue[i+1]);
			index =i;
			if(inputNum>=value1&& inputNum<=value2){
				if((value2 -inputNum) <=(inputNum -value1)){
					index =i+1;
				}
				break;
			}
		}
		return index;
	}
}