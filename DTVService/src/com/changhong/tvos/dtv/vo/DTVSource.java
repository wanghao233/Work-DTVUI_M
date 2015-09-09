
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class DTVSource implements Parcelable {
	public int    miSourceType;
	public int    miSourceID;
	public String miSourceName;
	public boolean    mbCanEdit;
	public boolean    mbPreset;


	public DTVSource(
			int    miSourceType,
			int    miSourceID,
			String miSourceName,
			boolean    mbCanEdit,
            boolean mbPreset) {
		this.miSourceType = miSourceType;
		this.miSourceID  = miSourceID;
		this.miSourceName = miSourceName;
		this.mbCanEdit = mbCanEdit;
		this.mbPreset  = mbPreset;
	}

	public int describeContents() {
		return 0;
	}

	public String toString() {
		return "[]";
	}

	public static final Creator<DTVSource> CREATOR = new Parcelable.Creator<DTVSource>() {
		public DTVSource createFromParcel(Parcel source) {
			return new DTVSource(source);
		}

		public DTVSource[] newArray(int size) {
			return new DTVSource[size];
		}
	};

	private DTVSource(Parcel source) {
		readFromParcel(source);
	}


	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeInt(miSourceType);
		dest.writeInt(miSourceID);
		dest.writeString(miSourceName);
        dest.writeInt((mbCanEdit == true) ? 1 : 0);
        dest.writeInt((mbPreset == true) ? 1 : 0);
	}


	public void readFromParcel(Parcel in) {
		miSourceType = in.readInt();
		miSourceID   = in.readInt();
		miSourceName = in.readString();
        mbCanEdit = (in.readInt() == 1) ? true : false;
        mbPreset = (in.readInt() == 1) ? true : false;
	}
}
