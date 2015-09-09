package com.changhong.pushoutView;

import com.changhong.pushoutView.ColorAnimView.OnColorEntryAnimDoneHalfListener;
import com.changhong.pushoutView.ColorAnimView.OnColorEntryAnimDoneListener;
import com.changhong.pushoutView.ColorAnimView.OnColorEntryAnimaStartListener;
import com.changhong.pushoutView.ColorAnimView.OnColorExitAnimDoneListener;
import com.changhong.pushoutView.ColorAnimView.OnColorExitAnimStartListener;
import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HorizontalView extends RelativeLayout {

	private Context mContext = null;
	
	protected HorizontalViewContainer mHorizontalViewContainer = null;
	
	private int index = 0;	
	
	private OnChildColorEntryAnimDoneListener mChildColorEntryAnimDoneListener = null;
	private OnChildColorExitAnimDoneListener mChildColorExitAnimDoneListener = null;
	
	private final static int COLOR_ANIM_DELTA = 50;
	
	private final static int SCREEN_WIDTH = 1920;
	
	private boolean mHasTitle = false;
	    
    public interface OnChildColorEntryAnimDoneListener{
		
		public void OnChildColorEntryAnimDone(int index);
	}
    
    public interface  OnChildColorExitAnimDoneListener{
    	
    	public void OnColorExitAnimDone(int index);
    }
    
    private OnColorEntryAnimaStartListener mOnColorEntryAnimStartListener = new OnColorEntryAnimaStartListener() {
		
		@Override
		public void OnColorEntryAnimaStart() {
			// TODO Auto-generated method stub
			MagicButtonCommon.print(false, "OnColorEntryAnimStartListener-------- OnColorEntryAnimStart");
			show(false);
		}
	};
    
    private OnColorEntryAnimDoneHalfListener mOnColorEntryAnimDoneHalfListener = new OnColorEntryAnimDoneHalfListener() {
		
		@Override
		public void OnColorEntryAnimDoneHalf() {
			// TODO Auto-generated method stub
			MagicButtonCommon.print(false, "OnColorEntryAnimDoneHalfListener-------- OnColorEntryAnimDoneHalf");
			show(true);
		}
	};
	
	private OnColorEntryAnimDoneListener mOnColorEntryAnimDoneListener = new OnColorEntryAnimDoneListener() {
		
		@Override
		public void OnColorEntryAnimDone() {
			// TODO Auto-generated method stub
			MagicButtonCommon.print(false, "OnColorAnimaDoneListener--------index=="+index);
			
			if(mChildColorEntryAnimDoneListener != null){
				
				mChildColorEntryAnimDoneListener.OnChildColorEntryAnimDone(index);
			}		
		}
	};
	
	private OnColorExitAnimStartListener mOnColorExitAnimStartListener = new OnColorExitAnimStartListener() {
		
		@Override
		public void OnColorExitAnimStart() {
			// TODO Auto-generated method stub
			show(false);
		}
	};
	
	private OnColorExitAnimDoneListener mOnColorExitAnimDoneListener = new OnColorExitAnimDoneListener() {
		
		@Override
		public void OnColorExitAnimDone() {
			// TODO Auto-generated method stub
			if(mChildColorExitAnimDoneListener != null){
				
				mChildColorExitAnimDoneListener.OnColorExitAnimDone(index);
			}
		}
	};
	
	public interface OnKeyDownListener{
		
		public boolean onKeyDown(int keyCode, KeyEvent event, View view);
	}
	
	private OnKeyDownListener mOnKeyDownListener = null;
	public void setOnKeyDownListener(OnKeyDownListener onKeyDownListener){
		
		mOnKeyDownListener = onKeyDownListener;
	}

	public HorizontalView(Context context, int width, int height){
		super(context);	
		
		mContext = context;
		
		init(width, height);
	}
	
	private void init(int width, int height){
		
		setFocusable(true);	
		setFocusableInTouchMode(true);
		
		addView(LayoutInflater.from(mContext).inflate(R.layout.push_out_magic_button, null));
		
		mHorizontalViewContainer = new HorizontalViewContainer();
		
		mHorizontalViewContainer.mTitleView = (TextView)findViewById(R.id.info_title_component);	
		mHorizontalViewContainer.mPosterView = (ImageView)findViewById(R.id.image_poster_component_parent);	
		mHorizontalViewContainer.mColorAnimView = (ColorAnimView)findViewById(R.id.color_anima_component);
		mHorizontalViewContainer.mHalfView = (ImageView)findViewById(R.id.image_poster_component);
		
		
		mHorizontalViewContainer.mColorAnimView.setColorEntryAnimStartListener(mOnColorEntryAnimStartListener);
		mHorizontalViewContainer.mColorAnimView.setColorEntryAnimDoneHalfListener(mOnColorEntryAnimDoneHalfListener);
		mHorizontalViewContainer.mColorAnimView.setColorEntryAnimDoneListener(mOnColorEntryAnimDoneListener);
		mHorizontalViewContainer.mColorAnimView.setColorExitAnimStartListener(mOnColorExitAnimStartListener);
		mHorizontalViewContainer.mColorAnimView.setColorExitAnimDoneListener(mOnColorExitAnimDoneListener);

		//scroll text
		mHorizontalViewContainer.mTitleView.setBackgroundColor(Color.argb(0xCC, 0, 0, 0));		
		setLayoutParams(new RelativeLayout.LayoutParams(width, height + COLOR_ANIM_DELTA));
	}

	public void setNormalItemIndex(int index){
		
		this.index = index;		
		mHorizontalViewContainer.mColorAnimView.setColorViewIndex(index);
	}
	
	public int getNormalItemIndex(){
		
		return this.index;
	}
	
	public void setTitleBkColor(int bkColor){

		mHorizontalViewContainer.mTitleView.setBackgroundColor(bkColor);
	}
	
	public void setTitleColor(int color){
		
		mHorizontalViewContainer.mTitleView.setTextColor(color);
	}
	
	public void setTitle(String title){
		
		if(title == null){
			
			mHasTitle = false;
			mHorizontalViewContainer.mTitleView.setVisibility(INVISIBLE);
		}else{
			
			mHasTitle = true;
			mHorizontalViewContainer.mTitleView.setText(title);
		}
	}
	

	public void setPoster(Bitmap poster){
		
		mHorizontalViewContainer.mPosterView.setBackground(new BitmapDrawable(mContext.getResources(),poster));
	}
	
	public void setChildColorEntryAnimDoneListener(OnChildColorEntryAnimDoneListener childColorEntryAnimDoneListener){
		
		this.mChildColorEntryAnimDoneListener = childColorEntryAnimDoneListener;
	}
	
	public void setChildColorExitAnimaDoneListener(OnChildColorExitAnimDoneListener childColorExitAnimaDoneListener){
		
		this.mChildColorExitAnimDoneListener = childColorExitAnimaDoneListener;
	}
	
	public View getPosterView(){
		
		Log.d("DTVpushview=====================", "mHorizontalViewContainer.mPosterView with is " + mHorizontalViewContainer.mPosterView.getWidth()
				+ "mHorizontalViewContainer.mPosterView hight" + mHorizontalViewContainer.mPosterView.getHeight());
		Log.d("DTVpushview=====================", "mHorizontalViewContainer.mColorAnimView with is " + mHorizontalViewContainer.mColorAnimView.getWidth()
				+ "mHorizontalViewContainer.mColorAnimView hight" + mHorizontalViewContainer.mColorAnimView.getHeight());
		
		return mHorizontalViewContainer.mPosterView;
	}
	
	private void show(boolean show){

			
		if(show){
			
			mHorizontalViewContainer.mPosterView.setVisibility(VISIBLE);
			mHorizontalViewContainer.mHalfView.setVisibility(VISIBLE);
		}else{
			
			mHorizontalViewContainer.mPosterView.setVisibility(INVISIBLE);
			mHorizontalViewContainer.mHalfView.setVisibility(INVISIBLE);
		}
	}
	
	public void playHorseLamp(boolean isScroll){
			
		mHorizontalViewContainer.mTitleView.setSelected(isScroll);
	}
	
	public void playEntryColorAnim(){
		
		mHorizontalViewContainer.mColorAnimView.playEntryAnima();
	}
	
	public void playExitColorAnim(){
		
		MagicButtonCommon.print(false, "playExitColorAnim---1---");
		mHorizontalViewContainer.mColorAnimView.playExitAnim();
	}
	
	public void hideViewIfOnScreen(){
		
		int location[] = new int[2];
		getLocationOnScreen(location);
		
		boolean onScreen = false;
		
		int x = location[0];
		if(x <= SCREEN_WIDTH && x >= 0){
			
			onScreen = true;
		}else if(x < 0 && x + getWidth() > 0){
			
			onScreen = true;
		}
		
		if(onScreen){
			
			show(false);
		}
	}
	
	@Override
	protected void onLayout(boolean hasChange, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		MagicButtonCommon.print(false, "onLayout---color---1---");
		super.onLayout(hasChange,l, t, r, b);		
	}

	class HorizontalViewContainer{
	
		public ImageView mPosterView;
		public ImageView mHalfView;
		public TextView mTitleView;	
		public ColorAnimView mColorAnimView;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("DTVpushviewHorizontalView",  "HorizontalView on key in super" + event);
		if (mOnKeyDownListener != null)
			return mOnKeyDownListener.onKeyDown(keyCode, event, this);
		else 
			Log.d("DTVpushwarning", "the mOnKeyDownListener is null");
		
		return super.onKeyDown(keyCode, event);
	}
}