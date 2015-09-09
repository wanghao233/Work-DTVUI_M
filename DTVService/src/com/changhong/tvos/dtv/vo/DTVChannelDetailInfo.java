/**
 * @filename DTV ��Ŀ��ϸ��Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 **/
public class DTVChannelDetailInfo implements Parcelable {

    /**  **/
    public static final int DTV_CHANNEL_AUDIO_MODE_MONO = 0;

    /**  **/
    public static final int DTV_CHANNEL_AUDIO_MODE_STERO = 1;

    /**
     * {@link DTVChannelBaseInfo DTVChannelBaseInfo}{@link DTVChannelDetailInfo DTVChannelDetailInfo}
     * */
    public int miChannelIndex;

    /**  **/
    public int miChannelnumber;

    /**  **/
    public String mstrServiceName;

    /** {@link DTVConstant.ServiceType DTVConstant.ServiceType} **/
    public int miServiceType;

    /** (false:fta true:scrambled) **/
    public boolean mbScrambled;

    /** **/
    public int miCarrierIndex;

    /**  **/
    public int miSignalLevel;

    /**  **/
    public int miSignalQuality;

    /** ServiceID **/
    public int miServiceID;

    /** TSID **/
    public int miTSID;

    /** Orignal Network ID **/
    public int miOrgNetID;

    /** {@link DTVConstant#ConstVideoEncodeType DTVConst.ConstVideoEncodeType} **/
    public int miVideoType;

    /** {@link DTVConstant#ConstAudioEncodeType DTVConst.ConstAudioEncodeType} **/
    public int miAudioType;


    public AudioTrack mAudioTrackInfo;

    /** {@link DTVConstant.ConstSoundMode DTVConst.ConstSoundMode} **/
    public int miSoundMode;


    /**  **/
    public int miBanlenceVolume;

    /**  **/
    public String msRating;

    /**  **/
    public boolean mbLock;

    /**  **/
    public boolean mbSkip;

    /**  **/
    public boolean mbFav;
    /**  **/
    public int miAudioPID;
    /**  **/
    public int miVedioPID;

    public DTVChannelDetailInfo(int miChannelIndex,
                                int miChannelnumber,
                                String mstrServiceName,
                                int miServiceType,
                                boolean mbScrambled,
                                int miCarrierIndex,
                                int miSignalLevel,
                                int miSignalQuality,
                                int miServiceID,
                                int miTSID,
                                int miOrgNetID,
                                int miVideoType,
                                int miAudioType,
                                AudioTrack mAudioTrackInfo,
                                int miSoundMode,
                                int miBanlenceVolume,
                                String msRating,
                                boolean mbLock,
                                boolean mbSkip,
                                boolean mbFav,
                                int miAudioPID,
                                int miVedioPID) {
        this.miChannelIndex = miChannelIndex;
        this.miChannelnumber = miChannelnumber;
        this.mstrServiceName = mstrServiceName;
        this.miServiceType = miServiceType;
        this.mbScrambled = mbScrambled;
        this.miCarrierIndex = miCarrierIndex;
        this.miSignalLevel = miSignalLevel;
        this.miSignalQuality = miSignalQuality;
        this.miServiceID = miServiceID;
        this.miTSID = miTSID;
        this.miOrgNetID = miOrgNetID;
        this.miVideoType = miVideoType;
        this.miAudioType = miAudioType;
        this.mAudioTrackInfo = mAudioTrackInfo;
        this.miSoundMode = miSoundMode;
        this.miBanlenceVolume = miBanlenceVolume;
        this.msRating = msRating;
        this.mbLock = mbLock;
        this.mbSkip = mbSkip;
        this.mbFav = mbFav;
        this.miAudioPID = miAudioPID;
        this.miVedioPID = miVedioPID;
    }

    public static final Creator<DTVChannelDetailInfo> CREATOR = new Parcelable.Creator<DTVChannelDetailInfo>() {
        public DTVChannelDetailInfo createFromParcel(Parcel source) {
            return new DTVChannelDetailInfo(source);
        }

        public DTVChannelDetailInfo[] newArray(int size) {
            return new DTVChannelDetailInfo[size];
        }
    };

    private DTVChannelDetailInfo(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }


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

        dest.writeInt(miSoundMode);

        dest.writeInt(miBanlenceVolume);
        dest.writeString(msRating);
        dest.writeInt(((mbLock) ? (1) : (0)));
        dest.writeInt(((mbSkip) ? (1) : (0)));
        dest.writeInt(((mbFav) ? (1) : (0)));
        dest.writeInt(miAudioPID);
        dest.writeInt(miVedioPID);
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
        mAudioTrackInfo = AudioTrack.CREATOR.createFromParcel(in);
        miSoundMode = in.readInt();
        miBanlenceVolume = in.readInt();
        msRating = in.readString();
        mbLock = (0 != in.readInt());
        mbSkip = (0 != in.readInt());
        mbFav = (0 != in.readInt());
        miAudioPID = in.readInt();
        miVedioPID = in.readInt();
    }
}
