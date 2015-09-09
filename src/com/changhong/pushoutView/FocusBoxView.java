package com.changhong.pushoutView;


import com.changhong.tvos.dtv.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.widget.RelativeLayout;

/**
 * Base class, you can derive child class from this.
 * Api : 
 * 1. Constructor
 * 2. void start(View toView)/void start(FocusBoxParam toParam) : switch focus box
 * 3. void show(boolean show) : show or hide focus box
 * 4. set listener: entry animation and exit animation listener
 * 5. set listener: focus change
 * 6. setFocusBoxImage
 */
public class FocusBoxView extends RelativeLayout {

	private Context mContext;

	//the focus view and the param
	private View mFocusBoxView;
	private LayoutParams mFocusBoxParams;

	//focus box starting position and size	
	private FocusBoxParam mFromParam = new FocusBoxParam();
	//focus box end position and size	
	private FocusBoxParam mToParam = new FocusBoxParam();
	//cur focus box end position and size	
	private FocusBoxParam mcurParam = new FocusBoxParam();

	protected int mPaddingX = 4;//horizon
	protected int mPaddingY = 4;//vertical
	
	private boolean mShow = false;
	
	//Entry Animation Done Listener
	public interface OnEntryAnimDoneListener{
		
		public void onEntryAnimDone(); 
	}
	protected OnEntryAnimDoneListener mOnEntryAnimDoneListener;
	public void OnEntryAnimDoneListener(OnEntryAnimDoneListener listener){
		
		mOnEntryAnimDoneListener = listener;
	}
	
	//Exit Animation Done Listener
	public interface OnExitAnimDoneListener{
		
		public void onExitAnimDone(); 
	}
	protected OnExitAnimDoneListener mOnExitAnimDoneListener;
	public void setOnExitAnimDoneListener(OnExitAnimDoneListener listener){
		
		mOnExitAnimDoneListener = listener;
	}
	
	//Focus Change Animation Done Listener
	public interface OnFocusChangeAnimDoneListener{
		
		public void onFocusChangeAnimDone(); 
	}
	protected OnFocusChangeAnimDoneListener mOnFocusChangeAnimDoneListener;
	public void setOnFocusChangeAnimDoneListener(OnFocusChangeAnimDoneListener listener){
		
		mOnFocusChangeAnimDoneListener = listener;
	}

	public FocusBoxView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}
	
	public FocusBoxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}
	
	public void start(View toView){
		
		start(null, toView);
	}
	
	public void start(FocusBoxParam toParam){
		
		start(null, toParam);
	}

	public void show(boolean show){
		
		if (show){
			
			if(mShow == false){
				
				MagicButtonCommon.print(false, "showFoucsWindow--------------1");
				addView(mFocusBoxView, mFocusBoxParams);
				
				//bring the focus box view to top
				ViewParent parent = getParent();
				if (parent != null){
					
					parent.bringChildToFront(this);
				}
				
				mShow = true;
			}
		}else{
			
			if(mShow == true){
				
				removeView(mFocusBoxView);
				mShow = false;
			}	
		}
	}

	/*
	 * Derived class can override this method to customize its own entry animation
	 * Note : don't call super.playEntryAnim()
	 */
	protected void playEntryAnim(){

		if (mOnEntryAnimDoneListener != null)
			mOnEntryAnimDoneListener.onEntryAnimDone();
	}
	
	/*
	 * Derived class can override this method to customize its own exit animation
	 * Note : don't call super.playExitAnim()
	 */
	public void playExitAnim(){

		if (mOnExitAnimDoneListener != null)
			mOnEntryAnimDoneListener.onEntryAnimDone();
	}
	
	/*
	 * Derived class can override this method to customize its own focus change animation
	 * Note : don't call super.playFocusChangeAnim()
	 */
	protected void playFocusChangeAnim(){
		
		setParam(mToParam);
		
		mFocusBoxView.bringToFront();
		
		if (mOnFocusChangeAnimDoneListener != null)
			mOnFocusChangeAnimDoneListener.onFocusChangeAnimDone();
	}
	
	/*
	 * Derived class can override this method to customize its own rebound animation
	 * Note : don't call super.playReboundAnim()
	 */
	public void playReboundAnim(boolean isForward){

	}
	
	public void setFocusBoxImage(int resId){
		
		mFocusBoxView.setBackgroundResource(resId);
	}
	
	protected View getFocusBoxView(){
		
		return mFocusBoxView;
	}
	
	protected FocusBoxParam getFocusBoxFromParam(){
		
		return mFromParam;
	}
	
	protected FocusBoxParam getFocusBoxToParam(){
		
		return mToParam;
	}
	
	protected FocusBoxParam getFocusBoxCurraram(){
		
		return mcurParam;
	}
	
	protected void setFocusBoxLayoutParam(int leftMargin, int topMargin, int width, int height){
		
		mFocusBoxParams.leftMargin = leftMargin;
		mFocusBoxParams.topMargin = topMargin;
		mFocusBoxParams.width = width;
		mFocusBoxParams.height = height;
	}

	private void init(){
		
		setFocusable(false);
		setFocusableInTouchMode(false);
		
		mFocusBoxView = new RelativeLayout(mContext);
		
		setFocusBoxImage(R.drawable.chfocus);
				
		mFocusBoxParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	/**
	 * set the focus box to toView from fromView with animation
	 * @param fromView
	 * @param toView
	 */
	private void start(View fromView, View toView){
		
		setStartInfo(fromView);
		setEndInfo(toView);
			
		if(isShow()){
			
			MagicButtonCommon.print(false, "start--------------1");	
			//start
			playFocusChangeAnim();
		}else{
			
			MagicButtonCommon.print(false, "start--------------2");
			initFocusBoxView();
		}
	}

	/**
	 * set the focus box to toParam from fromParam with animation
	 * @param fromParam
	 * @param toParam
	 */
	private void start(FocusBoxParam fromParam, FocusBoxParam toParam){
		
		setStartInfo(fromParam);
		setEndInfo(toParam);
			
		if(isShow()){
			
			MagicButtonCommon.print(false, "start--------------1");
			playFocusChangeAnim();
		}else{
			
			MagicButtonCommon.print(false, "start--------------2");
			initFocusBoxView();
		}
	}
	
	private int getTitleBarHeight(){  

		if (mContext instanceof Activity){
			
			Window window = ((Activity)mContext).getWindow();         
			
			//标题栏跟状�?栏的总体高度  
	        return window.findViewById(Window.ID_ANDROID_CONTENT).getTop();   	
		}

		return 0;
    } 
	
	private void setStartInfo(FocusBoxParam param) {
		
		if (param != null){
			
			mFromParam.mX = param.mX;
			mFromParam.mY = param.mY - getTitleBarHeight();
			mFromParam.mW = param.mW;
			mFromParam.mH = param.mH;
		}else{
			
			mFromParam.mX = mcurParam.mX;
			mFromParam.mY = mcurParam.mY - getTitleBarHeight();
			mFromParam.mW = mcurParam.mW;
			mFromParam.mH = mcurParam.mH;
		}
	}

	public void setEndInfo(FocusBoxParam param) {
		
		if (param != null){
			
			mToParam.mX = param.mX;
			mToParam.mY = param.mY - getTitleBarHeight();
			mToParam.mW = param.mW;
			mToParam.mH = param.mH;	
		}
	}

	private void setStartInfo(View v){
		
		if(v != null){
			
			int location[] = new int[2];
			v.getLocationOnScreen(location);
			
			setStartInfo(new FocusBoxParam(location[0], location[1], v.getWidth(), v.getHeight()));	
		}else{
			
			setStartInfo(mcurParam);
		}
	}

	private void setEndInfo(View v){
		
		if(v != null){
		
			int location[] = new int[2];
			v.getLocationOnScreen(location);
			
			MagicButtonCommon.print(false, "setEndInfo--------------location[0] = " + location[0]);
			MagicButtonCommon.print(false, "setEndInfo--------------location[1] = " + location[1]);
			
			mcurParam.mX = location[0];
			mcurParam.mY = location[1];
			mcurParam.mW = v.getWidth();
			mcurParam.mH = v.getHeight();
			
			setEndInfo(mcurParam);
		}
	}
	
	private void initFocusBoxView() {
		
		MagicButtonCommon.print(false, "showWindow--------------1");
		if(mToParam.mW == 0 || mToParam.mW == 0){
			
			MagicButtonCommon.print(false, "showWindow--------------2");
			return;
		}
		
		setParam(mToParam);

		MagicButtonCommon.print(false, "showWindow--------------3 mParams.leftMargin = " + mFocusBoxParams.leftMargin);
		MagicButtonCommon.print(false, "showWindow--------------3 mParams.topMargin = " + mFocusBoxParams.topMargin);
		
		show(true); 

		//play entry animation
		playEntryAnim();
	}
	
	private boolean isShow(){
		
		return mShow;
	}
	
	private void setParam(FocusBoxParam param){
		
		mFocusBoxParams.leftMargin = (int) (mToParam.mX) - mPaddingX;
		mFocusBoxParams.topMargin = (int) (mToParam.mY) - mPaddingY;
		mFocusBoxParams.width = (int) (mToParam.mW) + 2 * mPaddingX;
		mFocusBoxParams.height = (int) (mToParam.mH) + 2 * mPaddingY;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		MagicButtonCommon.print(false, "onTouchEvent--------------lllllll");
		show(false);
		return super.onTouchEvent(event);
	}
	
	public class FocusBoxParam{
		
		public float mX;
		public float mY;
		public int mW;
		public int mH;
		
		public FocusBoxParam(){

		}

		public FocusBoxParam(float x, float y, int w, int h){
			
			mX = x;
			mY = y;
			mW = w;
			mH = h;
		}
	}
}