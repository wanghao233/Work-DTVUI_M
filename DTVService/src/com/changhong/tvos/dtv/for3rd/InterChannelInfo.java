/**
 * @filename
 * @author:
 * @date: 
 * @version 0.1
 */
package com.changhong.tvos.dtv.for3rd;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * ChannelInfo
 */
public class InterChannelInfo implements Parcelable {

	/** 节目位置 **/
	public int miChannelIndex;

	/** 节目号 **/
	public int miChannelnumber;

	/** 换台号 **/
	public String mstrServiceName;

	/** {@link For3rdConst.ConstServiceType ConstServiceType}**/
	public int miServiceType;

	/** {@link For3rdConst.ConstVideoEncodeType ConstVideoEncodeType}**/
	public int miVideoType;

	/** {@link For3rdConst.ConstAudioEncodeType ConstAudioEncodeType}**/
	public int miAudioType;
		
	/** false:FTA; true: **/
	public boolean mbScrambled;

	/** {@link For3rdConst.ConstAudioMode ConstAudioMode}**/
	public int miAudioMode;

	/** 符号率 **/
	public String mstrRating;

	/** 音量 **/
	public int miBanlenceVolume;

	/** 是否锁屏 **/
	public boolean mbLock;

	/** 是否跳过 **/
	public boolean mbSkip;

	/** 是否主频 **/
	public boolean mbFav;

	/** URI地址 **/
	public String mstrURI;
	
	public int describeContents() {
		return 0;
	}

	public String toString() {
		return "[]";
	}

	public static final Creator<InterChannelInfo> CREATOR = new Parcelable.Creator<InterChannelInfo>() {
		public InterChannelInfo createFromParcel(Parcel source) {
			return new InterChannelInfo(source);
		}

		public InterChannelInfo[] newArray(int size) {
			return new InterChannelInfo[size];
		}
	};

	private InterChannelInfo(Parcel source) {
		readFromParcel(source);
	}

	public InterChannelInfo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 序列化节目信息
	 */
	public void readFromParcel(Parcel in) {
		miChannelIndex = in.readInt();
		miChannelnumber = in.readInt();
		mstrServiceName = in.readString();
		miServiceType = in.readInt();
		miVideoType = in.readInt();
		miAudioType = in.readInt();
		mbScrambled = (in.readInt() == 1);
		miAudioMode = in.readInt();
		mstrRating = in.readString();
		miBanlenceVolume = in.readInt();
		mbLock = (in.readInt() == 1);
		mbSkip = (in.readInt() == 1);
		mbFav = (in.readInt() == 1);
		mstrURI = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(miChannelIndex);
		dest.writeInt(miChannelnumber);
		dest.writeString(mstrServiceName);
		dest.writeInt(miServiceType);
		dest.writeInt(miVideoType);
		dest.writeInt(miAudioType);
		dest.writeInt((mbScrambled) ? 1 : 0);
		dest.writeInt(miAudioMode);
		dest.writeString(mstrRating);
		dest.writeInt(miBanlenceVolume);
		dest.writeInt((mbLock) ? 1 : 0);
		dest.writeInt((mbSkip) ? 1 : 0);
		dest.writeInt((mbFav) ? 1 : 0);
		dest.writeString(mstrURI);
	}	
}