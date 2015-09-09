package com.changhong.menuView;

import java.util.List;
import com.changhong.data.pageData.ListPageData;
import com.changhong.data.pageData.PageData;
import com.changhong.data.pageData.itemData.util.ItemData;
import com.changhong.menudata.MainMenuReceiver;
import com.changhong.menuView.itemView.ItemHaveSubView;
import com.changhong.menuView.itemView.ItemBaseView;
import com.changhong.menuView.itemView.ItemOptionInputView;
import com.changhong.menuView.itemView.ItemOptionView;
import com.changhong.menuView.itemView.ItemProgressBarView;
import com.changhong.menuView.itemView.ItemPromptView;
import com.changhong.menuView.itemView.ItemRadioButtonView;
import com.changhong.menuView.itemView.ItemRatingBarView;
import com.changhong.softkeyboard.CHSoftKeyboardManager;
import com.changhong.tvos.dtv.DtvRoot;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvAcrossPlatformAdaptationManager.AdjustCHSoftKeyboardManager;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstPageDataID;
import com.changhong.tvos.dtv.vo.DTVSource;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class MenuListView extends ListView {
	private static final String TAG = MenuListView.class.getSimpleName();
	Context mContext = null;
	private MenuItemAdapter mMenuItemAdapter = null;
	private ItemBaseView mCurIView = null;
	private ListPageData mCurPage = null;
	private TimeManager mTimeManager = null;
	private AdjustCHSoftKeyboardManager mAdjustCHSKM = null;
	private DtvSourceManager sourceManager;

	public MenuListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		this.init();

	}

	public MenuListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		this.init();
	}

	public MenuListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;

		this.init();
	}

	private void init() {
		setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.v(TAG, "onItemSelected isEnable ");
				mCurIView = (ItemBaseView) arg1;
				if (mCurIView.getItemData().isEnable() > 0) {
					mCurPage.setCurItem(mCurIView.getItemData());
				}
				Log.v(TAG, "onItemSelected isEnable " + mCurIView.getItemData().isEnable());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.v("tv", "onItemClick");
				mCurIView = (ItemBaseView) arg1;
				mCurPage.setCurItem(mCurIView.getItemData());
				mCurPage.setClickedItem(arg2);
			}
		});

		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.v("tv", "onTouch");
				return false;
			}
		});
		mTimeManager = new TimeManager(mContext, 30000) {

			@Override
			public void onTimer() {
				// TODO Auto-generated method stub
				Log.v("tv", "MenuListView onTimer");
				if (mCurPage != null) {
					mCurPage.reset();
				}
				MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
			}
		};
		setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				Log.v(TAG, "LL onFocusChange hasFocus =" + hasFocus);
				if (hasFocus) {

				} else {
					mTimeManager.pause();
				}
			}
		});
	}

	public void init(PageData pageData) {
		if (mMenuItemAdapter == null) {
			mMenuItemAdapter = new MenuItemAdapter(mContext);
			setAdapter(mMenuItemAdapter);
		}
		mCurPage = (ListPageData) pageData;

		mMenuItemAdapter.init(mCurPage);
		Log.v(TAG, "LL mCurPage.mCurItemIndex =" + mCurPage.getCurItemIndex() + ",isFoucsAble = " + mCurPage.isFoucsAble);

		setSelection(mCurPage.getCurItemIndex() % ListPageData.mPerPageItemNum);

		ItemData curItem = mCurPage.getCurItem();
		if (curItem != null) {
			curItem.requestFocus();
		}

		if (mCurPage.isFoucsAble == false) {
			setSelector(R.drawable.menu_item_selector_null);
		} else {
			setSelector(R.drawable.menu_item_selector);
		}
		mMenuItemAdapter.notifyDataSetChanged();
		requestFocus();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Log.v(TAG, "LL onDraw()***");
		if (isFocused()) {
			mTimeManager.start();
		}
		super.onDraw(canvas);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v(TAG, "LL onKeyDown***");
		boolean isKeyboardProcessed = false;
		switch (event.getScanCode()) {
		case 231://keyboard Menu
		case 233://keyboard Channel Up
		case 234://keyboard Channel Down
			break;
		case 232://keyboard Source 
			keyCode = KeyEvent.KEYCODE_DPAD_CENTER;
			isKeyboardProcessed = true;
			break;
		case 235://keyboard Volume Down
			keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
			isKeyboardProcessed = true;
			break;
		case 236://keyboard Volume Up
			keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
			isKeyboardProcessed = true;
			break;
		default:
			break;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isBackExitMenu()) {
				MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
				return true;
			}
		}
		mTimeManager.start();
		if (getSelectedView() != null) {

			if (mCurPage.onkeyDown(keyCode, event)) {
				Log.v("tv", "onKeyDown return true");
				return true;
			}
			Log.v("tv", "onKeyDown break");
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_SOURCE:
			//		case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBOX:
		case 4126://KEYCODE_CHANGHONGIR_TOOLBOX
		case 170://KEYCODE_CHANGHONGIR_TV
		case 4135:
		case KeyEvent.KEYCODE_MENU:
			mTimeManager.end();
			MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
			if (null != mAdjustCHSKM) {
				if (mAdjustCHSKM.isSoftKeyPanelOnShow()) {

					mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
				}
			}
			return true;
		case KeyEvent.KEYCODE_BACK:
			mTimeManager.end();
			break;
		default:
			break;
		}
		Log.i(TAG, "LL onKeyDown>>isKeyboardProcessed = " + isKeyboardProcessed);
		if (isKeyboardProcessed == true) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD: //软键盘
			if (mAdjustCHSKM == null) {
				mAdjustCHSKM = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(mContext);
			}
			if (null != mAdjustCHSKM) {
				if (mAdjustCHSKM.isSoftKeyPanelOnShow()) {
					mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_BACK, 0);
				} else {

					mAdjustCHSKM.processNumberSoftKeyPanel(KeyEvent.KEYCODE_CHANGHONGIR_SOFTKEYBOARD, CHSoftKeyboardManager.POS_BOTTOM_CENTER);
					return true;
				}
			}
			break;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		// TODO Auto-generated method stub
		Log.i(TAG, "LL onWindowFocusChanged()>>hasWindowFocus = " + hasWindowFocus);
		if (hasWindowFocus) {

		} else {
			mTimeManager.pause();
		}
		super.onWindowFocusChanged(hasWindowFocus);
	}

	class MenuItemAdapter extends BaseAdapter {

		ListPageData mPageData = null;
		Context mContext;

		public MenuItemAdapter(Context mContext) {
			super();
			this.mContext = mContext;
		}

		public void init(ListPageData pageData) {
			pageData.updatePage();
			mPageData = pageData;

		}

		public int getCount() {
			// TODO Auto-generated method stub
			int count = 0;
			if (mPageData != null) {
				count = mPageData.getCurItemList().size();

			}
			return count;
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			ItemData curItemData = mPageData.get(position);
			if (null != curItemData && curItemData.isEnable() > 0) {
				return true;
			} else {
				return false;
			}
		}

		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ItemBaseView mView = null;
			ItemData itemData = mPageData.getCurItemList().get(arg0);
			switch (itemData.getDataType()) {
			case EN_PROGRESSBAR:
				if (itemData.getView() == null) {
					mView = new ItemProgressBarView(mContext);
					mView.init(itemData);
					itemData.setView(mView);
				} else {
					mView = (ItemBaseView) itemData.getView();
				}
				break;
			case EN_OPTIONVIEW:
				if (itemData.getView() == null) {
					mView = new ItemOptionView(mContext);
					mView.init(itemData);
					itemData.setView(mView);
				} else {
					mView = (ItemBaseView) itemData.getView();
				}
				break;
			case EN_HAVESUBCHILD:
				if (itemData.getView() == null) {
					mView = new ItemHaveSubView(mContext);
					mView.init(itemData);
					itemData.setView(mView);
				} else {
					mView = (ItemBaseView) itemData.getView();
				}
				break;
			case EN_PROMPTVIEW:
				if (itemData.getView() == null) {
					mView = new ItemPromptView(mContext);
					mView.init(itemData);
					itemData.setView(mView);
				} else {
					mView = (ItemBaseView) itemData.getView();
				}
				break;
			case EN_RATINGBAR:
				if (itemData.getView() == null) {
					mView = new ItemRatingBarView(mContext);
					mView.init(itemData);
					itemData.setView(mView);
				} else {
					mView = (ItemBaseView) itemData.getView();
				}
				break;
			case EN_OPTIONINPUTVIEW:
				if (itemData.getView() == null) {
					mView = new ItemOptionInputView(mContext);
					mView.init(itemData);
					itemData.setView(mView);
				} else {
					mView = (ItemBaseView) itemData.getView();
				}
				break;
			case EN_RADIOBUTTON:
				if (itemData.getView() == null) {
					mView = new ItemRadioButtonView(mContext);
					mView.init(itemData);
					itemData.setView(mView);
				} else {
					mView = (ItemBaseView) itemData.getView();
				}
				break;
			}
			return mView;
		}
	}

	public ItemData getItemData() {
		return mCurIView.getItemData();
	}

	public ItemBaseView getCurView() {
		return mCurIView;
	}

	private boolean isBackExitMenu() {
		if (!DtvRoot.isEnterOperaterDirect) {
			return false;
		}
		String ID = mCurPage.mPageId;
		if (null == ID) {
			return false;
		}

		if (null == sourceManager) {
			sourceManager = DtvSourceManager.getInstance();
		}
		List<DTVSource> sourceList = sourceManager.getSourceList();
		if (null != sourceList) {
			if (sourceList.size() == 1) {
				return ID.equals(ConstPageDataID.OPERATOR_LIST_PAGE_DATA);
			} else if (sourceList.size() > 1) {
				return ID.equals(ConstPageDataID.SOURCE_SETUP_PAGE_DATA);
			}
		}
		return false;
	}

	public void update() {
		if (null != mMenuItemAdapter) {
			//			if(null != mCurPage.mPageId && ConstPageDataID.SYSTEM_SET_UP.equals(mCurPage.mPageId)){

			mMenuItemAdapter.notifyDataSetChanged();
			//			}
		}
	}
}