package com.changhong.menudata.menuPageData;

import java.util.List;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.changhong.data.pageData.ListPageData;
import com.changhong.data.pageData.PageData;
import com.changhong.data.pageData.itemData.ItemRadioButtonData;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvConfigManager;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstPageDataID;
import com.changhong.tvos.dtv.vo.DTVSource;
import com.changhong.tvos.system.commondialog.ScanWarnDialog;

public class SourceListData extends ListPageData {

	private static final String TAG = "SourceListData";
	Context mContext = null;
	private List<DTVSource> mSourceList = null;
	DtvSourceManager mSourceManager = null;
	private int mLastSourceIndex;
	private int mCurSourceIndex;
	boolean mSetSelected = true;
	private DtvChannelManager mChannelManager = null;

	public SourceListData(String pageId, String strTitle, int picTitle) {
		// TODO Auto-generated constructor stub
		super(pageId, strTitle, picTitle);
	}

	public SourceListData(String strTitle, int picTitle, Context context) {
		super(ConstPageDataID.SOURCE_LIST_PAGE_DATA, strTitle, picTitle);
		mContext = context;
		mType = EnumPageType.BroadListPage;
		this.init();
	}

	public void init() {
		// TODO Auto-generated method stub
		mSourceManager = DtvSourceManager.getInstance();
		mChannelManager = DtvChannelManager.getInstance();
		mSourceList = mSourceManager.getSourceList();
		mLastSourceIndex = mCurSourceIndex;
		mCurSourceIndex = mSourceManager.getCurSourceIndex();
		if (null != mSourceList && mSourceList.size() > 0) {
			int size = mSourceList.size();
			for (int i = 0; i < size; i++) {
				ItemRadioButtonData data = new ItemRadioButtonData(0, mSourceList.get(i).miSourceName, 0, 0) {

					@Override
					public int isEnable() {
						// TODO Auto-generated method stub
						return 1;
					}

				};
				if (i == mCurSourceIndex) {
					data.setSelected(true);
				}
				this.add(data);
			}
		} else {
			Log.i(TAG, "EL--> the demondType list size =0");
		}
	}

	private void updateSelectedView() {
		ItemRadioButtonData item = null;
		item = ((ItemRadioButtonData) get(mLastSourceIndex));
		if (null != item) {
			item.setSelected(false);
			item.update();
		}
		item = ((ItemRadioButtonData) get(mCurSourceIndex));
		if (null != item) {
			item.setSelected(true);
			item.update();
		}

		PageData pageData = null;

		pageData = this.getPageDataById(ConstPageDataID.CHANNEL_SCAN_SETUP_DATA);
		if (pageData != null && pageData instanceof ChannelScanSetupData) {

			((ChannelScanSetupData) pageData).updatePageData();
		}

		pageData = this.getPageDataById(ConstPageDataID.CHANNEL_SCAN_DATA);

		if (pageData != null && pageData instanceof ChannelScanData) {

			((ChannelScanData) pageData).updatePageView();
		} else {
			if (null == pageData) {
				Log.i("update", "updatePageView() -->>he page ChannelScanData is null");
			} else {
				Log.i("update", "updatePageView() -->>can't find the page ChannelScanData");
			}
		}
	}

	public void updatePage() {

		if (true == mSetSelected) {
			mSetSelected = false;
			if (mSourceManager.getCurSourceIndex() != mCurSourceIndex) {
				mCurSourceIndex = mSourceManager.getCurSourceIndex();
			}
			loadItemList();
			setCurItemIndex(mCurSourceIndex);
			loadCurItemList();
			updateRealTimeData();
		} else {
			super.updatePage();
		}
	}

	public void setClickedItem(int index) {
		setSource(index);
		super.setClickedItem(index);
	}

	private boolean setSource(final int index) {
		if (mCurSourceIndex == index) {
			return false; //update to prePage
		}
		final int curSourceIndex = index;
		Log.i(TAG, "LL setSource()>>curSource = " + curSourceIndex);
		final ScanWarnDialog mCommonAcionDialog = new ScanWarnDialog(mContext);

		//		String string= mContext.getResources().getString(R.string.dtv_scansetting_change_source_reboot_confirm, mDemodeTypeList.get(index));
		String string = mContext.getResources().getString(R.string.dtv_scansetting_change_source_normal_confirm, mSourceList.get(index).miSourceName);

		mCommonAcionDialog.setCancelable(true);
		mCommonAcionDialog.setDisplayString(mContext.getResources().getString(R.string.no_string), string);//dtv_scan_setup_waring
		mCommonAcionDialog.setDuration(30);
		mCommonAcionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if (mCurSourceIndex != mLastSourceIndex) {

					ChannelInfoData channelInfoData = (ChannelInfoData) SourceListData.this.getPageDataById(ConstPageDataID.CHANNEL_INFO_PAGE_DATA);
					if (null != channelInfoData) {
						channelInfoData.addPageItem();
					}
					//     				((ChannelInfoData)SourceListData.this.getPageDataById(ConstPageDataID.CHANNEL_INFO_PAGE_DATA)).addPageItem();;				
					mChannelManager.setCurChannelType(mChannelManager.getBootChannelType());

					//读取保存的节目， 如果没有，则用startBoot的那个节目号
					String tmp = DtvConfigManager.getInstance().getValue("savedProgram".concat(String.valueOf(mSourceManager.getCurSourceID())));

					if (tmp == null) {
						mChannelManager.channelForceChangeByProgramNum(mChannelManager.getBootChannelNum(), true);
					} else {
						mChannelManager.channelForceChangeByProgramNum(Integer.valueOf(tmp), true);
					}

				}

			}
		});
		mCommonAcionDialog.getmYesButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCommonAcionDialog.dismiss();
				mLastSourceIndex = mCurSourceIndex;
				mCurSourceIndex = index;
				DtvProgram curDtvProgram = mChannelManager.getCurProgram();
				if (null != curDtvProgram) {
					//切换源之前将当前节目的节目号保存
					DtvConfigManager.getInstance().setValue("savedProgram".concat(String.valueOf(mSourceManager.getCurSourceID())), String.valueOf(curDtvProgram.mProgramNum));
				}

				mSourceManager.setCurSource(mSourceManager.getSourceIDByIndex(curSourceIndex));
				updateSelectedView();
			}

		});

		mCommonAcionDialog.getmNoButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mCommonAcionDialog.dismiss();
			}
		});
		mCommonAcionDialog.show();

		return true;
	}

	public void reset() {
		mSetSelected = true;
		super.reset();
	}

	public boolean onkeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG, "LL onkeyDown***");
		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			if (setSource()) {
				return true;
			} else {
				return super.onkeyDown(KeyEvent.KEYCODE_BACK, event);
			}

		default:
			break;
		}
		return super.onkeyDown(keyCode, event);
	}

	private boolean setSource() {
		if (mCurSourceIndex == this.getCurItemIndex()) {
			return false;
		}
		return setSource(this.getCurItemIndex());

	}

	public Context getContext() {
		return mContext;
	}
}
