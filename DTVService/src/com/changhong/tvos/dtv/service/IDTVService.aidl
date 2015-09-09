package com.changhong.tvos.dtv.service;


import com.changhong.tvos.dtv.vo.DTVDTTime;
import com.changhong.tvos.dtv.vo.DTVSource;
import android.os.Parcel;
import android.os.Parcelable;

interface IDTVService {
  IBinder CreateDTVPlayer( String mUuid,int  iTunerID,int  eLayerType,int iIndex,int iCLientType);
  int DestroyDTVPlayer( in IBinder obj);
  IBinder CreateChannelManager( String mUuid);
  int DestroyChannelManager( in IBinder obj);
  IBinder GetDTVSettings();
  DTVDTTime getUTCTime();
  int isCanBreakdown();
  IBinder getTimerShudle();
  int SystemReset(int type);
  int requestResource(int type, int id);
  int releaseResource(int type, int id);
  DTVSource[] getDTVSourceList();
  IBinder GetPVR();
  int getPVRStatus();
 }