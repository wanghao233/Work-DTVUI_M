package com.changhong.tvos.dtv.service.jni;

import   com.changhong.tvos.dtv.vo.EPGEvent;
import   com.changhong.tvos.dtv.vo.DTVDTTime;

public class epg {
	private static epg instance = null;
	protected  epg(){
		
	}
	public static synchronized epg getinstance(){
		if(instance == null)
			instance = new epg();
		return instance;
	}
	public native int RegisterCallback(int routerID,epgcallback objcallback);
	public native int UnRegisterCallback(int routerID,epgcallback objcallback);
	
	public native int epgStart(int routerID);
	public native int epgStop(int routerID);
	public native int epgSuspend(int routerID);
	public native int epgResume(int routerID);

	public native EPGEvent[] getPFEvent(int routerID,int iChannelIndex);
	public native EPGEvent[] getSchelueEvent(int routerID,int iChannelIndex);
	public native EPGEvent[] getSchelueEventByTime(int routerID,int channelIndex, DTVDTTime startTime, DTVDTTime endTime);

	public native String getPFEventExtendInfo(int routerID,int iChannelIndex, int iEventID);
	public native String getSchelueEventExtendInfo(int routerID,int iChannelIndex, int iEventID);
	public native EPGEvent[] getSchelueEventWithStamp(int routerID,int iChannelIndex,DTVDTTime time);
}
