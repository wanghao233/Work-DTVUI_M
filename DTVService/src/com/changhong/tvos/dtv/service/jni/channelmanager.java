package com.changhong.tvos.dtv.service.jni;

import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DTVChannelBaseInfo;
import com.changhong.tvos.dtv.vo.DTVChannelDetailInfo;
import com.changhong.tvos.dtv.vo.DTVSource;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.dtv.vo.DVBSTransponder;
import com.changhong.tvos.dtv.vo.DVBTCarrier;
import com.changhong.tvos.dtv.vo.SatelliteInfo;

public class channelmanager {
    private static channelmanager instance = null;

    protected channelmanager() {

    }

    public static synchronized channelmanager getinstance() {
        if (instance == null)
            instance = new channelmanager();
        return instance;
    }

    //DVB-C
    public native int getDVBCCarrierCount();

    public native DVBCCarrier[] getDVBCCarrierList();

    public native DVBCCarrier FindDVBCCarrierByFreq(int iFrequencyK);

    public native DVBCCarrier FindDVBCCarrierByTsID(int iTsID, int iOrgTsID);

    public native DVBCCarrier getDVBCCarrierInfo(int iCarrierIndex);

    public native DVBCCarrier FindDVBCCarrierByChannelID(int iChannelIndex);

    //DVBT
    public native int getDVBTCarrierCount();

    public native DVBTCarrier[] getDVBTCarrierList();

    public native DVBTCarrier FindDVBTCarrierByFreq(int iFrequencyK);

    public native DVBTCarrier FindDVBTCarrierByTsID(int iTsID, int iOrgTsID);

    public native DVBTCarrier getDVBTCarrierInfo(int iCarrierIndex);

    public native DVBTCarrier FindDVBTCarrierByChannelID(int iChannelIndex);

    //DVBS
    public native int getDVBSCarrierCount();

    public native DVBSTransponder[] getDVBSCarrierList();

    public native DVBSTransponder FindDVBSCarrierByFreq(int iFrequencyK);

    public native DVBSTransponder FindDVBSCarrierByTsID(int iTsID, int iOrgTsID);

    public native DVBSTransponder getDVBSCarrierInfo(int iCarrierIndex);

    public native DVBSTransponder FindDVBSCarrierByChannelID(int iChannelIndex);

    //DMBTH
    public native int getDMBTHCarrierCount();

    public native DMBTHCarrier[] getDMBTHCarrierList();

    public native DMBTHCarrier FindDMBTHCarrierByFreq(int iFrequencyK);

    public native DMBTHCarrier FindDMBTHCarrierByTsID(int iTsID, int iOrgTsID);

    public native DMBTHCarrier getDMBTHCarrierInfo(int iCarrierIndex);

    public native DMBTHCarrier FindDMBTHCarrierByChannelID(int iChannelIndex);

    //
    public native int SwapUserGroup(int iSrcIndex, int iDestIndex);

    public native int getChannelCountByType(int iServiceType);

    public native int getChannelCountByGroup(int iGroup);

    public native DTVChannelBaseInfo[] getChannelListByTpye(int iServiceType);

    public native DTVChannelBaseInfo[] getChannelListByGroup(int iGroup);

    public native DTVChannelBaseInfo getChannelBaseInfo(int iChannelIndex);

    public native int getChannelCountByCarrierAndType(int iCarrierIndex, int iServiceType);

    public native DTVChannelBaseInfo[] getChannelListByCarrierAndType(int iCarrierIndex, int iServiceType);

    public native DTVChannelDetailInfo getChanneDetailInfo(int iChannelIndex);

    public native DTVChannelDetailInfo[] getChanneDetailInfoListByTpye(int iServiceType);

    public native DTVChannelBaseInfo[] getFavChannelList();

    public native int addUserGroup(String strGroupName);

    public native int delUserGroup(int iGroup);

    public native int addChannelToGroup(int iChannelIndex, int iGroup);

    public native int removeChannelFromGroup(int iChannelIndex, int iGroup);

    public native int renameGroupName(int iGroup, String strName);

    public native String getGroupName(int iGroup);

    public native int lockChannel(int iChannelIndex, boolean bIsLock);

    public native int skipChannel(int iChannelIndex, boolean bIsSkip);

    public native int setFav(int iChannelIndex, boolean bIsFav);

    public native int updateChannelInfo(DTVChannelBaseInfo channelInfo);

    public native int delChannel(int iChannelIndex);

    public native int delChannelByCarrier(int iCarrierIndex);

    public native int clearChannelDB();

    public native int swapChannel(int iSrcIndex, int iDestIndex);

    public native int Flush(int iGroup);

    public native int getChannelIndexBy3ID(int iServiceID, int iTSid, int iOrgNetId);

    public native int SaveChannelList(DTVChannelBaseInfo[] lchannelList);

    public native int SortChannelList(DTVChannelBaseInfo[] lchannelList);


    //
    public native int getSatelliteCount();

    public native SatelliteInfo[] getSatelliteInfoList();

    public native SatelliteInfo getSatelliteInfo(int iStateLitteIndex);

    public native int addSatellite(SatelliteInfo satelliteInfo);

    public native int UpdateSatelliteInfo(SatelliteInfo satelliteInfo);

    public native int delSatelliteInfo(int iStateLitteIndex);

    public native int AddTransponder(DVBSTransponder transponderInfo);

    public native int delTransponder(int iCarrierID);

    public native int UpdateTransponderInfo(DVBSTransponder transponderInfo);

    public native DVBSTransponder getTransponderInfoByFreqAndPol(int iStateLitteIndex, int iFreqKhz, int iPol, int iFreqBiasKhz);

    public native int getTransponderCountBySatellite(int iStateLitteIndex);

    public native DVBSTransponder[] getTransponderListBySatellite(int iStateLitteIndex);

    public native DVBSTransponder getNextTransponderBySatellite(int iStateLitteIndex, int iTransponderID);

    public native DVBSTransponder getPrevTransponderBySatellite(int iStateLitteIndex, int iTransponderID);

    public native int delAllTransponderBySatellite(int iStateLitteIndex);

    public native int delAllChannelBySatellite(int iStateLitteIndex);

    public native DTVChannelBaseInfo[] getAllChannelBySatellite(int iStateLitteIndex);

    public native SatelliteInfo getSatelliteInfoByTransponder(int iCarrierID);

    public native int setChannelSource(int iSourcetype, int iSourceID);

    public native int setChannelSortType(int iSortType);

    public native DTVSource[] GetSourceList();

    /**
     * @param pathname - db path+xx.db,download path
     * @param isSave   -[0-Load,1-save]
     * @return 0-success,1-fail
     */
    public native int LoadOrSavedDb(String pathname, int isSave);

    public native int getDBVersion();

    public native int getChannelListVersion();
}
