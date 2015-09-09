/**
 * @filename DTV ȷ�ϲ˵�
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class CICAMMenuConfirm extends CICAMMenuBase implements Parcelable {
    /**  **/
    public String mstrTitle;

    /**  **/
    public String mstrPrompt;

    public CICAMMenuConfirm(int iMsgType,
                            int iMsgPriority, int iMsgID, int iAbsX, int iAbsY,
                            int iSubRelativePos, int iMainRelativePos, int iMenuID,
                            int iParentID, boolean bDefaultOperate, int iMenuType,
                            String strTitle, String strPrompt) {

        super(iMsgType, iMsgPriority, iMsgID, iAbsX, iAbsY, iSubRelativePos,
                iMainRelativePos, iMenuID, iParentID, bDefaultOperate,
                iMenuType);

        this.mstrTitle = strTitle;
        this.mstrPrompt = strPrompt;
    }

    public static final Creator<CICAMMenuConfirm> CREATOR = new Parcelable.Creator<CICAMMenuConfirm>() {
        public CICAMMenuConfirm createFromParcel(Parcel source) {
            return new CICAMMenuConfirm(source);
        }

        public CICAMMenuConfirm[] newArray(int size) {
            return new CICAMMenuConfirm[size];
        }
    };

    private CICAMMenuConfirm(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        super.writeToParcel(dest, arg1);

        dest.writeString(mstrTitle);
        dest.writeString(mstrPrompt);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);

        mstrTitle = in.readString();
        mstrPrompt = in.readString();
    }
}
