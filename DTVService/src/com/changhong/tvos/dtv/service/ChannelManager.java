package com.changhong.tvos.dtv.service;

import android.os.RemoteException;
import android.util.Log;

import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DTVChannelBaseInfo;
import com.changhong.tvos.dtv.vo.DTVChannelDetailInfo;
import com.changhong.tvos.dtv.vo.DTVSource;
import com.changhong.tvos.dtv.vo.DVBCCarrier;
import com.changhong.tvos.dtv.vo.DVBSTransponder;
import com.changhong.tvos.dtv.vo.DVBTCarrier;
import com.changhong.tvos.dtv.vo.SatelliteInfo;

public class ChannelManager extends IChannelManager.Stub {
	final static String TAG = "DtvService.ChannelManager";
	private String objmUuid;
	private int objiSourcetype;
	private int objiSourceID = 1;
	private int objiTunerID;
	private int objiSortType;
	public static int bootScan = -1;  //0 free;1 busy

	public ChannelManager(String mUuid) {
		objmUuid = mUuid;
	}

	//@Override
	public int getDVBCCarrierCount() throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getDVBCCarrierCount();

	}

	//@Override
	public DVBCCarrier[] getDVBCCarrierList() throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getDVBCCarrierList();

	}

	//@Override
	public DVBCCarrier FindDVBCCarrierByFreq(int iFrequencyK)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().FindDVBCCarrierByFreq(iFrequencyK);

	}

	//@Override
	public DVBCCarrier FindDVBCCarrierByTsID(int iTsID, int iOrgTsID)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().FindDVBCCarrierByTsID(iTsID, iOrgTsID);

	}

	//@Override
	public DVBCCarrier getDVBCCarrierInfo(int iCarrierIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getDVBCCarrierInfo(iCarrierIndex);

	}

	//@Override
	public DVBCCarrier FindDVBCCarrierByChannelID(int iChannelIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().FindDVBCCarrierByChannelID(iChannelIndex);
	}

	//@Override
	public int getDVBTCarrierCount() throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getDVBTCarrierCount();

	}

	//@Override
	public DVBTCarrier[] getDVBTCarrierList() throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getDVBTCarrierList();
	}

	//@Override
	public DVBTCarrier FindDVBTCarrierByFreq(int iFrequencyK)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().FindDVBTCarrierByFreq(iFrequencyK);
	}

	//@Override
	public DVBTCarrier FindDVBTCarrierByTsID(int iTsID, int iOrgTsID)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().FindDVBTCarrierByTsID(iTsID, iOrgTsID);
	}

	//@Override
	public DVBTCarrier getDVBTCarrierInfo(int iCarrierIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getDVBTCarrierInfo(iCarrierIndex);
	}

	//@Override
	public DVBTCarrier FindDVBTCarrierByChannelID(int iChannelIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().FindDVBTCarrierByChannelID(iChannelIndex);

	}

	//@Override
	public int getDVBSCarrierCount() throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getDVBSCarrierCount();

	}

	//@Override
	public DVBSTransponder[] getDVBSCarrierList() throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getDVBSCarrierList();
	}

	//@Override
	public DVBSTransponder FindDVBSCarrierByFreq(int iFrequencyK)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().FindDVBSCarrierByFreq(iFrequencyK);
	}

	//@Override
	public DVBSTransponder FindDVBSCarrierByTsID(int iTsID, int iOrgTsID)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().FindDVBSCarrierByTsID(iTsID, iOrgTsID);
	}

	//@Override
	public DVBSTransponder getDVBSCarrierInfo(int iCarrierIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getDVBSCarrierInfo(iCarrierIndex);

	}

	//@Override
	public DVBSTransponder FindDVBSCarrierByChannelID(int iChannelIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().FindDVBSCarrierByChannelID(iChannelIndex);
	}

	//@Override
	public int getDMBTHCarrierCount() throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getDMBTHCarrierCount();

	}

	//@Override
	public DMBTHCarrier[] getDMBTHCarrierList() throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getDMBTHCarrierList();
	}

	//@Override
	public DMBTHCarrier FindDMBTHCarrierByFreq(int iFrequencyK)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().FindDMBTHCarrierByFreq(iFrequencyK);
	}

	//@Override
	public DMBTHCarrier FindDMBTHCarrierByTsID(int iTsID, int iOrgTsID)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().FindDMBTHCarrierByTsID(iTsID, iOrgTsID);
	}

	//@Override
	public DMBTHCarrier getDMBTHCarrierInfo(int iCarrierIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getDMBTHCarrierInfo(iCarrierIndex);

	}

	//@Override
	public DMBTHCarrier FindDMBTHCarrierByChannelID(int iChannelIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().FindDMBTHCarrierByChannelID(iChannelIndex);

	}

	//@Override
	public int SwapUserGroup(int iSrcIndex, int iDestIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().SwapUserGroup(iSrcIndex, iDestIndex);

	}

	//@Override
	public int getChannelCountByType(int iServiceType) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getChannelCountByType(iServiceType);

	}

	//@Override
	public int getChannelCountByGroup(int iGroup) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getChannelCountByGroup(iGroup);

	}

	//@Override
	public DTVChannelBaseInfo[] getChannelListByTpye(int iServiceType)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getChannelListByTpye(iServiceType);
	}

	//@Override
	public DTVChannelDetailInfo[] getChanneDetailInfoListByTpye(int iServiceType)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getChanneDetailInfoListByTpye(iServiceType);
	}

	//@Override
	public DTVChannelBaseInfo[] getChannelListByGroup(int iGroup)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getChannelListByGroup(iGroup);

	}

	//@Override
	public int getChannelCountByCarrierAndType(int iCarrierIndex,
											   int iServiceType) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getChannelCountByCarrierAndType(iCarrierIndex, iServiceType);
	}

	//@Override
	public DTVChannelBaseInfo[] getChannelListByCarrierAndType(
			int iCarrierIndex, int iServiceType) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getChannelListByCarrierAndType(iCarrierIndex, iServiceType);

	}

	//@Override
	public DTVChannelBaseInfo getChannelBaseInfo(int iChannelIndex) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getChannelBaseInfo(iChannelIndex);

	}

	//@Override
	public DTVChannelDetailInfo getChanneDetailInfo(int iChannelIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		//Log.i(TAG, "enter   getChanneDetailInfo   ");
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getChanneDetailInfo(iChannelIndex);

	}

	//@Override
	public DTVChannelBaseInfo[] getFavChannelList()
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getFavChannelList();
	}

	//@Override
	public int addUserGroup(String strGroupName) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().addUserGroup(strGroupName);

	}

	//@Override
	public int delUserGroup(int iGroup) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().delUserGroup(iGroup);
	}

	//@Override
	public int addChannelToGroup(int iChannelIndex, int iGroup)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().addChannelToGroup(iChannelIndex, iGroup);
	}

	//@Override
	public int removeChannelFromGroup(int iChannelIndex, int iGroup)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().removeChannelFromGroup(iChannelIndex, iGroup);

	}

	//@Override
	public int renameGroupName(int iGroup, String strName)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().renameGroupName(iGroup, strName);

	}

	//@Override
	public String getGroupName(int iGroup) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getGroupName(iGroup);

	}

	//@Override
	public int lockChannel(int iChannelIndex, boolean bIsLock)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().lockChannel(iChannelIndex, bIsLock);
	}

	//@Override
	public int skipChannel(int iChannelIndex, boolean bIsSkip)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().skipChannel(iChannelIndex, bIsSkip);

	}

	//@Override
	public int setFav(int iChannelIndex, boolean bIsFav) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().setFav(iChannelIndex, bIsFav);

	}

	//@Override
	public int updateChannelInfo(DTVChannelBaseInfo channelInfo)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().updateChannelInfo(channelInfo);

	}

	//@Override
	public int delChannel(int iChannelIndex) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().delChannel(iChannelIndex);

	}

	//@Override
	public int delChannelByCarrier(int iCarrierIndex) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().delChannelByCarrier(iCarrierIndex);

	}

	//@Override
	public int clearChannelDB() throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().clearChannelDB();

	}

	//@Override
	public int swapChannel(int iSrcIndex, int iDestIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().swapChannel(iSrcIndex, iDestIndex);
	}

	//@Override
	public int Flush(int iGroup) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().Flush(iGroup);

	}

	//@Override
	public int getChannelIndexBy3ID(int iServiceID, int iTSid, int iOrgNetId)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getChannelIndexBy3ID(iServiceID,
				iTSid, iOrgNetId);
	}

	//@Override
	public int SaveChannelList(DTVChannelBaseInfo[] lchannelList)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().SaveChannelList(lchannelList);
	}

	//@Override
	public int getSatelliteCount() throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getSatelliteCount();

	}

	//@Override
	public int addSatellite(SatelliteInfo satelliteInfo) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().addSatellite(satelliteInfo);
	}

	//@Override
	public int UpdateSatelliteInfo(SatelliteInfo satelliteInfo)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().UpdateSatelliteInfo(satelliteInfo);
	}

	//@Override
	public int delSatelliteInfo(int iStateLitteIndex) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().delSatelliteInfo(iStateLitteIndex);

	}

	//@Override
	public int AddTransponder(DVBSTransponder transponderInfo)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().AddTransponder(transponderInfo);

	}

	//@Override
	public int delTransponder(int iCarrierID) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().delTransponder(iCarrierID);
	}

	//@Override
	public int UpdateTransponderInfo(DVBSTransponder transponderInfo)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().UpdateTransponderInfo(transponderInfo);
	}

	//@Override
	public DVBSTransponder getTransponderInfoByFreqAndPol(int iStateLitteIndex,
														  int iFreqKhz, int iPol, int iFreqBiasKhz) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getTransponderInfoByFreqAndPol(iStateLitteIndex,
				iFreqKhz, iPol, iFreqBiasKhz);

	}

	//@Override
	public int getTransponderCountBySatellite(int iStateLitteIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getTransponderCountBySatellite(iStateLitteIndex);

	}

	//@Override
	public DVBSTransponder[] getTransponderListBySatellite(
			int iStateLitteIndex) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getTransponderListBySatellite(iStateLitteIndex);

	}

	//@Override
	public DVBSTransponder getNextTransponderBySatellite(int iStateLitteIndex,
														 int iTransponderID) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getNextTransponderBySatellite(iStateLitteIndex, iTransponderID);

	}

	//@Override
	public DVBSTransponder getPrevTransponderBySatellite(int iStateLitteIndex,
														 int iTransponderID) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getPrevTransponderBySatellite(iStateLitteIndex, iTransponderID);

	}

	//@Override
	public int delAllTransponderBySatellite(int iStateLitteIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().delAllTransponderBySatellite(iStateLitteIndex);

	}

	//@Override
	public int delAllChannelBySatellite(int iStateLitteIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().delAllChannelBySatellite(iStateLitteIndex);

	}

	//@Override
	public DTVChannelBaseInfo[] getAllChannelBySatellite(
			int iStateLitteIndex) throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getAllChannelBySatellite(iStateLitteIndex);

	}

	//@Override
	public SatelliteInfo getSatelliteInfoByTransponder(int iCarrierID)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getSatelliteInfoByTransponder(iCarrierID);

	}

	//@Override
	public int setChannelSource(int iSourcetype, int iTunerID)
			throws RemoteException {
		// TODO Auto-generated method stub
		objiSourcetype = iSourcetype;
		objiTunerID = iTunerID;
		return 0;
	}

	//@Override
	public int setChannelSortType(int iSortType) throws RemoteException {
		// TODO Auto-generated method stub
		objiSortType = iSortType;
		return 0;
	}

	//@Override
	public SatelliteInfo[] getSatelliteInfoList() throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getSatelliteInfoList();
	}

	//@Override
	public SatelliteInfo getSatelliteInfo(int iStateLitteIndex)
			throws RemoteException {
		// TODO Auto-generated method stub
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().getSatelliteInfo(iStateLitteIndex);
	}

	//@Override
	public DTVSource[] getSourceList() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_channelmanager_instance().GetSourceList();

	}

	@Override
	public int SortChannelList(DTVChannelBaseInfo[] lchannelList)
			throws RemoteException {
		DTVServiceJNI.get_channelmanager_instance().setChannelSource(objiSourcetype, objiSourceID);
		return DTVServiceJNI.get_channelmanager_instance().SortChannelList(lchannelList);
	}

	@Override
	public int LoadOrSavedDb(String pathname, int isSave)
			throws RemoteException {
		return DTVServiceJNI.get_channelmanager_instance().LoadOrSavedDb(pathname, isSave);
	}

	@Override
	public int getChannelListVersion() throws RemoteException {
		// TODO Auto-generated method stub
		return DTVServiceJNI.get_channelmanager_instance().getChannelListVersion();
	}

	@Override
	public int getDBVersion() throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(TAG, "getDBVersion start");
		return DTVServiceJNI.get_channelmanager_instance().getDBVersion();
	}

	@Override
	public void setBootScanBusy(int busy) throws RemoteException {
		bootScan = busy;
	}

	@Override
	public void setBootScanFree(int free) throws RemoteException {
		// TODO Auto-generated method stub
		bootScan = free;
	}
}
