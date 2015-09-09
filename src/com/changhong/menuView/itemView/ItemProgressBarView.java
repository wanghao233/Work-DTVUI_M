package com.changhong.menuView.itemView;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.changhong.data.pageData.itemData.ItemProgressBarData;
import com.changhong.tvos.dtv.R;

public class ItemProgressBarView extends ItemBaseView{
	RelativeLayout mImageBg;
	RelativeLayout mImage;
	TextView mName;
	RelativeLayout mBar;
	TextView mValue;
	android.widget.LinearLayout.LayoutParams mParams;
	RelativeLayout layout;
	
	private ItemProgressBarData mData =null;



	public ItemProgressBarView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	protected void initView() {
		mData =(ItemProgressBarData) mItemData;
		layout =(RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.menu_progressbar, null);
		this.addView(layout);
		mImageBg =(RelativeLayout) layout.findViewById(R.id.ItemImageBg);
		mImage =(RelativeLayout) layout.findViewById(R.id.ItemImage);
		mName =(TextView) layout.findViewById(R.id.ItemName);
		mBar =(RelativeLayout) layout.findViewById(R.id.bar);
		mValue =(TextView) layout.findViewById(R.id.progressValue);
		mParams =(android.widget.LinearLayout.LayoutParams)mBar.getLayoutParams();
		mName.setTextSize(TextSize);
		mValue.setTextSize(TextSize);
		if(mData.mImage ==0){
			mImageBg.setVisibility(View.GONE);
		}else{
			mImage.setBackgroundResource(mData.mImage);
		}

		mName.setText(mData.mName);
		update();
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(mItemData.isEnable() >0){
			mName.setTextColor(TextEnableColor);
			mValue.setTextColor(TextEnableColor);
		}else{
			mName.setTextColor(TextUnableColor);
			mValue.setTextColor(TextUnableColor);
		}
		Log.v("tv","ItemProgressBar update");
		if(mData.getMaxValue() -mData.getMinValue() !=0){
			mParams.width =(mData.getCurValue()-mData.getMinValue())*156/(mData.getMaxValue() -mData.getMinValue());
		}
		mBar.setLayoutParams(mParams);
		mValue.setText(""+mData.getCurValue());
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.v("tv","X ="+getX());
		Log.v("tv","Y ="+getY());
		if(event.getX() >getX()+getWidth()/2){
			getItemData().onkeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
		}else{
			getItemData().onkeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
		}
		return super.onTouchEvent(event);
	}
}