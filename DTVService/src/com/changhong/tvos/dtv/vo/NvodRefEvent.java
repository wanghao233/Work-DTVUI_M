package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * NVOD
 **/
public class NvodRefEvent implements Parcelable {

    public int refEventId;
    public int duration; /*����ʱ��*/
    public String EventName;
    public String EventDescribe;

    public NvodRefEvent(int refEventId, int duration,
                        String EventName, String EventDescribe) {
        this.refEventId = refEventId;
        this.duration = duration;
        this.EventName = EventName;
        this.EventDescribe = EventDescribe;
    }


    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<NvodRefEvent> CREATOR = new Parcelable.Creator<NvodRefEvent>() {
        public NvodRefEvent createFromParcel(Parcel source) {
            return new NvodRefEvent(source);
        }

        public NvodRefEvent[] newArray(int size) {
            return new NvodRefEvent[size];
        }
    };

    private NvodRefEvent(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(refEventId);
        dest.writeInt(duration);
        dest.writeString(EventName);
        dest.writeString(EventDescribe);
    }

    public void readFromParcel(Parcel in) {
        refEventId = in.readInt();
        duration = in.readInt();
        EventName = in.readString();
        EventDescribe = in.readString();
    }
}