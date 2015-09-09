/**
 * @filename DTV ����������Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class DfaTriggerInfo extends DfaMessageBase implements Parcelable {
    /**  **/
    public String mstrTitle;

    /**  **/
    public String mstrSubtitle;

    /**  **/
    public String[] mastrList;

    public DfaTriggerInfo() {

    }

    public DfaTriggerInfo(int iMsgType, int iMsgPriority,
                          int iMsgID, int iAbsX, int iAbsY, int iSubRelativePos,
                          int iMainRelativePos, String strTitle, String strSubtitle,
                          String[] astrList) {

        super(iMsgType, iMsgPriority, iMsgID, iAbsX, iAbsY, iSubRelativePos,
                iMainRelativePos);

        this.mstrTitle = strTitle;
        this.mstrSubtitle = strSubtitle;
        this.mastrList = astrList;
    }

    public static final Creator<DfaTriggerInfo> CREATOR = new Parcelable.Creator<DfaTriggerInfo>() {
        public DfaTriggerInfo createFromParcel(Parcel source) {
            return new DfaTriggerInfo(source);
        }

        public DfaTriggerInfo[] newArray(int size) {
            return new DfaTriggerInfo[size];
        }
    };

    private DfaTriggerInfo(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        super.writeToParcel(dest, arg1);

        dest.writeString(mstrTitle);
        dest.writeString(mstrSubtitle);
        if (mastrList == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(mastrList.length);
            dest.writeStringArray(mastrList);
        }

    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);

        mstrTitle = in.readString();
        mstrSubtitle = in.readString();
        int length = in.readInt();
        if (length > 0) {
            in.readStringArray(mastrList);
        }
    }
}
