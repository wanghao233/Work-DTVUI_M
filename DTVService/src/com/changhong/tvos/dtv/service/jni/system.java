package com.changhong.tvos.dtv.service.jni;

import com.changhong.tvos.dtv.vo.DTVDTTime;

public class system {
    private static system instance = null;

    protected system() {

    }

    public static synchronized system getinstance() {
        if (instance == null)
            instance = new system();
        return instance;
    }

    public native int createRouter(int ri_TunerID, int ri_LayerType, int ri_LayerIndex);

    public native int destroyRouter(int ri_RouterID);

    public native int dtvsystemStart(int ri_first, String param);   //

    public native int dtvsystemStop();                              //DTV

    public native int bootservice(int ri_RouterID);                 //

    public native int checkVersion(int curVersion);

    public native DTVDTTime getTDTTime();

    public native int callbackLoop();	                            //

    public native int RegisterCallback(channelInfoChangecallback objcallback);

    public native int UnRegisterCallback(channelInfoChangecallback objcallback);

    public native int setProductType(int type);                     //

    public native int setPanelInfo(int ri_width, int ri_height);    //
}
