package com.changhong.tvos.dtv.service.jni;

import android.util.Log;

public class DFA {
    public static final String TAG = "dtvservice.DFA";
    private static DFA instance = null;

    protected DFA() {

    }

    public static synchronized DFA getinstance() {
        if (instance == null)
            instance = new DFA();
        return instance;
    }

    /**
     * @param robj_Para
     * @return
     */
    public native int JNI_DFA_Init(DFAParams robj_Para);

    /**
     * @param type    1：DFA_TRIGGER; 2：DFA_PROGRESS
     * @param msgID
     * @param operand CHMID_OTA_NIT_DESC_STRUCT
     * @param opcode  OK；DEFAULT_OP_CODE_OK;DEFAULT_OP_CODE_EIXT
     * @param Items
     * @param List
     * @return
     */
    public native int JNI_DFA_Control(int type, int msgID, int operand, int opcode, int Items, String[] List);

    /**
     * @return
     */
    public native int JNI_DFA_Term();

    static {
        try {
            Log.i(TAG, "chdtv start");
            System.loadLibrary("chdtv");
            Log.i(TAG, "chdtv success");
            System.loadLibrary("DTVServiceJNI");
            Log.i(TAG, "servicejni success");
        } catch (Exception e) {
            Log.i(TAG, "e->" + e.getMessage());
        }

    }
}
