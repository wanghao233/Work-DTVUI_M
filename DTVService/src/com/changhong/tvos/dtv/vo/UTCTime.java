/**
 * @filename DTV ʱ��
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class UTCTime implements Parcelable {
    /**  **/
    public int miHour;

    /**  **/
    public int miMinute;

    /**  **/
    public int miSecond;

    /**
     * @param hour
     * @param minute
     * @param second
     * @throws
     */
    public UTCTime(int hour, int minute, int second) {
        this.miHour = hour;
        this.miMinute = minute;
        this.miSecond = second;
    }

    public UTCTime() {
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[" + miHour + "," + miMinute + "," + miSecond + "]";
    }

    public static final Creator<UTCTime> CREATOR = new Parcelable.Creator<UTCTime>() {
        public UTCTime createFromParcel(Parcel source) {
            return new UTCTime(source);
        }

        public UTCTime[] newArray(int size) {
            return new UTCTime[size];
        }
    };

    private UTCTime(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miHour);
        dest.writeInt(miMinute);
        dest.writeInt(miSecond);
    }

    public void readFromParcel(Parcel in) {
        miHour = in.readInt();
        miMinute = in.readInt();
        miSecond = in.readInt();
    }
}
