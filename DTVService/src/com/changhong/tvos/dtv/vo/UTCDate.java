/**
 * @filename DTV ����
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class UTCDate implements Parcelable {
    /**  **/
    public int miYear;

    /**  **/
    public int miMonth;

    /**  **/
    public int miDay;

    /**  **/
    public int miWeekDay;

    /**
     * @param year
     * @param month
     * @param day
     * @param weekDay
     * @exception
     */
    public UTCDate(int year, int month, int day, int weekDay) {
        this.miYear = year;
        this.miMonth = month;
        this.miDay = day;
        this.miWeekDay = weekDay;
    }

    public UTCDate() {
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[" + miYear + "," + miMonth + "," + miDay + "," + miWeekDay + "]";
    }

    public static final Creator<UTCDate> CREATOR = new Parcelable.Creator<UTCDate>() {
        public UTCDate createFromParcel(Parcel source) {
            return new UTCDate(source);
        }

        public UTCDate[] newArray(int size) {
            return new UTCDate[size];
        }
    };

    private UTCDate(Parcel source) {
        readFromParcel(source);
    }

    public void writeToParcel(Parcel dest, int arg1) {

        dest.writeInt(miYear);
        dest.writeInt(miMonth);
        dest.writeInt(miDay);
        dest.writeInt(miWeekDay);
    }

    public void readFromParcel(Parcel in) {
        miYear = in.readInt();
        miMonth = in.readInt();
        miDay = in.readInt();
        miWeekDay = in.readInt();
    }
}
