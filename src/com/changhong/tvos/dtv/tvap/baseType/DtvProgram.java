package com.changhong.tvos.dtv.tvap.baseType;

import android.util.Log;
import com.changhong.tvos.dtv.tvap.DtvInterface;
import com.changhong.tvos.dtv.tvap.DtvOperatorManager;
import com.changhong.tvos.dtv.vo.DTVChannelBaseInfo;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstServiceType;

public class DtvProgram{
	private static final String TAG = "DtvProgram";
	public int mProgramNum=0;
	public int mServiceIndex;
	public String mProgramName;
	public int mServiceID;
	public int mTsID;
	public int mOrgNetID;
	public int mProviderID;
	public int mFrequency;
	public boolean mIsSkip;
	public boolean mIsRadio;
	public boolean mIsScrambled;
	public int mAudioModeSel;
	public String mAudioTrackSel;
	public boolean mIsFav;
	public String mDtvLogo = null;
	public int mBalanceVolume=0;
	private String [] mAudioMode;
	private int mAudioTrackSelIndex;
	private String [] mAudioTrack =null;
	private DtvAudioTrack mDtvAudioTrack =null;
	private DtvInterface mDtvInterface = DtvInterface.getInstance();
	private DtvOperatorManager mDtvOperatorManager = DtvOperatorManager.getInstance();

	public DtvProgram(){}
    public DtvProgram(DTVChannelBaseInfo chChannel) {
		// TODO Auto-generated constructor stub
    	mProgramNum =chChannel.miChannelnumber;
    	mServiceIndex =chChannel.miChannelIndex;
    	mProgramName =chChannel.mstrServiceName;
    	mServiceID = chChannel.miServiceID;
    	mTsID = chChannel.miTSID;
    	mOrgNetID = chChannel.miOrgNetID;
    	mProviderID = mDtvOperatorManager.getCurOperator().getOperatorCode();
    	mIsSkip =chChannel.mbSkip;
    	mIsRadio =(chChannel.miServiceType == ConstServiceType.SERVICE_TYPE_RADIO);
    	mIsScrambled =chChannel.mbScrambled ;
    	mAudioModeSel =chChannel.miSoundMode;
    	mAudioTrackSel =chChannel.mstrCurAudioTrack;
    	mIsFav = chChannel.mbFav;
    	mDtvAudioTrack = mDtvInterface.getAudioTrack(mServiceIndex);
    	mFrequency = mDtvInterface.getFreqByChannelIndex(mServiceIndex);
    	mDtvLogo = chChannel.msLogo;
    	mBalanceVolume = chChannel.miBanlenceVolume;
		Log.i(TAG, "LL DtvProgram>>mFrequency = " + mFrequency
				+ ",mProgramName = " + mProgramName + ",mServiceIndex = "
				+ mServiceIndex + ",mProgramNum = " + mProgramNum + " ,mDtvLogo = " + mDtvLogo);
	}
    
	public int getProgramNum(){
		return mProgramNum;
	}
	
	public void setProgramNum(int programNum){
		mProgramNum =programNum;
	}
	
	public int getProgramServiceIndex(){
		return mServiceIndex;
	}
	public String getProgramName(){
		return mProgramName;
	}	

	public void setProgramName(String name) {
		mProgramName = name;
	}

	public boolean isRadio(){
		return mIsRadio;
	}	
	public boolean isSkip(){
		return mIsSkip;
	}	

	public boolean isFav(){
		return mIsFav;
	}	
	public void setFavState(boolean isFav){
		mIsFav=isFav;
		mDtvInterface.channelFav(mServiceIndex, mIsFav);
	}	
	
	public void setSkipState(boolean isSkip){
		mIsSkip =isSkip;
//		mDtvInterface.channelSkip(mServiceIndex, mIsSkip);
	}	
	
	public boolean isScrambled(){
		return mIsScrambled;
	}
	
	public String [] getAudioMode(){
		mAudioMode =mDtvInterface.getAudioMode(mServiceIndex);
		return mAudioMode;
	}
	
	public String [] getAudioTrack(String [] track){
		if(mAudioTrack ==null){
			if(mDtvAudioTrack!=null)
			{
				String[] trackArray = mDtvAudioTrack.getAudioTrackArray();
				if(trackArray != null&&trackArray.length>0)
				{
					mAudioTrack = mDtvAudioTrack.getDisplayAudioTrackArray(trackArray,mDtvAudioTrack.getAudioTrackArray().length,track);
				}
			}

		}
		
		if(mAudioTrack ==null){
			mAudioTrack = new String[1];
			mAudioTrack[0] = track[1];
			mAudioTrackSel ="chi";
		}
		
		return mAudioTrack;
	}
	
	public String getAudioTrackSel(){
		return mAudioTrackSel;
	}
	public int getAudioTrackSelIndex(){
		if(mDtvAudioTrack != null){
			return mDtvAudioTrack.getCurAudioTrack();
		}
		return 0;
	}
	
	public void setAudioTrack(int index){
		mAudioTrackSelIndex =index;
		mDtvAudioTrack.setCurAudioTrack(index);
		mDtvInterface.setAudioTrack(mAudioTrackSelIndex);
	}
	
	public int getAudioModeSel(){
		return mAudioModeSel;
	}
	
	public void setAudioMode(int index){
		mAudioModeSel =index;
		mDtvInterface.setAudioMode(mAudioModeSel);
	}
	
	@Override
	public boolean equals(Object o){
		if(null != o){
			DtvProgram program = (DtvProgram)o;
			if((this.mServiceID == program.mServiceID)
					&&(this.mTsID == program.mTsID)
					&&(this.mOrgNetID == program.mOrgNetID)
					&&((null != this.mProgramName) && this.mProgramName.equals(program.mProgramName)
					&&(this.mServiceIndex == program.mServiceIndex))){
				return true;
			}
		}
		return false;
	}
}
