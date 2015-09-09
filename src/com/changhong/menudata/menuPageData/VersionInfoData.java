package com.changhong.menudata.menuPageData;

import com.changhong.data.pageData.ListPageData;
import com.changhong.data.pageData.itemData.ItemPromptData;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvOperatorManager;
import com.changhong.tvos.dtv.tvap.DtvSoftWareInfoManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvCardStatus;
import com.changhong.tvos.dtv.tvap.baseType.DtvOperator;
import com.changhong.tvos.dtv.tvap.baseType.DtvVersion;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstOperatorCode;
import com.changhong.tvos.dtv.vo.DTVCardStatus.CardStatus;
import com.changhong.tvos.dtv.vo.DTVCardStatus.CardType;
import android.content.Context;
import android.util.Log;

public class VersionInfoData extends ListPageData{
	private static final String TAG = VersionInfoData.class.getSimpleName();
	Context mContext = null;
	//ItemPromptData mVendor = null;
	ItemPromptData mOperator = null;
	ItemPromptData mCardType = null;
	ItemPromptData mCardStatus = null;
	ItemPromptData mCAS = null;
////////////////////////////////////////////////
    ItemPromptData mHardVer =null;
////////////////////////////////////////////////
	
	ItemPromptData mAppVer = null;
	ItemPromptData mDriverVer = null;
	ItemPromptData mDate = null;
	ItemPromptData mReleaseDate = null;
	DtvOperatorManager mOperatorManager ;
	public VersionInfoData(String strTitle, int picTitle,Context context) {
		super(strTitle,picTitle);
		mContext = context;
		mType = EnumPageType.BroadListPage;
		this.isFoucsAble = false;
		mOperatorManager = DtvOperatorManager.getInstance();
		this.init();
	}

	public void updatePage(){
		Log.i(TAG,"LL updatePage***");
		this.updatePageData();
		removeItems();
		Log.i(TAG, "mOperatorManager.getCurOperator() "+ mOperatorManager.getCurOperator().getOperatorCode()
				+"  " +mOperatorManager.getCurOperator().getOperatorName());
		if(mOperatorManager.getCurOperator().getOperatorCode() != 
			ConstOperatorCode.DTMB_OP_CODE_COMMON_CHCA){
			this.add(mCardType);
			this.add(mCardStatus);
		}
		
//		this.add(mCAS);
        this.add(mHardVer);
		this.add(mAppVer);
		this.add(mReleaseDate);
		this.add(mDriverVer);
		this.add(mDate);
		
		super.updatePage();
	}
	
	private void init() {
		
		mOperator = new ItemPromptData(0,getContext().getResources().getString(R.string.menu_soft_ware_operator_info),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mOperator.isOnlyShow = true;
		
		mCardType = new ItemPromptData(0,getContext().getResources().getString(R.string.menu_soft_ware_card_type_info),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mCardType.isOnlyShow = true;
		
		mCardStatus = new ItemPromptData(0,getContext().getResources().getString(R.string.menu_soft_ware_card_status_info),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mCardStatus.isOnlyShow = true;
		
		mCAS = new ItemPromptData(0,getContext().getResources().getString(R.string.menu_soft_ware_card_cas_info),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mCAS.isOnlyShow = true;
		
//////////////////////////////////////////////////////
		
mHardVer = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_info_hw_version),0,0) {

@Override
public int isEnable() {
// TODO Auto-generated method stub
return 1;
}
};
mHardVer.isOnlyShow = true;
//////////////////////////////////////////////////////	


		
		
		mAppVer = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_info_app_sw_version),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mAppVer.isOnlyShow = true;		
		
		mDriverVer = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_info_drv_sw_version),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mDriverVer.isOnlyShow = true;			
		
		mDate = new ItemPromptData(0,getContext().getResources().getString(R.string.menu_soft_ware_release_time),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mDate.isOnlyShow = true;
		
		mReleaseDate = new ItemPromptData(0,getContext().getResources().getString(R.string.menu_soft_ware_release_time),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mReleaseDate.isOnlyShow = true;
		
		this.updatePageData();
		
		//this.add(mVendor);
		this.add(mOperator);
//		this.add(mCardType);
//		this.add(mCardStatus);
//		this.add(mCAS);
//		this.add(mAppVer);
//		this.add(mReleaseDate);
//		this.add(mDriverVer);
//		this.add(mDate);
	}

	private void updatePageData() {
		String strNoInfo = mContext.getString(R.string.menu_soft_ware_no_info);
		String strCardType = mContext
				.getString(R.string.menu_soft_ware_card_type_unknown);
		String strCardStatus = mContext
				.getString(R.string.menu_soft_ware_card_status_exception);
		DtvOperator dtvOperator = DtvSoftWareInfoManager.getCurOperator();
		if (dtvOperator != null) {
			mOperator.setValue(dtvOperator.getOperatorName());
		} else {
			mOperator.setValue(strNoInfo);

		}
		DtvCardStatus dtvCardStatus = DtvSoftWareInfoManager.getCardStatus();
		if (dtvCardStatus != null) {
			switch (dtvCardStatus.getCardType()) {
			case CardType.CARD_TYPE_CI:
				strCardType = mContext
						.getString(R.string.menu_soft_ware_card_type_ci);
				break;
			case CardType.CARD_TYPE_CA:
				strCardType = mContext
						.getString(R.string.menu_soft_ware_card_type_ca);
				break;
			case CardType.CARD_TYPE_CHECK:
			case CardType.CARD_TYPE_NO_CARD:
			default:
				break;
			}
			
			switch (dtvCardStatus.getCardStatus()) {
			case CardStatus.CARD_STATUS_OK:
				strCardStatus = mContext
						.getString(R.string.menu_soft_ware_card_status_ok);
				break;
			case CardStatus.CARD_STATUS_OUT:
				strCardStatus = mContext
						.getString(R.string.menu_soft_ware_card_status_out);
				break;
			case CardStatus.CARD_STATUS_INSERT:
			case CardStatus.CARD_STATUS_CHECKING:
			case CardStatus.CARD_STATUS_INVALID:
			default:
				break;
			}
			mCardType.setValue(strCardType);
			mCardStatus.setValue(strCardStatus);
		} else {
			mCardType.setValue(strCardType);
			mCardStatus.setValue(strCardStatus);
		}
		mCAS.setValue(strNoInfo);
		

	    mHardVer.setValue(DtvSoftWareInfoManager.mHardVersion);

		mAppVer.setValue(DtvSoftWareInfoManager.mSoftwareVersion);
		
		mReleaseDate.setValue(DtvSoftWareInfoManager.releaseTime);
		DtvVersion dtvVersion = DtvSoftWareInfoManager.getDtvVersion();
		if (null != dtvVersion) {
			mDriverVer.setValue(dtvVersion.getHardwareVersion() + "."
					+ dtvVersion.getOpVersion() + "."
					+ dtvVersion.getAPIMainVersion() + "."
					+ dtvVersion.getAPISubVersion() + "."
					+ dtvVersion.getMainVersion() + "."
					+ dtvVersion.getSubVersion() + "."
					+ dtvVersion.getKOVersion() + ".r"
//					+ dtvVersion.getReleaseDateTime()
					);
			String buildTime = dtvVersion.getReleaseDateTime();
//			mDate.setValue(buildTime.substring(buildTime.indexOf("<") +1));
			if(null == buildTime || (-1 == buildTime.indexOf("<")) || (-1 ==buildTime.lastIndexOf(":"))){
				Log.e(TAG, "TvosService has died");
				buildTime = "<00:00:00:";
			}
				mDate.setValue(buildTime.substring(buildTime.indexOf("<") +1, buildTime.lastIndexOf(":")));
		}

	}

	public Context getContext() {
		return mContext;
	}

	public void removeItems(){
		this.removeItem(mCardType);
		this.removeItem(mCardStatus);
		this.removeItem(mHardVer);
		this.removeItem(mAppVer);
		this.removeItem(mReleaseDate);
		this.removeItem(mDriverVer);
		this.removeItem(mDate);
	}
}
