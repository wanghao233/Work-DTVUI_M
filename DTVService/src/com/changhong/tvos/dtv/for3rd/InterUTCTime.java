/**
 * @filename
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.for3rd;

import android.os.Parcel;
import android.os.Parcelable;


/** UTCTime **/
public class InterUTCTime implements Parcelable {
    public int miHour;

    public int miMinute;

    public int miSecond;


    public InterUTCTime(int hour, int minute, int second) {
        this.miHour = hour;
        this.miMinute = minute;
        this.miSecond = second;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<InterUTCTime> CREATOR = new Parcelable.Creator<InterUTCTime>() {
        public InterUTCTime createFromParcel(Parcel source) {
            return new InterUTCTime(source);
        }

        public InterUTCTime[] newArray(int size) {
            return new InterUTCTime[size];
        }
    };

    private InterUTCTime(Parcel source) {
        readFromParcel(source);
    }

    public InterUTCTime() {
        // TODO Auto-generated constructor stub
    }

    public void readFromParcel(Parcel in) {
        miHour = in.readInt();
        miMinute = in.readInt();
        miSecond = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(miHour);
        dest.writeInt(miMinute);
        dest.writeInt(miSecond);
    }
}