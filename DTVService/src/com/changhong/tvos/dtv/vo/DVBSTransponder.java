/**
 * @filename DTV DVB-Sת������Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class DVBSTransponder extends CarrierInfo implements Parcelable {
    /** ֵ**/
    public int miSatliteID;

    /**  **/
    public int miPolarity;

    /** QPSK **/
    public int miQpskMode;

    public DVBSTransponder(int miSatliteID, int miPolarity, int miQpskMode) {
        this.miSatliteID = miSatliteID;
        this.miPolarity = miPolarity;
        this.miQpskMode = miQpskMode;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<DVBSTransponder> CREATOR = new Parcelable.Creator<DVBSTransponder>() {
        public DVBSTransponder createFromParcel(Parcel source) {
            return new DVBSTransponder(source);
        }

        public DVBSTransponder[] newArray(int size) {
            return new DVBSTransponder[size];
        }
    };

    private DVBSTransponder(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miDemodType);
        dest.writeInt(miIndex);
        dest.writeInt(miTsID);
        dest.writeInt(miOrgNetId);
        dest.writeInt(miFrequencyK);
        dest.writeByte(mcSpectrum);
        dest.writeInt(miSatliteID);
        dest.writeInt(miPolarity);
        dest.writeInt(miQpskMode);
    }

    public void readFromParcel(Parcel in) {
        miDemodType = in.readInt();
        miIndex = in.readInt();
        miTsID = in.readInt();
        miOrgNetId = in.readInt();
        miFrequencyK = in.readInt();
        mcSpectrum = in.readByte();
        miSatliteID = in.readInt();
        miPolarity = in.readInt();
        miQpskMode = in.readInt();
    }
}
