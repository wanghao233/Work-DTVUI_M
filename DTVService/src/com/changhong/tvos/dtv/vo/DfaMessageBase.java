/**
 * @filename DTV ������Ϣ����
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 */
public class DfaMessageBase implements Parcelable {
    /**  **/
    public abstract class ConstDfaMsgType {

        /**  **/
        public static final int MSG_TRIGGER = 0;

        /**  **/
        public static final int MSG_PROGRESS = 1;

        /**  **/
        public static final int MSG_RESULT = 2;
    }

    /**  **/
    public abstract class ConstMsgPriority {
        /** EL(Extremely Low) **/
        public static final int PRIORITY_EL = 0;
        /** SL(Super Low) **/
        public static final int PRIORITY_SL = 1;
        /** UL(Ultra Low) **/
        public static final int PRIORITY_UL = 2;
        /** L(Very Low) **/
        public static final int PRIORITY_VL = 3;
        /** (Low) **/
        public static final int PRIORITY_LO = 4;
        /**ͨ(Normal) **/
        public static final int PRIORITY_NM = 5;
        /** (High) **/
        public static final int PRIORITY_HI = 6;
        /** (Very High) **/
        public static final int PRIORITY_VH = 7;
        /** (Ultra High) **/
        public static final int PRIORITY_UH = 8;
        /** (Super High) **/
        public static final int PRIORITY_SH = 9;
        /** (Extremely High) **/
        public static final int PRIORITY_EH = 10;
    }

    /** {@link ConstDfaMsgType ConstDfaMsgType}**/
    public int miMsgType;

    /** {@link ConstMsgPriority ConstMsgPriority}**/
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

    public DfaMessageBase() {

    }

    public DfaMessageBase(int iMsgType, int iMsgPriority,
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

    public static final Creator<DfaMessageBase> CREATOR = new Parcelable.Creator<DfaMessageBase>() {
        public DfaMessageBase createFromParcel(Parcel source) {
            return new DfaMessageBase(source);
        }

        public DfaMessageBase[] newArray(int size) {
            return new DfaMessageBase[size];
        }
    };

    private DfaMessageBase(Parcel source) {
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
