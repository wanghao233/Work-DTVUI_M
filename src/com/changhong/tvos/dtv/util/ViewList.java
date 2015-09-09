package com.changhong.tvos.dtv.util;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public abstract class ViewList extends LinearLayout implements
		View.OnKeyListener {

	private static final String TAG = "ViewList";
	protected int onePageNum = 7;
	int curPageNum;
	int pageCount;
	int lastIndex = 0;
	public int curIndex = 0;
	protected List<?> list;
	protected List<View> viewList;
	protected Context context;
	public LinearLayout listLayout;
	RelativeLayout scrollBarbg1;
	int scrollBarbgH1;
	ImageView scrollBar1;
	protected ViewPageItem pageScrollBar;
	protected boolean scrollBarShowNum =true;
	android.widget.RelativeLayout.LayoutParams scrollBarLayoutParams;
	Handler myHandler; 
	Runnable myRunnable;
	public ViewList(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		onCreate();
	}

	public ViewList(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		onCreate();
	}

	void onCreate() {
		listLayout = new LinearLayout(context);
		//scrollBarbg = new RelativeLayout(context);
		//scrollBar = new ImageView(context);
		viewList = new ArrayList<View>();
		listLayout.setOrientation(VERTICAL);
		//scrollBarbg.setBackgroundResource(R.drawable.menu_page_gray);
		//scrollBar.setBackgroundResource(R.drawable.menu_page_blue);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		addView(listLayout, params);
		//addView(scrollBarbg, new LinearLayout.LayoutParams(5,
		//		LayoutParams.MATCH_PARENT));
		scrollBarLayoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//scrollBarbg.addView(scrollBar, scrollBarLayoutParams);
		// setWillNotDraw(false);
		pageScrollBar =new ViewPageItem(context);
		this.addView(pageScrollBar ,new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT));
		pageScrollBar.showPageItem(false);
		myHandler =new Handler();
		myRunnable =new Runnable() {
			public void run() {
				pageScrollBar.initData(pageCount,scrollBarShowNum);
				pageScrollBar.showPageItem(true);
			}
		};
	}

	public void init(List<?> list, int index) {
		myHandler.removeCallbacks(myRunnable);
		pageCount = (list.size() + onePageNum - 1) / onePageNum;
		curPageNum =index/onePageNum+1;
		lastIndex = index;
		curIndex = index;
		viewList.clear();
		this.list = list;
		listLayout.removeAllViews();
		if(curIndex >= list.size()){
			lastIndex = list.size()-1;
			curIndex =list.size() -1;
		}
		if(list !=null && list.size()>0){
			myHandler.postDelayed(myRunnable,100);			
		    updateOnePage();
		}else{
			pageScrollBar.showPageItem(false);
		}
		
	}

	public boolean onKey(View v, int keyCode, KeyEvent event) {
		Log.v("onKey", "onKey 3");
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
				lastIndex = curIndex;
				//curIndex = (curIndex > 0) ? curIndex - 1 : curIndex;
				curIndex = (curIndex > 0) ? curIndex - 1 : getListSize()-1;
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				lastIndex = curIndex;
//				curIndex = (curIndex < getListSize() - 1) ? curIndex + 1
//						: curIndex;
				curIndex = (curIndex < getListSize() - 1) ? curIndex + 1: 0;
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if(getListSize() >onePageNum){
					lastIndex = curIndex;
					if(curPageNum >1){
						curIndex =curIndex-onePageNum;
					}else{
						curIndex =(pageCount*onePageNum)+curIndex-onePageNum;
						if(curIndex >=getListSize()){
							curIndex =getListSize() - 1;
						}
					}
				}
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if(getListSize() >onePageNum){
					lastIndex = curIndex;
					if(curPageNum <pageCount){
						curIndex =curIndex+onePageNum;
						if(curIndex >=getListSize()){
							curIndex =getListSize() - 1;
						}
					}else{
						curIndex =curIndex%onePageNum;
					}
				}
				break;		
			default:
				break;
			}
			Log.i(TAG,"LL ViewList>>lastIndex = " + lastIndex + "curIndex = " + curIndex);
			if (lastIndex / onePageNum != curIndex / onePageNum) {
				updateView(lastIndex);
				updateOnePage();
			} else if (lastIndex != curIndex) {
				updateView(lastIndex);
				updateView(curIndex);
			}
		}
		return false;
	}

	public abstract View getView(int position);

	public abstract void updateView(int position);

	protected void updateOnePage() {
		listLayout.removeAllViews();

		if(getListSize()>0){
			int curPageStartIndex = curIndex / onePageNum * onePageNum;
			for (int i = 0; i < onePageNum; i++) {
				if (curPageStartIndex + i < getListSize()) {
					listLayout.addView(getView(curPageStartIndex + i));
				}
			}
		updateView(curIndex);
		curPageNum =curIndex/onePageNum+1;
		pageScrollBar.setCurPageNum(curPageNum);
		}
	}

	int getListSize() {
		return list.size();
	}

	public boolean isSelected(int position) {
		if (curIndex == position) {
			return true;
		} else {
			return false;
		}
	}

	public int getCurrentPosition() {
		return curIndex;
	}

	public int getOnePageNum() {
		return onePageNum;
	}

	public void SetOnePageNum(int num) {
		onePageNum = num;
	}

	public int getCurIndex() {
		return curIndex;
	}
	public void clear()
	{
		curIndex =0;
		viewList.clear();
		listLayout.removeAllViews();
		pageScrollBar.showPageItem(false);
		list =null;
	}
	protected ViewPageItem getPageItem(){
		return pageScrollBar;
	}
}

