package com.changhong.pushoutView;

import com.changhong.pushoutView.HorizontalView.OnChildColorEntryAnimDoneListener;
import com.changhong.pushoutView.HorizontalView.OnChildColorExitAnimDoneListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class HorizontalListView extends HorizontalListViewBase {
	
	private int mExitAnimNum = 0;
	private int mEntryAnimNum = 0;

	private Handler mHandler = new Handler();

	private OnChildColorEntryAnimDoneListener mOnChildColorEntryAnimDoneListener = new OnChildColorEntryAnimDoneListener() {
		
		@Override
		public void OnChildColorEntryAnimDone(int index) {
			// TODO Auto-generated method stub
			mEntryAnimNum++;
			if(mEntryAnimNum == mChildViewContainer.getChildCount()){

				mAnimDone = true;
				
				HorizontalView view = (HorizontalView)mChildViewContainer.getChildAt(0);
				mFocusBoxView.start(view.getPosterView());
				
				mEntryAnimNum = 0;
			}
		}
	};
	
	private OnChildColorExitAnimDoneListener mOnChildColorExitAnimaDoneListener = new OnChildColorExitAnimDoneListener() {
		
		@Override
		public void OnColorExitAnimDone(int index) {
			// TODO Auto-generated method stub
			mExitAnimNum++;
			
			if(mExitAnimNum == mChildViewContainer.getChildCount()){

				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (mOnExitDone != null)
							mOnExitDone.onExitDone();
					}
				});
				
				mExitAnimNum = 0;
				MagicButtonCommon.print(false, "OnNormalColorExitAnimaDoneListener-------1");
			}
		}
	};

	public HorizontalListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub		
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	public HorizontalListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub		
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
	public void playEntryAnima(){

		mAnimDone = false;
		for(RelativeLayout mRelativeLayout:getChildViewList()){
			
			((HorizontalView)mRelativeLayout).playEntryColorAnim();
		}
	}
	
	public void playExitAnima(){

		mAnimDone = false;
		for(RelativeLayout mRelativeLayout:getChildViewList()){
			
			((HorizontalView)mRelativeLayout).playExitColorAnim();
		}
	}
	
	public void hideChildViewOnScreen(){

		for(RelativeLayout mRelativeLayout : getChildViewList()){
	
			((HorizontalView)mRelativeLayout).hideViewIfOnScreen();				
		}
		
	}
	
	public void setPoster(int index, Bitmap poster){
		
		if(index < getChildViewList().size()){
			
			HorizontalView currentItemView = (HorizontalView)(getChildViewList().get(index));
			currentItemView.setPoster(poster);
		}
	}
	
//	public void setLabel(int index, int labelRid){
//		
//		if(index < getChildViewList().size()){
//			
//			HorizontalView currentItemView = (HorizontalView)(getChildViewList().get(index));
//			currentItemView.setLabel(labelRid);
//		}
//	}
	
	public void setTitle(int index, String title){
		
		if(getChildViewList().size() > index){
			
			HorizontalView currentItemView = (HorizontalView)(getChildViewList().get(index));
			currentItemView.setTitle(title);			
		}
	}

	@Override
	protected void initChildLayout(Context context) {
		// TODO Auto-generated method stub
		mChildViewContainer = new RelativeLayout(context);
		mChildViewContainer.setPadding(0, 0, 50, 0);
	}

	@Override
	protected void onLayoutSelf(RelativeLayout childView) {
		// TODO Auto-generated method stub
		int childCount = mChildViewContainer.getChildCount();
		
		//setup listener
		((HorizontalView)childView).setChildColorEntryAnimDoneListener(mOnChildColorEntryAnimDoneListener);
		((HorizontalView)childView).setChildColorExitAnimaDoneListener(mOnChildColorExitAnimaDoneListener);
		((HorizontalView)childView).setNormalItemIndex(childCount);

		//set LayoutParams and add view
		int widthSum = 0;
		for(int index = 0; index < childCount; index++){
			
			widthSum += mChildViewContainer.getChildAt(index).getLayoutParams().width;
		}

		RelativeLayout.LayoutParams childViewParams = (RelativeLayout.LayoutParams)childView.getLayoutParams();
		childViewParams.leftMargin = CHILD_VIEW_LEFT_BOUNDARY + widthSum + childCount * CHILD_VIEW_GAP_X;
		childViewParams.alignWithParent = true;
		childViewParams.addRule(RelativeLayout.CENTER_VERTICAL);

		mChildViewContainer.addView(childView, childViewParams);
		
		((HorizontalView)childView).setOnKeyDownListener(new HorizontalView.OnKeyDownListener() {
			
			@Override
			public boolean onKeyDown(int keyCode, KeyEvent event, View view) {
				// TODO Auto-generated method stub
				
				if (!mAnimDone)
					return true;
				
				switch (keyCode){
				
					case KeyEvent.KEYCODE_DPAD_RIGHT :{
						
						MagicButtonCommon.print(false, "onKeyDown-----------------KEYCODE_DPAD_RIGHT" );
						if (mFocusIndex < mChildViewContainer.getChildCount() - 1){
							
							mFocusIndex++;
							HorizontalView nexView = (HorizontalView)mChildViewContainer.getChildAt(mFocusIndex);
							mFocusBoxView.start(nexView.getPosterView());
							nexView.requestFocus();
						}
						
						if (mOnKeyDownListener != null){
							
							mOnKeyDownListener.onKeyDown(keyCode, event, view);
						
						}
						return true;
					}
					
					case KeyEvent.KEYCODE_DPAD_LEFT :{
						
						MagicButtonCommon.print(false, "onKeyDown-----------------KEYCODE_DPAD_LEFT" );
						if (mFocusIndex > 0){
							
							mFocusIndex--;
							HorizontalView nexView = (HorizontalView)mChildViewContainer.getChildAt(mFocusIndex);
							mFocusBoxView.start(nexView.getPosterView());
							nexView.requestFocus();
						}
						
						if (mOnKeyDownListener != null){
							
							mOnKeyDownListener.onKeyDown(keyCode, event, view);
						
						}
						return true;
					}
					
					default :
						
						MagicButtonCommon.print(false, "onKeyDown-----------------" );
//						removeFocusedView();
						if (mOnKeyDownListener != null){
							
							return mOnKeyDownListener.onKeyDown(keyCode, event, view);
						
						}else {
							MagicButtonCommon.print(false, "mOnKeyDownListener is null " );
						}
						
						return true;
					
				}
			}
		});
	}
	
	public void requestFocusExt(){
		mFocusIndex = 0;
		mChildViewContainer.requestFocus();
	}
}