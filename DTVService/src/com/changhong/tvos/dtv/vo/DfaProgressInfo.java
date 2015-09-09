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
public class DfaProgressInfo extends DfaMessageBase implements Parcelable {
    /**  **/
    public String mstrTitle;

    /**  **/
    public String mstrSubtitle;

    /**  **/
    public String[] mastrPromptList;

    /**  **/
    public int miProgress;

    public DfaProgressInfo() {

    }

    public DfaProgressInfo(int iMsgType, int iMsgPriority,
                           int iMsgID, int iAbsX, int iAbsY, int iSubRelativePos,
                           int iMainRelativePos, String strTitle, String strSubtitle,
                           String[] astrPromptList, int iProgress) {

        super(iMsgType, iMsgPriority, iMsgID, iAbsX, iAbsY, iSubRelativePos,
                iMainRelativePos);

        this.mstrTitle = strTitle;
        this.mstrSubtitle = strSubtitle;
        this.mastrPromptList = astrPromptList;
        this.miProgress = iProgress;
    }

    public static final Creator<DfaProgressInfo> CREATOR = new Parcelable.Creator<DfaProgressInfo>() {
        public DfaProgressInfo createFromParcel(Parcel source) {
            return new DfaProgressInfo(source);
        }

        public DfaProgressInfo[] newArray(int size) {
            return new DfaProgressInfo[size];
        }
    };

    private DfaProgressInfo(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        super.writeToParcel(dest, arg1);

        dest.writeString(mstrTitle);
        dest.writeString(mstrSubtitle);
        if (mastrPromptList != null) {
            dest.writeInt(mastrPromptList.length);
            dest.writeStringArray(mastrPromptList);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(miProgress);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);

        mstrTitle = in.readString();
        mstrSubtitle = in.readString();
        int length = in.readInt();
        if (length > 0) {
            mastrPromptList = new String[length];
            in.readStringArray(mastrPromptList);
        }
        miProgress = in.readInt();
    }
}
