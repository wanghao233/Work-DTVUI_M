package com.changhong.menuView.itemView;

import com.changhong.data.pageData.itemData.ItemOptionData;
import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemOptionView extends ItemBaseView{
	private static final String TAG  = ItemOptionView.class.getSimpleName();
	private RelativeLayout mImageBg;
	private RelativeLayout mImage;
	private RelativeLayout mLImage;
	private RelativeLayout mRImage;
	private TextView mName;
	private TextView mValue;
	private LinearLayout mDotL;
	private LinearLayout mDotM;
	private LinearLayout mDotR;
	private RelativeLayout layout;
	
	private ItemOptionData mData =null;
	
	public ItemOptionView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private void initDot(int curIndex,int dotCount){
		mDotL.removeAllViews();
		mDotR.removeAllViews();
		for(int i=0;i<curIndex;i++){
			RelativeLayout layout =new RelativeLayout(mContext);
			layout.setBackgroundResource(R.drawable.dot_unsel);
			mDotL.addView(layout);
		}
		for(int i=curIndex+1;i<dotCount;i++){
			RelativeLayout layout =new RelativeLayout(mContext);
			layout.setBackgroundResource(R.drawable.dot_unsel);
			mDotR.addView(layout);
		}
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
		mValue.setText(mData.getCurStr());
		Log.i(TAG,"LL update()>>str = " + mData.getCurStr());
		initDot(mData.getCurValue() ,mData.getOptionStrs().length);
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mData =(ItemOptionData) mItemData;
		layout =(RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.menu_optionview, null);
		this.addView(layout);
		mImageBg =(RelativeLayout) layout.findViewById(R.id.ItemImageBg);
		mImage =(RelativeLayout) layout.findViewById(R.id.ItemImage);
		mName =(TextView) layout.findViewById(R.id.ItemName);
		mValue =(TextView) layout.findViewById(R.id.Value);
		mDotL = (LinearLayout) layout.findViewById(R.id.DotL);
		mDotM = (LinearLayout) layout.findViewById(R.id.DotM);
		mDotR = (LinearLayout) layout.findViewById(R.id.DotR);
		mLImage = (RelativeLayout)layout.findViewById(R.id.LImage);
		mRImage = (RelativeLayout)layout.findViewById(R.id.RImage);
		mLImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG,"LL mLImage>>onClick()***");
				mData.onkeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
			}
		});
		mRImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG,"LL mRImage>>onClick()***");
				mData.onkeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
			}
		});
		mName.setTextSize(TextSize);
		mValue.setTextSize(TextSize);
		if(mData.mImage ==0){
			mImageBg.setVisibility(View.INVISIBLE);
		}else{
			mImage.setBackgroundResource(mData.mImage);
		}
		Log.i(TAG,"LL mData.mName = " + mData.mName);
		mName.setText(mData.mName);
		mData.onValueChange(mData.getCurValue());
		update();
	}
}