/**
 * @filename DTV DVB-CƵ����Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class DVBCCarrier extends CarrierInfo implements Parcelable {
    /** QAMMode {@link DTVConstant#ConstQAMMode DTVConstant.ConstQAMMode}) **/
    public int miQamMode;

    /** (kbps) **/
    public int miSymbolRateK;

    public DVBCCarrier(int miQamMode, int miSymbolRateK) {
        this.miQamMode = miQamMode;
        this.miSymbolRateK = miSymbolRateK;
    }


    public DVBCCarrier(int miIndex, int miDemodType, int miTsID, int miOrgNetId, int miFrequencyK, byte mcSpectrum, int miQamMode, int miSymbolRateK) {
        /**  **/
        this.miIndex = miIndex;

        /** DemodType {@link DTVCnst.ConstDemodeType}) **/
        this.miDemodType = miDemodType;

        /**  **/
        this.miTsID = miTsID;

        /**  **/
        this.miOrgNetId = miOrgNetId;

        /**  **/
        this.miFrequencyK = miFrequencyK;

        /** SpectrumMode{@link DTVConstant.SpectrumMode} **/
        this.mcSpectrum = mcSpectrum;

        this.miQamMode = miQamMode;
        this.miSymbolRateK = miSymbolRateK;
    }


    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<DVBCCarrier> CREATOR = new Parcelable.Creator<DVBCCarrier>() {
        public DVBCCarrier createFromParcel(Parcel source) {
            return new DVBCCarrier(source);
        }

        public DVBCCarrier[] newArray(int size) {
            return new DVBCCarrier[size];
        }
    };

    private DVBCCarrier(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miDemodType);
        dest.writeInt(miIndex);
        dest.writeInt(miTsID);
        dest.writeInt(miOrgNetId);
        dest.writeInt(miFrequencyK);
        dest.writeByte(mcSpectrum);
        dest.writeInt(miQamMode);
        dest.writeInt(miSymbolRateK);
    }

    public void readFromParcel(Parcel in) {
        miDemodType = in.readInt();
        miIndex = in.readInt();
        miTsID = in.readInt();
        miOrgNetId = in.readInt();
        miFrequencyK = in.readInt();
        mcSpectrum = in.readByte();
        miQamMode = in.readInt();
        miSymbolRateK = in.readInt();
    }
}
