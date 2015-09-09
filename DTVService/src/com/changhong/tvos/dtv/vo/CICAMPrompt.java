/**
 * @filename DTV CI CA��ͨ��ʾ��Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class CICAMPrompt extends CICAMMessageBase implements Parcelable {
    /**  **/
	public String mstrPrompt;

	public CICAMPrompt(int iMsgType,
			int iMsgPriority, int iMsgID, int iAbsX, int iAbsY,
			int iSubRelativePos, int iMainRelativePos, String strPrompt) {
		super(iMsgType, iMsgPriority, iMsgID, iAbsX, iAbsY, iSubRelativePos,
				iMainRelativePos);
		this.mstrPrompt = strPrompt;
	}

	public static final Creator<CICAMPrompt> CREATOR = new Parcelable.Creator<CICAMPrompt>() {
		public CICAMPrompt createFromParcel(Parcel source) {
			return new CICAMPrompt(source);
		}

		public CICAMPrompt[] newArray(int size) {
			return new CICAMPrompt[size];
		}
	};

	private CICAMPrompt(Parcel source) {
		readFromParcel(source);
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int arg1) {
		super.writeToParcel(dest, arg1);
		dest.writeString(mstrPrompt);
	}

	public void readFromParcel(Parcel in) {
		super.readFromParcel(in);
		mstrPrompt = in.readString();
	}
}
