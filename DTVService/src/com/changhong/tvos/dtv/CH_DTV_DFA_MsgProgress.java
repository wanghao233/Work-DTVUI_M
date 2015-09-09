package com.changhong.tvos.dtv;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public class CH_DTV_DFA_MsgProgress implements Parcelable{
	public int	mi_MsgType;			
	public int	mi_MsgPriority;		
	public int	mi_MsgID;			
	public int	mi_absX;			
	public int	mi_absY;			
	public int	mi_SubRelativePos;	
	public int	mi_MainRelativePos;	
	
	public int	mi_enuDFAType; 
	
	public String   				mstr_Title;	 
	public String	 				mstr_Subtitle; 	
	public String[]	 			mastr_PromptList; 
	public int						mi_Progress;			

	public CH_DTV_DFA_MsgProgress(int mi_MsgType, int mi_MsgPriority,
			int mi_MsgID, int mi_absX, int mi_absY, int mi_SubRelativePos,
			int mi_MainRelativePos, int	mi_enuDFAType, 
			String mstr_Title, String mstr_Subtitle,String[] mastr_PromptList,int mi_Progress) {
		
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
		this.mastr_PromptList = mastr_PromptList;
		this.mi_Progress = mi_Progress;
		
	}

	public static final Creator<CH_DTV_DFA_MsgProgress> CREATOR = new Parcelable.Creator<CH_DTV_DFA_MsgProgress>() {
        public CH_DTV_DFA_MsgProgress createFromParcel(Parcel source) {
            return new CH_DTV_DFA_MsgProgress(source);
        }

        public CH_DTV_DFA_MsgProgress[] newArray(int size) {
            return new CH_DTV_DFA_MsgProgress[size];
        }
    };
	
	private CH_DTV_DFA_MsgProgress(Parcel source) {
        readFromParcel(source);
    }
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int arg1) 
	{
		if( null == dest ){
			Log.e("Parcelable", "CH_DTV_DFA_MsgProgress>>writeToParcel>>param dest null");
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
//		if(mastr_PromptList!=null)
//		{
//			dest.writeStringArray(mastr_PromptList);
//		}
		dest.writeInt(mi_Progress);		
	}
	
	public void readFromParcel(Parcel in) 
	{	
		if( null == in ){
			Log.e("Parcelable", "CH_DTV_DFA_MsgProgress>>readFromParcel>>param in null");
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
		
		//mastr_PromptList 	=new String[CH_DTV_DFA_Constant.MAX_PROMPT_INFO_LIST+200];
		//in.readStringArray(mastr_PromptList);
		
		mi_Progress			= in.readInt();	
	}
}
