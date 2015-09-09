package com.changhong.tvos.dtv.service.jni;

import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DTVSource;
import com.changhong.tvos.dtv.vo.DTVTunerStatus;
import com.changhong.tvos.dtv.vo.DTVVideoInfo;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.dtv.vo.DVBSTransponder;
import com.changhong.tvos.dtv.vo.DVBTCarrier;

import android.media.AudioTrack;

public class avplayer {
	private static avplayer instance = null;
	protected  avplayer(){
		
	}
	public static synchronized avplayer getinstance(){
		if(instance == null)
			instance = new avplayer();
		return instance;
	}
	public native int RegisterCallback(int routerID,avplayercallback objcallback);
	public native int UnRegisterCallback(int routerID,avplayercallback objcallback);
	
	public native int setSwitchMode(int routerID,int iSwitchMode);
	public native int setVolume(int routerID,int volume);
	public native int getVolume(int routerID);
	public native  int getCurChannelIndex(int routerID);
	public native int play(int routerID,int iChannelIndex);
	public native int playvod(int routerID,int iFrequencyK, int iSymbolRateK, int eQamMode, int iServiceID, int PMTPID);
	public native int playStop(int routerID);
	public native int Pausevideo(int routerID);
	public native int Resumevideo(int routerID);
	public native int PauseAudio(int routerID);
	public native int ResumeAudio(int routerID);
	public native AudioTrack getChannelAudioTrack(int routerID);
	public native int setChannelAudioTrack(int routerID,int iAudioTrack);
	public native DTVVideoInfo getVideoInfo(int routerID);
	public native int clearVideoBuffer(int routerID);	
	 
	public native  DTVTunerStatus getTunerStatus(int ri_RouterID);
	public native  DVBCCarrier getDVBCCurTunerInfo(int ri_RouterID);
    public native  DVBSTransponder getDVBSCurTunerInfo(int ri_RouterID);
    public native  DVBTCarrier getDVBTCurTunerInfo(int ri_RouterID);
	public native  DMBTHCarrier getDMBTHCurTunerInfo(int ri_RouterID);
	
	public native  int setSource(int routerID,int iSourceID);	
	public native  DTVSource	getSource(int routerID);
	
	public native  int SmartSkip(int routerID, int iType);
	public native  int SmartSkipStop(int routerID);	
}
