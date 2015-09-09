/**
 * @filename DTV CACAMģ����Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/** CICA **/
public class CICAMInformation implements Parcelable {
    /** CI/CA **/
    public int mi_Type;

    /** CI/CA **/
    public String mstr_Description;

    public CICAMInformation(int mi_Type, String mstr_Description) {
        this.mi_Type = mi_Type;
        this.mstr_Description = mstr_Description;
    }

    public static final Creator<CICAMInformation> CREATOR = new Parcelable.Creator<CICAMInformation>() {
        public CICAMInformation createFromParcel(Parcel source) {
            return new CICAMInformation(source);
        }

        public CICAMInformation[] newArray(int size) {
            return new CICAMInformation[size];
        }
    };

    private CICAMInformation(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(mi_Type);
        dest.writeString(mstr_Description);
    }

    public void readFromParcel(Parcel in) {
        mi_Type = in.readInt();
        mstr_Description = in.readString();
    }
}
