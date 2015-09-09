package com.changhong.menudata.menuPageData;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.changhong.data.pageData.ListPageData;
import com.changhong.data.pageData.PageData;
import com.changhong.data.pageData.itemData.ItemHaveSubData;
import com.changhong.data.pageData.itemData.util.ItemData;
import com.changhong.menudata.MainMenuReceiver;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvCicaManager;
import com.changhong.tvos.dtv.tvap.DtvConfigManager;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvCardStatus;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstPageDataID;
import com.changhong.tvos.dtv.vo.DTVCardStatus.CardStatus;
import com.changhong.tvos.system.commondialog.CommonInfoDialog;
import com.changhong.tvos.system.commondialog.ScanWarnDialog;

public class SystemSetupData extends ListPageData {

	private static final String TAG = SystemSetupData.class.getSimpleName();
	private static final String DEBUG_CODE = "8888";
	private String mInputStr = "";
	private DebugData mDebugdata;
	Context mContext;
	CommonInfoDialog mDialog = null;
	ItemData mCASystem;
	ItemData mAudioSetup;
	ItemHaveSubData mAerialTypeSetup;
	ItemData mPasswordSetup;
	ItemData mModuleUpgrade;
	ItemData mInformation;
	ItemData mFactorySettings;
	Handler mHandler;
	//	Runnable mCheckCardRun;
	private boolean mSetSelected = true;
	private boolean isCACardEable = false;

	public SystemSetupData(String strTitle, int picTitle, Context context) {
		// TODO Auto-generated constructor stub
		super(ConstPageDataID.SYSTEM_SET_UP, strTitle, picTitle);
		mContext = context;
		//		mType = EnumPageType.NarrowListPage;
		mType = EnumPageType.BroadListPage;
		this.init();
		mHandler = new Handler();
		//		mCheckCardRun = new Runnable() {
		//			
		//			@Override
		//			public void run() {
		//				// TODO Auto-generated method stub
		//				if(mCASystem!=null){
		//					if(isCACardEable==false&&mCASystem.isEnable()>0){
		//						
		//						mCASystem.getView().update();
		//					}else if(isCACardEable==true&&mCASystem.isEnable()<=0){
		//						
		//						mCASystem.getView().update();
		//					}
		//				}
		//				Log.i(TAG,"LL onCheckCardRun()***");
		//				mHandler.postDelayed(mCheckCardRun, 1000);
		//			}
		//		};
	}

	public void updatePage() {
		Log.i(TAG, "LL updatePage()***");
		//		mHandler.removeCallbacks(mCheckCardRun);
		//		mHandler.post(mCheckCardRun);
		String curSourceName = DtvSourceManager.getInstance().getCurSourceName();
		mAerialTypeSetup.setValue(curSourceName);
		Log.i(TAG, "LL mSetSelected = " + mSetSelected + ",curSourceName = " + curSourceName);
		if (mSetSelected == true) {
			loadItemList();
			ItemData itemData = super.getCurItem();
			if (itemData == null || itemData.isEnable() <= 0) {
				setCurItemIndex(getFirstSelectedIndex());
			}
			loadCurItemList();
			updateRealTimeData();
			mSetSelected = false;
		} else {
			super.updatePage();
		}
	}

	public boolean onkeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			mSetSelected = true;
		case KeyEvent.KEYCODE_MENU:
		case KeyEvent.KEYCODE_CHANGHONGKB_MENU:
		case KeyEvent.KEYCODE_SOURCE:
			//			case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBOX:
		case 4126://KEYCODE_CHANGHONGIR_TOOLBOX
		case 170://KEYCODE_CHANGHONGIR_TV
		case 4135:
			//			mHandler.removeCallbacks(mCheckCardRun);
			break;
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
			mInputStr += (keyCode - KeyEvent.KEYCODE_0);
			if (mInputStr.contains(DEBUG_CODE)) {
				if (null == mDebugdata) {
					mDebugdata = new DebugData(getContext().getResources().getString(R.string.dtv_menu_system_debug), 0, mContext);
				}
				this.onPageTurn(mDebugdata);
				mInputStr = "";
			} else {

			}
		default:
			break;
		}
		return super.onkeyDown(keyCode, event);
	}

	public void reset() {
		mSetSelected = true;
		//		mHandler.removeCallbacks(mCheckCardRun);
		Log.i(TAG, "SystemSetupData remove mCheckCardRun");
		super.reset();
	}

	private void init() {
		String curSourceName = DtvSourceManager.getInstance().getCurSourceName();
		mDialog = new CommonInfoDialog(mContext);
		mCASystem = new ItemHaveSubData(R.drawable.system_setup_ca_settings, getContext().getResources().getString(R.string.dtv_systemsetup_CA_system), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				DtvCardStatus mCardStatus = DtvCicaManager.getCardStatus();
				if (mCardStatus != null && mCardStatus.getCardStatus() == CardStatus.CARD_STATUS_OK) {
					isCACardEable = true;
					return 1;
				}
				isCACardEable = false;
				return 0;
			}

			@Override
			public void onNextPage() {
				// TODO Auto-generated method stub
				Log.i(TAG, "LL click CICAInfo items***");
				DtvCardStatus mCardStatus = DtvCicaManager.getCardStatus();
				if (mCardStatus != null && mCardStatus.getCardStatus() == CardStatus.CARD_STATUS_OK) {
					MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
					Intent intent = new Intent(MainMenuReceiver.INTENT_CICAQUERY);
					intent.putExtra(MainMenuReceiver.DATA_CARDTYPE, mCardStatus.getCardType());
					mContext.sendBroadcast(intent);

				} else {
					Log.e(TAG, "LL no card or card exception***");
					mDialog.setMessage(R.string.dtv_menu_no_cica_prompt);
					mDialog.show();
				}
			}
		};
		mCASystem.isOnlyShow = true;
		mCASystem.IsRealTimeData = true;
		mAudioSetup = new ItemHaveSubData(R.drawable.system_setup_audio_settings, getContext().getResources().getString(R.string.dtv_systemsetup_audio_settings), 0, 0) {

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
		mPasswordSetup = new ItemHaveSubData(R.drawable.system_setup_password_settings, getContext().getResources().getString(R.string.dtv_systemsetup_password_settings), 0, 0) {

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
		mModuleUpgrade = new ItemHaveSubData(R.drawable.system_setup_module_upgrade, getContext().getResources().getString(R.string.dtv_systemsetup_module_upgrade), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public void onNextPage() {
				// TODO Auto-generated method stub
				//唐超 add start
				Log.i(TAG, "LL dtvUpgrade>>onNextPage()***");
				Intent mintent = new Intent();
				mintent.setAction("com.changhong.dtvloader.startMainMenu");//启动升级界面的广播
				getContext().sendBroadcast(mintent);
				MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
				//唐超 add end
			}
		};
		mModuleUpgrade.isOnlyShow = true;
		mInformation = new ItemHaveSubData(R.drawable.system_setup_information, getContext().getResources().getString(R.string.dtv_systemsetup_information), 0, 0) {

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

		mFactorySettings = new ItemHaveSubData(R.drawable.system_setup_factory_settings, getContext().getResources().getString(R.string.dtv_systemsetup_factory_settings), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public void onNextPage() {

				// TODO Auto-generated method stub
				final ScanWarnDialog dialog = new ScanWarnDialog(mContext);
				dialog.setDisplayString(mContext.getResources().getString(R.string.no_string), mContext.getResources().getString(R.string.dtv_reset_warn_info));
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
						//						new Handler().post(new Runnable() {
						//							
						//							@Override
						//							public void run() {
						// TODO Auto-generated method stub
						Log.i(TAG, "LL Start Click the BuutonYes###");
						DtvChannelManager.getInstance().reset();
						DtvConfigManager.getInstance().clearAll();
						MenuManager.getInstance().delAllScheduleEvents();
						PageData pageData = SystemSetupData.this.getPageDataById(ConstPageDataID.CHANNEL_SCAN_SETUP_DATA);
						if (pageData != null && pageData instanceof ChannelScanSetupData) {

							((ChannelScanSetupData) pageData).updatePageData();
						}

						dialog.dismiss();
						Log.i(TAG, "LL End Click the BuutonYes###");
						//							}
						//						});
					}
				});
			}
		};
		mFactorySettings.isOnlyShow = true;
		mAerialTypeSetup = new ItemHaveSubData(R.drawable.system_setup_audio_settings, getContext().getResources().getString(R.string.dtv_systemsetup_aerial_type_settings), 0, 0) {

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
		mAerialTypeSetup.IsRealTimeData = true;
		mAerialTypeSetup.setValue(curSourceName);

		this.add(mCASystem);
		this.add(mAudioSetup);
		//		this.add(mAerialTypeSetup);
		//		this.add(mPasswordSetup);
		this.add(mModuleUpgrade);
		this.add(mInformation);
		this.add(mFactorySettings);
		AudioData audioData = new AudioData(mContext.getString(R.string.dtv_menu_audio_setup_title), R.drawable.dtv_menu_audio_setup_pic_title, mContext);
		VersionInfoData versionData = new VersionInfoData(mContext.getString(R.string.dtv_systemsetup_information), R.drawable.dtv_menu_system_information_pic_title, mContext);
		versionData.isFoucsAble = false;
		//		SourceListData sourceListData = new SourceListData(mContext.getString(R.string.dtv_scansettings_source_setup_title), R.drawable.dtv_menu_system_information_pic_title, mContext);
		this.setNextPage(1, audioData);
		//		this.setNextPage(2, sourceListData);
		//		this.setNextPage(4, versionData);
		this.setNextPage(3, versionData);

	}

	public Context getContext() {
		return mContext;
	}

}
