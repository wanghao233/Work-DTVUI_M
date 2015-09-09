/**
 * @filename DTV TUNER״̬��Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class DTVTunerStatus implements Parcelable {
    /**  **/
    public int miSignalLevel;

    /**  **/
    public int miSignLevelPercent;

    /**  **/
    public int miSignalQuality;

    /**  **/
    public boolean mbLock;

    public int miBitErrorRate;

    /**  **/
    public int miSNR;

    /** Specturm(SpectrumMode{@link DTVConstant.SpectrumMode} ) **/
    public byte mcSpectrum;

    public DTVTunerStatus(
            int miSignalLevel,
            int miSignLevelPercent,
            int miSignalQuality,
            boolean mbLock,
            int miBitErrorRate,
            int miSNR,
            byte mcSpectrum) {
        this.miSignalLevel = miSignalLevel;
        this.miSignLevelPercent = miSignLevelPercent;
        this.miSignalQuality = miSignalQuality;
        this.mbLock = mbLock;
        this.miBitErrorRate = miBitErrorRate;
        this.miSNR = miSNR;
        this.mcSpectrum = mcSpectrum;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<DTVTunerStatus> CREATOR = new Parcelable.Creator<DTVTunerStatus>() {
        public DTVTunerStatus createFromParcel(Parcel source) {
            return new DTVTunerStatus(source);
        }

        public DTVTunerStatus[] newArray(int size) {
            return new DTVTunerStatus[size];
        }
    };

    private DTVTunerStatus(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miSignalLevel);
        dest.writeInt(miSignLevelPercent);
        dest.writeInt(miSignalQuality);
        dest.writeInt(((mbLock) ? (1) : (0)));
        dest.writeInt(miBitErrorRate);
        dest.writeInt(miSNR);
        dest.writeByte(mcSpectrum);
    }

    public void readFromParcel(Parcel in) {
        miSignalLevel = in.readInt();
        miSignLevelPercent = in.readInt();
        miSignalQuality = in.readInt();
        mbLock = (0 != in.readInt());
        miBitErrorRate = in.readInt();
        miSNR = in.readInt();
        mcSpectrum = in.readByte();
    }
}
