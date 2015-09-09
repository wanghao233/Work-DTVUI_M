package com.changhong.menuView.itemView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import com.changhong.data.pageData.itemData.util.ItemViewInterface;
import com.changhong.data.pageData.itemData.util.ItemData;

public abstract  class ItemBaseView extends   RelativeLayout implements ItemViewInterface{
	protected ItemData mItemData =null;
	protected boolean isSelect = false;
	protected int TextSize = 26;
	protected int TextEnableColor = 0xFF565656;
	protected int TextUnableColor = 0XFF999999;
	public ItemBaseView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ItemBaseView(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
	}

	public void init(ItemData mItemData){
		this.mItemData =mItemData;
		initView();
	}
	
	public ItemData getItemData(){
		return mItemData;
	}
	
	public void setSelect(boolean isSelect){
		Log.v("tv","reqFocus");
		Log.v("tv","onSelect");
		this.isSelect =isSelect;
		if(isSelect){
			
		}else{
			
		}
	}
	
	public boolean isSelect(){
		return isSelect;
	}
	protected abstract void initView();
	
	public interface OnReturnListener{
		public void OnReturn();
	}
	
	OnReturnListener mOnReturnListener =null;
	
	public void setOnReturnListener(OnReturnListener listener){
		mOnReturnListener =listener;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		getItemData().onkeyDown(keyCode, event);
		
		if(keyCode ==KeyEvent.KEYCODE_BACK){
			if(mOnReturnListener !=null){
				mOnReturnListener.OnReturn();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}