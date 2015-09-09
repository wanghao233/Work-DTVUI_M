/**
 * @filename DTV ����״̬��Ϣ.
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import java.util.UUID;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 **/
public class PlayStatusInfo implements Parcelable {

    /**  **/
    public boolean mbIsUseUUID;

    /**  **/
    public UUID mPlayerUuid;

    /** {@link DTVConstant#ConstPlayerEvent DTVConstant.ConstPlayerEvent} **/
    public int miPlayEvent;

    /**  **/
    public String mstrPrompt;

    public PlayStatusInfo() {

    }

    public PlayStatusInfo(int miPlayEvent, String mstrPrompt) {
        this.miPlayEvent = miPlayEvent;
        this.mstrPrompt = mstrPrompt;
    }


    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<PlayStatusInfo> CREATOR = new Parcelable.Creator<PlayStatusInfo>() {
        public PlayStatusInfo createFromParcel(Parcel source) {
            return new PlayStatusInfo(source);
        }

        public PlayStatusInfo[] newArray(int size) {
            return new PlayStatusInfo[size];
        }
    };

    private PlayStatusInfo(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt((mbIsUseUUID == true) ? 1 : 0);
        if (mbIsUseUUID == true) {
            dest.writeString(mPlayerUuid.toString());
        }
        dest.writeInt(miPlayEvent);

        dest.writeString(mstrPrompt);
    }

    public void readFromParcel(Parcel in) {

        mbIsUseUUID = (in.readInt() == 1) ? true : false;
        if (mbIsUseUUID == true) {
            mPlayerUuid.fromString(in.readString());
        }

        miPlayEvent = in.readInt();
        mstrPrompt = in.readString();
    }
}
