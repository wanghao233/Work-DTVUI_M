package com.changhong.tvos.dtv.service;

import java.util.List;
import com.changhong.tvos.dtv.vo.CarrierInfo;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.dtv.vo.DVBTCarrier;
import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DVBSTransponder;
import com.changhong.tvos.dtv.vo.DTVChannelBaseInfo;
import com.changhong.tvos.dtv.vo.DTVChannelDetailInfo;
/*import com.changhong.tvos.dtv.vo.DTVConstant;*/
import com.changhong.tvos.dtv.vo.OPFeatureInfo;
import com.changhong.tvos.dtv.vo.Operator;
import com.changhong.tvos.dtv.vo.PictureParam;
import com.changhong.tvos.dtv.vo.VersionInfo;
import com.changhong.tvos.dtv.vo.InterTVOSVersion;
import com.changhong.tvos.dtv.vo.InterPanelInfo;
import android.view.KeyEvent;
interface IDTVSettings {

String getDtvInfo(int type);

//CarrierInfo getOPDefaultFreq();
DVBCCarrier getDVBCOPDefaultFreq();
DVBSTransponder getDVBSOPDefaultFreq();
DVBTCarrier getDVBTOPDefaultFreq();
DMBTHCarrier getDMBTHOPDefaultFreq();

DVBCCarrier[] getDVBCOPMainFreqList();

OPFeatureInfo getOPFeature();

Operator[] getOPList();
Operator[] getOPListBySourceID(int sourceID);
int setOP(int iOperatorCode);
Operator getOP();
VersionInfo getVersion();
int setLanguage(String sLanguge);
String getLanguage();
int setAudioLanguage(String sLanguge);
String getAudioLanguage();
int setLocalTimeOffset(int iLocalTimeOffset);
int getLocalTimeOffset();
int setGlobalValumeFlag(boolean bGlobalValumeFlag);
int getGlobalValumeFlag();

int setVideoAspectMode(int iAspectMode);
int getVideoAspectMode();
int setVideoAspectRatio(int iAspectRatio);
int getVideoAspectRatio();
int setRFOutParam(int iFormat, int iChannelNumb);
int setRFStandby();
int setOutPorts(int iPorts);
int getOutPorts();
int setVideoOut(boolean bIsOpen);
int getVideoOut();
int setVideoOutFormat(int iPath, int outFormat);
int getVideoOutFormat(int iPath);

int setVideoPictureParam(in PictureParam pictureParam);
PictureParam getVideoPictureParam();
int setSPDIFOutMode(boolean bCompressMode);
int getSPDIFOutMode();

//int setSetLayerPriority(int[] layerList);
//int[] getSetLayerPriority();
int setSoundMode(int soundMode);
int getSoundMode();
int setScartOutMode(int scartOutMode);

int getScartOutMode();
boolean enterScreenSaver();
void enterCommonSetting();
void updateKeyboardConvertFlag(boolean flag);
boolean isKeyboardKey(int keyCode,in KeyEvent event);
boolean returnLastInputSource();
boolean startInputSource(int channelNum);

String getSWVesion();
String getPQVersion();
String getAQVersion();
String getFPVersion();
String getHWVersion();
String getDTVMWVersion();

InterTVOSVersion getTVOSVersion();
InterPanelInfo getPanelInfo();

String getCurProductType();
String getCurProductName();
String getCurrentProductChassisName();

//LSY ADD FOR CLOUD SERVICE
List<Operator> getOPListByCity(String province, String city);
int setOperator(int iOperatorCode);
boolean getSystem3D();
}