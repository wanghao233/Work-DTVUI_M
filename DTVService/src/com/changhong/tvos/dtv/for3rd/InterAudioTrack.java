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
 * InterAudioTrack
 **/
public class InterAudioTrack implements Parcelable {

	/** 音轨语言 **/
	public String[] mstrAudioLanguage;

	/** 音轨数 **/
	public int miTrackNumb;

	/** 当前选择 **/
	public int miCurrSelect;

	public int describeContents() {
		return 0;
	}

	public String toString() {
		return "[]";
	}

	public static final Creator<InterAudioTrack> CREATOR = new Parcelable.Creator<InterAudioTrack>() {
		public InterAudioTrack createFromParcel(Parcel source) {
			return new InterAudioTrack(source);
		}

		public InterAudioTrack[] newArray(int size) {
			return new InterAudioTrack[size];
		}
	};

	private InterAudioTrack(Parcel source) {
		readFromParcel(source);
	}

	public InterAudioTrack() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 取得音轨语言
	 */
	public void readFromParcel(Parcel in) {
		int length = in.readInt();
		mstrAudioLanguage = new String[length];
		in.readStringArray(mstrAudioLanguage);
		miTrackNumb = in.readInt();
		miCurrSelect = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(mstrAudioLanguage.length);
		dest.writeStringArray(mstrAudioLanguage);
		dest.writeInt(miTrackNumb);
		dest.writeInt(miCurrSelect);
	}	
}