/**
 * @filename DTV ���������Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class DfaResultInfo extends DfaMessageBase implements Parcelable {
    /**  **/
    public String mstrTitle;

    /**  **/
    public String mstrSubtitle;

    /**  **/
    public String mstrPrompt;

    /**  **/
    public int miResult;

    public DfaResultInfo() {

    }

    public DfaResultInfo(int iMsgType, int iMsgPriority,
                         int iMsgID, int iAbsX, int iAbsY, int iSubRelativePos,
                         int iMainRelativePos, String strTitle, String strSubtitle,
                         String strPrompt, int iResult) {

        super(iMsgType, iMsgPriority, iMsgID, iAbsX, iAbsY, iSubRelativePos,
                iMainRelativePos);

        this.mstrTitle = strTitle;
        this.mstrSubtitle = strSubtitle;
        this.mstrPrompt = strPrompt;
        this.miResult = iResult;
    }

    public static final Creator<DfaResultInfo> CREATOR = new Parcelable.Creator<DfaResultInfo>() {
        public DfaResultInfo createFromParcel(Parcel source) {
            return new DfaResultInfo(source);
        }

        public DfaResultInfo[] newArray(int size) {
            return new DfaResultInfo[size];
        }
    };

    private DfaResultInfo(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        super.writeToParcel(dest, arg1);

        dest.writeString(mstrTitle);
        dest.writeString(mstrSubtitle);
        dest.writeString(mstrPrompt);
        dest.writeInt(miResult);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);

        mstrTitle = in.readString();
        mstrSubtitle = in.readString();
        mstrPrompt = in.readString();
        miResult = in.readInt();
    }
}
