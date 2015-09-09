package com.changhong.data.pageData.itemData.util;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.view.KeyEvent;
import com.changhong.data.pageData.PageData;
import com.changhong.data.pageData.itemData.ItemHaveSubData;

public abstract  class ItemData{
	
	public interface OnNextPageListener{
		public void onNextPage(PageData data);
	}
	
	 private ItemViewInterface mIntItemView;
	 public ItemViewInterface mOnlyShowView =null;
	 
	 public String mId =null;
	 public int mImage;
	 public String mName; 
	 private int mhelpId;
	 private int mhelpImage;
	 protected EnumDataType mType =EnumDataType.EN_ROOTVIEW;
		
	 public PageData  mChild; 
	 public boolean IsRealTimeData =false; 
	 public boolean isOnlyShow =false;


		
	 protected List<OnNextPageListener> mOnNextPageListener =null;
	 
	 public ItemData(int mImage ,String mName ,int mhelpId ,int mhelpImage){
		this.mImage =mImage;
		this.mName =mName;
		this.mhelpId =mhelpId;
		this.mhelpImage =mhelpImage;
	 }
	 public abstract boolean onkeyDown(int keyCode ,KeyEvent event);
	 public abstract int isEnable();
	 public abstract boolean initData();

	public void update(){
		if(mOnlyShowView !=null){
			mOnlyShowView.update();
		}
		if(mIntItemView !=null){
			mIntItemView.update();
		}
	}
	
	public void requestFocus(){
		Log.v("tv","requestFocus");
		if(mIntItemView !=null && isEnable()>0){	
			mIntItemView.setSelect(true);
		}
	}
	
	public void clearFocus(){
		Log.v("tv","clearFocus");
		if(mIntItemView !=null){
			mIntItemView.setSelect(false);
		}
	}
	public EnumDataType getDataType(){
		return mType;
	}
	
	public ItemViewInterface getView(){
		return mIntItemView;
	}
	
	public void setView(ItemViewInterface view){
		mIntItemView =view;
	}
	public void setOnNextPageListener(OnNextPageListener listener){
		if(mOnNextPageListener ==null){
			mOnNextPageListener =new ArrayList<ItemHaveSubData.OnNextPageListener>();
		}
		mOnNextPageListener.add(listener);
	}
}