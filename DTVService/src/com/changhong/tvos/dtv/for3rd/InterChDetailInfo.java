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
 * InterChDetailInfo
 */
public class InterChDetailInfo implements Parcelable {

    /** MONO **/
    public static final int DTV_CHANNEL_AUDIO_MODE_MONO = 0;

    /** STERO **/
    public static final int DTV_CHANNEL_AUDIO_MODE_STERO = 1;

    /** 节目位置 **/
    public int miChannelIndex;

    /** 节目号 **/
    public int miChannelnumber;

    /** 服务号 **/
    public String mstrServiceName;

    /** {@link For3rdConst.ConstServiceType ConstServiceType}**/
    public int miServiceType;

    /** (false:fta true:scrambled) **/
    public boolean mbScrambled;

    /** 载波 **/
    public int miCarrierIndex;

    /** 信号等级 **/
    public int miSignalLevel;

    /** 信号质量 **/
    public int miSignalQuality;

    /** ServiceID **/
    public int miServiceID;

    /** TSID **/
    public int miTSID;

    /** Orignal Network ID **/
    public int miOrgNetID;

    /** {@link For3rdConst.ConstVideoEncodeType ConstVideoEncodeType}**/
    public int miVideoType;

    /** {@link For3rdConst.ConstAudioEncodeType ConstAudioEncodeType}**/
    public int miAudioType;


    public InterAudioTrack mAudioTrackInfo;

    /** {@link For3rdConst.ConstantAudioMode For3rdConst.ConstantAudioMode}��**/
    public int miAudioMode;


    /** 音量 **/
    public int miBanlenceVolume;

    /** 符号率 **/
    public String msRating;

    /** 锁频 **/
    public boolean mbLock;

    /** 是否跳过 **/
    public boolean mbSkip;

    /**  **/
    public boolean mbFav;

    /**URI **/
    public String mstrURI;

    public InterChDetailInfo() {
        mAudioTrackInfo = new InterAudioTrack();
    }

    public static final Creator<InterChDetailInfo> CREATOR = new Parcelable.Creator<InterChDetailInfo>() {
        public InterChDetailInfo createFromParcel(Parcel source) {
            return new InterChDetailInfo(source);
        }

        public InterChDetailInfo[] newArray(int size) {
            return new InterChDetailInfo[size];
        }
    };

    private InterChDetailInfo(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    /**
     * 序列化节目详细信息
     */
    public void writeToParcel(Parcel dest, int arg1) {

        dest.writeInt(miChannelIndex);
        dest.writeInt(miChannelnumber);
        dest.writeString(mstrServiceName);
        dest.writeInt(miServiceType);
        dest.writeInt(((mbScrambled) ? (1) : (0)));
        dest.writeInt(miCarrierIndex);

        dest.writeInt(miSignalLevel);
        dest.writeInt(miSignalQuality);

        dest.writeInt(miServiceID);
        dest.writeInt(miTSID);
        dest.writeInt(miOrgNetID);

        dest.writeInt(miVideoType);
        dest.writeInt(miAudioType);

        mAudioTrackInfo.writeToParcel(dest, arg1);

        dest.writeInt(miAudioMode);

        dest.writeInt(miBanlenceVolume);
        dest.writeString(msRating);
        dest.writeInt(((mbLock) ? (1) : (0)));
        dest.writeInt(((mbSkip) ? (1) : (0)));
        dest.writeInt(((mbFav) ? (1) : (0)));

        dest.writeString(mstrURI);
    }

    public void readFromParcel(Parcel in) {

        miChannelIndex = in.readInt();
        miChannelnumber = in.readInt();
        mstrServiceName = in.readString();
        miServiceType = in.readInt();
        mbScrambled = (0 != in.readInt());
        miCarrierIndex = in.readInt();
        miSignalLevel = in.readInt();
        miSignalQuality = in.readInt();
        miServiceID = in.readInt();
        miTSID = in.readInt();
        miOrgNetID = in.readInt();
        miVideoType = in.readInt();
        miAudioType = in.readInt();
        mAudioTrackInfo = InterAudioTrack.CREATOR.createFromParcel(in);
        miAudioMode = in.readInt();
        miBanlenceVolume = in.readInt();
        msRating = in.readString();
        mbLock = (0 != in.readInt());
        mbSkip = (0 != in.readInt());
        mbFav = (0 != in.readInt());
        mstrURI = in.readString();
    }
}