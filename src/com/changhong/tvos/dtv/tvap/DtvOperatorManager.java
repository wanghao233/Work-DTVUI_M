package com.changhong.tvos.dtv.tvap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.baseType.DtvOpFeature;
import com.changhong.tvos.dtv.tvap.baseType.DtvOperator;
import com.changhong.tvos.dtv.tvap.baseType.DtvTunerInfo;
import com.changhong.tvos.dtv.vo.Operator;
import android.content.Context;
import android.util.Log;

public class DtvOperatorManager {
	private static final String TAG = "DtvOperatorManager";
	private static DtvOperatorManager mDtvOperatorManager;
	//	private Map<Integer,List<DtvOperator>> mDtvOperatorListEx = new HashMap<Integer, List<DtvOperator>>();
	List<DtvOperator> mDtvOperatorList = null;
	List<DtvOperator> mdtvOperList = null;
	private DtvInterface mDtvInterface = DtvInterface.getInstance();
	private Context mContext;

	private DtvOperatorManager() {
		mDtvOperatorList = mDtvInterface.getOperatorList();
		//		List<DTVSource> sourceList = mDtvInterface.getSourceList();
		//		if(sourceList!=null&&sourceList.size()>0){
		//			for(int i=0;i<sourceList.size();i++){
		//				
		//				mDtvOperatorListEx.put(sourceList.get(i).miSourceID, mDtvInterface.getOperatorListBySourceID(sourceList.get(i).miSourceID));
		//			}
		//		}
	}

	public static DtvOperatorManager getInstance() {
		if (mDtvOperatorManager == null) {
			mDtvOperatorManager = new DtvOperatorManager();
		}
		return mDtvOperatorManager;
	}

	//////////////////FENGY 2014-3-24///////////////	
	static int tmpsourceID = 0;

	public List<DtvOperator> getOperatorListBySourceID(int sourceID) {
		Log.i(TAG, "LL getOperatorListBySourceID()>>sourceID = " + sourceID);
		Log.i(TAG, "LL getOperatorListBySourceID()>>tmpsourceID = " + tmpsourceID);
		if (tmpsourceID == sourceID) {
			if (mdtvOperList == null || mdtvOperList.size() <= 0) {
				// List<DtvOperator> dtvOperList = null;
				mdtvOperList = mDtvInterface.getOperatorListBySourceID(sourceID);
			}
		} else {
			mdtvOperList = mDtvInterface.getOperatorListBySourceID(sourceID);
			tmpsourceID = sourceID;
		}
		return mdtvOperList;
	}

	public void updateOperatorList() {
		mDtvOperatorList = mDtvInterface.getOperatorList();
	}

	public List<DtvOperator> getOperatorList() {
		if (mDtvOperatorList == null || mDtvOperatorList.size() <= 0) {
			mDtvOperatorList = mDtvInterface.getOperatorList();
		}
		return mDtvOperatorList;
	}

	public DtvOpFeature getOpFeature() {
		DtvOpFeature dtvOpFeature = mDtvInterface.getOperatorFeature();
		return dtvOpFeature;
	}

	public DtvTunerInfo getOPMainTunerInfo() {
		DtvTunerInfo dtvTunerInfo = null;
		DtvOperator dtvOperator = mDtvInterface.loadCurOperator();
		Log.i(TAG, "LL,getOPMainTunerInfo");
		if (dtvOperator != null) {
			dtvTunerInfo = mDtvInterface.getOPMainTunerInfo(dtvOperator.getOperatorCode());
		} else {
			Log.e(TAG, "LL,dtvOperator == null)");
			dtvTunerInfo = new DtvTunerInfo(235000, 6875, 5);
		}
		return dtvTunerInfo;
	}

	public List<DtvTunerInfo> getOPMainTunerList() {
		List<DtvTunerInfo> dtvTunerInfo = null;
		DtvOperator dtvOperator = mDtvInterface.loadCurOperator();
		if (dtvOperator != null) {
			dtvTunerInfo = mDtvInterface.getOPMainTunerList(dtvOperator.getOperatorCode());
		}
		return dtvTunerInfo;
	}

	public int getCurOpIndex() {
		DtvOperator dtvOperator = mDtvInterface.loadCurOperator();
		int index = 0;
		for (; index < mDtvOperatorList.size(); index++) {
			if (null != dtvOperator) {

				if (mDtvOperatorList.get(index).getOperatorName().equals(dtvOperator.getOperatorName())) {
					break;
				}
			}
		}
		if (index >= mDtvOperatorList.size()) {
			index = 0;
		}
		if (dtvOperator != null) {
			dtvOperator = null;
		}
		Log.i(TAG, "LL getCurOpIndex()>>index = " + index);
		return index;
	}

	public void setCurOperator(int index) {
		String str = String.format("LL index = %d,mDtvOperatorList.size() = %d", index, mDtvOperatorList.size());
		Log.i(TAG, str);
		if (null != mDtvOperatorList && index >= 0 && index < mDtvOperatorList.size()) {
			DtvOperator dtvOperator = mDtvOperatorList.get(index);
			if (dtvOperator != null) {
				Log.i(TAG, "LL setCurOperator(int)>>OPName = " + dtvOperator.getOperatorName());
				mDtvInterface.saveCurOperator(dtvOperator.getOperatorCode());
			}
		}
	}

	public DtvOperator getCurOperator() {

		DtvOperator dtvOperator = mDtvInterface.loadCurOperator();
		if (null != dtvOperator) {
			Log.i(TAG, "LL getCurOperator()>>dtvOperator = " + dtvOperator.getOperatorName());
		}
		if (dtvOperator == null) {
			dtvOperator = new DtvOperator(new Operator("", 0xffffffff));
			Log.e(TAG, "LL getCurOperator()>>dtvOperator == null");
		}
		return dtvOperator;
	}

	public void setCurOperator(DtvOperator operator) {
		if (null != operator) {
			Log.i(TAG, "LL setCurOperator(object)>>OPName = " + operator.getOperatorName());
			mDtvInterface.saveCurOperator(operator.getOperatorCode());
		}
	}

	/////////////////fengy 2014-5-30 //////////////
	public void setCurOperatorByCode(int operatorcode) {
		mDtvInterface.saveCurOperator(operatorcode);
	}

	public int getCurOperatorCode() {
		DtvOperator curOpatator = null;
		curOpatator = getCurOperator();
		return curOpatator.getOperatorCode();
	}

	public void setDtvLanguage(String uiLanguage) {
		mDtvInterface.setDtvLanguage(uiLanguage);
	}

	public void setDtvLanguageChinese(boolean isChinese) {
		if (isChinese == true) {
			setDtvLanguage("chi");
		} else {
			setDtvLanguage("eng");
		}

	}

	//////////////////FENGY 2014-5-13  modify///////////////
	public Map<String, List<DtvOperator>> getProvincesBySourceId(int sourceId, Context tmpcontext) {
		Log.i(TAG, "enter getProvincesBySourceId");
		List<DtvOperator> dtvOperList = null;
		List<DtvOperator> proviceList = null;
		mContext = tmpcontext;
		Map<String, List<DtvOperator>> map = new HashMap<String, List<DtvOperator>>();
		dtvOperList = getOperatorListBySourceID(sourceId);
		for (DtvOperator op : dtvOperList) {

			String provice = op.getProvince();

			if (null == provice) {
				provice = mContext.getString(R.string.dtv_scansettings_operator_general);//"???";
				//dtv_scansettings_operator_general
			}

			if (null != provice) {
				Log.i(TAG, "getProvincesBySourceId >>provice" + provice);
				if (map.containsKey(provice)) {
					proviceList = map.get(provice);
					proviceList.add(op);
				} else {
					proviceList = new ArrayList<DtvOperator>();
					proviceList.add(op);
					map.put(provice, proviceList);
				}
			}
		}
		if (map.size() == 0) {
			map.put(mContext.getString(R.string.dtv_scansettings_operator_countrywide), dtvOperList);
		}
		return map;
	}
}