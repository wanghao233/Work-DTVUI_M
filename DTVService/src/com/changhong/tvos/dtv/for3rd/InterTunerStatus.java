/**
 * @filename
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.for3rd;

import android.os.Parcel;
import android.os.Parcelable;


/** TunerStatus **/
public class InterTunerStatus implements Parcelable {
    /** 信号水平（0~127) **/
    public int miSignalLevel;

    /** 信号质量 **/
    public int miSignalQuality;

    /** 锁住 **/
    public boolean mbLock;

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<InterTunerStatus> CREATOR = new Parcelable.Creator<InterTunerStatus>() {
        public InterTunerStatus createFromParcel(Parcel source) {
            return new InterTunerStatus(source);
        }

        public InterTunerStatus[] newArray(int size) {
            return new InterTunerStatus[size];
        }
    };

    private InterTunerStatus(Parcel source) {
        readFromParcel(source);
    }

    public InterTunerStatus() {
        // TODO Auto-generated constructor stub
    }

    /**
     * 序列化
     */
    public void readFromParcel(Parcel in) {
        miSignalLevel = in.readInt();
        miSignalQuality = in.readInt();
        mbLock = (in.readInt() == 1);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(miSignalLevel);
        dest.writeInt(miSignalQuality);
        dest.writeInt((mbLock) ? 1 : 0);
    }
}