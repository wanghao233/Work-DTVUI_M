/**
 * @filename 锟斤拷DTVPlayer
 * DTV锟斤拷锟斤拷锟斤拷亟涌锟�
 * @author:
 * @date:
 * @version 0.1
 * @history: 2012-9-13  锟斤拷锟接匡拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷沤锟侥匡拷锟脚革拷为锟斤拷态锟斤拷锟斤拷锟斤拷busy锟斤拷锟�
 * 2012-12-25 锟斤拷锟狡碉拷前锟斤拷目锟斤拷锟斤拷锟节诧拷锟斤拷锟斤拷息锟斤拷锟斤拷锟矫和革拷位
 * 2012-12-28 锟斤拷锟接凤拷锟酵伙拷台锟姐播
 */
package com.changhong.tvos.dtv.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.changhong.tvos.common.HotelManager;
import com.changhong.tvos.common.ITVPlayer;
import com.changhong.tvos.common.SoundManager;
import com.changhong.tvos.common.TVManager;
import com.changhong.tvos.common.exception.IllegalValueException;
import com.changhong.tvos.common.exception.TVManagerNotInitException;
import com.changhong.tvos.dtv.service.jni.avplayercallback;
import com.changhong.tvos.dtv.service.jni.caci;
import com.changhong.tvos.dtv.service.jni.cacicallback;
import com.changhong.tvos.dtv.service.jni.channelInfoChangecallback;
import com.changhong.tvos.dtv.service.jni.epgcallback;
import com.changhong.tvos.dtv.service.jni.nvodcallback;
import com.changhong.tvos.dtv.service.jni.scancallback;
import com.changhong.tvos.dtv.vo.AudioTrack;
import com.changhong.tvos.dtv.vo.CICAMInformation;
import com.changhong.tvos.dtv.vo.CICAMMenuBase;
import com.changhong.tvos.dtv.vo.CICAMMessageBase;
import com.changhong.tvos.dtv.vo.CICAMMessageBase.ConstCICAMsgType;
import com.changhong.tvos.dtv.vo.CarrierInfo;
import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DTVCardStatus;
import com.changhong.tvos.dtv.vo.DTVChannelBaseInfo;
import com.changhong.tvos.dtv.vo.DTVChannelDetailInfo;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.dtv.vo.DTVConstant.BroadcastConst;
import com.changhong.tvos.dtv.vo.DTVDTTime;
import com.changhong.tvos.dtv.vo.DTVSource;
import com.changhong.tvos.dtv.vo.DTVTunerStatus;
import com.changhong.tvos.dtv.vo.DTVVideoInfo;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.dtv.vo.DVBSTransponder;
import com.changhong.tvos.dtv.vo.DVBTCarrier;
import com.changhong.tvos.dtv.vo.EPGEvent;
import com.changhong.tvos.dtv.vo.NvodRefEvent;
import com.changhong.tvos.dtv.vo.NvodRefService;
import com.changhong.tvos.dtv.vo.NvodShiftEvent;
import com.changhong.tvos.dtv.vo.PlayStatusInfo;
import com.changhong.tvos.dtv.vo.ScanStatusInfo;
import com.changhong.tvos.dtv.vo.StartControlInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DTVPlayer extends IDTVPlayer.Stub implements IRmlistener {

	final static String TAG = "DtvService.DTVPlayer";

	/**
	 * ROUTER状态锟斤拷DTVRouterInfo锟斤拷状态
	 */
	static final int ROUTER_INIT = 0;
	static final int ROUTER_VALID = 1;
	static final int ROUTER_DESTROIED = 3;
	static final int ROUTER_USED_BY_OTHER = 4;

	//HotelManager       tvHotelManager = null;
	HotelManager hotelM = null;

	TVManager tvM = null;//锟叫伙拷前锟斤拷锟斤拷
	SoundManager soundM = null;
	ITVPlayer tvplayer = null;

	private static DTVServiceRm rmManager = null;// 锟斤拷源锟斤拷锟斤拷锟斤拷
	private Context context;

	private String objmUuid;
	private int objiTunerID = -1;
	private int objeLayerType = -1;
	private int objiIndex = -1;
	public int objiCLientType = -1;
	private int objRouterID = -1;
	public int objRouterStatus = ROUTER_INIT;

	public static int isPlaying = 0;
	public static int currentProgramID = -1;
	public static int lastProgramID = -1;
	public static int isDTV_Busying = 0;
	public static int miTunerStatus = -1;
	public static int iProductType = 0;
	public static boolean isRelease = false; //release resources...
	public static boolean isPrepare = false;
	public static List<Integer> rmLock = new ArrayList<Integer>(2);

	/**0:unused;1:play;2:pvr**/
	static {
		Log.i(TAG, "[lsy static]");
		rmLock.add(0, 0);
		rmLock.add(1, 0);
	}

	public static int DtvUIActivityState = 0;

	private static int recordProgramID = -1;

	static scancallback_class callback_obj1 = null;
	static avplayercallback_class callback_obj2 = null;
	static cacicallback_class callback_obj3 = null;
	static epgcallback_class callback_obj4 = null;
	static nvodcallback_class callback_obj5 = null;
	static ChannelInfocallback_class callbackobj = null;
	private PVRRecord pvr;
	private PlayerPvrCallback pvrCallback;

	public DTVPlayer(Context context, String mUuid, int iTunerID, int eLayerType, int iIndex, int iCLientType) {
		objmUuid = mUuid;
		objiTunerID = iTunerID;
		objeLayerType = eLayerType;
		objiIndex = iIndex;
		objiCLientType = iCLientType;
		this.context = context;
		Log.i(TAG, "[lsy entry DTVPlayer]");

		if (rmManager == null) {
			rmManager = DTVServiceRm.getinstance();
			if (rmManager != null) {
				rmManager.RmSetTVOS(true, context);
				rmManager.RmRgListener(this); //注锟斤拷氐锟�
			} else {
				Log.i(TAG, "[rmManager is null]");
			}
		}

		/**挪锟斤拷prepare锟叫撅拷锟藉创锟斤拷**/
		//	objRouterID = DTVServiceJNI.get_system_instance().createRouter(iTunerID, eLayerType, iIndex);
		//	Log.i(TAG, "DTVPlayer() routeid" + objRouterID);

		callback_obj1 = new scancallback_class();
		callback_obj2 = new avplayercallback_class();
		callback_obj3 = new cacicallback_class();
		callback_obj4 = new epgcallback_class();
		callback_obj5 = new nvodcallback_class();

		callbackobj = new ChannelInfocallback_class();
		Log.i(TAG, "objRouterStatus=0,ROUTER_INIT");
		objRouterStatus = ROUTER_INIT;

		//  HotelManager hotelM = null;
		tvM = TVManager.getInstance(null);
		try {
			hotelM = tvM.getHotelManager();
			//	Log.i(TAG, "setHotelMode-->>>>>>"+bHotelMode );
			//	hotelM.setHotelMode(bHotelMode);
		} catch (TVManagerNotInitException e) {
			e.printStackTrace();
		}

		//锟叫伙拷前锟斤拷锟斤拷
		/*
		tvM = TVManager.getInstance(null);
		try{
			soundM = tvM.getSoundManager();
			tvplayer = tvM.getTVPlayer();
		}catch(TVManagerNotInitException e){
			// TO DO nothing
		}
		*/

		new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);

						handle_PlayedMessage();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
		pvr = PVRRecord.getInstance(context);
		pvr.setPlayer(this);
		Log.i(TAG, "[lsy leave DTVPlayer]");
	}

	public void setCallback(PlayerPvrCallback callback) {
		pvrCallback = callback;
		//release resource
		Log.i(TAG, "[lsy pvr call player release start]");
		try {
			isRelease = true;
			Release();
			isRelease = false;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Log.i(TAG, "[lsy pvr call player release end]");
		pvrCallback.ReleaseResource(0);
	}

	void handle_PlayedMessage() {
		int tmpRecrodId = -1;
		if (recordProgramID == -1) {
			return;
		}

		tmpRecrodId = recordProgramID;
		recordProgramID = -1;
		//锟斤拷锟斤拷锟侥硷拷
		DTVService.createCurchTmpFile(tmpRecrodId);

		DTVChannelDetailInfo channelInfo = DTVServiceJNI.get_channelmanager_instance().getChanneDetailInfo(tmpRecrodId);
		if (channelInfo != null) {
			EPGEvent[] event = DTVServiceJNI.get_epg_instance().getPFEvent(objRouterID, tmpRecrodId);
			try {
				//锟斤拷锟斤拷TVOS
				if (event != null) {
					TVManager.getInstance(context).getSystemManager().setDTVPlayingInfo(channelInfo.mstrServiceName, event[0].eventName);
				} else {
					TVManager.getInstance(context).getSystemManager().setDTVPlayingInfo(channelInfo.mstrServiceName, null);
				}
			} catch (TVManagerNotInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//锟斤拷锟酵伙拷台锟姐播
			Intent myintent = new Intent("com.changhong.tvos.dtv.CHANNEL_CHANGED");
			Bundle bundle = new Bundle();

			bundle.putString("channelName", channelInfo.mstrServiceName);
			bundle.putInt("channelIndex", tmpRecrodId);

			myintent.putExtras(bundle);
			context.sendBroadcast(myintent);
		} else {
			Log.i(TAG, "[channelInfo is null]");
		}
	}

	private boolean CheckRouterValid() {
		if (objRouterStatus == ROUTER_VALID && objRouterID != -1) {
			return true;
		} else {
			Log.e(TAG, "route is fialed id:" + objRouterID + "status:" + objRouterStatus);
			return false;
		}
	}

	//@Override
	public synchronized int epgStart() throws RemoteException {
		// TODO Auto-generated method stub
		Log.i("epg service", "epgStart");
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_epg_instance().epgStart(objRouterID);
	}

	//@Override
	public synchronized int epgStop() throws RemoteException {
		// TODO Auto-generated method stub
		Log.i("epg service", "epgStop");
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_epg_instance().epgStop(objRouterID);
	}

	//@Override
	public synchronized int epgSuspend() throws RemoteException {
		// TODO Auto-generated method stub
		Log.i("epg service", "epgSuspend");
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_epg_instance().epgSuspend(objRouterID);
	}

	//@Override
	public synchronized int epgResume() throws RemoteException {
		// TODO Auto-generated method stub
		Log.i("epg service", "epgResume");
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_epg_instance().epgResume(objRouterID);
	}

	//@Override
	public EPGEvent[] getPFEvent(int iChannelIndex) throws RemoteException {
		// TODO Auto-generated method stub
		Log.i("epg service", "getPFEvent");
		//		if(CheckRouterValid() == false)
		//		{
		//		  return  null;
		//		}
		return DTVServiceJNI.get_epg_instance().getPFEvent(objRouterID, iChannelIndex);

	}

	//@Override
	public EPGEvent[] getSchelueEvent(int iChannelIndex) throws RemoteException {
		// TODO Auto-generated method stub
		Log.i("epg service", "getSchelueEvent");
		//		if(CheckRouterValid() == false)
		//		{
		//		  return  null;
		//		}
		return DTVServiceJNI.get_epg_instance().getSchelueEvent(objRouterID, iChannelIndex);
	}

	//@Override
	public EPGEvent[] getSchelueEventByTime(int channelIndex, DTVDTTime startTime, DTVDTTime endTime) throws RemoteException {
		// TODO Auto-generated method stub
		Log.i("epg service", "getSchelueEventByTime");
		//		if(CheckRouterValid() == false)
		//		{
		//		  return  null;
		//		}
		return DTVServiceJNI.get_epg_instance().getSchelueEventByTime(objRouterID, channelIndex, startTime, endTime);
	}

	//@Override
	public String getPFEventExtendInfo(int iChannelIndex, int iEventID) throws RemoteException {
		// TODO Auto-generated method stub
		Log.i("epg service", "getPFEventExtendInfo");
		//		if(CheckRouterValid() == false)
		//		{
		//		  return  null;
		//		}
		return DTVServiceJNI.get_epg_instance().getPFEventExtendInfo(objRouterID, iChannelIndex, iEventID);
	}

	//@Override
	public String getSchelueEventExtendInfo(int iChannelIndex, int iEventID) throws RemoteException {
		// TODO Auto-generated method stub
		Log.i("epg service", "getSchelueEventExtendInfo");
		//		if(CheckRouterValid() == false)
		//		{
		//		  return  null;
		//		}
		return DTVServiceJNI.get_epg_instance().getPFEventExtendInfo(objRouterID, iChannelIndex, iEventID);
	}

	//@Override
	public synchronized int SetDVBCParam(int iScanMode, DVBCCarrier[] lFreqList) throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "SetDVBCParam");

		if (CheckRouterValid() == false) {
			return -1;
		}

		return DTVServiceJNI.get_scan_instance().SetDVBCParam(objRouterID, iScanMode, lFreqList);
	}

	//@Override
	public synchronized int SetDVBSParam(int iScanMode, DVBSTransponder[] lFreqList) throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "SetDVBSParam");

		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_scan_instance().SetDVBSParam(objRouterID, iScanMode, lFreqList);
	}

	//@Override
	public synchronized int SetDVBTParam(int iScanMode, DVBTCarrier[] lFreqList) throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "SetDVBTParam");

		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_scan_instance().SetDVBTParam(objRouterID, iScanMode, lFreqList);
	}

	//@Override
	public synchronized int SetDMBTHParam(int iScanMode, DMBTHCarrier[] lFreqList) throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "SetDMBTHParam");

		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_scan_instance().SetDMBTHParam(objRouterID, iScanMode, lFreqList);
	}

	//@Override
	public synchronized int setSatellite(int iSatelliteID) throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "setSatellite");

		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_scan_instance().setSatellite(objRouterID, iSatelliteID);
	}

	//@Override
	public synchronized int scanStart() throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "scanStart");
		if (CheckRouterValid() == false) {
			Log.i(TAG, "router is invalid..");
			return -1;
		}
		SetDtvBusyState(1);
		lastProgramID = currentProgramID;
		currentProgramID = -1;

		return DTVServiceJNI.get_scan_instance().scanStart(objRouterID);
	}

	//@Override
	public synchronized int scanStop() throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "scanStop");

		if (CheckRouterValid() == false) {
			return -1;
		}
		SetDtvBusyState(0);
		return DTVServiceJNI.get_scan_instance().scanStop(objRouterID);
	}

	//@Override
	public synchronized int setSwitchMode(int iSwitchMode) throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "setSwitchMode");
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_avplayer_instance().setSwitchMode(objRouterID, iSwitchMode);
	}

	//@Override
	public int setVolume(int volume) throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "setVolume");

		if (CheckRouterValid() == false) {
			return -1;
		}
		try {
			DTVServiceRm.getinstance().tvos.tvos_SoundManager.updateVolumeCompVal(0);
			DTVServiceRm.getinstance().tvos.tvos_SoundManager.setVolume(volume);
			Log.i(TAG, "setVolume =" + volume);
		} catch (IllegalValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		//return DTVServiceJNI.get_avplayer_instance().setVolume(objRouterID,volume);
	}

	//@Override
	public int getVolume() throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "getVolume");

		int volume = -1;
		if (CheckRouterValid() == false) {
			return -1;
		}
		try {
			volume = DTVServiceRm.getinstance().tvos.tvos_SoundManager.getVolume();
			Log.i(TAG, "getVolume =" + volume);

		} catch (IllegalValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return volume;
		//return DTVServiceJNI.get_avplayer_instance().getVolume(objRouterID);
	}

	//@Override
	public synchronized int play(int iChannelIndex) throws RemoteException {
		Log.i(TAG, "[Entry play()]" + "[iChannelIndex:" + iChannelIndex + "]");

		int iResult = -1;
		int iCurChannelIndex = -1;
		DTVTunerStatus mTunerStatus = null;

		if (CheckRouterValid() == false) {
			Log.i(TAG, "[play() checkRouterValid is false]");
			return -1;
		}
		if (soundM != null) {
			soundM.enableUserMute(true);
		}

		iCurChannelIndex = DTVServiceJNI.get_avplayer_instance().getCurChannelIndex(objRouterID);
		//2013-03-05 test, iCurChannelIndex=-1
		Log.i(TAG, "[objRouterID]" + objRouterID);
		Log.i(TAG, "[iChannelIndex]" + iChannelIndex + "[currentProgramID]" + currentProgramID + "[iCurChannelIndex]" + iCurChannelIndex);
		if ((iChannelIndex == currentProgramID) && (iChannelIndex == iCurChannelIndex)) {
			iResult = 0;
			Log.i(TAG, "play --->iChannelIndex = currentProgramID =" + currentProgramID);
		} else {
			iResult = DTVServiceJNI.get_avplayer_instance().play(objRouterID, iChannelIndex);
		}
		Log.i(TAG, "[iResult]" + iResult);
		if (iResult == 0) {
			lastProgramID = currentProgramID;
			currentProgramID = iChannelIndex;
			recordProgramID = iChannelIndex;
			isPlaying = 1;
			Log.i(TAG, "[lastProgramID]" + lastProgramID + "[currentProgramID]" + currentProgramID + "[recordProgramID]" + recordProgramID);
			mTunerStatus = DTVServiceJNI.get_avplayer_instance().getTunerStatus(objRouterID);
			Log.i(TAG, "mTunerStatus=" + mTunerStatus);
			if (mTunerStatus != null) {
				Log.i(TAG, "[mTunerStatus]" + mTunerStatus.miBitErrorRate + ";" + mTunerStatus.miSignalLevel + ";" + mTunerStatus.miSignalQuality + ";" + mTunerStatus.miSignLevelPercent + ";"
						+ mTunerStatus.miSNR + ";" + mTunerStatus.mbLock);
				if (!mTunerStatus.mbLock) {
					Log.e(TAG, "------>handle_HisPlayMessage--DTVTunerStatus-is unLock");
					miTunerStatus = 0;
				} else {
					miTunerStatus = 1;
				}
			} else {
				Log.e(TAG, "------>handle_HisPlayMessage--DTVTunerStatus-is NULL");
				miTunerStatus = 0;
			}
		} else {
			if (currentProgramID != -1) {
				lastProgramID = currentProgramID;
				currentProgramID = -1;
			}
		}
		Log.i(TAG, "[leave play]");
		return iResult;
	}

	//@Override
	public synchronized int playvod(int iFrequencyK, int iSymbolRateK, int eQamMode, int iServiceID, int PMTPID) throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}

		lastProgramID = currentProgramID;
		currentProgramID = -1;

		isPlaying = 1;
		if (soundM != null) {
			soundM.enableUserMute(true);
		}

		int iResult = DTVServiceJNI.get_avplayer_instance().playvod(objRouterID, iFrequencyK, iSymbolRateK, eQamMode, iServiceID, PMTPID);
		if (soundM != null) {
			soundM.enableUserMute(false);
		}
		return iResult;
	}

	//@Override
	public synchronized int playStop() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}

		if (currentProgramID != -1) {
			lastProgramID = currentProgramID;
			currentProgramID = -1;
		}

		isPlaying = 0;

		if (soundM != null) {
			soundM.enableUserMute(true);
		}

		if (tvplayer != null) {
			tvplayer.muteVideo();
		}
		return DTVServiceJNI.get_avplayer_instance().playStop(objRouterID);
	}

	//@Override
	public synchronized int Pausevideo() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_avplayer_instance().Pausevideo(objRouterID);
	}

	//@Override
	public synchronized int Resumevideo() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_avplayer_instance().Resumevideo(objRouterID);
	}

	//@Override
	public synchronized int PauseAudio() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_avplayer_instance().PauseAudio(objRouterID);
	}

	//@Override
	public synchronized int ResumeAudio() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_avplayer_instance().ResumeAudio(objRouterID);
	}

	//@Override
	public int getPlayingProgramID() throws RemoteException {
		// TODO Auto-generated method stub
		return currentProgramID;
	}

	//@Override
	public int setPlayingProgramID(int nProgramID) throws RemoteException {
		// TODO Auto-generated method stub
		if (nProgramID == -1) {
			Log.i(TAG, "setPlayingProgramID  == -1");
		}
		currentProgramID = nProgramID;
		return currentProgramID;
	}

	//@Override
	public int getLastProgramID() throws RemoteException {
		// TODO Auto-generated method stub
		return lastProgramID;
	}

	//@Override
	public int isPlaying() throws RemoteException {
		// TODO Auto-generated method stub
		return isPlaying;
	}

	//@Override
	public synchronized AudioTrack getChannelAudioTrack() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return null;
		}
		return null;
	}

	//@Override
	public synchronized int setChannelAudioTrack(int iAudioTrack) throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_avplayer_instance().setChannelAudioTrack(objRouterID, iAudioTrack);
	}

	//@Override
	public synchronized DTVVideoInfo getVideoInfo() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return null;
		}
		return DTVServiceJNI.get_avplayer_instance().getVideoInfo(objRouterID);
	}

	//@Override
	public synchronized int clearVideoBuffer() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_avplayer_instance().clearVideoBuffer(objRouterID);
	}

	//@Override
	public synchronized CICAMInformation getInfo() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return null;
		}
		return DTVServiceJNI.get_caci_instance().getInfo(objRouterID);
	}

	//@Override
	public synchronized String getSmartCardID() throws RemoteException {
		// TODO Auto-generated method stub
		//		if(CheckRouterValid() == false)
		//		{
		//		  return  null;
		//		}
		return DTVServiceJNI.get_caci_instance().getSmartCardID(objRouterID);
	}

	//@Override
	public synchronized int clearUserData() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_caci_instance().clearUserData(objRouterID);
	}

	//@Override
	public synchronized void queryControl(int iMsgType, int iMsgID, int iMenuID, int operand, int opcode, int defOpcode, int inputItems, String[] inputList) throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return;
		}
		DTVServiceJNI.get_caci_instance().queryControl(objRouterID, iMsgType, iMsgID, iMenuID, operand, opcode, defOpcode, inputItems, inputList);
	}

	//@Override
	public synchronized int DFAControl(int iType, int iMsgID, int iOperand, int iOpcode, String[] strInputList) throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_dfa_instance().JNI_DFA_Control(objRouterID, iType, iMsgID, iOperand, iOpcode, strInputList);
	}

	//@Override
	public synchronized int start() throws RemoteException {

		Log.i("FFFFFFYYYYYYYY 0000888888 ", "service start()!!!!");
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_system_instance().bootservice(objRouterID);
	}

	//@Override
	public synchronized int checkVersion(int curVersion) throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_system_instance().checkVersion(curVersion);
	}

	//@Override
	public synchronized DTVDTTime getTDTTime() throws RemoteException {
		// TODO Auto-generated method stub
		//		if(CheckRouterValid() == false)
		//		{
		//		  return  null;
		//		}
		return DTVServiceJNI.get_system_instance().getTDTTime();
	}

	//@Override
	public int prepare() throws RemoteException {
		Log.i(TAG, "Enter prepare-->RouterStatus:" + objRouterStatus + "->objRouterID:" + objRouterID + this + "->this_uuid=" + this.objmUuid);
		Log.i(TAG, "prepare return 0000  DtvUIActivityState:" + DtvUIActivityState);
		isPrepare = true;
		if (isRelease) {
			Log.i(TAG, "[lsy_player is releasing]");
			while (true) {
				if (!isRelease) {
					Log.i(TAG, "is not release");
					break;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		synchronized (rmLock) {
			Log.i(TAG, "[lsy rmLock=]" + rmLock.get(0));
			if (rmLock.get(0) == 2) {
				pvr.setCallback(new PlayerPvrCallback() {
					@Override
					public void ReleaseResource(int iret) {
						if (iret == 0) { //pvr release success
							Log.i(TAG, "[lsy pvr release success]");
						} else {
							Log.i(TAG, "[lsy pvr release fail]");
						}
					}
				});
			}
		}
		Log.i(TAG, "lsy callback end");
		if (DtvUIActivityState == 5) {
			Log.i(TAG, "prepare return 1111  DtvUIActivityState:" + DtvUIActivityState);
			return 0;
		}
		synchronized (rmManager.RouterList) {
			int result;
			int request_type = DTVServiceRm.request_auto;
			if ((objRouterStatus == ROUTER_VALID) && (objRouterID != -1)) {
				Log.i(TAG, "RmRequest RouterStatus:" + objRouterStatus);
				return 0;
			}

			boolean isDTVRs = TVOSService.getinstance(context).requestResource(TVOSService.RSS_DTV);
			rmManager.SetRsStaus(isDTVRs);
			Log.i(TAG, "[isDTVRs]" + isDTVRs + "[objiCLientType]" + objiCLientType);
			if (objiCLientType == RsClient.CLIENT_FORCE) {
				request_type = DTVServiceRm.request_force;
			} else if (objiCLientType == RsClient.CLIENT_AUTO) {
				request_type = DTVServiceRm.request_auto;
			} else if (objiCLientType == RsClient.CLIENT_SHARE) { //used
				request_type = DTVServiceRm.request_share;
			} else if (objiCLientType == RsClient.CLIENT_PRIORITY) {
				request_type = DTVServiceRm.request_prioity;
			}

			//锟酵凤拷锟斤拷源锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷馗锟斤拷锟斤拷锟絉OUTER_DESTROIED/ROUTER_USED_BY_OTHER
			Log.i(TAG, "[objRouterStatus]" + objRouterStatus);
			if (objRouterStatus != ROUTER_INIT) {
				rmManager.RmRelease(objiTunerID, Resource.RESOURCE_TUNER, objmUuid);
				rmManager.RmRelease(objeLayerType, Resource.RESOURCE_VIDEODECODER, objmUuid);
				rmManager.RmRelease(objiIndex, Resource.RESOURCE_VIDEOWINDDOW, objmUuid);
			}
			Log.i(TAG, "[objiTunerID]" + objiTunerID + "[Resource.RESOURCE_TUNER]" + Resource.RESOURCE_TUNER + "[request_type]" + request_type + "[objmUuid]" + objmUuid + "[objiCLientType]"
					+ objiCLientType);
			result = rmManager.RmRequest(objiTunerID, Resource.RESOURCE_TUNER, request_type, objmUuid, objiCLientType);
			if (result != 0) {
				Log.i(TAG, "RmRequest RESOURCE_TUNER result=" + result);
				return -1;
			}

			result = rmManager.RmRequest(objeLayerType, Resource.RESOURCE_VIDEODECODER, request_type, objmUuid, objiCLientType);
			if (result != 0) {
				Log.i(TAG, "RmRequest RESOURCE_VIDEODECODER result=" + result);
				result = rmManager.RmRelease(objiTunerID, Resource.RESOURCE_TUNER, objmUuid);
				return -1;
			}
			result = rmManager.RmRequest(objiIndex, Resource.RESOURCE_VIDEOWINDDOW, request_type, objmUuid, objiCLientType);
			if (result != 0) {
				Log.i(TAG, "RmRequest RESOURCE_VIDEOWINDDOW result=" + result);
				result = rmManager.RmRelease(objiTunerID, Resource.RESOURCE_TUNER, objmUuid);
				result = rmManager.RmRelease(objeLayerType, Resource.RESOURCE_VIDEODECODER, objmUuid);
				return -1;
			}
			//锟斤拷锟斤拷锟斤拷源锟缴癸拷锟斤拷锟斤拷锟矫讹拷应JNI锟接匡拷锟斤拷实锟绞碉拷锟斤拷源锟斤拷锟斤拷锟斤拷锟斤拷
			Log.i(TAG, "[objiTunerID/objeLayerType/objiIndex request successed]");
			Log.i(TAG, "[lsy player requestrouterid begin]");

			objRouterID = rmManager.RequestRouteID(objiTunerID, objeLayerType, objiIndex, this);
			Log.i(TAG, "[lsy player requestrouterid end]");
			DTVServiceJNI.get_scan_instance().RegisterCallback(objRouterID, callback_obj1);
			DTVServiceJNI.get_avplayer_instance().RegisterCallback(objRouterID, callback_obj2);
			DTVServiceJNI.get_caci_instance().RegisterCallback(objRouterID, callback_obj3);
			//DTVServiceJNI.get_epg_instance().RegisterCallback(objRouterID, callback_obj4);
			//DTVServiceJNI.get_nvod_instance().RegisterCallback(objRouterID, callback_obj5);

			DTVServiceJNI.get_system_instance().RegisterCallback(callbackobj);

			Log.i(TAG, "prepare() routeid" + objRouterID);
			if (objRouterID != -1) {
				Log.i(TAG, "objRouterStatus=1,valid");
				objRouterStatus = ROUTER_VALID;
			}
			Log.i(TAG, "[mManager.RmRgListener]");
			//锟斤拷锟斤拷注锟斤拷氐锟斤拷锟斤拷锟斤拷锟节讹拷锟絧layer锟斤拷锟叫伙拷锟斤拷锟秸诧拷锟斤拷锟截碉拷锟斤拷锟斤拷锟�
			rmManager.RmRgListener(this);
			isPrepare = false;
			return 0;
		}
	}

	//@Override
	public int Release() throws RemoteException {
		Log.i(TAG, "Release()[enter] objRouterID = " + objRouterID);
		synchronized (rmManager.RouterList) {
			int result;
			result = rmManager.RmRelease(objiTunerID, Resource.RESOURCE_TUNER, objmUuid);
			Log.i(TAG, "[releaseTunner]" + result);
			result = rmManager.RmRelease(objeLayerType, Resource.RESOURCE_VIDEODECODER, objmUuid);
			Log.i(TAG, "[releaseVideoDecoder]" + result);
			result = rmManager.RmRelease(objiIndex, Resource.RESOURCE_VIDEOWINDDOW, objmUuid);
			Log.i(TAG, "[releaseVideowindow]" + result);
			if (result == 0) {
				//锟酵凤拷锟斤拷源锟缴癸拷锟斤拷锟斤拷锟矫讹拷应JNI锟接匡拷锟斤拷实锟绞碉拷锟斤拷源锟斤拷锟斤拷锟斤拷锟斤拷
				if (objRouterID != -1) {
					synchronized (rmLock) {
						Log.i(TAG, "[lsy player release destroy begin]" + rmLock.get(0));
						rmManager.DestroyRouteID(objiTunerID, objeLayerType, objiIndex, this, false);
						objRouterStatus = ROUTER_DESTROIED;
						if (pvr.mChannelID != -1 && PVRRecord.isRecordMode == true) {
							pvr.PVR_REC_START(pvr.mHID, pvr.mChannelID, pvr.mUrl, pvr.mChannelName, pvr.msTime, pvr.meTime);
						}
						Log.i(TAG, "[lsy player release destroy end]");
					}
				}
			} else {
				Log.i(TAG, "Release_rmManager.RmRelease fail");
			}

		}
		return 0;
	}

	//@Override
	public int SetDVBCTuner(DVBCCarrier carrierInfo) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	//@Override
	public int SetDVBSTuner(DVBSTransponder carrierInfo) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	//@Override
	public int SetDVBTTuner(DVBTCarrier carrierInfo) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	//@Override
	public int SetDMBTHTuner(DMBTHCarrier carrierInfo) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	//@Override
	public DTVTunerStatus getTunerStatus() throws RemoteException {
		// TODO Auto-generated method stub

		return DTVServiceJNI.get_avplayer_instance().getTunerStatus(objRouterID);
	}

	//@Override
	public synchronized DVBCCarrier getDVBCCurTunerInfo() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return null;
		}
		return DTVServiceJNI.get_avplayer_instance().getDVBCCurTunerInfo(objRouterID);
	}

	//@Override
	public synchronized DVBSTransponder getDVBSCurTunerInfo() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return null;
		}
		return DTVServiceJNI.get_avplayer_instance().getDVBSCurTunerInfo(objRouterID);
	}

	//@Override
	public synchronized DVBTCarrier getDVBTCurTunerInfo() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return null;
		}
		return DTVServiceJNI.get_avplayer_instance().getDVBTCurTunerInfo(objRouterID);
	}

	//@Override
	public synchronized DMBTHCarrier getDMBTHCurTunerInfo() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return null;
		}
		return DTVServiceJNI.get_avplayer_instance().getDMBTHCurTunerInfo(objRouterID);
	}

	//瀹炵幇璧勬簮锟接讹拷?锟斤拷雽栫洃鍚拷茷璋冿紝璇ュ疄鐜扮敱瀹㈡埛绔拷氩堬拷锟�??
	public void RmlistenCallback(int Staus, int RsID, int RsType, String ClientID) {
		Log.i(TAG, "Enter RmlistenCallback-->Staus:" + Staus + " RsID:" + RsID + "RsType:" + RsType + " ClientID:" + ClientID);
		switch (Staus) {
			case Resource.STATUS_FREEED:
				Log.i(TAG, "RmlistenCallback STATUS_FREEED,RsID=" + RsID + "ClientID =" + ClientID);
				break;
			case Resource.STATUS_USED:
				Log.i(TAG, "RmlistenCallback STATUS_USED,RsID=" + RsID + "ClientID =" + ClientID);
				break;
			case Resource.STATUS_FORCEUSED:
				Log.i(TAG, "RmlistenCallback STATUS_FORCEUSED,RsID=" + RsID + "ClientID =" + ClientID);
				if (ClientID != objmUuid)/*涓嶆槸锟斤拷锟芥湰韬姠鍗犲紩璧风殑callback*/ {
					int result;
					//閲婃斁鐩稿叧璧勬簮鍙婂?
					result = rmManager.RmRelease(RsID, RsType, objmUuid);
					Log.i(TAG, "RmlistenCallback RmRelease RsType=" + RsType + "result=" + result);
				}
				isDTV_Busying = 0;
				Log.i(TAG, "objRouterStatus=3,destroied");
				objRouterStatus = ROUTER_DESTROIED;
				//com.changhong.tvos.dtv.DTV_PLAYER_STATUS_CHANGE
				Intent intent = new Intent(DTVConstant.DTV_PLAYER_STATUS_CHANGE);
				Bundle bundle = new Bundle();

				PlayStatusInfo status = new PlayStatusInfo();
				status.mbIsUseUUID = true;
				status.mPlayerUuid = UUID.fromString(objmUuid);
				status.miPlayEvent = DTVConstant.ConstPlayerEvent.FORCE_PAUSE;
				status.mstrPrompt = null;

				bundle.putInt(DTVConstant.BroadcastConst.MSG_TYPE_NAME, 0);
				bundle.putParcelable(DTVConstant.BroadcastConst.MSG_INFO_NAME, status);
				intent.putExtras(bundle);
				context.sendBroadcast(intent);
				break;
			default:
				break;
		}
	}

	//@Override
	public synchronized int nvodstart() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_nvod_instance().nvodstart(objRouterID);
	}

	//@Override
	public synchronized int nvodstop() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_nvod_instance().nvodstop(objRouterID);
	}

	//@Override
	public synchronized int nvodsuspend() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_nvod_instance().nvodsuspend(objRouterID);
	}

	//@Override
	public synchronized int nvodresume() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return -1;
		}
		return DTVServiceJNI.get_nvod_instance().nvodresume(objRouterID);
	}

	//@Override
	public synchronized NvodRefService[] getRefServices() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return null;
		}
		return DTVServiceJNI.get_nvod_instance().getRefServices(objRouterID);
	}

	//@Override
	public synchronized NvodRefEvent[] getRefEvents(int serviceId) throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return null;
		}
		return DTVServiceJNI.get_nvod_instance().getRefEvents(objRouterID, serviceId);
	}

	//@Override
	public synchronized NvodShiftEvent[] getShiftEvents(int serviceId, int refEventId) throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckRouterValid() == false) {
			return null;
		}
		return DTVServiceJNI.get_nvod_instance().getShiftEvents(objRouterID, serviceId, refEventId);
	}

	public class scancallback_class implements scancallback {

		@Override
		public int ScanStatusInfo_Callback(int routerID, ScanStatusInfo obj, CarrierInfo[] lCarrier, DTVChannelBaseInfo[] lService, DTVTunerStatus turnerStatus) {
			// TODO Auto-generated method stub
			Log.i(TAG, "[routerID]" + routerID);
			Log.i(TAG, "" + obj.miCurCarrierIndex);
			Log.i(TAG, "" + obj.miProgress);
			Log.i(TAG, "" + obj.miStatus);
			Log.i(TAG, "" + obj.mstrPrompt);
			if (lCarrier != null) {
				Log.i(TAG, "[lCarrierLen]" + lCarrier.length);
				for (int i = 0; i < lCarrier.length; i++) {
					Log.i(TAG, "[lCarriermcSpectrum" + i + "]" + lCarrier[i].mcSpectrum);
					Log.i(TAG, "[lCarriermiDemodType" + i + "]" + lCarrier[i].miDemodType);
					Log.i(TAG, "[lCarriermiFrequencyK" + i + "]" + lCarrier[i].miFrequencyK);
					Log.i(TAG, "[lCarriermiTsID" + i + "]" + lCarrier[i].miTsID);
				}
			}
			if (lService != null) {
				Log.i(TAG, "[lServiceLen]" + lService.length);
				for (int i = 0; i < lService.length; i++) {
					Log.i(TAG, "[lServicemmiServiceType" + i + "]" + lService[i].miServiceType);
					Log.i(TAG, "[lServicemstrServiceName" + i + "]" + lService[i].mstrServiceName);
					Log.i(TAG, "[lServicemiChannelIndex" + i + "]" + lService[i].miChannelIndex);
					Log.i(TAG, "[lServicemiChannelnumber" + i + "]" + lService[i].miChannelnumber);
					Log.i(TAG, "[lServicemiServiceID" + i + "]" + lService[i].miServiceID);
				}
			}
			if (turnerStatus != null) {
				Log.i(TAG, "[turnerStatusmbLock]" + turnerStatus.mbLock);
			}

			Intent myintent = new Intent(DTVConstant.SCAN_STATUS_BROADCAST);
			Bundle bundle = new Bundle();
			bundle.putParcelable(BroadcastConst.MSG_INFO_NAME, obj);

			Log.i(TAG, "ScanStatusInfo_Callback eventid: " + obj.miStatus);
			switch (obj.miStatus) {
				case ScanStatusInfo.ScanEvent.STATUS_INIT://0
				case ScanStatusInfo.ScanEvent.STATUS_NIT_BEGIN://2
				case ScanStatusInfo.ScanEvent.STATUS_NIT_NEXT://4
				case ScanStatusInfo.ScanEvent.STATUS_SORTED://9
				case ScanStatusInfo.ScanEvent.STATUS_SORTING://8
				case ScanStatusInfo.ScanEvent.STATUS_SAVING://10
				case ScanStatusInfo.ScanEvent.STATUS_SAVED://11
					break;

				case ScanStatusInfo.ScanEvent.STATUS_END://12
				case ScanStatusInfo.ScanEvent.STATUS_FAIL://13
					isDTV_Busying = 0;
					//节目列表改变（搜台/信号源改变等）
					new Thread() {
						public void run() {
							DTVService.createChlistTmpFile();
						}
					}.start();
					break;

				case ScanStatusInfo.ScanEvent.STATUS_INIT_END://1
					break;

				case ScanStatusInfo.ScanEvent.STATUS_NIT_DONE://3
					if (lCarrier != null) {
						bundle.putParcelableArray(BroadcastConst.MSG_INFO_NAME_1, lCarrier);
					} else {
						Log.e(TAG, "No Carrierlist find by nit !!!!!");
						return 0;
					}
					break;

				case ScanStatusInfo.ScanEvent.STATUS_TUNING_BEGIN://5
					if ((lCarrier != null) && (lCarrier.length > 0)) {
						if (obj.miCurCarrierIndex < lCarrier.length) {
							bundle.putParcelable(BroadcastConst.MSG_INFO_NAME_1, lCarrier[obj.miCurCarrierIndex]);
						} else {
							bundle.putParcelable(BroadcastConst.MSG_INFO_NAME_1, lCarrier[0]);
						}
					} else {
						Log.e(TAG, "no current carrierInfo!!!!!");
						return 0;
					}
					break;

				case ScanStatusInfo.ScanEvent.STATUS_TUNING_STATUS_FLUSH://6
					if (turnerStatus != null) {
						bundle.putParcelable(BroadcastConst.MSG_INFO_NAME_1, turnerStatus);
					} else {
						Log.e(TAG, "no tuner status!!!!!");
						return 0;
					}
					break;

				case ScanStatusInfo.ScanEvent.STATUS_SERVICE_DONE://7
					if ((lService != null) && (lService.length > 0)) {
						bundle.putParcelableArray(BroadcastConst.MSG_INFO_NAME_1, lService);
					} else {
						Log.e(TAG, "no channel list!!!!!");
						return 0;
					}
					break;
			}

			myintent.putExtras(bundle);
			context.sendBroadcast(myintent);
			return 0;
		}

		/** 锟斤拷锟姐播锟较诧拷锟斤拷一锟斤拷锟斤拷锟解几锟斤拷锟接口诧拷锟斤拷使锟斤拷
		 @Override public int ScanStatusInfo_Callback(int routerID,ScanStatusInfo obj,CarrierInfo[] lCarrier, DTVChannelBaseInfo[] lService)
		 // TODO Auto-generated method stub
		 Intent myintent = new Intent(DTVConstant.SCAN_STATUS_BROADCAST);
		 Bundle bundle = new Bundle();
		 Log.i(TAG,"ScanStatusInfo_Callback");

		 bundle.putString("calientid", objmUuid);
		 bundle.putParcelable("scan_msg_info", obj);
		 myintent.putExtras(bundle);
		 context.sendBroadcast(myintent);
		 return 0;
		 }

		 //@Override
		 public int CarrierInfo_Callback(int routerID, CarrierInfo obj) {
		 // TODO Auto-generated method stub
		 Intent myintent = new Intent(DTVConstant.SCAN_STATUS_BROADCAST);
		 Bundle bundle = new Bundle();
		 Log.i(TAG,"CarrierInfo_Callback");

		 bundle.putString("calientid", objmUuid);
		 bundle.putParcelable("scan_msg_info", obj);
		 myintent.putExtras(bundle);
		 context.sendBroadcast(myintent);
		 return 0;
		 }

		 //@Override
		 public int ScanChannelInfo_Callback(int routerID, ScanChannelInfo obj) {
		 // TODO Auto-generated method stub
		 Intent myintent = new Intent(DTVConstant.SCAN_STATUS_BROADCAST);
		 Bundle bundle = new Bundle();
		 Log.i(TAG,"ScanChannelInfo_Callback");

		 bundle.putString("calientid", objmUuid);
		 bundle.putParcelable("scan_msg_info", obj);
		 myintent.putExtras(bundle);
		 context.sendBroadcast(myintent);
		 return 0;
		 }

		 @Override public int ScanStatusInfo_Callback(int routerID, ScanStatusInfo obj,
		 CarrierInfo[] obj_Carrier, DTVChannelBaseInfo[] obj_Service) {
		 // TODO Auto-generated method stub
		 return 0;
		 }
		 **/

	}

	public class ChannelInfocallback_class implements channelInfoChangecallback {

		@Override
		public int channelInfoChanged_Callback(int routerID) {
			// TODO Auto-generated method stub

			Intent myintent = new Intent(DTVConstant.CHANNEL_INFO_CHANGED_BROADCAST);
			Log.i(TAG, "---------------->channel info is changed!");
			context.sendBroadcast(myintent);
			return 0;
		}
	}

	public class cacicallback_class implements cacicallback {
		//@Override
		public int Prompt_notify_Callback(int routerID, CICAMMessageBase obj) {
			// TODO Auto-generated method stub
			Intent myintent = new Intent(DTVConstant.DTV_CICAM_PROMPT_NOTIRY);
			Bundle bundle = new Bundle();
			Log.i(TAG, "Prompt_notify_Callback");
			if (obj == null) {
				Log.e(TAG, "Prompt_notify_Callback obj is null");
				return 0;
			}

			switch (obj.miMsgType) {
				case ConstCICAMsgType.MSG_MAIL:
				case ConstCICAMsgType.MSG_FINGER:
				case ConstCICAMsgType.MSG_PROMPT:
				case ConstCICAMsgType.MSG_SUBTITLE:
				case ConstCICAMsgType.MSG_ANNOUNCE:
				case ConstCICAMsgType.MSG_USER_MENU:
				case ConstCICAMsgType.MSG_FORCE_MENU:
				case ConstCICAMsgType.MSG_FORCE_RESORT:
				case ConstCICAMsgType.MSG_FORCE_RESCAN:
					break;
				case ConstCICAMsgType.MSG_FORCE_CHANNEL:
					lastProgramID = currentProgramID;
					currentProgramID = -1;
					break;
				case ConstCICAMsgType.MSG_NO_CARD_SETS:
				case ConstCICAMsgType.MSG_CHANNEL_UPDATE:
					break;
				default:
					Log.e(TAG, "Menu_notify_Callback: not support type");
					return 0;
			}

			bundle.putString("calientid", objmUuid);
			bundle.putInt(BroadcastConst.MSG_TYPE_NAME, obj.miMsgType);
			bundle.putParcelable("caci_msg_info", obj);
			myintent.putExtras(bundle);
			context.sendBroadcast(myintent);
			return 0;
		}

		//@Override
		public int Menu_notify_Callback(int routerID, CICAMMenuBase obj) {
			// TODO Auto-generated method stub
			Intent myintent = new Intent(DTVConstant.DTV_CICAM_PROMPT_NOTIRY);
			Bundle bundle = new Bundle();
			Log.i(TAG, "Menu_notify_Callback");

			if (obj == null) {
				Log.e(TAG, "Menu_notify_Callback obj is null");
				return 0;
			}

			if ((obj.miMsgType != ConstCICAMsgType.MSG_USER_MENU) && (obj.miMsgType != ConstCICAMsgType.MSG_FORCE_MENU)) {
				Log.e(TAG, "Menu_notify_Callback type is not right!");
				return 0;
			}

			bundle.putString(BroadcastConst.CLIENT_UUID_NAME, objmUuid);
			bundle.putInt(BroadcastConst.MSG_TYPE_NAME, obj.miMsgType);
			bundle.putParcelable(BroadcastConst.MSG_TYPE_NAME, obj);

			myintent.putExtras(bundle);
			context.sendBroadcast(myintent);
			return 0;
		}

		@Override
		public int cica_notify_Callback(int routerID, CICAMMessageBase obj) {
			// TODO Auto-generated method stub
			Intent myintent = new Intent(DTVConstant.DTV_CICAM_PROMPT_NOTIRY);
			Bundle bundle = new Bundle();
			Log.i(TAG, "cica_notify_Callback");

			if (obj == null) {
				Log.e(TAG, "cica_notify_Callback obj is null");
				return 0;
			}

			Log.e(TAG, "service cica_notify_Callback type = " + obj.miMsgType);

			bundle.putString("calientid", objmUuid);
			bundle.putInt(BroadcastConst.MSG_TYPE_NAME, obj.miMsgType);
			// bundle.putInt("Menu_Type", obj.miMsgID);
			bundle.putParcelable(BroadcastConst.MSG_INFO_NAME, obj);

			myintent.putExtras(bundle);
			context.sendBroadcast(myintent);
			return 0;
		}
	}

	public class avplayercallback_class implements avplayercallback {

		@Override
		public int player_TvosCallback(int routerID, int event) {
			// TODO Auto-generated method stub
			switch (event) {
				case DTVConstant.ConstPlayerTvosEvent.MUTE:
					if (soundM != null) {
						//						soundM
					}
					break;
				case DTVConstant.ConstPlayerTvosEvent.UNMUTE:
					if (soundM != null) {
						//						soundM
					}
					break;
				case DTVConstant.ConstPlayerTvosEvent.HIDE_VIDEO:
					if (tvplayer != null) {
						tvplayer.muteVideo();
					}
					break;
				case DTVConstant.ConstPlayerTvosEvent.SHOW_VIDEO:
					if (tvplayer != null) {
						tvplayer.unmuteVideo();
					}
					break;
				case DTVConstant.ConstPlayerTvosEvent.FREEZE_VIDEO:
					if (tvplayer != null) {
						tvplayer.freezeVideo();
					}
					break;
				case DTVConstant.ConstPlayerTvosEvent.UNFREEZE_VIDEO:
					if (tvplayer != null) {
						tvplayer.unFreezeVideo();
					}
					break;
				case DTVConstant.ConstPlayerTvosEvent.CLEAR_VIDDEO_BUFFER:
					break;
			}
			return 0;
		}

		@Override
		public int startServiceCallback(int routerID, StartControlInfo info) {
			// TODO Auto-generated method stub
			if (info != null) {
				Intent myintent = new Intent(DTVConstant.startControlConst.DTV_START_SERVICE_INFO);
				Bundle bundle = new Bundle();
				Log.i(TAG, "Start_Service_notify_Callback");

				bundle.putString("calientid", objmUuid);
				bundle.putParcelable(BroadcastConst.MSG_INFO_NAME, info);

				myintent.putExtras(bundle);
				context.sendBroadcast(myintent);
			}
			return 0;
		}

		@Override
		public int playerStatusCallbck(int routerID, PlayStatusInfo info) {
			// TODO Auto-generated method stub
			Log.i(TAG, "jackie>>player_StatusCallbck>>" + info.miPlayEvent + ", " + info.mstrPrompt);
			if (info != null) {
				Intent myintent = new Intent(DTVConstant.DTV_PLAYER_STATUS_CHANGE);
				Bundle bundle = new Bundle();
				Log.i(TAG, "playerStatusCallbck");

				bundle.putInt(DTVConstant.BroadcastConst.MSG_TYPE_NAME, 0);
				bundle.putParcelable(BroadcastConst.MSG_INFO_NAME, info);

				myintent.putExtras(bundle);
				context.sendBroadcast(myintent);
			}
			return 0;
		}

	}

	public class epgcallback_class implements epgcallback {

		//@Override
		public int epgdone_Callback(int routerID) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	public class nvodcallback_class implements nvodcallback {

		//@Override
		public int nvoddone_Callback(int routerID) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	//@Override
	public int getSourceType() throws RemoteException {
		// TODO Auto-generated method stub
		//		if(objiTunerID < 100){
		//			return DTVConstant.ConstDemodType.DVB_C;
		//		}
		//		else if(objiTunerID >=100 && objiTunerID <200){
		//			return DTVConstant.ConstDemodType.DVB_T;
		//		}
		//		else if(objiTunerID >=200 && objiTunerID <300){
		//			return DTVConstant.ConstDemodType.DVB_S;
		//		}
		//		else if(objiTunerID >= 300){
		//			return DTVConstant.ConstDemodType.DMB_TH;
		//		}
		//		else{
		//			return DTVConstant.ConstDemodType.DVB_C;
		//		}

		DTVSource obj_SRC = DTVServiceJNI.get_avplayer_instance().getSource(objRouterID);
		if (null == obj_SRC) {
			Log.e(TAG, "getSourceType>( null == obj_SRC )");
			return DTVConstant.ConstDemodType.DVB_C;
		}

		Log.i(TAG, "getSourceType=" + obj_SRC.miSourceType);
		return obj_SRC.miSourceType;
	}

	@Override
	public int getProductType() throws RemoteException {
		if (iProductType == 0) {
			return -1;
		}

		return iProductType;
	}

	@Override
	public DTVCardStatus getCardStatus() throws RemoteException {
		// TODO Auto-generated method stub
		Log.e(TAG, "dtvservice>>getCardStatus>>" + caci.getinstance().getCardStatus().miCardType + ", " + caci.getinstance().getCardStatus().miCardStatus);
		return caci.getinstance().getCardStatus();
	}

	@Override
	public int ReleaseForBack() throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "Release()[enter] objRouterID = " + objRouterID);
		int result;
		result = rmManager.RmRelease(objiTunerID, Resource.RESOURCE_TUNER, objmUuid);
		result = rmManager.RmRelease(objeLayerType, Resource.RESOURCE_VIDEODECODER, objmUuid);
		result = rmManager.RmRelease(objiIndex, Resource.RESOURCE_VIDEOWINDDOW, objmUuid);

		if (result == 0) {
			//锟酵凤拷锟斤拷源锟缴癸拷锟斤拷锟斤拷锟矫讹拷应JNI锟接匡拷锟斤拷实锟绞碉拷锟斤拷源锟斤拷锟斤拷锟斤拷锟斤拷
			if (objRouterID != -1) {
				rmManager.DestroyRouteIDForBack(objiTunerID, objeLayerType, objiIndex, this);
				//DTVServiceJNI.get_system_instance().destroyRouter(objRouterID);
				//DTVServiceJNI.get_system_instance().dtvsystemStop();
			}
			Log.i(TAG, "objRouterStatus,3,destrod");
			objRouterStatus = ROUTER_DESTROIED;
		}

		return 0;
	}

	@Override
	public int getPlayerStatus() throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "getPlayerStatus=" + objRouterStatus);
		switch (objRouterStatus) {
			case ROUTER_VALID:
				return 0;
			case ROUTER_INIT:
				return 1;
			case ROUTER_USED_BY_OTHER:
				return 2;
			case ROUTER_DESTROIED:
				return 3;
		}
		return 0;
	}

	@Override
	public EPGEvent[] getSchelueEventWithStamp(int iChannelIndex, DTVDTTime time) throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_epg_instance().getSchelueEventWithStamp(objRouterID, iChannelIndex, time);
	}

	public int setSource(int iSourceID) throws RemoteException {
		// TODO Auto-generated method stub
		int miSourceID = 0;
		DTVSource mDTVSource = null;

		mDTVSource = DTVServiceJNI.get_avplayer_instance().getSource(objRouterID);
		miSourceID = mDTVSource.miSourceID;

		if (miSourceID != iSourceID) {
			Log.i(TAG, "setSource --->miSourceID " + mDTVSource.miSourceID + "--->iSourceID:" + iSourceID);
			lastProgramID = currentProgramID;
			currentProgramID = -1;
			isPlaying = 0;
		}

		return DTVServiceJNI.get_avplayer_instance().setSource(objRouterID, iSourceID);
	}

	@Override
	public DTVSource getSource() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_avplayer_instance().getSource(objRouterID);
	}

	@Override
	public int SmartSkip(int iType) throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_avplayer_instance().SmartSkip(objRouterID, iType);
	}

	@Override
	public int SmartSkipStop() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_avplayer_instance().SmartSkipStop(objRouterID);
	}

	public int SetDtvBusyState(int isBusyState) throws RemoteException {
		Log.i(TAG, "SetDtvBusyState-" + isBusyState);
		this.isDTV_Busying = isBusyState;
		return (isDTV_Busying);
	}

	@Override
	public void setHotelMode(boolean bHotelMode) throws RemoteException {
		// TODO Auto-generated method stub

		Log.i(TAG, "setHotelMode-->>>>>>" + bHotelMode);
		hotelM.setHotelMode(bHotelMode);
	}

	@Override
	public void setHotelMaxVolume(int iMaxVolume) throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "setHotelMaxVolume-->>>>>>" + iMaxVolume);
		hotelM.setHotelMaxVolume(iMaxVolume);
	}

	@Override
	public boolean getHotelMode() throws RemoteException {
		// TODO Auto-generated method stub
		return (hotelM.getHotelMode());//false;
	}

	@Override
	public int getHotelMaxVolume() throws RemoteException {
		// TODO Auto-generated method stub
		return (hotelM.getHotelMaxVolume());
	}

	@Override
	public int getHotelPowerOnMode() throws RemoteException {
		// TODO Auto-generated method stub
		return (hotelM.getHotelPowerOnMode());
	}

	@Override
	public void setHotelPowerOnMode(int iPowerOnMode) throws RemoteException {
		// TODO Auto-generated method stub
		hotelM.setHotelPowerOnMode(iPowerOnMode);
	}

	@Override
	public int getHotelPowerOnVolume() throws RemoteException {
		// TODO Auto-generated method stub
		return (hotelM.getHotelPowerOnVolume());
	}

	@Override
	public void setHotelPowerOnVolume(int iPowerOnVolume) throws RemoteException {
		// TODO Auto-generated method stub
		hotelM.setHotelPowerOnVolume(iPowerOnVolume);
	}

	@Override
	public int getHotelPowerOnChannel() throws RemoteException {
		// TODO Auto-generated method stub
		return (hotelM.getHotelPowerOnChannel());
	}

	@Override
	public void setHotelPowerOnChannel(int iPowerOnChannel) throws RemoteException {
		// TODO Auto-generated method stub
		hotelM.setHotelPowerOnChannel(iPowerOnChannel);
	}

	@Override
	public boolean getHotelLocalKeyLockFlag() throws RemoteException {
		// TODO Auto-generated method stub
		return (hotelM.getHotelLocalKeyLockFlag());
	}

	@Override
	public void setHotelLocalKeyLockFlag(boolean bLocalKeyLockFlag) throws RemoteException {
		// TODO Auto-generated method stub
		hotelM.setHotelLocalKeyLockFlag(bLocalKeyLockFlag);
	}

	@Override
	public boolean getHotelTuneLockFlag() throws RemoteException {
		// TODO Auto-generated method stub
		return (hotelM.getHotelTuneLockFlag());
	}

	@Override
	public void setHotelTuneLockFlag(boolean bTuneLockFlag) throws RemoteException {
		// TODO Auto-generated method stub
		hotelM.setHotelTuneLockFlag(bTuneLockFlag);
	}

	@Override
	public int getHotelMusicMode() throws RemoteException {
		// TODO Auto-generated method stub
		return (hotelM.getHotelMusicMode());
	}

	@Override
	public void setHotelMusicMode(int iMusicMode) throws RemoteException {
		// TODO Auto-generated method stub
		hotelM.setHotelMusicMode(iMusicMode);
	}

	@Override
	public int getHotelPowerOnSource() throws RemoteException {
		// TODO Auto-generated method stub
		return (hotelM.getHotelPowerOnSource());
	}

	@Override
	public void setHotelPowerOnSource(int iPowerOnSource) throws RemoteException {
		// TODO Auto-generated method stub
		hotelM.setHotelPowerOnSource(iPowerOnSource);
	}

	@Override
	public boolean getHotelAutoPresetFlag() throws RemoteException {
		// TODO Auto-generated method stub
		return (hotelM.getHotelAutoPresetFlag());
	}

	@Override
	public void setHotelAutoPresetFlag(boolean bAutoPresetFlag) throws RemoteException {
		// TODO Auto-generated method stub
		hotelM.setHotelAutoPresetFlag(bAutoPresetFlag);
	}

	@Override
	public void SetDtvUIActivityState(int iState) throws RemoteException {
		// TODO Auto-generated method stub
		//return 0;
		DtvUIActivityState = iState;
	}
}