package com.changhong.tvos.dtv.channel_manager;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.userMenu.BaseGridMenu;
import com.changhong.tvos.dtv.util.PageIndexView;
import com.changhong.tvos.dtv.util.ViewGridBase.OnKeyActionCallBack;

public class MenuChannelOnOffDialog extends BaseGridMenu{
	
	public static String TAG ="MenuSkipChannelDialog";
	
	ViewChannelOnOffGrid mOnOrOffChannelView;
	MenuManager mMenuManager = MenuManager.getInstance();;
	
	private MenuManager.listState mListState =null;
	
	private ViewChannelOnOffGrid mOnOrOffChannelView2;
	
	private Handler mHandler = new Handler();
	
	public MenuChannelOnOffDialog(Context context) {
		// TODO Auto-generated constructor stub
		super(context,R.style.Theme_ActivityTransparent);
		mContext = context;
		
		setContentView(R.layout.menu_chanenl_open_off);
		
		layout= (RelativeLayout)findViewById(R.id.mainLayout);
		layout2 = (RelativeLayout)findViewById(R.id.mainLayout2);
		this.mListState =MenuManager.listState.channel_Edit;
		filpper = (ViewFlipper) findViewById(R.id.fliperView);
		mOnOrOffChannelView =(ViewChannelOnOffGrid) findViewById(R.id.channel_manager_grid_view);
		mOnOrOffChannelView2  =(ViewChannelOnOffGrid) findViewById(R.id.channel_manager_grid_view2);
		mOnOrOffChannelView.setKeyActionCallBack(new OnKeyActionCallBack(){
			
			@Override
			public void resetTimer() {
				
				mHandler.removeCallbacks(mRunShow);
				mHandler.postDelayed(mRunShow, mShowTime);
			}
			

			@Override
			public void animationAction(int direct) {
				// TODO Auto-generated method stub
				Animation inAnimation = null;
				switch (direct) {
				case KeyEvent.KEYCODE_DPAD_LEFT:
					filpper.setInAnimation(mContext, R.anim.anim_right_in);
					filpper.setOutAnimation(mContext, R.anim.anim_left_out);
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					filpper.setInAnimation(mContext, R.anim.anim_left_in);
					filpper.setOutAnimation(mContext, R.anim.anim_right_out);
					break;
				case KeyEvent.KEYCODE_DPAD_UP:
					filpper.setInAnimation(mContext, R.anim.anim_down_in);
					filpper.setOutAnimation(mContext, R.anim.anim_up_out);
					break;
				case KeyEvent.KEYCODE_DPAD_DOWN:
					filpper.setInAnimation(mContext, R.anim.anim_up_in);
					filpper.setOutAnimation(mContext, R.anim.anim_down_out);
					
					break;
				default: 
					break;
				}
				
				inAnimation = filpper.getInAnimation();
				if(null != inAnimation){
					inAnimation.setAnimationListener(new AnimationListener(){

						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							if(SHOW_LAYOUT1 == witchViewShouldSee){
								
								setWitchViewShow(SHOW_LAYOUT2);
								mOnOrOffChannelView2.startChannelChange();
								
							}else if(SHOW_LAYOUT2  == witchViewShouldSee){
								
								setWitchViewShow(SHOW_LAYOUT1);
								mOnOrOffChannelView.startChannelChange();
								
							}
							
							mOnOrOffChannelView2.setAnimationOk(true);
							mOnOrOffChannelView.setAnimationOk(true);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
							if(SHOW_LAYOUT1 == witchViewShouldSee){
								layout2.requestFocus();
							}else if(SHOW_LAYOUT2  == witchViewShouldSee){
								layout.requestFocus();
							}
							setWitchViewShow(SHOW_ALL);
						}
						
					});
				}else {
					Log.i(TAG, "The out animation is null");
				}
				
				mHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						filpper.showNext();
					}
				}, 150);
			}


			@Override
			public void keyActionUp(int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				keyUpAction(keyCode, event);
			}

			@Override
			public void keyActionDown(int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				keyDownAction(keyCode, event);
			}
			
		});
		
		mOnOrOffChannelView2.setKeyActionCallBack(mOnOrOffChannelView.getKeyActionCallBack());
		
		leftArrow = (ImageView)findViewById(R.id.page_pre);
		rightArrow = (ImageView)findViewById(R.id.page_next);
		pageDot = (PageIndexView)findViewById(R.id.page_dot);	
		
		leftArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v.getId() == leftArrow.getId()){
					mHandler.removeCallbacks(mRunShow);
					mHandler.postDelayed(mRunShow, mShowTime);
					Log.i(TAG,"EL leftArrow.onClick()***");
					keyUpAction( KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL, null);
				}
			}
		});
		
		rightArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TAG,"EL leftArrow.onClick()***");
				mHandler.removeCallbacks(mRunShow);
				mHandler.postDelayed(mRunShow, mShowTime);
				keyUpAction( KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR, null);
			}
		});
		setWitchViewShow(SHOW_LAYOUT1);
	}
	
	@Override
	public void update(){
		if(mOnOrOffChannelView != null && mOnOrOffChannelView2 != null){
			mOnOrOffChannelView.init(mMenuManager,mListState);
			mOnOrOffChannelView2.init(mMenuManager,mListState);
			this.updatePageView();
		}
	}
	
	
	@Override
	public void show() {
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);

		mMenuManager.init(mListState);
		if (getWitchViewShouldSee() == SHOW_LAYOUT1) {
			
			mOnOrOffChannelView.init(mMenuManager,mListState);
			super.show();
			mOnOrOffChannelView2.init(mMenuManager,mListState);
			setPreShowAndNextShow(mOnOrOffChannelView2, mOnOrOffChannelView);
			this.updatePageView();			
		} else {
			
			mOnOrOffChannelView2.init(mMenuManager,mListState);
			super.show();
			mOnOrOffChannelView.init(mMenuManager,mListState);
			setPreShowAndNextShow(mOnOrOffChannelView, mOnOrOffChannelView2);
			this.updatePageView();
		}
	}
	


	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		mOnOrOffChannelView.onDestroy();
		mOnOrOffChannelView2.onDestroy();
		mHandler.removeCallbacks(mRunShow);
		
		if(null != mOnOrOffChannelView.popupWindow){
			mOnOrOffChannelView.popupWindow.dismiss();
		}
		if(null != mOnOrOffChannelView2.popupWindow){
			mOnOrOffChannelView2.popupWindow.dismiss();
		}
		
		super.dismiss();
		
	}

	@Override
	public void keyUpAction(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_GREEN:
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL:
			if(getWitchViewShouldSee() == SHOW_LAYOUT1){
				setPreShowAndNextShow(mOnOrOffChannelView, mOnOrOffChannelView2);
				slidShowNext(ViewChannelOnOffGrid.DIRECTION_LEFT);
			}else {
				setPreShowAndNextShow(mOnOrOffChannelView2, mOnOrOffChannelView);
				slidShowNext(ViewChannelOnOffGrid.DIRECTION_LEFT);
			}
			
			break;
			
		case KeyEvent.KEYCODE_YELLOW:
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR:
			if(getWitchViewShouldSee() == SHOW_LAYOUT1){
				setPreShowAndNextShow(mOnOrOffChannelView, mOnOrOffChannelView2);
				slidShowNext(ViewChannelOnOffGrid.DIRECTION_RIGHT);
			}else {
				setPreShowAndNextShow(mOnOrOffChannelView2, mOnOrOffChannelView);
				slidShowNext(ViewChannelOnOffGrid.DIRECTION_RIGHT);
			}
			
			break;
		default:
			break;
			
		}
	}
	@Override
	public void keyDownAction(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
		switch (keyCode) {
			
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if(getWitchViewShouldSee() == SHOW_LAYOUT1){
				if(mOnOrOffChannelView.getCurPage()!= mOnOrOffChannelView.getLastPage()){
					setPreShowAndNextShow(mOnOrOffChannelView, mOnOrOffChannelView2);
					showNext(keyCode);
				}
			}else {
				if(mOnOrOffChannelView2.getCurPage()!= mOnOrOffChannelView2.getLastPage()){
					setPreShowAndNextShow(mOnOrOffChannelView2, mOnOrOffChannelView);
					showNext(keyCode);
				}
			}
			
			break;
		case KeyEvent.KEYCODE_SOURCE:
//		case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBAR:
		case 4126://KEYCODE_CHANGHONGIR_TOOLBAR
		case 170://KEYCODE_CHANGHONGIR_TV
		case 4135:
			setShowTV(true);
		case KeyEvent.KEYCODE_MENU:
		case KeyEvent.KEYCODE_BACK:
			dismiss();
			break;
		default:
			break;
		}
	}

}
