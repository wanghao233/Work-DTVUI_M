/**
 * @filename DTV ��Ҫ�û�����Ĳ˵���Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class CICAMMenuInput extends CICAMMenuBase implements Parcelable {
    /**  **/
    public String mstrTitle;

    /**  **/
    public String mstrHelp;

    /**  **/
    public int miItems;

    /**  **/
    public CICAMEditBox[] maEditboxList;

    public CICAMMenuInput() {

    }

    public CICAMMenuInput(int iMsgType,
                          int iMsgPriority, int iMsgID, int iAbsX, int iAbsY,
                          int iSubRelativePos, int iMainRelativePos, int iMenuID,
                          int iParentID, boolean bDefaultOperate, int iMenuType,
                          String strTitle, String strHelp, int iItems,
                          CICAMEditBox[] aEditboxList) {

        super(iMsgType, iMsgPriority, iMsgID, iAbsX, iAbsY, iSubRelativePos,
                iMainRelativePos, iMenuID, iParentID, bDefaultOperate,
                iMenuType);

        this.mstrTitle = strTitle;
        this.mstrHelp = strHelp;
        this.miItems = iItems;
        this.maEditboxList = aEditboxList;
    }

    public static final Creator<CICAMMenuInput> CREATOR = new Parcelable.Creator<CICAMMenuInput>() {
        public CICAMMenuInput createFromParcel(Parcel source) {
            return new CICAMMenuInput(source);
        }

        public CICAMMenuInput[] newArray(int size) {
            return new CICAMMenuInput[size];
        }
    };

    private CICAMMenuInput(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        int i;
        super.writeToParcel(dest, arg1);

        dest.writeString(mstrTitle);
        dest.writeString(mstrHelp);
        dest.writeInt(miItems);
        if (maEditboxList != null) {
            for (i = 0; i < miItems; i++) {
                maEditboxList[i].writeToParcel(dest, arg1);
            }
        }
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);

        mstrTitle = in.readString();
        mstrHelp = in.readString();
        miItems = in.readInt();

        // Log.e("zg test ", "mao_EditboxListb statt");

        if (miItems > 0) {
            maEditboxList = new CICAMEditBox[miItems];

            // Log.e("zg test ",
            // "setOnItemClickListener>>onItemClick>>menuList.mastr_List null ");

            for (int i = 0; i < miItems; i++) {
                maEditboxList[i] = CICAMEditBox.CREATOR.createFromParcel(in);
            }
        }
    }

}
