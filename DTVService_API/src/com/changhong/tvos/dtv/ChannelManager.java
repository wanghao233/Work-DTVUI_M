/**
 * @filename
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv;

import java.util.UUID;
import com.changhong.tvos.dtv.service.IChannelManager;
import com.changhong.tvos.dtv.service.IDTVService;
import com.changhong.tvos.dtv.vo.CarrierInfo;
import com.changhong.tvos.dtv.vo.DTVChannelBaseInfo;
import com.changhong.tvos.dtv.vo.DTVChannelDetailInfo;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstDemodType;
import com.changhong.tvos.dtv.vo.DTVConstant.ErrorCode;
import com.changhong.tvos.dtv.vo.DTVSource;
import com.changhong.tvos.dtv.vo.DVBSTransponder;
import com.changhong.tvos.dtv.vo.SatelliteInfo;
import com.changhong.tvos.dtv.vo.UserGroupInfo;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.os.ServiceManager;

/**
 * ChannelManager
 */
public class ChannelManager {
    final static String TAG = "DTVAPI.ChannelManager";

    private UUID mUuid;

    private IBinder IChManangerBinder = null;
    private IChannelManager mIChManagerServer = null;
    private IDTVService mIDtvServer = null;

    /**{@link DTVConstant#ConstantDemodeType DTVConstant.ConstantDemodeType}*/
    private int miCurDemodeType;

    private int sorttype = -1;
    private int sourceType = -1;
    private int sourceIndex = -1;

    public CarrierInfoManager carrierMananger = new CarrierInfoManager();
    public ChannelInfoManager channelMananger = new ChannelInfoManager();
    public SatelliteManager satelliteMananger = new SatelliteManager();

    /**
     * checkSericeOK
     * @return
     */
    private boolean checkSericeOK() {
        if ((IChManangerBinder != null) && !IChManangerBinder.isBinderAlive()) {//service
            mIDtvServer = null;
            mIChManagerServer = null;
            IChManangerBinder = null;
        }

        if (mIDtvServer == null) {
            IBinder bind = ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME);
            if (bind != null) {
                mIDtvServer = IDTVService.Stub.asInterface(bind);
            } else {
                return false;
            }

        }

        if (mIChManagerServer == null) {
            try {
                IChManangerBinder = mIDtvServer.CreateChannelManager(mUuid.toString());
                if (IChManangerBinder != null) {
                    mIChManagerServer = IChannelManager.Stub.asInterface(IChManangerBinder);

                    if (sorttype != -1) {
                        setChannelSortType(sorttype);
                    }

                    if ((sourceType != -1) && (sourceIndex != -1)) {
                        setChannelSource(sourceType, sourceIndex);
                    }

                    return true;
                }

                return false;
            } catch (RemoteException exception) {
                return false;
            }
        }


        return true;
    }

    /**
     * ChannelManager
     * @param context
     */
    public ChannelManager(Context context) {
        mUuid = UUID.randomUUID();
        miCurDemodeType = ConstDemodType.DVB_C;

        IBinder bind = ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME);
        if (bind != null) {
            mIDtvServer = IDTVService.Stub.asInterface(bind);
            try {
                IChManangerBinder = mIDtvServer.CreateChannelManager(mUuid.toString());
            } catch (RemoteException exception) {
                return;
            }

            if (IChManangerBinder != null) {
                mIChManagerServer = IChannelManager.Stub.asInterface(IChManangerBinder);
            }
        } else {
            Log.e(TAG, "service bind filed");
        }
    }


    /**
     * release
     * @return
     */
    public int release() {
        if (!checkSericeOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            mIDtvServer.DestroyChannelManager(IChManangerBinder);
            IChManangerBinder = null;
            mIChManagerServer = null;
            return DTVConstant.DTV_OK;
        } catch (RemoteException ex) {
            Log.e(TAG, "release exception");
            ex.printStackTrace();
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }


    /**
     * ChannelInfoManager
     */
    public class ChannelInfoManager {

        /**
         * getUserGroupList
         */
        public UserGroupInfo[] getUserGroupList() {
            if (!checkSericeOK()) {
                return null;
            }

/**
 try
 {
 return mIChManagerServer.getUserGroupList();
 }
 catch (RemoteException exception)
 {
 return null;
 }
 **/
            return null;
        }

        /**
         * SwapUserGroup
         */
        public int SwapUserGroup(int iSrcIndex, int iDestIndex) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.SwapUserGroup(iSrcIndex, iDestIndex);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * getChannelCountByType
         */
        public int getChannelCountByType(int iServiceType) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.getChannelCountByType(iServiceType);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * getChannelCountByGroup
         */
        public int getChannelCountByGroup(int iGroup) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.getChannelCountByGroup(iGroup);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * getChannelListByTpye
         */
        public DTVChannelBaseInfo[] getChannelListByTpye(int iServiceType) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getChannelListByTpye(iServiceType);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * getChannelListByGroup
         */
        public DTVChannelBaseInfo[] getChannelListByGroup(int iGroup) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getChannelListByGroup(iGroup);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * getFavChannelList
         */
        public DTVChannelBaseInfo[] getFavChannelList() {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getFavChannelList();
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * getChannelCountByCarrierAndType
         */
        public int getChannelCountByCarrierAndType(int iCarrierIndex, int iServiceType) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.getChannelCountByCarrierAndType(iCarrierIndex, iServiceType);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * getChannelListByCarrierAndType
         */
        public DTVChannelBaseInfo[] getChannelListByCarrierAndType(int iCarrierIndex, int iServiceType) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getChannelListByCarrierAndType(iCarrierIndex, iServiceType);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * getChannelBaseInfo
         */
        public DTVChannelBaseInfo getChannelBaseInfo(int iChannelIndex) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getChannelBaseInfo(iChannelIndex);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * getChanneDetailInfo
         */
        public DTVChannelDetailInfo getChanneDetailInfo(int iChannelIndex) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getChanneDetailInfo(iChannelIndex);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * getChanneDetailInfoListByTpye
         */
        public DTVChannelDetailInfo[] getChanneDetailInfoListByTpye(int iServiceType) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getChanneDetailInfoListByTpye(iServiceType);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * addUserGroup
         */
        public int addUserGroup(String strGroupName) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.addUserGroup(strGroupName);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * delUserGroup
         */
        public int delUserGroup(int iGroup) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.delUserGroup(iGroup);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * addChannelToGroup
         */
        public int addChannelToGroup(int iChannelIndex, int iGroup) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.addChannelToGroup(iChannelIndex, iGroup);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * removeChannelFromGroup
         */
        public int removeChannelFromGroup(int iChannelIndex, int iGroup) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.removeChannelFromGroup(iChannelIndex, iGroup);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * renameGroupName
         */
        public int renameGroupName(int iGroup, String strName) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.renameGroupName(iGroup, strName);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * getGroupName
         */
        public String getGroupName(int iGroup) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getGroupName(iGroup);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * lockChannel
         */
        public int lockChannel(int iChannelIndex, boolean bIsLock) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.lockChannel(iChannelIndex, bIsLock);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * skipChannel
         */
        public int skipChannel(int iChannelIndex, boolean bIsSkip) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.skipChannel(iChannelIndex, bIsSkip);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * setFav
         */
        public int setFav(int iChannelIndex, boolean bIsFav) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.setFav(iChannelIndex, bIsFav);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * updateChannelInfo
         */
        public int updateChannelInfo(DTVChannelBaseInfo channelInfo) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.updateChannelInfo(channelInfo);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * delChannel
         */
        public int delChannel(int iChannelIndex) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.delChannel(iChannelIndex);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * delChannelByCarrier
         */
        public int delChannelByCarrier(int iCarrierIndex) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.delChannelByCarrier(iCarrierIndex);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }


        /**
         * clearChannelDB
         */
        public int clearChannelDB() {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.clearChannelDB();
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * swapChannel
         */
        public int swapChannel(int iSrcIndex, int iDestIndex) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.swapChannel(iSrcIndex, iDestIndex);
            } catch (RemoteException exception) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * Flush
         */
        public int Flush(int iGroup) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.Flush(iGroup);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         *  getChannelIndexBy3ID
         */
        public int getChannelIndexBy3ID(int iServiceID, int iTSid, int iOrgNetId) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.getChannelIndexBy3ID(iServiceID, iTSid, iOrgNetId);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * SaveChannelList
         **/
        public int SaveChannelList(DTVChannelBaseInfo[] lchannelList) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.SaveChannelList(lchannelList);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * SortChannelList
         **/
        public int SortChannelList(DTVChannelBaseInfo[] lchannelList) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.SortChannelList(lchannelList);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }
    }

    /**
     * CarrierInfoManager
     */
    public class CarrierInfoManager {
        /**
         * getCarrierCount
         **/
        public int getCarrierCount() {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                if (miCurDemodeType == ConstDemodType.DVB_C) {
                    return mIChManagerServer.getDVBCCarrierCount();
                } else if (miCurDemodeType == ConstDemodType.DVB_S) {
                    return mIChManagerServer.getDVBSCarrierCount();
                } else if (miCurDemodeType == ConstDemodType.DMB_TH) {
                    return mIChManagerServer.getDMBTHCarrierCount();
                } else if (miCurDemodeType == ConstDemodType.DVB_T) {
                    return mIChManagerServer.getDVBTCarrierCount();
                } else {
                    return ErrorCode.ERROR_NOT_SUPPORT;
                }
            } catch (RemoteException exception) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * getCarrierList
         **/
        public CarrierInfo[] getCarrierList() {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                if (miCurDemodeType == ConstDemodType.DVB_C) {
                    return (CarrierInfo[]) mIChManagerServer.getDVBCCarrierList();
                } else if (miCurDemodeType == ConstDemodType.DVB_S) {
                    return (CarrierInfo[]) mIChManagerServer.getDVBSCarrierList();
                } else if (miCurDemodeType == ConstDemodType.DMB_TH) {
                    return (CarrierInfo[]) mIChManagerServer.getDMBTHCarrierList();
                } else if (miCurDemodeType == ConstDemodType.DVB_T) {
                    return (CarrierInfo[]) mIChManagerServer.getDVBTCarrierList();
                } else {
                    return null;
                }
            } catch (RemoteException exception) {
                return null;
            }
        }

        /**
         * FindCarrierByFreq
         **/
        public CarrierInfo FindCarrierByFreq(int iFrequencyK) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                if (miCurDemodeType == ConstDemodType.DVB_C) {
                    return (CarrierInfo) mIChManagerServer.FindDVBCCarrierByFreq(iFrequencyK);
                } else if (miCurDemodeType == ConstDemodType.DVB_S) {
                    return (CarrierInfo) mIChManagerServer.FindDVBSCarrierByFreq(iFrequencyK);
                } else if (miCurDemodeType == ConstDemodType.DMB_TH) {
                    return (CarrierInfo) mIChManagerServer.FindDMBTHCarrierByFreq(iFrequencyK);
                } else if (miCurDemodeType == ConstDemodType.DVB_T) {
                    return (CarrierInfo) mIChManagerServer.FindDVBTCarrierByFreq(iFrequencyK);
                } else {
                    return null;
                }
            } catch (RemoteException exception) {
                return null;
            }
        }

        /**
         * FindCarrierByTsID
         **/
        public CarrierInfo FindCarrierByTsID(int iTsID, int iOrgTsID) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                if (miCurDemodeType == ConstDemodType.DVB_C) {
                    return (CarrierInfo) mIChManagerServer.FindDVBCCarrierByTsID(iTsID, iOrgTsID);
                } else if (miCurDemodeType == ConstDemodType.DVB_S) {
                    return (CarrierInfo) mIChManagerServer.FindDVBSCarrierByTsID(iTsID, iOrgTsID);
                } else if (miCurDemodeType == ConstDemodType.DMB_TH) {
                    return (CarrierInfo) mIChManagerServer.FindDMBTHCarrierByTsID(iTsID, iOrgTsID);
                } else if (miCurDemodeType == ConstDemodType.DVB_T) {
                    return (CarrierInfo) mIChManagerServer.FindDVBTCarrierByTsID(iTsID, iOrgTsID);
                } else {
                    return null;
                }
            } catch (RemoteException exception) {
                return null;
            }
        }

        /**
         * getCarrierInfo
         **/
        public CarrierInfo getCarrierInfo(int iCarrierIndex) {
            if (!checkSericeOK()) {
                Log.e(TAG, "dtvapi>>getCarrierInfo()>> checkSericeOK" + checkSericeOK());
                return null;
            }

            try {
                if (miCurDemodeType == ConstDemodType.DVB_C) {
                    return (CarrierInfo) mIChManagerServer.getDVBCCarrierInfo(iCarrierIndex);
                } else if (miCurDemodeType == ConstDemodType.DVB_S) {
                    return (CarrierInfo) mIChManagerServer.getDVBSCarrierInfo(iCarrierIndex);
                } else if (miCurDemodeType == ConstDemodType.DMB_TH) {
                    return (CarrierInfo) mIChManagerServer.getDMBTHCarrierInfo(iCarrierIndex);
                } else if (miCurDemodeType == ConstDemodType.DVB_T) {
                    return (CarrierInfo) mIChManagerServer.getDVBTCarrierInfo(iCarrierIndex);
                } else {
                    return null;
                }
            } catch (RemoteException exception) {
                return null;
            }
        }

        /**
         * FindCarrierByChannelID
         **/
        public CarrierInfo FindCarrierByChannelID(int iChannelIndex) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                if (miCurDemodeType == ConstDemodType.DVB_C) {
                    return (CarrierInfo) mIChManagerServer.FindDVBCCarrierByChannelID(iChannelIndex);
                } else if (miCurDemodeType == ConstDemodType.DVB_S) {
                    return (CarrierInfo) mIChManagerServer.FindDVBSCarrierByChannelID(iChannelIndex);
                } else if (miCurDemodeType == ConstDemodType.DMB_TH) {
                    return (CarrierInfo) mIChManagerServer.FindDMBTHCarrierByChannelID(iChannelIndex);
                } else if (miCurDemodeType == ConstDemodType.DVB_T) {
                    return (CarrierInfo) mIChManagerServer.FindDVBTCarrierByChannelID(iChannelIndex);
                } else {
                    return null;
                }
            } catch (RemoteException exception) {
                return null;
            }
        }
    }

    /**
     * SatelliteManager
     */
    public class SatelliteManager {
        /**
         * getSatelliteCount
         **/
        public int getSatelliteCount() {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.getSatelliteCount();
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * getSatelliteInfoList
         **/
        public SatelliteInfo[] getSatelliteInfoList() {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getSatelliteInfoList();
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }


        /**
         * getSatelliteInfo
         **/
        public SatelliteInfo getSatelliteInfo(int iStateLitteIndex) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getSatelliteInfo(iStateLitteIndex);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * addSatellite
         **/
        public int addSatellite(SatelliteInfo satelliteInfo) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.addSatellite(satelliteInfo);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * UpdateSatelliteInfo
         **/
        public int UpdateSatelliteInfo(SatelliteInfo satelliteInfo) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.UpdateSatelliteInfo(satelliteInfo);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * delSatelliteInfo
         **/
        public int delSatelliteInfo(int iStateLitteIndex) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.delSatelliteInfo(iStateLitteIndex);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * addTransponder
         **/
        public int addTransponder(DVBSTransponder transponderInfo) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.AddTransponder(transponderInfo);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * delTransponder
         **/
        public int delTransponder(int iCarrierID) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.delTransponder(iCarrierID);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * UpdateTransponderInfo
         **/
        public int UpdateTransponderInfo(DVBSTransponder transponderInfo) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.UpdateTransponderInfo(transponderInfo);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * getTransponderInfoByFreqAndPol
         **/
        public DVBSTransponder getTransponderInfoByFreqAndPol(int iStateLitteIndex, int iFreqKhz, int iPol, int iFreqBiasKhz) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getTransponderInfoByFreqAndPol(iStateLitteIndex, iFreqKhz, iPol, iFreqBiasKhz);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * getTransponderCountBySatellite
         **/
        public int getTransponderCountBySatellite(int iStateLitteIndex) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.getTransponderCountBySatellite(iStateLitteIndex);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * getTransponderListBySatellite
         **/
        public DVBSTransponder[] getTransponderListBySatellite(int iStateLitteIndex) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getTransponderListBySatellite(iStateLitteIndex);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * getNextTransponderBySatellite
         **/
        public DVBSTransponder getNextTransponderBySatellite(int iStateLitteIndex, int iTransponderID) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getNextTransponderBySatellite(iStateLitteIndex, iTransponderID);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * getPrevTransponderBySatellite
         **/
        public DVBSTransponder getPrevTransponderBySatellite(int iStateLitteIndex, int iTransponderID) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getPrevTransponderBySatellite(iStateLitteIndex, iTransponderID);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * delAllTransponderBySatellite
         **/
        public int delAllTransponderBySatellite(int iStateLitteIndex) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.delAllTransponderBySatellite(iStateLitteIndex);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * delAllChannelBySatellite
         **/
        public int delAllChannelBySatellite(int iStateLitteIndex) {
            if (!checkSericeOK()) {
                return ErrorCode.ERROR_BINDER_FAILD;
            }

            try {
                return mIChManagerServer.delAllChannelBySatellite(iStateLitteIndex);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return ErrorCode.ERROR_BINDER_FAILD;
            }
        }

        /**
         * getAllChannelBySatellite
         **/
        public DTVChannelBaseInfo[] getAllChannelBySatellite(int iStateLitteIndex) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getAllChannelBySatellite(iStateLitteIndex);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * getSatelliteInfoByTransponder
         **/
        public SatelliteInfo getSatelliteInfoByTransponder(int iCarrierID) {
            if (!checkSericeOK()) {
                return null;
            }

            try {
                return mIChManagerServer.getSatelliteInfoByTransponder(iCarrierID);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }


    /**
     * getSourceList
     */
    public DTVSource[] getSourceList() {
        if (!checkSericeOK()) {
            return null;
        }

        try {
            return mIChManagerServer.getSourceList();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getSourceList exception");

            return null;
        }
    }

    /**
     * setChannelSource
     */
    public int setChannelSource(int iSourcetype, int iSourceID) {
        sourceType = iSourcetype;
        sourceIndex = iSourceID;
        if (!checkSericeOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return mIChManagerServer.setChannelSource(iSourcetype, iSourceID);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }

    /**
     * setChannelSortType
     **/
    public int setChannelSortType(int iSortType) {

        sorttype = iSortType;

        if (!checkSericeOK()) {
            return ErrorCode.ERROR_BINDER_FAILD;
        }

        try {
            return mIChManagerServer.setChannelSortType(iSortType);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return ErrorCode.ERROR_BINDER_FAILD;
        }
    }


    public int LoadOrSavedDb(String pathname, int isSave) {
        if (!checkSericeOK()) {
            return -1;
        }

        try {
            return mIChManagerServer.LoadOrSavedDb(pathname, isSave);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "LoadOrSavedDb exception");

            return -1;
        }
    }

    public int getChannelListVersion() {
        if (!checkSericeOK()) {
            return -1;
        }

        try {
            return mIChManagerServer.getChannelListVersion();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getChannelListVersion exception");

            return -1;
        }
    }

    public int getDBVersion() {
        if (!checkSericeOK()) {
            return -1;
        }

        try {
            return mIChManagerServer.getDBVersion();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Log.e(TAG, "getDBVersion exception");

            return -1;
        }
    }


    public void setBootScanBusy(int busy) {
        if (!checkSericeOK()) {
            Log.i(TAG, "[setBootScanBusy service not ok]");
            return;
        }
        try {
            mIChManagerServer.setBootScanBusy(busy);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }


    public void setBootScanFree(int free) {
        if (!checkSericeOK()) {
            Log.i(TAG, "[setBootScanFree service not ok]");
            return;
        }
        try {
            mIChManagerServer.setBootScanFree(free);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
}