/**
 * @filename DTV CI CA��Ļ��ʾ��Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class CICAMSubtitle extends CICAMMessageBase implements Parcelable {
    /**  **/
    public String mstr_Subtitle;

    /**  **/
    public int mi_Showtime;

    public CICAMSubtitle() {
    }

    public CICAMSubtitle(int iMsgType, int iMsgPriority,
                         int iMsgID, int iAbsX, int iAbsY, int iSubRelativePos,
                         int iMainRelativePos, String subtitle, int showtime) {
        super(iMsgType, iMsgPriority, iMsgID, iAbsX, iAbsY, iSubRelativePos, iMainRelativePos);
        mstr_Subtitle = subtitle;
        mi_Showtime = showtime;
    }

    public static final Creator<CICAMSubtitle> CREATOR = new Parcelable.Creator<CICAMSubtitle>() {
        public CICAMSubtitle createFromParcel(Parcel source) {
            return new CICAMSubtitle(source);
        }

        public CICAMSubtitle[] newArray(int size) {
            return new CICAMSubtitle[size];
        }
    };

    private CICAMSubtitle(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        super.writeToParcel(dest, arg1);

        dest.writeString(mstr_Subtitle);
        dest.writeInt(mi_Showtime);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);

        mstr_Subtitle = in.readString();
        mi_Showtime = in.readInt();
    }
}
