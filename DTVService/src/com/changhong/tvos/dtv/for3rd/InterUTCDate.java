/**
 * @filename
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.for3rd;

import android.os.Parcel;
import android.os.Parcelable;

/** UTCDate **/
public class InterUTCDate implements Parcelable {
    public int miYear;

    public int miMonth;

    public int miDay;

    public int miWeekDay;


    public InterUTCDate(int iYear, int iMonth, int iDay, int iWeekDay) {
        this.miYear = iYear;
        this.miMonth = iMonth;
        this.miDay = iDay;
        this.miWeekDay = iWeekDay;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<InterUTCDate> CREATOR = new Parcelable.Creator<InterUTCDate>() {
        public InterUTCDate createFromParcel(Parcel source) {
            return new InterUTCDate(source);
        }

        public InterUTCDate[] newArray(int size) {
            return new InterUTCDate[size];
        }
    };

    private InterUTCDate(Parcel source) {
        readFromParcel(source);
    }

    public InterUTCDate() {
        // TODO Auto-generated constructor stub
    }

    public void readFromParcel(Parcel in) {
        miYear = in.readInt();
        miMonth = in.readInt();
        miDay = in.readInt();
        miWeekDay = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(miYear);
        dest.writeInt(miMonth);
        dest.writeInt(miDay);
        dest.writeInt(miWeekDay);
    }
}