package com.changhong.tvos.dtv;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.changhong.tvos.dtv.service.IDTVService;
import com.changhong.tvos.dtv.service.IPVR;
import com.changhong.tvos.dtv.vo.DTVConstant;

public class PVR {
    private static final String TAG = "PVR";
    private IDTVService mDTVServer;
    public IPVR mPVR = null;
    private static PVR mInstance;

    private PVR() {
        bindService();
    }

    public static PVR getInstace() {
        if (mInstance == null) {
            mInstance = new PVR();
        }
        return mInstance;
    }

    private void bindService() {
        if (mDTVServer == null) {
            IBinder bind = ServiceManager.getService(DTVConstant.DTV_SERVICE_NAME);
            if (bind != null) {
                mDTVServer = IDTVService.Stub.asInterface(bind);
            } else {
                Log.e(TAG, "service bind failed");
            }
            if (mPVR == null) {
                try {
                    if (mDTVServer != null) {
                        IBinder binder = mDTVServer.GetPVR();
                        if (binder != null) {
                            mPVR = IPVR.Stub.asInterface(binder);
                        }
                    }
                } catch (RemoteException exception) {
                    Log.i(TAG, "IPVR binder failed!");
                }
            }
            Log.i(TAG, "[pvr]" + mPVR);
        }
    }

    public int PVR_REC_START(int hID, int ChannelID, String url, String ChannelName,
                             long sTime, long eTime) {
        try {
            return mPVR.PVR_REC_START(hID, ChannelID, url, ChannelName, sTime, eTime);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getPVRStatus() {
        int iret = -1;
        if (mPVR != null) {
            try {
                iret = mPVR.getPVRStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return iret;
    }

}
