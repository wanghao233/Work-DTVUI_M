/**
 * @filename
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.for3rd;

import android.os.Parcel;
import android.os.Parcelable;


/** Operator **/
public class InterOperator implements Parcelable {
    /** 运营商名字 **/
	public String mstrOperatorName;

    /** 运营商code **/
	public int miOperatorCode;

    public InterOperator(
			 String mstrOperatorName,
			 int miOperatorCode) {
		this.mstrOperatorName = mstrOperatorName;
		this.miOperatorCode = miOperatorCode;
	}

	public static final Creator<InterOperator> CREATOR = new Parcelable.Creator<InterOperator>() {
		public InterOperator createFromParcel(Parcel source) {
			return new InterOperator(source);
		}

		public InterOperator[] newArray(int size) {
			return new InterOperator[size];
		}
	};

	private InterOperator(Parcel source) {
		readFromParcel(source);
	}

	public InterOperator() {
		// TODO Auto-generated constructor stub
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(mstrOperatorName);
		dest.writeInt(miOperatorCode);
	}

	public void readFromParcel(Parcel in) {
		mstrOperatorName = in.readString();
		miOperatorCode = in.readInt();
	}
}