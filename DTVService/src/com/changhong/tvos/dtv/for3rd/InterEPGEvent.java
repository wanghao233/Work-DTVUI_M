/**
 * @filename
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.for3rd;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * EPGEvent
 */
public class InterEPGEvent implements Parcelable {
    /** 节目事件ID **/
    public int miEventID;

    /** 开始时间Date **/
    public InterUTCDate mStartDate;

    /** 开始时间 **/
    public InterUTCTime mStartTime;

    /** 时长 **/
    public InterUTCTime mDuringTime;

    /** 事件名称 **/
    public String mstrEventName;

    /** 名字 **/
    public String mstrShortText;

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<InterEPGEvent> CREATOR = new Parcelable.Creator<InterEPGEvent>() {
        public InterEPGEvent createFromParcel(Parcel source) {
            return new InterEPGEvent(source);
        }

        public InterEPGEvent[] newArray(int size) {
            return new InterEPGEvent[size];
        }
    };

    private InterEPGEvent(Parcel source) {
        readFromParcel(source);
    }

    public InterEPGEvent() {
        // TODO Auto-generated constructor stub
    }

    /**
     * 序列化
     */
    public void readFromParcel(Parcel in) {
        miEventID = in.readInt();
//		mStartDate.readFromParcel(in);
//		mStartTime.readFromParcel(in);
//		mDuringTime.readFromParcel(in);
        mStartDate = InterUTCDate.CREATOR.createFromParcel(in);
        mStartTime = InterUTCTime.CREATOR.createFromParcel(in);
        mDuringTime = InterUTCTime.CREATOR.createFromParcel(in);
        mstrEventName = in.readString();
        mstrShortText = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(miEventID);
        mStartDate.writeToParcel(dest, flags);
        mStartTime.writeToParcel(dest, flags);
        mDuringTime.writeToParcel(dest, flags);
//		dest.writeParcelable(mStartDate, flags);
//		dest.writeParcelable(mStartTime, flags);
//		dest.writeParcelable(mDuringTime, flags);
        dest.writeString(mstrEventName);
        dest.writeString(mstrShortText);
    }
}