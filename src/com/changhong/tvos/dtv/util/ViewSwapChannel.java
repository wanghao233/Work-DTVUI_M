package com.changhong.tvos.dtv.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.changhong.tvos.dtv.AsyncImageLoader;
import com.changhong.tvos.dtv.AsyncImageLoader.ImageCallback;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.menuManager.MenuManager.listState;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;

public class ViewSwapChannel extends ViewGridBase {

	private boolean isChooseOneSwap = false;
	private static final String TAG = "ViewSwapChannel";
	public boolean findOneSwap =false;
	private int  toLocation[]={0,0};
	private int  fromLocation[]={0,0};
	
	public interface OnFlickerListener{
		public void setFlickerPosition(int[] location);
		public void startFlicker();
		public void stopFlicker();
	}
	private OnFlickerListener onFlickerListener;
	
	
	public void setOnFlickerListener(OnFlickerListener onFlickerListener) {
		this.onFlickerListener = onFlickerListener;
	}

	public OnFlickerListener getOnFlickerListener() {
		return onFlickerListener;
	}
	
	public void startSwapFlicker(){
		onFlickerListener.startFlicker();
	}
	public void startSwapFlicker(int postion){
		
		getChildAt(postion).getLocationInWindow(toLocation);
		onFlickerListener.setFlickerPosition(toLocation);
		onFlickerListener.startFlicker();
	}

	public void stopSwapFlicker(){
		onFlickerListener.stopFlicker();
	}
	
	
	public ViewSwapChannel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		if(null == mAdapter){
			mAdapter = new SwapChannelAdapter(mContext);
			setAdapter(mAdapter);
		}
		
		setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.v(TAG,"LL onItemSelected*** " + arg2);
				
				ViewHolder viewHolder = (ViewHolder)arg1.getTag();
				curPosition = arg2;
				arg1.setSelected(true);
				
				curChooseDtvProgram = viewHolder.tmpChannel;
				startChannelChange();

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Log.v(TAG,"LL onNothingSelected***");
			}
		});
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.i(TAG,"LL onItemClick()>>arg2 = " + arg2 + ",arg3 = " + arg3);
				keyActionCallBack.resetTimer();
				ViewHolder viewHolder = (ViewHolder)arg1.getTag();
				curPosition = arg2;
				
				if(isChooseOneSwap){
					//已经选中了一个了，不换台
					setChooseOneSwap(false);
					manager.programSwap(manager.getprogramSwapSelect(),manager.getCurList().indexOf(viewHolder.tmpChannel));
					manager.setprogramSwapSelect(Integer.MAX_VALUE);
					stopSwapFlicker();
					mAdapter.notifyDataSetChanged();
				}else {
					//第一次点击选中的时候,换台
					curChooseDtvProgram = viewHolder.tmpChannel;
					handler.removeCallbacks(changeChannelRunnable);
					
					if(null != curChooseDtvProgram){
						manager.changeChannelByProgramServiceIndex(curChooseDtvProgram.getProgramServiceIndex());

					}
					
					setChooseOneSwap(true);
					
					manager.setprogramSwapSelect(manager.getCurProgramListIndex());
					arg1.getLocationInWindow(fromLocation);
					startSwapFlicker(arg2);				
					mAdapter.needReStartFlicker = false;
					((RelativeLayout) arg1.findViewById(R.id.relativeLayout3)).setBackgroundResource(R.drawable.translucent_background);
				}
				
//				if (manager.getprogramSwapSelect() == Integer.MAX_VALUE) {
//					manager.setprogramSwapSelect(manager.getCurProgramListIndex());
//					arg1.getLocationInWindow(fromLocation);
//					startSwapFlicker(arg2);				
//					mAdapter.needReStartFlicker = false;
//					((RelativeLayout) arg1.findViewById(R.id.relativeLayout3)).setBackgroundResource(R.drawable.translucent_background);
//					
//				} else {
//					
//					manager.programSwap(manager.getprogramSwapSelect(),manager.getCurList().indexOf(viewHolder.tmpChannel));
//					manager.setprogramSwapSelect(Integer.MAX_VALUE);
//					stopSwapFlicker();
//					mAdapter.notifyDataSetChanged();
//				}				
			}
		});
	}
	
	@Override
	public void onDestroy() {
		findOneSwap =false;
		super.onDestroy();
	}

	@Override
	public void startChannelChange(){
		if(!isChooseOneSwap){
			super.startChannelChange();
		}
	}

	@Override
	public void init(MenuManager listmanager, listState state) {
		setAnimationOk(true);
		setChooseOneSwap(false);
		
		manager = listmanager;
//		manager.init(state);
		curProgramIndexUpdate(manager.getCurProgramListIndex());
		lastPage = curPage;
		mAdapter.init(manager);
		mAdapter.notifyDataSetChanged();
		setSelection(curPosition);
	}
	
	
	@Override
	public void setChooseOneSwap(boolean isChooseOneSwap) {
		this.isChooseOneSwap = isChooseOneSwap;
		Log.i(TAG, "set choose on swap false");
	}

	@Override
	public boolean isChooseOneSwap() {
		return isChooseOneSwap;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v("tv", "LL onKeyDown>>keyCode = " + keyCode);
		if(!isAnimationOk()){
			Log.v(TAG, "EL channelListGridView>>onKeyDown>> still Animation not keyAction;");
			return true;
		}
		Log.i(TAG, "EL--> curPosion is "+ curPosition +" and the selction " + getSelectedItemPosition());
		lastPage = curPage;
		
		int curKey = revertKeyCode(keyCode, event);
		
		Log.v(TAG, "LL onKeyDown>>keyCode = " + keyCode + "and revert keycode is + " + curKey);
		
		switch (curKey) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
//		case KeyEvent.KEYCODE_DPAD_UP:
//		case KeyEvent.KEYCODE_DPAD_DOWN:
			
		jugePageOrNot(curKey);
	}	
		
//		switch (curKey) {
//			
//			case KeyEvent.KEYCODE_GREEN:
//				if (manager.getprogramSwapSelect() != Integer.MAX_VALUE) {
//					manager.setprogramSwapSelect(Integer.MAX_VALUE);
//					this.stopSwapFlicker();
//					((SwapChannelAdapter) this.getAdapter()).notifyDataSetChanged();
//				}
//				keyActionCallBack.keyActionDown(keyCode,event);
//				return false;
//			
//			default:
//				break;
//		}	
		keyActionCallBack.keyActionDown(curKey,event);
		
		if(lastPage != curPage){
			keyActionCallBack.animationAction(curKey);
			return true;
		}
		
		if(curKey != keyCode){
			Log.i(TAG, "EL--> scanKeyCode is "+ event.getScanCode());
			super.onKeyDown(curKey, event);
			return true;
		}
		return super.onKeyDown(curKey, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(!isAnimationOk) {
			Log.i(TAG, "EL--> onKeyUp not response due to still animation ");
			return true;
		}
		keyActionCallBack.keyActionUp(keyCode, event);
		return super.onKeyUp(keyCode, event);
	}
	
	
	
	private class SwapChannelAdapter extends SimpleGridAdapter{
		private View.OnFocusChangeListener listener;
		private Animation myAnimation;
		
		public SwapChannelAdapter(Context myContext) {
			mContext = myContext;
			myAnimation =new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

			listener =new OnFocusChangeListener(){
				
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub

					if(hasFocus){
						Log.v(TAG,"LL onFocusChange true");
						v.startAnimation(myAnimation);
						//
					}else{
						Log.v(TAG,"LL onFocusChange false");
						v.clearAnimation();
					}
					
				}
			};
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			RelativeLayout myRelativeLayout = null;
			final ImageView channelLogImageView;
			TextView channelNum;
			TextView channelName;
			viewHolder = new ViewHolder();
			myRelativeLayout = (RelativeLayout) LayoutInflater.from(mContext)
					.inflate(R.layout.view_swap_channel_sel, null);
				myRelativeLayout.setTag(viewHolder);
			
			channelLogImageView = (ImageView) myRelativeLayout
					.findViewById(R.id.imageView1);
			channelNum = (TextView) myRelativeLayout.findViewById(R.id.textView1);
			channelName = (TextView) myRelativeLayout.findViewById(R.id.textView2);
			
			DtvProgram tmpChannel;

			tmpChannel = manager.getCurList().get(
					(position + curPage*(columns*linage)));
			viewHolder.tmpChannel = tmpChannel;
			channelNum.setText("D" + tmpChannel.mProgramNum);
			channelName.setText(tmpChannel.mProgramName);

			channelLogImageView.setScaleType(ImageView.ScaleType.FIT_XY);

			Drawable drawable = AsyncImageLoader.getInstance().loadDrawable(
						("/data/dtv/logo/" + tmpChannel.mDtvLogo), new ImageCallback(){

							@Override
							public void imageLoaded(Drawable imageDrawable,
									String imageUrl) {
								if(null == imageDrawable) {
									Log.i(TAG, "EL--> the image is null");
								}else {
									channelLogImageView.setImageDrawable(imageDrawable);
								}
							}
							
						});
				 if(null != drawable) {
//					 Log.i(TAG, "EL--> the image drawable is not null");
					 channelLogImageView.setImageDrawable(drawable);
			}
					
			Log.v(TAG, "the select index is " + manager.getprogramSwapSelect() + "and cur Index is "  + position + curPage * getPageCounts());

			if ( position + curPage * getPageCounts() == manager.getprogramSwapSelect() && needReStartFlicker) {
				Log.v(TAG, "needReStartFlicker is true");
				((ViewBreathingLight)myRelativeLayout.findViewById(R.id.relativeLayout3))
					.setBackgroundResource(R.drawable.transparent_background);
				findOneSwap = true;
			}
			myRelativeLayout.setOnFocusChangeListener(listener);
			return myRelativeLayout;
		}
		
	}
	
	private class ViewHolder{
		public DtvProgram tmpChannel;
	}

}
