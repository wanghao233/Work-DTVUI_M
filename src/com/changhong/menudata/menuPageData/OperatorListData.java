package com.changhong.menudata.menuPageData;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.changhong.data.pageData.ListPageData;
import com.changhong.data.pageData.PageData;
import com.changhong.data.pageData.itemData.ItemRadioButtonData;
import com.changhong.menudata.MainMenuReceiver;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvConfigManager;
import com.changhong.tvos.dtv.tvap.DtvOperatorManager;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass;
import com.changhong.tvos.dtv.tvap.baseType.DtvOperator;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstPageDataID;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstSourceID;
import com.changhong.tvos.system.commondialog.CommonProgressInfoDialog;
import com.changhong.tvos.system.commondialog.ScanWarnDialog;

public class OperatorListData extends ListPageData {
	private static final String TAG = OperatorListData.class.getSimpleName();
	Context mContext = null;
	int mCurOperatorIndex = 0;
	int mSourceID = ConstSourceID.DVBC;
	int mLastOperatorIndex = 0;
	boolean mSetSelected = true;
	boolean mOperatorHasChanged = false;
	List<String> mOperatorNameList = null;
	DtvOperatorManager mOperatorManager = null;
	DtvSourceManager mSourceManager = null;
	private DtvChannelManager mChannelManager;
	private DtvConfigManager mDtvConfigManager;
	private MenuManager mMenuManager;

	public OperatorListData(String strTitle, int picTitle, Context context) {
		super(ConstPageDataID.OPERATOR_LIST_PAGE_DATA, strTitle, picTitle);
		mContext = context;
		mType = EnumPageType.BroadListPage;
		mSourceManager = DtvSourceManager.getInstance();
		mSourceID = mSourceManager.getCurSourceID();
		mChannelManager = DtvChannelManager.getInstance();
		mDtvConfigManager = DtvConfigManager.getInstance();
		mMenuManager = MenuManager.getInstance();
		this.init();
	}

	public OperatorListData(String strTitle, int picTitle, int sourceID, Context context) {
		super(ConstPageDataID.OPERATOR_LIST_PAGE_DATA, strTitle, picTitle);
		mContext = context;
		mType = EnumPageType.BroadListPage;
		mSourceManager = DtvSourceManager.getInstance();
		mSourceID = sourceID;
		mChannelManager = DtvChannelManager.getInstance();
		mDtvConfigManager = DtvConfigManager.getInstance();
		mMenuManager = MenuManager.getInstance();
		this.init(sourceID);
	}

	private List<String> getOperaterNameList(List<DtvOperator> operatorList) {
		List<String> list = new ArrayList<String>();
		Log.i(TAG, "LL [enter] function getcityNameList()");
		if (null != operatorList) {
			for (int i = 0; i < operatorList.size(); i++) {
				Log.i(TAG, "LL broadcastOperatorName = " + operatorList.get(i).getOperatorName());
				list.add(operatorList.get(i).getOperatorName());
			}
		}
		Log.i(TAG, "LL [end] function getcityNameList()");
		return list;
	}

	private void init(int sourceID) {
		mOperatorManager = DtvOperatorManager.getInstance();
		mOperatorNameList = getOperaterNameList(mOperatorManager.getOperatorListBySourceID(sourceID));
		mLastOperatorIndex = mCurOperatorIndex;
		if (sourceID == mSourceManager.getCurSourceID()) {
			mCurOperatorIndex = mOperatorManager.getCurOpIndex();
		} else {
			mCurOperatorIndex = -1;
		}
		Log.i(TAG, "LL init()>>mCurOperatorIndex = " + mCurOperatorIndex);
		if (mOperatorNameList != null && mOperatorNameList.size() > 0) {
			for (int i = 0; i < mOperatorNameList.size(); i++) {
				ItemRadioButtonData data = new ItemRadioButtonData(0, mOperatorNameList.get(i), 0, 0) {

					@Override
					public int isEnable() {
						// TODO Auto-generated method stub
						return 1;
					}
				};

				if (i == mCurOperatorIndex) {
					data.setSelected(true);
				}
				Log.i(TAG, "LL init()>>data()>>i = " + i);
				this.add(data);
			}

		}
	}

	private void init() {
		mOperatorManager = DtvOperatorManager.getInstance();
		mOperatorNameList = getOperaterNameList(mOperatorManager.getOperatorList());
		mLastOperatorIndex = mCurOperatorIndex;
		mCurOperatorIndex = mOperatorManager.getCurOpIndex();
		Log.i(TAG, "LL init()>>mCurOperatorIndex = " + mCurOperatorIndex);
		if (mOperatorNameList != null && mOperatorNameList.size() > 0) {
			for (int i = 0; i < mOperatorNameList.size(); i++) {
				ItemRadioButtonData data = new ItemRadioButtonData(0, mOperatorNameList.get(i), 0, 0) {

					@Override
					public int isEnable() {
						// TODO Auto-generated method stub
						return 1;
					}
				};

				if (i == mCurOperatorIndex) {
					data.setSelected(true);
				}
				Log.i(TAG, "LL init()>>data()>>i = " + i);
				this.add(data);
			}

		}
	}

	private void updateSelectedView() {
		ItemRadioButtonData item = null;
		item = ((ItemRadioButtonData) get(mLastOperatorIndex));
		if (null != item) {
			item.setSelected(false);
			item.update();
		}
		item = ((ItemRadioButtonData) get(mCurOperatorIndex));
		if (null != item) {
			item.setSelected(true);
			item.update();
		}
		PageData pageData = this.getPageDataById(ConstPageDataID.CHANNEL_SCAN_SETUP_DATA);
		if (pageData != null && pageData instanceof ChannelScanSetupData) {

			((ChannelScanSetupData) pageData).updatePageData();
		}
	}

	public void setClickedItem(int index) {
		Log.i(TAG, "LL setClickedItem()>>index = " + index);
		setOperator(index + getPageIndex() * mPerPageItemNum);
		super.setClickedItem(index);
	}

	public void updatePage() {
		Log.i(TAG, "LL updatePage()>>mCurOperatorIndex = " + mCurOperatorIndex);
		if (true == mSetSelected) {
			mSetSelected = false;
			if (mSourceID == mSourceManager.getCurSourceID()) {
				mCurOperatorIndex = mOperatorManager.getCurOpIndex();
			} else {
				mCurOperatorIndex = -1;
			}

			loadItemList();
			setCurItemIndex(mCurOperatorIndex);
			loadCurItemList();
			updateRealTimeData();
		} else {
			mOperatorManager.updateOperatorList();
			super.updatePage();
		}
	}

	public void reset() {
		mSetSelected = true;
		super.reset();
	}

	private boolean setOperator(final int index) {
		if (mCurOperatorIndex == index) {
			Log.i(TAG, "setOperator failed and index equal to mCurOperatorIndex " + mCurOperatorIndex);
			return false;
		}
		final ScanWarnDialog dialog = new ScanWarnDialog(mContext);
		dialog.setDisplayString(mContext.getResources().getString(R.string.no_string), mContext.getResources().getString(R.string.dtv_scan_setup_waring_info));//dtv_scan_setup_waring
		dialog.getmYesButton().setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mCurOperatorIndex != index) {
					mOperatorHasChanged = true;
					mLastOperatorIndex = mCurOperatorIndex;
					mCurOperatorIndex = index;

					mSourceManager.setCurSource(mSourceID);
					mOperatorManager.setCurOperator(mCurOperatorIndex);//设置运营商
					updateSelectedView();

					dialog.dismiss();
				}
			}
		});
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface arg0) {
				dialog.setShowTV(false);
			}
		});
		dialog.setOnDismissListener(new OnDismissListener() {

			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				if (dialog.isShowTV()) {
					MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
					dialog.setShowTV(false);
				} else {

					if (mOperatorHasChanged) {
						CommonProgressInfoDialog progressDiglog = new CommonProgressInfoDialog(mContext);
						//提高菜单显示层级 2014.05.16
						progressDiglog.getWindow().setType(2003);
						progressDiglog.setDuration(1000000);
						progressDiglog.setButtonVisible(false);
						progressDiglog.setCancelable(false);
						progressDiglog.setMessage(mContext.getString(R.string.dtv_system_reboot));
						progressDiglog.show();

						mChannelManager.reset();
						mDtvConfigManager.clearAll();

						mDtvConfigManager.setValue(ConstValueClass.ConstOperatorState.OPCHANGED, "true");
						mMenuManager.delAllScheduleEvents();

						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
								powerManager.reboot("");

							}
						}, 5000);
					}
				}
			}
		});
		dialog.show();
		return true;
	}

	private boolean setOperator() {
		if (mCurOperatorIndex == this.getCurItemIndex()) {

			return false;
		}
		final ScanWarnDialog dialog = new ScanWarnDialog(mContext);
		dialog.setDisplayString(mContext.getResources().getString(R.string.no_string), mContext.getResources().getString(R.string.dtv_scan_setup_waring_info));//dtv_scan_setup_waring
		dialog.getmYesButton().setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mCurOperatorIndex != getCurItemIndex()) {
					mOperatorHasChanged = true;
					mLastOperatorIndex = mCurOperatorIndex;
					mCurOperatorIndex = getCurItemIndex();
					mSourceManager.setCurSource(mSourceID);
					mOperatorManager.setCurOperator(mCurOperatorIndex);//设置运营商
					updateSelectedView();

					dialog.dismiss();
				}
			}
		});
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface arg0) {
				dialog.setShowTV(false);
			}
		});
		dialog.setOnDismissListener(new OnDismissListener() {

			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				if (dialog.isShowTV()) {
					MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
					dialog.setShowTV(false);
				} else {

					if (mOperatorHasChanged) {
						CommonProgressInfoDialog progressDiglog = new CommonProgressInfoDialog(mContext);
						//提高菜单显示层级 2014.05.16
						progressDiglog.getWindow().setType(2003);
						progressDiglog.setDuration(1000000);
						progressDiglog.setButtonVisible(false);
						progressDiglog.setCancelable(false);
						progressDiglog.setMessage(mContext.getString(R.string.dtv_system_reboot));
						progressDiglog.show();

						mChannelManager.reset();
						mDtvConfigManager.clearAll();
						mDtvConfigManager.setValue(ConstValueClass.ConstOperatorState.OPCHANGED, "true");
						mMenuManager.delAllScheduleEvents();

						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
								powerManager.reboot("");

							}
						}, 5000);
					}
				}
			}
		});
		dialog.show();
		return true;
	}

	public boolean onkeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG, "LL onkeyDown***");
		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			if (setOperator()) {
				return true;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
		case KeyEvent.KEYCODE_DPAD_LEFT:
			pageNextByDirection(keyCode);
			return true;
		default:
			break;
		}
		return super.onkeyDown(keyCode, event);
	}

	public Context getContext() {
		return mContext;
	}

}
