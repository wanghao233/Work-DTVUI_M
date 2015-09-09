package com.changhong.tvos.dtv.service;

import java.util.ArrayList;
import java.util.List;

import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;

import com.changhong.tvos.common.exception.IllegalValueException;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstSoundMode;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.dtv.vo.DVBTCarrier;
import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DVBSTransponder;
import com.changhong.tvos.dtv.vo.InterPanelInfo;
import com.changhong.tvos.dtv.vo.OPFeatureInfo;
import com.changhong.tvos.dtv.vo.Operator;
import com.changhong.tvos.dtv.vo.PictureParam;
import com.changhong.tvos.dtv.vo.VersionInfo;
import com.changhong.tvos.dtv.vo.InterTVOSVersion;
import com.changhong.tvos.model.EnumInputSource;
import com.changhong.tvos.model.ChOsType.ENUMSoundChannle;
import com.changhong.tvos.model.ChOsType.EnumPipInputSource;
import com.changhong.tvos.model.PanelInfo;
import com.changhong.tvos.model.TVOSVersion;

public class DTVSettings extends IDTVSettings.Stub {
	final static String TAG = "dtvservice.DTVSettings";
	private  static DTVServiceRm rmManager = null;
	
	public DTVSettings(){
		if(rmManager == null)
		{
			rmManager =  DTVServiceRm.getinstance();
		}
	}

	//@Override
	public DVBCCarrier getDVBCOPDefaultFreq() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getDVBCOPDefaultFreq();
	}

	//@Override
	public DVBSTransponder getDVBSOPDefaultFreq() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getDVBSOPDefaultFreq();
	}

	//@Override
	public DVBTCarrier getDVBTOPDefaultFreq() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getDVBTOPDefaultFreq();
	}

	//@Override
	public DMBTHCarrier getDMBTHOPDefaultFreq() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getDMBTHOPDefaultFreq();
	}

	//@Override
	public OPFeatureInfo getOPFeature() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getOPFeature();
	}

	//@Override
	public Operator[] getOPList() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getOPList();
	}

	//@Override
	public Operator[] getOPListBySourceID(int sourceID) throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getOPListBySourceID(sourceID);
	}
	
	//@Override
	public int setOP(int iOperatorCode) throws RemoteException {
		// TODO Auto-generated method stub
		int result = DTVServiceJNI.get_settings_instance().setOP(iOperatorCode);
		DTVService.createOpFile();
		
		return result;
	}
	
	public Operator getOP() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getOP();
	}
	//@Override
	public VersionInfo getVersion() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getVersion();
	}

	//@Override
	public int setLanguage(String sLanguge) throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setLanguage(sLanguge);
	}

	//@Override
	public String getLanguage() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getLanguage();
	}

	//@Override
	public int setAudioLanguage(String sLanguge) throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setAudioLanguage(sLanguge);
	}

	//@Override
	public String getAudioLanguage() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getAudioLanguage();
	}

	//@Override
	public int setLocalTimeOffset(int iLocalTimeOffset) throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setLocalTimeOffset(iLocalTimeOffset);
	}

	//@Override
	public int getLocalTimeOffset() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getLocalTimeOffset();

	}

	//@Override
	public int setGlobalValumeFlag(boolean bGlobalValumeFlag)
			throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setGlobalValumeFlag(bGlobalValumeFlag);
	}

	//@Override
	public int getGlobalValumeFlag() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getGlobalValumeFlag();
	}

	//@Override
	public int setVideoAspectMode(int iAspectMode) throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setVideoAspectMode(iAspectMode);
	}

	//@Override
	public int getVideoAspectMode() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getVideoAspectMode();
	}

	//@Override
	public int setVideoAspectRatio(int iAspectRatio) throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setVideoAspectRatio(iAspectRatio);
	}

	//@Override
	public int getVideoAspectRatio() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getVideoAspectRatio();
	}

	//@Override
	public int setRFOutParam(int iFormat, int iChannelNumb)
			throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setRFOutParam(iFormat,iChannelNumb);
	}

	//@Override
	public int setRFStandby() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setRFStandby();
	}

	//@Override
	public int setOutPorts(int iPorts) throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setOutPorts(iPorts);
	}

	//@Override
	public int getOutPorts() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getOutPorts();
	}

	//@Override
	public int setVideoOut(boolean bIsOpen) throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setVideoOut(bIsOpen);
	}

	//@Override
	public int getVideoOut() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getVideoOut();
	}

	//@Override
	public int setVideoOutFormat(int iPath, int outFormat)
			throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setVideoOutFormat(iPath,outFormat);
	}

	//@Override
	public int getVideoOutFormat(int iPath) throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getVideoOutFormat(iPath);
	}

	//@Override
	public int setVideoPictureParam(PictureParam pictureParam)
			throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setVideoPictureParam(pictureParam);
	}

	//@Override
	public PictureParam getVideoPictureParam() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getVideoPictureParam();
	}

	//@Override
	public int setSPDIFOutMode(boolean bCompressMode) throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setSPDIFOutMode(bCompressMode);
	}

	//@Override
	public int getSPDIFOutMode() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getSPDIFOutMode();
	}

	//@Override
	public int setSoundMode(int soundMode) throws RemoteException {
		// TODO Auto-generated method stub
		try{
			ENUMSoundChannle enSudChannel = ENUMSoundChannle.CH_SOUND_CHANNEL_STEREO;
			switch(soundMode){
			   case ConstSoundMode.CH_DTV_SOUND_LEFT:
				   enSudChannel = ENUMSoundChannle.CH_SOUND_CHANNEL_LEFT;
				   break;
			   case ConstSoundMode.CH_DTV_SOUND_RIGHT:
				   enSudChannel = ENUMSoundChannle.CH_SOUND_CHANNEL_RIGHT;
				   break;
			   case ConstSoundMode.CH_DTV_SOUND_STERE0:
			   case ConstSoundMode.CH_DTV_SOUND_MONO:
				   enSudChannel = ENUMSoundChannle.CH_SOUND_CHANNEL_STEREO;
				   break;
			   case ConstSoundMode.CH_DTV_SOUND_AUTO:
				   enSudChannel = ENUMSoundChannle.CH_SOUND_CHANNEL_AUTO;
				   break;
			   default:
				   break;
			 }

			Log.i(TAG,"----------------->setSoundMode start--->enSudChannel:" + enSudChannel + "-->soundMode:" + soundMode);
			
			DTVServiceRm.getinstance().tvos.tvos_SoundManager.updateSoundChannel(enSudChannel);
			DTVServiceJNI.get_settings_instance().setSoundMode(soundMode);
			Log.i(TAG,"----------------->setSoundMode end");
		}
		catch (IllegalValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return 0;
		//return DTVServiceJNI.get_settings_instance().setSoundMode(soundMode);
	}

	//@Override
	public int getSoundMode() throws RemoteException {
		// TODO Auto-generated method stub
		int soundMode = ConstSoundMode.CH_DTV_SOUND_STERE0;
		try {
			ENUMSoundChannle soundChannel = DTVServiceRm.getinstance().tvos.tvos_SoundManager.getSoundChannel();
			if(soundChannel == ENUMSoundChannle.CH_SOUND_CHANNEL_LEFT){
				soundMode = ConstSoundMode.CH_DTV_SOUND_LEFT;
			}else if(soundChannel == ENUMSoundChannle.CH_SOUND_CHANNEL_RIGHT){
				soundMode = ConstSoundMode.CH_DTV_SOUND_RIGHT;
				
			}else if(soundChannel == ENUMSoundChannle.CH_SOUND_CHANNEL_STEREO){
				soundMode = ConstSoundMode.CH_DTV_SOUND_STERE0;
				
			}else if(soundChannel == ENUMSoundChannle.CH_SOUND_CHANNEL_AUTO){
				soundMode = ConstSoundMode.CH_DTV_SOUND_AUTO;
				
			}else{
				soundMode = ConstSoundMode.CH_DTV_SOUND_STERE0;
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return soundMode;
	}

	//@Override
	public int setScartOutMode(int scartOutMode) throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setScartOutMode(scartOutMode);
	}

	//@Override
	public int getScartOutMode() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getScartOutMode();
	}
	
	@Override
	public DVBCCarrier[] getDVBCOPMainFreqList() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().getDVBCOPMainFreqList();
	}
	@Override
	public boolean enterScreenSaver() throws RemoteException{
		boolean isEnter = false;
		try {
			isEnter = DTVServiceRm.getinstance().tvos.tvos_SystemManager.enterScreenSaver();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return isEnter;
	}
	@Override
	public void enterCommonSetting() throws RemoteException{
		try {
			DTVServiceRm.getinstance().tvos.tvos_SystemManager.enterCommonSetting();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	@Override
	public void updateKeyboardConvertFlag(boolean flag) throws RemoteException{
		try {
			DTVServiceRm.getinstance().tvos.tvos_SystemManager.updateKeyboardConvertFlag(flag);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	@Override
	public boolean isKeyboardKey(int keyCode, KeyEvent event) throws RemoteException{
		boolean isKeyBoardKey = false;
		try {
			isKeyBoardKey = DTVServiceRm.getinstance().tvos.tvos_SystemManager.isKeyboardKey(keyCode, event);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return isKeyBoardKey;
	}
	
	@Override
	public boolean returnLastInputSource() throws RemoteException{
		boolean success = false;
		try {
			success = DTVServiceRm.getinstance().tvos.tvos_ISourceManager.returnLastInputSource(EnumPipInputSource.E_MAIN_INPUT_SOURCE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return success;
	}
	
	@Override
	public boolean startInputSource(int channelNum) throws RemoteException{
		boolean success = false;
		try {
			success = DTVServiceRm.getinstance().tvos.tvos_ITVPlayer.startInputSource(EnumInputSource.E_INPUT_SOURCE_DTV, channelNum);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return success;
	}
	
	@Override
	public String getSWVesion() throws RemoteException{
		String version = null;
		try {
			Log.i(TAG,"DTVServiceRm="+DTVServiceRm.getinstance());
			Log.i(TAG, "tvos="+DTVServiceRm.getinstance().tvos);
			Log.i(TAG,"MiscManager="+DTVServiceRm.getinstance().tvos.tvos_MiscManager);
			Log.i(TAG,"swversion="+DTVServiceRm.getinstance().tvos.tvos_MiscManager.getSWVersion());
			version = DTVServiceRm.getinstance().tvos.tvos_MiscManager.getSWVersion();
			Log.i(TAG,"version="+version);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return version;
	}
	
	@Override
	public String getPQVersion() throws RemoteException{
		String version = null;
		try {
			version = DTVServiceRm.getinstance().tvos.tvos_MiscManager.getPQVersion();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return version;
	}
	
	@Override
	public String getAQVersion() throws RemoteException{
		String version = null;
		try {
			version = DTVServiceRm.getinstance().tvos.tvos_MiscManager.getAQVersion();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return version;
	}
	
	@Override
	public String getFPVersion() throws RemoteException{
		String version = null;
		try {
			version = DTVServiceRm.getinstance().tvos.tvos_MiscManager.getFPVersion();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return version;
	}
	
	@Override
	public String getHWVersion() throws RemoteException{
		String version = null;
		try {
			version = DTVServiceRm.getinstance().tvos.tvos_MiscManager.getHWVersion();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return version;
	}
	
	@Override
	public String getDTVMWVersion() throws RemoteException{
		String version = null;
		try {
			version = DTVServiceRm.getinstance().tvos.tvos_MiscManager.getDTVMWVersion();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return version;
	}
	
	@Override
	public InterTVOSVersion getTVOSVersion() throws RemoteException{
		try {
			InterTVOSVersion mInterTVOSVersion = new InterTVOSVersion();
			TVOSVersion mTVOSVersion = new TVOSVersion();
			
			mTVOSVersion = DTVServiceRm.getinstance().tvos.tvos_SystemManager.getTVOSVersion();
			
			mInterTVOSVersion.mMainVersion = mTVOSVersion.mMainVersion;
			mInterTVOSVersion.mSubVersion = mTVOSVersion.mSubVersion;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public InterPanelInfo getPanelInfo() throws RemoteException{
		try {
			InterPanelInfo mInterPanelInfo = new InterPanelInfo();
			PanelInfo mPanelInfo = new PanelInfo();
			
			mPanelInfo = DTVServiceRm.getinstance().tvos.tvos_MiscManager.getPanelInfo();
			
			mInterPanelInfo.miPanelWidth = mPanelInfo.miPanelWidth;
			mInterPanelInfo.miPanelHeight = mPanelInfo.miPanelHeight;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public String getCurProductType() throws RemoteException{
		String productType = null;
		
		try {
			productType = DTVServiceRm.getinstance().tvos.tvos_MiscManager.getCurProductType();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return productType;
	}
	
	@Override
	public String getCurProductName() throws RemoteException{
		String productName = null;
		
		try {
			productName = DTVServiceRm.getinstance().tvos.tvos_MiscManager.getCurProductName();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return productName;
	}
	
	@Override
	public String getCurrentProductChassisName() throws RemoteException{
		String CurrentProductChassisName = null;
	    try {
	      CurrentProductChassisName = DTVServiceRm.getinstance().tvos.tvos_MiscManager.getCurrentProductChassisName();
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    return CurrentProductChassisName;
	}

	@Override
	public String getDtvInfo(int type) throws RemoteException {
		return DTVServiceJNI.get_settings_instance().getDtvInfo(type);
	}

	@Override
	public List<Operator> getOPListByCity(String province, String city)
			throws RemoteException {
		Operator[] ops =DTVServiceJNI.get_settings_instance().getOPListByCity(province, city);
		List<Operator> oplist =new ArrayList<Operator>();
		if(ops!=null){
			for(int i=0;i<ops.length;i++){
				oplist.add(ops[i]);
			}
		}
		return oplist;
	}

	@Override
	public int setOperator(int iOperatorCode) throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_settings_instance().setOP(iOperatorCode);
	}

	@Override
	public boolean getSystem3D() throws RemoteException {
		// TODO Auto-generated method stub
		try {
			InterPanelInfo mInterPanelInfo = new InterPanelInfo();
			PanelInfo mPanelInfo = new PanelInfo();
			
			mPanelInfo = DTVServiceRm.getinstance().tvos.tvos_MiscManager.getPanelInfo();
			
			mInterPanelInfo.miPanelWidth = mPanelInfo.miPanelWidth;
			mInterPanelInfo.miPanelHeight = mPanelInfo.miPanelHeight;
			
			Log.i(TAG, "!--->setPanelInfo-----miPanelWidth:"+mPanelInfo.miPanelWidth + " miPanelHeight:"+mPanelInfo.miPanelHeight);				
			mInterPanelInfo.mePanel3DType = mPanelInfo.mePanel3DType;	
			 switch(mInterPanelInfo.mePanel3DType)
	    {

         case  PANEL_PDP_2D:
	     case  PANEL_LCD_2D:		 	
	        Log.i(TAG, "!--->getPanelInfo-----2D!!!2D!!!2D!!!");
	         return false;

         case  PANEL_PDP_SG_HALF3D:				 	
         case  PANEL_PDP_SG_FULL3D:
         case  PANEL_LCD_PR_3D:		 	
         case  PANEL_LCD_SG_3D:
		 	 Log.i(TAG, "!--->getPanelInfo-----3D!!!3D!!!3D!!!");
		 	 return true;		 	    	
	    }
		    return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
}
