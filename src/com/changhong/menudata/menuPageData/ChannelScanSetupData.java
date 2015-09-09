package com.changhong.menudata.menuPageData;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.changhong.data.pageData.ListPageData;
import com.changhong.data.pageData.itemData.ItemHaveSubData;
import com.changhong.data.pageData.itemData.ItemOptionData;
import com.changhong.data.pageData.itemData.ItemOptionInputData;
import com.changhong.menudata.MainMenuReceiver;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.DtvConfigManager;
import com.changhong.tvos.dtv.tvap.DtvInterface;
import com.changhong.tvos.dtv.tvap.DtvOperatorManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvTunerInfo;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstPageDataID;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstScanParams;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstSourceID;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstStringKey;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstDemodType;
import com.changhong.tvos.system.commondialog.CommenInputDialog;
import com.changhong.tvos.system.commondialog.CommonAcionDialog;
import com.changhong.tvos.system.commondialog.CommonInfoDialog;

/**
 * 申明 目前节目收索设置运营商在清流单T下是不可选的。 如以后需要扩展，扩展机器类型即可
 * @author Administrator
 *
 */
public class ChannelScanSetupData extends ListPageData {
	Context mContext = null;
	private static final String TAG = ChannelScanSetupData.class.getSimpleName();
	private DtvOperatorManager mOperatorManager = null;
	private CommonAcionDialog mCommonAcionDialog = null;
	int mSourceID = ConstSourceID.DVBC;
	DtvSourceManager mSourceManager = null;
	SourceListData mSourceListData = null;
	SourceSetupData mSourceSetupData = null;
	OperatorListData mOperatorListData = null;
	ItemHaveSubData mOperator = null;
	ItemOptionInputData mFrequency = null;
	ItemOptionData mSymbolRate = null;
	ItemOptionData mQam = null;

	ItemHaveSubData mCurSource;

	ItemOptionInputData mi_FreqKHz; /* 下行频率，单位KHz */
	//	ItemOptionInputData mi_NCOFreqKHz; /* NCO频率，单位KHz */
	//	ItemOptionData mi_CarrierMode; /* 载波模式 */
	//	ItemOptionData mi_LDPCRate; /* LDCP（内码纠错）码率 */
	//	ItemOptionData mi_FrameHeader; /* 帧头格式 */
	//	ItemOptionData mi_InterleaverMod; /* 交织模式 */

	ItemOptionData mBandWidth;

	String[] mFrequencyStr = null;
	String[] mSymbolRateStr = null;
	String[] mQamStr = null;

	String[] mi_NCOFreqStr = null;
	String[] mi_CarrierStr = null;
	String[] mi_LDPCRateStr = null;
	String[] mi_FrameHeaderStr = null;
	String[] mi_InterleaverModStr = null;

	CommonInfoDialog mydialog = null;
	private boolean mSetSelected = true;
	private CommenInputDialog inputDialog = null;

	private int dialog_height = 70;
	private int dialog_width = 520;
	private int dialog_margin = 480;
	private int dialog_margin_y = 30;

	public ChannelScanSetupData(String strTitle, int picTitle, Context context) {
		super(ConstPageDataID.CHANNEL_SCAN_SETUP_DATA, strTitle, picTitle);
		mContext = context;
		mType = EnumPageType.BroadListPage;
		mOperatorManager = DtvOperatorManager.getInstance();
		mSourceManager = DtvSourceManager.getInstance();
		mSourceID = mSourceManager.getCurSourceID();
		DisplayMetrics mDisplayMetrics = mContext.getResources().getDisplayMetrics();
		dialog_width = (int) (dialog_width * mDisplayMetrics.scaledDensity);
		dialog_height = (int) (dialog_height * mDisplayMetrics.scaledDensity);
		dialog_margin = (int) (dialog_margin * mDisplayMetrics.scaledDensity);

		this.init();
	}

	protected void updatePageData() {

		removeCurPageData();
		reset();
		init();
		updatePage();
	}

	public void updatePage() {
		Log.i(TAG, "LL updatePage()>>mSetSelected = " + mSetSelected);
		if (mSetSelected == true) {
			if (mSourceManager.getCurSourceID() != mSourceID) {
				removeCurPageData();
				reset();
				init();
				mSourceID = mSourceManager.getCurSourceID();
			}
			loadItemList();
			setCurItemIndex(getFirstSelectedIndex());
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
			break;
		default:
			break;
		}
		return super.onkeyDown(keyCode, event);
	}

	public void reset() {
		mSetSelected = true;
		super.reset();
	}

	private void init() {

		// 获取可选数组
		mFrequencyStr = this.getAllFreTable();
		String cityStr = mOperatorManager.getCurOperator().getOperatorName();

		int demode = DtvSourceManager.getInstance().getCurDemodeType();

		//		mCurSource = new ItemHaveSubData(0, getContext().getResources()
		//				.getString(R.string.scan_source), 0,
		//				0){
		//
		//					@Override
		//					public void onNextPage() {
		//						// TODO Auto-generated method stub
		//						
		//					}
		//
		//					@Override
		//					public int isEnable() {
		//						// TODO Auto-generated method stub
		//						if(mSourceManager.getSourceList()!=null && mSourceManager.getSourceList().size()>1)
		//						{
		//							return 1;
		//						}
		//						return 0;
		//					}
		//
		//			
		//		};
		//		
		//		mCurSource.setValue(DtvSourceManager.getInstance().getCurSourceName());
		//		mCurSource.isOnlyShow = false;
		//		mCurSource.mIsDismissArrow = true;
		//		mCurSource.setImmediateFlag(true);
		//		
		//		mOperator = new ItemHaveSubData(0, getContext().getResources()
		//				.getString(R.string.dtv_scansettings_operator_setup), 0, 0) {
		//
		//			@Override
		//			public int isEnable() {
		//				// TODO Auto-generated method stub
		//				if(mSourceManager.getProductType() == ConstProductType.PRODUCT_T){
		//					//单T 不可选
		//					return 0;
		//				}
		//				return 1;
		//			}
		//
		//			@Override
		//			public void onNextPage() {
		//				// TODO Auto-generated method stub
		//				Log.i(TAG, "mOperator.onNext");
		////				showInputPassworldDialog();
		//				MenuOperation op = new MenuOperation(mContext);
		//				op.setShowTv(false);
		//				op.show();
		//			}
		//
		//			@Override
		//			public  boolean onkeyDown(int keyCode ,KeyEvent event){
		//				switch (keyCode) {
		//					case KeyEvent.KEYCODE_DPAD_CENTER:
		//					case KeyEvent.KEYCODE_ENTER:
		//					case KeyEvent.KEYCODE_DPAD_RIGHT:
		//					     onNextPage();
		//					     return true;
		//				}
		//				
		//				return false;
		//			}
		//		};
		//		mOperator.setValue(cityStr);
		//
		//		mOperator.isOnlyShow = false;
		//		
		//		this.add(mCurSource);
		//		this.add(mOperator);
		//		Log.i(TAG, "getProductType() "+ mSourceManager.getProductType());
		//		if(mSourceManager.getSourceList()!=null&&mSourceManager.getSourceList().size()>1){
		//			mCurSource.isOnlyShow = true;
		//			mCurSource.mIsDismissArrow = false;
		//			if(mSourceListData!=null){
		//				this.unRegistPageData(mSourceListData);
		//			}
		//			mSourceListData = new SourceListData(mContext.getString(R.string.scan_source), R.drawable.dtv_menu_system_information_pic_title, mContext);
		//			this.registPageData(mSourceListData);
		//			this.setNextPage(0, mSourceListData);
		//			if(mSourceSetupData!=null){
		//				this.registPageData(mSourceSetupData);
		//			}
		//			
		//			mSourceSetupData = new SourceSetupData(getContext().getResources().getString(R.string.scan_source),
		//					0, mContext);
		//			this.registPageData(mSourceSetupData);
		//			this.setNextPage(1, mSourceSetupData);
		//		}
		//		else{
		//			if(mOperatorListData!=null){
		//				this.unRegistPageData(mOperatorListData);
		//			}
		//			mOperatorListData = new OperatorListData(getContext()
		//					.getString(R.string.dtv_scansettings_operator_setup_title),
		//					R.drawable.menu_operator_setup_pic_title,mContext);
		//			
		//			if(mSourceManager.getProductType() != ConstProductType.PRODUCT_T){
		//			this.registPageData(mOperatorListData);
		//			this.setNextPage(1, mOperatorListData);
		//			}else {
		//				mOperator.isOnlyShow = false;
		//				mCurSource.mIsDismissArrow = true;
		//			}
		//			
		//		}
		if (ConstDemodType.DVB_C == demode) {
			init_DTV_C();
		} else if (ConstDemodType.DMB_TH == demode) {

			init_DBMT();
		}
	}

	protected void showInputPassworldDialog() {
		// TODO Auto-generated method stub
		if (null == inputDialog) {
			inputDialog = new CommenInputDialog(mContext);
			inputDialog.setCancelable(true);
			inputDialog.setDuration(30);
			inputDialog.setEncode(true);
			inputDialog.setTitle(R.string.code_input);
			inputDialog.setMessage(R.string.code_string);
			inputDialog.setOKButtonListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					inputDialog.dismiss();
					String password = inputDialog.getInputWorld();
					if (null != password && password.equals("8888")) {
						mOperator.onNextChildPage();
					} else {
						if (null == mydialog) {
							mydialog = new CommonInfoDialog(mContext);
							mydialog.setGravity(Gravity.BOTTOM | Gravity.LEFT, dialog_margin, dialog_margin_y);
							mydialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
							mydialog.info_layout.setLayoutParams(new FrameLayout.LayoutParams(dialog_width, dialog_height));
							mydialog.tv.setTextColor(Color.WHITE);
							mydialog.tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f);
							mydialog.setMessage(R.string.code_err_info);
						}
						mydialog.show();
					}
				}
			});
			inputDialog.setCancelButtonListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					inputDialog.dismiss();
				}
			});

			inputDialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface arg0) {
					// TODO Auto-generated method stub

				}
			});

		}

		inputDialog.show();
	}

	public void init_DTV_C() {

		String tmp = null;

		mSymbolRateStr = getContext().getResources().getStringArray(R.array.menu_scan_symbol_rate);
		mQamStr = getContext().getResources().getStringArray(R.array.menu_scan_modulation);

		tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_FREQ);
		int freValue = tmp == null ? mOperatorManager.getOPMainTunerInfo().getFrequency() : Integer.valueOf(tmp);
		tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_SYM);
		int symValue = tmp == null ? MainMenuReceiver.getIndexByItem(mSymbolRateStr, String.valueOf(mOperatorManager.getOPMainTunerInfo().getSymbolRate())) : MainMenuReceiver.getIndexByItem(
				mSymbolRateStr, tmp);

		mFrequency = new ItemOptionInputData(0, getContext().getResources().getString(R.string.scan_start_fre), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public boolean initData() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onValueChange(int Value) {
				// TODO Auto-generated method stub
				int tem = (Value > 10000) ? Value : Value * 1000;
				DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_SCAN_FREQ, String.valueOf(tem));
			}
		};
		mFrequency.mId = "frequencyKHz";
		mFrequency.setOptionValues(mFrequencyStr);
		mFrequency.setMinMaxValue(ConstScanParams.FREQUANCE_MIN_K_C, ConstScanParams.FREQUANCE_MAX_K / 1000);
		mFrequency.setCurValue(freValue);

		mSymbolRate = new ItemOptionData(0, getContext().getResources().getString(R.string.dtv_scansettings_symbol_rate_setup), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public boolean initData() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onValueChange(int Value) {
				// TODO Auto-generated method stub
				DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_SCAN_SYM, String.valueOf(this.getOptionStrs()[Value].replace("Kbps", "")));
			}
		};
		mSymbolRate.setOptionValues(this.concatString(mSymbolRateStr, "Kbps"));
		mSymbolRate.setCurValue(symValue);

		tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_QAM);
		int qamValue = tmp == null ? mOperatorManager.getOPMainTunerInfo().getQamMode() - 1 : Integer.valueOf(tmp);
		mQam = new ItemOptionData(0, getContext().getResources().getString(R.string.dtv_scansettings_qam_setup), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public boolean initData() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onValueChange(int Value) {
				// TODO Auto-generated method stub
				DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_SCAN_QAM, String.valueOf(Value));
			}
		};
		mQam.setOptionValues(this.concatString(mQamStr, "QAM"));
		mQam.setCurValue(qamValue);

		this.add(mFrequency);
		this.add(mSymbolRate);
		this.add(mQam);

	}

	public void init_DBMT() {
		DtvTunerInfo tunerInfo = DtvInterface.getInstance().getDBMTTunerInfo();
		if (null == tunerInfo) {
			Log.e(TAG, "LL tunerInfo == null");
			return;
		}
		mQamStr = getContext().getResources().getStringArray(R.array.menu_scan_modulation_dmbt);
		String tmp = null;
		mi_FreqKHz = new ItemOptionInputData(0, getContext().getResources().getString(R.string.scan_start_fre), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public boolean initData() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onValueChange(int Value) {
				// TODO Auto-generated method stub

				int tem = (Value > 10000) ? Value : Value * 100;
				DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_SCAN_LOW_FREQ, String.valueOf(tem));
			}
		};
		tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_LOW_FREQ);

		int low_fre = (tmp == null ? tunerInfo.getMi_FreqKHz() : Integer.valueOf(tmp));
		mi_FreqKHz.setMinMaxValue(ConstScanParams.FREQUANCE_MIN_K_T, ConstScanParams.FREQUANCE_MAX_K / 100);
		mi_FreqKHz.setOptionValues(mFrequencyStr);
		mi_FreqKHz.setCurValue(low_fre);
		mi_FreqKHz.mId = "frequencyKHz";

		tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_BANDWIDTH);
		mBandWidth = new ItemOptionData(0, getContext().getResources().getString(R.string.scan_bandwidth), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public boolean initData() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onValueChange(int Value) {
				// TODO Auto-generated method stub
				DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_BANDWIDTH, String.valueOf(Value));
			}
		};
		mBandWidth.setOptionValues(new String[] { "8 MHz" });
		mBandWidth.setCurValue(0);

		//		this.add(mi_CarrierMode);
		this.add(mi_FreqKHz);
		//		this.add(mi_NCOFreqKHz);
		//		this.add(mi_LDPCRate);
		//		this.add(mi_FrameHeader);
		//		this.add(mi_InterleaverMod);
		// 		this.add(mBandWidth);

	}

	public Context getContext() {
		return mContext;
	}

	private String[] getAllFreTable() {
		String[] freTable = null;

		int index = 0;
		int fre;
		if (ConstDemodType.DVB_C == mSourceManager.getCurDemodeType()) {
			freTable = new String[96];
			fre = ConstScanParams.FREQUANCE_MIN_K_C;
			for (; fre < 474000 && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
				freTable[index] = String.valueOf(fre);
			}
			for (fre = 474000; fre <= ConstScanParams.FREQUANCE_MAX_K && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {

				freTable[index] = String.valueOf(fre);
			}
		} else {

			freTable = new String[57];
			fre = ConstScanParams.FREQUANCE_MIN_K_T;
			for (; fre < 70000 && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
				freTable[index] = String.valueOf(fre);
			}
			freTable[index++] = String.valueOf(80000);
			freTable[index++] = String.valueOf(88000);
			for (fre = 171000; fre <= 219000 && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
				freTable[index] = String.valueOf(fre);
			}
			for (fre = 474000; fre <= 562000 && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
				freTable[index] = String.valueOf(fre);
			}
			for (fre = 610000; fre <= ConstScanParams.FREQUANCE_MAX_K && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {

				freTable[index] = String.valueOf(fre);
			}
		}

		return freTable;
	}

	private String[] concatString(String[] str1, String str2) {
		String[] strArray = null;

		if (str1 != null) {
			strArray = new String[str1.length];
			for (int i = 0; i < str1.length; i++) {
				strArray[i] = str1[i].concat(str2);
				//				strArray[i] = str2.concat(str1[i]);
			}
		}
		return strArray;
	}

	public void removeCurPageData() {

		removeItem(mCurSource);
		removeItem(mOperator);
		removeItem(mFrequency);
		removeItem(mQam);
		removeItem(mSymbolRate);
		removeItem(mi_FreqKHz);
		removeItem(mBandWidth);
	}

}
