/**
 * @filename DTV
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 *
 **/
public class UserGroupInfo {
    /**  **/
    int miIndex;

    /**  **/
    String mstrName;

    /**
     * ༭
     **/
    boolean mbCanEdit;


    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<UserGroupInfo> CREATOR = new Parcelable.Creator<UserGroupInfo>() {
        public UserGroupInfo createFromParcel(Parcel source) {
            return new UserGroupInfo(source);
        }

        public UserGroupInfo[] newArray(int size) {
            return new UserGroupInfo[size];
        }
    };

    private UserGroupInfo(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miIndex);
        dest.writeString(mstrName);
        dest.writeInt((mbCanEdit == true) ? 1 : 0);
    }

    public void readFromParcel(Parcel in) {
        miIndex = in.readInt();
        mstrName = in.readString();
        mbCanEdit = (in.readInt() == 1) ? true : false;
    }
}
