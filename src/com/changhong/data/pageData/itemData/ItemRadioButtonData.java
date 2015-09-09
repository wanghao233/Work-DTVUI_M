package com.changhong.data.pageData.itemData;

import com.changhong.data.pageData.itemData.util.EnumDataType;
import com.changhong.data.pageData.itemData.util.ItemData;
import android.view.KeyEvent;

public abstract class ItemRadioButtonData extends ItemData{

	protected boolean mSelected = false; 
	public ItemRadioButtonData(int mImage, String mName, int mhelpId, int mhelpImage) {
		// TODO Auto-generated constructor stub
		super(mImage, mName, mhelpId, mhelpImage);
		mType =EnumDataType.EN_RADIOBUTTON;
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
	public boolean getSelected(){
		return mSelected;
	}
	
	public void setSelected(boolean selected){
		mSelected = selected;
	}
}