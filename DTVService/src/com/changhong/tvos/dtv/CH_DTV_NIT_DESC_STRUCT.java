package com.changhong.tvos.dtv;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
 
// CH_DTV_CICA_MsgPrompt
/**
 * NIT表
 */
public class CH_DTV_NIT_DESC_STRUCT implements Parcelable{
	public int 						mi_Update_type;
	public int 						mi_PID;
	public int 						mi_OP_ID;
	public int 						mi_HW_ID;
	public int 						mi_SW_ID;
	public int						mi_number_start;
	public int 						mi_number_end;
	public int 						mi_Frequency;
	public int 						mi_modulation;
	public int 						mi_symbolrate;
	public int 						mi_private_data;
	public String				    ms_start_time;
	public int 						mi_reseverd;
	public CH_DTV_NIT_DESC_STRUCT(int Update_type,
			int PID,
			int OP_ID,
			int HW_ID,
			int SW_ID,
			int number_start,
			int number_end,
			int Frequency,
			int modulation,
			int symbolrate,
			int private_data,
			String start_time,
			int reseverd)
		{
			this.mi_Update_type = Update_type;
			this.mi_PID=PID;
			this.mi_OP_ID=OP_ID;
			this.mi_HW_ID=HW_ID;
			this.mi_SW_ID=SW_ID;
			this.mi_number_start=number_start;
			this.mi_number_end=number_end;
			this.mi_Frequency=Frequency;
			this.mi_modulation=modulation;
			this.mi_symbolrate=symbolrate;
			this.mi_private_data=private_data;
			this.ms_start_time=start_time;
			this.mi_reseverd=reseverd;
		}
	public void init(int Update_type,
			int PID,
			int OP_ID,
			int HW_ID,
			int SW_ID,
			int number_start,
			int number_end,
			int Frequency,
			int modulation,
			int symbolrate,
			int private_data,
			int reseverd){
		this.mi_Update_type = Update_type;
		this.mi_PID=PID;
		this.mi_OP_ID=OP_ID;
		this.mi_HW_ID=HW_ID;
		this.mi_SW_ID=SW_ID;
		this.mi_number_start=number_start;
		this.mi_number_end=number_end;
		this.mi_Frequency=Frequency;
		this.mi_modulation=modulation;
		this.mi_symbolrate=symbolrate;
		this.mi_private_data=private_data;
		this.mi_reseverd=reseverd;
	}
	public static final Creator<CH_DTV_NIT_DESC_STRUCT> CREATOR = new Parcelable.Creator<CH_DTV_NIT_DESC_STRUCT>() {
        public CH_DTV_NIT_DESC_STRUCT createFromParcel(Parcel source) {
            return new CH_DTV_NIT_DESC_STRUCT(source);
        }

        public CH_DTV_NIT_DESC_STRUCT[] newArray(int size) {
            return new CH_DTV_NIT_DESC_STRUCT[size];
        }
    };
	
	private CH_DTV_NIT_DESC_STRUCT(Parcel source) {
        readFromParcel(source);
    }
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int arg1) 
	{	
		if( null == dest ){
			Log.e("Parcelable", "CH_DTV_NIT_DESC_STRUCT>>writeToParcel>>param dest null");
			return;
		}
		dest.writeInt(mi_Update_type);
		dest.writeInt(mi_PID);
		dest.writeInt(mi_OP_ID);
		dest.writeInt(mi_HW_ID);
		dest.writeInt(mi_SW_ID);
		dest.writeInt(mi_number_start);
		dest.writeInt(mi_number_end);
		dest.writeInt(mi_Frequency);
		dest.writeInt(mi_modulation);
		dest.writeInt(mi_symbolrate);	
		dest.writeInt(mi_private_data);
		dest.writeString(ms_start_time);
		dest.writeInt(mi_reseverd);
	}
	
	public void readFromParcel(Parcel in) 
	{
		if( null == in ){
			Log.e("Parcelable", "CH_DTV_NIT_DESC_STRUCT>>readfromParcel>>param dest null");
			return;
		}
		mi_Update_type 			= in.readInt();
		mi_PID 					= in.readInt();
		mi_OP_ID 				= in.readInt();
		mi_HW_ID 				= in.readInt();
		mi_SW_ID 				= in.readInt();
		mi_number_start			= in.readInt();
		mi_number_end			= in.readInt();
		mi_Frequency 			= in.readInt();
		mi_modulation 			= in.readInt();		
		mi_symbolrate			= in.readInt();
		mi_private_data			= in.readInt();
		ms_start_time			= in.readString();
		mi_reseverd				= in.readInt();
	}
}
