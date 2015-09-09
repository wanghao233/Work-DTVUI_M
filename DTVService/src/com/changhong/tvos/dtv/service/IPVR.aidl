package com.changhong.tvos.dtv.service;

interface IPVR {
	int getStatus();
	int getLength();
	int PVR_REC_START(int hID,int ChannelID,String url,String ChannelName,
	long sTime,long eTime);
	int PVR_REC_PAUSE(int ChannelID);
  	int PVR_REC_STOP(int ChannelID);
  	int PVRSTOP();
  	int write(in byte[] buffer);
  	ParcelFileDescriptor getFileDescriptor();
  	void refreshRecBuffer(int ChannelID);
  	int getPVRStatus();
}