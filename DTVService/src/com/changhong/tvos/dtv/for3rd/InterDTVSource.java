
package com.changhong.tvos.dtv.for3rd;

import android.os.Parcel;
import android.os.Parcelable;

public class InterDTVSource implements Parcelable{
	public int    miSourceType;
	public int    miSourceID;
	public String miSourceName;

	
	public InterDTVSource(
			int    miSourceType,
			int    miSourceID,
			String miSourceName){
		this.miSourceType = miSourceType;
		this.miSourceID  = miSourceID;
		this.miSourceName = miSourceName;
	}
	
	public int describeContents() {
		return 0;
	}

	public String toString() {
		return "[]";
	}

	public static final Creator<InterDTVSource> CREATOR = new Parcelable.Creator<InterDTVSource>() {
		public InterDTVSource createFromParcel(Parcel source) {
			return new InterDTVSource(source);
		}

		public InterDTVSource[] newArray(int size) {
			return new InterDTVSource[size];
		}
	};

	private InterDTVSource(Parcel source) {
		readFromParcel(source);
	}


	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeInt(miSourceType);
		dest.writeInt(miSourceID);
		dest.writeString(miSourceName);
	}


	public void readFromParcel(Parcel in) {
		miSourceType = in.readInt();
		miSourceID   = in.readInt();
		miSourceName = in.readString();
	}
}
