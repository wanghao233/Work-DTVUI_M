/**
 * @filename DTV ʱ��
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 **/
public class DTVDTTime implements Parcelable {

    /**  **/
    public UTCDate mstruDate;

    /**  **/
    public UTCTime mstruTime;

    public DTVDTTime(UTCDate mstruDate, UTCTime mstruTime) {
        //mstruDate = new UTCDate();
        //mstruTime = new UTCTime();

        this.mstruDate = mstruDate;
        this.mstruTime = mstruTime;
    }

    public void setTime(int year, int month, int day, int weekday, int hour, int minute, int second) {
        this.mstruDate.miYear = year;
        this.mstruDate.miMonth = month;
        this.mstruDate.miDay = day;
        this.mstruDate.miWeekDay = weekday;
        this.mstruTime.miHour = hour;
        this.mstruTime.miMinute = minute;
        this.mstruTime.miSecond = second;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        String name = "[";
        if (mstruDate != null) {
            name += mstruDate;
        } else {
            name += "null";
        }
        name += ",";

        if (mstruTime != null) {
            name += mstruTime;
        } else {
            name += "null";
        }
        name += "]";

        return name;
    }

    public static final Creator<DTVDTTime> CREATOR = new Parcelable.Creator<DTVDTTime>() {
        public DTVDTTime createFromParcel(Parcel source) {
            return new DTVDTTime(source);
        }

        public DTVDTTime[] newArray(int size) {
            return new DTVDTTime[size];
        }
    };

    private DTVDTTime(Parcel source) {
        readFromParcel(source);
    }

    public DTVDTTime() {
        // TODO Auto-generated constructor stub
        mstruDate = new UTCDate();
        mstruTime = new UTCTime();
    }

    public void writeToParcel(Parcel dest, int arg1) {

        if (mstruDate == null) {
            mstruDate = new UTCDate(1970, 1, 1, 4);
        }
        if (mstruTime == null) {
            mstruTime = new UTCTime(0, 0, 0);
        }
        mstruDate.writeToParcel(dest, arg1);
        mstruTime.writeToParcel(dest, arg1);
    }

    public void readFromParcel(Parcel in) {
        if (mstruDate == null) {
            mstruDate = new UTCDate(1970, 1, 1, 4);
        }
        if (mstruTime == null) {
            mstruTime = new UTCTime(0, 0, 0);
        }
        mstruDate.readFromParcel(in);
        mstruTime.readFromParcel(in);
    }
}
