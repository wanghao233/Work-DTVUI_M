package com.changhong.menudata;

import android.content.Context;
import com.changhong.data.MenuData;
import com.changhong.data.pageData.ListPageData;
import com.changhong.menudata.menuPageData.MainMenuRootData;
import com.changhong.tvos.dtv.R;

public class MenuDataTree extends MenuData{
	Context mContext;
	public MainMenuRootData mMainMenuRootData;
	
	MenuDataTree(Context context){
		mContext =context;
		mMainMenuRootData = new MainMenuRootData(mContext.getString(R.string.dtv_menu_main_title),R.drawable.main_menu_title,mContext);

		mTopPage = mMainMenuRootData;
		mCurPage = mMainMenuRootData;
	}
	
	public void update(ListPageData curPageData){
		mCurPage =curPageData;
	}
	
	public void update(ListPageData topPageData,ListPageData curPageData){
		mTopPage = topPageData;
		mCurPage =curPageData;
	}
	
	public void update(ListPageData pageData,int itemId){
		mCurPage =pageData;
		mCurPage.updatePage();
		mCurPage.setCurItem(itemId);
	}
	
	public void update(ListPageData topPageData, ListPageData curPageData,int itemId){
		mTopPage =topPageData;
		mCurPage =curPageData;
		mCurPage.updatePage();
		mCurPage.setCurItem(itemId);
	}
}