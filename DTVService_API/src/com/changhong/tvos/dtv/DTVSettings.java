/**
 * @filename
 * @describe
 * @author:
 * @date:
 * @version 0.1
 * history:
 * 2012-7-17 getOPMainFreqList
 */
package com.changhong.tvos.dtv;

import java.util.List;
import android.app.Instrumentation;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.KeyEvent;
import com.changhong.tvos.dtv.service.IDTVService;
import com.changhong.tvos.dtv.service.IDTVSettings;
import com.changhong.tvos.dtv.vo.CarrierInfo;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.dtv.vo.DTVConstant.ErrorCode;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.dtv.vo.InterTVOSVersion;
import com.changhong.tvos.dtv.vo.OPFeatureInfo;
import com.changhong.tvos.dtv.vo.Operator;
import com.changhong.tvos.dtv.vo.PictureParam;
import com.changhong.tvos.dtv.vo.VersionInfo;

/**
 * DTVSettings
 */
public class DTVSettings {

    final static String TAG = "DTVAPI.DTVSettings";
    static DTVSettings msInstance = null;

    private IDTVSettings DTVSettingsServer = null;
    private IDTVService mDTVServer = null;

    private DTVSettings() {
    }

    private boolean checkServiceOK() {
        if ((mDTVServer == null) || (!mDTVServer.asBinder().isBinderAlive())) {
            IBinder bind = ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME);
            if (bind != null) {
                mDTVServer = IDTVService.Stub.asInterface(bind);
            } else {
                Log.e(TAG, "service bind filed");
                return false;
            }

        }

        if (mDTVServer == null) {
            Log.e(TAG, "service bind filed");
            return false;
        }

        if ((DTVSettingsServer == null) || (!DTVSettingsServer.asBinder().isBinderAlive())) {
            try {
                IBinder binder = mDTVServer.GetDTVSettings();
                if (binder != null) {
                    DTVSettingsServer = IDTVSettings.Stub.asInterface(binder);
                    return true;
                }
                return false;
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        //Log.e(TAG, "SettingsServer bind filed");
        return true;
    }

    /**
     * getInstance
     */
    public static DTVSettings getInstance(Context context) {
        if (msInstance != null) {
            return msInstance;
        }

        msInstance = new DTVSettings();

        IDTVService dtvServer = null;
        IDTVSettings dtvSetingsServer = null;
        IBinder bind = ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME);
        if (bind != null) {
            dtvServer = IDTVService.Stub.asInterface(bind);
            try {
                IBinder serviceBinder = dtvServer.GetDTVSettings();
                if (serviceBinder != null) {
                    dtvSetingsServer = IDTVSettings.Stub.asInterface(serviceBinder);
                }
            } catch (RemoteException exception) {
                Log.e(TAG, "service bind failed");
//				return null;
            }
        } else {
            Log.e(TAG, "service bind failed");
//			return null;
        }


        msInstance.mDTVServer = dtvServer;
        msInstance.DTVSettingsServer = dtvSetingsServer;

        return msInstance;
    }

    /**
     * getOPFeature
     * @return
     */
    public OPFeatureInfo getOPFeature() {
        if (!checkServiceOK()) {
            Log.e(TAG, "getOPFeature()>>service bind failed");
            return null;
        }

        try {
            return DTVSettingsServer.getOPFeature();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getOPFeature --->>exception");

            return null;
        }
    }

    public String getDtvInfo(int type) {
        if (!checkServiceOK()) {
            Log.e(TAG, "GetDTVInfo()>>service bind failed");
            return "";
        }

        try {
            return DTVSettingsServer.getDtvInfo(type);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "GetDTVSoftVersion --->>exception");

            return "";
        }
    }

    /**
     * getOPList
     */
    public Operator[] getOPList() {
        if (!checkServiceOK()) {
            Log.e(TAG, "getOPList()>>service bind failed");
            return null;
        }

        try {
            return DTVSettingsServer.getOPList();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * getOPListBySourceID
     */
    public Operator[] getOPListBySourceID(int sourceID) {
        if (!checkServiceOK()) {
            Log.e(TAG, "getOPListBySource()>>service bind failed");
            return null;
        }

        try {
            return DTVSettingsServer.getOPListBySourceID(sourceID);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * getOPMainFreqList
     */
    public CarrierInfo[] getOPMainFreqList(int iOperatorCode) {
        if (!checkServiceOK()) {
            return null;
        }

////		try
//		{
////			return DTVSettingsServer.getOPList();
//		}
////		catch (RemoteException exception)
        {
            return null;
        }
    }

    public DVBCCarrier[] getDVBCOPMainFreqList() {
        if (!checkServiceOK()) {
            Log.e(TAG, "getDVBCOPMainFreqList()>>service bind failed");
            return null;
        }

        try {
            return DTVSettingsServer.getDVBCOPMainFreqList();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * setOP
     */
    public int setOP(int iOperatorCode) {
        if (!checkServiceOK()) {
            Log.e(TAG, "setOP()>>service bind failed");
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setOP(iOperatorCode);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "setOP --->>exception");
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * getOP
     */
    public Operator getOP() {
        if (!checkServiceOK()) {
            Log.e(TAG, "getOP()>>service bind failed");
            return null;
        }

        try {
            return DTVSettingsServer.getOP();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getOP --->>exception");

            return null;
        }
    }

    /**
     * getVersion
     */
    public VersionInfo getVersion() {
        if (!checkServiceOK()) {
            Log.e(TAG, "getVersion()>>service bind failed");
            return null;
        }

        try {
            return DTVSettingsServer.getVersion();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getVersion --->>exception");
            return null;
        }
    }

    /**
     * setLanguage
     */
    public int setLanguage(String sLanguge) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setLanguage(sLanguge);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "setLanguage --->>exception");
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * getLanguage
     */
    public String getLanguage() {
        if (!checkServiceOK()) {
            return null;
        }

        try {
            return DTVSettingsServer.getLanguage();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getLanguage --->>exception");
            return null;
        }
    }

    /**
     * setAudioLanguage
     */
    public int setAudioLanguage(String sLanguge) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setAudioLanguage(sLanguge);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "setAudioLanguage --->>exception");

            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * getAudioLanguage
     */
    public String getAudioLanguage() {
        if (!checkServiceOK()) {
            return null;
        }

        try {
            return DTVSettingsServer.getAudioLanguage();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getAudioLanguage --->>exception");

            return null;
        }
    }

    /**
     * setLocalTimeOffset
     */
    public int setLocalTimeOffset(int iLocalTimeOffset) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setLocalTimeOffset(iLocalTimeOffset);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "setLocalTimeOffset --->>exception");

            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * getLocalTimeOffset
     */
    public int getLocalTimeOffset() {
        if (!checkServiceOK()) {
            return -1002;
        }

        try {
            return DTVSettingsServer.getLocalTimeOffset();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getLocalTimeOffset --->>exception");

            return -1002;
        }
    }

    /**
     * setGlobalValumeFlag
     */
    public int setGlobalValumeFlag(boolean bGlobalValumeFlag) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setGlobalValumeFlag(bGlobalValumeFlag);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "setGlobalValumeFlag --->>exception");

            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * getGlobalValumeFlag
     */
    public int getGlobalValumeFlag() {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.getGlobalValumeFlag();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getGlobalValumeFlag --->>exception");

            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * setVideoAspectMode
     */
    public int setVideoAspectMode(int iAspectMode) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setVideoAspectMode(iAspectMode);
        } catch (RemoteException exception) {
            Log.e(TAG, "setVideoAspectMode --->>exception");
            exception.printStackTrace();
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * getVideoAspectMode
     */
    public int getVideoAspectMode() {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.getVideoAspectMode();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getVideoAspectMode --->>exception");

            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }


    /**
     * setVideoAspectRatio
     */
    public int setVideoAspectRatio(int iAspectRatio) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setVideoAspectRatio(iAspectRatio);
        } catch (RemoteException exception) {
            exception.printStackTrace();
            Log.e(TAG, "setVideoAspectRatio --->>exception");
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * getVideoAspectRatio
     */
    public int getVideoAspectRatio() {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.getVideoAspectRatio();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getVideoAspectRatio --->>exception");
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * setRFOutParam
     */
    public int setRFOutParam(int iFormat, int iChannelNumb) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setRFOutParam(iFormat, iChannelNumb);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "setRFOutParam --->>exception");

            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * setRFStandby
     */
    public int setRFStandby() {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setRFStandby();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "setRFStandby --->>exception");
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * setOutPorts
     */
    public int setOutPorts(int iPorts) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setOutPorts(iPorts);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "setOutPorts --->>exception");

            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * getOutPorts
     */
    public int getOutPorts() {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.getOutPorts();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getOutPorts --->>exception");

            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * setVideoOut
     */
    public int setVideoOut(boolean bIsOpen) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setVideoOut(bIsOpen);
        } catch (RemoteException exception) {
            Log.e(TAG, "setVideoOut --->>exception");
            exception.printStackTrace();
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * getVideoOut
     */
    public int getVideoOut() {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.getVideoOut();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getVideoOut --->>exception");

            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * setVideoOutFormat
     */
    public int setVideoOutFormat(int iPath, int iOutFormat) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setVideoOutFormat(iPath, iOutFormat);
        } catch (RemoteException exception) {
            Log.e(TAG, "setVideoOutFormat --->>exception");
            exception.printStackTrace();
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * getVideoOutFormat
     */
    public int getVideoOutFormat(int iPath) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.getVideoOutFormat(iPath);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getVideoOutFormat --->>exception");
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * setVideoPictureParam
     */
    public int setVideoPictureParam(PictureParam pictureParam) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setVideoPictureParam(pictureParam);
        } catch (RemoteException exception) {
            Log.e(TAG, "setVideoPictureParam --->>exception");
            exception.printStackTrace();
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * getVideoPictureParam
     */
    public PictureParam getVideoPictureParam() {
        if (!checkServiceOK()) {
            return null;
        }

        try {
            return DTVSettingsServer.getVideoPictureParam();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getVideoPictureParam --->>exception");
            return null;
        }
    }

    /**
     * setSPDIFOutMode
     */
    public int setSPDIFOutMode(boolean bCompressMode) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setSPDIFOutMode(bCompressMode);
        } catch (RemoteException exception) {
            Log.e(TAG, "setSPDIFOutMode --->>exception");
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * getSPDIFOutMode
     */
    public int getSPDIFOutMode() {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.getSPDIFOutMode();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getSPDIFOutMode --->>exception");
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * setLayerPriority
     */
    public int setLayerPriority(int[] layerList) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

/**        try
 {
 return DTVSettingsServer.set
 }
 catch (RemoteException exception)
 {
 return 1;
 }
 **/
        return ErrorCode.ERROR_BINDER_FAILD;
    }

    /**
     * getLayerPriority
     */
    public int[] getLayerPriority() {
        if (!checkServiceOK()) {
            return null;
        }

/**        try
 {
 return DTVSettingsServer
 }
 catch (RemoteException exception)
 {
 return null;
 }
 **/
        return null;
    }

    /**
     * setSoundMode
     */
    public int setSoundMode(int iSoundMode) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setSoundMode(iSoundMode);
        } catch (RemoteException exception) {
            exception.printStackTrace();
            Log.e(TAG, "setSoundMode Exception1");
            return ErrorCode.ERROR_BINDER_FAILD;
        } catch (Exception ex) {
            Log.e(TAG, "setSoundMode Exception2");

            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * getSoundMode
     */
    public int getSoundMode() {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.getSoundMode();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getSoundMode --->>exception");
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * setScartOutMode
     */
    public int setScartOutMode(int iScartOutMode) {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.setScartOutMode(iScartOutMode);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "setScartOutMode --->>exception");
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * getScartOutMode
     */
    public int getScartOutMode() {
        if (!checkServiceOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return DTVSettingsServer.getScartOutMode();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getScartOutMode --->>exception");

            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * enterScreenSaver
     */
    public boolean enterScreenSaver() {
        if (!checkServiceOK()) {
            return false;
        }

        try {
            return DTVSettingsServer.enterScreenSaver();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "enterScreenSaver --->>exception");
            return false;
        }
    }

    /**
     * enterCommonSetting
     */
    public void enterCommonSetting() {
        if (!checkServiceOK()) {
            return;
        }

        try {
            DTVSettingsServer.enterCommonSetting();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "enterCommonSetting --->>exception");
            return;
        }
    }

    /**
     * isKeyboardKey
     */
    public boolean isKeyboardKey(int keyCode, KeyEvent event) {
        if (!checkServiceOK()) {
            return false;
        }

        try {
            return DTVSettingsServer.isKeyboardKey(keyCode, event);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "isKeyboardKey --->>exception");

            return false;
        }
    }

    /**
     * updateKeyboardConvertFlag
     */
    public void updateKeyboardConvertFlag(boolean flag) {
        if (!checkServiceOK()) {
            return;
        }

        try {
            DTVSettingsServer.updateKeyboardConvertFlag(flag);
        } catch (RemoteException exception) {
            Log.e(TAG, "updateKeyboardConvertFlag --->>exception");
            exception.printStackTrace();
            return;
        }
    }

    /**
     * returnLastInputSource
     */
    public boolean returnLastInputSource() {
        if (!checkServiceOK()) {
            return false;
        }

        try {
            return DTVSettingsServer.returnLastInputSource();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "returnLastInputSource --->>exception");
            return false;
        }
    }

    /**
     * startInputSource
     */
    public boolean startInputSource(int channelNum) {
        if (!checkServiceOK()) {
            return false;
        }

        try {
            return DTVSettingsServer.startInputSource(channelNum);
        } catch (RemoteException exception) {
            Log.e(TAG, "startInputSource --->>exception");
            exception.printStackTrace();
            return false;
        }
    }

    ///fengy 2014-7-
    public boolean getSystem3D() {
        if (!checkServiceOK()) {
            return false;
        }

        try {
            return DTVSettingsServer.getSystem3D();
        } catch (RemoteException exception) {
            Log.e(TAG, "startInputSource --->>exception");
            exception.printStackTrace();
            return false;
        }
    }


    /**
     * getSWVersion
     */
    public String getSWVersion() {
        if (!checkServiceOK()) {
            return null;
        }

        try {
            return DTVSettingsServer.getSWVesion();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getSWVersion --->>exception");
        }
        return null;
    }

    /**
     * getPQVersion
     */
    public String getPQVersion() {
        if (!checkServiceOK()) {
            return null;
        }

        try {
            return DTVSettingsServer.getPQVersion();
        } catch (RemoteException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e(TAG, "getPQVersion --->>exception");
        }
        return null;
    }

    /**
     * getAQVersion
     */
    public String getAQVersion() {
        if (!checkServiceOK()) {
            return null;
        }

        try {
            return DTVSettingsServer.getAQVersion();
        } catch (RemoteException e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e(TAG, "getAQVersion --->>exception");
        }
        return null;
    }

    /**
     * getFPVersion
     */
    public String getFPVersion() {
        if (!checkServiceOK()) {
            return null;
        }

        try {
            return DTVSettingsServer.getFPVersion();
        } catch (RemoteException e) {
            // TODO: handle exception
            e.printStackTrace();

            Log.e(TAG, "getFPVersion --->>exception");
        }
        return null;
    }

    /**
     * getHWVersion
     */
    public String getHWVersion() {
        if (!checkServiceOK()) {
            return null;
        }

        try {
            return DTVSettingsServer.getHWVersion();
        } catch (RemoteException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    /**
     * getDTVMWVersion
     */
    public String getDTVMWVersion() {
        if (!checkServiceOK()) {
            return null;
        }

        try {
            return DTVSettingsServer.getDTVMWVersion();
        } catch (RemoteException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    /**
     * getTVOSVersion
     */
    public InterTVOSVersion getTVOSVersion() {
        if (!checkServiceOK()) {
            return null;
        }

        try {
            return DTVSettingsServer.getTVOSVersion();
        } catch (RemoteException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    /**
     * getCurProductType
     */
    public String getCurProductType() {
        if (!checkServiceOK()) {
            return null;
        }

        try {
            return DTVSettingsServer.getCurProductType();
        } catch (RemoteException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    /**
     * getCurProductName
     */
    public String getCurProductName() {
        if (!checkServiceOK()) {
            return null;
        }

        try {
            return DTVSettingsServer.getCurProductName();
        } catch (RemoteException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    /**
     * getCurrentProductChassisName
     */
    public String getCurrentProductChassisName() {
        if (!checkServiceOK()) {
            return null;
        }

        try {
            return DTVSettingsServer.getCurrentProductChassisName();
        } catch (RemoteException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

///////////////////////fy    2014-2-18//////////////////

    /**
     * muteVideo
     */
    public void muteVideo() {
        if (!checkServiceOK()) {
            return;
        }

//		try
//		{
//			Log.i("API API ", "fyyyyy  muteVideo ");
//			//DTVSettingsServer.hotelMuteVideo();
//		}
//		catch (RemoteException ex)
//		{
//			ex.printStackTrace();
//			Log.e(TAG, "enterCommonSetting --->>exception");
//			
//			return;
//		}
    }


    /**
     * unmuteVideo
     */
//	public void unmuteVideo(){
//		if(!checkServiceOK())
//		{
//			return;
//		}
//		
//		try
//		{
//			DTVSettingsServer.hotelUnMuteVideo();
//		}
//		catch (RemoteException ex)
//		{
//			ex.printStackTrace();
//			Log.e(TAG, "enterCommonSetting --->>exception");
//			
//			return;
//		}
    //}
    public List<Operator> getOPListByCity(String province, String city) {
        if (!checkServiceOK()) {
            Log.e(TAG, "getOPListByCity()>>service bind failed");
            return null;
        }

        try {
            return DTVSettingsServer.getOPListByCity(province, city);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int setOperator(int iOperatorCode) {
        if (!checkServiceOK()) {
            Log.e(TAG, "setOperator()>>service bind failed");
            return -1;
        }

        try {
            return DTVSettingsServer.setOperator(iOperatorCode);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * FY ADD
     * @param KeyCode
     */
    public static void SimulateKey(final int KeyCode) {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    Log.e(TAG, "sendKeyDownUpSync>>KeyCode=" + KeyCode);
                    inst.sendKeyDownUpSync(KeyCode);
                } catch (Exception e) {
                    Log.e("Exception when sendKeyDownUpSync", e.toString());
                }
            }
        }.start();
    }
}