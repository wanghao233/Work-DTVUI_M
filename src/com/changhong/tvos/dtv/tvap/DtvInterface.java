package com.changhong.tvos.dtv.tvap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;

import com.changhong.tvos.dtv.ChannelManager;
import com.changhong.tvos.dtv.DTVPlayer;
import com.changhong.tvos.dtv.DTVSettings;
import com.changhong.tvos.dtv.TimerManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvAudioTrack;
import com.changhong.tvos.dtv.tvap.baseType.DtvCardStatus;
import com.changhong.tvos.dtv.tvap.baseType.DtvEvent;
import com.changhong.tvos.dtv.tvap.baseType.DtvOpFeature;
import com.changhong.tvos.dtv.tvap.baseType.DtvOperator;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.DtvTunerInfo;
import com.changhong.tvos.dtv.tvap.baseType.DtvTunerStatus;
import com.changhong.tvos.dtv.tvap.baseType.DtvVersion;
import com.changhong.tvos.dtv.vo.AudioTrack;
import com.changhong.tvos.dtv.vo.CarrierInfo;
import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DTVCardStatus;
import com.changhong.tvos.dtv.vo.DTVChannelBaseInfo;
import com.changhong.tvos.dtv.vo.DTVChannelDetailInfo;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstDemodType;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstServiceType;
import com.changhong.tvos.dtv.vo.DTVDTTime;
import com.changhong.tvos.dtv.vo.DTVSource;
import com.changhong.tvos.dtv.vo.DTVTunerStatus;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.dtv.vo.EPGEvent;
import com.changhong.tvos.dtv.vo.InterTVOSVersion;
import com.changhong.tvos.dtv.vo.OPFeatureInfo;
import com.changhong.tvos.dtv.vo.Operator;
import com.changhong.tvos.dtv.vo.TimerInfo;
import com.changhong.tvos.dtv.vo.TimerInfo.TimerType;
import com.changhong.tvos.dtv.vo.UTCDate;
import com.changhong.tvos.dtv.vo.UTCTime;
import com.changhong.tvos.dtv.vo.VersionInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DtvInterface {
	public static final String TAG = "DtvInterface";
	private static DtvInterface DtvInterface = null;

	private static final int CHANNEL_LIST_TYPE_WATCHED = 0x10000;

	private DTVPlayer mDTVPlayer = null;
	private DTVPlayer.Play mPlay = null;
	private DTVPlayer.Epg mEpg = null;
	private DTVPlayer.Scan mScan = null;
	private DTVPlayer.Cicam mCicam = null;
	private DTVPlayer.StartControl startControl = null;
	public ChannelManager mChannelManager = null;
	private ChannelManager.ChannelInfoManager mChannelInfoMagager = null;
	private ChannelManager.CarrierInfoManager mCarrierInfoManager = null;
	private TimerManager mTimerManager = null;
	private DTVSettings mDTVSetting = null;

	public void init() {
		mDTVPlayer = new DTVPlayer(null);
		Log.i(TAG, "LL DtvInterface>>init()Call Function mDTVPlayer.prepare() begin in Thread" + Thread.currentThread());
		// mDTVPlayer.prepare(); //2015-4-8
		Log.i(TAG, "LL DtvInterface>>init()Call Function mDTVPlayer.prepare() end");
		mPlay = mDTVPlayer.play;
		mEpg = mDTVPlayer.epg;
		mScan = mDTVPlayer.scan;
		mCicam = mDTVPlayer.cicam;
		startControl = mDTVPlayer.startControl;
		mChannelManager = new ChannelManager(null);
		mChannelInfoMagager = mChannelManager.channelMananger;
		mCarrierInfoManager = mChannelManager.carrierMananger;
		mDTVSetting = DTVSettings.getInstance(null);
		mTimerManager = TimerManager.getInstance(null);
		Log.i(TAG, "LL DtvInterface>>init()Call Function end");
	}

	private DtvInterface() {
		this.init();
	}

	public synchronized static DtvInterface getInstance() {
		if (DtvInterface == null) {
			DtvInterface = new DtvInterface();
		}
		return DtvInterface;
	}

	public synchronized int resourcePrepare() {
		Log.i(TAG, "prepare inThread:" + Thread.currentThread());
		return mDTVPlayer.prepare();
	}

	public void resourceRelease() {
		Log.i(TAG, "resourceRelease inThread:" + Thread.currentThread());
		mDTVPlayer.Release();
	}

	// ////////////////////fengy 2014-10-29 /////////
	public void SimulateKey(final int KeyCode) {
		mDTVSetting.SimulateKey(KeyCode);
	}

	public int ConvertKeyBoardCode(int keyCode, KeyEvent event) {
		int curKeyCode = keyCode;
		Log.i(TAG, "revertKeyCode getScanCode = " + event.getScanCode());
		switch (event.getScanCode()) {
			case 231:// keyboard Menu
				curKeyCode = KeyEvent.KEYCODE_MENU;
				break;
			case 232:// keyboard Source
				curKeyCode = KeyEvent.KEYCODE_DPAD_CENTER;
				break;
			case 233:// keyboard Channel Up
				curKeyCode = KeyEvent.KEYCODE_DPAD_UP;
				break;
			case 234:// keyboard Channel Down
				curKeyCode = KeyEvent.KEYCODE_DPAD_DOWN;
				break;
			case 235:// keyboard Volume Down
				curKeyCode = KeyEvent.KEYCODE_DPAD_LEFT;
				break;
			case 236:// keyboard Volume Up
				curKeyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
				break;
			default:
				break;
		}
		return curKeyCode;
	}

	// ///////////////////////////////////////////////////////////////////////

	/**
	 * @param type TV(1) radio(2)
	 * @return
	 */
	public List<DtvProgram> getDtvChannels(int type) {
		ArrayList<DtvProgram> list;
		int channeltype = DTVConstant.ConstServiceType.SERVICE_TYPE_TV;
		if (type == DTVConstant.ConstServiceType.SERVICE_TYPE_RADIO) {

			channeltype = DTVConstant.ConstServiceType.SERVICE_TYPE_RADIO;
		}
		list = new ArrayList<DtvProgram>();
		DTVChannelBaseInfo[] channels = mChannelInfoMagager.getChannelListByTpye(channeltype);
		if (channels != null) {
			Log.e(TAG, "LL length of channelList is " + channels.length);

			for (DTVChannelBaseInfo chChannel : channels) {
				list.add(new DtvProgram(chChannel));
				Log.i(TAG, "LL chChannel.mstrServiceName = " + chChannel.mstrServiceName);
			}
		}
		return list;
	}

	public void deleteChannel(int ri_ChannelIndex) {
		mChannelInfoMagager.delChannel(ri_ChannelIndex);
	}

	public int getBootChannelNum(int channelIndex) {
		int channelNum = 0;
		DTVChannelDetailInfo channelDetailInfo = null;
		channelDetailInfo = mChannelInfoMagager.getChanneDetailInfo(channelIndex);
		if (null != channelDetailInfo) {
			channelNum = channelDetailInfo.miChannelnumber;
		}
		return channelNum;
	}

	/**
	 * @param type TV(1) radio(2)
	 * @return
	 */
	public List<DtvProgram> getWatchedChannelList(int type) {
		ArrayList<DtvProgram> list = new ArrayList<DtvProgram>();
		DTVChannelBaseInfo[] channels = mChannelInfoMagager.getChannelListByTpye(CHANNEL_LIST_TYPE_WATCHED);
		if (channels != null) {
			Log.e(TAG, "LL getWatchedChannelList()>>length of watched channel list is " + channels.length);

			for (DTVChannelBaseInfo chChannel : channels) {
				// Log.i("YangLiu",
				// chChannel.mstrServiceName+"的miServiceType为："+chChannel.miServiceType);
				if (chChannel.miServiceType == type) {
					Log.i(TAG, "LL getWatchedChannelList()>>chChannel.mstrServiceName = " + chChannel.mstrServiceName);
					list.add(new DtvProgram(chChannel));
				}
			}
		}
		return list;
	}

	public DTVChannelDetailInfo getDtvChannelDetailInfo(int channelIndex) {
		DTVChannelDetailInfo channelDetailInfo = null;
		channelDetailInfo = mChannelInfoMagager.getChanneDetailInfo(channelIndex);
		return channelDetailInfo;
	}

	/**
	 * @param channelIndex
	 * @return channeltype TV<1> radio<2>
	 */
	public int getBootChannelType(int channelIndex) {
		int channelType = ConstServiceType.SERVICE_TYPE_TV;
		DTVChannelDetailInfo channelDetailInfo = null;
		channelDetailInfo = mChannelInfoMagager.getChanneDetailInfo(channelIndex);
		if (null != channelDetailInfo) {
			channelType = (channelDetailInfo.miServiceType == ConstServiceType.SERVICE_TYPE_RADIO) ? ConstServiceType.SERVICE_TYPE_RADIO : ConstServiceType.SERVICE_TYPE_TV;
		}
		Log.i(TAG, "LL getBootChannelType()>>channelType = " + channelType);
		return channelType;
	}

	public boolean checkVersion(int curVersion) {
		Log.i(TAG, "LL checkVersion()>>curVersion = " + curVersion);
		return mDTVPlayer.checkVersion(curVersion) == 1 ? true : false;
	}

	/**
	 * @return
	 */
	public boolean startBootService() {
		boolean success = false;
		// if(null != mDTVPlayer){
		// int result = mDTVPlayer.prepare();
		// if(0 != result){
		// Log.e(TAG, "LL prepare before startControl failed***");
		// }
		// }else{
		// Log.e(TAG, "LL mDTVPlayer == null***");
		// }

		if (null != startControl) {
			int result = startControl.start();
			if (0 != result) {
				Log.e(TAG, "LL startControl failed and try again***");
				if (null != mDTVPlayer) {

					result = resourcePrepare();
					if (0 != result) {
						Log.e(TAG, "LL prepare before startControl failed***");
					} else {
						result = startControl.start();
						if (0 != result) {
							Log.e(TAG, "LL startControl failed again***");
						}
						success = true;
					}
				} else {
					Log.e(TAG, "LL mDTVPlayer == null***");
				}
			} else {
				success = true;
			}
		} else {
			Log.e(TAG, "LL startControl == null***");
		}
		Log.i(TAG, "LL startBootService()>>success = " + success);
		return success;
	}

	/**
	 * @param channelIndex
	 * @return 0
	 */
	public int channelSelect(int channelIndex) {
		Log.i(TAG, "LL DtvInterface>>channelSelect>>channelIndex = " + channelIndex);
		return mPlay.play(channelIndex);
	}

	public void channelStop() {
		mPlay.stop();
	}

	public void channelSwap(int iSrcIndex, int iDestIndex) {

		mChannelInfoMagager.swapChannel(iSrcIndex, iDestIndex);
	}

	public synchronized void channelSkip(int Index, Boolean isSkip) {
		mChannelInfoMagager.skipChannel(Index, isSkip);
	}

	/* added by zhangWei set favorite */
	public void channelFav(int Index, Boolean isFav) {
		mChannelInfoMagager.setFav(Index, isFav);
	}

	private boolean isNeedFilterEvent(List<DtvEvent> list, DtvEvent item) {
		if (list == null || item == null) {
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(item)) {
				list.set(i, item);
				return true;
			}
		}
		return false;
	}

	public List<DtvEvent> getEventsBytime(int proramIndex, DTVDTTime startTime, DTVDTTime endTime) {
		List<DtvEvent> list = new ArrayList<DtvEvent>();
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		EPGEvent[] events = mEpg.getSchelueEventByTime(proramIndex, startTime, endTime);
		if (events != null) {
			for (EPGEvent event : events) {
				// Log.i(TAG,"LL event:" + event.eventName + " ," +
				// event.startDate.miYear + " ," + event.startDate.miMonth +
				// " ," + event.startDate.miDay +" " +event.startTime.miHour
				// +":"+ event.startTime.miMinute);
				long meventStartTime = chTimeToLong(event.startDate, event.startTime);
				long meventEndTime = meventStartTime + event.duringTime.miHour * 60 * 60 * 1000 + event.duringTime.miMinute * 60 * 1000 + event.duringTime.miSecond * 1000;
				long mstarTime = chTimeToLong(startTime.mstruDate, startTime.mstruTime);
				long mendTime = chTimeToLong(endTime.mstruDate, endTime.mstruTime);
				Log.i(TAG,
						"LL getEventsBytime()>>event:" + event.eventName + "mstarTime = " + dtFormat.format(new Date(mstarTime)) + " ,meventStartTime = " + dtFormat.format(new Date(meventStartTime))
								+ " ,meventEndTime = " + dtFormat.format(new Date(meventEndTime)) + ", mendTime = " + dtFormat.format(new Date(mendTime)));
				if (meventStartTime / 1000L > mstarTime / 1000L && meventStartTime / 1000L < mendTime / 1000L) {
					DtvEvent dtvEvent = new DtvEvent(event);
					if (!this.isNeedFilterEvent(list, dtvEvent)) {
						list.add(new DtvEvent(event));
					} else {
						Log.e(TAG, "LL filter event that has the same start time and service id,eventName = " + dtvEvent.getTitle());
					}
				}
			}
		}
		return list;
	}

	public DtvEvent[] getPFInfo(int proramIndex) {
		DtvEvent[] pfEvent = new DtvEvent[]{null, null};
		EPGEvent[] events = mEpg.getPFEvent(proramIndex);
		if (events != null && events[0] != null && chTimeToLong(events[0].duringTime) > 0) {
			pfEvent[0] = new DtvEvent(events[0]);
			Log.i(TAG, "LL pfEvent[0] = " + pfEvent[0].getTitle());
		}
		if (events != null && events[1] != null && chTimeToLong(events[1].duringTime) > 0) {
			pfEvent[1] = new DtvEvent(events[1]);
			Log.i(TAG, "LL pfEvent[1] = " + pfEvent[1].getTitle());
		}
		return pfEvent;
	}

	public Date getCurrentDate() {
		DTVDTTime dt = null;
		long systemTime = System.currentTimeMillis();
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Log.i(TAG, "LL getSystemTime()>>" + dtFormat.format(new Date(systemTime)));
		long dtvTime = 0;
		dt = mDTVPlayer.getTDTTime();
		if (dt != null) {
			String str = String.format("LL getCurrentDate() TDTTIME>>year = %d,month = %d,day = %d,hour = %d,minute = %d,second = %d", dt.mstruDate.miYear, dt.mstruDate.miMonth, dt.mstruDate.miDay,
					dt.mstruTime.miHour, dt.mstruTime.miMinute, dt.mstruTime.miSecond);
			Log.i(TAG, str);
		}
		dtvTime = chTimeToLong(dt);
		return new Date(dtvTime > systemTime ? dtvTime : systemTime);
	}

	public long chTimeToLong(DTVDTTime dt) {
		long chTime = 0;
		if (dt != null) {
			UTCDate date = dt.mstruDate;
			UTCTime time = dt.mstruTime;

			Calendar cal = Calendar.getInstance();
			cal.set(date.miYear, date.miMonth - 1, date.miDay, time.miHour, time.miMinute, time.miSecond);
			chTime = cal.getTimeInMillis();
		}
		return chTime;
	}

	public long chTimeToLong(UTCTime time) {
		long chTime = 0;
		if (time != null) {
			chTime = time.miHour * 60 * 60 + time.miMinute * 60 + time.miSecond;
			String str = String.format("LL chTimeToLong()>>hour = %d,minute = %d,second = %d,chTime = %d", time.miHour, time.miMinute, time.miSecond, chTime);
			Log.i(TAG, str);
		}
		return chTime;

	}

	public long chTimeToLong(UTCDate date, UTCTime time) {
		// String str =
		// String.format("LL chTimeToLong()>>year = %d,month = %d,day = %d,hour = %d,minute = %d,second = %d",date.miYear,date.miMonth,date.miDay,time.miHour,time.miMinute,time.miSecond);
		// Log.i(TAG,str);
		long chTime = 0;
		Calendar cal = Calendar.getInstance();
		cal.set(date.miYear, date.miMonth - 1, date.miDay, time.miHour, time.miMinute, time.miSecond);
		chTime = cal.getTimeInMillis();
		// Log.i(TAG,"LL chTimeToLong()>>chTime = " + chTime);
		return chTime;
	}

	public void setAudioMode(int index) {
		mDTVSetting.setSoundMode(index);
	}

	public void setAudioTrack(int index) {
		mPlay.setChannelAudioTrack(index);
	}

	public String[] getAudioMode(int programIndex) {
		return null;
	}

	public DtvAudioTrack getAudioTrack(int programIndex) {
		DtvAudioTrack audioTrack = null;
		AudioTrack audio = null;
		DTVChannelDetailInfo channelDetailInfo = null;
		channelDetailInfo = mChannelInfoMagager.getChanneDetailInfo(programIndex);
		if (null != channelDetailInfo) {
			audio = channelDetailInfo.mAudioTrackInfo;
			if (audio != null) {
				audioTrack = new DtvAudioTrack(audio);
			}
		}
		return audioTrack;
	}

	public DVBCCarrier getDVBCCurTunerInfo() {
		DVBCCarrier carrier = null;
		carrier = mPlay.getDVBCCurTunerInfo();
		return carrier;
	}

	public DMBTHCarrier getDMBTHCarrier() {
		DMBTHCarrier carrier = null;
		try {
			carrier = (DMBTHCarrier) mDTVPlayer.getCurTunerInfo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(TAG, "LL carrire is not DBMT");
		}
		return carrier;
	}

	public DtvTunerStatus getTunerStatus() {
		DTVTunerStatus mTunerStatus = null;
		mTunerStatus = mDTVPlayer.getTunerStatus();
		return new DtvTunerStatus(mTunerStatus);
	}

	public boolean isLock() {
		boolean isLock = true;
		DTVTunerStatus tunerStatus = null;
		tunerStatus = mDTVPlayer.getTunerStatus();
		if (tunerStatus != null) {
			isLock = tunerStatus.mbLock;
		}
		return isLock;
	}

	public DtvVersion getDTVVersionInfo() {
		VersionInfo dtvVsersionl = null;
		dtvVsersionl = mDTVSetting.getVersion();
		return new DtvVersion(dtvVsersionl);
	}

	public void setDtvLanguage(String uiLanguage) {
		mDTVSetting.setAudioLanguage(uiLanguage);
	}

	public String getCicaSmartNumber() {
		String smartNumber = null;

		smartNumber = mCicam.getSmartCardID();
		return smartNumber;
	}

	public DtvCardStatus getCicaCardStatus() {
		DTVCardStatus dtvCardStatus = mCicam.getSmartCardStatus();
		DtvCardStatus status = null;
		if (dtvCardStatus != null) {
			status = new DtvCardStatus(dtvCardStatus.miCardType, dtvCardStatus.miCardStatus);
		}
		return status;
	}

	public void cicaFlushMemory() {
		mCicam.clearUserData();
	}

	public void cicaQueryControl(int type, int msgID, int menuID, int operand, int opcode, int defOpcode, int inputItems, String[] inputList) {
		Log.i(TAG, "LL enter cicaQueryControl()***");
		try {
			mCicam.queryControl(type, msgID, menuID, operand, opcode, defOpcode, inputItems, inputList);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void scanStart(int ri_ScanMode, DtvTunerInfo fre[]) {
		int m_FreqCnt = 0;

		Log.i(TAG, "scanStart(" + ri_ScanMode + ")");

		if (fre != null) {
			m_FreqCnt = fre.length;
		}

		if (m_FreqCnt != 0) {
			if (this.getDemodType() == ConstDemodType.DVB_C) {

				DVBCCarrier[] FreqArr = null;
				FreqArr = new DVBCCarrier[m_FreqCnt];
				int iLoop = 0;
				for (iLoop = 0; iLoop < m_FreqCnt; iLoop++) {
					FreqArr[iLoop] = new DVBCCarrier(5, 6875);
					FreqArr[iLoop].miDemodType = this.getDemodType();
					FreqArr[iLoop].miFrequencyK = fre[iLoop].getFrequency();
					FreqArr[iLoop].miQamMode = fre[iLoop].getQamMode();
					FreqArr[iLoop].miSymbolRateK = fre[iLoop].getSymbolRate();
					String str = String.format("LL FreqArr[%d].miFrequencyK = %d,FreqArr[%d].miSymbolRateK = %d,FreqArr[%d].miQamMode = %d", iLoop, FreqArr[iLoop].miFrequencyK, iLoop,
							FreqArr[iLoop].miSymbolRateK, iLoop, FreqArr[iLoop].miQamMode);
				}

				mScan.setParam(ri_ScanMode, FreqArr);
				mScan.start();

			} else if (this.getDemodType() == ConstDemodType.DMB_TH) {
				DMBTHCarrier[] FreqArr = null;
				FreqArr = new DMBTHCarrier[m_FreqCnt];
				for (int iLoop = 0; iLoop < m_FreqCnt; iLoop++) {
					FreqArr[iLoop] = new DMBTHCarrier(0, 0, 0, 0, 0, 0);
					FreqArr[iLoop].miDemodType = this.getDemodType();
					FreqArr[iLoop].miCarrierMode = fre[iLoop].getMi_CarrierMode();
					FreqArr[iLoop].miDTMBTHQamMode = fre[iLoop].getQamMode();
					FreqArr[iLoop].miFrameHeader = fre[iLoop].getMi_FrameHeader();
					FreqArr[iLoop].miFrequencyK = fre[iLoop].getMi_FreqKHz();
					FreqArr[iLoop].miInterleaverMode = fre[iLoop].getMi_InterleaverMod();
					FreqArr[iLoop].miNCOFrequencyKhz = fre[iLoop].getMi_NCOFreqKHz();
					FreqArr[iLoop].miLDPCRate = fre[iLoop].getMi_LDPCRate();
					String str = String.format("LL FreqArr[%d].miCarrierMode = %d, " + "FreqArr[%d].miDTMBTHQamMode = %d, " + "FreqArr[%d].miFrameHeader = %d " + "FreqArr[%d].miFrequencyK = %d,"
									+ "FreqArr[%d].miInterleaverMode = %d, " + "FreqArr[%d].miNCOFrequencyKhz = %d " + "FreqArr[iLoop].miLDPCRate = %d", iLoop, FreqArr[iLoop].miCarrierMode, iLoop,
							FreqArr[iLoop].miDTMBTHQamMode, iLoop, FreqArr[iLoop].miFrameHeader, iLoop, FreqArr[iLoop].miFrequencyK, iLoop, FreqArr[iLoop].miInterleaverMode, iLoop,
							FreqArr[iLoop].miNCOFrequencyKhz, iLoop, FreqArr[iLoop].miLDPCRate);
					Log.i(TAG, str);
				}

				mScan.setParam(ri_ScanMode, FreqArr);
				mScan.start();
			}
		}
	}

	// public void scanStart(int ri_ScanMode, DtvTunerInfo fre[]) {

	// DVBCCarrier[] FreqArr = null;
	// int m_FreqCnt = 0;
	// if (fre != null) {
	// m_FreqCnt = fre.length;
	// }
	// if (m_FreqCnt != 0) {
	//
	// FreqArr = new DVBCCarrier[m_FreqCnt];
	// int iLoop = 0;
	// for (iLoop = 0; iLoop < m_FreqCnt; iLoop++) {
	// FreqArr[iLoop] = new DVBCCarrier(5, 6875);
	// FreqArr[iLoop].miFrequencyK = fre[iLoop].getFrequency();
	// FreqArr[iLoop].miQamMode = fre[iLoop].getQamMode();
	// FreqArr[iLoop].miSymbolRateK = fre[iLoop].getSymbolRate();
	// String str = String
	// .format("LL FreqArr[%d].miFrequencyK = %d,FreqArr[%d].miSymbolRateK = %d,FreqArr[%d].miQamMode = %d",
	// iLoop, FreqArr[iLoop].miFrequencyK, iLoop,
	// FreqArr[iLoop].miSymbolRateK, iLoop,
	// FreqArr[iLoop].miQamMode);
	// Log.i(TAG, str);
	// }
	// }
	// mScan.setParam(ri_ScanMode, FreqArr);
	// mScan.start();
	// Log.i(TAG, "scanStart>>1");
	// scanStart(getDemodType(), ri_ScanMode, fre);
	// return;
	// }

	public void scanStop() {
		mScan.stop();
	}

	public List<DtvOperator> getOperatorList() {
		// TODO Auto-generated method stub
		List<DtvOperator> listDtvOperator = new ArrayList<DtvOperator>();
		Operator[] operator = mDTVSetting.getOPList();
		Log.i(TAG, "LL getOperatorList()>> operator = " + operator);
		if (operator != null) {
			for (int i = 0; i < operator.length; i++) {
				Log.i(TAG, "LL getOperatorListBySourceID()>>operatorName = " + operator[i].mstrOperatorName + operator[i].province + operator[i].city);
				DtvOperator dtvOperator = new DtvOperator(operator[i]);
				listDtvOperator.add(dtvOperator);
			}
		}
		return listDtvOperator;
	}

	public List<DtvOperator> getOperatorListBySourceID(int sourceID) {
		// TODO Auto-generated method stub
		List<DtvOperator> listDtvOperator = new ArrayList<DtvOperator>();
		Operator[] operator = mDTVSetting.getOPListBySourceID(sourceID);
		Log.i(TAG, "LL getOperatorListBySourceID()>> operator = " + operator);
		if (operator != null) {
			for (int i = 0; i < operator.length; i++) {
				Log.i(TAG, "LL getOperatorListBySourceID()>>operatorName = " + operator[i].mstrOperatorName + operator[i].province + operator[i].city);
				DtvOperator dtvOperator = new DtvOperator(operator[i]);
				listDtvOperator.add(dtvOperator);
			}
		}
		return listDtvOperator;
	}

	public DtvOpFeature getOperatorFeature() {
		DtvOpFeature mDtvOpFeature = null;
		OPFeatureInfo opFeature = mDTVSetting.getOPFeature();
		if (opFeature != null) {
			mDtvOpFeature = new DtvOpFeature(opFeature);
			Log.i(TAG, "LL opFeature.miStartProgramIndex = " + opFeature.miStartProgramIndex + ",mDtvOpFeature.mStartChannelIndex = " + mDtvOpFeature.getStartChannelIndex()
					+ ", opFeature.mMainFreqList.length = " + opFeature.mMainFreqList.length + ", opFeature.mbEpgOnlyOneFreq = " + opFeature.mbEpgOnlyOneFreq + ",opFeature.mEpgFreq = "
					+ opFeature.mbEpgOnlyOneFreq + opFeature.mEpgFreq + ",opFeature.hidden = " + opFeature.mbHideListScan);
			int i = 0;
			for (i = 0; i < opFeature.mMainFreqList.length; i++) {
				Log.i(TAG, "LL mMainFreqList[" + i + "]=" + opFeature.mMainFreqList[i]);
			}
		}
		return mDtvOpFeature;
	}

	public DtvTunerInfo getOPMainTunerInfo(int iOperatorCode) {
		DtvTunerInfo chOpTunerInfo = null;

		DVBCCarrier[] opMainCarrierInfo = null;
		opMainCarrierInfo = mDTVSetting.getDVBCOPMainFreqList();
		if (null != opMainCarrierInfo && opMainCarrierInfo.length > 0) {
			Log.i(TAG, "LL getOPMainTunerInfo>>" + ", miFrequencyK:" + opMainCarrierInfo[0].miFrequencyK + ", miSymbolRateK:" + opMainCarrierInfo[0].miSymbolRateK + ", miQamMode:"
					+ opMainCarrierInfo[0].miQamMode);

			chOpTunerInfo = new DtvTunerInfo(opMainCarrierInfo[0].miFrequencyK, opMainCarrierInfo[0].miSymbolRateK, opMainCarrierInfo[0].miQamMode);
		} else {
			Log.e(TAG, "LL getOPMainTunerInfo>>( null == opMainCarrierInfo||opMainCarrierInfo.length<=0)");
			chOpTunerInfo = new DtvTunerInfo(235000, 6875, 5);
		}

		return chOpTunerInfo;
	}

	public DtvTunerInfo getDBMTTunerInfo() {
		DtvTunerInfo chOpTunerInfo = null;
		//
		// DMBTHCarrier dmbt_carrier = null;
		// try {
		// dmbt_carrier = (DMBTHCarrier)mDTVPlayer.getCurTunerInfo();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// if(null != dmbt_carrier){
		//
		// chOpTunerInfo = new DtvTunerInfo(dmbt_carrier.miCarrierMode,
		// dmbt_carrier.miFrequencyK,
		// dmbt_carrier.miNCOFrequencyKhz,
		// dmbt_carrier.miDTMBTHQamMode,
		// dmbt_carrier.miLDPCRate,
		// dmbt_carrier.miFrameHeader,
		// dmbt_carrier.miInterleaverMode
		// );
		//
		// }else {
		chOpTunerInfo = new DtvTunerInfo(0, 52500, 52500, 0, 0, 0, 0);
		// }
		Log.i(TAG, "get the tuner info");
		return chOpTunerInfo;
	}

	private String TmpSourceName = "无线";

	public List<DTVSource> getSourceList() {
		List<DTVSource> dtvSourceList = new ArrayList<DTVSource>();
		DTVSource[] source = mChannelManager.getSourceList();
		if (null != source) {
			for (int i = 0; i < source.length; i++) {
				// ///////////////Fengy 2014 - 3- 26
				// ///////////////////////////////////
				if (source[i].miSourceName.equals(TmpSourceName))
				// if(source[i].miSourceName.equals(DtvRoot.mContext.getResources()
				// .getString(R.string.dtmb_source)))
				{
					source[i].miSourceName = "DTMB";
				} else {
					source[i].miSourceName = "DVBC";
				}
				dtvSourceList.add(source[i]);
			}
		} else {
			Log.e(TAG, "LL mChannelManager.getSourceList()==null");
		}

		return dtvSourceList;
	}

	public DTVSource getSource() {
		DTVSource dtvSource = mDTVPlayer.getSource();
		if (null == dtvSource) {
			Log.e(TAG, "LL getSource>> null == obj_SRC .");
		} else {
			Log.i(TAG, "LL getSource>> RET =" + dtvSource.miSourceID);
		}

		return dtvSource;

	}

	public int getDemodType() {
		int demodType = ConstDemodType.DVB_C;
		DTVSource dtvSource = mDTVPlayer.getSource();
		if (null != dtvSource) {
			demodType = dtvSource.miSourceType;
			Log.i(TAG, "LL getDemodType>> RET =" + dtvSource.miSourceType);
		} else {
			Log.e(TAG, "LL getDemodType>> null == obj_SRC");
		}

		return demodType;
	}

	public int getSourceID() {
		int source = 1;
		DTVSource dtvSource = mDTVPlayer.getSource();
		if (null != dtvSource) {
			source = dtvSource.miSourceID;
			Log.i(TAG, "LL getSourceID>> RET =" + dtvSource.miSourceID);
		} else {
			Log.e(TAG, "LL getSourceID>> null == obj_SRC");
		}

		return source;
	}

	public void setSource(int sourceID) {
		Log.i(TAG, "LL setSource()>>sourceID = " + sourceID);
		mDTVPlayer.setSource(sourceID);
	}

	public DtvOperator loadCurOperator() {
		// int i;
		DtvOperator dtvOperator = null;
		Operator operator = null;

		// for(i = 0;i < 10000; i++)
		{
			operator = mDTVSetting.getOP();
			// Log.e(TAG,"LL >>loadCurOperator>>(pang == dtvOperator==)" + i);
		}
		if (null != operator) {
			Log.i(TAG, "LL loadCurOperator()>>operator = " + operator.mstrOperatorName);
			dtvOperator = new DtvOperator(operator);
			if (dtvOperator == null) {
				Log.e(TAG, "LL >>loadCurOperator>>(null == dtvOperator)");
			}
			operator = null;
		} else {
			Log.e(TAG, "LL >>loadCurOperator>>(null == operator)");
		}
		return dtvOperator;
	}

	public void saveCurOperator(int opCode) {
		Log.i(TAG, "LL saveCurOperator(int)>>opCode = " + opCode);
		mDTVSetting.setOP(opCode);
	}

	public List<DtvTunerInfo> getOPMainTunerList(int iOperatorCode) {
		List<DtvTunerInfo> chOpTunerList = new ArrayList<DtvTunerInfo>();

		DVBCCarrier[] opMainCarrierInfo = null;
		opMainCarrierInfo = mDTVSetting.getDVBCOPMainFreqList();
		if (opMainCarrierInfo != null) {
			Log.i(TAG, "LL getOPMainTunerList>>opMainCarrierInfo.length: " + opMainCarrierInfo.length);

			for (int i = 0; i < opMainCarrierInfo.length; i++) {
				if (opMainCarrierInfo[i] != null) {

					Log.i(TAG, "LL getOPMainTunerList>>[" + i + "], miFrequencyK:" + opMainCarrierInfo[i].miFrequencyK + ", miSymbolRateK:" + opMainCarrierInfo[i].miSymbolRateK + ", miQamMode:"
							+ opMainCarrierInfo[i].miQamMode);

					chOpTunerList.add(new DtvTunerInfo(opMainCarrierInfo[i].miFrequencyK, opMainCarrierInfo[i].miSymbolRateK, opMainCarrierInfo[i].miQamMode));
				}
			}
		} else {
			Log.e(TAG, "LL getOPMainTunerList>>(opMainCarrierInfo == null) ");
		}
		return chOpTunerList;
	}

	public void dtvReset() {
		// TODO Auto-generated method stub
		mChannelInfoMagager.clearChannelDB();
	}

	// 0:本地EPG预约 1:主场景预约 2:欢网预约
	public List<TimerInfo> getEPGScheduleEventsByOriginal(int original) {// 2015-3-23
		// YangLiu
		List<TimerInfo> timerInfos = new ArrayList<TimerInfo>();

		List<TimerInfo> tempTimerInfos = new ArrayList<TimerInfo>();
		tempTimerInfos = mTimerManager.getTimerList(TimerType.EPG_TIMER);// 取得所有EPG预约

		if (null != tempTimerInfos) {
			Log.i(TAG, "LL_Huan getScheduleEvents>>schNum = " + tempTimerInfos.size());
			for (TimerInfo timerInfo : tempTimerInfos) {
				if (timerInfo.mOriginal == original) {// 取得对应mOriginal的预约列表
					timerInfos.add(timerInfo);
				}
			}
		}
		if (timerInfos != null && timerInfos.size() > 0) {
			Log.i(TAG, "LL getScheduleEvents mOriginal=" + original + ">>schNum = " + tempTimerInfos.size());
		}
		return timerInfos;
	}

	public List<TimerInfo> getScheduleEvents(int timerType) {
		List<TimerInfo> list = new ArrayList<TimerInfo>();
		list = mTimerManager.getTimerList(timerType);

		if (null != list) {
			Log.i(TAG, "LL getScheduleEvents>>schNum = " + list.size());
		}
		return list;
	}

	public void addScheduleEvent(TimerInfo timerInfo) {
		if (null != timerInfo) {
			Log.i(TAG, "LL addScheduleEvent()>>timerInfo = " + timerInfo);
			mTimerManager.addTimer(timerInfo);
		}
	}

	public void delScheduleEvent(TimerInfo timerInfo) {
		if (null != timerInfo) {
			Log.i(TAG, "LL delScheduleEvent()>>timerInfo = " + timerInfo);
			mTimerManager.deleteTimer(timerInfo);
		}
	}

	public void delAllScheduleEvents() {
		mTimerManager.deleteAllTimer();
	}

	public int getFreqByChannelIndex(int channelIndex) {
		int fre = 0;
		CarrierInfo carrierInfo = mCarrierInfoManager.FindCarrierByChannelID(channelIndex);
		if (null != carrierInfo) {
			fre = carrierInfo.miFrequencyK;
		}
		return fre;
	}

	public boolean enterScreenSaver() {
		boolean isEnter = false;
		isEnter = mDTVSetting.enterScreenSaver();
		return isEnter;
	}

	public void enterCommonSetting() {
		mDTVSetting.enterCommonSetting();
	}

	public boolean isKeyboardKey(int keyCode, KeyEvent event) {
		boolean isKeyboardKey = false;
		isKeyboardKey = mDTVSetting.isKeyboardKey(keyCode, event);
		return isKeyboardKey;
	}

	public void updateKeyboardConvertFlag(boolean flag) {
		Log.i(TAG, "LL updateKeyboardConvertFlag()>>flag = " + flag);
		mDTVSetting.updateKeyboardConvertFlag(flag);
	}

	public boolean returnLastInputSource() {
		boolean success = false;
		success = mDTVSetting.returnLastInputSource();
		return success;
	}

	public String getSWVersion() {
		String version = null;
		version = mDTVSetting.getSWVersion();
		return version;
	}

	public boolean getSystem3D() {
		return mDTVSetting.getSystem3D();
	}

	public boolean startInputSource(int channelNum) {
		boolean succes = false;
		succes = mDTVSetting.startInputSource(channelNum);
		return succes;
	}

	public int getPlayerStatus() {
		int status = -1;
		status = mDTVPlayer.getPlayerStatus();
		return status;
	}

	public void setPlayingProgramIndex(int programIndex) {
		try {
			mDTVPlayer.setPlayingProgramID(programIndex);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "LL setPlayingProgramID()>>Throw RemoteException***");
			e.printStackTrace();
		}
	}

	/**
	 * action to start filter unused channels
	 *
	 * @return
	 */
	public int startSmartSkip() {
		// TODO Auto-generated method stub
		return mDTVPlayer.SmartSkip(0);
	}

	public int SetDtvBusyState(int d_state) {
		// TODO Auto-generated method stub
		return mDTVPlayer.SetDtvBusyState(d_state);
	}

	/**
	 * action to cancel filter unused channels
	 *
	 * @return
	 */
	public int cancellSmartSkip() {
		// TODO Auto-generated method stub
		return mDTVPlayer.SmartSkipStop();
	}

	/**
	 * @param myChangeList
	 */
	public void setSortChannelList(List<DtvProgram> myChangeList) {
		// TODO Auto-generated method stub
		if (null == myChangeList || myChangeList.size() == 0) {
			return;
		}
		int demodeType = this.getDemodType();
		int channelType = getBootChannelType(myChangeList.get(0).mProgramNum);
		final DTVChannelBaseInfo[] channelInfos = new DTVChannelBaseInfo[myChangeList.size()];
		for (int i = 0; i < myChangeList.size(); i++) {
			DtvProgram program = myChangeList.get(i);

			DTVChannelBaseInfo baseInfo = new DTVChannelBaseInfo(program.mServiceIndex, program.mProgramNum, program.mProgramName, channelType, program.mServiceID, program.mTsID, program.mOrgNetID,
					program.mIsScrambled, program.mAudioTrackSel, program.mAudioModeSel, 0, null, false, program.mIsSkip, program.mIsFav, demodeType, null);
			channelInfos[i] = baseInfo;
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mChannelInfoMagager.SortChannelList(channelInfos);

			}
		}).start();
	}

	/**
	 * C <1> T<2> T+C<3>
	 *
	 * @return
	 */
	public int getProductType() {
		return mDTVPlayer.getProductType();
	}

	/**
	 * @return
	 */
	public String getPlatformName() {
		String name = "";
		try {
			// name = mDTVSetting.getCurrentProductChassisName();
			name = getSWVersion();
			Log.i(TAG, "Dtvinterface getPlatformName() " + name);
		} catch (Exception e) {
			Log.e(TAG, "err in  getPlatformName() " + e);
		}
		return "";
	}

	/**
	 * @return
	 */
	public String getTVOSversion() {
		InterTVOSVersion tvos = mDTVSetting.getTVOSVersion();
		if (null != tvos) {
			return tvos.toString();
		} else {
			Log.i(TAG, "getTVOSversion() is null");
		}
		return "";
	}

	/**
	 * @return
	 */
	public String getAndroidVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 *
	 */
	public synchronized Bitmap getSubtitle() {
		byte[] data = new byte[1024];
		if (null != data) {
			return BitmapFactory.decodeByteArray(data, 0, data.length);
		}
		return null;
	}
}