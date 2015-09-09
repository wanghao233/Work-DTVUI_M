package com.changhong.tvos.dtv;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public class CH_DTV_DFA_MsgTrigger implements Parcelable {

	public int	mi_MsgType;			
	public int	mi_MsgPriority;		
	public int	mi_MsgID;			
	public int	mi_absX;			
	public int	mi_absY;			
	public int	mi_SubRelativePos;	
	public int	mi_MainRelativePos;	
	public int	mi_enuDFAType; 
	public String   				mstr_Title;			// 标题 
	public String	 				mstr_Subtitle; 		// 副标题
	public String[]				mastr_List;			// 升级选项列表
	
	public CH_DTV_DFA_MsgTrigger (int						mi_MsgType,
			int						mi_MsgPriority	,
			int						mi_MsgID,
			int						mi_absX,
			int						mi_absY,
			int						mi_SubRelativePos,
			int						mi_MainRelativePos,
			int 		mi_enuDFAType,
			String					mstr_Title,
			String					mstr_Subtitle,
			String[]				mastr_List	
	)
	{
		this.mi_MsgType = mi_MsgType;
		this.mi_MsgPriority = mi_MsgPriority;
		this.mi_MsgID = mi_MsgID;
		this.mi_absX = mi_absX;
		this.mi_absY = mi_absY;
		this.mi_SubRelativePos = mi_SubRelativePos;
		this.mi_MainRelativePos = mi_MainRelativePos;
		this.mi_enuDFAType = mi_enuDFAType;
		this.mstr_Title = mstr_Title;
		this.mstr_Subtitle = mstr_Subtitle;
		this.mastr_List = mastr_List;
	}
	public static final Creator<CH_DTV_DFA_MsgTrigger> CREATOR = new Parcelable.Creator<CH_DTV_DFA_MsgTrigger>() {
        public CH_DTV_DFA_MsgTrigger createFromParcel(Parcel source) {
            return new CH_DTV_DFA_MsgTrigger(source);
        }

        public CH_DTV_DFA_MsgTrigger[] newArray(int size) {
            return new CH_DTV_DFA_MsgTrigger[size];
        }
    };
	
	private CH_DTV_DFA_MsgTrigger(Parcel source) {
        readFromParcel(source);
    }
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int arg1) 
	{	
		if( null == dest ){
			Log.e("Parcelable", "CH_DTV_DFA_MsgTrigger>>writeToParcel>>param dest null");
			return;
		}
		dest.writeInt(mi_MsgType);
		dest.writeInt(mi_MsgPriority);
		dest.writeInt(mi_MsgID);
		dest.writeInt(mi_absX);
		dest.writeInt(mi_absY);
		dest.writeInt(mi_SubRelativePos);
		dest.writeInt(mi_MainRelativePos);
		dest.writeInt(mi_enuDFAType);
		dest.writeString(mstr_Title);
		dest.writeString(mstr_Subtitle);
		if ( null != mastr_List )
		{
			dest.writeStringArray(mastr_List);
		}
	}
	
	public void readFromParcel(Parcel in) 
	{	
		if( null == in ){
			Log.e("Parcelable", "CH_DTV_DFA_MsgTrigger>>readFromParcel>>param in null");
			return;			
		}
		mi_MsgType 			= in.readInt();
		mi_MsgPriority 		= in.readInt();
		mi_MsgID 			= in.readInt();
		mi_absX 			= in.readInt();
		mi_absY 			= in.readInt();
		mi_SubRelativePos 	= in.readInt();
		mi_MainRelativePos 	= in.readInt();	
		mi_enuDFAType 		= in.readInt();
		mstr_Title			=in.readString();
		mstr_Subtitle		=in.readString();
		
		mastr_List   		= new String[CH_DTV_DFA_Constant.MAX_NIT_DESC_COUNT];
		in.readStringArray(mastr_List);
		
	}
}
