/**
 * @filename DTV ��Ӫ����?
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class Operator implements Parcelable {

    /**  **/
    public String mstrOperatorName;

    /**  **/
    public int miOperatorCode;

    /**  **/
    public String province;

    /**  **/
    public String city;

    public Operator(
            String mstrOperatorName,
            int miOperatorCode) {
        this.mstrOperatorName = mstrOperatorName;
        this.miOperatorCode = miOperatorCode;
    }

    public Operator(
            String mstrOperatorName,
            int miOperatorCode,
            String location,
            String city) {
        this.mstrOperatorName = mstrOperatorName;
        this.miOperatorCode = miOperatorCode;
        this.province = location;
        this.city = city;
    }

    public static final Creator<Operator> CREATOR = new Parcelable.Creator<Operator>() {
        public Operator createFromParcel(Parcel source) {
            return new Operator(source);
        }

        public Operator[] newArray(int size) {
            return new Operator[size];
        }
    };

    private Operator(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeString(mstrOperatorName);
        dest.writeInt(miOperatorCode);
        dest.writeString(province);
        dest.writeString(city);
    }

    public void readFromParcel(Parcel in) {
        mstrOperatorName = in.readString();
        miOperatorCode = in.readInt();
        province = in.readString();
        city = in.readString();
    }
}
