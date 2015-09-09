/**
 * @filename DTV ���ͼ��������?
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 **/
public class PictureParam implements Parcelable {
    /**  **/
    public int miContrast;

    /**  **/
    public int miChroma;

    /**  **/
    public int miSaturation;

    /**  **/
    public int miBrightness;

    public PictureParam(
            int miContrast,
            int miChroma,
            int miSaturation,
            int miBrightness) {
        this.miContrast = miContrast;
        this.miChroma = miChroma;
        this.miSaturation = miSaturation;
        this.miBrightness = miBrightness;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<PictureParam> CREATOR = new Parcelable.Creator<PictureParam>() {
        public PictureParam createFromParcel(Parcel source) {
            return new PictureParam(source);
        }

        public PictureParam[] newArray(int size) {
            return new PictureParam[size];
        }
    };

    private PictureParam(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miContrast);
        dest.writeInt(miChroma);
        dest.writeInt(miSaturation);
        dest.writeInt(miBrightness);
    }

    public void readFromParcel(Parcel in) {
        miContrast = in.readInt();
        miChroma = in.readInt();
        miSaturation = in.readInt();
        miBrightness = in.readInt();
    }
}
