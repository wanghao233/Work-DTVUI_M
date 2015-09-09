package com.changhong.tvos.dtv.util;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.changhong.tvos.dtv.AsyncImageLoader;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.AsyncImageLoader.ImageCallback;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.menuManager.MenuManager.listState;
import com.changhong.tvos.dtv.tvap.DtvInterface;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;

public class ViewChannelSort extends ViewGridBase implements android.widget.AdapterView.OnItemSelectedListener,
	android.widget.AdapterView.OnItemClickListener{
	private static final  String TAG = "ViewChannelSort";
	private static List<DtvProgram> myChangeList;
	private int[] location;
 	private int curSelected = -1;
	private DtvInterface mInterface = DtvInterface.getInstance();

	
	public interface OnSortChangeCallBack{
		/**
		 * 添加一个进入排序行列回调，可执行动画等动作
		 * @param program
		 */
		public void onSortAdd(int[] from,DtvProgram program);
		
		/**
		 * 取消一个排序回调，可执行动画等
		 * @param program
		 */
		public void onSortCancel(DtvProgram program);
	}
	
	private OnSortChangeCallBack mSortChangeCallBack;
	public static boolean isFirstShow = true;
	
	public ViewChannelSort(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	@Override
	public void init(MenuManager listmanager, listState state) {
		// TODO Auto-generated method stub
		manager = listmanager;
		setAnimationOk(true);
		setChooseOneSwap(false);
		
		curProgramIndexUpdate(manager.getCurProgramListIndex());
		
		mAdapter.init(manager);
		
		if(null == myChangeList){
			 //临时备份一个list 用来处理加载UI， 原本的节目列表则用来处理交换等事件
			myChangeList = new ArrayList<DtvProgram>(manager.getCurList()); 
		}else{
			myChangeList.clear();
			myChangeList.addAll(manager.getCurList());
		}		
//		if(null == holderList){
//			holderList = new Stack<ViewChannelSort.SortedHolder>();
//		}
		
//		if(null == backDrawable){
//			backDrawable = mContext.getResources().getDrawable(R.drawable.sort_back);
//			selectedDrawable = mContext.getResources().getDrawable(R.drawable.sort_flidder);
//		}
		mAdapter.notifyDataSetChanged();
		setSelection(curPosition);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		if(mAdapter == null){
			mAdapter = new SortAdapter(mContext);
			setAdapter(mAdapter);
		}
		location = new int[2];
		setOnItemSelectedListener(this);
		setOnItemClickListener(this);
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

	
	@Override
	public void onItemClick(AdapterView<?> arg0, final View contentView, int position, long id) {
		// TODO Auto-generated method stub
		curPosition = position;
		final SortedHolder holder = (SortedHolder) contentView.getTag();
				Log.i(TAG, "current select is not ordered, and last order is : " + curSelected);
				
				curSelected = position + curPage*(columns*linage);
				Animation dis = AnimationUtils.loadAnimation(mContext, R.anim.zoom_exit);
				contentView.setVisibility(View.GONE);
				if(null != holder.pro){
					contentView.getLocationInWindow(location);
					removeProgram(location,holder.pro);
//					contentView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.zoom_exit));
				}
	}


	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		curPosition = arg2;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void resetData(){
		myChangeList.clear();
		myChangeList.addAll(manager.getCurList());
		curSelected = -1;
	}
	
	public void cancelSort(){
		resetData();
		mAdapter.notifyDataSetChanged();
		setSelection(curPosition);
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
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v(TAG, "LL onKeyDown>>keyCode = " + keyCode);
		if(!isAnimationOk()){
			Log.v(TAG, "EL channelListGridView>>onKeyDown>> still Animation not keyAction;");
			return true;
		}
		Log.i(TAG, "EL--> curPosion is "+ curPosition +" and the selction " + getSelectedItemPosition());
		
	////////////////////////fengy 2014-10-29 //////////////
	  int curKey =mInterface.ConvertKeyBoardCode(keyCode, event);
		
		Log.v(TAG, "LL onKeyDown>>keyCode = " + keyCode + "and revert keycode is + " + curKey);
		
		lastPage = curPage;
		switch (curKey) {
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_DOWN:

		jugePageOrNot(curKey);
	}	
		
		
		keyActionCallBack.keyActionDown(curKey,event);
		
		if(lastPage != curPage){
//			keyActionCallBack.resetTimer();
			keyActionCallBack.animationAction(curKey);
			return true;
		}
		
		if(curKey != keyCode){
			Log.i(TAG, "EL--> scanKeyCode is "+ event.getScanCode());
			mInterface.SimulateKey(curKey);
			return true;
		}
		
		
		return super.onKeyDown(curKey, event);
	}
	
	
	public void setSortChangeCallBack(OnSortChangeCallBack sortChangeCallBack) {
		mSortChangeCallBack = sortChangeCallBack;
	}

	public OnSortChangeCallBack getSortChangeCallBack() {
		return mSortChangeCallBack;
	}

	public void updatePages(List<DtvProgram> list){
		if(null == list || list.size() == 0){
			mAdapter.maxPage = -1;
		}else{
			mAdapter.maxPage = (list.size()-1) / getPageCounts();
		}
		
		//最后一页最后一个
		if((curPage == mAdapter.maxPage )&&  
				(0 == list.size()- getPageCounts() * curPage)&&
				curPage != 0){
			curPage -= 1;
			curPosition = 0;
		}
		Log.i(TAG, "curPage is "+ curPage);
		mAdapter.curPage = curPage;
		mAdapter.notifyDataSetChanged();
		
		if((0 != curPosition) && curPosition >= (mAdapter.getCount() -1)){
			//最后一个
			curPosition -= 1;
		}
		setSelection(curPosition);
	}
	
	/**
	 * 用于添加节目到所有节目列表中
	 * @param program
	 */
	public void addProgram(DtvProgram program){
		
		int index = 0;
		for(;index < myChangeList.size(); index++){
			DtvProgram pro =  myChangeList.get(index);
			if(pro.mProgramNum > program.mProgramNum){
				break;
			}
		}
		lastPage = curPage;
		myChangeList.add(index, program);
		
		mAdapter.maxPage = (myChangeList.size() -1)/ getPageCounts();
		curPage = index / getPageCounts();

		/////////////////delete By YangLiu 2014-11-27/////////////////
							/*不记住撤销节目后返回的位置*/
		//		curPosition = index % getPageCounts();		
		
		Log.d(TAG,"index is " + index + " lastPage is " + lastPage + " curPage is" + curPage);
		
		Log.d(TAG, "maxPage is " + mAdapter.maxPage);
		freshView();
	}

	
	/**
	 * 用于在所有节目中添加排序到排序列表
	 * @param pro
	 */
	private void removeProgram(int[] location, DtvProgram pro) {
		// TODO Auto-generated method stub
		myChangeList.remove(pro);

		Log.i(TAG, "curPage is " + curPage);
		Log.i(TAG, "maxPage is " + mAdapter.maxPage);
		
		mSortChangeCallBack.onSortAdd(location,pro);
		
		if(curPage != 0 && myChangeList.size()% getPageCounts() == 0){
			curPage--;
		}
		mAdapter.maxPage = (myChangeList.size()-1) / getPageCounts();
	
		Log.i(TAG, "curPage is " + curPage);
		Log.i(TAG, "maxPage is " + mAdapter.maxPage);
		freshView();
	}
	
	
	public SortedHolder newHolder(){
		return new SortedHolder();
	}
	
	public class SortedHolder{
		public DtvProgram pro;
		public ImageView icon;
		public TextView tv_num;
		public TextView tv_name;
		
		public SortedHolder(){};
	}
	
	
	private class SortAdapter extends SimpleGridAdapter{

		private Context mContext;
		public SortAdapter(Context mContext) {
			// TODO Auto-generated constructor stub
			this.mContext = mContext;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (myChangeList != null){
				if(myChangeList.size() < getPageCounts()){
					return myChangeList.size();
				}else if(curPage < maxPage){
					return  getPageCounts();
				}else{
					return myChangeList.size()- getPageCounts() * curPage;
				}
			}
			else {
				return 0;
			}

		}
		
		
		@Override
		public View getView(int position, View contentView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			SortedHolder holder = null;
			if(contentView == null){
				holder = new SortedHolder();
				contentView = LayoutInflater.from(mContext).inflate(R.layout.view_program_cell, null);
				contentView.setTag(holder);
				
				holder.tv_num = (TextView)contentView.findViewById(R.id.textView1);
				holder.tv_name = (TextView)contentView.findViewById(R.id.textView2);
				holder.icon = (ImageView) contentView.findViewById(R.id.imageView1);
			}else{
				holder = (SortedHolder)contentView.getTag();
				contentView.setVisibility(View.VISIBLE);
			}
			final ImageView channelLogImageView = holder.icon;
			holder.icon.setImageDrawable(null);
			DtvProgram tmpChannel = null;
			if(position + curPage * getPageCounts() < myChangeList.size()){
				
				tmpChannel = myChangeList.get(position + curPage * getPageCounts());
				holder.pro = tmpChannel;
				Drawable drawable = AsyncImageLoader.getInstance().loadDrawable(
						("/data/dtv/logo/" + tmpChannel.mDtvLogo), new ImageCallback(){
							
							@Override
							public void imageLoaded(Drawable imageDrawable,
									String imageUrl) {
								if(null == imageDrawable) {
								}else {
									channelLogImageView.setImageDrawable(imageDrawable);
								}
							}
							
						});
				if(null != drawable) {
					holder.icon.setImageDrawable(drawable);
				}
				
				holder.tv_num.setText("D" +  tmpChannel.getProgramNum());
				holder.tv_name.setText(tmpChannel.getProgramName());
			}else{
				contentView.setVisibility(View.GONE);
				Log.e(TAG, "outIndex err curpage is" + curPage + " maxPage is " + maxPage);
			}
			return contentView;
		}
		
	}


	public List<DtvProgram> getChannelList() {
		// TODO Auto-generated method stub
		return myChangeList;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void reset(){
		myChangeList = null;
	}
}
