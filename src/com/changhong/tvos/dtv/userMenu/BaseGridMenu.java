package com.changhong.tvos.dtv.userMenu;

import java.util.List;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;
import com.changhong.tvos.dtv.AsyncImageLoader;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvDialogManager;
import com.changhong.tvos.dtv.util.PageIndexView;
import com.changhong.tvos.dtv.util.ViewChannelInfo;
import com.changhong.tvos.dtv.util.ViewGridBase;

public abstract class BaseGridMenu extends Dialog{
	protected static final String TAG = "BaseGridMenu";
	protected static final int SHOW_LAYOUT1 = 0;
	protected static final int SHOW_LAYOUT2 = 1;
	protected static final int SHOW_ALL = 2;
	private Animation out;
	private boolean hasDissMiss = true;
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		AsyncImageLoader.getInstance().clearCache();
		ViewChannelInfo.isNeedShow = true;
		if(null == out){
			out = AnimationUtils.loadAnimation(mContext, R.anim.zoom_exit);
			out.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub
					hasDissMiss = false;
				}
				
				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub
					Log.i(TAG, "onAnimationRepeat");
				}
				
				@Override
				public void onAnimationEnd(Animation arg0) {
					// TODO Auto-generated method stub
					hasDissMiss = true;
					BaseGridMenu.super.dismiss();
					try{
						Runtime.getRuntime().gc();
					}catch (Exception e){
						Log.i(TAG,"err Runtime gc " + e);
					}
				}
			});
		}
		if(isShowing() && null != filpper && hasDissMiss){
			filpper.startAnimation(out);
			Log.i(TAG, "dissmiss");
		}
		
	}

	protected static final long mShowTime = 30000;
	
	public Context mContext;
	public int witchViewShouldSee;
	private boolean isShowTV = false;
	public  ViewFlipper filpper;
	public RelativeLayout layout;
	public ImageView leftArrow ;
	public ImageView rightArrow ;
	public RelativeLayout layout2;

	MenuManager mMenuManager = MenuManager.getInstance();
	
	private ViewGridBase preShow ;
	private ViewGridBase nextShow ;
	public PageIndexView pageDot;
	
	public BaseGridMenu(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BaseGridMenu(Context context, int themeActivitytransparent) {
		super(context,R.style.Theme_ActivityTransparent);
		mContext = context;
	}

	public Runnable mRunShow = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			dismiss();
		}
	};
	
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		ViewChannelInfo.isNeedShow = false;
		DtvDialogManager.AddShowDialog(this);
		super.show();
		if(null != filpper){
			filpper.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.wave_scale));
		}
	}

	public void setWitchViewShow(int num){
		if(SHOW_LAYOUT1 == num){
			layout.setVisibility(View.VISIBLE);
			layout2.setVisibility(View.GONE);
			witchViewShouldSee = num;
			
		}else if(SHOW_LAYOUT2 == num){
			
			layout.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
			
			witchViewShouldSee = num;
		}else {
			layout.setVisibility(View.VISIBLE);
			layout2.setVisibility(View.VISIBLE);
		}
		
		
	}
	public int getWitchViewShouldSee() {
		return witchViewShouldSee;
	}
	
	public void slidShowNext(int direct){
		if(null == preShow || null == nextShow){
			return;
		}
		if(preShow.getMaxPage() < 1 || nextShow.getMaxPage() < 1){
			return;
		}
		preShow.onDestroy();
		preShow.setAnimationOk(false);
		nextShow.setChooseOneSwap(preShow.isChooseOneSwap());
		nextShow.setCurPage(preShow.getCurPage());
		nextShow.slideTurnPage(direct, preShow.getCurPosition());
		updatePageView();
	}
	
	public void showNext(int direct){
		
		if(null == preShow || null == nextShow){
			return;
		}
		if(preShow.getMaxPage() < 1 || nextShow.getMaxPage() < 1){
			return;
		}
		nextShow.setMaxPage(preShow.getMaxPage());
		
		int lastPage = preShow.getLastPage();
		nextShow.setChooseOneSwap(preShow.isChooseOneSwap());
		preShow.setAnimationOk(false);
		nextShow.setAnimationOk(false);
		preShow.onDestroy();
		nextShow.onDestroy();
		
		int curSelect = preShow.getSelectedItemPosition();
		if(-1 == curSelect){
			Log.i(TAG, "EL--> !! the preView has not selected !!" );
		}
		int nextPage = preShow.getCurPage();
		Log.i(TAG, "EL--> The curSelect  is position " + curSelect);
		
		switch (direct) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (nextPage == preShow.getMaxPage()){
				int count  = (getCurListSize() - preShow.getPageCounts() * nextPage);
				int line = count /  preShow.getColumns();
				if(line <= curSelect / preShow.getColumns() ){
					curSelect = count -1;
				}else {
					curSelect += (preShow.getColumns() -1);
				}
				Log.i(TAG, "EL--> last page and last line");
			}else {
				curSelect += preShow.getColumns() -1;
			}
			
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (nextPage == preShow.getMaxPage()){
				int count  = (getCurListSize() - preShow.getPageCounts() * nextPage);
				int line = count /  preShow.getColumns();
				if(line < curSelect / preShow.getColumns() ){
					curSelect = line *  preShow.getColumns();
				}else {
					curSelect -= (preShow.getColumns() -1);
				}
				Log.i(TAG, "EL--> last page and last line");
			}else if (lastPage == preShow.getMaxPage()) {
				curSelect = 0;
			}else{
				curSelect -= (preShow.getColumns() -1);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			if (nextPage == preShow.getMaxPage()){
				mMenuManager = MenuManager.getInstance();
				int count  = (getCurListSize() - preShow.getPageCounts() * nextPage);
				int line = count /  preShow.getColumns();
				if(count % preShow.getColumns() >= curSelect % preShow.getColumns()){
					curSelect = line * preShow.getColumns() + curSelect % preShow.getColumns();
				}else {
					curSelect = count -1;
				}
				Log.i(TAG, "EL--> last page and last line");
			}else{
				curSelect = (curSelect +(preShow.getLinage() -1) * preShow.getColumns()) % 
				preShow.getPageCounts();
			}
			
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if(nextPage == preShow.getMaxPage()){
				int count  = (getCurListSize() - preShow.getPageCounts() * nextPage);
				if(count % preShow.getColumns() >= curSelect % preShow.getColumns()){
					curSelect = curSelect % preShow.getColumns();
				}else {
					curSelect = count -1;
				}
			}else {
				curSelect = curSelect % preShow.getColumns();
			}
			break;
		default:
			break;
		}
		Log.i(TAG, "EL--> The nextSelect  is position " + curSelect);
		nextShow.setCurPageAndPosition(nextPage, curSelect);
		updatePageView();
	}

	public abstract void keyUpAction(int keyCode, KeyEvent event);
	public abstract void keyDownAction(int keyCode, KeyEvent event);
	public abstract void update();
	public void updatePageView(){
		if (null != pageDot) {
			pageDot.update(nextShow.getCurPage(), nextShow.getMaxPage()+1);
		}
	}

	public void setPreShowAndNextShow(ViewGridBase preShow, ViewGridBase nextShow) {
		this.preShow = preShow;
		this.nextShow = nextShow;
	}

	public ViewGridBase getPreShow() {
		return preShow;
	}

	public ViewGridBase getNextShow() {
		return nextShow;
	}


	public void setShowTV(boolean isShowTV) {
		this.isShowTV = isShowTV;
	}


	public boolean isShowTV() {
		return isShowTV;
	}
	
	public int getCurListSize(){
		mMenuManager = MenuManager.getInstance();

		return mMenuManager.getCurList().size(); 
	}
}
