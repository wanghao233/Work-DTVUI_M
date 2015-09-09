package com.changhong.menudata.menuPageData;

import com.changhong.data.pageData.ListPageData;
import com.changhong.data.pageData.itemData.ItemHaveSubData;
import com.changhong.menudata.MainMenuReceiver;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.channel_manager.FilterChannels;
import com.changhong.tvos.dtv.scan.MenuScan;
import com.changhong.tvos.dtv.scan.ScanManager.scantype;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvOperatorManager;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvOpFeature;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstPageDataID;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstProductType;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstSourceID;
import com.changhong.tvos.dtv.userMenu.MenuOperation;
import com.changhong.tvos.dtv.userMenu.MenuSearchGuide;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstServiceType;
import com.changhong.tvos.system.commondialog.CommonAcionDialog;
import com.changhong.tvos.system.commondialog.ScanWarnDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ChannelScanData extends ListPageData {
	Context mContext = null;
	int mSourceID = ConstSourceID.DVBC;
	DtvSourceManager mSourceManager = null;
	ItemHaveSubData mAutoScan = null;
	ItemHaveSubData mListScan = null;
	ItemHaveSubData mManualScan = null;
	ItemHaveSubData mScanSettings = null;
	ItemHaveSubData mSeartchGuide = null;
	private DtvChannelManager mChannelManager;

	SourceListData mSourceListData = null;
	SourceSetupData mSourceSetupData = null;
	OperatorListData mOperatorListData = null;
	ItemHaveSubData mOperator = null;
	private DtvOperatorManager mOperatorManager = null;
	ItemHaveSubData mCurSource;

	public ChannelScanData(String strTitle, int picTitle, Context context) {
		super(ConstPageDataID.CHANNEL_SCAN_DATA, strTitle, picTitle);
		this.mContext = context;
		// this.mType = EnumPageType.NarrowListPage;
		this.mType = EnumPageType.BroadListPage;
		mSourceManager = DtvSourceManager.getInstance();
		mSourceID = mSourceManager.getCurSourceID();
		this.init();
	}

	private void init() {
		mChannelManager = DtvChannelManager.getInstance();
		mOperatorManager = DtvOperatorManager.getInstance();

		mAutoScan = new ItemHaveSubData(R.drawable.channel_scan_auto_scan, getContext().getResources().getString(R.string.dtv_channelscan_auto_scan), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public void onNextPage() {
				// TODO Auto-generated method stub
				final ScanWarnDialog dialog = new ScanWarnDialog(mContext);
				dialog.setOnShowListener(new DialogInterface.OnShowListener() {

					@Override
					public void onShow(DialogInterface arg0) {
						dialog.setShowTV(false);
					}
				});
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						if (dialog.isShowTV()) {
							MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
							dialog.setShowTV(false);
						}
					}
				});
				dialog.show();
				dialog.getmYesButton().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						MenuScan menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanAuto);
						menuAutoScan.show();
						dialog.dismiss();
						menuAutoScan.setOnDismissListener(new DialogInterface.OnDismissListener() {

							@Override
							public void onDismiss(DialogInterface dialog) {
								// TODO Auto-generated method
								// stub
								// MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_VISIBLE);
								showDialogFilter(mContext);
							}
						});
						MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_INVISIBLE);
					}
				});
			}
		};
		mAutoScan.isOnlyShow = true;
		mListScan = new ItemHaveSubData(R.drawable.channel_scan_list_scan, getContext().getResources().getString(R.string.dtv_channelscan_list_scan), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				DtvOpFeature opFeature = DtvOperatorManager.getInstance().getOpFeature();
				if (null != opFeature && opFeature.isHideListScan()) {
					return 0;
				}
				return 1;
			}

			@Override
			public void onNextPage() {
				// TODO Auto-generated method stub
				final ScanWarnDialog dialog = new ScanWarnDialog(mContext);
				dialog.setOnShowListener(new DialogInterface.OnShowListener() {

					@Override
					public void onShow(DialogInterface arg0) {
						dialog.setShowTV(false);
					}
				});
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						if (dialog.isShowTV()) {
							MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
							dialog.setShowTV(false);
						}
					}
				});
				dialog.show();
				dialog.getmYesButton().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						MenuScan menuListScan = new MenuScan(mContext, scantype.DTV_ScanList);
						menuListScan.show();
						dialog.dismiss();
						menuListScan.setOnDismissListener(new DialogInterface.OnDismissListener() {

							@Override
							public void onDismiss(DialogInterface dialog) {
								// TODO Auto-generated method
								// stub
								// MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_VISIBLE);
								showDialogFilter(mContext);
							}
						});
						MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_INVISIBLE);
					}
				});

			}
		};
		mListScan.isOnlyShow = true;
		mManualScan = new ItemHaveSubData(R.drawable.channel_scan_manual_scan, getContext().getResources().getString(R.string.dtv_channelscan_manual_scan), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public void onNextPage() {
				// TODO Auto-generated method stub
				final MenuScan menuManualScan = new MenuScan(mContext, scantype.DTV_ScanMaunal);
				menuManualScan.show();
				menuManualScan.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						if (menuManualScan.isShowTv) {
							MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
						} else if (menuManualScan.isShowFilter()) {
							showDialogFilter(mContext);
						} else {
							MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_VISIBLE);
						}
					}
				});
				MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_INVISIBLE);
			}
		};
		mManualScan.isOnlyShow = true;
		mScanSettings = new ItemHaveSubData(R.drawable.channel_scan_settings, getContext().getResources().getString(R.string.dtv_channelscan_scansettings), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public void onNextPage() {
				// TODO Auto-generated method stub
			}
		};

		mSeartchGuide = new ItemHaveSubData(R.drawable.dtv_menu_guide_icon, getContext().getResources().getString(R.string.dtv_search_guide_title), 0, 0) {

			@Override
			public void onNextPage() {
				// TODO Auto-generated method stub
				final MenuSearchGuide guideMenu = new MenuSearchGuide(mContext);
				guideMenu.show();
				guideMenu.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						if (guideMenu.isShowTv()) {
							MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
						}
					}
				});

				guideMenu.setOnSureListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						MenuScan menuAutoScan = new MenuScan(mContext, scantype.DTV_ScanAuto);
						menuAutoScan.show();
						guideMenu.dismiss();
						menuAutoScan.setOnDismissListener(new DialogInterface.OnDismissListener() {

							@Override
							public void onDismiss(DialogInterface dialog) {
								// TODO Auto-generated method
								// stub
								// MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_VISIBLE);
								showDialogFilter(mContext);
							}
						});
						MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_INVISIBLE);
					}
				});
			}

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

		};
		mSeartchGuide.isOnlyShow = true;

		mCurSource = new ItemHaveSubData(R.drawable.menu_source_in, getContext().getResources().getString(R.string.scan_source), 0, 0) {

			@Override
			public void onNextPage() {
				// TODO Auto-generated method stub

			}

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub

				return 1;
			}

		};

		//		mCurSource.setValue(mSourceManager.getCurSourceName());
		mCurSource.isOnlyShow = false;
		mCurSource.setImmediateFlag(true);

		//		String cityStr = mOperatorManager.getCurOperator().getOperatorName();

		mOperator = new ItemHaveSubData(R.drawable.setting_operator_icon, getContext().getResources().getString(R.string.dtv_scansettings_operator_setup), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				if (mSourceManager.getProductType() == ConstProductType.PRODUCT_T) {
					// 单T 不可选
					return 0;
				}
				return 1;
			}

			@Override
			public void onNextPage() {
				// TODO Auto-generated method stub
				MenuOperation op = new MenuOperation(mContext);
				op.setShowTv(false);
				op.show();
			}

		};
		//		mOperator.setValue(cityStr);

		mOperator.isOnlyShow = true;
		//		mOperator.IsRealTimeData = true;
		mCurSource.IsRealTimeData = true;
		this.add(mSeartchGuide);
		this.add(mAutoScan);
		this.add(mListScan);
		this.add(mManualScan);
		this.add(mScanSettings);
		this.add(mCurSource);
		this.add(mOperator);
		ChannelScanSetupData channelScanSetupData = new ChannelScanSetupData(mContext.getString(R.string.dtv_menu_channel_scan_setup_title), R.drawable.dtv_menu_scan_setup_pic_title, mContext);
		this.registPageData(channelScanSetupData);
		this.setNextPage(this.getItemDataIndexInPage(mScanSettings), channelScanSetupData);
		mSourceListData = new SourceListData(mContext.getString(R.string.scan_source), R.drawable.dtv_menu_system_information_pic_title, mContext);

		if (null != mSourceManager.getSourceNameList() && mSourceManager.getSourceNameList().size() == 1) {

			if (mSourceManager.getProductType() == ConstProductType.PRODUCT_T) { // 单T
				this.removeItem(mCurSource);
				this.removeItem(mOperator);
			} else if (mSourceManager.getProductType() == ConstProductType.PRODUCT_C) {
				this.removeItem(mCurSource); // 单C
			}
		} else {
			this.registPageData(mSourceListData);
			this.setNextPage(getItemDataIndexInPage(mCurSource), mSourceListData);

		}

	}

	public Context getContext() {
		return mContext;
	}

	private void showDialogFilter(Context context) {
		if (null == mChannelManager || null == mChannelManager.getChannelList() || mChannelManager.getChannelList().size() == 0
				|| DtvChannelManager.getInstance().getCurChannelType() == ConstServiceType.SERVICE_TYPE_RADIO) {
			Log.d("ScanOver", "showDialogFilter-->AfterScan NO CHANNEL FOUND");
			MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_VISIBLE);
			return;
		}
		final FilterChannels filterChannels = FilterChannels.getInstance(context);
		filterChannels.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if (filterChannels.isShowTv()) {
					MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
				} else {
					MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_VISIBLE);
				}
			}
		});

		final CommonAcionDialog chooseOrnotDialog = new CommonAcionDialog(context, 0, R.string.dtv_filter_title, 0, 10);
		chooseOrnotDialog.setCancelable(true);
		chooseOrnotDialog.setDefaultFocusButton(CommonAcionDialog.FOCUS_BUTTON_CANCEL);
		chooseOrnotDialog.setDuration(30);
		chooseOrnotDialog.setOkButtonText(R.string.yes);
		chooseOrnotDialog.setCancelButtonText(R.string.no);
		chooseOrnotDialog.setOKButtonListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				filterChannels.show();
				filterChannels.startSmartSkip();
				chooseOrnotDialog.dismiss();
			}
		});

		chooseOrnotDialog.setCancelButtonListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				chooseOrnotDialog.dismiss();
				// MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_VISIBLE);
			}
		});
		chooseOrnotDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if (chooseOrnotDialog.isShowTV()) {
					MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
				} else if (filterChannels.isShowing()) {

				} else {
					MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_VISIBLE);
				}
			}
		});
		chooseOrnotDialog.setShowTV(false);
		chooseOrnotDialog.show();
	}

	@Override
	public void updatePage() {
		// TODO Auto-generated method stub
		Log.i("ChannelScanData", "ChannelScanData enter updatePage()");
		if (mSourceManager.getCurSourceID() != mSourceID) {
			this.updatePageView();
			mSourceID = mSourceManager.getCurSourceID();
		}
		//		if (null != mCurSource) {
		//			
		//			mCurSource.setValue(mSourceManager.getCurSourceName());
		//			Log.i("ChannelScanData", "getCurSourceName:" + mSourceManager.getCurSourceName());
		//		}
		//
		//		if (null != mOperator) {
		//			String cityStr = mOperatorManager.getCurOperator()
		//					.getOperatorName();
		//			mOperator.setValue(cityStr);
		//			Log.i("ChannelScanData", "cityStr:" + cityStr);
		//		}
		super.updatePage();
	}

	public void updatePageView() {
		// TODO Auto-generated method stub
		if (null != mListScan) {
			mListScan.update();
			Log.i("update", "updatePageView() -->> mListScan.update");
		} else {
			Log.i("update", "null is ");
			init();
		}
	}
}
