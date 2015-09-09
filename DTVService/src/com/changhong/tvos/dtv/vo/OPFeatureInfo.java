/**
 * @filename DTV ��Ӫ��������Ϣ
 * @author:
 * @date:
 * @version 0.1
 * @history: 2012-9-11 	���ӿ�����Ŀ
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class OPFeatureInfo implements Parcelable {
    /**  **/
    public boolean mbHideManualScan; // 1. if hide manual scan in menu
    /**  **/
    public boolean mbHideListScan; // 2. if hide list scan in menu
    /**  **/
    public boolean mbEditListScan; // 3. if user can edit the param in list menu
    /** EPG **/
    public boolean mbEpgOnlyOneFreq; // 4. schedule epg is only in one freq or
    // each freq
    /** EPG **/
    public int mEpgFreq; // 5. if schedule epg is only in one
    public int[] mMainFreqList;

    public boolean mbBanlanceVolByRatio; // 5. if balance volume is in ratio
    /**  **/
    public boolean mbScanShowFreq; // 6. if show freq when scan
    /**  **/
    public boolean mbScanShowServiceCountByCarrier; // 7. if show carrier
    // service count when scan
    /**  **/
    public boolean mbScanShowTotalServiceCount; // 8. if show total service
    // count when scan
    /**  **/
    public boolean mbShowScanMenuAfterAutoScanFail; // 10.if go to manual scan
    // menu when auto scan fail
    /**  **/
    public boolean mbUseLogicNumber; // 11.use logice number

    /**  **/
    public boolean mbForbidenConflictEvent; // 12.forbidenConflictEvent

    /**  **/
    public int miStartProgramIndex; // 13.startPlayProgram

    public OPFeatureInfo(
            boolean mbHideManualScan,
            boolean mbHideListScan,
            boolean mbEditListScan,
            boolean mbEpgOnlyOneFreq,
            int mEpgFreq,
            int[] mMainFreqList,
            boolean mbBanlanceVolByRatio,
            boolean mbScanShowFreq,
            boolean mbScanShowServiceCountByCarrier,
            boolean mbScanShowTotalServiceCount,
            boolean mbShowScanMenuAfterAutoScanFail,
            boolean mbUseLogicNumber,
            boolean mbForbidenConflictEvent,
            int miStartProgramIndex) {
        this.mbHideManualScan = mbHideManualScan;
        this.mbHideListScan = mbHideListScan;
        this.mbEditListScan = mbEditListScan;
        this.mbEpgOnlyOneFreq = mbEpgOnlyOneFreq;
        this.mEpgFreq = mEpgFreq;
        this.mMainFreqList = mMainFreqList;
        this.mbBanlanceVolByRatio = mbBanlanceVolByRatio;
        this.mbScanShowFreq = mbScanShowFreq;
        this.mbScanShowServiceCountByCarrier = mbScanShowServiceCountByCarrier;
        this.mbScanShowTotalServiceCount = mbScanShowTotalServiceCount;
        this.mbShowScanMenuAfterAutoScanFail = mbShowScanMenuAfterAutoScanFail;
        this.mbUseLogicNumber = mbUseLogicNumber;
        this.mbForbidenConflictEvent = mbForbidenConflictEvent;
        this.miStartProgramIndex = miStartProgramIndex;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<OPFeatureInfo> CREATOR = new Parcelable.Creator<OPFeatureInfo>() {
        public OPFeatureInfo createFromParcel(Parcel source) {
            return new OPFeatureInfo(source);
        }

        public OPFeatureInfo[] newArray(int size) {
            return new OPFeatureInfo[size];
        }
    };

    private OPFeatureInfo(Parcel source) {
        readFromParcel(source);
    }

    public void writeToParcel(Parcel dest, int arg1) {

        dest.writeInt(mbHideManualScan == true ? 1 : 0);
        dest.writeInt(mbHideListScan == true ? 1 : 0);
        dest.writeInt(mbEditListScan == true ? 1 : 0);

        dest.writeInt(mbEpgOnlyOneFreq == true ? 1 : 0);
        dest.writeInt(mEpgFreq);
        if (mMainFreqList != null) {
            dest.writeInt(mMainFreqList.length);
            dest.writeIntArray(mMainFreqList);
        } else {
            dest.writeInt(0);
        }

        dest.writeInt(mbBanlanceVolByRatio == true ? 1 : 0);
        dest.writeInt(mbScanShowFreq == true ? 1 : 0);
        dest.writeInt(mbScanShowServiceCountByCarrier == true ? 1 : 0);

        dest.writeInt(mbScanShowTotalServiceCount == true ? 1 : 0);
        dest.writeInt(mbShowScanMenuAfterAutoScanFail == true ? 1 : 0);
        dest.writeInt(mbUseLogicNumber == true ? 1 : 0);
        dest.writeInt(miStartProgramIndex);
    }

    public void readFromParcel(Parcel in) {
        mbHideManualScan = in.readInt() == 1 ? true : false;
        mbHideListScan = in.readInt() == 1 ? true : false;
        mbEditListScan = in.readInt() == 1 ? true : false;

        mbEpgOnlyOneFreq = in.readInt() == 1 ? true : false;
        mEpgFreq = in.readInt();
        int mainFreqnumb = in.readInt();
        if (mainFreqnumb != 0) {
            mMainFreqList = new int[mainFreqnumb];
            in.readIntArray(mMainFreqList);
        }
        mbBanlanceVolByRatio = in.readInt() == 1 ? true : false;
        mbScanShowFreq = in.readInt() == 1 ? true : false;
        mbScanShowServiceCountByCarrier = in.readInt() == 1 ? true : false;

        mbScanShowTotalServiceCount = in.readInt() == 1 ? true : false;
        mbShowScanMenuAfterAutoScanFail = in.readInt() == 1 ? true : false;
        mbUseLogicNumber = in.readInt() == 1 ? true : false;
        miStartProgramIndex = in.readInt();
    }
}
