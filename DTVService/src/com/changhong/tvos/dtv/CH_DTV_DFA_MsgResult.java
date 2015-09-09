package com.changhong.tvos.dtv;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public class CH_DTV_DFA_MsgResult implements Parcelable{
	public int	mi_MsgType;			
	public int	mi_MsgPriority;		
	public int	mi_MsgID;			
	public int	mi_absX;			
	public int	mi_absY;			
	public int	mi_SubRelativePos;	
	public int	mi_MainRelativePos;	
	
	public int	mi_enuDFAType; // DFA类型
	
	public String   			mstr_Title;	 
	public String	 			mstr_Subtitle; 	 
	public String 	 			mstr_PromptList; 
	public int					mi_Result;	
	
	public CH_DTV_DFA_MsgResult (int						mi_MsgType,
			int						mi_MsgPriority	,
			int						mi_MsgID,
			int						mi_absX,
			int						mi_absY,
			int						mi_SubRelativePos,
			int						mi_MainRelativePos,
			int 		mi_enuDFAType,
			String			mstr_Title,
			String			mstr_Subtitle,
			String 	 		mstr_PromptList,
			int			mi_Result	
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
		this.mstr_PromptList = mstr_PromptList;
		this.mi_Result = mi_Result;
		//Log.v ( "-->CiCaCallback", "mi_MsgType:" + mi_MsgType );
	}
	
	public static final Creator<CH_DTV_DFA_MsgResult> CREATOR = new Parcelable.Creator<CH_DTV_DFA_MsgResult>() {
        public CH_DTV_DFA_MsgResult createFromParcel(Parcel source) {
            return new CH_DTV_DFA_MsgResult(source);
        }

        public CH_DTV_DFA_MsgResult[] newArray(int size) {
            return new CH_DTV_DFA_MsgResult[size];
        }
    };
	
	private CH_DTV_DFA_MsgResult(Parcel source) {
        readFromParcel(source);
    }
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int arg1) 
	{
		if( null == dest ){
			Log.e("Parcelable", "CH_DTV_DFA_MsgResult>>writeToParcel>>param dest null");
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
		//dest.writeString(mstr_PromptList);
		dest.writeInt(mi_Result);	
	}
	
	public void readFromParcel(Parcel in) 
	{	
		if( null == in ){
			Log.e("Parcelable", "CH_DTV_DFA_MsgResult>>readFromParcel>>param in null");
			return;			
		}
		mi_MsgType 			= in.readInt();
		mi_MsgPriority 		= in.readInt();
		mi_MsgID 			= in.readInt();
		mi_absX 			= in.readInt();
		mi_absY 			= in.readInt();
		mi_SubRelativePos 	= in.readInt();
		mi_MainRelativePos 	= in.readInt();
		
		mi_enuDFAType		= in.readInt();
		
		mstr_Title			=in.readString();
		mstr_Subtitle		=in.readString();
		//mstr_PromptList		=in.readString();		
		mi_Result			= in.readInt();			
	}

}
