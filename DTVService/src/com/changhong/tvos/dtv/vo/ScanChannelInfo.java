/**
 * @filename DTV ����ģ�鷴���Ľ�Ŀ��Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author:
 * @version 1.0
 */
public class ScanChannelInfo implements Parcelable {
    /**  **/
    public int miTotalTVCount;
    /**  **/
    public int miTotalRadioCount;
    /**  **/
    public String mstrCurChannelName;
    /**  **/
    public int miCurChannelType;

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<ScanChannelInfo> CREATOR = new Parcelable.Creator<ScanChannelInfo>() {
        public ScanChannelInfo createFromParcel(Parcel source) {
            return new ScanChannelInfo(source);
        }

        public ScanChannelInfo[] newArray(int size) {
            return new ScanChannelInfo[size];
        }
    };

    private ScanChannelInfo(Parcel source) {
        readFromParcel(source);
    }

    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miTotalTVCount);
        dest.writeInt(miTotalRadioCount);
        dest.writeString(mstrCurChannelName);
        dest.writeInt(miCurChannelType);
    }

    public void readFromParcel(Parcel in) {
        miTotalTVCount = in.readInt();
        miTotalRadioCount = in.readInt();
        mstrCurChannelName = in.readString();
        miCurChannelType = in.readInt();
    }
}