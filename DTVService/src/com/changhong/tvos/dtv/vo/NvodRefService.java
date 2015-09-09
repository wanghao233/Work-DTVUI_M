package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * NVOD
 **/
public class NvodRefService implements Parcelable {

    public int tsId;
    public int orgNetworkId;
    public int serviceId;
    public String serviceName;

    public NvodRefService(int tsId, int orgNetworkId,
                          int serviceId, String serviceName) {
        this.tsId = tsId;
        this.orgNetworkId = orgNetworkId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<NvodRefService> CREATOR = new Parcelable.Creator<NvodRefService>() {
        public NvodRefService createFromParcel(Parcel source) {
            return new NvodRefService(source);
        }

        public NvodRefService[] newArray(int size) {
            return new NvodRefService[size];
        }
    };

    private NvodRefService(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(tsId);
        dest.writeInt(orgNetworkId);
        dest.writeInt(serviceId);
        dest.writeString(serviceName);
    }

    public void readFromParcel(Parcel in) {
        tsId = in.readInt();
        orgNetworkId = in.readInt();
        serviceId = in.readInt();
        serviceName = in.readString();
    }
}