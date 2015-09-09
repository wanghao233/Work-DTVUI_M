package com.changhong.menuView.itemView;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.changhong.data.pageData.itemData.ItemOptionInputData;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstDemodType;

public class ItemOptionInputView extends ItemBaseView{
	private static final String TAG = ItemOptionInputView.class.getSimpleName();
	private RelativeLayout layout;
	private RelativeLayout mImageBg;
	private RelativeLayout mImage;
	private RelativeLayout mLImage;
	private RelativeLayout mRImage;
	private TextView mName;
	private TextView mValue;
	private LinearLayout mDotL;
	private LinearLayout mDotM;
	private LinearLayout mDotR;
	private ItemOptionInputData mData = null;
	public ItemOptionInputView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mData = (ItemOptionInputData)mItemData;
		layout =(RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.menu_optionview, null);
		this.addView(layout);
		mImageBg =(RelativeLayout) layout.findViewById(R.id.ItemImageBg);
		mImage =(RelativeLayout) layout.findViewById(R.id.ItemImage);
		mName =(TextView) layout.findViewById(R.id.ItemName);
		mValue =(TextView) layout.findViewById(R.id.Value);
		mDotL = (LinearLayout) layout.findViewById(R.id.DotL);
		mDotM = (LinearLayout) layout.findViewById(R.id.DotM);
		mDotR = (LinearLayout) layout.findViewById(R.id.DotR);
		mDotL.setVisibility(View.GONE);
		mDotM.setVisibility(View.GONE);
		mDotR.setVisibility(View.GONE);
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
	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(mData.isEnable()>0){
			mName.setTextColor(TextEnableColor);
			mValue.setTextColor(TextEnableColor);
		}else{
			mName.setTextColor(TextUnableColor);
			mValue.setTextColor(TextUnableColor);
		}
		mName.setText(mData.mName);
		if("frequencyKHz".equals(mData.mId)){
			int demodType = DtvSourceManager.getInstance().getCurDemodeType();
			int tem = mData.getCurValue();
			String show = null;
			if(ConstDemodType.DVB_C == demodType){
				tem = (tem < 1000 ) ? tem : tem / 1000;
				show = String.valueOf(tem);
			}else {
				tem = (tem < 10000) ? tem : tem/100;
				if(tem < 10){
					show = "000."+ tem % 10;
				}else if(tem < 100){
					show= "00" + tem / 10 + "." + tem % 10;
				}else if (tem < 1000) {
					show= "0" + tem / 10 + "." + tem % 10;
				}else{
					show= tem / 10 + "." + tem % 10;
				}
				
			}
			
			mValue.setText(show + "MHz");
		}else{
		mValue.setText(""+mData.getCurValue());
	}
		
	}
}