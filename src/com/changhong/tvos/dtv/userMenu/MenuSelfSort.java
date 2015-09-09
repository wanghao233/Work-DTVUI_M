package com.changhong.tvos.dtv.userMenu;

import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
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
import com.changhong.tvos.dtv.util.ViewGridBase.OnKeyActionCallBack;
import com.changhong.tvos.dtv.util.ViewSelfOrder;
import com.changhong.tvos.dtv.util.ViewSelfOrder.InputShowListner;
import com.changhong.tvos.system.commondialog.VchCommonToastDialog;

public class MenuSelfSort extends BaseGridMenu {

public static String TAG ="MenuSkipChannelDialog";
	
	private ViewSelfOrder mSelfOrderlView;
	private MenuManager mMenuManager = MenuManager.getInstance();;
	
	private MenuManager.listState mListState =null;
	
	private ViewSelfOrder mSelfOrderlView2;
	
	private Handler mHandler = new Handler();
	
//	private CommonInfoDialog myDialog;
	private VchCommonToastDialog myDialog;
	private int dialog_height = 70;
	private int dialog_width = 520;
	private int dialog_margin = 480;
	private int dialog_margin_y = 30;
	
	public MenuSelfSort(Context context) {
		// TODO Auto-generated constructor stub
		super(context,R.style.Theme_ActivityTransparent);
		
		mContext = context;
		DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();	
		dialog_width = (int) (dialog_width * mDisplayMetrics.scaledDensity);
		dialog_height = (int) (dialog_height * mDisplayMetrics.scaledDensity);
		
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
		
		setContentView(R.layout.menu_self_order);
		
		layout= (RelativeLayout)findViewById(R.id.mainLayout);
		layout2 = (RelativeLayout)findViewById(R.id.mainLayout2);
		this.mListState =MenuManager.listState.channel_Edit;
		filpper = (ViewFlipper) findViewById(R.id.fliperView);
		
		mSelfOrderlView =(ViewSelfOrder) findViewById(R.id.self_sort_grid_view);
		mSelfOrderlView2  =(ViewSelfOrder) findViewById(R.id.self_sort_grid_view2);
		mSelfOrderlView.setKeyActionCallBack(new OnKeyActionCallBack(){
			
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
								mSelfOrderlView2.startChannelChange();
								
							}else if(SHOW_LAYOUT2  == witchViewShouldSee){
								
								setWitchViewShow(SHOW_LAYOUT1);
								mSelfOrderlView.startChannelChange();
								
							}
							
							mSelfOrderlView2.setAnimationOk(true);
							mSelfOrderlView.setAnimationOk(true);
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
		
		mSelfOrderlView2.setKeyActionCallBack(mSelfOrderlView.getKeyActionCallBack());
		
		mSelfOrderlView.setInputShowListner(new InputShowListner() {
			
			@Override
			public void showMenu() {
				// TODO Auto-generated method stub
//				setShowTV(false);
//				show();
			}
			
			@Override
			public void hideMenu() {
				// TODO Auto-generated method stub
//				setShowTV(true);
//				dismiss();
			}
		});
		mSelfOrderlView2.setInputShowListner(mSelfOrderlView.getInputShowListner());
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
		if(mSelfOrderlView != null && mSelfOrderlView2 != null){
			mSelfOrderlView.init(mMenuManager,mListState);
			mSelfOrderlView2.init(mMenuManager,mListState);
			this.updatePageView();
		}
	}
	
	
	@Override
	public void show() {
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);

		
		if (getWitchViewShouldSee() == SHOW_LAYOUT1) {
			
			mSelfOrderlView.init(mMenuManager,mListState);
			mSelfOrderlView.requestFocus();
			super.show();
			mSelfOrderlView2.init(mMenuManager,mListState);
			setPreShowAndNextShow(mSelfOrderlView2, mSelfOrderlView);
			this.updatePageView();			
		} else {
			
			mSelfOrderlView2.init(mMenuManager,mListState);
			super.show();
			mSelfOrderlView.init(mMenuManager,mListState);
			setPreShowAndNextShow(mSelfOrderlView, mSelfOrderlView2);
			this.updatePageView();
			mSelfOrderlView2.requestFocus();
		}
	}
	


	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		mSelfOrderlView.onDestroy();
		mSelfOrderlView2.onDestroy();
		mHandler.removeCallbacks(mRunShow);
		super.dismiss();
		if(mSelfOrderlView.hasChanged){
			showSavedInfo();
		}
		mSelfOrderlView.setChangedList();
	}

	private void showSavedInfo() {
		// TODO Auto-generated method stub
		if(null == myDialog){
//			myDialog = new CommonInfoDialog(mContext);
//			myDialog.setGravity(Gravity.BOTTOM|Gravity.LEFT, dialog_margin, dialog_margin_y);
//			myDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
//			myDialog.info_layout.setLayoutParams(new FrameLayout.LayoutParams(dialog_width,dialog_height));
//			myDialog.tv.setTextColor(Color.WHITE);
//			myDialog.tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f);
//			myDialog.setMessage(R.string.dtv_menu_saved_info);
			myDialog = new VchCommonToastDialog(mContext);
			myDialog.getWindow().setType(2003);
			myDialog.setMessage(R.string.dtv_menu_saved_info);
			myDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
		}
		myDialog.show();
	}

	@Override
	public void keyUpAction(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_GREEN:
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL:
			if(getWitchViewShouldSee() == SHOW_LAYOUT1){
				setPreShowAndNextShow(mSelfOrderlView, mSelfOrderlView2);
				slidShowNext(ViewSelfOrder.DIRECTION_LEFT);
			}else {
				setPreShowAndNextShow(mSelfOrderlView2, mSelfOrderlView);
				slidShowNext(ViewSelfOrder.DIRECTION_LEFT);
			}
			
			
			break;
		case KeyEvent.KEYCODE_YELLOW:
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR:
			if(getWitchViewShouldSee() == SHOW_LAYOUT1){
				setPreShowAndNextShow(mSelfOrderlView, mSelfOrderlView2);
				slidShowNext(ViewSelfOrder.DIRECTION_RIGHT);
			}else {
				setPreShowAndNextShow(mSelfOrderlView2, mSelfOrderlView);
				slidShowNext(ViewSelfOrder.DIRECTION_RIGHT);
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
				if(mSelfOrderlView.getCurPage()!= mSelfOrderlView.getLastPage()){
					setPreShowAndNextShow(mSelfOrderlView, mSelfOrderlView2);
					showNext(keyCode);
				}
			}else {
				if(mSelfOrderlView2.getCurPage()!= mSelfOrderlView2.getLastPage()){
					setPreShowAndNextShow(mSelfOrderlView2, mSelfOrderlView);
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
