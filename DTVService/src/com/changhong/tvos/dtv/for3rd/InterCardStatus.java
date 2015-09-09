
package com.changhong.tvos.dtv.for3rd;

import android.os.Parcel;
import android.os.Parcelable;

public class InterCardStatus implements Parcelable {
    /**
     * CardType
     **/
    public abstract class CardType {
        /**
         * CI
         **/
        public final static int CARD_TYPE_CI = 0;

        /**
         * CA
         **/
        public final static int CARD_TYPE_CA = 1;

        /**
         * CHECK
         **/
        public final static int CARD_TYPE_CHECK = 2;

        /**
         * NO_CARD
         **/
        public final static int CARD_TYPE_NO_CARD = -1;
    }

    /**
     * CardStatus
     **/
    public abstract class CardStatus {
        /**
         * INSERT
         **/
        public final static int CARD_STATUS_INSERT = 0;

        /**
         * CHECKING
         **/
        public final static int CARD_STATUS_CHECKING = 1;

        /**
         * OUT
         **/
        public final static int CARD_STATUS_OUT = 2;

        /**
         * OK
         **/
        public final static int CARD_STATUS_OK = 3;

        /**
         * INVALID
         **/
        public final static int CARD_STATUS_INVALID = 4;
    }

    /**
     * CardType
     **/
    public int miCardType;

    /**
     * CardStatus
     **/
    public int miCardStatus;

    /**
     * CardID
     **/
    public String mstrCardID;

    public InterCardStatus(
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

    public static final Creator<InterCardStatus> CREATOR = new Parcelable.Creator<InterCardStatus>() {
        public InterCardStatus createFromParcel(Parcel source) {
            return new InterCardStatus(source);
        }

        public InterCardStatus[] newArray(int size) {
            return new InterCardStatus[size];
        }
    };

    private InterCardStatus(Parcel source) {
        readFromParcel(source);
    }


    public InterCardStatus() {
        // TODO Auto-generated constructor stub
    }

    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miCardType);
        dest.writeInt(miCardStatus);
        dest.writeString(mstrCardID);
    }


    public void readFromParcel(Parcel in) {
        miCardType = in.readInt();
        miCardStatus = in.readInt();
        mstrCardID = in.readString();
    }
}
