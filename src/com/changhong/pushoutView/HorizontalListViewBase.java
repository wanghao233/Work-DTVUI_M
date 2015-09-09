package com.changhong.pushoutView;

import java.util.ArrayList;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

public class HorizontalListViewBase extends HorizontalScrollView {

	protected Context mContext;

	protected RelativeLayout mChildViewContainer = null;	
	
	private int mRightScrollDeltaX = 0;
	private int mLeftScrollDeltaX = 0;

	protected boolean mOnKeyUp = false;
	
	protected boolean mAnimDone = false;
	
	public final static int CHILD_VIEW_GAP_X = 10;
	public final static int CHILD_VIEW_LEFT_BOUNDARY = 10;
	
	protected int mFocusIndex = 0;
	
	public class Postion{
		
		public int x_postion;
		public int y_postion;
	}

	public interface OnKeyDownListener{
		
		public boolean onKeyDown(int keyCode, KeyEvent event, View focusedView);
	}
	
	protected OnKeyDownListener mOnKeyDownListener = null;
	public void setOnKeyDownListener(OnKeyDownListener onKeyDownListener){
		
		mOnKeyDownListener = onKeyDownListener;
	}
	
	protected FocusBoxView mFocusBoxView = null;
	public void setFocusBoxView(FocusBoxView focusBoxView){
		
		mFocusBoxView = focusBoxView;
		
		final HorizontalListView listView = (HorizontalListView)this;
		mFocusBoxView.setOnExitAnimDoneListener(new FocusBoxView.OnExitAnimDoneListener() {
			
			@Override
			public void onExitAnimDone() {
				// TODO Auto-generated method stub
				MagicButtonCommon.print(false, "--------OnFocusExitAnimaDone");
				// TODO Auto-generated method stub
				if (listView.mChildViewContainer.getChildCount() > 0){
					
					MagicButtonCommon.print(false, "--------OnFocusExitAnimaDone 1");
					listView.playExitAnima();
				}else{
					
					if (mOnExitDone != null){
						
						MagicButtonCommon.print(false, "--------OnFocusExitAnimaDone 2");
						mOnExitDone.onExitDone();
					}
				}

				mFocusBoxView.show(false);
			}
		});
	}
	
	public interface OnExitDone{
		
		public void onExitDone();
	}
	
	protected OnExitDone mOnExitDone = null;
	public void setOnExitDone(OnExitDone onExitDone){
		
		mOnExitDone = onExitDone;
	}

	public HorizontalListViewBase(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		
		init();
	}

	public HorizontalListViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		
		init();
	}
	
	/**
	 * add child view and data
	 * @param childView
	 */
	public void addChildView(RelativeLayout childView){
		
		onLayoutSelf(childView);		
	}
	
	public void removeFocusedView(){
		
		MagicButtonCommon.print(false, "removeDataSourceView-----------------1" );

		if (mFocusIndex < 0 || mFocusIndex > mChildViewContainer.getChildCount() - 1){
			
			return;
		}
		
		View focusedView = mChildViewContainer.getChildAt(mFocusIndex);

		if (mChildViewContainer.getChildCount() == 1){
			
			mFocusBoxView.playExitAnim();
		}else if (mChildViewContainer.getChildCount() > 1){
			
			if (mFocusIndex == mChildViewContainer.getChildCount() - 1){

				mFocusIndex--;	
				HorizontalView prevView = (HorizontalView)mChildViewContainer.getChildAt(mFocusIndex);
				mFocusBoxView.start(prevView.getPosterView());
			}else{
				
				for (int i = mFocusIndex + 1; i < mChildViewContainer.getChildCount(); i++){
					
					View childView = mChildViewContainer.getChildAt(i);
					if (childView != null){
						
						ObjectAnimator anim = ObjectAnimator.ofFloat(childView, "x", childView.getX(), 
								childView.getX() - focusedView.getWidth() - CHILD_VIEW_GAP_X);
						anim.setDuration(200);
						anim.start();
					}
				}
			}
		}
		mFocusIndex = 0;
		mChildViewContainer.removeView(focusedView);
	}
	
	public void setLeftScrollDeltaX(int leftDeltaX){
		
		mLeftScrollDeltaX = leftDeltaX;
	}
	
	public void setRightScrollDeltaX(int rightDeltaX){
		
		mRightScrollDeltaX = rightDeltaX;
	}
	
	/**
	 * child can override this method to custom a special layout
	 * @param context
	 */
	protected void initChildLayout(Context context){
		
		mChildViewContainer = new RelativeLayout(context);
		MagicButtonCommon.print(false, "CUKHoriScrollView-----init---1");
	}
	
	private void addChildView(){
			
		addView(mChildViewContainer);
	}
	
	private void init(){
		
		setHorizontalScrollBarEnabled(false);
		
		initChildLayout(mContext);
		
		addChildView();
	}

	/**
	 * child can override this method to custom a special layout
	 * @param childView
	 */
	protected void onLayoutSelf(RelativeLayout childView){
		
		mChildViewContainer.addView(childView);
	}

	public void clearAllView(){
		
		mChildViewContainer.removeAllViews();
		
//		mFocusView = null;
	}
	
	public RelativeLayout getChildView(int index){
		
		if(index < mChildViewContainer.getChildCount()){
			
			return (RelativeLayout)mChildViewContainer.getChildAt(index);
		}else{
			
			return null;
		}
	}	
	
	public ArrayList<RelativeLayout> getChildViewList(){
		
		ArrayList<RelativeLayout> childViewList = new ArrayList<RelativeLayout>();
		for (int i = 0; i < mChildViewContainer.getChildCount(); i++){
			
			RelativeLayout view = (RelativeLayout)mChildViewContainer.getChildAt(i);
			childViewList.add(view);
		}
		
		return childViewList;
	}

	public RelativeLayout getChildLayout(){
		
		return mChildViewContainer;
	}

	protected boolean hasOnFocus(){
		
		for(int i = 0; i < mChildViewContainer.getChildCount(); i++){
			
			View view = mChildViewContainer.getChildAt(i);
			if(view.hasFocus()){
				
				return true;
			}	
		}
		
		return false;
	}

	@Override
	protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
					
		MagicButtonCommon.print(false, "rect.right = " + rect.right);
		MagicButtonCommon.print(false, "rect.left = " + rect.left);
		MagicButtonCommon.print(false, "mRightScrollDeltaX = " + mRightScrollDeltaX);
		MagicButtonCommon.print(false, "mLeftScrollDeltaX = " + mLeftScrollDeltaX);
		rect.right += mRightScrollDeltaX;
		rect.left += mLeftScrollDeltaX;
		return super.computeScrollDeltaToGetChildRectOnScreen(rect);
	}
}