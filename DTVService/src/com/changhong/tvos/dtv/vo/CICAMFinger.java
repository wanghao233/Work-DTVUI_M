/**
 * @filename DTV CAָ��
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class CICAMFinger extends CICAMMessageBase implements Parcelable {
    /**  **/
    public String mstrText;

    /**  **/
    public boolean mbFlash;

    /**  **/
    public int miShowtime;

    public CICAMFinger() {

    }

    public static final Creator<CICAMFinger> CREATOR = new Parcelable.Creator<CICAMFinger>() {
        public CICAMFinger createFromParcel(Parcel source) {
            return new CICAMFinger(source);
        }

        public CICAMFinger[] newArray(int size) {
            return new CICAMFinger[size];
        }
    };

    private CICAMFinger(Parcel source) {
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
