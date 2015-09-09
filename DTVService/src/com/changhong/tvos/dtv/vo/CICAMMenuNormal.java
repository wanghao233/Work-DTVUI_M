/**
 * @filename DTV CI CA��ͨ�˵���Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**  **/
public class CICAMMenuNormal extends CICAMMenuBase implements Parcelable {
    /**  **/
    public String mstrTitle;

    /**  **/
    public String mstrSubtitle;

    /**  **/
    public String mstrHelp;

    /**  **/
    public int miOperateItems;

    /**  **/
    public String[] mastrOperateList;

    /**  **/
    public int miRow;

    /**  **/
    public int miCol;

    /**  **/
    public String[] mastrContentList;

    /**  **/
    public String[] mastrContentListTitle;

    public CICAMMenuNormal(int iMsgType,
                           int iMsgPriority, int iMsgID, int iAbsX, int iAbsY,
                           int iSubRelativePos, int iMainRelativePos,

                           int iMenuID, int iParentID, boolean bDefaultOperate,
                           int iMenuType,
                           String mstrTitle, String mstrSubtitle,
                           String mstrHelp, int miOperateItems, String[] mastrOperateList,
                           int miRow, int miCol, String[] mastrContentList, String[] mastrContentListTitle
    ) {
        super(iMsgType, iMsgPriority, iMsgID, iAbsX, iAbsY, iSubRelativePos,
                iMainRelativePos, iMenuID, iParentID, bDefaultOperate,
                iMenuType);


        this.mstrTitle = mstrTitle;
        this.mstrSubtitle = mstrSubtitle;
        this.mstrHelp = mstrHelp;
        this.miOperateItems = miOperateItems;
        this.mastrOperateList = mastrOperateList;
        this.miRow = miRow;
        this.miCol = miCol;
        this.mastrContentList = mastrContentList;
        this.mastrContentListTitle = mastrContentListTitle;

        Log.i("CICAMMenuNormal", "mstrTitle =" + mstrTitle
                + ",mstrSubtitle = " + mstrSubtitle
                + ",mstrHelp = " + mstrHelp);
        if (miOperateItems > 0 && mastrOperateList != null) {
            int i;
            for (i = 0; i < miOperateItems; i++) {
                Log.i("CICAMMenuNormal", "---mastrOperateList[" + i + "]=" + mastrOperateList[i]);
            }
        }
    }

    public static final Creator<CICAMMenuNormal> CREATOR = new Parcelable.Creator<CICAMMenuNormal>() {
        public CICAMMenuNormal createFromParcel(Parcel source) {
            return new CICAMMenuNormal(source);
        }

        public CICAMMenuNormal[] newArray(int size) {
            return new CICAMMenuNormal[size];
        }
    };

    private CICAMMenuNormal(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        super.writeToParcel(dest, arg1);
        Log.i("parcel wx3", "writeToParcel");

        dest.writeString(mstrTitle);
        dest.writeString(mstrSubtitle);
        dest.writeString(mstrHelp);

        dest.writeInt(miOperateItems);

        Log.i("parcel", "CICAMMenuNormal write 2");

        if (miOperateItems > 0) {
            Log.i("parcel", "CICAMMenuNormal write opitems = "
                    + mastrOperateList + "OperateList len =" + mastrOperateList.length);
            dest.writeStringArray(mastrOperateList);
        }

        dest.writeInt(miRow);
        dest.writeInt(miCol);
        if (miCol > 0 && miRow > 0) {
            Log.i("parcel", "CICAMMenuNormal write [miCol, miRow] = ["
                    + miCol + "," + miRow + "]");

            dest.writeStringArray(mastrContentList);
            dest.writeStringArray(mastrContentListTitle);
        }
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);


        mstrTitle = in.readString();

        mstrSubtitle = in.readString();

        mstrHelp = in.readString();

        miOperateItems = in.readInt();

        Log.i("parcel", "CICAMMenuNormal read 3");

        if (miOperateItems > 0) {
            mastrOperateList = new String[miOperateItems];
            in.readStringArray(mastrOperateList);

        }
        miRow = in.readInt();
        miCol = in.readInt();

        Log.i("parcel", "CICAMMenuNormal write [miCol, miRow] = ["
                + miCol + "," + miRow + "]");

        if (miCol > 0 && miRow > 0) {
            mastrContentList = new String[miCol * miRow];
            mastrContentListTitle = new String[miCol];

            in.readStringArray(mastrContentList);
            in.readStringArray(mastrContentListTitle);
        }

        Log.i("parcel", "CICAMMenuNormal read end");
    }
}
