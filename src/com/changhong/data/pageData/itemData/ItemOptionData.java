package com.changhong.data.pageData.itemData;

import com.changhong.data.pageData.itemData.util.EnumDataType;
import com.changhong.data.pageData.itemData.util.ItemData;
import android.util.Log;
import android.view.KeyEvent;

public abstract class ItemOptionData extends ItemData{

    private static final String TAG = ItemOptionData.class.getSimpleName();
	protected int mValue; 
    protected String[] mOptionValue;
    protected int mMinValue = -1; 
    protected int mMaxValue = -1;

    public ItemOptionData(int mImage, String mName, int mhelpId, int mhelpImage) {
		super(mImage, mName, mhelpId, mhelpImage);
		// TODO Auto-generated constructor stub
		mType =EnumDataType.EN_OPTIONVIEW;
		IsRealTimeData =true;
	}	
	public abstract  void onValueChange(int Value);

	public int getCurValue(){
		return mValue;
	}
	
	public int getMaxValue(){
		if(mOptionValue !=null){
			mMaxValue =mOptionValue.length -1;
		}
		return mMaxValue;
	}
	
	public int getMinValue(){
		if(mOptionValue !=null){
			mMinValue =0;
		}
		return mMinValue;
	}
	
	
	public String[] getOptionStrs(){
		return mOptionValue;
	}
	
	public String getCurStr(){
		if(mOptionValue ==null ||mOptionValue.length ==0){
			Log.e(TAG,"LL getCurStr()>>curStr == null");
			return null;	
		}
		if(mValue >=mOptionValue.length){
			Log.e(TAG,"LL getCurStr()>>curStr == [0]");
			mValue =0;
		}
		Log.i(TAG,"LL getCurStr()>>mValue = " + mValue);
		return mOptionValue[mValue];
	}
	
	public int setCurValue(int value){
		if(mOptionValue ==null ||mOptionValue.length ==0){
			Log.e(TAG,"LL setCurValue()>>value == 0");
			mValue =0;	
		}else if(value <mOptionValue.length){
			mValue =value;
		}
		Log.i(TAG,"LL setCurValue()>>value = " + mValue);
		return mValue;
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
			mValue = (mValue + mOptionValue.length - 1) % mOptionValue.length;
			update();
			onValueChange(mValue);
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			mValue = (mValue + 1) % mOptionValue.length;
			update();
			onValueChange(mValue);
			return true;
		default:
			break;
		}
		return false;	
	}
}