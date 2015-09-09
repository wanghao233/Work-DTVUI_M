/**
 * @filename ��DTV3rdManager.java
 * 	���3���ṩ�ӿڵ�service�������
 * @author:
 * @date: 
 * @version 0.1
 * @history:
 *  2012-12-25 Ϊ�����������������Ӧ�û���ֶ�������������
 * 1.2 ����ReleaseForBack�ӿ�
 * 1.3 ����SystemReset�ӿ�
 * 1.3 ����releaseForBack��SystemReset�ӿ�
 * 1.4 ���ӻ�ȡ��Դ�к����롢�ͷ�DTV������Դ�Ľӿ�
 * 1.5 ���ӻ�ȡ��Ŀ��ϸ��Ϣ�ӿ�
 * */

package com.changhong.tvos.dtv.for3rd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.changhong.tvos.dtv.provider.BaseProgram;
import com.changhong.tvos.dtv.provider.BaseProgramManager;
import com.changhong.tvos.dtv.service.IChannelManager;
import com.changhong.tvos.dtv.service.IDTVPlayer;
import com.changhong.tvos.dtv.service.IDTVService;
import com.changhong.tvos.dtv.service.IDTVSettings;
import com.changhong.tvos.dtv.service.IPVR;
import com.changhong.tvos.dtv.service.ITimerShedule;
import com.changhong.tvos.dtv.vo.AudioTrack;
import com.changhong.tvos.dtv.vo.DTVCardStatus;
import com.changhong.tvos.dtv.vo.DTVChannelBaseInfo;
import com.changhong.tvos.dtv.vo.DTVChannelDetailInfo;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.dtv.vo.DTVDTTime;
import com.changhong.tvos.dtv.vo.DTVSource;
import com.changhong.tvos.dtv.vo.DTVTunerStatus;
import com.changhong.tvos.dtv.vo.EPGEvent;
import com.changhong.tvos.dtv.vo.Operator;
import com.changhong.tvos.dtv.vo.TimerInfo;
import com.changhong.tvos.dtv.vo.VersionInfo;

public class DTV3rdManager extends IDTV3rdInterface.Stub {
	public final String TAG = "DTV3rdservice ";

	public final int API_VERSION_MAIN = 1;
	public final int API_VERSION_SUB = 5;

	public String mUuid = null;
	public int miPid = 0;
	public int miGuid = 0;
	public IPlayerEventListener mPlayerEventLister = null;
	public IBinder mheartbeatCheck;
	public IBinder mDTVPlayerBinder = null;
	public IDTVPlayer mPlayer = null;
	public IDTVService mIDtvServer = null;
	public IDTVSettings mIDTVSettingsServer = null;
	public IChannelManager mIChannelManagerServer = null;
	public ITimerShedule mITimerServer = null;
	public IPVR mPVR = null;
	public boolean mIsCurUser = false;
	private static final String CONFIG_FILE_PATH = "/data/dtv/dtv_properties";
	private Properties propertie = new Properties();
	private BaseProgramManager baseProgramManager = null;

	public int CheckServiceBinder() {
		if ((mIDTVSettingsServer != null) && (mIChannelManagerServer != null) && (mPlayer != null) && (mITimerServer != null) && (mPVR != null)) {
			return 0;
		}

		if (mIDtvServer == null) {
			IBinder binder;

			binder = ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME);
			if (binder != null) {
				mIDtvServer = IDTVService.Stub.asInterface(binder);
			} else {
				Log.i(TAG, "DTV3rdManager check service binder failed!");
				return -1;
			}
		}

		if (mIDTVSettingsServer == null) {
			try {
				IBinder binder = mIDtvServer.GetDTVSettings();
				if (binder != null) {
					mIDTVSettingsServer = IDTVSettings.Stub.asInterface(binder);
				}
			} catch (RemoteException exception) {
				Log.i(TAG, "DTV3rdManager GetDTVSettings binder failed!");
			}
		}

		if (mPVR == null) {
			try {
				IBinder binder = mIDtvServer.GetPVR();
				if (binder != null) {
					mPVR = IPVR.Stub.asInterface(binder);
					Log.i(TAG, "[mPVR]" + mPVR);
				}
			} catch (RemoteException exception) {
				Log.i(TAG, "DTV3rdManager IPVR binder failed!");
			}
		}

		if (mITimerServer == null) {
			if (mIDtvServer != null) {
				try {
					IBinder serviceBinder = mIDtvServer.getTimerShudle();
					if (serviceBinder != null) {
						mITimerServer = ITimerShedule.Stub.asInterface(serviceBinder);
					}
				} catch (RemoteException exception) {
					Log.i(TAG, "DTV3rdManager getTimerShudle binder failed!");
					;
				}
			} else {
				Log.i(TAG, "DTV3rdManager check ITimerServer binder failed!");
			}
		}

		if (mIChannelManagerServer == null) {
			try {
				IBinder binder = mIDtvServer.CreateChannelManager(null);
				if (binder != null) {
					mIChannelManagerServer = IChannelManager.Stub.asInterface(binder);

					mIChannelManagerServer.setChannelSource(DTVConstant.ConstDemodType.DVB_C, 0);
				}
			} catch (RemoteException exception) {
				Log.i(TAG, "DTV3rdManager CreateChannelManager binder failed!");
			}
		}

		//2015-1-19 YangLiu
		if (baseProgramManager == null) {
			baseProgramManager = BaseProgramManager.getInstance(null);
		}

		if (mPlayer == null) {
			try {
				mDTVPlayerBinder = mIDtvServer.CreateDTVPlayer(mUuid.toString(), 0, DTVConstant.ConstVideoLayerType.VIDEO_LAYER, 0, 0);
				if (mDTVPlayerBinder != null) {
					mPlayer = IDTVPlayer.Stub.asInterface(mDTVPlayerBinder);
				}
			} catch (RemoteException exception) {
				Log.i(TAG, "DTV3rdManager CreateDTVPlayer binder failed!");
			}
		}

		if ((mIDTVSettingsServer == null) || (mIChannelManagerServer == null) || (mPlayer == null)) {
			Log.i(TAG, "DTV3rdManager check bind failed! seting" + mIDTVSettingsServer + "channel:" + mIChannelManagerServer + "player:" + mPlayer);
			return -1;
		} else {
			return 0;
		}
	}

	public DTV3rdManager(String uuid, int pid, int guid, IBinder heartbeatCheck, IDTVService IDtvServer, IDTVSettings IDTVSettingsServer, IChannelManager IChannelManagerServer,
			ITimerShedule ITimerServer) {
		Log.i(TAG, "DTV3rdManager start");
		Log.i(TAG, "heartbeatCheck:" + heartbeatCheck);
		Log.i(TAG, "IDtvServer:" + heartbeatCheck);
		Log.i(TAG, "IDTVSettingsServer:" + heartbeatCheck);
		Log.i(TAG, "IChannelManagerServer:" + heartbeatCheck);
		Log.i(TAG, "ITimerShedule:" + ITimerServer);

		mUuid = uuid;
		miPid = pid;
		miGuid = guid;
		mheartbeatCheck = heartbeatCheck;
		Log.i(TAG, "asInterface start");
		mPlayerEventLister = IPlayerEventListener.Stub.asInterface(heartbeatCheck);
		Log.i(TAG, "asInterface end");
		mIDtvServer = IDtvServer;
		mIDTVSettingsServer = IDTVSettingsServer;
		mIChannelManagerServer = IChannelManagerServer;
		mITimerServer = ITimerServer;
		Log.i(TAG, "DTV3rdManager CheckServiceBinder");
		CheckServiceBinder();
		Log.i(TAG, "DTV3rdManager end");
	}

	private InterChannelInfo ConvertChannelInfo(DTVChannelDetailInfo channelInfo, int channelNumber) {
		if ((channelInfo.miServiceType != DTVConstant.ConstServiceType.SERVICE_TYPE_TV) && (channelInfo.miServiceType != DTVConstant.ConstServiceType.SERVICE_TYPE_RADIO)) {
			Log.i(TAG, "service type is :" + channelInfo.miServiceType);
			//return null;
		}

		//	Log.i(TAG, "ConvertChannelInfo channelno:" + channelNumber + "src channelno:" + channelInfo.miChannelnumber + " name:" + channelInfo.mstrServiceName);

		InterChannelInfo interInfo = new InterChannelInfo();
		interInfo.miChannelIndex = channelInfo.miChannelIndex;
		interInfo.miChannelnumber = channelNumber;
		interInfo.mstrServiceName = channelInfo.mstrServiceName;

		if (channelInfo.miServiceType == DTVConstant.ConstServiceType.SERVICE_TYPE_TV) {
			interInfo.miServiceType = For3rdConst.ConstantServiceType.DTV_PROGRAM_TYPE_TV;
		} else {
			interInfo.miServiceType = For3rdConst.ConstantServiceType.DTV_PROGRAM_TYPE_RADIO;
		}

		interInfo.miVideoType = channelInfo.miVideoType;
		interInfo.miAudioType = channelInfo.miAudioType;
		interInfo.mbScrambled = channelInfo.mbScrambled;
		interInfo.mstrRating = channelInfo.msRating;
		interInfo.miBanlenceVolume = channelInfo.miBanlenceVolume;
		interInfo.mbLock = channelInfo.mbLock;
		interInfo.mbSkip = channelInfo.mbSkip;
		interInfo.mbFav = channelInfo.mbFav;
		interInfo.mstrURI = null;

		return interInfo;
	}

	private InterChDetailInfo ConvertDetailChannelInfo(DTVChannelDetailInfo channelInfo, int channelNumber) {
		if ((channelInfo.miServiceType != DTVConstant.ConstServiceType.SERVICE_TYPE_TV) && (channelInfo.miServiceType != DTVConstant.ConstServiceType.SERVICE_TYPE_RADIO)) {
			Log.i(TAG, "service type is :" + channelInfo.miServiceType);
			return null;
		}

		InterChDetailInfo interInfo = new InterChDetailInfo();
		interInfo.miChannelIndex = channelInfo.miChannelIndex;
		interInfo.miChannelnumber = channelNumber;
		interInfo.mstrServiceName = channelInfo.mstrServiceName;

		if (channelInfo.miServiceType == DTVConstant.ConstServiceType.SERVICE_TYPE_TV) {
			interInfo.miServiceType = For3rdConst.ConstantServiceType.DTV_PROGRAM_TYPE_TV;
		} else {
			interInfo.miServiceType = For3rdConst.ConstantServiceType.DTV_PROGRAM_TYPE_RADIO;
		}

		interInfo.miServiceID = channelInfo.miServiceID;
		interInfo.miTSID = channelInfo.miTSID;
		interInfo.miOrgNetID = channelInfo.miOrgNetID;

		interInfo.miCarrierIndex = channelInfo.miCarrierIndex;
		interInfo.miSignalLevel = channelInfo.miSignalLevel;
		interInfo.miSignalQuality = channelInfo.miSignalQuality;

		interInfo.miVideoType = channelInfo.miVideoType;
		interInfo.miAudioType = channelInfo.miAudioType;

		interInfo.mAudioTrackInfo.miCurrSelect = channelInfo.mAudioTrackInfo.miCurrSelect;
		interInfo.mAudioTrackInfo.miTrackNumb = channelInfo.mAudioTrackInfo.mAudioLanguagelist.length;
		interInfo.mAudioTrackInfo.mstrAudioLanguage = new String[interInfo.mAudioTrackInfo.miTrackNumb];
		interInfo.mAudioTrackInfo.mstrAudioLanguage = channelInfo.mAudioTrackInfo.mAudioLanguagelist;

		interInfo.miAudioMode = channelInfo.miSoundMode;
		interInfo.mbScrambled = channelInfo.mbScrambled;
		interInfo.msRating = channelInfo.msRating;
		interInfo.miBanlenceVolume = channelInfo.miBanlenceVolume;
		interInfo.mbLock = channelInfo.mbLock;
		interInfo.mbSkip = channelInfo.mbSkip;
		interInfo.mbFav = channelInfo.mbFav;
		interInfo.mstrURI = null;

		return interInfo;
	}

	private InterDTVChannelBaseInfo ConvertChannelBaseInfo(DTVChannelBaseInfo channelInfo, int channelNumber) {
		if ((channelInfo.miServiceType != DTVConstant.ConstServiceType.SERVICE_TYPE_TV) && (channelInfo.miServiceType != DTVConstant.ConstServiceType.SERVICE_TYPE_RADIO)) {
			Log.i(TAG, "ConvertChannelBaseInfo service type is :" + channelInfo.miServiceType);
			return null;
		}

		InterDTVChannelBaseInfo interInfo = new InterDTVChannelBaseInfo();

		interInfo.miChannelIndex = channelInfo.miChannelIndex;
		interInfo.miChannelnumber = channelInfo.miChannelnumber;
		interInfo.mstrServiceName = channelInfo.mstrServiceName;

		if (channelInfo.miServiceType == DTVConstant.ConstServiceType.SERVICE_TYPE_TV) {
			interInfo.miServiceType = For3rdConst.ConstantServiceType.DTV_PROGRAM_TYPE_TV;
		} else {
			interInfo.miServiceType = For3rdConst.ConstantServiceType.DTV_PROGRAM_TYPE_RADIO;
		}

		interInfo.miServiceID = channelInfo.miServiceID;
		interInfo.miTSID = channelInfo.miTSID;
		interInfo.miOrgNetID = channelInfo.miOrgNetID;

		interInfo.mbScrambled = channelInfo.mbScrambled;
		interInfo.mstrCurAudioTrack = channelInfo.mstrCurAudioTrack;
		interInfo.miSoundMode = channelInfo.miSoundMode;
		interInfo.miBanlenceVolume = channelInfo.miBanlenceVolume;
		interInfo.msRating = channelInfo.msRating;
		interInfo.mbLock = channelInfo.mbLock;
		interInfo.mbSkip = channelInfo.mbSkip;
		interInfo.mbFav = channelInfo.mbFav;
		interInfo.miDemodType = channelInfo.miDemodType;

		interInfo.msLogo = channelInfo.msLogo;

		return interInfo;
	}

	private InterEPGEvent ConvertEPGEvent(EPGEvent event) {
		InterEPGEvent interEvent = new InterEPGEvent();
		interEvent.miEventID = event.miEventID;
		interEvent.mStartDate = new InterUTCDate(event.startDate.miYear, event.startDate.miMonth, event.startDate.miDay, event.startDate.miWeekDay);

		interEvent.mStartTime = new InterUTCTime(event.startTime.miHour, event.startTime.miMinute, event.startTime.miSecond);
		interEvent.mDuringTime = new InterUTCTime(event.duringTime.miHour, event.duringTime.miMinute, event.duringTime.miSecond);
		interEvent.mstrEventName = event.eventName;
		interEvent.mstrShortText = event.shortText;
		return interEvent;

	}

	@Override
	public List<InterChannelInfo> getChannelList() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.getChannelListByTpye service binder failed");
			return null;
		}
		//long time=System.currentTimeMillis();

		try {
			//Log.d(TAG, "DTV3rdManager-->enter jni_getChanneDetailInfoListByTpye ");

			DTVChannelDetailInfo[] tmpList = mIChannelManagerServer.getChanneDetailInfoListByTpye(DTVConstant.ConstServiceType.SERVICE_TYPE_ALL);
			if ((tmpList == null) || (tmpList.length <= 0)) {
				Log.e(TAG, "DTV3rdManager.getChanneDetailInfoListByTpye NULL!");

				return null;
			}

			//Log.e(TAG, "DTV3rdManager.getChannelList size:"+tmpList.length);
			InterChannelInfo interInfo;
			List<InterChannelInfo> channelList = new ArrayList<InterChannelInfo>();
			List<InterChannelInfo> listTemp = new ArrayList<InterChannelInfo>();

			for (int i = 0; i < tmpList.length; i++) {
				//Log.e(TAG, "DTV3rdManager.getChannelList servicetype:"+tmpList[i].miServiceType);

				interInfo = ConvertChannelInfo(tmpList[i], tmpList[i].miChannelnumber);
				if (interInfo == null) {
					//Log.e(TAG, "DTV3rdManager.getChannelList NULL");
					//return null;
				} else {
					Log.i(TAG, "interInfo.mbSkip" + interInfo.mstrServiceName);
					Log.i(TAG, "interInfo.mbSkip" + interInfo.mbSkip);

					if (!interInfo.mbSkip) {
						listTemp.add(interInfo);
					}
					channelList.add(interInfo);
				}
			}

			//Log.e(TAG, "DTV3rdManager.getChannelList channelList size:"+channelList.size());
			//Log.d(TAG, "DTV3rdManager-->TotalTimer=="+(System.currentTimeMillis()-time));
			return listTemp;
			//	return channelList;
		} catch (RemoteException exception) {
			//no thing to do
			Log.e(TAG, "DTV3rdManager.getChannelList exception");
		}

		return null;

		/*
		if(CheckServiceBinder() != 0){
			Log.i(TAG, "DTV3rdManager.getChannelList service binder failed");
			return null;
		}
		
		Log.i(TAG, "Enter DTV3rdManager.getChannelList");
		
		List<InterChannelInfo> channelList = new ArrayList<InterChannelInfo>();
		
		if(mIChannelManagerServer != null){
			try{
				DTVChannelBaseInfo[] channelArray;
				
				channelArray = mIChannelManagerServer.getChannelListByTpye(DTVConstant.ConstServiceType.SERVICE_TYPE_ALL);
				if((channelArray == null) || (channelArray.length == 0)){
					Log.i(TAG, "DTV3rdManager.getChannelList get null");
					return null;
				}
				
				DTVChannelDetailInfo detailInfo;
				InterChannelInfo interInfo;
				for(int i = 0; i < channelArray.length; i++){
					try{
						detailInfo = mIChannelManagerServer.getChanneDetailInfo(channelArray[i].miChannelIndex);
						if(detailInfo != null){
							interInfo = ConvertChannelInfo(detailInfo, channelArray[i].miChannelnumber);
							if(interInfo != null){
								channelList.add(interInfo);
							}
						}
					}
					catch (RemoteException exception){
						//nothing to do!
						Log.i(TAG, "DTV3rdManager.getChannelList get list failed-1");
					}
				}
				
				Log.i(TAG, "Exit DTV3rdManager.getChannelList");
				
				return channelList;
			}
			catch (RemoteException exception){
				Log.i(TAG, "DTV3rdManager.getChannelList get list failed");
				//nothing to do!
			}
		}
		return null;
		*/
	}

	@Override
	public int getChannelCount() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckServiceBinder() != 0) {
			return -1;
		}

		if (mIChannelManagerServer != null) {
			try {
				int iTVCount;
				int iRadioCount;

				iTVCount = mIChannelManagerServer.getChannelCountByType(DTVConstant.ConstServiceType.SERVICE_TYPE_TV);
				iRadioCount = mIChannelManagerServer.getChannelCountByType(DTVConstant.ConstServiceType.SERVICE_TYPE_RADIO);
				return iTVCount + iRadioCount;
			} catch (RemoteException exception) {
				return 0;
			}
		} else {
			return 0;
		}

	}

	@Override
	public List<InterChannelInfo> getChannelListByType(int itype) throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckServiceBinder() != 0) {
			return null;
		}

		int iServiceType;

		if (itype == For3rdConst.ConstantServiceType.DTV_PROGRAM_TYPE_TV) {
			iServiceType = DTVConstant.ConstServiceType.SERVICE_TYPE_TV;
		} else if (itype == For3rdConst.ConstantServiceType.DTV_PROGRAM_TYPE_RADIO) {
			iServiceType = DTVConstant.ConstServiceType.SERVICE_TYPE_RADIO;
		} else {
			return null;
		}

		List<InterChannelInfo> channelList = new ArrayList<InterChannelInfo>();

		List<InterChannelInfo> listTemp = new ArrayList<InterChannelInfo>();

		if (mIChannelManagerServer != null) {
			try {
				DTVChannelBaseInfo[] channelArray;

				channelArray = mIChannelManagerServer.getChannelListByTpye(iServiceType);
				if ((channelArray == null) || (channelArray.length == 0)) {
					return null;
				}

				DTVChannelDetailInfo detailInfo;
				InterChannelInfo interInfo;
				for (int i = 0; i < channelArray.length; i++) {
					if ((channelArray[i].miServiceType == DTVConstant.ConstServiceType.SERVICE_TYPE_TV) || (channelArray[i].miServiceType == DTVConstant.ConstServiceType.SERVICE_TYPE_RADIO)) {
						try {
							detailInfo = mIChannelManagerServer.getChanneDetailInfo(channelArray[i].miChannelIndex);
							if (detailInfo != null) {
								interInfo = ConvertChannelInfo(detailInfo, channelArray[i].miChannelnumber);
								if (interInfo == null) {
									//Log.e(TAG, "DTV3rdManager.getChannelList NULL");
									//return null;
								} else {

									//Log.i(TAG,"interInfo.mbSkip"+interInfo.mstrServiceName);
									//Log.i(TAG,"interInfo.mbSkip"+interInfo.mbSkip);

									if (!interInfo.mbSkip) {
										listTemp.add(interInfo);
									}
									////////////////////////////////////////////////////////
									channelList.add(interInfo);
								}
							}
						} catch (RemoteException exception) {
							Log.i(TAG, "DTV3rdManager getChanneDetailInfo failed!");
						}
					}
				}
				return listTemp;
				//return channelList;
			} catch (RemoteException exception) {
				//nothing to do!
			}
		}
		return null;
	}

	@Override
	public int prepare() throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "prepare entry");
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "prepare failed");
			return 1;
		}
		//		if(mPlayer == null)
		//		 {
		//			try
		//			{
		//				mDTVPlayerBinder = mIDtvServer.CreateDTVPlayer(mUuid.toString(), 0, DTVConstant.ConstVideoLayerType.VIDEO_LAYER, 0, 0);
		//				if(mDTVPlayerBinder != null)
		//				{
		//					mPlayer = IDTVPlayer.Stub.asInterface(mDTVPlayerBinder);
		//				}
		//			}
		//			catch (RemoteException exception)
		//			{
		//				//do nothing
		//			}
		//		 }

		if (mPlayer != null) {
			try {
				Log.i(TAG, "dtv service prepare entry");
				return mPlayer.prepare();
			} catch (RemoteException exception) {
				Log.i(TAG, "prepare exception");
			}
		}

		Log.i(TAG, "3rd prepare failed!!!");
		return 1;
	}

	@Override
	public int Release() throws RemoteException {
		// TODO Auto-generated method stub

		if (mPlayer == null) {
			return 0;
		}

		Log.i(TAG, "DTV3rdManager Release:");

		try {
			mPlayer.Release();
			mIDtvServer.DestroyDTVPlayer(mDTVPlayerBinder);
			mDTVPlayerBinder = null;
			mPlayer = null;
			return 0;
		} catch (RemoteException exception) {
			Log.i(TAG, "Release exception");

			return 1;
		}
	}

	@Override
	public int play(int channelIndex) throws RemoteException {
		Log.i(TAG, "[play]" + channelIndex);
		if (mPlayer == null) {
			return 1;
		}

		try {
			int iCurPlayingChannel = mPlayer.getPlayingProgramID();
			if (iCurPlayingChannel == channelIndex) {
				if (mPlayer.isPlaying() == 1) {
					Log.i(TAG, "�����Ŀ���ڲ���!!");
					return 0;
				}
			}
			return mPlayer.play(channelIndex);
		} catch (RemoteException exception) {
			Log.i(TAG, "play exception");

			return 1;
		}
	}

	@Override
	public int stop() throws RemoteException {
		// TODO Auto-generated method stub
		if (mPlayer == null) {
			return 1;
		}

		try {
			return mPlayer.playStop();
		} catch (RemoteException exception) {
			return 1;
		}
	}

	@Override
	public int isPlaying() throws RemoteException {
		// TODO Auto-generated method stub
		if (mPlayer == null) {
			return -1;
		}

		try {
			return mPlayer.isPlaying();
		} catch (RemoteException exception) {
			return -1;
		}
	}

	@Override
	public InterTunerStatus getTunerStatus() throws RemoteException {
		// TODO Auto-generated method stub
		DTVTunerStatus tunerStatus;
		if (mPlayer == null) {
			return null;
		}

		try {
			tunerStatus = mPlayer.getTunerStatus();
			if (tunerStatus != null) {
				InterTunerStatus status = new InterTunerStatus();
				status.mbLock = tunerStatus.mbLock;
				status.miSignalLevel = tunerStatus.miSignalLevel;
				status.miSignalQuality = tunerStatus.miSignalQuality;
				return status;
			}
		} catch (RemoteException exception) {
			//no thing to do
		}

		return null;
	}

	@Override
	public InterAudioTrack getAudioTrack() throws RemoteException {
		// TODO Auto-generated method stub
		AudioTrack track;
		if (mPlayer == null) {
			return null;
		}

		try {
			track = mPlayer.getChannelAudioTrack();
			if (track != null) {
				InterAudioTrack audioTrack = new InterAudioTrack();
				audioTrack.miCurrSelect = track.miCurrSelect;
				audioTrack.miTrackNumb = track.mAudioLanguagelist.length;
				audioTrack.mstrAudioLanguage = track.mAudioLanguagelist;

				return audioTrack;
			}
		} catch (RemoteException exception) {
			//no thing to do
		}

		return null;
	}

	@Override
	public int setAudioTrack(int audioTrack) throws RemoteException {
		// TODO Auto-generated method stub
		if (mPlayer == null) {
			return 1;
		}

		try {
			return mPlayer.setChannelAudioTrack(audioTrack);
		} catch (RemoteException exception) {
			//no thing to do
		}

		return 1;
	}

	@Override
	public List<InterEPGEvent> getEPGPFEvent(int channelIndex) throws RemoteException {
		// TODO Auto-generated method stub
		if (mPlayer == null) {
			return null;
		}

		try {
			EPGEvent[] epgevent = mPlayer.getPFEvent(channelIndex);
			if (epgevent != null) {
				List<InterEPGEvent> epgEventList = new ArrayList<InterEPGEvent>();
				for (int i = 0; i < epgevent.length; i++) {
					InterEPGEvent eventTmp = ConvertEPGEvent(epgevent[i]);
					epgEventList.add(eventTmp);
				}

				return epgEventList;
			}
			Log.i(TAG, "DTV3rdManager.getEPGPFEvent null:");
		} catch (RemoteException exception) {
			//no thing to do
		}

		return null;
	}

	@Override
	public List<InterEPGEvent> getEPGSchelueEvent(int channelIndex) throws RemoteException {
		// TODO Auto-generated method stub
		if (mPlayer == null) {
			return null;
		}

		try {
			EPGEvent[] epgevent = mPlayer.getSchelueEvent(channelIndex);
			if (epgevent != null) {
				List<InterEPGEvent> epgEventList = new ArrayList<InterEPGEvent>();
				for (int i = 0; i < epgevent.length; i++) {
					InterEPGEvent eventTmp = ConvertEPGEvent(epgevent[i]);
					epgEventList.add(eventTmp);
				}

				return epgEventList;
			}
		} catch (RemoteException exception) {
			//no thing to do
		}

		return null;
	}

	@Override
	public String getEPGPFEventExtendInfo(int channelIndex, int eventID) throws RemoteException {
		// TODO Auto-generated method stub
		if (mPlayer == null) {
			return null;
		}

		try {
			return mPlayer.getPFEventExtendInfo(channelIndex, eventID);
		} catch (RemoteException exception) {
			//no thing to do
		}

		return null;
	}

	@Override
	public String getEPGSchelueEventExtendInfo(int channelIndex, int eventID) throws RemoteException {
		// TODO Auto-generated method stub
		if (mPlayer == null) {
			return null;
		}

		try {
			return mPlayer.getSchelueEventExtendInfo(channelIndex, eventID);
		} catch (RemoteException exception) {
			//no thing to do
		}

		return null;
	}

	@Override
	public boolean isDTVBusy() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckServiceBinder() != 0) {
			return false;
		}

		if (mIDtvServer == null) {
			return false;
		}

		try {
			int iResult = mIDtvServer.isCanBreakdown();
			if (iResult == 1) {
				return false;
			} else {
				return true;
			}
		} catch (RemoteException exception) {
			//no thing to do
		}

		return false;
	}

	@Override
	public int getDTVTime() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckServiceBinder() != 0) {
			return 0;
		}

		if (mIDtvServer == null) {
			return 0;
		}

		try {
			DTVDTTime time = mIDtvServer.getUTCTime();
			if (time == null) {
				Log.i("3rd service", "getUTCTime null");
				return 0;
			}
			if ((time.mstruDate.miYear <= 1970) || (time.mstruDate.miYear <= 1970)) {
				return 0;
			} else {
				int mjd1970 = 40587;
				int i_Temp = ((time.mstruDate.miMonth == 1) || (time.mstruDate.miMonth == 2)) ? 1 : 0;

				int i_Mjd = (14956 + time.mstruDate.miDay + ((((time.mstruDate.miYear - 1900) - i_Temp) * 36525) / 100) + (((time.mstruDate.miMonth + 1 + i_Temp * 12) * 306001) / 10000));

				int i_sec = (((i_Mjd - mjd1970) * 24 + time.mstruTime.miHour) * 60 + time.mstruTime.miMinute) * 60 + time.mstruTime.miSecond;

				//����GMTʱ��
				i_sec -= (TimeZone.getDefault().getRawOffset() / 1000);
				return i_sec;
			}
		} catch (RemoteException exception) {
			//no thing to do
		}

		return 0;
	}

	@Override
	public InterVersionInfo getDTVSwVersion() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckServiceBinder() != 0) {
			return null;
		}

		try {
			VersionInfo version;
			version = mIDTVSettingsServer.getVersion();
			if (version == null) {
				Log.e("DTV3rdMananger", "getDTVSwVersion is failed!");
				return null;
			}
			InterVersionInfo dtvVersion = new InterVersionInfo();
			dtvVersion.miHardwareVersion = version.miHardwareVersion;
			dtvVersion.miOpVersion = version.miOpVersion;
			dtvVersion.miMainVersion = version.miMainVersion;
			dtvVersion.miSubVersion = version.miAPISubVersion;
			dtvVersion.miAPIMainVersion = version.miAPIMainVersion;
			dtvVersion.miAPISubVersion = version.miAPISubVersion;
			dtvVersion.mstrReleaseDateTime = version.mstrReleaseDateTime;
			dtvVersion.mi3rdAPIMainVersion = API_VERSION_MAIN;
			dtvVersion.mi3rdAPISubVersion = API_VERSION_SUB;

			return dtvVersion;
		} catch (RemoteException exception) {
			//no thing to do
		}

		return null;
	}

	@Override
	public int getDTVLastChannel() throws RemoteException {
		// TODO Auto-generated method stub
		if (mPlayer == null) {
			return -1;
		}

		try {
			return mPlayer.getLastProgramID();
		} catch (RemoteException exception) {
			//no thing to do
		}

		return -1;
	}

	@Override
	public int getDTVCurrentChannel() throws RemoteException {
		// TODO Auto-generated method stub
		if (mPlayer == null) {
			Log.e(TAG, "DTV3rdManager.getDTVCurrentChannel mPlayer == null");
			return -1;
		}

		try {
			int iChannelNumber = -1;

			iChannelNumber = mPlayer.getPlayingProgramID();

			Log.i(TAG, "DTV3rdManager.getDTVCurrentChannel CurrentChannelNumber:" + Integer.toString(iChannelNumber));

			//lsy add for alibaba start
			if (iChannelNumber == -1) {
				int ptype = mPlayer.getSource().miSourceType;
				String ctypekey = "CHANNEL_TYPE" + ptype;
				Log.i(TAG, "[ptype]" + ptype + "[ctypekey]" + ctypekey);
				String ctypevalue = getValue(ctypekey); //1
				if ("".equals(ctypevalue) || ctypevalue == null) {
					Log.i(TAG, "[ctypevalue is not right]");
				} else {
					Log.i(TAG, "[ctypevalue]" + ctypevalue);
					String cprogkey = "CHANNEL_SERVICE_INDEX" + ctypevalue + ptype;
					Log.i(TAG, "[cprogkey]" + cprogkey);
					String cur = getValue(cprogkey);
					Log.i(TAG, "[cur]" + cur);
					if ("".equals(cur) || cur == null) { //new update
						Log.i(TAG, "[new update]");
					} else {
						iChannelNumber = Integer.valueOf(cur);//20
					}
				}
				Log.i(TAG, "[iChannelNumber]" + iChannelNumber);
			}
			//lsy add for alibaba end

			return iChannelNumber;
		} catch (RemoteException exception) {
			//nothing to do
			Log.e(TAG, "DTV3rdManager.getDTVCurrentChannel exception" + exception.toString());
		}
		return -1;
	}

	private void loadFile(String filePath) {
		File file = null;
		FileInputStream inputFile = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				Log.i(TAG, "dtvService>>loadFile>>createNewFile***");
				file.createNewFile();
				Runtime.getRuntime().exec("chmod 777 " + file);
			}
			inputFile = new FileInputStream(file);
			Log.i(TAG, "dtvService>>loadFile>>load***");
			propertie.load(inputFile);

		} catch (FileNotFoundException ex) {
			Log.e(TAG, "dtvService>>loadFile>>FileNotFoundException***");
			ex.printStackTrace();
		} catch (IOException ex) {
			Log.e(TAG, "dtvService>>loadFile>>IOException***");
			ex.printStackTrace();
		} finally {
			try {
				if (null != inputFile) {
					inputFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getValue(String key) {
		Log.i(TAG, "[Entry getValue-key]" + key);
		String value = null;
		loadFile(CONFIG_FILE_PATH);
		if (propertie.containsKey(key)) {
			value = propertie.getProperty(key);
			Log.i(TAG, "[value]" + value);
			return value;
		} else {
			return value;
		}
	}

	@Override
	public InterCardStatus getCardStatus() throws RemoteException {
		// TODO Auto-generated method stub
		DTVCardStatus dtvCard = mPlayer.getCardStatus();

		if (dtvCard != null) {
			InterCardStatus interCardStatus = new InterCardStatus();

			interCardStatus.miCardStatus = dtvCard.miCardStatus;
			interCardStatus.miCardType = dtvCard.miCardType;
			interCardStatus.mstrCardID = mPlayer.getSmartCardID();
			return interCardStatus;
		}
		return null;
	}

	@Override
	public InterOperator getOperator() throws RemoteException {
		// TODO Auto-generated method stub

		Operator operator = mIDTVSettingsServer.getOP();
		if (operator == null) {
			return null;
		}

		InterOperator interOperator = new InterOperator();
		interOperator.miOperatorCode = operator.miOperatorCode;
		interOperator.mstrOperatorName = operator.mstrOperatorName;

		return interOperator;
	}

	@Override
	public InterChannelInfo getChannelInfo(int channelIndex) throws RemoteException {
		// TODO Auto-generated method stub
		DTVChannelDetailInfo detailInfo;
		detailInfo = mIChannelManagerServer.getChanneDetailInfo(channelIndex);
		if (detailInfo != null) {
			InterChannelInfo interInfo = ConvertChannelInfo(detailInfo, detailInfo.miChannelnumber);
			return interInfo;
		}

		return null;
	}

	@Override
	public int ReleaseForBack() throws RemoteException {
		// TODO Auto-generated method stub
		if (mPlayer == null) {
			return 0;
		}

		try {
			mPlayer.ReleaseForBack();
			mIDtvServer.DestroyDTVPlayer(mDTVPlayerBinder);
			mDTVPlayerBinder = null;
			mPlayer = null;
			return 0;
		} catch (RemoteException exception) {
			return 1;
		}
	}

	@Override
	public int SystemReset(int type) throws RemoteException {
		// TODO Auto-generated method stub
		if (mIDtvServer == null) {
			return 1;
		}

		try {
			return mIDtvServer.SystemReset(type);
		} catch (RemoteException exception) {
			return 1;
		}
	}

	@Override
	public int requestResource(int type, int id) throws RemoteException {
		// TODO Auto-generated method stub
		if (mIDtvServer == null) {
			return 1;
		}

		try {
			return mIDtvServer.requestResource(type, id);
		} catch (RemoteException exception) {
			return 1;
		}
	}

	@Override
	public int releaseResource(int type, int id) throws RemoteException {
		// TODO Auto-generated method stub
		if (mIDtvServer == null) {
			return 1;
		}

		try {
			return mIDtvServer.releaseResource(type, id);
		} catch (RemoteException exception) {
			return 1;
		}
	}

	@Override
	public int getCurSourceID() throws RemoteException {
		if (mPlayer == null) {
			return -1;
		}

		try {
			int miSourceID = 0;
			DTVSource mDTVSource = mPlayer.getSource();
			miSourceID = mDTVSource.miSourceID;

			return miSourceID;
		} catch (RemoteException exception) {
			return -1;
		}
	}

	@Override
	public List<InterDTVSource> getDTVSourceList() throws RemoteException {
		// TODO Auto-generated method stub
		if (mIDtvServer == null) {
			return null;
		}

		try {
			DTVSource[] tmpList = mIDtvServer.getDTVSourceList();
			if ((tmpList == null) || (tmpList.length <= 0)) {
				return null;
			}

			InterDTVSource tmpSource = null;
			List<InterDTVSource> sourceList = new ArrayList<InterDTVSource>();
			for (int i = 0; i < tmpList.length; i++) {
				tmpSource = new InterDTVSource(tmpList[i].miSourceType, tmpList[i].miSourceID, tmpList[i].miSourceName);
				sourceList.add(tmpSource);
			}
			return sourceList;
		} catch (RemoteException exception) {
			return null;
		}
	}

	@Override
	public InterChDetailInfo getChannelDetailInfo(int channelIndex) throws RemoteException {
		// TODO Auto-generated method stub
		DTVChannelDetailInfo detailInfo;
		detailInfo = mIChannelManagerServer.getChanneDetailInfo(channelIndex);
		if (detailInfo != null) {
			InterChDetailInfo interInfo = ConvertDetailChannelInfo(detailInfo, detailInfo.miChannelnumber);
			return interInfo;
		}
		return null;
	}

	@Override
	public List<InterEPGEvent> getSchelueEventWithStamp(int iChannelIndex, InterUTCDate date, InterUTCTime time) throws RemoteException {
		// TODO Auto-generated method stub
		if (mPlayer == null) {
			return null;
		}

		try {
			DTVDTTime time1 = new DTVDTTime();
			EPGEvent[] epgevent = mPlayer.getSchelueEventWithStamp(iChannelIndex, time1);
			if (epgevent != null) {
				List<InterEPGEvent> epgEventList = new ArrayList<InterEPGEvent>();
				for (int i = 0; i < epgevent.length; i++) {
					InterEPGEvent eventTmp = ConvertEPGEvent(epgevent[i]);
					epgEventList.add(eventTmp);
				}
				date.miYear = time1.mstruDate.miYear;
				date.miMonth = time1.mstruDate.miMonth;
				date.miDay = time1.mstruDate.miDay;
				date.miWeekDay = time1.mstruDate.miWeekDay;
				time.miHour = time1.mstruTime.miHour;
				time.miMinute = time1.mstruTime.miMinute;
				time.miSecond = time1.mstruTime.miSecond;

				return epgEventList;
			}
		} catch (RemoteException exception) {
			//no thing to do
		}

		return null;
	}

	@Override
	public InterDTVChannelBaseInfo getChannelBaseInfo(int iChannelIndex) throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.getChannelBaseInfo service binder failed");
			return null;
		}

		Log.i(TAG, "Enter getChannelBaseInfo iChannelIndex:" + iChannelIndex);

		DTVChannelBaseInfo channelbaseInfo;
		channelbaseInfo = mIChannelManagerServer.getChannelBaseInfo(iChannelIndex);
		if (channelbaseInfo != null) {
			InterDTVChannelBaseInfo interInfo = ConvertChannelBaseInfo(channelbaseInfo, channelbaseInfo.miChannelnumber);

			//Log.i(TAG, "Enter InterDTVChannelBaseInfo iChannelIndex:" + interInfo.miChannelIndex + "  log:" + interInfo.msLogo);

			return interInfo;
		}

		Log.i(TAG, "Enter getChannelBaseInfo channelbaseInfo: null");

		return null;
	}

	@Override
	public List<InterDTVChannelBaseInfo> getChannelListByTpye(int iServiceType) throws RemoteException {

		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.getChannelListByTpye service binder failed");
			return null;
		}

		try {

			DTVChannelBaseInfo[] tmpList = mIChannelManagerServer.getChannelListByTpye(iServiceType);
			if ((tmpList == null) || (tmpList.length <= 0)) {
				return null;
			}

			InterDTVChannelBaseInfo tmpChinannelBaseInfo = null;
			List<InterDTVChannelBaseInfo> chinannelBaseInfoList = new ArrayList<InterDTVChannelBaseInfo>();
			for (int i = 0; i < tmpList.length; i++) {
				tmpChinannelBaseInfo = new InterDTVChannelBaseInfo(tmpList[i].miChannelIndex, tmpList[i].miChannelnumber, tmpList[i].mstrServiceName, tmpList[i].miServiceType, tmpList[i].miServiceID,
						tmpList[i].miTSID, tmpList[i].miOrgNetID, tmpList[i].mbScrambled, tmpList[i].mstrCurAudioTrack, tmpList[i].miSoundMode, tmpList[i].miBanlenceVolume, tmpList[i].msRating,
						tmpList[i].mbLock, tmpList[i].mbSkip, tmpList[i].mbFav, tmpList[i].miDemodType, tmpList[i].msLogo);
				chinannelBaseInfoList.add(tmpChinannelBaseInfo);
			}
			return chinannelBaseInfoList;
		} catch (RemoteException exception) {
			//no thing to do
			Log.e(TAG, "DTV3rdManager.getChannelListByTpye exception");
		}

		return null;
	}

	/*������EPGԤԼ�ӿ�*/
	private InterTimerInfo ConvertTimerInfo(TimerInfo timer) {
		InterTimerInfo interTimerInfo = new InterTimerInfo();

		interTimerInfo.miIndex = timer.miIndex;
		interTimerInfo.miType = timer.miType;
		interTimerInfo.mOriginal = timer.mOriginal;//2015-1-21
		interTimerInfo.mlStartNotifyTime = timer.mlStartNotifyTime;
		interTimerInfo.mlNotNotifyTime = timer.mlNotNotifyTime;
		interTimerInfo.mstrTriggerBroadCast = timer.mstrTriggerBroadCast;
		interTimerInfo.mByteDataInfo = timer.mByteDataInfo;

		return interTimerInfo;

	}

	@Override
	public InterTimerInfo getTimerInfo(long start, long end, int original, int sourceID, int serviceIndex, String url, int programNum, String programName, String eventName) throws RemoteException {
		InterTimerInfo interTimerInfo = null;

		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.getTimerInfo service binder failed");
			return null;
		}
		Date dstart = null;
		String mill = readLine("/data/pvrstart.txt");
		if (mill != null) {
			try {
				dstart = new Date(System.currentTimeMillis() + Long.valueOf(mill));
			} catch (NumberFormatException e) {
				Log.i(TAG, e.getMessage());
				e.printStackTrace();
			}
		} else {
			dstart = new Date(start);
		}

//		dstart = new Date(System.currentTimeMillis()+30000);

		Date dend = new Date(end);
		String wikiInfo = url;
		Log.i("YangLiu", "	startTime=" + new Date(start) + "	endTime=" + new Date(end) + "	original=" + original + "	sourceID=" + sourceID + "	serviceIndex=" + serviceIndex + "	url=" + url
				+ "	programNum=" + programNum + "	programName=" + programName + "	eventName=" + eventName);

		BaseProgram baseProgram = new BaseProgram(dstart, dend, sourceID, original, serviceIndex, programNum, programName, eventName, wikiInfo);
		TimerInfo timerInfo = baseProgramManager.convertScheduleProgramToTimerInfo(baseProgram);

		if (timerInfo != null) {
			interTimerInfo = ConvertTimerInfo(timerInfo);
		}
		return interTimerInfo;
	}

	private String readLine(String path) {
		FileReader reader = null;
		String line = null;
		BufferedReader bufferedReader = null;
		try {
			reader = new FileReader(path);
			bufferedReader = new BufferedReader(reader);
			line = bufferedReader.readLine();
			Log.i(TAG, "[line]" + line);
			bufferedReader.close();
		} catch (Exception e) {
			Log.i(TAG, "[e]" + e.getMessage());
		}
		return line;
	}

	@Override
	public int addTimer(InterTimerInfo timer) throws RemoteException {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.addTimer service binder failed");
			return -1;
		}

		Log.i(TAG, "DTV3rdManager.addTimer ----1");
		if (timer == null) {
			Log.i(TAG, "timer is null");
		} else {
			Log.i("[index]", String.valueOf(timer.miIndex));
			Log.i("[type]", String.valueOf(timer.miType));
			Log.i("[start]", String.valueOf(timer.mlStartNotifyTime));
			Log.i("[end]", String.valueOf(timer.mlNotNotifyTime));
			Log.i("[original]", String.valueOf(timer.mOriginal));
			Log.i("[broadcast]", timer.mstrTriggerBroadCast);
		}
		try {
			TimerInfo mTimerInfo = new TimerInfo();

			mTimerInfo.miIndex = timer.miIndex;
			mTimerInfo.miType = timer.miType;
			mTimerInfo.mlStartNotifyTime = timer.mlStartNotifyTime;
			mTimerInfo.mlNotNotifyTime = timer.mlNotNotifyTime;
			mTimerInfo.mOriginal = timer.mOriginal;
			mTimerInfo.mstrTriggerBroadCast = timer.mstrTriggerBroadCast;
			mTimerInfo.mByteDataInfo = timer.mByteDataInfo;

			Log.i(TAG, "DTV3rdManager.addTimer ----2");

			return mITimerServer.addTimer(mTimerInfo);
		} catch (RemoteException exception) {
			return -1;
		}
	}

	@Override
	public List<InterTimerInfo> getTimerList(int type) throws RemoteException {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.getTimerList service binder failed");
			return null;
		}

		try {
			List<TimerInfo> mTimerInfo = new ArrayList<TimerInfo>();
			List<InterTimerInfo> tmpTimerList = new ArrayList<InterTimerInfo>();

			mTimerInfo = mITimerServer.getTimerList(type);
			if (mTimerInfo == null) {
				Log.i(TAG, "List<TimerInfo> mTimerInfo null:");

				return null;
			}

			Log.i(TAG, "getTimerList : " + mTimerInfo.size());
			synchronized (mTimerInfo) {
				Iterator<TimerInfo> iterator = mTimerInfo.iterator();
				while (iterator.hasNext()) {
					TimerInfo tempTimer = iterator.next();
					if (tempTimer.miType == type) {
						tmpTimerList.add(ConvertTimerInfo(tempTimer));
					}
				}
			}

			return tmpTimerList;
		} catch (RemoteException exception) {
			return null;
		}
	}

	@Override
	public int deleteTimer(InterTimerInfo timer) throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.deleteTimer service binder failed");
			return -1;
		}

		try {
			TimerInfo mTimerInfo = new TimerInfo();

			mTimerInfo.miIndex = timer.miIndex;
			mTimerInfo.miType = timer.miType;
			mTimerInfo.mlStartNotifyTime = timer.mlStartNotifyTime;
			mTimerInfo.mlNotNotifyTime = timer.mlNotNotifyTime;
			mTimerInfo.mstrTriggerBroadCast = timer.mstrTriggerBroadCast;
			mTimerInfo.mByteDataInfo = timer.mByteDataInfo;

			return mITimerServer.deleteTimer(mTimerInfo);
		} catch (RemoteException exception) {
			return -1;
		}
	}

	@Override
	public int deleteAllTimer() throws RemoteException {
		// TODO Auto-generated method stub
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.deleteAllTimer service binder failed");
			return -1;
		}

		try {
			return mITimerServer.deleteAllTimer();
		} catch (RemoteException exception) {
			return 1;
		}
	}

	@Override
	public int LoadOrSavedDb(String pathname, int isSave) throws RemoteException {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.LoadOrSavedDb service binder failed");
			return -1;
		}
		return mIChannelManagerServer.LoadOrSavedDb(pathname, isSave);
	}

	@Override
	public int getChannelListVersion() throws RemoteException {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.getChannelListVersion service binder failed");
			return -1;
		}
		return mIChannelManagerServer.getChannelListVersion();
	}

	@Override
	public int getDBVersion() throws RemoteException {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.getDBVersion service binder failed");
			return -1;
		}
		Log.i(TAG, "getDBVersion checkSB ok");
		return mIChannelManagerServer.getDBVersion();
	}

	@Override
	public List<InterOperator> getOPListByCity(String province, String city) throws RemoteException {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.getOPListByCity service binder failed");
			return null;
		}
		Log.i(TAG, "DTV3rdManager.getOPListByCity service binder SUCCESS");

		List<Operator> operators = mIDTVSettingsServer.getOPListByCity(province, city);
		List<InterOperator> interOperatorArray = new ArrayList<InterOperator>();
		for (int i = 0; i < operators.size(); i++) {
			InterOperator interOperator = new InterOperator();
			interOperator.miOperatorCode = operators.get(i).miOperatorCode;
			interOperator.mstrOperatorName = operators.get(i).mstrOperatorName;
			interOperatorArray.add(interOperator);
		}
		return interOperatorArray;

	}

	@Override
	public int setOP(int iOperatorCode) throws RemoteException {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.setOP service binder failed");
			return -1;
		}
		return mIDTVSettingsServer.setOperator(iOperatorCode);
	}

	@Override
	public int PVR_REC_START(int hID, int ChannelID, String url, String ChannelName, long sTime, long eTime) throws RemoteException {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.PVR_REC_START service binder failed");
			return -1;
		}
		return mPVR.PVR_REC_START(hID, ChannelID, url, ChannelName, sTime, eTime);
	}

	@Override
	public int PVR_REC_STOP(int ChannelID) throws RemoteException {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.PVR_REC_STOP service binder failed");
			return -1;
		}
		return mPVR.PVR_REC_STOP(ChannelID);
	}

	@Override
	public int PVRSTOP() throws RemoteException {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.PVRSTOP service binder failed");
			return -1;
		}
		return mPVR.PVRSTOP();
	}

	@Override
	public int getPVRStatus() throws RemoteException {
		Log.i(TAG, "[getPVRS]");
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.getStatus service binder failed");
			return -1;
		}
		Log.i(TAG, "[DTV3rdManager.getStatus]");
		return mPVR.getPVRStatus();
	}

	@Override
	public int write(byte[] buffer) throws RemoteException {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.write service binder failed");
			return -1;
		}
		return mPVR.write(buffer);
	}

	@Override
	public int getLength() throws RemoteException {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.getLength service binder failed");
			return -1;
		}
		return mPVR.getLength();
	}

	@Override
	public ParcelFileDescriptor getFileDescriptor() throws RemoteException {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.getFileDescriptor service binder failed");
			return null;
		}
		return mPVR.getFileDescriptor();
	}

	@Override
	public void refreshRecBuffer(int ChannelID) throws RemoteException {
		if (CheckServiceBinder() != 0) {
			Log.i(TAG, "DTV3rdManager.refreshRecBuffer service binder failed");
			return;
		}
		mPVR.refreshRecBuffer(ChannelID);
	}
}