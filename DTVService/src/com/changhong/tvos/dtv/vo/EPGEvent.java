/**
 * @filename DTV EPG�¼���Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/** EPG **/
public class EPGEvent implements Parcelable {
    /** ID **/
    public int miEventID;

    /**  **/
    public UTCDate startDate;

    /**  **/
    public UTCTime startTime;

    /**  **/
    public UTCTime duringTime;

    /**  **/
    public String eventName;

    /**  **/
    public String shortText;

    public EPGEvent() {

    }

    public EPGEvent(int eventId, UTCDate date, UTCTime time, UTCTime duration,
                    String EventName, String shortText) {
        this.miEventID = eventId;
        this.startDate = date;
        this.startTime = time;
        this.duringTime = duration;
        this.eventName = EventName;
        this.shortText = shortText;
    }


    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<EPGEvent> CREATOR = new Parcelable.Creator<EPGEvent>() {
        public EPGEvent createFromParcel(Parcel source) {
            return new EPGEvent(source);
        }

        public EPGEvent[] newArray(int size) {
            return new EPGEvent[size];
        }
    };

    private EPGEvent(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miEventID);
        startDate.writeToParcel(dest, arg1);
        startTime.writeToParcel(dest, arg1);
        duringTime.writeToParcel(dest, arg1);
        dest.writeString(eventName);
        dest.writeString(shortText);
    }

    public void readFromParcel(Parcel in) {
        miEventID = in.readInt();
        startDate = UTCDate.CREATOR.createFromParcel(in);
        startTime = UTCTime.CREATOR.createFromParcel(in);
        duringTime = UTCTime.CREATOR.createFromParcel(in);
        eventName = in.readString();
        shortText = in.readString();
    }
}
