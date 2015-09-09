/**
 * @filename DTV CA�༭����Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class CICAMEditBox implements Parcelable {
    /**  **/
    public int miLength;

    /**  **/
    public boolean mbHide;

    /** false:true: **/
    public boolean mbAuto;

    /**  **/
    public String mstrPrompt;

    /**  **/
    public String mstrHelp;

    public CICAMEditBox() {
    }

    public CICAMEditBox(int iLength, boolean bHide, boolean bAuto,
                        String strPrompt, String strHelp) {
        this.miLength = iLength;
        this.mbHide = bHide;
        this.mbAuto = bAuto;
        this.mstrPrompt = strPrompt;
        this.mstrHelp = strHelp;
    }

    public static final Creator<CICAMEditBox> CREATOR = new Parcelable.Creator<CICAMEditBox>() {
        public CICAMEditBox createFromParcel(Parcel source) {
            return new CICAMEditBox(source);
        }

        public CICAMEditBox[] newArray(int size) {
            return new CICAMEditBox[size];
        }
    };

    private CICAMEditBox(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miLength);
        dest.writeInt((mbHide == true) ? 1 : 0);
        dest.writeInt((mbAuto == true) ? 1 : 0);
        dest.writeString(mstrPrompt);
        dest.writeString(mstrHelp);
    }

    public void readFromParcel(Parcel in) {
        miLength = in.readInt();
        mbHide = in.readInt() == 1 ? true : false;
        mbAuto = in.readInt() == 1 ? true : false;
        mstrPrompt = in.readString();
        mstrHelp = in.readString();
    }
}
