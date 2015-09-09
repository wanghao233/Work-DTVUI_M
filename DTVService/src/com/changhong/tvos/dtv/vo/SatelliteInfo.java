/**
 * @filename DTV ������Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class SatelliteInfo implements Parcelable {
    /**  **/
    public int miSatelliteIndex;

    /** LNB(DTVConstant.LNBPowerModeType {@link DTVConstant.LNBPowerModeType})*/
    public int miLNBType;

    public int miLongitude;

    /**  **/
    public String msName;

    /**  **/
    public int miPositioner;

    /** 22K(DTVConstant.Tone22KhzState {@linkDTVConstant.Tone22KhzState})**/
    public int miTone22KHz;

    /** DiSEqC10(DTVConstant.Diseqc10Port {@linkDTVConstant.Diseqc10Port})**/
    public byte mcDiSEqC10Port;

    /** 12V(true:false:)**/
    public boolean mb12VFlag;

    /** LNB(DTVConstant.LNBPowerModeType {@linkDTVConstant.LNBPowerModeType})**/
    public byte mcLNBVoltage;

    /** DiSEqC12 **/
    public int miDiseqc12SaveIndex;

    /** MiniDiseqc **/
    public byte mcMiniDiseqc;

    /** DiSEqCDTVConstant.DiseqcVersion {@linkDTVConstant.DiseqcVersion})**/
    public int miDiseqcVersion;

    /** LNB **/
    public int miLnbLowFrequencyKHz;

    /** LNB **/
    public int miLnbHighFrequencyKHz;

    public SatelliteInfo(
            int miSatelliteIndex,
            int miLNBType,
            int miLongitude,
            String msName,
            int miPositioner,
            int miTone22KHz,
            byte mcDiSEqC10Port,
            boolean mb12VFlag,
            byte mcLNBVoltage,
            int miDiseqc12SaveIndex,
            byte mcMiniDiseqc,
            int miDiseqcVersion,
            int miLnbLowFrequencyKHz,
            int miLnbHighFrequencyKHz) {
        this.miSatelliteIndex = miSatelliteIndex;
        this.miLNBType = miLNBType;
        this.miLongitude = miLongitude;
        this.msName = msName;
        this.miPositioner = miPositioner;
        this.miTone22KHz = miTone22KHz;
        this.mcDiSEqC10Port = mcDiSEqC10Port;
        this.mb12VFlag = mb12VFlag;
        this.mcLNBVoltage = mcLNBVoltage;
        this.miDiseqc12SaveIndex = miDiseqc12SaveIndex;
        this.mcMiniDiseqc = mcMiniDiseqc;
        this.miDiseqcVersion = miDiseqcVersion;
        this.miLnbLowFrequencyKHz = miLnbLowFrequencyKHz;
        this.miLnbHighFrequencyKHz = miLnbHighFrequencyKHz;

    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<SatelliteInfo> CREATOR = new Parcelable.Creator<SatelliteInfo>() {
        public SatelliteInfo createFromParcel(Parcel source) {
            return new SatelliteInfo(source);
        }

        public SatelliteInfo[] newArray(int size) {
            return new SatelliteInfo[size];
        }
    };

    private SatelliteInfo(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miSatelliteIndex);
        dest.writeInt(miLNBType);
        dest.writeInt(miLongitude);
        dest.writeString(msName);
        dest.writeInt(miPositioner);
        dest.writeInt(miTone22KHz);
        dest.writeByte(mcDiSEqC10Port);
        dest.writeInt((mb12VFlag == true) ? 1 : 0);
        dest.writeByte(mcLNBVoltage);
        dest.writeInt(miDiseqc12SaveIndex);
        dest.writeByte(mcMiniDiseqc);
        dest.writeInt(miDiseqcVersion);
        dest.writeInt(miLnbLowFrequencyKHz);
        dest.writeInt(miLnbHighFrequencyKHz);
    }

    public void readFromParcel(Parcel in) {
        miSatelliteIndex = in.readInt();
        miLNBType = in.readInt();
        msName = in.readString();
        miPositioner = in.readInt();
        miTone22KHz = in.readInt();
        mcDiSEqC10Port = in.readByte();
        mb12VFlag = (in.readInt() == 1) ? true : false;
        mcLNBVoltage = in.readByte();
        miDiseqc12SaveIndex = in.readInt();
        mcMiniDiseqc = in.readByte();
        miDiseqcVersion = in.readInt();
        miLnbLowFrequencyKHz = in.readInt();
        miLnbHighFrequencyKHz = in.readInt();
    }
}
