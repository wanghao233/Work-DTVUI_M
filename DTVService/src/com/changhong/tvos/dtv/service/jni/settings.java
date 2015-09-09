package com.changhong.tvos.dtv.service.jni;

import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.dtv.vo.DVBSTransponder;
import com.changhong.tvos.dtv.vo.DVBTCarrier;
import com.changhong.tvos.dtv.vo.OPFeatureInfo;
import com.changhong.tvos.dtv.vo.Operator;
import com.changhong.tvos.dtv.vo.PictureParam;
import com.changhong.tvos.dtv.vo.VersionInfo;

public class settings {
	private static final String TAG="settings";
	private static settings instance = null;
//	static
//	{
//		try{
//			Log.i(TAG,"chdtv settings start");
//			System.loadLibrary("chdtv");
//			Log.i(TAG,"chdtv settings success");
//			System.loadLibrary("DTVServiceJNI");
//			Log.i(TAG,"servicejni dtvinfo success");
//		}catch(Exception e){
//			Log.i(TAG,"e->"+e.getMessage());
//		}
//
//	}
	protected  settings(){
		
	}
	public static synchronized settings getinstance(){
		if(instance == null)
			instance = new settings();
		return instance;
	}
	
	
	
	public native String getDtvInfo(int type);
	
	public native DVBCCarrier getDVBCOPDefaultFreq();
	public native DVBSTransponder getDVBSOPDefaultFreq();
	public native DVBTCarrier getDVBTOPDefaultFreq();
	public native DMBTHCarrier getDMBTHOPDefaultFreq();

	public native DVBCCarrier[] getDVBCOPMainFreqList();

	public native OPFeatureInfo getOPFeature();

	public native Operator[] getOPList();
	public native Operator[] getOPListBySourceID(int sourceID);
	public native int setOP(int iOperatorCode);
	public native Operator getOP();
	public native VersionInfo getVersion();
	public native int setLanguage(String sLanguge);
	public native String getLanguage();
	public native int setAudioLanguage(String sLanguge);
	public native String getAudioLanguage();
	public native int setLocalTimeOffset(int iLocalTimeOffset);
	public native int getLocalTimeOffset();
	public native int setGlobalValumeFlag(boolean bGlobalValumeFlag);
	public native int getGlobalValumeFlag();

	public native int setVideoAspectMode(int iAspectMode);
	public native int getVideoAspectMode();
	public native int setVideoAspectRatio(int iAspectRatio);
	public native int getVideoAspectRatio();
	public native int setRFOutParam(int iFormat, int iChannelNumb);
	public native int setRFStandby();
	public native int setOutPorts(int iPorts);
	public native int getOutPorts();
	public native int setVideoOut(boolean bIsOpen);
	public native int getVideoOut();
	public native int setVideoOutFormat(int iPath, int outFormat);
	public native int getVideoOutFormat(int iPath);

	public native int setVideoPictureParam( PictureParam pictureParam);
	public native PictureParam getVideoPictureParam();
	public native int setSPDIFOutMode(boolean bCompressMode);
	public native int getSPDIFOutMode();

	public native int setSoundMode(int soundMode);
	public native int getSoundMode();
	public native int setScartOutMode(int scartOutMode);

	public native int getScartOutMode();
	
	public native int systemReset(int type);
	
	// 获取运营商
	public native Operator[] getOPListByCity(String province, String city);
}
