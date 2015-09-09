package com.changhong.tvos.dtv.tvap;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstSourceID;
import com.changhong.tvos.dtv.vo.DMBTHCarrier;
import com.changhong.tvos.dtv.vo.DTVSource;

public class DtvSourceManager {
	private static final  String TAG= "DtvSourceManager";
	private static DtvSourceManager mDtvSourceManager;
	private DtvInterface mDtvInterface = DtvInterface.getInstance();
	private DtvOperatorManager mOperatorManager = null;
	private List<DTVSource> mSourceList = null;

	private  DtvSourceManager() {
		mSourceList = mDtvInterface.getSourceList();
		mOperatorManager = DtvOperatorManager.getInstance();
	}
	public  static DtvSourceManager getInstance(){
		if(mDtvSourceManager == null){
			mDtvSourceManager = new DtvSourceManager();
		}
		return mDtvSourceManager;
	}
	
	public List<String> getSourceNameList() {
		List<String> sourceNameList = new ArrayList<String>();
		if(mSourceList==null){
			mSourceList = mDtvInterface.getSourceList();
		}
		if(mSourceList!=null){
			for(DTVSource source:mSourceList){
				sourceNameList.add(source.miSourceName);
			}
		}
		return sourceNameList;
	}
	
	public List<DTVSource> getSourceList() {
		if(mSourceList==null){
			mSourceList = mDtvInterface.getSourceList();
			
		}
		return mSourceList;
	}
	
	public void setCurSource(int sourceID){
		mDtvInterface.setSource(sourceID);
		mOperatorManager.updateOperatorList();
		
	}
	public int getCurSourceID(){
		return mDtvInterface.getSourceID();
	}
	public int getCurDemodeType() {
		
		return mDtvInterface.getDemodType();
	}
	public int getSourceIDByIndex(int index){
		int sourceID = ConstSourceID.DVBC;
		if(mSourceList==null){
			mSourceList = mDtvInterface.getSourceList();
		}
		if(null!=mSourceList&&mSourceList.size()>0&&index<mSourceList.size()){
			sourceID = mSourceList.get(index).miSourceID;
			Log.i(TAG,"LL getSourceIDByIndex()>>sourceID = " + sourceID + ",index = " + index);
		}else{
			Log.e(TAG,"LL getSourceIDByIndex()>>sourceID = " + sourceID);
		}
		return sourceID;
	}
	public int getCurSourceIndex() {
		int index = -1;
		if(mSourceList==null){
			mSourceList = mDtvInterface.getSourceList();
		}
		if(null!=mSourceList){
			Log.i(TAG,"LL getCurDemodeTypeIndex()>>mSourceList.size() = " + mSourceList.size());
			for(DTVSource source:mSourceList){
				if(source.miSourceID==getCurSourceID()){
					index = mSourceList.indexOf(source);
				}
			}
		}else{
			Log.e(TAG, "EL--> err mSourceList is null");
		}
		return index;
	}
	
	public String getCurSourceName(){
		String name = null;
		if(mSourceList==null){
			mSourceList = mDtvInterface.getSourceList();
		}
		if(null  != mSourceList){
			Log.i(TAG,"LL getCurSourceName()>>mSourceList.size() = " + mSourceList.size());
			for(DTVSource source:mSourceList){
				if(source.miSourceID == getCurSourceID()){
					name = source.miSourceName;
				}
			}
		}else{
			Log.e(TAG, "EL--> err mSourceList is null");
		}
		return name;
	}
	public DMBTHCarrier getDMBTCarrierInfo() {
		// TODO Auto-generated method stub
		return mDtvInterface.getDMBTHCarrier();
	}
	
	public int getProductType() {
		return mDtvInterface.getProductType();
	}
	
	public CharSequence getSourceNameById(int sourceId) {
		// TODO Auto-generated method stub
		String name = null;
		for(DTVSource source:mSourceList){
			if(source.miSourceID == sourceId){
				name = source.miSourceName;
			}
		}
		return name;
	}
}
