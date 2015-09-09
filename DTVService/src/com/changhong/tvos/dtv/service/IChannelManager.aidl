package com.changhong.tvos.dtv.service;

import java.util.List;
import com.changhong.tvos.dtv.vo.CarrierInfo;
import com.changhong.tvos.dtv.vo.DTVChannelBaseInfo;
import com.changhong.tvos.dtv.vo.DTVChannelDetailInfo;
/*import com.changhong.tvos.dtv.vo.DTVConstant;*/
import com.changhong.tvos.dtv.vo.DVBSTransponder;
import com.changhong.tvos.dtv.vo.DfaProgressInfo;
import com.changhong.tvos.dtv.vo.SatelliteInfo;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.dtv.vo.DVBTCarrier;
import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DTVSource;
import android.os.Parcel;
import android.os.Parcelable;

interface IChannelManager {

//DVB-C
int getDVBCCarrierCount();
DVBCCarrier[] getDVBCCarrierList();
DVBCCarrier FindDVBCCarrierByFreq(int iFrequencyK);
DVBCCarrier FindDVBCCarrierByTsID(int iTsID, int iOrgTsID);
DVBCCarrier getDVBCCarrierInfo(int iCarrierIndex);
DVBCCarrier FindDVBCCarrierByChannelID(int iChannelIndex);

//DVBT
int getDVBTCarrierCount();
DVBTCarrier[] getDVBTCarrierList();
DVBTCarrier FindDVBTCarrierByFreq(int iFrequencyK);
DVBTCarrier FindDVBTCarrierByTsID(int iTsID, int iOrgTsID);
DVBTCarrier getDVBTCarrierInfo(int iCarrierIndex);
DVBTCarrier FindDVBTCarrierByChannelID(int iChannelIndex);

//DVBS
int getDVBSCarrierCount();
DVBSTransponder[] getDVBSCarrierList();
DVBSTransponder FindDVBSCarrierByFreq(int iFrequencyK);
DVBSTransponder FindDVBSCarrierByTsID(int iTsID, int iOrgTsID);
DVBSTransponder getDVBSCarrierInfo(int iCarrierIndex);
DVBSTransponder FindDVBSCarrierByChannelID(int iChannelIndex);

//DMBTH
int getDMBTHCarrierCount();
DMBTHCarrier[] getDMBTHCarrierList();
DMBTHCarrier FindDMBTHCarrierByFreq(int iFrequencyK);
DMBTHCarrier FindDMBTHCarrierByTsID(int iTsID, int iOrgTsID);
DMBTHCarrier getDMBTHCarrierInfo(int iCarrierIndex);
DMBTHCarrier FindDMBTHCarrierByChannelID(int iChannelIndex);

//
int SwapUserGroup(int iSrcIndex, int iDestIndex);
int getChannelCountByType(int iServiceType);
int getChannelCountByGroup(int iGroup);
DTVChannelBaseInfo[] getChannelListByTpye(int iServiceType);
DTVChannelBaseInfo[] getChannelListByGroup(int iGroup);
DTVChannelBaseInfo getChannelBaseInfo(int iChannelIndex);
int getChannelCountByCarrierAndType(int iCarrierIndex, int iServiceType);
DTVChannelBaseInfo[] getChannelListByCarrierAndType(int iCarrierIndex, int iServiceType);
DTVChannelDetailInfo getChanneDetailInfo(int iChannelIndex);
DTVChannelDetailInfo[] getChanneDetailInfoListByTpye(int iServiceType);
DTVChannelBaseInfo[] getFavChannelList();

int addUserGroup(String strGroupName);
int delUserGroup(int iGroup);
int addChannelToGroup(int iChannelIndex, int iGroup);
int removeChannelFromGroup(int iChannelIndex, int iGroup);
int renameGroupName(int iGroup, String strName);
String getGroupName(int iGroup);
int lockChannel(int iChannelIndex, boolean bIsLock);
int skipChannel(int iChannelIndex, boolean bIsSkip);
int setFav(int iChannelIndex, boolean bIsFav);
int updateChannelInfo(in DTVChannelBaseInfo channelInfo);
int delChannel(int iChannelIndex);
int delChannelByCarrier(int iCarrierIndex);
int clearChannelDB();
int swapChannel(int iSrcIndex, int iDestIndex);
int Flush(int iGroup);
int getChannelIndexBy3ID(int iServiceID, int iTSid, int iOrgNetId);
int SaveChannelList(in DTVChannelBaseInfo[] lchannelList);
int SortChannelList(in DTVChannelBaseInfo[] lchannelList);

//
int getSatelliteCount();
SatelliteInfo[] getSatelliteInfoList();
SatelliteInfo getSatelliteInfo(int iStateLitteIndex);

int addSatellite(in SatelliteInfo satelliteInfo);
int UpdateSatelliteInfo(in SatelliteInfo satelliteInfo);
int delSatelliteInfo(int iStateLitteIndex);
int AddTransponder(in DVBSTransponder transponderInfo);
int delTransponder(int iCarrierID);
int UpdateTransponderInfo(in DVBSTransponder transponderInfo);
DVBSTransponder getTransponderInfoByFreqAndPol(int iStateLitteIndex, int iFreqKhz, int iPol, int iFreqBiasKhz);
int getTransponderCountBySatellite(int iStateLitteIndex);
DVBSTransponder[] getTransponderListBySatellite(int iStateLitteIndex);
DVBSTransponder getNextTransponderBySatellite(int iStateLitteIndex, int iTransponderID);
DVBSTransponder getPrevTransponderBySatellite(int iStateLitteIndex, int iTransponderID);
int delAllTransponderBySatellite(int iStateLitteIndex);
int delAllChannelBySatellite(int iStateLitteIndex);
DTVChannelBaseInfo[] getAllChannelBySatellite(int iStateLitteIndex);
SatelliteInfo getSatelliteInfoByTransponder(int iCarrierID);
int setChannelSource(int iSourcetype, int iSourceID);
int setChannelSortType(int iSortType);
DTVSource[] getSourceList();

//lsy add for cloud service
int LoadOrSavedDb(String pathname, int isSave);
int getChannelListVersion();
int getDBVersion();

//pangshanbin
void setBootScanBusy(int busy);
void setBootScanFree(int free);
}