package com.changhong.menuView.itemView;

import com.changhong.data.pageData.itemData.ItemHaveSubData;
import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemHaveSubView extends ItemBaseView{
	private static final String TAG = ItemHaveSubView.class.getSimpleName();
	RelativeLayout mImageBg;
	RelativeLayout mImage;
	TextView mName;
	TextView mValue;
	RelativeLayout mRImage;
	RelativeLayout layout;
	
	private ItemHaveSubData mData =null;
	public ItemHaveSubView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(mData.isEnable() >0){
			mName.setTextColor(TextEnableColor);
			mValue.setTextColor(TextEnableColor);
		}else{
			mName.setTextColor(TextUnableColor);
			mValue.setTextColor(TextUnableColor);
		}
		if(mData.mIsDismissArrow==true){
			
			mRImage.setVisibility(View.GONE);
		}else{
			mRImage.setVisibility(View.VISIBLE);
		}
		mValue.setText(mData.getValue());
		
		Log.i("update", "updatePageView() -->> ItemHaveSubView mListScan.update");
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mData = (ItemHaveSubData)mItemData;
		layout =(RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.menu_havesub, null);
		this.addView(layout);
		mImageBg =(RelativeLayout) layout.findViewById(R.id.ItemImageBg);
		mImage =(RelativeLayout) layout.findViewById(R.id.ItemImage);
		mName =(TextView) layout.findViewById(R.id.ItemName);
		mValue =(TextView) layout.findViewById(R.id.Value);
		mRImage = (RelativeLayout) layout.findViewById(R.id.RImage);
		mName.setTextSize(TextSize);
		mValue.setTextSize(TextSize);
		Log.i(TAG,"LL mImage = " + mData.mImage);
		if(mData.mImage ==0){
			mImageBg.setVisibility(View.GONE);
		}else{
			mImage.setBackgroundResource(mData.mImage);
		}
		mName.setText(mData.mName);
		if(mData.mIsDismissArrow==true){
			
			mRImage.setVisibility(View.GONE);
		}else{
			mRImage.setVisibility(View.VISIBLE);
		}
		this.update();
	}
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG,"LL onTouchEvent()***");
		getItemData().onkeyDown(KeyEvent.KEYCODE_DPAD_CENTER, null);
		return super.onTouchEvent(event);
	}
}