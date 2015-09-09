package com.changhong.data;

import android.util.Log;
import android.view.KeyEvent;
import com.changhong.data.pageData.ListPageData;

public class MenuData{
	public ListPageData mTopPage;
	public ListPageData mCurPage;
	
	public boolean onkeyDown(int keyCode ,KeyEvent event){
		Log.v("tv","MenuData onKeyDown ");
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:	
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:	
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:	
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:	
		case KeyEvent.KEYCODE_ENTER:
			break;			
		default:
			break;
		}
		
		return mCurPage.onkeyDown(keyCode ,event);
	}

	public MenuData(){
	}
}