/**
 * @filename DTV �汾
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

/** **/
public class StartControlInfo implements Parcelable {

    public static final int DTV_START_SERVICE_ADV_PIC = 0;

    public static final int DTV_START_SERVICE_ADV_PIC_END = 1;

    public static final int DTV_START_SERVICE_ADV_CHANNEL = 2;

    public static final int DTV_START_SERVICE_ADV_CHANNEL_END = 3;

    public static final int DTV_START_SERVICE_DISP_PROMPT = 4;

    public static final int DTV_START_SERVICE_DISP_PROMPT_END = 5;

    public static final int DTV_START_SERVICE_SCAN_PROGRAM = 6;

    public static final int DTV_START_SERVICE_SCAN_PROGRAM_END = 7;

    public static final int DTV_BOOT_SVC_AUTO_SCAN_CONFIRM = 8;

    public static final int DTV_START_SERVICE_END = 0x100;

    /**  **/
    public int miMessageType;

    /**  **/
    public int miStartChannelIndex;

    /** ַ**/
    public String mstrPicFileName;

    /**  **/
    public Rect mPosition;

    public StartControlInfo(
            int iMessageType,
            int iStartChannelIndex,
            String strPicFileName,
            Rect Position) {
        this.miMessageType = iMessageType;
        this.miStartChannelIndex = iStartChannelIndex;
        this.mstrPicFileName = strPicFileName;
        this.mPosition = Position;
    }

    public static final Creator<StartControlInfo> CREATOR = new Parcelable.Creator<StartControlInfo>() {
        public StartControlInfo createFromParcel(Parcel source) {
            return new StartControlInfo(source);
        }

        public StartControlInfo[] newArray(int size) {
            return new StartControlInfo[size];
        }
    };

    private StartControlInfo(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miMessageType);
        dest.writeInt(miStartChannelIndex);
        dest.writeString(mstrPicFileName);
        mPosition.writeToParcel(dest, arg1);
    }

    public void readFromParcel(Parcel in) {
        miMessageType = in.readInt();
        miStartChannelIndex = in.readInt();
        mstrPicFileName = in.readString();
        mPosition = Rect.CREATOR.createFromParcel(in);
    }
}
