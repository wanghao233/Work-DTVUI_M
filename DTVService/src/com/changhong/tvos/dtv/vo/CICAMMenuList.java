/**
 * @filename DTV CI CA�б�˵���Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**  **/
public class CICAMMenuList extends CICAMMenuBase implements Parcelable {
    /**  **/
    public String mstrTitle;

    /**  **/
    public String mstrSubtitle;

    /**  **/
    public int miItems;

    /**  **/
    public String[] mastrList;

    /**  **/
    public String mstrHelp;

    public CICAMMenuList(int iMsgType,
                         int iMsgPriority, int iMsgID, int iAbsX, int iAbsY,
                         int iSubRelativePos, int iMainRelativePos,

                         int iMenuID, int iParentID, boolean bDefaultOperate,
                         int iMenuType, String strTitle, String strSubtitle,
                         String strHelp, int iItems, String[] astrList) {
        super(iMsgType, iMsgPriority, iMsgID, iAbsX, iAbsY, iSubRelativePos,
                iMainRelativePos, iMenuID, iParentID, bDefaultOperate,
                iMenuType);

        Log.v("-->CH_DTV_CICA_MsgMenuList ", "begin CH_DTV_CICA_MsgMenuList"
                + this);

        this.mastrList = astrList;
        this.miItems = iItems;
        this.mstrHelp = strHelp;
        this.mstrSubtitle = strSubtitle;
        this.mstrTitle = strTitle;

        Log.v("-->CH_DTV_CICA_MsgMenuList ", "end CH_DTV_CICA_MsgMenuList"
                + this);
    }

    public static final Creator<CICAMMenuList> CREATOR = new Parcelable.Creator<CICAMMenuList>() {
        public CICAMMenuList createFromParcel(Parcel source) {
            return new CICAMMenuList(source);
        }

        public CICAMMenuList[] newArray(int size) {
            return new CICAMMenuList[size];
        }
    };

    private CICAMMenuList(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        super.writeToParcel(dest, arg1);

        dest.writeString(mstrTitle);
        dest.writeString(mstrSubtitle);

        if (null != mastrList) {
            if (miItems == mastrList.length) {
                dest.writeInt(miItems);
                dest.writeStringArray(mastrList);
            } else {
                Log.e("cica", "CICAMMenuList parcel. the array num error items ="
                        + miItems + "arraynum = " + mastrList.length);
                dest.writeInt(0);
            }
        } else {
            dest.writeInt(0);
        }

        dest.writeString(mstrHelp);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);

        mstrTitle = in.readString();
        mstrSubtitle = in.readString();
        miItems = in.readInt();
        if (miItems > 0) {
            mastrList = new String[miItems];
            in.readStringArray(mastrList);
        }
        mstrHelp = in.readString();
    }
}
