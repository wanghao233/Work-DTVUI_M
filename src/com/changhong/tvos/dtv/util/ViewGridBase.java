package com.changhong.tvos.dtv.util;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.menuManager.MenuManager.listState;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;

public abstract class ViewGridBase extends GridView {
	
	public static final int DIRECTION_LEFT = 0;
	public static final int DIRECTION_RIGHT = 1;
	public static final int DIRECTION_UP = 3;
	public static final int DIRECTION_DOWN = 4;
	
	protected static final long DELAY_TIME = 2000;
	private static final String TAG = "ViewGridBase";
	
	public Context mContext;
	public int linage = 3;
	public int columns = 6;
	public int lastPage;
	public int curPage;
	public int curPosition;
	
	public boolean isAnimationOk;
	public DtvProgram curChooseDtvProgram;
	public MenuManager manager;
	
	public interface OnKeyActionCallBack{
		public void resetTimer();
		public void animationAction(int direct);
		public void keyActionUp(int keyCode, KeyEvent event);
		public void keyActionDown(int keyCode, KeyEvent event);
	}
	
	public abstract void init(MenuManager listmanager,listState state);
	public abstract void init();
	
	public OnKeyActionCallBack keyActionCallBack;
	public SimpleGridAdapter mAdapter;
	
	public Handler handler = new Handler();
	public Runnable changeChannelRunnable = new Runnable() {
		public void run() {
			if(null != curChooseDtvProgram){
				manager.changeChannelByProgramServiceIndex(curChooseDtvProgram.getProgramServiceIndex());
			}
		}
	};
	
	public ViewGridBase(Context context) {
		super(context);
		mContext= context;
		// TODO Auto-generated constructor stub
	}

	public ViewGridBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext= context;
		// TODO Auto-generated constructor stub
	}

	public ViewGridBase(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext= context;
		// TODO Auto-generated constructor stub
	}
	
	public void startChannelChange(){
		
		int curProgramIndex = curPage * getPageCounts() + curPosition;
		curChooseDtvProgram = manager.getProgramByListIndex(curProgramIndex);
		if(null != curChooseDtvProgram){
			handler.removeCallbacks(changeChannelRunnable);
			handler.postDelayed(changeChannelRunnable, DELAY_TIME);
			Log.v(TAG, "EL--> change program index in list " + curProgramIndex);
		}
	}
	
	public void channelChangeRightNow(){
		
		handler.removeCallbacks(changeChannelRunnable);
		int curProgramIndex = curPage * getPageCounts() + curPosition;
		curChooseDtvProgram = manager.getProgramByListIndex(curProgramIndex);
		if(null != curChooseDtvProgram){
			manager.changeChannelByProgramServiceIndex(curChooseDtvProgram.getProgramServiceIndex());
		}
	}
	
	public int revertKeyCode(int keyCode, KeyEvent event){
		boolean isKeyboardProcessed = false;
		int curKeyCode = keyCode;
		switch (event.getScanCode()) {
		case 231://keyboard Menu
			isKeyboardProcessed = true;
			curKeyCode = KeyEvent.KEYCODE_MENU;
			break;
		case 232://keyboard Source 
			curKeyCode = KeyEvent.KEYCODE_DPAD_CENTER;
			isKeyboardProcessed = true;
			break;
		case 233://keyboard Channel Up
			isKeyboardProcessed = true;
			curKeyCode = KeyEvent.KEYCODE_DPAD_UP;
			//curKeyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
			break;
		case 234://keyboard Channel Down
			isKeyboardProcessed = true;
			curKeyCode = KeyEvent.KEYCODE_DPAD_DOWN;
		//	curKeyCode = KeyEvent.KEYCODE_DPAD_LEFT;
			break;
		case 235://keyboard Volume Down
		//	curKeyCode = KeyEvent.KEYCODE_DPAD_DOWN;
			curKeyCode = KeyEvent.KEYCODE_DPAD_LEFT;
			isKeyboardProcessed = true;
			break;
		case 236://keyboard Volume Up
			//curKeyCode = KeyEvent.KEYCODE_DPAD_UP;
			curKeyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
			isKeyboardProcessed = true;
			break;
		default:
			break;
		}
		
		if(isKeyboardProcessed) {
//			event.setKeyCode(curKeyCode);
		}
		
		return curKeyCode;
	}
	
	public void jugePageOrNot(int keyCode){
		int position = curPosition;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			
			if(0 == position % columns){ //在第一列
				if(curPage > 0){
					curPage--;
				}else{
					curPage = mAdapter.getMaxPage();
				}
				
				Log.v(TAG, "EL-->first column");
			}
			
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			
			if((curPage == mAdapter.getMaxPage()) && 
					(position == manager.getCurList().size()-((columns*linage)*curPage) -1)){
				
				Log.v(TAG, "EL-->last page and last column");
				curPage =0;
			
			}else if((columns -1 ) == (position % columns)){//在最后一列
				if(curPage < mAdapter.getMaxPage()){
					curPage++;
				}else{
					curPage =0;
				}
				Log.v(TAG, "EL-->last column");
			}
			
			break;	

		case KeyEvent.KEYCODE_DPAD_UP:
			
			if(position < columns){//表示在第一行
				
				if(curPage > 0){
					curPage--;
				}else{
					curPage = mAdapter.getMaxPage();
				}
				Log.v(TAG, "EL-->first line");
			}
			
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			
			Log.v(TAG, "EL--> the last page last line " + ((manager.getCurList().size() -curPage * linage * columns -1) /columns ) +
					" and cur line is " + (position / columns));
			if((curPage == mAdapter.getMaxPage()) && 
					(((mAdapter.getCount() -1) /columns ) == (position / columns))){
				
				Log.v(TAG, "EL-->last page and last line and position is " + position);
				curPage =0;
			
			}else if(position >= columns * (linage -1)){ //在最下一行
				if(curPage < mAdapter.getMaxPage()){
					if (curPage == mAdapter.getMaxPage() - 1 ) {
						position = (position - 2 * columns);
						int length = manager.getCurList().size()-((columns*linage)* (curPage+ 1)) -1;
						Log.i(TAG, "the last count is " + length + " and the position is " + position);
						if (length < position) {
							position = length;
						}
						
					}
					curPage++;
				}else{
					curPage =0;
				}
				
				Log.v(TAG, "EL-->last line");
			}
			break;

		}
	}
	
	public  void curProgramIndexUpdate(int CurProgramIndex){
		lastPage = curPage;
		curPage = manager.getCurProgramListIndex() / getPageCounts();
		curPosition = manager.getCurProgramListIndex() % getPageCounts();
	}	
	
	public void setCurPageAndPosition(int page, int position) {
		curPage = page;
		curPosition = position;
		freshView();
	}
	
	public  void freshView(){
		Log.v(TAG, "EL--> fresh view curPage is " + curPage + " curPosition is " + curPosition);
		mAdapter.setCurPage(curPage);
		mAdapter.notifyDataSetChanged();
		setSelection(curPosition);
	}
	
	public void onDestroy(){
		handler.removeCallbacks(changeChannelRunnable);
	}
	
	public void slideTurnPage(int direction, int position){
		int key_direction = -1;
		onDestroy();
		setAnimationOk(false);
		switch (direction) {
		case DIRECTION_UP:
			key_direction = KeyEvent.KEYCODE_DPAD_UP;
			if(curPage > 0){
				curPage--;
			}else{
				curPage = mAdapter.getMaxPage();
			}
			break;
		case DIRECTION_LEFT:
			key_direction = KeyEvent.KEYCODE_DPAD_LEFT;
			if(curPage > 0){
				curPage--;
			}else{
				curPage = mAdapter.getMaxPage();
			}
			
			break;
		case DIRECTION_DOWN:
			key_direction = KeyEvent.KEYCODE_DPAD_DOWN;
			if(curPage < mAdapter.getMaxPage()){
				curPage++;
			}else{
				curPage =0;
			}
			break;
		case DIRECTION_RIGHT:
			key_direction = KeyEvent.KEYCODE_DPAD_RIGHT;
			if(curPage < mAdapter.getMaxPage()){
				curPage++;
			}else{
				curPage =0;
			}
			
			break;
		default:
			break;
		}
		
		mAdapter.setCurPage(curPage);
		mAdapter.notifyDataSetChanged();
		
		if(position < 0 ){
			position =0;
		}else if(position > (mAdapter.getCount() -1 )){
			position = mAdapter.getCount() -1;
		}
		curPosition = position;
		keyActionCallBack.animationAction(key_direction);
		setSelection(curPosition);
	}
	
	public void setLinage(int linage) {
		this.linage = linage;
	}

	public int getLinage() {
		return linage;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getColumns() {
		return columns;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPosition(int curPosition) {
		this.curPosition = curPosition;
	}

	public int getCurPosition() {
		return curPosition;
	}
	
	public int getPageCounts(){
		
		return columns * linage;
	}
	
	public int getMaxPage(){
		return mAdapter.maxPage;
	}
	
	public void setKeyActionCallBack(OnKeyActionCallBack keyActionCallBack) {
		this.keyActionCallBack = keyActionCallBack;
	}

	public OnKeyActionCallBack getKeyActionCallBack() {
		return keyActionCallBack;
	}

	public void setCurChooseDtvProgram(DtvProgram curChooseDtvProgram) {
		this.curChooseDtvProgram = curChooseDtvProgram;
	}

	public DtvProgram getCurChooseDtvProgram() {
		return curChooseDtvProgram;
	}
	
	public void setAnimationOk(boolean isAnimationOk) {
		this.isAnimationOk = isAnimationOk;
	}

	public boolean isAnimationOk() {
		return isAnimationOk;
	}
	
	public abstract class SimpleGridAdapter extends BaseAdapter{
		public Context mContext;
		public int maxPage;
		public int curPage;
		public boolean needReStartFlicker = true;
		
		public void init(MenuManager manager) {
			curPage = manager.getCurProgramListIndex() / getPageCounts() ;
			maxPage = (manager.getCurList().size()-1) / getPageCounts();

		}
		public void setCurPage(int curpage){
			curPage =curpage;
		}
		
		public int getCurPage(){
			return curPage;
		}
		public int getMaxPage(){
			return maxPage;
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			if (manager != null){
				if(manager.getCurList().size() < getPageCounts()){
					return manager.getCurList().size();
				}else if(curPage < maxPage){
				return  getPageCounts();
				}else{
					return manager.getCurList().size()- getPageCounts() * curPage;
				}
			}
			else {
				return 0;
			}

		}
		
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			needReStartFlicker = true;
			super.notifyDataSetChanged();
		}

	}
	
	public abstract  void setChooseOneSwap(boolean isChooseOneSwap);
	
	public abstract boolean isChooseOneSwap() ;
	public void setMaxPage(int maxPage) {
		// TODO Auto-generated method stub
		mAdapter.maxPage = maxPage;
	}
}
