/**
 * @filename DTV CA�ʼ���Ϣ
 * @author:
 * @date:
 * @version 0.1
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class CICAMMail extends CICAMMessageBase implements Parcelable {
    /**  **/
    public final static int MAIL_MSG_CLEAR = 0;

    /**  **/
    public final static int MAIL_MSG_NEW = 1;

    /**  **/
    public final static int MAIL_MSG_FULL = 2;

    /**  **/
    public final static int MAIL_MSG_INVALID = 0xffffffff;

    /**  **/
    public int miNewCnt;

    /**  **/
    public int miReadCnt;

    /**
     * {@linkMAIL_MSG_CLEAR MAIL_MSG_CLEAR}<br>
     * {@linkMAIL_MSG_NEW MAIL_MSG_NEW}<br>
     * {@linkMAIL_MSG_FULL MAIL_MSG_FULL}
     * **/
    public int miParam;

    public CICAMMail(int mi_MsgType, int mi_MsgPriority,
                     int mi_MsgID, int mi_absX, int mi_absY, int mi_SubRelativePos,
                     int mi_MainRelativePos, int mi_NewCnt, int mi_ReadCnt, int mi_Param) {
        super(mi_MsgType, mi_MsgPriority, mi_MsgID, mi_absX, mi_absY, mi_SubRelativePos,
                mi_MainRelativePos);
        this.miNewCnt = mi_NewCnt;
        this.miReadCnt = mi_ReadCnt;
        this.miParam = mi_Param;
    }


    public static final Creator<CICAMMail> CREATOR = new Parcelable.Creator<CICAMMail>() {
        public CICAMMail createFromParcel(Parcel source) {
            return new CICAMMail(source);
        }

        public CICAMMail[] newArray(int size) {
            return new CICAMMail[size];
        }
    };

    private CICAMMail(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        super.writeToParcel(dest, arg1);

        dest.writeInt(miNewCnt);
        dest.writeInt(miReadCnt);
        dest.writeInt(miParam);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);

        miNewCnt = in.readInt();
        miReadCnt = in.readInt();
        miParam = in.readInt();
    }
}
