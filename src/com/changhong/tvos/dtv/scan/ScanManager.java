package com.changhong.tvos.dtv.scan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import com.changhong.menudata.MainMenuReceiver;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvConfigManager;
import com.changhong.tvos.dtv.tvap.DtvInterface;
import com.changhong.tvos.dtv.tvap.DtvOperatorManager;
import com.changhong.tvos.dtv.tvap.DtvScheduleManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvTunerInfo;
import com.changhong.tvos.dtv.tvap.baseType.DtvTunerStatus;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstScanParams;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstStringKey;
import com.changhong.tvos.dtv.vo.CarrierInfo;
import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DTVChannelBaseInfo;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.dtv.vo.DTVConstant.BroadcastConst;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstDemodType;
import com.changhong.tvos.dtv.vo.ScanStatusInfo;
import com.changhong.tvos.dtv.vo.ScanStatusInfo.ScanEvent;

public class ScanManager {
	private DtvOperatorManager mOperatorManager = DtvOperatorManager.getInstance();
	private DtvSourceManager mDemodeTypeManager = DtvSourceManager.getInstance();

	public enum scantype {
		DTV_ScanAuto, DTV_ScanList, DTV_ScanMaunal, DTV_ScanMaunal_Dmbt, DTV_ScanAutoExtra, DTV_ScanAutoExtra_Dmbt, DTV_ScanAutoExtra_SearchGuide,
	};

	private final static int SCAN_MODE_NIT = 1;
	private final static int SCAN_MODE_LIST = 2;
	private final static int SCAN_MODE_MANUAL = 3;

	public int demodType;
	public Context mContext;
	public static final String TAG = "ScanManager";
	public int scanProgress = 0;

	public int currentFrequency = 400000;
	public int symbolRate = 6875;
	//	public int modulMode = 64;
	public int modulMode = 5;

	public int resultOfDTV = 0;
	public int resultOfRadio = 0;
	public int resultOfData = 0;
	private int signalQuality = 0;
	private int signalStrength = 0;

	private int listFreMax;
	private int listFreMin;

	public scantype scanType;
	private static boolean isSearching = false;
	private int[] dtvScanFreTable;
	private int[] dtvScanSymbolRateTable;
	//	private int[] dtvScanModulModeTable;
	private String[] dtvScanModulModeTable;
	private ScanReceiver mScanReceiver = null;
	private IntentFilter mReceiverfilter = null;
	Handler handler;
	private DtvInterface dtvInterface = DtvInterface.getInstance();
	DtvTunerStatus tunerStatus = dtvInterface.getTunerStatus();
	private int miInterleaverMode;
	private int miCarrierMode;
	private int miLDPCRate;
	private int miFrameHeader;
	private int miFreq;
	int miNCOFrequency;

	public ScanManager(Context context, scantype type) {
		mContext = context;
		scanType = type;
		String[] mSymbolRateStr = mContext.getResources().getStringArray(R.array.menu_scan_symbol_rate);

		demodType = mDemodeTypeManager.getCurDemodeType();
		dtvScanSymbolRateTable = this.convertString2Integer(mSymbolRateStr);

		String tmp = null;
		if (demodType == ConstDemodType.DVB_C) {
			//			String[] mQamStr = mContext.getResources().getStringArray(
			//					R.array.menu_scan_modulation);
			//			dtvScanModulModeTable = this.convertString2Integer(mQamStr);
			dtvScanModulModeTable = mContext.getResources().getStringArray(R.array.menu_scan_modulation);
			tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_FREQ);
			int freValue = tmp == null ? DtvOperatorManager.getInstance().getOPMainTunerInfo().getFrequency() : Integer.valueOf(tmp);
			tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_SYM);
			int symValue = tmp == null ? MainMenuReceiver.getIndexByItem(mSymbolRateStr, String.valueOf(DtvOperatorManager.getInstance().getOPMainTunerInfo().getSymbolRate())) : MainMenuReceiver
					.getIndexByItem(mSymbolRateStr, tmp);
			tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_QAM);
			int qamValue = tmp == null ? DtvOperatorManager.getInstance().getOPMainTunerInfo().getQamMode() - 1 : Integer.valueOf(tmp);
			currentFrequency = freValue;
			if (null != dtvScanSymbolRateTable) {
				symbolRate = dtvScanSymbolRateTable[symValue];
			}

			Log.i(TAG, "LL ScanManager()>> ScanManager.symbolRate == " + symbolRate);
			//			modulMode = dtvScanModulModeTable[qamValue];
			if (qamValue >= 0 && qamValue < dtvScanModulModeTable.length) {
				modulMode = qamValue;
			} else {
				modulMode = 4;//default 64Qam
			}

			listFreMin = ConstScanParams.FREQUANCE_MIN_K_C;
			listFreMax = ConstScanParams.FREQUANCE_MAX_K;

		} else if (demodType == ConstDemodType.DMB_TH) {
			//			String[] mQamStr = mContext.getResources().getStringArray(
			//					R.array.menu_scan_modulation_dmbt);
			//			dtvScanModulModeTable = this.convertString2Integer(mQamStr);
			dtvScanModulModeTable = mContext.getResources().getStringArray(R.array.menu_scan_modulation_dmbt);
			DtvTunerInfo tunerInfo = DtvInterface.getInstance().getDBMTTunerInfo();
			if (null == tunerInfo) {
				Log.e(TAG, "LL tunerInfo == null");
				return;
			}
			tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_LOW_FREQ);
			int freValue = (tmp == null) ? tunerInfo.getMi_FreqKHz() : Integer.valueOf(tmp);

			tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_QAM);
			int qamValue = (tmp == null) ? tunerInfo.getQamMode() : Integer.valueOf(tmp);

			currentFrequency = freValue;
			Log.i(TAG, "LL ScanManager()>>currentFrequency = " + currentFrequency);
			//			modulMode = dtvScanModulModeTable[qamValue];
			if (qamValue >= 0 && qamValue < dtvScanModulModeTable.length) {

				modulMode = qamValue;
			} else {
				modulMode = 4;//default 64Qam
			}

			listFreMin = ConstScanParams.FREQUANCE_MIN_K_T;
			listFreMax = ConstScanParams.FREQUANCE_MAX_K_T;
		}
		Log.i(TAG, "LL ScanManager()>>demodType = " + demodType + ",modulMode = " + modulMode);
	}

	public void install() {
		if (null == mScanReceiver) {
			mScanReceiver = new ScanReceiver();
			mReceiverfilter = new IntentFilter(DTVConstant.SCAN_STATUS_BROADCAST);
			mContext.registerReceiver(mScanReceiver, mReceiverfilter);
			Log.i(TAG, "LL mScanReceiver was registed");
		}
	}

	public void unInstall() {
		if (null != mScanReceiver) {
			mContext.unregisterReceiver(mScanReceiver);
			mScanReceiver = null;
			Log.i(TAG, "LL mScanReceiver was unregisted");
		}
	}

	public static boolean isSearching() {
		return isSearching;
	}

	public int[] getFreTable() {

		int[] freTable = null;

		int index = 0;
		int fre;
		if (ConstDemodType.DVB_C == mDemodeTypeManager.getCurDemodeType()) {
			freTable = new int[96];
			fre = ConstScanParams.FREQUANCE_MIN_K_C;
			for (; fre < 474000 && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
				freTable[index] = fre;
			}
			for (fre = 474000; fre <= ConstScanParams.FREQUANCE_MAX_K && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {

				freTable[index] = fre;
			}
		} else {

			freTable = new int[57];
			fre = ConstScanParams.FREQUANCE_MIN_K_T;
			for (; fre < 70000 && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
				freTable[index] = fre;
			}
			freTable[index++] = fre;
			freTable[index++] = fre;
			for (fre = 171000; fre <= 219000 && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
				freTable[index] = fre;
			}
			for (fre = 474000; fre <= 562000 && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {
				freTable[index] = fre;
			}
			for (fre = 610000; fre <= ConstScanParams.FREQUANCE_MAX_K && index < freTable.length; fre = fre + ConstScanParams.FREQUANCE_BANDWIDTH, index++) {

				freTable[index] = fre;
			}
		}

		dtvScanFreTable = freTable;
		return dtvScanFreTable;
	}

	public int[] getSymbolRateTable() {
		return dtvScanSymbolRateTable;
	}

	//	public int[] getModulModeTable() {
	//		return dtvScanModulModeTable;
	//	}

	public String[] getModulModeTable() {
		return dtvScanModulModeTable;
	}

	public int getSignalLevel() {
		signalStrength = tunerStatus.getSignalLevel();
		return signalStrength;
	};

	public int getSignalQuality() {
		signalQuality = tunerStatus.getSignalQuality();
		return signalQuality;
	}

	public void scanStart(final Handler handler, int frequency, int symbolRate, int modulMode) {
		Log.v("tv", "dtvScanStart frequency =" + frequency + ",symbolRate = " + symbolRate + ",modulMode = " + modulMode);
		this.handler = handler;
		//		this.currentFrequency = frequency;
		//		this.symbolRate = symbolRate;
		//		this.modulMode = modulMode;
		resultOfDTV = 0;
		resultOfRadio = 0;
		resultOfData = 0;
		isSearching = true;
		switch (scanType) {
		case DTV_ScanAuto: {
			Log.i(TAG, "LL enter DTV_ScanAuto *** ");
			// DtvChannelManager.getInstance().getChannelList().clear();
			DtvChannelManager.getInstance().reset();
			DtvScheduleManager.getInstance().delAllScheduleEvents();
			List<DtvTunerInfo> opMianTunerList = mOperatorManager.getOPMainTunerList();

			DtvTunerInfo tunerInfo = new DtvTunerInfo(frequency, symbolRate, modulMode + 1);
			List<DtvTunerInfo> list = this.getFreqParams(opMianTunerList, tunerInfo);
			DtvTunerInfo[] tuners = null;
			if (null != list) {
				tuners = new DtvTunerInfo[list.size()];
				for (int index = 0; index < list.size(); index++) {
					//		tuners[index] = new DtvTunerInfo(list.get(index).getFrequency(), symbolRate, getQamModeIndex(modulMode));
					tuners[index] = new DtvTunerInfo(list.get(index).getFrequency(), symbolRate, modulMode + 1);
				}
			} else {
				tuners = new DtvTunerInfo[1];
				tuners[0] = tunerInfo;
			}
			dtvInterface.scanStart(SCAN_MODE_NIT, tuners);
			break;
		}

		case DTV_ScanList: {
			Log.i(TAG, "LL enter DTV_ScanList *** ");
			// DtvChannelManager.getInstance().getChannelList().clear();
			DtvChannelManager.getInstance().reset();
			DtvScheduleManager.getInstance().delAllScheduleEvents();
			dtvScanFreTable = getFreTable();
			DtvTunerInfo[] tunerList = new DtvTunerInfo[dtvScanFreTable.length];
			for (int index = 0; index < dtvScanFreTable.length; index++) {
				tunerList[index] = new DtvTunerInfo(dtvScanFreTable[index], symbolRate, modulMode + 1);
			}
			dtvInterface.scanStart(SCAN_MODE_LIST, tunerList);
			break;
		}

		case DTV_ScanMaunal: {
			Log.i(TAG, "LL enter DTV_ScanMaunal *** " + SCAN_MODE_MANUAL);
			DtvTunerInfo tuner = new DtvTunerInfo(frequency, symbolRate, modulMode + 1);
			DtvTunerInfo[] tunerList = null;
			if (listFreMax <= frequency) {
				tunerList = new DtvTunerInfo[] { tuner, tuner };
			} else {
				tunerList = new DtvTunerInfo[] { tuner, new DtvTunerInfo(listFreMax, symbolRate, modulMode + 1) };
			}
			dtvInterface.scanStart(SCAN_MODE_MANUAL, tunerList);
			break;
		}

		default:
			break;
		}
	}

	/**
	 * getDMBTParams()
	 * 锟斤拷实锟斤拷要锟斤拷锟斤拷锟斤拷频锟绞ｏ拷锟斤拷锟斤拷锟斤拷锟斤拷璨伙拷瓒硷拷锟接帮拷锟
	 * @return
	 */
	public boolean getDMBTParams() {
		DtvTunerInfo tunerInfo = dtvInterface.getDBMTTunerInfo();
		if (null == tunerInfo) {
			Log.d(TAG, "EL->getDMBTParams>>> can't get DMBT tunner info");
			return false;
		}

		String tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_INTER_LEAVE);
		miInterleaverMode = (tmp == null) ? tunerInfo.getMi_InterleaverMod() : Integer.valueOf(tmp);

		tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_CARRIER_MODE);
		miCarrierMode = (tmp == null) ? tunerInfo.getMi_CarrierMode() : Integer.valueOf(tmp);

		tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_LDPC_RATE);
		miLDPCRate = (tmp == null) ? tunerInfo.getMi_LDPCRate() : Integer.valueOf(tmp);

		tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_FRAM_HEADER);
		miFrameHeader = (tmp == null) ? tunerInfo.getMi_FrameHeader() : Integer.valueOf(tmp);

		tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_LOW_FREQ);
		miFreq = (tmp == null) ? tunerInfo.getMi_FreqKHz() : Integer.valueOf(tmp);

		//		tmp = DtvConfigManager.getInstance().getValue(
		//				ConstStringKey.USER_SET_SCAN_QAM);
		//		modulMode = (tmp == null) ?  tunerInfo.getQamMode() : Integer.valueOf(tmp);

		tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_SCAN_NCO_FREQ);
		miNCOFrequency = (tmp == null) ? tunerInfo.getMi_NCOFreqKHz() : Integer.valueOf(tmp);

		return true;
	}

	public void scanStartDMBT(final Handler handler, int frequency, int miNCOFrequency, int miDTMBTHQamMode) {
		Log.v("tv", "dtvScanStart frequency =" + frequency);
		this.handler = handler;

		resultOfDTV = 0;
		resultOfRadio = 0;
		resultOfData = 0;
		isSearching = true;

		if (!getDMBTParams()) {
			isSearching = false;
			Log.d(TAG, "EL-->scanStartDMBT>> can't get DMBT scanParams");
			return;
		}

		switch (scanType) {
		case DTV_ScanAuto:{
			Log.i(TAG, "LL enter DTV_ScanAuto *** ");
			// DtvChannelManager.getInstance().getChannelList().clear();
			DtvChannelManager.getInstance().reset();
			DtvScheduleManager.getInstance().delAllScheduleEvents();

			DtvTunerInfo info = new DtvTunerInfo(miCarrierMode, frequency, miNCOFrequency, miDTMBTHQamMode, miLDPCRate, miFrameHeader, miInterleaverMode);
			List<DtvTunerInfo> opMianTunerList = mOperatorManager.getOPMainTunerList();
			List<DtvTunerInfo> list = this.getFreqParams(opMianTunerList, info);
			DtvTunerInfo[] tuners = null;
			if (null != list && list.size() > 0) {
				tuners = new DtvTunerInfo[list.size()];

				for (int index = 0; index < list.size(); index++) {
					Log.i(TAG, "Frequancy is + " + list.get(index).getFrequency());
					tuners[index] = new DtvTunerInfo(miCarrierMode, list.get(index).getFrequency(), miNCOFrequency, miDTMBTHQamMode, miLDPCRate, miFrameHeader, miInterleaverMode);
				}
			} else {
				tuners = new DtvTunerInfo[1];
				tuners[0] = info;
			}
			dtvInterface.scanStart(SCAN_MODE_NIT, tuners);
			break;
		}

		case DTV_ScanList: {
			Log.i(TAG, "LL enter DTV_ScanList *** ");
			// DtvChannelManager.getInstance().getChannelList().clear();
			DtvChannelManager.getInstance().reset();
			DtvScheduleManager.getInstance().delAllScheduleEvents();
			dtvScanFreTable = getFreTable();
			DtvTunerInfo[] tunerList = new DtvTunerInfo[dtvScanFreTable.length];
			for (int index = 0; index < dtvScanFreTable.length; index++) {
				tunerList[index] = new DtvTunerInfo(miCarrierMode, dtvScanFreTable[index], miNCOFrequency, miDTMBTHQamMode, miLDPCRate, miFrameHeader, miInterleaverMode);
			}
			dtvInterface.scanStart(SCAN_MODE_LIST, tunerList);
			break;
		}

		case DTV_ScanMaunal:{
			Log.i(TAG, "LL enter DTV_ScanMaunal *** " + SCAN_MODE_MANUAL);
			DtvTunerInfo tuner = new DtvTunerInfo(miCarrierMode, frequency, miNCOFrequency, miDTMBTHQamMode, miLDPCRate, miFrameHeader, miInterleaverMode);
			DtvTunerInfo[] tunerList = { tuner };
			dtvInterface.scanStart(SCAN_MODE_MANUAL, tunerList);
			break;
		}

		default:
			break;
		}
	}

	public void scanStop() {
		dtvInterface.scanStop();
	}

	private List<DtvTunerInfo> getFreqParams(List<DtvTunerInfo> list, DtvTunerInfo tunerInfo) {
		List<DtvTunerInfo> tmpList = new ArrayList<DtvTunerInfo>();
		if (null == tunerInfo) {
			tmpList = list;
		} else {
			tmpList.add(tunerInfo);
			if (list != null) {
				for (DtvTunerInfo dtvTunerInfo : list) {
					if (dtvTunerInfo.getFrequency() != tunerInfo.getFrequency()) {
						tmpList.add(dtvTunerInfo);
					}
				}
			}
		}
		return tmpList;
	}

	public int getQamModeIndex(String strMode) {
		int index = 0;
		for (index = 0; index < dtvScanModulModeTable.length; index++) {
			if (strMode.equals(dtvScanModulModeTable[index])) {
				break;
			}
		}
		return index;
	}

	private int[] convertString2Integer(String[] str) {
		int[] intArray = null;
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);

		if (str != null) {
			intArray = new int[str.length];
			for (int i = 0; i < str.length; i++) {
				intArray[i] = Integer.parseInt(p.matcher(str[i]).replaceAll("").trim());
			}
		}
		return intArray;
	}

	public void setListFreMax(int listFreMax) {
		this.listFreMax = listFreMax;
	}

	public int getListFreMax() {
		return listFreMax;
	}

	public void setListFreMin(int listFreMin) {
		this.listFreMin = listFreMin;
	}

	public int getListFreMin() {
		return listFreMin;
	}

	private int[] getListScanFreTable() {
		int[] fre = null;
		if (listFreMin <= listFreMax - 8000) {
			int length = 0;
			int table[] = getFreTable();
			for (int i = 0; i < table.length; i++) {
				if (table[i] >= listFreMin && table[i] <= listFreMax) {
					length++;
				}
			}
			if (length > 0) {
				fre = new int[length];
				for (int i = 0; i < length; i++) {
					fre[i] = table[i];
				}
				return fre;
			}
		}
		return null;
	}

	private class ScanReceiver extends BroadcastReceiver {

		private static final String TAG = "ScanReceiver";

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bd_Get = null;
			ScanStatusInfo statusInfo = null;
			bd_Get = intent.getExtras(); // get extras info
			statusInfo = (ScanStatusInfo) bd_Get.getParcelable(BroadcastConst.MSG_INFO_NAME); // Get
			if (statusInfo == null) {
				Log.e(TAG, "LL statusInfo = null");
				return;
			}
                        scanProgress = statusInfo.miProgress;
			Log.i(TAG, "LL statusInfo.miProgress = " + statusInfo.miProgress);
			/*if(handler == null){
				Log.e(TAG, "LL handler = null");
				return;
			}*/

			if (handler != null) {//2015-4-14 YangLiu
				handler.sendEmptyMessage(MenuScan.SCAN_UPDATE_MESSAGE);
			} else {
				Log.e(TAG, "LL handler = null");
			}

			//	Log.i(TAG, "sendEmptyMessage fy0000");
			//	handler.sendEmptyMessage(MainMenuRootData.Auto_SCAN_UPDATE_MESSAGE);

			Log.i(TAG, "LL statusInfo.miStatus= " + statusInfo.miStatus);
			switch (statusInfo.miStatus) {
			case ScanEvent.STATUS_INIT:
				Log.i(TAG, "Reveiced STATUS_INIT");
				break;

			case ScanEvent.STATUS_INIT_END:
				Log.i(TAG, "Reveiced STATUS_INIT_END");
				break;

			case ScanEvent.STATUS_NIT_BEGIN:
				Log.i(TAG, "STATUS_NIT_BEGIN");
				break;

			case ScanEvent.STATUS_NIT_DONE:
				Log.i(TAG, "STATUS_NIT_DONE");
				break;

			case ScanEvent.STATUS_NIT_NEXT:
				Log.i(TAG, "STATUS_NIT_NEXT");
				break;

			case ScanEvent.STATUS_TUNING_BEGIN:
				Log.i(TAG, "STATUS_TUNING_BEGIN");
				CarrierInfo o_Car = null;
				o_Car = (CarrierInfo) bd_Get.getParcelable(BroadcastConst.MSG_INFO_NAME_1);
				Log.i(TAG, "LL o_Car.miFrequencyK = " + o_Car.miFrequencyK);
				currentFrequency = o_Car.miFrequencyK;
				try {
					if (demodType == ConstDemodType.DMB_TH) {
						miNCOFrequency = ((DMBTHCarrier) o_Car).miNCOFrequencyKhz;
					}
				} catch (Exception e) {
				}
				Log.i(TAG, "sendEmptyMessage fy011111");
				if (handler != null) {//2015-4-14 YangLiu
					handler.sendEmptyMessage(MenuScan.SCAN_UPDATE_MESSAGE);
				} else {
					Log.i(TAG, "handler=" + handler);
				}
				//handler.sendEmptyMessage(MainMenuRootData.Auto_SCAN_UPDATE_MESSAGE);
				break;

			case ScanEvent.STATUS_TUNING_STATUS_FLUSH:
				Log.i(TAG, "STATUS_TUNING_STATUS_FLUSH");
				break;

			case ScanEvent.STATUS_SERVICE_DONE:
				Log.i(TAG, "STATUS_SERVICE_DONE");
				Parcelable[] tmpParcelable = bd_Get.getParcelableArray(BroadcastConst.MSG_INFO_NAME_1);
				DTVChannelBaseInfo o_Ch = null;
				Log.i(TAG, "LL tmpParcelable.length = " + tmpParcelable.length);
				for (int i = 0; i < tmpParcelable.length; i++) {
					Log.i(TAG, "LL tmpParcelable[" + i + "] = " + tmpParcelable[i]);
					o_Ch = (DTVChannelBaseInfo) tmpParcelable[i];

					switch (o_Ch.miServiceType) {
					case DTVConstant.ConstServiceType.SERVICE_TYPE_RADIO:
						resultOfRadio++;
						break;
					case DTVConstant.ConstServiceType.SERVICE_TYPE_TV:
					case 101:
					case 102:
					case 103:
						resultOfDTV++;
						break;

					default:
						break;
					}
				}
				if (handler != null) {//2015-4-14 YangLiu
					handler.sendEmptyMessage(MenuScan.SCAN_UPDATE_MESSAGE);
				} else {
					Log.i(TAG, "handler=" + handler);
				}
				break;

			case ScanEvent.STATUS_SORTING:
				Log.i(TAG, "STATUS_SORTING");
				break;

			case ScanEvent.STATUS_SORTED:
				Log.i(TAG, "STATUS_SORTED");
				break;

			case ScanEvent.STATUS_SAVING:
				Log.i(TAG, "STATUS_SAVING");
				scanProgress = 100;
				Log.i(TAG, "LL saving data and set scanProgress to full ***");
				if (handler != null) {//2015-4-14 YangLiu
					handler.sendEmptyMessage(MenuScan.SAVE_DATA_MESSAGE);
				} else {
					Log.i(TAG, "handler=" + handler);
				}
				break;

			case ScanEvent.STATUS_SAVED:
				Log.i(TAG, "STATUS_SAVED");
				break;

			case ScanEvent.STATUS_FAIL:
				Log.i(TAG, "STATUS_FAIL");
				isSearching = false;
				if (handler != null) {//2015-4-14 YangLiu
					handler.sendEmptyMessage(MenuScan.DIALOG_EXIT_MESSAGE);
				} else {
					Log.i(TAG, "handler=" + handler);
				}
				break;

			case ScanEvent.STATUS_END:
				Log.i(TAG, "STATUS_END");

				isSearching = false;
				if (handler != null) {//2015-4-14 YangLiu
					handler.sendEmptyMessage(MenuScan.DIALOG_EXIT_MESSAGE);
				} else {
					Log.i(TAG, "handler=" + handler);
				}
				break;

			default:
				break;
			}
		}
	}
}