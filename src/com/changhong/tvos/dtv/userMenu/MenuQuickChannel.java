package com.changhong.tvos.dtv.userMenu;

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
import com.changhong.tvos.dtv.tvap.DtvConfigManager;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstDefautUserValue;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstStringKey;
import com.changhong.tvos.dtv.util.PageIndexView;
import com.changhong.tvos.dtv.util.ViewGridBase.OnKeyActionCallBack;
import com.changhong.tvos.dtv.util.ViewQuickChannel;
import com.changhong.tvos.dtv.util.ViewQuickChannel.OnQuickChannelDissMissListener;
import com.changhong.tvos.dtv.util.ViewQuickChannel.OnQuickChannelLongPressListener;

public class MenuQuickChannel extends BaseGridMenu{

	public static String TAG ="MenuChannelSwap";
	
	ViewQuickChannel mQuickChannelView =null;
	private MenuManager.listState mListState =null;
	
	private ViewQuickChannel mQuickChannelView2 =null;
	
	private Handler mHandler = new Handler();
	
	public MenuQuickChannel(Context context) {
		// TODO Auto-generated constructor stub
		super(context,R.style.Theme_ActivityTransparent);
		
		setContentView(R.layout.menu_quick_channel);
		
		this.mListState =MenuManager.listState.channel_List;
		
		layout = (RelativeLayout)findViewById(R.id.mainLayout);
		layout2 = (RelativeLayout)findViewById(R.id.mainLayout2);
		
		filpper = (ViewFlipper) findViewById(R.id.fliperView);
		
		mQuickChannelView =(ViewQuickChannel) findViewById(R.id.quick_channel_grid_view);
		mQuickChannelView2 =(ViewQuickChannel) findViewById(R.id.quick_channel_grid_view2);
		
		mQuickChannelView.setOnQuickChannelLongPressListener(new OnQuickChannelLongPressListener() {

			@Override
			public boolean onQuickChannelLongPress() {
				// TODO Auto-generated method stub
				Log.i(TAG,"LL onQuickChannelLongPress***");
				mQuickChannelView.changeChannelListType();
				updatePageView();
				return true;
			}
		});
		mQuickChannelView2.setOnQuickChannelLongPressListener(new OnQuickChannelLongPressListener() {

			@Override
			public boolean onQuickChannelLongPress() {
				// TODO Auto-generated method stub
				Log.i(TAG,"LL onQuickChannelLongPress***");
				mQuickChannelView2.changeChannelListType();
				updatePageView();
				return true;
			}
		});
		
		mQuickChannelView.setKeyActionCallBack(new OnKeyActionCallBack() {
			
		
			@Override
			public void animationAction(int direct) {
				
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
								mQuickChannelView2.startChannelChange();
							}else if(SHOW_LAYOUT2  == witchViewShouldSee){
								setWitchViewShow(SHOW_LAYOUT1);
								mQuickChannelView.startChannelChange();
							}
							
							mQuickChannelView2.setAnimationOk(true);
							mQuickChannelView.setAnimationOk(true);
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
			public void resetTimer() {
				// TODO Auto-generated method stub
				mHandler.removeCallbacks(mRunShow);
				mHandler.postDelayed(mRunShow, mShowTime);
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
		mQuickChannelView2.setKeyActionCallBack(mQuickChannelView.getKeyActionCallBack());
		
		mQuickChannelView.setOnQuickChannelDissMissListener(new OnQuickChannelDissMissListener() {
			
			@Override
			public void disMissListener() {
				// TODO Auto-generated method stub
				MenuQuickChannel.this.dismiss();
			}
		});
		
		mQuickChannelView2.setOnQuickChannelDissMissListener(mQuickChannelView.getOnQuickChannelDissMissListener());
		
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
	}
	
	@Override
	public void update(){
		if(mQuickChannelView !=null && mQuickChannelView2 !=null){
			String tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_FAV_SORT);
			int curMode = tmp== null ? ConstDefautUserValue.FAV_SORT_DEFAULT_VALUE: Integer.valueOf(tmp);
			Log.i(TAG,"LL update()>>curMode = " + curMode);
			this.mListState =MenuManager.listState.channel_List;
			if(curMode == 0){
				this.mListState =MenuManager.listState.channel_WatchedList;
			}
/*			else if(curMode == 1){
				this.mListState =MenuManager.listState.channel_List;
			}else{
				this.mListState =MenuManager.listState.channel_List;
			}
*/			
			mQuickChannelView.init(mMenuManager,mListState);
			mQuickChannelView2.init(mMenuManager,mListState);
			this.updatePageView();
		}
	}
	

	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
			String tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_FAV_SORT);
		int curMode = tmp== null ? ConstDefautUserValue.FAV_SORT_DEFAULT_VALUE: Integer.valueOf(tmp);
		Log.i(TAG,"LL show()>>curMode = " + curMode);
		if(curMode == 0){
			this.mListState =MenuManager.listState.channel_WatchedList;
		}else if(curMode == 1){
			this.mListState =MenuManager.listState.channel_List;
		}else{
			this.mListState =MenuManager.listState.channel_List;
		}
		
		mMenuManager.init(mListState);
		
		if (getWitchViewShouldSee() == SHOW_LAYOUT1) {
			mQuickChannelView.init(mMenuManager,mListState);
			super.show();
			mQuickChannelView2.init(mMenuManager,mListState);
			
			setPreShowAndNextShow(mQuickChannelView2, mQuickChannelView);
			updatePageView();
		} else {
			
			mQuickChannelView2.init(mMenuManager,mListState);
			super.show();
			
			mQuickChannelView.init(mMenuManager,mListState);
			setPreShowAndNextShow(mQuickChannelView, mQuickChannelView2);
			updatePageView();
		}
	}
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		mQuickChannelView.onDestroy();
		mQuickChannelView2.onDestroy();
		mQuickChannelView.isResponseOnKeyUp = false;
		mQuickChannelView2.isResponseOnKeyUp = false;
		mHandler.removeCallbacks(mRunShow);
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
				if(mQuickChannelView.getCurPage()!= mQuickChannelView.getLastPage()){
					setPreShowAndNextShow(mQuickChannelView, mQuickChannelView2);
					showNext(keyCode);
				}
			}else {
				if(mQuickChannelView2.getCurPage()!= mQuickChannelView2.getLastPage()){
					setPreShowAndNextShow(mQuickChannelView2, mQuickChannelView);
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
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL:
			if(getWitchViewShouldSee() == SHOW_LAYOUT1){
				setPreShowAndNextShow(mQuickChannelView, mQuickChannelView2);
				slidShowNext(ViewQuickChannel.DIRECTION_LEFT);
			}else {
				setPreShowAndNextShow(mQuickChannelView2, mQuickChannelView);
				slidShowNext(ViewQuickChannel.DIRECTION_LEFT);
			}
			
			
			break;
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR:
			if(getWitchViewShouldSee() == SHOW_LAYOUT1){
				setPreShowAndNextShow(mQuickChannelView, mQuickChannelView2);
				slidShowNext(ViewQuickChannel.DIRECTION_RIGHT);
			}else {
				setPreShowAndNextShow(mQuickChannelView2, mQuickChannelView);
				slidShowNext(ViewQuickChannel.DIRECTION_RIGHT);
			}
			
			break;
		default:
			break;
			
		}
	}
}