package com.changhong.tvos.dtv.menuManager;

import android.app.Dialog;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout.LayoutParams;

public class MenuMoveManager{
	protected static final String TAG = "MenuMoveManager";
	private Handler mHandler;
	private Runnable mRunnable;
	private View mView;
	private LayoutParams mViewLayoutParams;
	private int mViewStartleftMargin;
	private Dialog mDialog;
	private int mStartPos =0;
	private int mEndPos =1280;
	private boolean mToRight =true;
	Window mWindow;
	WindowManager.LayoutParams mDialogLayoutParams;
	public MenuMoveManager(){
		mHandler =new Handler();
		mRunnable =new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				if(mView !=null&&mView.isShown()){
//					Log.i(TAG,"LL mViewLayoutParams.leftMargin = " + mViewLayoutParams.leftMargin);
					if(mToRight == true){
						mViewLayoutParams.leftMargin +=10;
						
						if(mView.getWidth()!=0){
							
							if(mView.getWidth() +mViewLayoutParams.leftMargin >mEndPos){
								mViewLayoutParams.leftMargin =mEndPos -mView.getWidth(); 
								mToRight =false;
							}
						}else{
							mViewLayoutParams.leftMargin = mViewStartleftMargin;
							mToRight =true;
						}
					}else{
						mViewLayoutParams.leftMargin -=10;
						if(mViewLayoutParams.leftMargin <mStartPos){
							mViewLayoutParams.leftMargin =mStartPos;
							mToRight =true;
						}
					}
					mView.setLayoutParams(mViewLayoutParams);
				}else if(mDialog !=null&&mDialog.isShowing()){
					mDialogLayoutParams.x += 10;
					if(mDialogLayoutParams.width +mDialogLayoutParams.x >mEndPos/2){
						mDialogLayoutParams.x =mEndPos/2 -mDialogLayoutParams.width; 
						mToRight =false;
					}else{
						mDialogLayoutParams.x -=10;
						if(mDialogLayoutParams.x <-(mEndPos/2)){
							mDialogLayoutParams.x =-(mEndPos/2);
							mToRight =true;
						}
					}
					mWindow.setAttributes(mDialogLayoutParams);
				}
				
				mHandler.postDelayed(mRunnable, 1000);
			}
		};
	}
	
	public void init(View view){
		if(view != null){
			mView =view;
			mViewLayoutParams =(LayoutParams) mView.getLayoutParams();
			mViewStartleftMargin = mViewLayoutParams.leftMargin;
			mHandler.postDelayed(mRunnable, 1000);
		}
	}
	
	public void init(View view ,int startPos,int endPos){
		mStartPos =startPos;
		mEndPos =endPos;
		init(view);
	}
	public void init(Dialog dialog){
		if(dialog!=null){
			mDialog =dialog;
			mWindow = mDialog.getWindow();
			mDialogLayoutParams= mWindow.getAttributes();
			mHandler.postDelayed(mRunnable, 1000);
		}
	}
	public void init(Dialog dialog ,int startPos,int endPos){
		mStartPos =startPos;
		mEndPos =endPos;
		init(dialog);
	}
	public void setPositon(int xoff, int yoff) {
		Window window = mDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.x = xoff;
		lp.y = yoff;

		window.setAttributes(lp);
	}
}