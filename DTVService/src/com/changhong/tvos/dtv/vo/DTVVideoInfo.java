/**
 * @filename DTV ��Ƶ��Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 **/
public class DTVVideoInfo implements Parcelable {
    public enum EnumAspectRatio {
        VIDEO_ASPECT_4X3,
        VIDEO_ASPECT_16X9,
        ASPECT_SQUARE,
        ASPECT_UNKNOWN
    }

    public enum EnumFrameRate {
        VIDEO_FR_23_976,
        VIDEO_FR_24,
        VIDEO_FR_25,
        VIDEO_FR_29_97,
        VIDEO_FR_30,
        VIDEO_FR_50,
        VIDEO_FR_59_94,
        VIDEO_FR_60,
        VIDEO_FR_UNKNOWN
    }

    public enum EnumFormat {
        VIDEO_FORMAT_480I60,
        VIDEO_FORMAT_576I50,
        VIDEO_FORMAT_576I60,
        VIDEO_FORMAT_576I50_SECAM,
        FORMAT_HD480P60,
        FORMAT_HD576P50,
        FORMAT_HD720P50,
        FORMAT_HD720P60,
        FORMAT_HD1080I50,
        FORMAT_HD1080I60,
        FORMAT_HD1080P50,
        FORMAT_UNKNOWN
    }

    /**  **/
    public EnumFormat meFormat;

    /**  **/
    public EnumAspectRatio meAspectRatio;

    /**  **/
    public EnumFrameRate meFrameRate;

    /**  **/
    public int miWitdth;

    /**  **/
    public int miHight;

    public DTVVideoInfo() {
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<DTVVideoInfo> CREATOR = new Parcelable.Creator<DTVVideoInfo>() {
        public DTVVideoInfo createFromParcel(Parcel source) {
            return new DTVVideoInfo(source);
        }

        public DTVVideoInfo[] newArray(int size) {
            return new DTVVideoInfo[size];
        }
    };

    private DTVVideoInfo(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(meFormat.ordinal());
        dest.writeInt(meAspectRatio.ordinal());
        dest.writeInt(meFrameRate.ordinal());
        dest.writeInt(miWitdth);
        dest.writeInt(miHight);
    }

    public void readFromParcel(Parcel in) {
        meFormat = EnumFormat.values()[in.readInt()];
        meAspectRatio = EnumAspectRatio.values()[in.readInt()];
        meFrameRate = EnumFrameRate.values()[in.readInt()];
        miWitdth = in.readInt();
        miHight = in.readInt();
    }
}
