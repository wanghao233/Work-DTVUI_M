package com.changhong.tvos.dtv.tvap.baseType;

import com.changhong.tvos.dtv.vo.AudioTrack;

public class DtvAudioTrack{
	private String []mAudioTrackArray;
	private int mCurAudioTrack;
	public DtvAudioTrack(AudioTrack audioTrack){
		mAudioTrackArray =audioTrack.mAudioLanguagelist;
		mCurAudioTrack =audioTrack.miCurrSelect;
	}
	public String[] getAudioTrackArray(){
		return mAudioTrackArray;
	}
	public int getCurAudioTrack(){
		return mCurAudioTrack;
	}

	public void setCurAudioTrack(int value){
		mCurAudioTrack = value;
	}
	public String[] getDisplayAudioTrackArray(String [] trackArray,int total ,String [] valueArraystr)
	{

		String[] valueArray = new String[total];
		int chiNum = 0;
		if(trackArray!=null&&trackArray.length>0)
		{
			for(int i=0;i<total;i++)
			{
				valueArray[i] = new String();
				if(trackArray[i].equals("chi"))//Chinese
				{
					if(chiNum != 0){
						valueArray[i] = valueArraystr[1] + chiNum;
					}else {
						valueArray[i] = valueArraystr[1];//Chinese
					}
					chiNum++;
				}
				else if(trackArray[i].equals("asm"))//Assamese
				{
					valueArray[i] = valueArraystr[3];
				}
				else if(trackArray[i].equals("ben"))//Bengali
				{
					valueArray[i] = valueArraystr[4];
				}
				else if(trackArray[i].equals("ger"))//German
				{
					valueArray[i] = valueArraystr[5];
				}
				else if(trackArray[i].equals("eng"))//English
				{
					valueArray[i] = valueArraystr[0];
				}
				else if(trackArray[i].equals("fra"))//French
				{
					valueArray[i] = valueArraystr[6];
				}
				else if(trackArray[i].equals("rus"))//Russian
				{
					valueArray[i] = valueArraystr[7];
				}
				else if(trackArray[i].equals("pan"))//Punjabi
				{
					valueArray[i] = valueArraystr[8];
				}
				else if(trackArray[i].equals("deu"))//Germany
				{
					valueArray[i] = valueArraystr[9];
				}
				else if(trackArray[i].equals("ita"))//Italian
				{
					valueArray[i] = valueArraystr[10];
				}
				else if(trackArray[i].equals("spa"))//Spainish
				{
					valueArray[i] = valueArraystr[11];
				}
				else if(trackArray[i].equals("pol"))//Polanish
				{
					valueArray[i] = valueArraystr[12];
				}
				else if(trackArray[i].equals("eng"))
				{
					valueArray[i] = valueArraystr[0];//english
				}
				else
				{
					if(chiNum != 0){
						valueArray[i] = valueArraystr[13] + chiNum;
					}else {
						valueArray[i] = valueArraystr[13];//Chinese
					}
					chiNum++;
				}
			}
			return valueArray;
		}
		
		return null;
	}
}