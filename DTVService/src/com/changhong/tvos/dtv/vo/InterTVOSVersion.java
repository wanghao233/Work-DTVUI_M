package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class InterTVOSVersion implements Parcelable {
    public int mMainVersion;
    public int mSubVersion;

    public static final Creator<InterTVOSVersion> CREATOR = new Parcelable.Creator<InterTVOSVersion>() {
        public InterTVOSVersion createFromParcel(Parcel source) {
            return new InterTVOSVersion(source);
        }

        public InterTVOSVersion[] newArray(int size) {
            return new InterTVOSVersion[size];
        }
    };

    public InterTVOSVersion() {
    }

    public InterTVOSVersion(int mainVersion, int subVersion) {
        this.mMainVersion = mainVersion;
        this.mSubVersion = subVersion;
    }

    private InterTVOSVersion(Parcel source) {
        readFromParcel(source);
    }

    public void readFromParcel(Parcel in) {
        this.mMainVersion = in.readInt();
        this.mSubVersion = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mMainVersion);
        dest.writeInt(this.mSubVersion);
    }
}
