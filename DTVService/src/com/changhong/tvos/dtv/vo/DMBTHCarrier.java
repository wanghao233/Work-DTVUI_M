/**
 * @filename DTV ������沨Ƶ����Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class DMBTHCarrier extends CarrierInfo implements Parcelable {
    /** NCOƵ(λ:Khz)**/
    public int miNCOFrequencyKhz;

    /** (DMBTHCarrierMode {@link DTVConstant.DMBTHCarrierMode})**/
    public int miCarrierMode;

    /** DTMBT(DTMBTHQAMMode {@link DTVConstant.DTMBTHQAMMode})**/
    public int miDTMBTHQamMode;

    /** DMBTHLDCP(DMBTHLDPCRate {@linkDTVConst.DMBTHLDPCRate})**/
    public int miLDPCRate;

    /** (DMBTHFrameHeader {@link DTVConstant.DMBTHFrameHeader})*/
    public int miFrameHeader;

    /** (DMBTHInterleaverMode {@link DTVConstant.DMBTHInterleaverMode})*/
    public int miInterleaverMode;

    public DMBTHCarrier(int miNCOFrequencyKhz, int miCarrierMode,
                        int miDTMBTHQamMode, int miLDPCRate, int miFrameHeader,
                        int miInterleaverMode) {
        this.miNCOFrequencyKhz = miNCOFrequencyKhz;
        this.miCarrierMode = miCarrierMode;
        this.miDTMBTHQamMode = miDTMBTHQamMode;
        this.miLDPCRate = miLDPCRate;
        this.miFrameHeader = miFrameHeader;
        this.miInterleaverMode = miInterleaverMode;

    }

    public DMBTHCarrier(int miIndex, int miDemodType, int miTsID, int miOrgNetId, int miFrequencyK, byte mcSpectrum,
                        int miNCOFrequencyKhz, int miCarrierMode, int miDTMBTHQamMode, int miLDPCRate, int miFrameHeader, int miInterleaverMode) {
        /**  **/
        this.miIndex = miIndex;

        /** (DemodType {@link DTVCnst.ConstDemodeType})*/
        this.miDemodType = miDemodType;

        /** */
        this.miTsID = miTsID;

        /** */
        this.miOrgNetId = miOrgNetId;

        /** (Khz) **/
        this.miFrequencyK = miFrequencyK;

        /** (0:SpectrumMode{@link DTVConstant.SpectrumMode} **/
        this.mcSpectrum = mcSpectrum;

        this.miNCOFrequencyKhz = miNCOFrequencyKhz;
        this.miCarrierMode = miCarrierMode;
        this.miDTMBTHQamMode = miDTMBTHQamMode;
        this.miLDPCRate = miLDPCRate;
        this.miFrameHeader = miFrameHeader;
        this.miInterleaverMode = miInterleaverMode;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<DMBTHCarrier> CREATOR = new Parcelable.Creator<DMBTHCarrier>() {
        public DMBTHCarrier createFromParcel(Parcel source) {
            return new DMBTHCarrier(source);
        }

        public DMBTHCarrier[] newArray(int size) {
            return new DMBTHCarrier[size];
        }
    };

    private DMBTHCarrier(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miDemodType);
        dest.writeInt(miIndex);
        dest.writeInt(miTsID);
        dest.writeInt(miOrgNetId);
        dest.writeInt(miFrequencyK);
        dest.writeByte(mcSpectrum);
        dest.writeInt(miNCOFrequencyKhz);
        dest.writeInt(miCarrierMode);
        dest.writeInt(miDTMBTHQamMode);
        dest.writeInt(miLDPCRate);
        dest.writeInt(miFrameHeader);
        dest.writeInt(miInterleaverMode);
    }

    public void readFromParcel(Parcel in) {
        miDemodType = in.readInt();
        miIndex = in.readInt();
        miTsID = in.readInt();
        miOrgNetId = in.readInt();
        miFrequencyK = in.readInt();
        mcSpectrum = in.readByte();
        miNCOFrequencyKhz = in.readInt();
        miCarrierMode = in.readInt();
        miDTMBTHQamMode = in.readInt();
        miLDPCRate = in.readInt();
        miFrameHeader = in.readInt();
        miInterleaverMode = in.readInt();
    }
}
