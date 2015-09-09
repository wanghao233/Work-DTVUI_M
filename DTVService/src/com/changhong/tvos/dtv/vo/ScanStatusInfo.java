/**
 * @filename DTV ����ģ�鷴����״̬��Ϣ
 * @author:
 * @date: 2012-7-16 ����״̬������״̬��������Ƶ��״̬��Ϣ�ᵽ����ȥ��
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class ScanStatusInfo implements Parcelable {
    /**  **/
    public abstract class ScanEvent {

        /**  **/
        public static final int STATUS_INIT = 0;

        public static final int STATUS_INIT_END = 1;

        /**  **/
        public static final int STATUS_NIT_BEGIN = 2;

        public static final int STATUS_NIT_DONE = 3;

        /**  **/
        public static final int STATUS_NIT_NEXT = 4;

        public static final int STATUS_TUNING_BEGIN = 5;

        /** {@link vo.DTVTunerStatus DTVTunerStatus} **/
        public static final int STATUS_TUNING_STATUS_FLUSH = 6;

        /** {@link vo.DTVChannelBaseInfo DTVChannelBaseInfo} **/
        public static final int STATUS_SERVICE_DONE = 7;

        /**  **/
        public static final int STATUS_SORTING = 8;

        /**  **/
        public static final int STATUS_SORTED = 9;

        /**  **/
        public static final int STATUS_SAVING = 10;

        /**  **/
        public static final int STATUS_SAVED = 11;

        /**  **/
        public static final int STATUS_END = 12;

        /**  **/
        public static final int STATUS_FAIL = 13;
    }

    /**
     * ̬@link ScanEvent ScanEvent}
     **/
    public int miStatus;

    /**  **/
    public int miProgress;

    /** **/
    public int miTotalCarriers;

    /**꣩**/
    public int miCurCarrierIndex;

    /**  **/
    public String mstrPrompt;

    public ScanStatusInfo() {

    }

    public ScanStatusInfo(int iStatus, int iProgress, int iTotalCarriers, int iCurCarrierIndex, String strPrompt) {
        /**
         * ̬@link ScanEvent ScanEvent}
         **/
        this.miStatus = iStatus;

        /**  **/
        this.miProgress = iProgress;

        /**  **/
        this.miTotalCarriers = iTotalCarriers;

        /** **/
        this.miCurCarrierIndex = iCurCarrierIndex;

        /** **/
        this.mstrPrompt = strPrompt;
    }


    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<ScanStatusInfo> CREATOR = new Parcelable.Creator<ScanStatusInfo>() {
        public ScanStatusInfo createFromParcel(Parcel source) {
            return new ScanStatusInfo(source);
        }

        public ScanStatusInfo[] newArray(int size) {
            return new ScanStatusInfo[size];
        }
    };

    private ScanStatusInfo(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miStatus);
        dest.writeInt(miProgress);
        dest.writeInt(miTotalCarriers);
        dest.writeInt(miCurCarrierIndex);
        dest.writeString(mstrPrompt);
    }

    public void readFromParcel(Parcel in) {
        miStatus = in.readInt();
        miProgress = in.readInt();
        miTotalCarriers = in.readInt();
        miCurCarrierIndex = in.readInt();
        mstrPrompt = in.readString();
    }
}