package com.changhong.tvos.dtv.service;

import java.util.List;
import com.changhong.tvos.dtv.vo.CICAMInformation;
import com.changhong.tvos.dtv.vo.EPGEvent;
import com.changhong.tvos.dtv.vo.DTVDTTime;
import com.changhong.tvos.dtv.vo.AudioTrack;
import com.changhong.tvos.dtv.vo.DTVVideoInfo;
import com.changhong.tvos.dtv.vo.CarrierInfo;
import com.changhong.tvos.dtv.vo.DTVChannelBaseInfo;
import com.changhong.tvos.dtv.vo.DTVChannelDetailInfo;
import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.dtv.vo.DVBSTransponder;
import com.changhong.tvos.dtv.vo.DVBTCarrier;
import com.changhong.tvos.dtv.vo.SatelliteInfo;
import com.changhong.tvos.dtv.vo.DTVTunerStatus;
import com.changhong.tvos.dtv.vo.DfaMessageBase;
import com.changhong.tvos.dtv.vo.NvodRefService;
import com.changhong.tvos.dtv.vo.NvodRefEvent;
import com.changhong.tvos.dtv.vo.NvodShiftEvent;
import com.changhong.tvos.dtv.vo.DTVCardStatus;
import com.changhong.tvos.dtv.vo.DTVSource;
import android.os.Parcel;
import android.os.Parcelable;

interface IDTVPlayer {
//nvod
		 int nvodstart();
		 int nvodstop();
		 int nvodsuspend();
		 int nvodresume();
		 NvodRefService[] getRefServices();
		 NvodRefEvent[] getRefEvents(int serviceId);
		 NvodShiftEvent[] getShiftEvents(int serviceId,int refEventId);

//EPG
int epgStart();
int epgStop();
int epgSuspend();
int epgResume();

EPGEvent[] getPFEvent(int iChannelIndex);
EPGEvent[] getSchelueEvent(int iChannelIndex);
EPGEvent[] getSchelueEventByTime(int channelIndex,in DTVDTTime startTime,in DTVDTTime endTime);

String getPFEventExtendInfo(int iChannelIndex, int iEventID);
String getSchelueEventExtendInfo(int iChannelIndex, int iEventID);
EPGEvent[] getSchelueEventWithStamp(int iChannelIndex,out DTVDTTime time);

//Scan
//int setParam(int iScanMode, in CarrierInfo[] lFreqList);
int SetDVBCParam(int iScanMode,in DVBCCarrier[] lFreqList);
int SetDVBSParam(int iScanMode,in DVBSTransponder[] lFreqList);
int SetDVBTParam(int iScanMode,in DVBTCarrier[] lFreqList);
int SetDMBTHParam(int iScanMode,in DMBTHCarrier[] lFreqList);

int setSatellite(int iSatelliteID);

int scanStart();
int scanStop();
//int scanCancel();

//Play
int setSwitchMode(int iSwitchMode);
int setVolume(int volume);
int getVolume();
int play(int iChannelIndex);
int playvod(int iFrequencyK, int iSymbolRateK, int eQamMode, int iServiceID, int PMTPID);
int playStop();

int Pausevideo();
int Resumevideo();
int PauseAudio();
int ResumeAudio();
AudioTrack getChannelAudioTrack();
int setChannelAudioTrack(int iAudioTrack);
DTVVideoInfo getVideoInfo();
int clearVideoBuffer();
int getPlayingProgramID();
int setPlayingProgramID(int nProgramID);
int getLastProgramID();
int isPlaying();
//CI/CAM
CICAMInformation getInfo();
String getSmartCardID();

DTVCardStatus getCardStatus();

int clearUserData();
void queryControl(int iMsgType, int iMsgID, int iMenuID,int operand, int opcode, int defOpcode, int inputItems,in String[] inputList);

//DFA
int DFAControl(int iType, int iMsgID,int iOperand, int iOpcode, in String[] strInputList);

//
int start();
int checkVersion(int curVersion);
DTVDTTime getTDTTime();
int prepare();
int Release();

//int SetTuner(in CarrierInfo carrierInfo);
int SetDVBCTuner(in DVBCCarrier carrierInfo);
int SetDVBSTuner(in DVBSTransponder carrierInfo);
int SetDVBTTuner(in DVBTCarrier carrierInfo);
int SetDMBTHTuner(in DMBTHCarrier carrierInfo);

DTVTunerStatus getTunerStatus(); 

//CarrierInfo getCurTunerInfo();
DVBCCarrier getDVBCCurTunerInfo();
DVBSTransponder getDVBSCurTunerInfo();
DVBTCarrier getDVBTCurTunerInfo();
DMBTHCarrier getDMBTHCurTunerInfo();

int	setSource(int iSourceID);
DTVSource	getSource();
int getSourceType();
int getProductType();

int SmartSkip(int iType);
int SmartSkipStop();	
int SetDtvBusyState(int isBusyState);
int ReleaseForBack();

int getPlayerStatus();

void SetDtvUIActivityState(int iState);
////////////////Hotel mode func////////////fy 2014-2-25
void setHotelMode(boolean bHotelMode);
boolean getHotelMode();

int getHotelPowerOnMode();
void setHotelPowerOnMode(int iPowerOnMode);

int getHotelPowerOnSource();
void setHotelPowerOnSource(int iPowerOnSource);

void setHotelMaxVolume(int iMaxVolume);
int getHotelMaxVolume();

int getHotelPowerOnVolume();
void setHotelPowerOnVolume(int iPowerOnVolume);

int getHotelPowerOnChannel();
void setHotelPowerOnChannel(int iPowerOnChannel);

boolean getHotelLocalKeyLockFlag();
void setHotelLocalKeyLockFlag(boolean bLocalKeyLockFlag);

boolean getHotelTuneLockFlag();
void setHotelTuneLockFlag(boolean bTuneLockFlag);

int getHotelMusicMode();
void setHotelMusicMode(int iMusicMode);

boolean getHotelAutoPresetFlag();
void setHotelAutoPresetFlag(boolean bAutoPresetFlag);
}