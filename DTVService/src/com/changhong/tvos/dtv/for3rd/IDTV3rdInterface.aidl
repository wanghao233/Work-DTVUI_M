package com.changhong.tvos.dtv.for3rd;

import android.os.Parcel;
import android.os.Parcelable;
import com.changhong.tvos.dtv.for3rd.InterChannelInfo;
import com.changhong.tvos.dtv.for3rd.InterTunerStatus;
import com.changhong.tvos.dtv.for3rd.InterAudioTrack;
import com.changhong.tvos.dtv.for3rd.InterEPGEvent;
import com.changhong.tvos.dtv.for3rd.InterVersionInfo;
import com.changhong.tvos.dtv.for3rd.InterCardStatus;
import com.changhong.tvos.dtv.for3rd.InterOperator;
import com.changhong.tvos.dtv.for3rd.InterDTVSource;
import com.changhong.tvos.dtv.for3rd.InterChDetailInfo;
import com.changhong.tvos.dtv.for3rd.InterUTCDate;
import com.changhong.tvos.dtv.for3rd.InterUTCTime;
import com.changhong.tvos.dtv.for3rd.InterTimerInfo;
import com.changhong.tvos.dtv.for3rd.InterDTVChannelBaseInfo;

interface IDTV3rdInterface {
  List<InterChannelInfo> getChannelList();
  int	getChannelCount();
  List<InterChannelInfo> getChannelListByType(int itype);
  int prepare();
  int Release();
  int ReleaseForBack();
  int play(int channelIndex);
  int stop();
  int isPlaying();
  InterTunerStatus getTunerStatus();
  InterAudioTrack getAudioTrack();
  int setAudioTrack(int audioTrack);
  List<InterEPGEvent> getEPGPFEvent(int channelIndex);
  List<InterEPGEvent> getEPGSchelueEvent(int channelIndex);
  String getEPGPFEventExtendInfo(int channelIndex, int eventID);
  String getEPGSchelueEventExtendInfo(int channelIndex, int eventID);
  boolean isDTVBusy();
  int getDTVTime();
  InterVersionInfo getDTVSwVersion();
  int getDTVLastChannel();
  int getDTVCurrentChannel();
  InterCardStatus getCardStatus();
  InterOperator getOperator();
  InterChannelInfo getChannelInfo(int channelIndex);
  int SystemReset(int type);
  int requestResource(int type, int id);
  int releaseResource(int type, int id);
  List<InterDTVSource> getDTVSourceList();
  int getCurSourceID();
  InterChDetailInfo getChannelDetailInfo(int channelIndex);
  InterDTVChannelBaseInfo getChannelBaseInfo(int channelIndex);
  List<InterDTVChannelBaseInfo> getChannelListByTpye(int iServiceType);
  List<InterEPGEvent> getSchelueEventWithStamp(int iChannelIndex,out InterUTCDate date, out InterUTCTime time);
  
  InterTimerInfo getTimerInfo(
					long start,long end,int original,
					int sourceID,int serviceIndex,String url,
					int programNum,String programName,String eventName);
  int addTimer(in InterTimerInfo timer);
  int deleteTimer(in InterTimerInfo timer);
  List<InterTimerInfo> getTimerList(int type);
  int deleteAllTimer();
  
  //lsy add for cloud service
  int LoadOrSavedDb(String pathname, int isSave);
  int getDBVersion();
  int getChannelListVersion();
  List<InterOperator> getOPListByCity(String province, String city);
  int setOP(int iOperatorCode);

  int getPVRStatus();
  int getLength();
  int PVR_REC_START(int hID,int ChannelID,String url,String ChannelName,
	long sTime,long eTime);
  int PVR_REC_STOP(int ChannelID);
  int PVRSTOP();
  int write(in byte[] buffer);
  ParcelFileDescriptor getFileDescriptor();
  void refreshRecBuffer(int ChannelID);
}