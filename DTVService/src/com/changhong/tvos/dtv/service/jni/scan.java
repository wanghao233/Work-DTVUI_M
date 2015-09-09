package com.changhong.tvos.dtv.service.jni;

import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.dtv.vo.DVBSTransponder;
import com.changhong.tvos.dtv.vo.DVBTCarrier;

public class scan {
	private static scan instance = null;
	protected  scan(){
		
	}
	public static synchronized scan getinstance(){
		if(instance == null)
			instance = new scan();
		return instance;
	}
	    public native int RegisterCallback(int routerID,scancallback objcallback);
	    public native int UnRegisterCallback(int routerID,scancallback objcallback);
	    
		public native int SetDVBCParam(int routerID,int iScanMode, DVBCCarrier[] lFreqList);
		public native int SetDVBSParam(int routerID,int iScanMode, DVBSTransponder[] lFreqList);
		public native int SetDVBTParam(int routerID,int iScanMode, DVBTCarrier[] lFreqList);
		public native int SetDMBTHParam(int routerID,int iScanMode, DMBTHCarrier[] lFreqList);
		public native int setSatellite(int routerID,int iSatelliteID);
		
		public native int scanStart(int routerID);
		public native int scanStop(int routerID);
}
