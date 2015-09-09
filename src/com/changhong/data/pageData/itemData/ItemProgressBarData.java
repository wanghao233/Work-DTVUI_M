package com.changhong.data.pageData.itemData;

import com.changhong.data.pageData.itemData.util.EnumDataType;
import com.changhong.data.pageData.itemData.util.ItemData;
import android.util.Log;
import android.view.KeyEvent;

public abstract class ItemProgressBarData extends ItemData{

	public int mValue; 
    public int mMinValue; 
    public int mMaxValue;
    private int mStepValue =1; 


    
    public ItemProgressBarData(int mImage, String mName, int mhelpId, int mhelpImage) {
		super(mImage, mName, mhelpId, mhelpImage);
		mType =EnumDataType.EN_PROGRESSBAR;
		IsRealTimeData =true;
	}	
    
    public ItemProgressBarData(int mImage, String mName, int mhelpId, int mhelpImage,int mValue,int mMinValue,int mMaxValue) {
		super(mImage, mName, mhelpId, mhelpImage);
		this.mValue =mValue;
		this.mMinValue =mMinValue;
		this.mMaxValue =mMaxValue;
		mType =EnumDataType.EN_PROGRESSBAR;
		IsRealTimeData =true;
	}	
    
	public abstract void onValueChange(int Value);

	public int getCurValue(){
		return mValue;
	}
	
	public int getMaxValue(){
		return mMaxValue;
	}
	
	public int getMinValue(){
		return mMinValue;
	}
	
	
	public int setCurValue(int value){
		if(value <=mMaxValue && value >=mMinValue){
		mValue =value;
		}
		Log.v("tv","ProgressBar mValue =" +mValue);
		return mValue;
	}
	public void setMinValue(int value){
		mMinValue =value;
	}
	
	public void setMaxValue(int value){
		mMaxValue =value;
	}
	
	public void setStepValue(int value){
		mStepValue =value;
	}
	public  boolean onkeyDown(int keyCode ,KeyEvent event){
		if(mMinValue ==mMaxValue){
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
			mValue = (mValue -mStepValue)<mMinValue?mValue:mValue -mStepValue;
			onValueChange(mValue);
			update();
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			mValue = (mValue +mStepValue)>mMaxValue?mValue:mValue +mStepValue;
			onValueChange(mValue);
			update();
			return true;
		default:
			break;
		}
		return false;	
	}
}