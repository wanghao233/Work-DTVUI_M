package com.changhong.menuView.itemView;

import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemRootView extends ItemBaseView{
	private static final String TAG = ItemRootView.class.getSimpleName();
	RelativeLayout mImageBg;
	ImageView mImage;
	TextView mName;
	RelativeLayout layout;
	public ItemRootView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(mItemData.isEnable() >0){
			mName.setTextColor(TextEnableColor);
		}else{
			mName.setTextColor(TextUnableColor);
		}
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		layout =(RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.menu_root_item, null);
		this.addView(layout);
		mImageBg =(RelativeLayout) layout.findViewById(R.id.relativeLayout1);
		mImage =(ImageView) layout.findViewById(R.id.ItemImage);
		mName =(TextView) layout.findViewById(R.id.ItemName);
		mName.setTextSize(TextSize);
		if(mItemData.mImage==0){
			mImageBg.setVisibility(View.GONE);
		}else{
			mImage.setImageResource(mItemData.mImage);
		}
		mName.setText(mItemData.mName);
		update();
	}
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG,"LL onTouchEvent()***");
		getItemData().onkeyDown(KeyEvent.KEYCODE_DPAD_CENTER, null);
		return super.onTouchEvent(event);
	}
}