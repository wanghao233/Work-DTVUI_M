package com.changhong.data.pageData.itemData;

import com.changhong.data.pageData.itemData.util.EnumDataType;
import com.changhong.data.pageData.itemData.util.ItemData;
import android.view.KeyEvent;

public abstract class ItemRatingBarData extends ItemData{

	protected int mBlueCount; 
    protected int mTotalCount = -1; 
	public ItemRatingBarData(int mImage, String mName, int mhelpId, int mhelpImage) {
		// TODO Auto-generated constructor stub
		super(mImage, mName, mhelpId, mhelpImage);
		mType =EnumDataType.EN_RATINGBAR;
		IsRealTimeData = true;
	}	
	
	public  boolean onkeyDown(int keyCode ,KeyEvent event){
		// TODO Auto-generated method stub
		return false;	 
	}
	
	public boolean initData() {
		// TODO Auto-generated method stub
		return false;
	}
	public int getBlueCount(){
		return mBlueCount;
	}
	
	public void setBlueCount(int blueCount){
		mBlueCount = blueCount;
	}
	public int getTotalCount(){
		return mTotalCount;
	}
	public void setTotalCount(int totalCount){
		mTotalCount = totalCount;
	}
	
}