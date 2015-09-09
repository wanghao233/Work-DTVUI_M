package com.changhong.tvos.dtv.for3rd;

import android.os.Parcel;
import android.os.Parcelable;

public class InterTimerInfo implements Parcelable {

    public class InterTimerType {
        /**
         * EPG
         **/
        public final static int EPG_TIMER = 0;

        /**
         * PVR
         **/
        public final static int PVR_TIMER = 1;

        /**
         * shut timer
         **/
        public final static int SHUT_DOWN_TIMER = 2;
    }

    /**
     * 预约类型
     **/
    public int mOriginal = 0;

    /**
     * 下标
     **/
    public int miIndex;

    /**
     * 类型
     **/
    public int miType;

    /**
     * 开始提示时间
     **/
    public long mlStartNotifyTime;

    /**
     * 提示时间
     **/
    public long mlNotNotifyTime;

    /**
     * 广播
     **/
    public String mstrTriggerBroadCast;

    /**
     * 详细信息
     **/
    public byte[] mByteDataInfo;


    public InterTimerInfo(
            int iType,
            long lStartNotifyTime,
            long lNotNotifyTime,
            String strTriggerBroadCast,
            byte[] mByteDataInfo) {
        this.miType = iType;
        this.mlStartNotifyTime = lStartNotifyTime;
        this.mlNotNotifyTime = lNotNotifyTime;
        this.mstrTriggerBroadCast = strTriggerBroadCast;
        this.mByteDataInfo = mByteDataInfo;
    }

    public InterTimerInfo(int mOriginal, int miType, long mlStartNotifyTime,
                          long mlNotNotifyTime, String mstrTriggerBroadCast,
                          byte[] mByteDataInfo) {
        super();
        this.mOriginal = mOriginal;
        this.miType = miType;
        this.mlStartNotifyTime = mlStartNotifyTime;
        this.mlNotNotifyTime = mlNotNotifyTime;
        this.mstrTriggerBroadCast = mstrTriggerBroadCast;
        this.mByteDataInfo = mByteDataInfo;
    }

    public static final Creator<InterTimerInfo> CREATOR = new Parcelable.Creator<InterTimerInfo>() {
        public InterTimerInfo createFromParcel(Parcel source) {
            return new InterTimerInfo(source);
        }

        public InterTimerInfo[] newArray(int size) {
            return new InterTimerInfo[size];
        }
    };

    private InterTimerInfo(Parcel source) {
        readFromParcel(source);
    }

    public InterTimerInfo() {
        // TODO Auto-generated constructor stub
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(miIndex);
        dest.writeInt(miType);
        dest.writeLong(mlStartNotifyTime);
        dest.writeLong(mlNotNotifyTime);
        dest.writeInt(mOriginal);
        dest.writeString(mstrTriggerBroadCast);
        if (mByteDataInfo != null) {
            dest.writeInt(mByteDataInfo.length);
            dest.writeByteArray(mByteDataInfo);
        } else {
            dest.writeInt(0);
        }
    }

    public void readFromParcel(Parcel in) {
        miIndex = in.readInt();
        miType = in.readInt();
        mlStartNotifyTime = in.readLong();
        mlNotNotifyTime = in.readLong();
        mOriginal = in.readInt();
        mstrTriggerBroadCast = in.readString();
        int len = in.readInt();
        if (len > 0) {
            mByteDataInfo = new byte[len];
            in.readByteArray(mByteDataInfo);
        }
    }
    /*
	public boolean IsSame(InterTimerInfo timer)	{
		if((this.miType != timer.miType)
				||(this.mlStartNotifyTime/1000L != timer.mlStartNotifyTime/1000L) 
				|| (this.mlNotNotifyTime/1000L != timer.mlNotNotifyTime/1000L)){
			return false;
		}

		if((this.mstrTriggerBroadCast!= null) && (timer.mstrTriggerBroadCast != null)){
			if(!this.mstrTriggerBroadCast.equals(timer.mstrTriggerBroadCast)){
				return false;
			}
		}else if((this.mByteDataInfo!= null) && (timer.mByteDataInfo != null)){
			if(!this.mByteDataInfo.equals(timer.mByteDataInfo)){
			return false;
		}		
		}		

		return true;
	}
	*/
}
