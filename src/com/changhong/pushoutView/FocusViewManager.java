package com.changhong.pushoutView;

import com.changhong.tvos.dtv.R;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

public class FocusViewManager extends RelativeLayout {
	
	private static final String TAG = "DTVpushview";
	ValueAnimator mValueAnimator;
	Context context;
	float fromX;
	float fromY;
	float fromW;
	float fromH;

	float toX;
	float toY;
	float toW;
	float toH;

	View mView;
	LayoutParams mParams;
	
	int time = 400;

	private static FocusViewManager instance = null;
	private final int shade1 = 6;//hori
	private final int shade2 = 4;//vertical
	
	private final int KICKBACK_DISTANCE = 40;

	float mAnimatedValue;
	
	private OnFocusEntryAnimaDoneListener onFocusEntryAnimaDoneListener = null;
	private OnFocusExitAnimaDoneListener onFocusExitAnimaDoneListener = null;
	
	private Handler animaListenerHandler = new Handler();
	private boolean entryAnimaListenerMark = false;
	private boolean exitAnimaListenerMark = false;
	
	private boolean isShow = false;
	
	public interface OnFocusEntryAnimaDoneListener{
		
		public void OnFocusEntryAnimaDone();
	}
	
	public interface OnFocusExitAnimaDoneListener{
		
		public void OnFocusExitAnimaDone();
	}
	
	public void setOnFocusEntryAnimaDoneListener(OnFocusEntryAnimaDoneListener onFocusEntryAnimaDoneListener){
		
		this.onFocusEntryAnimaDoneListener = onFocusEntryAnimaDoneListener;
	}
	
	public void setOnFocusExitAnimaDoneListener(OnFocusExitAnimaDoneListener onFocusExitAnimaDoneListener){
		
		this.onFocusExitAnimaDoneListener = onFocusExitAnimaDoneListener;
	}
	
	public FocusViewManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		this.context = context;
		init();
	
	}
	
	public FocusViewManager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		this.context = context;
		init();
	}
	
	private void init(){
		
		setFocusable(false);
		setFocusableInTouchMode(false);
		
		mView = new RelativeLayout(context);
		mView.setBackgroundResource(R.drawable.chfocus);
				
		mParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//addView(mView, mParams);
				
		mValueAnimator =new ValueAnimator();

		mValueAnimator.setFloatValues(0.04f,1);
		mValueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				
				
			
				float value = (Float) animation.getAnimatedValue();
				
				if(mAnimatedValue ==value){
									
					return;
				}else{
					mAnimatedValue =value;
				}
				
				Log.v("scroll---1", ((Float) animation.getAnimatedValue()).toString());
				mParams.leftMargin = (int) (fromX - shade1 + (toX - fromX) * value);
				mParams.topMargin = (int) (fromY - shade2 + (toY - fromY) * value);
				mParams.width = (int) (fromW + 2*shade1 + (toW - fromW) * value);
				mParams.height = (int) (fromH + 2*shade2 + (toH - fromH) * value);
				mView.setLayoutParams(mParams);
				mView.bringToFront();							
					
			}
		});
		
		
		mValueAnimator.setDuration(time);
	}

	/*public static FocusViewManager getInstance(Context context){
		
		if(instance == null){
			
			instance = new FocusViewManager(context);
		}
		return instance;
		
	}*/
	
	public void setStartInfo(float x, float y, float w, float h) {
		fromX = x;
		fromY = y;
		fromW = w;
		fromH = h;
	}

	public void setEndInfo(float x, float y, float w, float h) {
		toX = x;
		toY = y;
		toW = w;
		toH = h;
	}

	public void setStartInfo(View v){
		
		if(v ==null){
			return;
		}
		int location[] =new int[2];
		
		v.getLocationOnScreen(location);
		setStartInfo(location[0], location[1], v.getWidth(), v.getHeight());
	}
	
	
	public void setEndInfo(View v){
		
		if(v ==null){
			return;
		}
		
		int location[] =new int[2];
		v.getLocationOnScreen(location);
		setEndInfo(location[0], location[1], v.getWidth(), v.getHeight());
	}
	

	public void prepare() {
		
		fromX = toX;
		fromY = toY;
		fromW = toW;
		fromH = toH;

	}
	
	public void showWindow() {
		
		MagicButtonCommon.print(false, "showWindow--------------1");
		if(toW == 0 || toH == 0){
			
			MagicButtonCommon.print(false, "showWindow--------------2");
			return;
		}
		mParams.leftMargin = (int) (toX) - shade1;
		mParams.topMargin = (int) (toY) - shade2;
		mParams.width = (int) (toW) + 2*shade1;
		mParams.height = (int) (toH) + 2*shade2;
		mView.setLayoutParams(mParams);
		showFoucsWindow();
	}
	
	private void playFocusEntryAnima(){
		
		Animation scaleAnimation = new ScaleAnimation(0.1f, 1.0f,0.1f,1.0f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
		Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		
		AnimationSet entrySet = new AnimationSet(true);
		
		entrySet.addAnimation(alphaAnimation);
		entrySet.addAnimation(scaleAnimation);
		entrySet.setInterpolator(new DecelerateInterpolator());
		
		entrySet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				
				MagicButtonCommon.print(false, "entrySet---onAnimationEnd");
				setFocusEntryAnimaDoneListener();
			}
		});
		
		entrySet.setDuration(450);
		entryAnimaListenerMark = false;
		mView.startAnimation(entrySet);
		
		animaListenerHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub				
				setFocusEntryAnimaDoneListener();		
			}
		}, 500);
	}
	
	private void setFocusEntryAnimaDoneListener(){
		
		MagicButtonCommon.print(false, "entrySet---onAnimationEnd");
		if(!entryAnimaListenerMark){
			
			entryAnimaListenerMark = true;
			if(onFocusEntryAnimaDoneListener != null){
				
				onFocusEntryAnimaDoneListener.OnFocusEntryAnimaDone();
			}
		}
		
	}
	
	private void setFocusExitAnimaDoneListener(){
		
		MagicButtonCommon.print(true, "exitSet---onAnimationEnd---0");
		if(!exitAnimaListenerMark){
			
			exitAnimaListenerMark = true;
			if(onFocusExitAnimaDoneListener != null){
				
				MagicButtonCommon.print(true, "exitSet---onAnimationEnd---1");
				onFocusExitAnimaDoneListener.OnFocusExitAnimaDone();
			}
		}
		
	}
	
	private void playFocusEntryAnima(Point startPoint){
		
		MagicButtonCommon.print(false, "playFocusEntryAnima-----x="+startPoint.x);
		MagicButtonCommon.print(false, "playFocusEntryAnima-----y="+startPoint.y);
		MagicButtonCommon.print(false, "playFocusEntryAnima-----t="+toX);
		MagicButtonCommon.print(false, "playFocusEntryAnima-----y="+toY);
		
		int deltafromY = (int)(startPoint.y - toY);
		int deltafromX = (int)(startPoint.x - toX);
		
		Animation translateAnimation = new TranslateAnimation(deltafromX, 0, deltafromY, 0);
		Animation scaleAnimation = new ScaleAnimation(0.1f, 1.0f,0.1f,1.0f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
		Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		
		AnimationSet entrySet = new AnimationSet(true);
		
		entrySet.addAnimation(alphaAnimation);
		entrySet.addAnimation(scaleAnimation);
		entrySet.addAnimation(translateAnimation);
		entrySet.setInterpolator(new DecelerateInterpolator());
		
		entrySet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				
				MagicButtonCommon.print(false, "entrySet---onAnimationEnd");
				if(onFocusEntryAnimaDoneListener != null){
					
					onFocusEntryAnimaDoneListener.OnFocusEntryAnimaDone();
				}
			}
		});
		
		entrySet.setDuration(300);
		mView.startAnimation(entrySet);
		
	} 
	
	private void playFocusExitAnima(){
		
		Animation scaleAnimation = new ScaleAnimation(1.0f, 0.1f,1.0f,0.1f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
		Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
		//Animation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
		
		AnimationSet exitSet = new AnimationSet(true);
		exitSet.addAnimation(alphaAnimation);
		//exitSet.addAnimation(rotateAnimation);
		exitSet.addAnimation(scaleAnimation);
		exitSet.setInterpolator(new AccelerateInterpolator());
		
		exitSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				
				MagicButtonCommon.print(true, "exitSet---onAnimationEnd");
				setFocusExitAnimaDoneListener();
			}
		});	
		
		
		exitSet.setDuration(400);
		exitAnimaListenerMark = false;
		mView.startAnimation(exitSet);
		
		animaListenerHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
							
				setFocusExitAnimaDoneListener();							
			}
		}, 700);
	}
	
	private void playFocusExitAnima(Point endPoint){
		
		MagicButtonCommon.print(false, "playFocusExitAnima-----y="+endPoint.y);
		MagicButtonCommon.print(false, "playFocusExitAnima-----fromY="+fromY);
		
		int deltaToY = (int)(endPoint.y - fromY);
		int deltaToX = (int)(endPoint.x - fromX);
		
		Animation translateAnimation = new TranslateAnimation(0, deltaToX, 0, deltaToY);
		Animation scaleAnimation = new ScaleAnimation(1.0f, 0.1f,1.0f,0.1f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
		Animation alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
		//Animation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
		
		AnimationSet exitSet = new AnimationSet(true);
		exitSet.addAnimation(alphaAnimation);
		//exitSet.addAnimation(rotateAnimation);
		exitSet.addAnimation(scaleAnimation);
		//exitSet.setInterpolator(new AccelerateInterpolator());
		exitSet.addAnimation(translateAnimation);
		
		
		
		exitSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				
				MagicButtonCommon.print(false, "exitSet---onAnimationEnd");
				if(onFocusExitAnimaDoneListener != null){
					
					onFocusExitAnimaDoneListener.OnFocusExitAnimaDone();
				}
			}
		});	
		
		
		exitSet.setDuration(450);
		mView.startAnimation(exitSet);
	} 
	
	public void kickBackAnima(boolean isForward){
		
		int deltafromX = 0;
		if(isForward){
			
			deltafromX = KICKBACK_DISTANCE;
		}else{
			
			deltafromX = -KICKBACK_DISTANCE;
		}
		
		mView.clearAnimation();
		Animation translateAnimation = new TranslateAnimation(0,deltafromX, 0, 0);
		translateAnimation.setDuration(300);
		mView.startAnimation(translateAnimation);
	}
	
	public void hideFocus(){
		
		mView.setVisibility(View.INVISIBLE);
	}
	
	public void showFocus(){
		
		mView.setVisibility(View.VISIBLE);
	}

	
	
	public void showFoucsWindow(){
		
		if(isShow ==false){
			
			addView(mView,mParams);
			isShow =true;
		}
	}
	
	public void hideWindow(){
		
		if(isShow ==true){
			
			removeView(mView);
			isShow =false;
		}
	}

	public boolean isShowWindow(){
		
		return isShow;
	}
	
	
	public void start(Point prePoint, View currentView ){
		
	
		setEndInfo(currentView);
		
		if(isShowWindow()){
			
			mValueAnimator.start();		
		}else{
			
			MagicButtonCommon.print(false, "initfocus--------------11");
			showWindow();	
			playFocusEntryAnima();
			//playFocusEntryAnima(prePoint);
		}
		
	}
	
	public void start(View preView, Point endPoint){
		
		MagicButtonCommon.print(false, "preView-------------="+preView);
		setStartInfo(preView);
			
		if(isShowWindow()){
			
			//playFocusExitAnima(endPoint);
			playFocusExitAnima();
		}
		
	}
	
	
	public void start(View preView, View currentView, int mode){
		
		setStartInfo(preView);
		setEndInfo(currentView);
			
		if(isShowWindow()){
			
			mValueAnimator.start();		
		}else{
			
			MagicButtonCommon.print(false, "initfocus--------------1");
			showWindow();
		}
		
	}
	
	public void start() {
		
		if(isShowWindow()){
			
			mValueAnimator.start();		
		}else{
			
			MagicButtonCommon.print(false, "initfocus--------------1");
			showWindow();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		MagicButtonCommon.print(false, "onTouchEvent--------------lllllll");
		hideWindow();
		return super.onTouchEvent(event);
	}

}
