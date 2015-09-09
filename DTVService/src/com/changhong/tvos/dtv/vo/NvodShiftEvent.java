package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * NVOD
 **/
public class NvodShiftEvent implements Parcelable {

    public int tsId;                    /*ԭʼ���紫������Ψһ��ʶ*/
    public int orgNetworkId;            /*ԭʼ�����ʶ*/
    public int serviceId;	            /*ҵ���ʶ,����TS��ȷ�������ҵ��*/
    public int eventId;                 /*�¼���ʶ*/

    public int year;         			/*�¼���ʼʱ*/
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;

    public NvodShiftEvent(int tsId, int orgNetworkId,
                          int serviceId, int eventId,
                          int year, int month, int day,
                          int hour, int minute, int second) {
        this.tsId = tsId;
        this.orgNetworkId = orgNetworkId;
        this.serviceId = serviceId;
        this.eventId = eventId;

        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<NvodShiftEvent> CREATOR = new Parcelable.Creator<NvodShiftEvent>() {
        public NvodShiftEvent createFromParcel(Parcel source) {
            return new NvodShiftEvent(source);
        }

        public NvodShiftEvent[] newArray(int size) {
            return new NvodShiftEvent[size];
        }
    };

    private NvodShiftEvent(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(tsId);
        dest.writeInt(orgNetworkId);
        dest.writeInt(serviceId);

        dest.writeInt(eventId);
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeInt(second);
    }

    public void readFromParcel(Parcel in) {
        tsId = in.readInt();
        orgNetworkId = in.readInt();
        serviceId = in.readInt();
        eventId = in.readInt();
        year = in.readInt();
        month = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        second = in.readInt();
    }
}