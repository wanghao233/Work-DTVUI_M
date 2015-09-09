package com.changhong.data.pageData;

import android.view.KeyEvent;

public abstract class PageData{
	public enum EnumPageType{
		BroadListPage,
		NarrowListPage,
	}
	public interface OnPageTurnListener{
		public void onPageTurn(PageData data);
	}
	public String mPageId ="NO ID";
	public int mhelpId;
	public PageData  mParent;
	public EnumPageType mType =EnumPageType.BroadListPage;
	public OnPageTurnListener mOnPageTurnListener =null;
	
	public boolean isFoucsAble =true;

	public PageData(String pageId){
		if(pageId !=null){			
			mPageId =pageId;
		}
	}
	public abstract boolean onkeyDown(int keyCode, KeyEvent event);
	public abstract void reset();
	
	public void onPageTurn(PageData data){
		if(mOnPageTurnListener !=null){
			mOnPageTurnListener.onPageTurn(data);
		}
	}
	
	public  void setOnPageTurnListener(OnPageTurnListener listener){
		if(mOnPageTurnListener ==null){
			mOnPageTurnListener =listener;
		}
	}
	
}