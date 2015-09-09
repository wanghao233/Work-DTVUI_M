package com.changhong.tvos.dtv;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
 
// CH_DTV_CICA_MsgPrompt
/**
 * 
 */
public class CH_DTV_DFA_MsgDownloadStatus implements Parcelable{
	public int						mi_download_progress;
	public int						mi_download_status;
	public int 						mi_download_errorcode;

	public CH_DTV_DFA_MsgDownloadStatus (int mi_download_progress,int mi_download_status,int mi_download_errorcode)
	{
		this.mi_download_progress = mi_download_progress;
		this.mi_download_status = mi_download_status;	
		this.mi_download_errorcode = mi_download_errorcode;
	}
	public void init(int mi_download_progress,int mi_download_status,int mi_download_errorcode){
		this.mi_download_progress = mi_download_progress;
		this.mi_download_status = mi_download_status;	
		this.mi_download_errorcode = mi_download_errorcode;
	}
	public static final Creator<CH_DTV_DFA_MsgDownloadStatus> CREATOR = new Parcelable.Creator<CH_DTV_DFA_MsgDownloadStatus>() {
        public CH_DTV_DFA_MsgDownloadStatus createFromParcel(Parcel source) {
            return new CH_DTV_DFA_MsgDownloadStatus(source);
        }

        public CH_DTV_DFA_MsgDownloadStatus[] newArray(int size) {
            return new CH_DTV_DFA_MsgDownloadStatus[size];
        }
    };
	
	private CH_DTV_DFA_MsgDownloadStatus(Parcel source) {
        readFromParcel(source);
    }
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int arg1) 
	{
		if( null == dest ){
			Log.e("Parcelable", "CH_DTV_DFA_MsgDownloadStatus>>writeToParcel>>param dest null");
			return;
		}
		dest.writeInt(mi_download_progress);
		dest.writeInt(mi_download_status);
		dest.writeInt(mi_download_errorcode);
	}
	
	public void readFromParcel(Parcel in) 
	{	
		if( null == in ){
			Log.e("Parcelable", "CH_DTV_DFA_MsgDownloadStatus>>readfromParcel>>param dest null");
			return;
		}
		mi_download_progress 			= in.readInt();
		mi_download_status 				= in.readInt();
		mi_download_errorcode 			= in.readInt();
	}
}
