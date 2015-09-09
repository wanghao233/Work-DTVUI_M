package com.changhong.tvos.dtv.util;

import java.util.List;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public  class ViewQuickChannel extends ViewGridBase{

	public interface OnQuickChannelLongPressListener{
		public boolean onQuickChannelLongPress();
	}
	
	public interface OnQuickChannelDissMissListener{
		public void disMissListener();
	}
	private static final String TAG = "ViewQuickChannel";

	
	listState listType = listState.channel_List;
	
	int curSelectedListIndex;
	boolean isLongPressedFlag = false;
	public boolean isResponseOnKeyUp = false;
	OnQuickChannelLongPressListener mOnQuickChannelLongPressListener = null;
	OnQuickChannelDissMissListener mDissMissListener = null;
	public Handler handler = new Handler();
	
	
	public ViewQuickChannel(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ViewQuickChannel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.i(TAG,"LL onItemSelected()***");
				arg1.setSelected(true);
				int curProgramIndex = getPageCounts() * getCurPage() + arg2;
				setCurSelectedListIndex(curProgramIndex);
				curSelectedUpdate(getCurSelectedListIndex());
				
				curPosition = arg2;
				curChooseDtvProgram = manager.getProgramByListIndex(curProgramIndex);
				startChannelChange();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Log.i(TAG,"LL onNothingSelected()***");
			}
		});
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				keyActionCallBack.resetTimer();
				Log.i(TAG,"LL onItemClick()>>arg2 = " + arg2 + ",arg3 = " + arg3);
				
				setCurSelectedListIndex(getPageCounts() * getCurPage()+ arg2);
				curSelectedUpdate(getCurSelectedListIndex());
				
				curChooseDtvProgram = manager.getProgramByListIndex(getCurSelectedListIndex());
				channelChangeRightNow();
				mDissMissListener.disMissListener();
			}
		});
		
		
		if (mAdapter == null) {
			mAdapter = new QuickChannelAdapter(mContext);
			setAdapter(mAdapter);
		}
	}
	
	
	public void setOnQuickChannelLongPressListener(OnQuickChannelLongPressListener listener){
		mOnQuickChannelLongPressListener = listener;
	}
	
	public void setOnQuickChannelDissMissListener(OnQuickChannelDissMissListener listener){
		mDissMissListener = listener;
	}
	
	public OnQuickChannelLongPressListener getOnQuickChannelLongPressListener(){
		return mOnQuickChannelLongPressListener ;
	}
	
	public OnQuickChannelDissMissListener getOnQuickChannelDissMissListener(){
		return mDissMissListener ;
	}
	
	
	public boolean changeChannelListType() {
		
		listState type;
		if (listType == listState.channel_WatchedList) {	
			manager.init(listState.channel_List);
			this.curSelectedUpdate(manager.getCurProgramListIndex());	
			type = listState.channel_List;
		} else {
			List<DtvProgram> list = manager.getWatchedChannelList();
			if (list == null || list.size() <= 0) {
				Log.e(TAG,"LL changeChannelListType>>list == null");
				return false;
			}
			manager.setCur_list(list);
			this.curSelectedUpdate(0);
			type = listState.channel_WatchedList;
		}
		
		mAdapter.init(manager);
		mAdapter.setCurPage(curPage);
		mAdapter.notifyDataSetChanged();
		
		setSelection(getCurPosition());	
		
		listType = type;
		
		if(listType == listState.channel_WatchedList){
			onDestroy();
			DtvProgram tem = manager.getProgramByListIndex(0);
			if(null != tem){
				manager.changeChannelByProgramServiceIndex(tem.mServiceIndex);
			}
		}else{
			DtvProgram dtvProgram = manager.getProgramByListIndex(getCurSelectedListIndex());
			if(dtvProgram!=null){
				onDestroy();
				manager.changeChannelByProgramServiceIndex(dtvProgram.mServiceIndex);
			}	
		}
		return true;
	}

	
	
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		switch (keyCode) {
			case KeyEvent.KEYCODE_ENTER:
			case KeyEvent.KEYCODE_DPAD_CENTER:{
				if (event.isLongPress()) {
					Log.i(TAG,"LL dispatchKeyEvent()>>isLongPress***");
					mOnQuickChannelLongPressListener.onQuickChannelLongPress();
					isLongPressedFlag = true;
					return true;
				}
			}
		}
		return super.dispatchKeyEvent(event);

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v("tv", "LL channelListGridView>>onKeyDown>>keyCode = " + keyCode);
		isResponseOnKeyUp = true;
		if(!isAnimationOk) {
			Log.v(TAG, "EL channelListGridView>>onKeyDown>> still Animation not keyAction;");
			return true;
		}
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
		
		switch (curKey) {
			case KeyEvent.KEYCODE_ENTER:
			case KeyEvent.KEYCODE_DPAD_CENTER:
				//返回true，获得该按键信息之后不执行onClick时间
//				keyActionCallBack.keyActionDown(curKey, event);
				return true;	
			default:
				break;
		}		
		keyActionCallBack.keyActionDown(curKey, event);
		if(lastPage != curPage){
			keyActionCallBack.animationAction(curKey);
			return true;
		}
		
		if(curKey != keyCode){
			Log.i(TAG, "EL--> scanKeyCode is "+ event.getScanCode());
			super.onKeyDown(curKey, event);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(!isAnimationOk) {
			Log.i(TAG, "EL-->onKeyUp event not response due to the animation is not finish");
			return true;
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			Log.i(TAG,"LL onKeyUp>>KEYCODE_DPAD_CENTER>>isLongPressedFlag="+isLongPressedFlag);
			if(isLongPressedFlag == false && isResponseOnKeyUp){				
				curChooseDtvProgram = manager.getProgramByListIndex(curPage * getPageCounts() + curPosition);
				Log.i(TAG, "change program to index : " + curPage * getPageCounts() + curPosition);
				onDestroy();
				if(null != curChooseDtvProgram){
				manager.changeChannelByProgramServiceIndex(curChooseDtvProgram.getProgramServiceIndex());
				}
				mDissMissListener.disMissListener();
				return true;
			}
			break;
		
		case KeyEvent.KEYCODE_YELLOW:
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR:
			Log.i(TAG, "EL-->onKeyUp event action the change page right");
			keyActionCallBack.keyActionUp(KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR, event);
			break;
			
		case KeyEvent.KEYCODE_GREEN:
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL:
			Log.i(TAG, "EL-->onKeyUp event action the change pag left");
			keyActionCallBack.keyActionUp(KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL, event);
			break;
		default:
			break;
		}
		isLongPressedFlag = false;	
		
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void init(MenuManager listmanager,listState state) {
		setAnimationOk(true);
		manager = listmanager;
//		manager.init(state);
		this.curSelectedUpdate(manager.getCurProgramListIndex());	
		lastPage = curPage;
		mAdapter.init(manager);
		mAdapter.notifyDataSetChanged();
		setSelection(getCurPosition());	
	}
	
	public void setCurSelectedListIndex(int selectedIndex){
		curSelectedListIndex = selectedIndex;
	}
	public int getCurSelectedListIndex(){
		return curSelectedListIndex;
	}
	private int calculatePage(int curSelectedIndex){
		return curSelectedIndex / getPageCounts();
	}
	private int calculatePosition(int curSelectedIndex){
		return curSelectedIndex % getPageCounts();
	}	

	public void curSelectedUpdate(int curSelectedIndex){
		this.curSelectedListIndex = curSelectedIndex;
		lastPage = curPage;
		curPage = calculatePage(curSelectedIndex);
		curPosition = calculatePosition(curSelectedIndex);
		Log.v(TAG,"LL curSelectedIndex ="+curSelectedIndex);
		Log.v(TAG,"LL lastPage ="+lastPage);
		Log.v(TAG,"LL curPage ="+curPage);
		Log.v(TAG,"LL curPosition ="+curPosition);
	}	

	


	private class QuickChannelAdapter extends SimpleGridAdapter {
	
		
		
		public QuickChannelAdapter(Context myContext) {
			// TODO Auto-generated constructor stub
			mContext = myContext;
		}


		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			RelativeLayout myRelativeLayout;
			TextView channelNum;
			TextView channelName;
			final ImageView channelLog;
			
			if(null == convertView){
				
				myRelativeLayout = (RelativeLayout) LayoutInflater.from(mContext)
						.inflate(R.layout.view_quick_channel_sel, null);
			}else{
				myRelativeLayout = (RelativeLayout)convertView;
			}
			
			channelNum = (TextView)myRelativeLayout.findViewById(R.id.textView1);
			channelName = (TextView)myRelativeLayout.findViewById(R.id.textView2);
			channelLog = (ImageView) myRelativeLayout.findViewById(R.id.imageView1);

			DtvProgram tmpChannel;
			tmpChannel = manager.getCurList().get((position+curPage*(columns*linage)));
			
			if(position == 12){
				
				Log.i(TAG, "getView at position " + position + " and channel is: "+tmpChannel.mProgramName
						+" ,serviceIndex is "+ tmpChannel.mServiceIndex + ",logo is:"+ tmpChannel.mDtvLogo
				);
			}
			channelNum.setText("D"+tmpChannel.mProgramNum);
			channelName.setText(tmpChannel.mProgramName);
			Drawable drawable = AsyncImageLoader.getInstance().loadDrawable(
							("/data/dtv/logo/" + tmpChannel.mDtvLogo), new ImageCallback(){

								@Override
								public void imageLoaded(Drawable imageDrawable,
										String imageUrl) {
									if(null == imageDrawable) {
										Log.i(TAG, "EL--> the image is null");
									}else {
										if(null != channelLog){
											
										channelLog.setImageDrawable(imageDrawable);
				}
			}
				}

							});
			if(null != drawable) {
					 channelLog.setImageDrawable(drawable);
			}

			return myRelativeLayout;
		}
	}



	@Override
	public void setChooseOneSwap(boolean isChooseOneSwap) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isChooseOneSwap() {
		// TODO Auto-generated method stub
		return false;
	}
}