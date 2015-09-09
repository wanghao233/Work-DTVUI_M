/**
 * @filename
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.for3rd;

import android.os.Parcel;
import android.os.Parcelable;


/** VersionInfo **/
public class InterVersionInfo implements Parcelable {
    /** 软件版本 **/
    public int miHardwareVersion;

    /** 运用商信息 **/
    public int miOpVersion;

    /** 应用主版本号 **/
    public int miAPIMainVersion;

    /** 应用子版本号 **/
    public int miAPISubVersion;

    /** 主版本号 **/
    public int miMainVersion;

    /** 子版本号 **/
    public int miSubVersion;

    /** 发布时间 **/
    public String mstrReleaseDateTime;

    /** 3rd主版本号 **/
    public int mi3rdAPIMainVersion;

    /** 3rd子版本号 **/
    public int mi3rdAPISubVersion;

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<InterVersionInfo> CREATOR = new Parcelable.Creator<InterVersionInfo>() {
        public InterVersionInfo createFromParcel(Parcel source) {
            return new InterVersionInfo(source);
        }

        public InterVersionInfo[] newArray(int size) {
            return new InterVersionInfo[size];
        }
    };

    private InterVersionInfo(Parcel source) {
        readFromParcel(source);
    }

    public InterVersionInfo() {
        // TODO Auto-generated constructor stub
    }

    public void readFromParcel(Parcel in) {
        miHardwareVersion = in.readInt();
        miOpVersion = in.readInt();
        miAPIMainVersion = in.readInt();
        miAPISubVersion = in.readInt();
        miMainVersion = in.readInt();
        miSubVersion = in.readInt();
        mstrReleaseDateTime = in.readString();
        mi3rdAPIMainVersion = in.readInt();
        mi3rdAPISubVersion = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(miHardwareVersion);
        dest.writeInt(miOpVersion);
        dest.writeInt(miAPIMainVersion);
        dest.writeInt(miAPISubVersion);
        dest.writeInt(miMainVersion);
        dest.writeInt(miSubVersion);
        dest.writeString(mstrReleaseDateTime);
        dest.writeInt(mi3rdAPIMainVersion);
        dest.writeInt(mi3rdAPISubVersion);
    }
}