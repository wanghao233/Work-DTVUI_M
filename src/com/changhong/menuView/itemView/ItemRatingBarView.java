package com.changhong.menuView.itemView;

import com.changhong.data.pageData.itemData.ItemRatingBarData;
import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemRatingBarView extends ItemBaseView{
	private static final String TAG  = ItemRatingBarView.class.getSimpleName();
	private RelativeLayout layout;
	private RelativeLayout mImageBg;
	private RelativeLayout mImage;
	private TextView mName;
	private RelativeLayout mBarBg;
	private LinearLayout mGrayBarBg;
	private LinearLayout mBlueBarBg;
	private LayoutParams mLayoutParams = null;
	private ItemRatingBarData mData =null;
	
	public ItemRatingBarView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private void initBlueRate(int blueCount){
		mBlueBarBg.removeAllViews();
		for(int i=0;i<blueCount;i++){
			RelativeLayout layout =new RelativeLayout(mContext);
			layout.setBackgroundResource(R.drawable.scan_signal_strength_quality_blue);
			mBlueBarBg.addView(layout,mLayoutParams);
		}
	}
	
	private void initGrayRate(int totalCount){
		mGrayBarBg.removeAllViews();
		for(int i=0;i<totalCount;i++){
			RelativeLayout layout =new RelativeLayout(mContext);
			layout.setBackgroundResource(R.drawable.scan_signal_strength_quality_gray);
			mGrayBarBg.addView(layout,mLayoutParams);
		}
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(mData.isEnable() >0){
			mName.setTextColor(TextEnableColor);
		}else{
			mName.setTextColor(TextUnableColor);
		}
		initBlueRate(mData.getBlueCount());
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mData =(ItemRatingBarData) mItemData;
		mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		mLayoutParams.rightMargin = 20;
		layout =(RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.menu_ratingbar, null);
		this.addView(layout);
		mImageBg =(RelativeLayout) layout.findViewById(R.id.ItemImageBg);
		mImage =(RelativeLayout) layout.findViewById(R.id.ItemImage);
		mName =(TextView) layout.findViewById(R.id.ItemName);
		mBarBg = (RelativeLayout) layout.findViewById(R.id.barBg);
		mBlueBarBg = (LinearLayout) layout.findViewById(R.id.blueBarBg);
		mGrayBarBg = (LinearLayout) layout.findViewById(R.id.grayBarBg);
		mName.setTextSize(TextSize);
		if(mData.mImage ==0){
			mImageBg.setVisibility(View.INVISIBLE);
		}else{
			mImage.setBackgroundResource(mData.mImage);
		}
		Log.i(TAG,"LL mData.mName = " + mData.mName);
		mName.setText(mData.mName);
		initGrayRate(mData.getTotalCount());
		update();
	}
}