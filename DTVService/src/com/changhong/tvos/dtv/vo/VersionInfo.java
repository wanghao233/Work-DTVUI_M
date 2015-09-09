/**
 * @filename DTV �汾
 * @author:
 * @date:
 * @version 0.1
 * @history: 2012-9-13  ����KO�汾
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class VersionInfo implements Parcelable {
    /**  **/
    public int miHardwareVersion;

    /**  **/
    public int miOpVersion;

    /**  **/
    public int miAPIMainVersion;

    /**  **/
    public int miAPISubVersion;

    /**  **/
    public int miMainVersion;

    /**  **/
    public int miSubVersion;

    /**  **/
    public int miKOVersion;

    /**  **/
    public String mstrReleaseDateTime;

    public VersionInfo(
            int miHardwareVersion,
            int miOpVersion,
            int miAPIMainVersion,
            int miAPISubVersion,
            int miMainVersion,
            int miSubVersion,
            int miKOVersion,
            String mstrReleaseDateTime) {
        this.miHardwareVersion = miHardwareVersion;
        this.miOpVersion = miOpVersion;
        this.miAPIMainVersion = miAPIMainVersion;
        this.miAPISubVersion = miAPISubVersion;
        this.miMainVersion = miMainVersion;
        this.miSubVersion = miSubVersion;
        this.miKOVersion = miKOVersion;
        this.mstrReleaseDateTime = mstrReleaseDateTime;
    }

    public static final Creator<VersionInfo> CREATOR = new Parcelable.Creator<VersionInfo>() {
        public VersionInfo createFromParcel(Parcel source) {
            return new VersionInfo(source);
        }

        public VersionInfo[] newArray(int size) {
            return new VersionInfo[size];
        }
    };

    private VersionInfo(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miHardwareVersion);
        dest.writeInt(miOpVersion);
        dest.writeInt(miMainVersion);
        dest.writeInt(miSubVersion);
        dest.writeInt(miKOVersion);
        dest.writeString(mstrReleaseDateTime);
    }

    public void readFromParcel(Parcel in) {
        miHardwareVersion = in.readInt();
        miOpVersion = in.readInt();
        miMainVersion = in.readInt();
        miSubVersion = in.readInt();
        miKOVersion = in.readInt();
        mstrReleaseDateTime = in.readString();
    }
}
