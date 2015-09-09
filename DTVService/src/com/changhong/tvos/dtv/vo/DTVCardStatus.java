
package com.changhong.tvos.dtv.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class DTVCardStatus implements Parcelable {
    /**  **/
    public abstract class CardType {
        /**
         * CI
         **/
        public final static int CARD_TYPE_CI = 0;

        /**
         * CA
         **/
        public final static int CARD_TYPE_CA = 1;

        /**  **/
        public final static int CARD_TYPE_CHECK = 2;

        /**  **/
        public final static int CARD_TYPE_NO_CARD = -1;
    }

    /**   **/
    public abstract class CardStatus {
        /**  **/
        public final static int CARD_STATUS_INSERT = 0;

        /**  **/
        public final static int CARD_STATUS_CHECKING = 1;

        /**  **/
        public final static int CARD_STATUS_OUT = 2;

        /**  **/
        public final static int CARD_STATUS_OK = 3;

        /**  **/
        public final static int CARD_STATUS_INVALID = 4;
    }

    /**
     * {@link CardType CardType}
     **/
    public int miCardType;

    /**
     * {@link CardStatus CardStatus}
     **/
    public int miCardStatus;


    public DTVCardStatus(
            int iCardType,
            int iCardStatus) {

        miCardType = iCardType;
        miCardStatus = iCardStatus;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    public static final Creator<DTVCardStatus> CREATOR = new Parcelable.Creator<DTVCardStatus>() {
        public DTVCardStatus createFromParcel(Parcel source) {
            return new DTVCardStatus(source);
        }

        public DTVCardStatus[] newArray(int size) {
            return new DTVCardStatus[size];
        }
    };

    private DTVCardStatus(Parcel source) {
        readFromParcel(source);
    }


    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miCardType);
        dest.writeInt(miCardStatus);
    }


    public void readFromParcel(Parcel in) {
        miCardType = in.readInt();
        miCardStatus = in.readInt();
    }
}
