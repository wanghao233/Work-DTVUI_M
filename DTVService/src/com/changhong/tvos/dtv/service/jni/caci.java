package com.changhong.tvos.dtv.service.jni;

import com.changhong.tvos.dtv.vo.CICAMInformation;
import com.changhong.tvos.dtv.vo.DTVCardStatus;


public class caci {
	private static caci instance = null;
	protected  caci(){
		
	}
	public static synchronized caci getinstance(){
		if(instance == null)
			instance = new caci();
		return instance;
	}
	public native int RegisterCallback(int routerID,cacicallback objcallback);
	public native int UnRegisterCallback(int routerID,cacicallback objcallback);
	
	public native CICAMInformation getInfo(int routerID);
	public native String getSmartCardID(int routerID);
	public native DTVCardStatus  getCardStatus();
	public native int clearUserData(int routerID);
	public native void queryControl(int routerID,int iMsgType, int iMsgID, int iMenuID,int operand, int opcode, int defOpcode, int inputItems, String[] inputList);

	
}
