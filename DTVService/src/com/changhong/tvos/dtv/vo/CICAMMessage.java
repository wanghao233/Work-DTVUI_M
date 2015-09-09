/**
 * @filename DTV CI CA�б���Ϣ��Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class CICAMMessage extends CICAMMessageBase implements Parcelable {

	public CICAMMenuList mMenuList;
	public CICAMPrompt mPrompt;

	public CICAMMessage(int iMsgType,
			int iMsgPriority, int iMsgID, int iAbsX, int iAbsY,
			int iSubRelativePos, int iMainRelativePos, CICAMMenuList MenuList,
			CICAMPrompt Prompt) {
		super(iMsgType, iMsgPriority, iMsgID, iAbsX, iAbsY, iSubRelativePos,
				iMainRelativePos);
		this.mMenuList = MenuList;
		this.mPrompt = Prompt;
		// Log.v ( "-->CiCaCallback", "mi_MsgType:" + mi_MsgType );
	}

	public static final Creator<CICAMMessage> CREATOR = new Parcelable.Creator<CICAMMessage>() {
		public CICAMMessage createFromParcel(Parcel source) {
			return new CICAMMessage(source);
		}

		public CICAMMessage[] newArray(int size) {
			return new CICAMMessage[size];
		}
	};

	private CICAMMessage(Parcel source) {
		readFromParcel(source);
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int arg1) {
		super.writeToParcel(dest, arg1);

		mMenuList.writeToParcel(dest, arg1);
		mPrompt.writeToParcel(dest, arg1);
	}

	public void readFromParcel(Parcel in) {
		super.readFromParcel(in);
		mMenuList.readFromParcel(in);
		mPrompt.readFromParcel(in);
	}
}
