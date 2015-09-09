package com.changhong.menuView.itemView;

import com.changhong.data.pageData.itemData.ItemRadioButtonData;
import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemRadioButtonView extends ItemBaseView{
	private static final String TAG  = ItemRadioButtonView.class.getSimpleName();
	private RelativeLayout layout;
	private RelativeLayout mImageBg;
	private RelativeLayout mImage;
	private TextView mName;
	private ImageView mRadioBg;
	private ItemRadioButtonData mData =null;
	public final static int SELECT_STYLE_RADIO = 0;
	public final static int SELECT_STYLE_CHECK = 1;

	private int selectStyle = SELECT_STYLE_RADIO;
	public ItemRadioButtonView(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
	}
	public ItemRadioButtonView(Context context,AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
	}
	@SuppressWarnings("deprecation")
	private void initRadio(boolean selected){
		if(true == selected){
			Log.i(TAG,"LL selected == true");
			if(selectStyle == SELECT_STYLE_RADIO){
				
				mRadioBg.setImageDrawable(getResources().getDrawable(R.drawable.menu_set_operator_selected));
			}else{
				mRadioBg.setImageDrawable(getResources().getDrawable(R.drawable.icon_select));
			}
		}else{
			Log.i(TAG,"LL selected == false");
			if(selectStyle == SELECT_STYLE_RADIO){
				
				mRadioBg.setImageDrawable(getResources().getDrawable(R.drawable.menu_set_operator_unselected));
			}else{
				mRadioBg.setImageDrawable(null);
			}
		}
	}
	
	/**
	 * @param style
	 * 0 : radio  
	 * 1 : square
	 */
	public void setSelectStyle(int style){
		selectStyle = style;
	}
	
	/**
	 * @return selectStyle
	 * 0 : radio  
	 * 1 : square
	 */
	public int getSelectStyle(){
		return selectStyle;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(mData.isEnable() >0){
			mName.setTextColor(TextEnableColor);
		}else{
			mName.setTextColor(TextUnableColor);
		}
		initRadio(mData.getSelected());
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mData =(ItemRadioButtonData)mItemData;
		layout =(RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.menu_radio_button_view, null);
		this.addView(layout);
		mImageBg =(RelativeLayout) layout.findViewById(R.id.ItemImageBg);
		mImage =(RelativeLayout) layout.findViewById(R.id.ItemImage);
		mName =(TextView) layout.findViewById(R.id.ItemName);
		mRadioBg = (ImageView) layout.findViewById(R.id.ridioBg);
		mName.setTextSize(TextSize);
		if(mData.mImage ==0){
			mImageBg.setVisibility(View.INVISIBLE);
		}else{
			mImage.setBackgroundResource(mData.mImage);
		}
		Log.i(TAG,"LL mData.mName = " + mData.mName);
		mName.setText(mData.mName);
		update();
	}
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG,"LL onTouchEvent()***");
		getItemData().onkeyDown(KeyEvent.KEYCODE_DPAD_CENTER, null);
		return super.onTouchEvent(event);
	}
	
	public void setTextGravity(int gravity){
		if(null != mName){
			mName.setGravity(gravity);
		}
	}
}