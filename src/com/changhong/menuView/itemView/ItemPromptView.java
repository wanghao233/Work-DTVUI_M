package com.changhong.menuView.itemView;

import com.changhong.data.pageData.itemData.ItemPromptData;
import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemPromptView extends ItemBaseView{
	private static final String TAG = ItemPromptView.class.getSimpleName();
	RelativeLayout mImageBg;
	RelativeLayout mImage;
	TextView mName;
	TextView mValue;
	RelativeLayout layout;
	ItemPromptData mData;
	public ItemPromptView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		mName.setText(mItemData.mName);
		if(mItemData.isEnable() >0){
			mName.setTextColor(TextEnableColor);
			mValue.setTextColor(TextEnableColor);
		}else{
			mName.setTextColor(TextUnableColor);
			mValue.setTextColor(TextUnableColor);
		}
		mValue.setText(mData.getValue());
//		if(null != mData.getValue() && mData.getValue().contains("AC3")){
//			Drawable drawable = mContext.getResources().getDrawable(R.drawable.logo_for_dolby);
//			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());  
//			mValue.setCompoundDrawables(drawable, null, null, null);
//		}else{
//			mValue.setCompoundDrawables(null, null, null, null);
//		}
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mData =(ItemPromptData)mItemData;
		layout =(RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.menu_prompt, null);
		this.addView(layout);
		mImageBg =(RelativeLayout) layout.findViewById(R.id.ItemImageBg);
		mImage =(RelativeLayout) layout.findViewById(R.id.ItemImage);
		mName =(TextView) layout.findViewById(R.id.ItemName);
		mValue =(TextView) layout.findViewById(R.id.ItemValue);
		mName.setTextSize(TextSize);
		mValue.setTextSize(TextSize);
		Log.i(TAG,"LL mImage = " + mItemData.mImage);
		if(mItemData.mImage ==0){
			mImageBg.setVisibility(View.GONE);
		}else{
			mImage.setBackgroundResource(mItemData.mImage);
		}
//		if(null != mData.getValue() && mData.getValue().contains("AC3")){
//			Drawable drawable = mContext.getResources().getDrawable(R.drawable.logo_for_dolby);
//			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());  
//			mValue.setCompoundDrawables(drawable, null, null, null);
//		}else{
//			mValue.setCompoundDrawables(null, null, null, null);
//		}
		mName.setText(mItemData.mName);
		this.update();
	}
}