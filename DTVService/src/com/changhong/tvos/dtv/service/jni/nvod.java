package com.changhong.tvos.dtv.service.jni;

import   com.changhong.tvos.dtv.vo.NvodRefService;
import   com.changhong.tvos.dtv.vo.NvodRefEvent;
import   com.changhong.tvos.dtv.vo.NvodShiftEvent;

public class nvod {
	private static nvod instance = null;
	protected  nvod(){
		
	}
	public native int RegisterCallback(int routerID,nvodcallback objcallback);
	public native int UnRegisterCallback(int routerID,nvodcallback objcallback);
	
	public static synchronized nvod getinstance(){
		if(instance == null)
			instance = new nvod();
		return instance;
	}
	public native int nvodstart(int routerID);
	public native int nvodstop(int routerID);
	public native int nvodsuspend(int routerID);
	public native int nvodresume(int routerID);
	public native NvodRefService[] getRefServices(int routerID);
	public native NvodRefEvent[] getRefEvents(int routerID,int serviceId);
	public native NvodShiftEvent[] getShiftEvents(int routerID,int serviceId,int refEventId);
	
}
