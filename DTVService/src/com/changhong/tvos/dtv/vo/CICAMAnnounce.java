/**
 * @filename DTV CA������Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/** **/
public class CICAMAnnounce extends CICAMMessageBase implements Parcelable {
    /**  **/
    public String mstrText;

    /**  **/
    public boolean mbFlash;

    /**  **/
    public int miShowtime;

    public CICAMAnnounce() {

    }

    public static final Creator<CICAMAnnounce> CREATOR = new Parcelable.Creator<CICAMAnnounce>() {
        public CICAMAnnounce createFromParcel(Parcel source) {
            return new CICAMAnnounce(source);
        }

        public CICAMAnnounce[] newArray(int size) {
            return new CICAMAnnounce[size];
        }
    };

    private CICAMAnnounce(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        super.writeToParcel(dest, arg1);
        dest.writeString(mstrText);
        dest.writeInt(mbFlash == true ? 1 : 0);
        dest.writeInt(miShowtime);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        mstrText = in.readString();
        mbFlash = in.readInt() == 1 ? true : false;
        miShowtime = in.readInt();
    }
}
