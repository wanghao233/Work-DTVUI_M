package com.changhong.menudata.menuPageData;

import java.util.List;
import android.content.Context;
import android.util.Log;
import com.changhong.data.pageData.ListPageData;
import com.changhong.data.pageData.itemData.ItemHaveSubData;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstPageDataID;
import com.changhong.tvos.dtv.vo.DTVSource;

public class SourceSetupData extends ListPageData {
	
	private static final String TAG ="SourceSetupData";
	Context mContext = null;
	private List<DTVSource> mSourceList = null;
	DtvSourceManager mSourceManager = null;
	private int mCurSourceIndex;
	boolean mSetSelected = true;
	
	public SourceSetupData(String strTitle, int picTitle,Context context){
		super(ConstPageDataID.SOURCE_SETUP_PAGE_DATA, strTitle, picTitle);
		mContext = context;
		mType =  EnumPageType.BroadListPage;
		this.init();
	}

	public void init() {
		// TODO Auto-generated method stub
		mSourceManager = DtvSourceManager.getInstance();
		mSourceList = mSourceManager.getSourceList();
		mCurSourceIndex = mSourceManager.getCurSourceIndex();
		if(null != mSourceList && mSourceList.size() > 0){
			int size = mSourceList.size();
			for(int i = 0; i < size; i++){
				ItemHaveSubData data = new ItemHaveSubData(0, mSourceList.get(i).miSourceName, 0, 0){

					@Override
					public int isEnable() {
						// TODO Auto-generated method stub
						return 1;
					}

					@Override
					public void onNextPage() {
						// TODO Auto-generated method stub
						
					}

				};
				OperatorListData operatorListData = new OperatorListData(getContext()
						.getString(R.string.dtv_scansettings_operator_setup_title),
						R.drawable.menu_operator_setup_pic_title, mSourceManager.getSourceIDByIndex(i),mContext);

				this.add(data);
				this.setNextPage(i, operatorListData);
			}
		}else{
			Log.i(TAG, "LL init()>>mSourceList==null");
		}
	}
	
	public void updatePage(){
		
		if(true == mSetSelected){
			mSetSelected = false;
			if(mSourceManager.getCurSourceIndex()!=mCurSourceIndex){
				mCurSourceIndex = mSourceManager.getCurSourceIndex();
			}
			loadItemList();
			setCurItemIndex(mCurSourceIndex);
			loadCurItemList();
			updateRealTimeData();
		}else{
			super.updatePage();
		}
	}

	public void reset(){
		mSetSelected = true;
		super.reset();
	}
	
	public Context getContext() {
		return mContext;
	}
}
