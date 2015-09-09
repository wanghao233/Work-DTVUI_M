package com.changhong.data.pageData;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.view.KeyEvent;
import com.changhong.data.pageData.itemData.util.EnumDataType;
import com.changhong.data.pageData.itemData.util.ItemData;
import com.changhong.data.pageData.itemData.util.ItemData.OnNextPageListener;

public class ListPageData extends PageData{
	
 	private static final String TAG = ListPageData.class.getSimpleName();
	public static int mPerPageItemNum =8;
	protected static List<PageData> mPageDataAll = new ArrayList<PageData>();
 	private List<ItemData> mItemListAll = new ArrayList<ItemData>();
 	private List<ItemData> mItemList =new ArrayList<ItemData>();;
	private List<ItemData> mCurList =new ArrayList<ItemData>();
	private ItemData mCurItem =null;
	
	private boolean mUpdatePageData = false;
	private int mCurItemIndex =0;
	private int pageIndex =0;
	private int pageNum =0;
	private String mStrTitle = "NO TITLE";
	private int mPicTitle = 0;

	public ListPageData(String strTitle,int picTitle){
		super(null);
		mStrTitle =strTitle;
		mPicTitle = picTitle;
	}
	public ListPageData(String pageId ,String strTitle,int picTitle){
		super(pageId);
		mStrTitle =strTitle;
		mPicTitle = picTitle;
	}
	
	public String getStrTitle(){
		return mStrTitle;
	}
	
	public int getPicTitle(){
		return mPicTitle;
	}
	
	public int getPageIndex(){
		Log.i(TAG,"LL getPageIndex()>>pageIndex = " + pageIndex);
		return pageIndex;
	}
	public int getFirstSelectedIndex(){
		if(mItemList !=null){
			for(int i=0;i<mItemList.size();i++){
				if(mItemList.get(i).isEnable()>0){
					return i;
				}
			}
		}
		return 0;
	}
	public int getLastSelectedIndex(){
		if(mItemList !=null){
			for(int i=mItemList.size()-1;i>=0;i--){
				if(mItemList.get(i).isEnable()>0){
					return i;
				}
			}
		return mItemList.size()-1;
	}
		return 0;
	}
	public ItemData get(int index){
		if(mItemList !=null&&index>=0&&index<mItemList.size()){
			return mItemList.get(index);
		}
		return null;
	}
	public void add(ItemData item){
		if(mItemListAll ==null){
			mItemListAll =new ArrayList<ItemData>();
		}
		mItemListAll.add(item);
		
		if(item.getDataType() ==EnumDataType.EN_HAVESUBCHILD){
			if(!item.isOnlyShow){
				item.setOnNextPageListener(new OnNextPageListener() {
					@Override
					public void onNextPage(PageData data) {
						// TODO Auto-generated method stub
							onPageTurn(data);
					}
				});
			}else{
				
			}
			
		}
	}
		
	public int getItemDataIndexInPage(ItemData item){
		if(null == mItemListAll){
			return -1;
		}
		return mItemListAll.indexOf(item);
	}
	
	public void setClickedItem(int index){
		
	}
	public void setCurItem(ItemData item){
		if(mCurItem !=null){
			mCurItem.clearFocus();
		}
		mCurItem =item;
		mCurItemIndex =mItemList.indexOf(mCurItem);
		mCurItem.requestFocus();
	}
	
	public void setCurItemIndex(int index){
		if(mItemList!=null&&index>=0&&index<mItemList.size()){
			Log.i(TAG,"LL mItemList.size() = " + mItemList.size());
			mCurItemIndex =index;
		}else{
			Log.e(TAG,"LL mCurItemIndex == 0");
			mCurItemIndex = 0;
		}
	}
	
	public void setCurItem(int index){
		mCurItemIndex =index;
		mCurItem =mItemList.get(mCurItemIndex);
	}
	public ItemData getCurItem(){
		if(mCurItem !=null){
			return mCurItem;
		}else{
			if(mItemList!=null&&mItemList.size()>0){
				return mItemList.get(0);
			}
			return null;
		}
	}
	public int getItemIndex(ItemData item){
		if(item !=null){
			return mItemList.indexOf(item);
		}else{
			return 0;
		}
	}
	protected void loadItemList(){
		if(mItemListAll==null ||mItemListAll.size() ==0){
			Log.e(TAG,"LL loadItemList()>>mItemListAll.size() == 0");
			return;
		}
		mItemList.clear();
		for(int i=0;i<mItemListAll.size();i++){
			if(mItemListAll.get(i).isEnable()>=0){
				mItemList.add(mItemListAll.get(i));
			}
		}
	}
	protected void loadCurItemList(){
		if(mItemList==null||mItemList.size()==0){
			Log.e(TAG,"LL loadCurItemList()>>mItemList.size() == 0");
			return;
		}
		mCurList.clear();
		pageNum =(mItemList.size()+mPerPageItemNum-1)/mPerPageItemNum;
		Log.i(TAG,"LL mCurItemIndex = " + mCurItemIndex);
		pageIndex =mCurItemIndex/mPerPageItemNum;
		
		if( mItemList.size() <= mCurItemIndex )
		{
			Log.i(TAG,"LL IndexOutOfBoundsException mCurItemIndex = " + mCurItemIndex +", " +mItemList.size());
			return;
		}
		mCurItem =mItemList.get(mCurItemIndex);
		
		int index =pageIndex*mPerPageItemNum;
		
		for(;index<mItemList.size();index++){
			mCurList.add(mItemList.get(index));
			if(mCurList.size() ==mPerPageItemNum){
				break;
			}
		}
	}
	protected void updateRealTimeData(){
		if(mItemList==null||mItemList.size()==0){
			Log.e(TAG,"LL updateRealTimeData()>>mItemList.size() == 0");
			return;
		}
		for(ItemData item:mItemList){
			if(item.IsRealTimeData){
				item.initData();
				item.update();
			}
		}
	}
	private void initCurList(){
		this.loadItemList();
		this.loadCurItemList();		
	}

//	private void initCurList(){
//		if(mItemListAll==null ||mItemListAll.size() ==0){
//			return;
//		}
//
//		mCurList.clear();
//		mItemList.clear();
//
//		for(int i=0;i<mItemListAll.size();i++){
//			if(mItemListAll.get(i).isEnable()>=0){
//				mItemList.add(mItemListAll.get(i));
//			}
//		}
//
//		pageNum =(mItemList.size()+mPerPageItemNum-1)/mPerPageItemNum;
//		Log.i(TAG,"LL mCurItemIndex = " + mCurItemIndex);
//		pageIndex =mCurItemIndex/mPerPageItemNum;
//		mCurItem =mItemList.get(mCurItemIndex);
//		
//		int index =pageIndex*mPerPageItemNum;
//		
//		for(;index<mItemList.size();index++){
//			mCurList.add(mItemList.get(index));
//			if(mCurList.size() ==mPerPageItemNum){
//				break;
//			}
//		}
//	}
	public List<ItemData> getCurItemList(){
		if(mCurList.size() ==0){
			initCurList();
		}
		return mCurList;
	}
	
	public int getCurItemIndex(){
		return mCurItemIndex;
	}
	public boolean isShowMultiPageIcon(){
		return pageIndex < pageNum-1;
	}
	public boolean onkeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v(TAG,"LL onkeyDown()>>keyCode="+keyCode + ",mCurItem"+mCurItem);
		mUpdatePageData = false;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if(isFoucsAble == false){
				if(pageIndex == 0){					
					mCurItemIndex =(mCurItemIndex + mItemList.size()  - (mItemList.size() - (pageNum - 1)*mPerPageItemNum) - (mCurItemIndex - pageIndex*mPerPageItemNum))%mItemList.size();
				}else{
					
					mCurItemIndex =(mCurItemIndex +mItemList.size()- mPerPageItemNum - (mCurItemIndex - pageIndex*mPerPageItemNum))%mItemList.size();
				}
				if(pageIndex !=mCurItemIndex/mPerPageItemNum){
					if(mCurItem!=null){
						mCurItem.clearFocus();
					}
					initCurList();
					onPageTurn(this);
					return true;	
				}
			}

			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if(isFoucsAble == false){
				if(pageIndex == pageNum-1){					
					mCurItemIndex =(mCurItemIndex + (mItemList.size() - (pageNum-1)*mPerPageItemNum) - (mCurItemIndex - pageIndex*mPerPageItemNum))%mItemList.size();
				}else{
					
					mCurItemIndex =(mCurItemIndex+ mItemList.size() + mPerPageItemNum - (mCurItemIndex - pageIndex*mPerPageItemNum))%mItemList.size();
				}
				if(pageIndex !=mCurItemIndex/mPerPageItemNum){
					if(mCurItem!=null){
						mCurItem.clearFocus();
					}
					initCurList();
					onPageTurn(this);
					return true;	
				}
			}
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			
			if(isFoucsAble == false){
				if(pageIndex == 0){					
					mCurItemIndex =(mCurItemIndex + mItemList.size()  - (mItemList.size() - (pageNum - 1)*mPerPageItemNum) - (mCurItemIndex - pageIndex*mPerPageItemNum))%mItemList.size();
				}else{
					
					mCurItemIndex =(mCurItemIndex +mItemList.size()- mPerPageItemNum - (mCurItemIndex - pageIndex*mPerPageItemNum))%mItemList.size();
				}
			}else{
				Log.i(TAG,"LL mCurItemIndex = " + mCurItemIndex + ",firstSelectedIndex = " + getFirstSelectedIndex());
				if(mCurItemIndex ==getFirstSelectedIndex()){
					mUpdatePageData = true;
					mCurItemIndex = getLastSelectedIndex();
				}else{
					mCurItemIndex =(mCurItemIndex+mItemList.size() -1)%mItemList.size();
				}
			}
			
			if(pageIndex !=mCurItemIndex/mPerPageItemNum || mUpdatePageData == true){
				if(mCurItem!=null){
					mCurItem.clearFocus();
				}
				initCurList();
				onPageTurn(this);
				return true;	
			}
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			
			if(isFoucsAble == false){
				if(pageIndex == pageNum-1){					
					mCurItemIndex =(mCurItemIndex + (mItemList.size() - (pageNum-1)*mPerPageItemNum) - (mCurItemIndex - pageIndex*mPerPageItemNum))%mItemList.size();
				}else{
					
					mCurItemIndex =(mCurItemIndex+ mItemList.size() + mPerPageItemNum - (mCurItemIndex - pageIndex*mPerPageItemNum))%mItemList.size();
				}
			}else{
				Log.i(TAG,"LL mCurItemIndex = " + mCurItemIndex + ",lastSelectedIndex = " + getLastSelectedIndex());
				if(mCurItemIndex ==getLastSelectedIndex()){
					mUpdatePageData = true;
					mCurItemIndex = getFirstSelectedIndex();
				}else{
					
					mCurItemIndex =(mCurItemIndex +1)%mItemList.size();
				}
			}
			
			if(pageIndex !=mCurItemIndex/mPerPageItemNum || mUpdatePageData == true){
				if(mCurItem!=null){
					mCurItem.clearFocus();
				}
				initCurList();
				onPageTurn(this);
				return true;	
			}
			break;
		case KeyEvent.KEYCODE_BACK:	
			if(mParent !=null){
				if(mType == EnumPageType.BroadListPage ||
				mType == EnumPageType.NarrowListPage){
					reset();
					onPageTurn((ListPageData)mParent);
					return true;	
				}
			}
			break;
		default:
			break;
		}
		return mCurItem!=null?mCurItem.onkeyDown(keyCode ,event):false;
	}

	/**
	 * 左右翻页方法
	 * @param direction
	 */
	public void pageNextByDirection(int direction){
		switch(direction){
			case KeyEvent.KEYCODE_DPAD_LEFT:
					if(pageIndex == 0){					
						mCurItemIndex =(mCurItemIndex + mItemList.size()  - (mItemList.size() - (pageNum - 1)*mPerPageItemNum) - (mCurItemIndex - pageIndex*mPerPageItemNum))%mItemList.size();
					}else{
						
						mCurItemIndex =(mCurItemIndex +mItemList.size()- mPerPageItemNum - (mCurItemIndex - pageIndex*mPerPageItemNum))%mItemList.size();
					}
					if(pageIndex !=mCurItemIndex/mPerPageItemNum){
						if(mCurItem!=null){
							mCurItem.clearFocus();
						}
						initCurList();
						onPageTurn(this);
					}
	
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
					if(pageIndex == pageNum-1){					
						mCurItemIndex =(mCurItemIndex + (mItemList.size() - (pageNum-1)*mPerPageItemNum) - (mCurItemIndex - pageIndex*mPerPageItemNum))%mItemList.size();
					}else{
						
						mCurItemIndex =(mCurItemIndex+ mItemList.size() + mPerPageItemNum - (mCurItemIndex - pageIndex*mPerPageItemNum))%mItemList.size();
					}
					if(pageIndex !=mCurItemIndex/mPerPageItemNum){
						if(mCurItem!=null){
							mCurItem.clearFocus();
						}
						initCurList();
						onPageTurn(this);
					}
				break;
			default: 
				break;
		}
	}
	public void updatePage(){
		initCurList();
		updateRealTimeData();
	}
	
	public void registPageData(PageData pageData){
		if(mPageDataAll == null){
			mPageDataAll = new ArrayList<PageData>();
		}
		mPageDataAll.add(pageData);
	}
	public PageData getPageDataById(String strPageID){
		if(mPageDataAll != null&&strPageID!=null){
			for(PageData pageData:mPageDataAll){
				if(strPageID.equals(pageData.mPageId)){
					Log.i(TAG,"LL find pageData data by pageID***");
					return pageData;
				}
			}
		}
		return null;
	}
	public void setNextPage(ItemData itemData,PageData nextPageData){
		nextPageData.mParent =ListPageData.this;
		itemData.mChild =nextPageData;
	}
	public void setNextPage(int index,PageData nextPageData){
		if(index <0 || index >=mItemListAll.size()) {
			Log.i(TAG, "index is err:" + index);
			return;
		}
		nextPageData.mParent =ListPageData.this;
		mItemListAll.get(index).mChild =nextPageData;
	}
	
	public void reset(){
		if(mCurItem !=null){
			mCurItem.clearFocus();
		}
		mCurItem =null;
		mCurItemIndex =0;
		mCurList.clear();
	}
	public void clear(){
		reset();
		mItemListAll.clear();
	}
	
	public void removeItem(ItemData item){
		if(null == item){
			return;
		}
		mItemListAll.remove(item);
		item.setView(null);
		item = null;
		
	}
	
	public void unRegistPageData(PageData pageData){
		if(mPageDataAll == null || pageData == null ||mPageDataAll.size() == 0){
			return;
		}
		mPageDataAll.remove(pageData);
	}
}