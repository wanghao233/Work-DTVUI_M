package com.changhong.menudata;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import com.changhong.data.pageData.ListPageData;
import com.changhong.menuView.ListMenu;
import com.changhong.tvos.dtv.DtvRoot;
import com.changhong.tvos.dtv.tvap.DtvAcrossPlatformAdaptationManager.AdjustCHSoftKeyboardManager;

/**
 * 菜单列表的显示控制
 * @author lulei
 * @rectify enlong
 * 将广播处理菜单显示改为直接处理。取消广播机制，提快显示速度
 */
public class MainMenuReceiver {

	private static final String TAG = MainMenuReceiver.class.getSimpleName();
	public static final String INTENT_CICAQUERY = "com.changhong.tvos.dtv.MainMenu.CiCaQuery";
	public static final String DATA_CARDTYPE = "com.changhong.tvos.dtv.MainMenu.CardType";
	public static final String INTENT_AUTOSEARCH = "com.changhong.tvos.dtv.MainMenu.AutoSearch";
	public static final String DATA_SEARCHTYPE = "com.changhong.tvos.dtv.MainMenu.SearchType";
	public static final String INTENT_MAINMENU_BROADCAST = "com.changhong.menudata.intent.action.MainMenuReceiver";
	public static final String INTEND_EXTRA_PAGE_ID = "pageID";
	public static final String INTEND_EXTRA_KEY = "key";
	public static final String INTEND_EXTRA_ENTER = "enter";
	public static final String INTEND_EXTRA_EXIT = "exit";
	public static final String INTEND_EXTRA_VISIBLE = "visible";
	public static final String INTEND_EXTRA_INVISIBLE = "invisible";
	public static boolean isRemoved = true;
	private static Context mContext;
	private static WindowManager.LayoutParams layoutparams = null;
	private static WindowManager mWm = null;;
	private static ListMenu mListMenu = null;
	private static MenuDataTree mMenuDataTree = null;
	private static boolean isHasInitData = false;
	private static List<MainMenuReceiver.OnMenuRemovedListener> mOnMenuRemovedListener = null;
	private AdjustCHSoftKeyboardManager mAdjustCHSKM = null;

	private static MainMenuReceiver myReceiver = new MainMenuReceiver();

	public interface OnMenuRemovedListener {
		public void onRemoved(boolean removed);
	}

	//	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "LL [enter] onReceive***");
		myReceiver.receiveIntent(intent);
	}

	private void initViewData() {
		Log.i(TAG, "[enter] init***");
		//		mWm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
		layoutparams = new WindowManager.LayoutParams();
		layoutparams.x = 0;
		layoutparams.y = 0;
		layoutparams.gravity = Gravity.LEFT;
		layoutparams.windowAnimations = 0;
		//        layoutparams.format = PixelFormat.TRANSLUCENT  | WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW;
		layoutparams.format = PixelFormat.TRANSLUCENT | WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW;

		layoutparams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
		//		layoutparams.type = android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		layoutparams.type = android.view.WindowManager.LayoutParams.TYPE_APPLICATION;

		//deleted by cuixy
		//mListMenu =new ListMenu(mContext);
		mMenuDataTree = new MenuDataTree(mContext);
		Log.i(TAG, "[end] init***");
	}

	private void setCurPageDataByID(String strPageID) {
		ListPageData pageData = (ListPageData) mMenuDataTree.mMainMenuRootData.getPageDataById(strPageID);
		if (pageData != null) {
			mMenuDataTree.update(pageData);
		}
	}

	private void setViewAdded() {
		//deleted by cuixy
		//		mListMenu.init(mMenuDataTree);
		//		mListMenu.show();
		//		Log.i(TAG,"LL width = " + layoutparams.width + ", height = " + layoutparams.height);
		////		Log.i(TAG,"LL width = " + mWm.getDefaultDisplay().getWidth() + ", height = " +  mWm.getDefaultDisplay().getHeight());
		//		try{
		//			
		//		mWm.addView(mListMenu, layoutparams);
		//		}catch (Exception e) {
		//			// TODO: handle exception
		//			Log.e(TAG, "window add view err:" +e);
		//			this.onRemoved(true);
		//			isRemoved = true;
		//		}
		//deleted by cuixy
	}

	private void setViewRemoved() {
		//deleted by cuixy
		//mListMenu.hide();
		//mWm.removeView(mListMenu);
		mMenuDataTree.update(mMenuDataTree.mMainMenuRootData, mMenuDataTree.mMainMenuRootData);
		//deleted by cuixy
		//mListMenu.init(mMenuDataTree);
	}

	public static void menuVisibilityControl(String action) {
		Intent intent = new Intent(MainMenuReceiver.INTENT_MAINMENU_BROADCAST);
		intent.putExtra(MainMenuReceiver.INTEND_EXTRA_KEY, action);
		if (mContext == null) {
			mContext = DtvRoot.mContext;
		}
		Log.i(TAG, "LL onSendBroadcast(INTENT_MAINMENU_BROADCAST)***");
		//		mContext.sendBroadcast(intent);
		myReceiver.receiveIntent(intent);
	}

	public static void menuVisibilityControl(String action, String pageID) {
		Intent intent = new Intent(MainMenuReceiver.INTENT_MAINMENU_BROADCAST);
		intent.putExtra(MainMenuReceiver.INTEND_EXTRA_KEY, action);
		intent.putExtra(MainMenuReceiver.INTEND_EXTRA_PAGE_ID, pageID);
		Log.i(TAG, "menuVisibilityControl() action: " + action + " pageID: " + pageID);
		if (mContext == null) {
			mContext = DtvRoot.mContext;
		}
		Log.i(TAG, "LL sendBroadcast(INTENT_MAINMENU_BROADCAST)***");
		//		mContext.sendBroadcast(intent);
		myReceiver.receiveIntent(intent);
	}

	public static <T> int getIndexByItem(T[] data, T item) {
		int index = 0;
		if (data != null && item != null) {
			for (index = 0; index < data.length; index++) {
				if (item.equals(data[index])) {
					break;
				}
			}
		}
		if (data != null && index >= data.length) {
			index = 0;
		}
		return index;
	}

	private void onRemoved(boolean removed) {
		if (mOnMenuRemovedListener != null) {
			for (OnMenuRemovedListener listener : mOnMenuRemovedListener) {
				listener.onRemoved(removed);
			}
		}
	}

	public static void setOnMenuRemovedListener(OnMenuRemovedListener listener) {
		if (mOnMenuRemovedListener == null) {
			mOnMenuRemovedListener = new ArrayList<MainMenuReceiver.OnMenuRemovedListener>();
		}
		mOnMenuRemovedListener.add(listener);
	}

	public void receiveIntent(Intent intent) {
		if (mContext == null) {
			mContext = DtvRoot.mContext;
		}
		String action = intent.getStringExtra(MainMenuReceiver.INTEND_EXTRA_KEY);
		String pageID = intent.getStringExtra(INTEND_EXTRA_PAGE_ID);
		if (action != null) {

			if (action.equals(MainMenuReceiver.INTEND_EXTRA_ENTER)) {
				Log.v(TAG, "enter 11111111111");
				//deleted by cuixy
				//if(isHasInitData == false)
				{
					initViewData();
					isHasInitData = true;
				}
				if (DtvRoot.isEnterOperaterDirect || ((mContext instanceof DtvRoot) && (((DtvRoot) mContext).isDisplayMainMenu()))) {
					Log.v(TAG, "enter 22222222222222");

					if (isRemoved == true) {
						Log.v(TAG, "enter 333333333333333");
						if (pageID != null) {
							Log.v(TAG, "enter 4444444444444");
							this.setCurPageDataByID(pageID);
						}
						//deleted by cuixy
						//this.setViewAdded();
						this.onRemoved(false);
						isRemoved = false;
						Log.v(TAG, "enter 555555555555");
					}
				}
			} else if (action.equals(MainMenuReceiver.INTEND_EXTRA_EXIT)) {
				Log.v(TAG, "enter 6666666666666666");
				if (isRemoved == false) {
					Log.v(TAG, "enter 777777777777777");
					if (mAdjustCHSKM == null) {
						Log.v(TAG, "enter 88888888888888888");
						mAdjustCHSKM = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(mContext);
					}
					if (null != mAdjustCHSKM) {
						Log.v(TAG, "enter 999999999999999999");
						if (mAdjustCHSKM.isSoftKeyPanelOnShow()) {
							Log.v(TAG, "enter aaaaaaaaaaaaaaaa");
							mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
						}
					}
					Log.v(TAG, "enter bbbbbbbbbbbbbbbbbbbbbb");
					this.setViewRemoved();
					this.onRemoved(true);
					isRemoved = true;
				}
			} else if (action.equals(MainMenuReceiver.INTEND_EXTRA_INVISIBLE)) {
				Log.v(TAG, "enter cccccccccccccccc");
				//deleted by cuixy
				//				if(mListMenu.isShown()){
				//					if (mAdjustCHSKM == null) {
				//						mAdjustCHSKM = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(mContext);		
				//					}
				//					if(null != mAdjustCHSKM){
				//						if(mAdjustCHSKM.isSoftKeyPanelOnShow()){
				//							
				//							mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
				//						}
				//					}
				//					mListMenu.hide();
				//				}
			} else if (action.equals(MainMenuReceiver.INTEND_EXTRA_VISIBLE)) {
				Log.v(TAG, "enter ddddddddddddddddd");
				if ((mContext instanceof DtvRoot) && (((DtvRoot) mContext).isDisplayMainMenu())) {
					Log.v(TAG, "enter eeeeeeeeeeeeeeeeee");
					//deleted by cuixy
					//if(!mListMenu.isShown()){
					//		mListMenu.show();
					//	}
				}
			}
		}
		Log.i(TAG, "LL [end] onReceive***");
	}

	public static void setWindows(WindowManager window) {
		mWm = window;
	}

	public static void update() {
		//deleted by cuixy
		//mListMenu.update();
	}
}