/**
 * @filename DTV CI CA
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class CICAMMenuBase extends CICAMMessageBase implements Parcelable {

    /** {@link DTVConstant.CICAMMenuID CICAMMenuID})**/
    public int miMenuID;

    /**  **/
    public int miParentID;

    /**  **/
    public boolean mbDefaultOperate;

    /** {@link DTVConstant#ConstCICAMMenuType ConstCICAMMenuType}**/
    public int miMenuType;

    public CICAMMenuBase() {

    }

    public CICAMMenuBase(int iMsgType,
                         int iMsgPriority, int iMsgID, int iAbsX, int iAbsY,
                         int iSubRelativePos, int iMainRelativePos, int iMenuID,
                         int iParentID, boolean bDefaultOperate, int iMenuType) {
        super(iMsgType, iMsgPriority, iMsgID, iAbsX, iAbsY, iSubRelativePos,
                iMainRelativePos);
        miMenuID = iMenuID;
        miParentID = iParentID;
        mbDefaultOperate = bDefaultOperate;
        miMenuType = iMenuType;
    }

    public static final Creator<CICAMMenuBase> CREATOR = new Parcelable.Creator<CICAMMenuBase>() {
        public CICAMMenuBase createFromParcel(Parcel source) {
            return new CICAMMenuBase(source);
        }

        public CICAMMenuBase[] newArray(int size) {
            return new CICAMMenuBase[size];
        }
    };

    private CICAMMenuBase(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        super.writeToParcel(dest, arg1);

        dest.writeInt(miMenuID);
        dest.writeInt(miParentID);
        dest.writeInt(mbDefaultOperate == true ? 1 : 0);
        dest.writeInt(miMenuType);

    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        miMenuID = in.readInt();
        miParentID = in.readInt();
        mbDefaultOperate = in.readInt() == 1 ? true : false;
        miMenuType = in.readInt();
    }
}
