/**
 * @filename ��ʱ����Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**  **/
public class TimerInfo implements Parcelable {
    public class TimerType {
        /** EPG **/
        public final static int EPG_TIMER = 0;

        /** PVR **/
        public final static int PVR_TIMER = 1;

        /** **/
        public final static int SHUT_DOWN_TIMER = 2;
    }

    /** EPG **/
    public int mOriginal = 0;

    /**  **/
    public int miIndex;

    /**  **/
    public int miType;

    /**  **/
    public long mlStartNotifyTime;

    /**  **/
    public long mlNotNotifyTime;

    /**  **/
    public String mstrTriggerBroadCast;

    /**  **/
    public byte[] mByteDataInfo;


    public TimerInfo(
            int iType,
            long lStartNotifyTime,
            long lNotNotifyTime,
            String strTriggerBroadCast,
            byte[] mByteDataInfo) {
        this.miType = iType;
        this.mlStartNotifyTime = lStartNotifyTime;
        this.mlNotNotifyTime = lNotNotifyTime;
        this.mstrTriggerBroadCast = strTriggerBroadCast;
        this.mByteDataInfo = mByteDataInfo;
    }

    public TimerInfo(int mOriginal, int miType, long mlStartNotifyTime,
                     long mlNotNotifyTime, String mstrTriggerBroadCast,
                     byte[] mByteDataInfo) {
        super();
        this.mOriginal = mOriginal;
        this.miType = miType;
        this.mlStartNotifyTime = mlStartNotifyTime;
        this.mlNotNotifyTime = mlNotNotifyTime;
        this.mstrTriggerBroadCast = mstrTriggerBroadCast;
        this.mByteDataInfo = mByteDataInfo;
    }

    public static final Creator<TimerInfo> CREATOR = new Parcelable.Creator<TimerInfo>() {
        public TimerInfo createFromParcel(Parcel source) {
            return new TimerInfo(source);
        }

        public TimerInfo[] newArray(int size) {
            return new TimerInfo[size];
        }
    };

    private TimerInfo(Parcel source) {
        readFromParcel(source);
    }

    public TimerInfo() {

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miIndex);
        dest.writeInt(miType);
        dest.writeInt(mOriginal);
        dest.writeLong(mlStartNotifyTime);
        dest.writeLong(mlNotNotifyTime);
        dest.writeString(mstrTriggerBroadCast);
        if (mByteDataInfo != null) {
            dest.writeInt(mByteDataInfo.length);
            dest.writeByteArray(mByteDataInfo);
        } else {
            dest.writeInt(0);
        }
    }

    public void readFromParcel(Parcel in) {
        miIndex = in.readInt();
        miType = in.readInt();
        mOriginal = in.readInt();
        mlStartNotifyTime = in.readLong();
        mlNotNotifyTime = in.readLong();
        mstrTriggerBroadCast = in.readString();
        int len = in.readInt();
        if (len > 0) {
            mByteDataInfo = new byte[len];
            in.readByteArray(mByteDataInfo);
        }
    }

    public boolean IsSame(TimerInfo timer) {
        if ((this.miType != timer.miType
                || this.mOriginal != timer.mOriginal)
                || (this.mlStartNotifyTime / 1000L != timer.mlStartNotifyTime / 1000L)
                || (this.mlNotNotifyTime / 1000L != timer.mlNotNotifyTime / 1000L)) {
            Log.i("TimerInfo", "start or end not equal");
            return false;
        }

        if ((this.mstrTriggerBroadCast != null) && (timer.mstrTriggerBroadCast != null)) {
            if (!this.mstrTriggerBroadCast.equals(timer.mstrTriggerBroadCast)) {
                Log.i("TimerInfo", "riggerBroadCast not equal");
                return false;
            }
        } else if ((this.mByteDataInfo != null) && (timer.mByteDataInfo != null)) {
            if (!this.mByteDataInfo.equals(timer.mByteDataInfo)) {
                Log.i("TimerInfo", "mByteDataInfo not equal");
                return false;
            }
        }
        return true;
    }
}
