/**
 * @filename DTV DVB-TƵ����Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class DVBTCarrier extends CarrierInfo implements Parcelable {
    /** BandWidth {@link DTVConstant.BandWidth})**/
	public int miBandWidth;

	public DVBTCarrier(int miBandWidth) {
         this.miBandWidth = miBandWidth;
	}

	public int describeContents() {
		return 0;
	}

	public String toString() {
		return "[]";
	}

	public static final Creator<DVBTCarrier> CREATOR = new Parcelable.Creator<DVBTCarrier>() {
		public DVBTCarrier createFromParcel(Parcel source) {
			return new DVBTCarrier(source);
		}

		public DVBTCarrier[] newArray(int size) {
			return new DVBTCarrier[size];
		}
	};

	private DVBTCarrier(Parcel source) {
		readFromParcel(source);
	}


	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeInt(miDemodType);
		dest.writeInt(miIndex);
		dest.writeInt(miTsID);
		dest.writeInt(miOrgNetId);
		dest.writeInt(miFrequencyK);
		dest.writeByte(mcSpectrum);
		dest.writeInt(miBandWidth);
	}

	public void readFromParcel(Parcel in) {
		miDemodType = in.readInt();
		miIndex = in.readInt();
		miTsID = in.readInt();
		miOrgNetId = in.readInt();
		miFrequencyK = in.readInt();
		mcSpectrum = in.readByte();
		miBandWidth = in.readInt();
	}
}
