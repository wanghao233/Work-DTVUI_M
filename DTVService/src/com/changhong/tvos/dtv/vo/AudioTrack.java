/**
 * @filename DTV �������?
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * AudioTrack
 **/
public class AudioTrack implements Parcelable {
    /**
     * ISO 639-1
     **/
    public String[] mAudioLanguagelist;

    /** **/
    public int miCurrSelect;

    public AudioTrack() {
    }

    public AudioTrack(String[] mAudioLanguagelist, int miCurrSelect) {
        this.mAudioLanguagelist = mAudioLanguagelist;
        this.miCurrSelect = miCurrSelect;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<AudioTrack> CREATOR = new Parcelable.Creator<AudioTrack>() {
        public AudioTrack createFromParcel(Parcel source) {
            return new AudioTrack(source);
        }

        public AudioTrack[] newArray(int size) {
            return new AudioTrack[size];
        }
    };

    private AudioTrack(Parcel source) {
        readFromParcel(source);
    }

    public void writeToParcel(Parcel dest, int arg1) {
        if (mAudioLanguagelist == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(mAudioLanguagelist.length);
            dest.writeStringArray(mAudioLanguagelist);
        }
        dest.writeInt(miCurrSelect);
    }

    public void readFromParcel(Parcel in) {
        int length = in.readInt();
        if (length > 0) {
            mAudioLanguagelist = new String[length];
            in.readStringArray(mAudioLanguagelist);
        }
        miCurrSelect = in.readInt();
    }
}
