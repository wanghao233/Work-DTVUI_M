package com.changhong.data.pageData.itemData;

import com.changhong.data.pageData.itemData.util.EnumDataType;
import com.changhong.data.pageData.itemData.util.ItemData;
import android.util.Log;
import android.view.KeyEvent;

public abstract class ItemHaveSubData extends ItemData{
	private static final String TAG = ItemHaveSubData.class.getSimpleName();
	protected String mValue = ""; 
	protected String mPassword;
	private boolean isIdentified;
	private boolean isImmediate;
	private String mInputStr;
	public boolean mIsDismissArrow;

	public ItemHaveSubData(int mImage, String mName, int mhelpId, int mhelpImage) {
		super(mImage, mName, mhelpId, mhelpImage);
		// TODO Auto-generated constructor stub
		mType =EnumDataType.EN_HAVESUBCHILD;
	}	
	public String getValue(){
		return mValue;
	}
	public void setValue(String value){
		mValue =value;
	}
	public  boolean onkeyDown(int keyCode ,KeyEvent event){
		Log.i(TAG,"LL onkeyDown()>>keyCode = " + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			
			if(mOnNextPageListener !=null){
				for(OnNextPageListener listener:mOnNextPageListener){
					
					if(isIdentify()==true){
						isIdentified = false;
						if(mChild !=null){
							mChild.reset();					
						}
						listener.onNextPage(mChild); 
					}
				}
				return true;	 
			}else {
				onNextPage();
				return true;
			}
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_1:	
		case KeyEvent.KEYCODE_2:
		case KeyEvent.KEYCODE_3:	
		case KeyEvent.KEYCODE_4:	
		case KeyEvent.KEYCODE_5:	
		case KeyEvent.KEYCODE_6:	
		case KeyEvent.KEYCODE_7:	
		case KeyEvent.KEYCODE_8:	
		case KeyEvent.KEYCODE_9:
			if(isIdentify() ==false){
				mInputStr +=(keyCode - KeyEvent.KEYCODE_0);
				if(mInputStr.contains(mPassword)){
					Log.i(TAG,"LL isIdentify() = " + isIdentify() + ", mInputStr = " + mInputStr + ", mPassword = " + mPassword);
					if(isImmediate){
						if(mOnNextPageListener !=null){
							for(OnNextPageListener listener:mOnNextPageListener){
								if(mChild !=null){
									mChild.reset();					
								}
								listener.onNextPage(mChild); 
							}
							return true;	 
						}else {
							onNextPage();
							return true;
						}
					}else{
						isIdentified =true;
						
					}
					mInputStr="";
				}
				
			}
			break;
		default:
			break;
		}
		return false;	 
	}
	public void requestFocus(){
		super.requestFocus();
		mInputStr ="";
	}

	public abstract void onNextPage();
	
	public boolean initData() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setImmediateFlag(boolean flag){
		this.isImmediate = flag;
	}

	public void setPassword(String password){
		this.mPassword = password;
	}
	public boolean passwordIdentify(String password){
		if(mPassword ==password){
			isIdentified =true;
		}else{
			isIdentified =false;
		}
		return isIdentified;
	}
	public boolean isIdentify(){
		if(mPassword ==null){
			isIdentified =true;
		}
		return isIdentified;	
	}
	
	public void onNextChildPage(){
		if(mOnNextPageListener !=null){
			for(OnNextPageListener listener:mOnNextPageListener){
				if(mChild !=null){
					mChild.reset();					
				}
				listener.onNextPage(mChild); 
			}
		}
	}
}