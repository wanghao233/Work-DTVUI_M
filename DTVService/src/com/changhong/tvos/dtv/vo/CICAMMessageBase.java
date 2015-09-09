/**
 * @filename DTV CI CA�㲥��Ϣ����
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/** CI CA */
public class CICAMMessageBase implements Parcelable {
    /**  **/
    public abstract class ConstCICAMsgType {

        /**  **/
        public final static int MSG_MAIL = 0;

        /**  **/
        public final static int MSG_FINGER = 1;

        /**  **/
        public final static int MSG_PROMPT = 2;

        /**  **/
        public final static int MSG_SUBTITLE = 3;

        /**  **/
        public final static int MSG_ANNOUNCE = 4;

        /**  **/
        public final static int MSG_USER_MENU = 5;

        /**  **/
        public final static int MSG_FORCE_MENU = 6;

        /**  **/
        public final static int MSG_FORCE_RESORT = 7;

        /** force rescan **/
        public final static int MSG_FORCE_RESCAN = 8;

        /** force channel **/
        public final static int MSG_FORCE_CHANNEL = 9;

        /**  **/
        public final static int MSG_NO_CARD_SETS = 10;

        /**  **/
        public final static int MSG_CHANNEL_UPDATE = 11;

    }

    /**  **/
    public abstract class ConstCICAMsgPriority {
        /** EL(Extremely Low) **/
        public final static int PRIORITY_EL = 0;
        /** SL(Super Low) **/
        public final static int PRIORITY_SL = 1;
        /** UL(Ultra Low) **/
        public final static int PRIORITY_UL = 2;
        /** VL(Very Low) **/
        public final static int PRIORITY_VL = 3;
        /** (Low) **/
        public final static int PRIORITY_LO = 4;
        /** ͨ(Normal)***/
        public final static int PRIORITY_NM = 5;
        /** (High) **/
        public final static int PRIORITY_HI = 6;
        /** (Very High) **/
        public final static int PRIORITY_VH = 7;
        /** (Ultra High) **/
        public final static int PRIORITY_UH = 8;
        /** (Super High) **/
        public final static int PRIORITY_SH = 9;
        /** (Extremely High) **/
        public final static int PRIORITY_EH = 10;
    }

    /** {@link ConstCICAMsgType ConstCICAMsgType} **/
    public int miMsgType;

    /** {@link ConstCICAMsgPriority ConstCICAMsgPriority}**/
    public int miMsgPriority;

    /**  **/
    public int miMsgID;

    /** 0~720 **/
    public int miAbsX;

    /** 0~1080 **/
    public int miAbsY;

    /**  **/
    public int miSubRelativePos;

    /**  **/
    public int miMainRelativePos;

    public CICAMMessageBase() {

    }

    public CICAMMessageBase(int iMsgType, int iMsgPriority,
                            int iMsgID, int iAbsX, int iAbsY, int iSubRelativePos,
                            int iMainRelativePos) {
        this.miAbsX = iAbsX;
        this.miAbsY = iAbsY;
        this.miMainRelativePos = iMainRelativePos;
        this.miMsgID = iMsgID;
        this.miMsgPriority = iMsgPriority;
        this.miMsgType = iMsgType;
        this.miSubRelativePos = iSubRelativePos;
        // Log.v ( "-->CiCaCallback", "mi_MsgType:" + mi_MsgType );
    }

    public static final Creator<CICAMMessageBase> CREATOR = new Parcelable.Creator<CICAMMessageBase>() {
        public CICAMMessageBase createFromParcel(Parcel source) {
            return new CICAMMessageBase(source);
        }

        public CICAMMessageBase[] newArray(int size) {
            return new CICAMMessageBase[size];
        }
    };

    private CICAMMessageBase(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miMsgType);
        dest.writeInt(miMsgPriority);
        dest.writeInt(miMsgID);
        dest.writeInt(miAbsX);
        dest.writeInt(miAbsY);
        dest.writeInt(miSubRelativePos);
        dest.writeInt(miMainRelativePos);
    }

    public void readFromParcel(Parcel in) {
        miMsgType = in.readInt();
        miMsgPriority = in.readInt();
        miMsgID = in.readInt();
        miAbsX = in.readInt();
        miAbsY = in.readInt();
        miSubRelativePos = in.readInt();
        miMainRelativePos = in.readInt();
    }
}
