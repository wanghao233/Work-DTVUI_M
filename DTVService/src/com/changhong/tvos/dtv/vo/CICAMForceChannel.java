/**
 * @filename DTV CAǿ�ƻ�̨��Ϣ
 * @author:
 * @date:
 * @version 0.1
 * @history: 2012-9-11  ���Ӳ���ģʽ
 */
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**  **/
public class CICAMForceChannel extends CICAMMessageBase implements Parcelable {
    /** **/
    public int miChannelIndex;

    /**<br>
     * 0
     * 1
     * 2
     * 3
     * 4
     * **/
    public int miOpType;

    public CICAMForceChannel() {
    }

    public CICAMForceChannel(int iMsgType,
                             int iMsgPriority, int iMsgID, int iAbsX, int iAbsY,
                             int iSubRelativePos, int iMainRelativePos, int iChannelIndex, int iOpType) {
        super(iMsgType, iMsgPriority, iMsgID, iAbsX, iAbsY, iSubRelativePos,
                iMainRelativePos);
        this.miChannelIndex = iChannelIndex;
        this.miOpType = iOpType;
    }

    public static final Creator<CICAMForceChannel> CREATOR = new Parcelable.Creator<CICAMForceChannel>() {
        public CICAMForceChannel createFromParcel(Parcel source) {
            return new CICAMForceChannel(source);
        }

        public CICAMForceChannel[] newArray(int size) {
            return new CICAMForceChannel[size];
        }
    };

    private CICAMForceChannel(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        super.writeToParcel(dest, arg1);
        dest.writeInt(miChannelIndex);
        dest.writeInt(miOpType);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        miChannelIndex = in.readInt();
        miOpType = in.readInt();
    }
}
