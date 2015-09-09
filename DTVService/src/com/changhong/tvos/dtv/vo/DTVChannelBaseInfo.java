/**
 * @filename DTV ��Ŀ������Ϣ
 * @author:
 * @date: 2012-7-16 ����tsid\orgnetid,��miAudioMode��ΪmiSoundMode��detail����һ�¡�
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 **/
public class DTVChannelBaseInfo implements Parcelable {
    /**  **/
    public static final int DTV_CHANNEL_AUDIO_MODE_MONO = 0;
    /**  **/
    public static final int DTV_CHANNEL_AUDIO_MODE_STERO = 1;

    /** {@link DTVChannelBaseInfo DTVChannelBaseInfo}{@link DTVChannelDetailInfo DTVChannelDetailInfo}
     * */
    public int miChannelIndex;

    /**  **/
    public int miChannelnumber;

    /**  **/
    public String mstrServiceName;

    /** {@link DTVConstant.ServiceType DTVConstant.ServiceType} **/
    public int miServiceType;

    /** ServiceID **/
    public int miServiceID;

    /** TSID **/
    public int miTSID;

    /** Orignal Network ID **/
    public int miOrgNetID;

    /**  **/
    public boolean mbScrambled;

    /**  **/
    public String mstrCurAudioTrack;

    /**  **/
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

    /** {@link DTVConstant#ConstDemodeType ConstDemodeType} **/
    public int miDemodType;

    /** DTV{@link null} **/
    public String msLogo;

    /**
     *
     */
    public DTVChannelBaseInfo(int miChannelIndex, int miChannelnumber,
                              String mstrServiceName,    /** {@link DTVConstant.ServiceType DTVConstant.ServiceType} **/
                              int miServiceType,
                              int miServiceID,
                              int miTSID,
                              int miOrgNetID,
                              boolean mbScrambled,
                              String mstrCurAudioTrack,
                              int miAudioMode,
                              int miBanlenceVolume,
                              String msRating,
                              boolean mbLock,
                              boolean mbSkip,
                              boolean mbFav,
                              int miDemodType,
                              String msLogo) {
        this.miChannelIndex = miChannelIndex;
        this.miChannelnumber = miChannelnumber;
        this.mstrServiceName = mstrServiceName;
        this.miServiceType = miServiceType;
        this.miServiceID = miServiceID;
        this.miTSID = miTSID;
        this.miOrgNetID = miOrgNetID;
        this.mbScrambled = mbScrambled;
        this.mstrCurAudioTrack = mstrCurAudioTrack;
        this.miSoundMode = miAudioMode;

        this.miBanlenceVolume = miBanlenceVolume;
        this.msRating = msRating;
        this.mbLock = mbLock;
        this.mbSkip = mbSkip;
        this.mbFav = mbFav;
        this.miDemodType = miDemodType;
        this.msLogo = msLogo;
    }

    public static final Creator<DTVChannelBaseInfo> CREATOR = new Parcelable.Creator<DTVChannelBaseInfo>() {
        public DTVChannelBaseInfo createFromParcel(Parcel source) {
            return new DTVChannelBaseInfo(source);
        }

        public DTVChannelBaseInfo[] newArray(int size) {
            return new DTVChannelBaseInfo[size];
        }
    };

    private DTVChannelBaseInfo(Parcel source) {
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
        dest.writeInt(miServiceID);
        dest.writeInt(miTSID);
        dest.writeInt(miOrgNetID);
        dest.writeInt(((mbScrambled) ? (1) : (0)));
        dest.writeString(mstrCurAudioTrack);
        dest.writeInt(miSoundMode);
        dest.writeInt(miBanlenceVolume);
        dest.writeString(msRating);
        dest.writeInt(((mbLock) ? (1) : (0)));
        dest.writeInt(((mbSkip) ? (1) : (0)));
        dest.writeInt(((mbFav) ? (1) : (0)));
        dest.writeInt(miDemodType);
        dest.writeString(msLogo);
    }

    public void readFromParcel(Parcel in) {

        miChannelIndex = in.readInt();
        miChannelnumber = in.readInt();
        mstrServiceName = in.readString();
        miServiceType = in.readInt();
        miServiceID = in.readInt();
        miTSID = in.readInt();
        miOrgNetID = in.readInt();
        mbScrambled = (0 != in.readInt());
        mstrCurAudioTrack = in.readString();
        miSoundMode = in.readInt();
        miBanlenceVolume = in.readInt();
        msRating = in.readString();
        mbLock = (0 != in.readInt());
        mbSkip = (0 != in.readInt());
        mbFav = (0 != in.readInt());
        miDemodType = in.readInt();
        msLogo = in.readString();
    }
}