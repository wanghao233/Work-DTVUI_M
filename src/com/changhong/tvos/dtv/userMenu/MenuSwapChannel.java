package com.changhong.tvos.dtv.userMenu;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import com.changhong.tvos.dtv.util.PageIndexView;
import com.changhong.tvos.dtv.util.ViewBreathingLight;
import com.changhong.tvos.dtv.util.ViewGridBase.OnKeyActionCallBack;
import com.changhong.tvos.dtv.util.ViewSwapChannel;
import com.changhong.tvos.dtv.util.ViewSwapChannel.OnFlickerListener;

public class MenuSwapChannel extends BaseGridMenu{
	
	public static String TAG ="MenuChannelSwap";
	
	ViewBreathingLight mBreathView = null;
	ViewSwapChannel mViewSwapChannel =null;
	private MenuManager.listState mListState =null;
	private final int BREATH_GONE = 0; 
	private ViewSwapChannel mViewSwapChannel2 =null;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what == BREATH_GONE){
				mBreathView.setVisibility(View.GONE);
				mBreathView.stopFlicker();
			}
		}
		
	};
	
	public MenuSwapChannel(Context context) {
		// TODO Auto-generated constructor stub
		super(context,R.style.Theme_ActivityTransparent);
		mContext = context;
		
		setContentView(R.layout.menu_swap_channel);
		
		layout= (RelativeLayout)findViewById(R.id.mainLayout);
		layout2 = (RelativeLayout)findViewById(R.id.mainLayout2);
		mBreathView = (ViewBreathingLight)findViewById(R.id.viewBreath);
		
		mBreathView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(BREATH_GONE);
				if(SHOW_LAYOUT1 == witchViewShouldSee){
					mViewSwapChannel.setChooseOneSwap(false);
				}else{
					mViewSwapChannel2.setChooseOneSwap(false);
				}
			}
		});
		
		filpper = (ViewFlipper) findViewById(R.id.fliperView);
		
		this.mListState = MenuManager.listState.channel_Edit;
		
		mViewSwapChannel =(ViewSwapChannel) findViewById(R.id.swap_channel_grid_view);
		mViewSwapChannel2 =(ViewSwapChannel) findViewById(R.id.swap_channel_grid_view2);
		
		mViewSwapChannel.setKeyActionCallBack(new OnKeyActionCallBack() {
			
			@Override
			public void resetTimer() {
				// TODO Auto-generated method stub
				mHandler.removeCallbacks(mRunShow);
				mHandler.postDelayed(mRunShow, mShowTime);
			}
			
			@Override
			public void keyActionUp(int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				keyUpAction( keyCode,  event);
			}
			
			@Override
			public void keyActionDown(int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				keyDownAction( keyCode,  event);
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
								if(mViewSwapChannel2.findOneSwap){
									mViewSwapChannel2.startSwapFlicker();
								}
								mViewSwapChannel2.startChannelChange();
							}else if(SHOW_LAYOUT2  == witchViewShouldSee){
								setWitchViewShow(SHOW_LAYOUT1);
								if(mViewSwapChannel.findOneSwap){
									mViewSwapChannel.startSwapFlicker();
							}
								mViewSwapChannel.startChannelChange();
							}
							
							mViewSwapChannel.setAnimationOk(true);
							mViewSwapChannel2.setAnimationOk(true);
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
						mHandler.sendEmptyMessage(BREATH_GONE);
					}
				}, 150);
			}
		});
		
		mViewSwapChannel2.setKeyActionCallBack(mViewSwapChannel.getKeyActionCallBack());
		
	
		
		leftArrow = (ImageView)findViewById(R.id.page_pre);
		rightArrow = (ImageView)findViewById(R.id.page_next);
		pageDot = (PageIndexView)findViewById(R.id.page_dot);
		leftArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG,"LL leftArrow.onClick()***");
				mHandler.removeCallbacks(mRunShow);
				mHandler.postDelayed(mRunShow, mShowTime);
				keyUpAction( KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL, null);
			}
		});
		rightArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG,"LL rightArrow.onClick***");
				mHandler.removeCallbacks(mRunShow);
				mHandler.postDelayed(mRunShow, mShowTime);
				keyUpAction( KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR, null);
			}
		});
		
		setWitchViewShow(SHOW_LAYOUT1);
		
		mViewSwapChannel.setOnFlickerListener(new OnFlickerListener() {
			
			@Override
			public void stopFlicker() {
				// TODO Auto-generated method stub
				mBreathView.setVisibility(View.GONE);
				mBreathView.stopFlicker();
			}
			
			@Override
			public void startFlicker() {
				// TODO Auto-generated method stub
				mBreathView.setVisibility(ViewBreathingLight.VISIBLE);
				mBreathView.startFlicker();
			}
			
			@Override
			public void setFlickerPosition(int[] location) {
				// TODO Auto-generated method stub
				mBreathView.setLocation(location);
			}
		});
		mViewSwapChannel2.setOnFlickerListener(mViewSwapChannel.getOnFlickerListener());
		
	}
	
	@Override
	public void update(){
		if(mViewSwapChannel !=null && mViewSwapChannel2 != null){
			mViewSwapChannel.init(mMenuManager,mListState);
			mViewSwapChannel2.init(mMenuManager,mListState);
			updatePageView();
		}
	}
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
		mMenuManager.init(mListState);
		if (getWitchViewShouldSee() == SHOW_LAYOUT1) {
			mViewSwapChannel.init(mMenuManager,mListState);
			super.show();
			mViewSwapChannel2.init(mMenuManager,mListState);
			setPreShowAndNextShow(mViewSwapChannel2, mViewSwapChannel);
			this.updatePageView();
			
		} else {
			
			mViewSwapChannel2.init(mMenuManager,mListState);
			super.show();
			mViewSwapChannel.init(mMenuManager,mListState);
			setPreShowAndNextShow(mViewSwapChannel, mViewSwapChannel2);
			this.updatePageView();
		}
	}
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mRunShow);
		mViewSwapChannel.stopSwapFlicker();
		mViewSwapChannel2.stopSwapFlicker();
		
		mViewSwapChannel.onDestroy();
		mViewSwapChannel2.onDestroy();
		
		super.dismiss();
	}
	
	@Override
	public void keyDownAction(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v(TAG,"LL MenuSwapChannel>>onkeyDown>>keyCode = " + keyCode);
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
		switch (keyCode) {
		
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if(getWitchViewShouldSee() == SHOW_LAYOUT1){
				if(mViewSwapChannel.getCurPage()!= mViewSwapChannel.getLastPage()){
					setPreShowAndNextShow(mViewSwapChannel, mViewSwapChannel2);
					showNext(keyCode);
				}
			}else {
				if(mViewSwapChannel2.getCurPage()!= mViewSwapChannel2.getLastPage()){
					setPreShowAndNextShow(mViewSwapChannel2, mViewSwapChannel);
					showNext(keyCode);
				}
			}
			
			break;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:		
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
	@Override
	public void keyUpAction(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_GREEN:
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL:
			if(getWitchViewShouldSee() == SHOW_LAYOUT1){
				setPreShowAndNextShow(mViewSwapChannel, mViewSwapChannel2);
				slidShowNext(ViewSwapChannel.DIRECTION_LEFT);
			}else {
				setPreShowAndNextShow(mViewSwapChannel2, mViewSwapChannel);
				slidShowNext(ViewSwapChannel.DIRECTION_LEFT);
			}
			
			
			break;
		case KeyEvent.KEYCODE_YELLOW:
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR:
			if(getWitchViewShouldSee() == SHOW_LAYOUT1){
				setPreShowAndNextShow(mViewSwapChannel, mViewSwapChannel2);
				slidShowNext(ViewSwapChannel.DIRECTION_RIGHT);
			}else {
				setPreShowAndNextShow(mViewSwapChannel2, mViewSwapChannel);
				slidShowNext(ViewSwapChannel.DIRECTION_RIGHT);
			}
			
			break;
		default:
			break;
			
		}
	}
	
}
	
	