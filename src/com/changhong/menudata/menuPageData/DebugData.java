package com.changhong.menudata.menuPageData;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import com.changhong.data.pageData.ListPageData;
import com.changhong.data.pageData.itemData.ItemOptionData;
import com.changhong.data.pageData.itemData.ItemPromptData;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvAcrossPlatformAdaptationManager;
import com.changhong.tvos.dtv.tvap.DtvDebugManager;
import com.changhong.tvos.dtv.tvap.DtvInterface;

public class DebugData extends ListPageData {

	private ItemOptionData mDebugData;
	private ItemPromptData mTVOSVersion = null;
	private ItemPromptData mAndroidVersion = null;
	private ItemPromptData mPlatform = null;
	private DtvDebugManager mDtvDebugManager = null;
	private Context mContext;
	public DebugData(String strTitle, int picTitle) {
		super(strTitle, picTitle);
		// TODO Auto-generated constructor stub
	}

	public DebugData(String strTitle, int picTitle, Context context) {
		super(strTitle, picTitle);
		this.mContext = context;
		init();
	}

	public void init(){
		mDtvDebugManager = DtvDebugManager.getInstance();
		if(null == mDebugData){
			mDebugData = new ItemOptionData(0,
					getContext().getResources().getString(R.string.dtv_menu_system_debug),
					0, 0){

						@Override
						public void onValueChange(int Value) {
							// TODO Auto-generated method stub
							if(1 == Value){
								mDtvDebugManager.setDebug(false);
							}else{
								mDtvDebugManager.setDebug(true);
							}
						}

						@Override
						public int isEnable() {
							// TODO Auto-generated method stub
							return 1;
						}

						@Override
						public boolean initData() {
							// TODO Auto-generated method stub
							return false;
						}
				
			};
		}
		
		
		String[] array = mContext.getResources().getStringArray(R.array.str_dtv_usrset_fav_switch);
		mDebugData.setOptionValues(array);
		
		if(mDtvDebugManager.isDebug()){
			mDebugData.setCurValue(0);
		}else{
			mDebugData.setCurValue(1);
		}
		
		this.add(mDebugData);
		
		
		ItemPromptData mCheckOutSystemErrs =  new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_out_system_log),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public boolean onkeyDown(int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) || (keyCode == KeyEvent.KEYCODE_ENTER)){
					
					if(null != mDtvDebugManager){
						mDtvDebugManager.checkOutSystemErr();
						
						SimpleDateFormat sdf =new  SimpleDateFormat("_MM_dd_HH_mm");
						String end = sdf.format(new Date());
						
						Toast.makeText(mContext, "checkOutSystemErrInfo in 10s /sdcard/info" +end, Toast.LENGTH_LONG).show();
						return super.onkeyDown(KeyEvent.KEYCODE_BACK, event);
					}
				}
				return super.onkeyDown(keyCode, event);
			}
			
			
		};
		this.add(mCheckOutSystemErrs);
		
		mTVOSVersion = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_system_tvos_version),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mTVOSVersion.isOnlyShow = true;	
		
		mAndroidVersion = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_system_android_version),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mAndroidVersion.isOnlyShow = true;
		
		
		mPlatform = new ItemPromptData(0,getContext().getResources().getString(R.string.dtv_menu_system_platform),0,0) {
			
			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		mPlatform.isOnlyShow = true;
		
		this.add(mPlatform);
		this.add(mAndroidVersion);
		this.add(mTVOSVersion);
		DtvInterface mDtvInterface = DtvInterface.getInstance();
//////////////////////////////////////////////////////////////////////////////
		try {
			ApplicationInfo pkg;
			pkg = mContext.getPackageManager().getApplicationInfo(
			mContext.getPackageName(),  
			PackageManager.GET_META_DATA);
			String chipMode =  pkg.metaData.getString("ChipModel");

//			String[] platform_name = //chipMode.split("_");
		/*	if(null != platform_name){
				if(platform_name[0].contains(DtvAcrossPlatformAdaptationManager.GetPlatTypeName(mContext))){
				mPlatform.setValue(platform_name[0]);
				}else{
					mPlatform.setValue(DtvAcrossPlatformAdaptationManager.GetPlatTypeName(mContext)); //manifest与平台不一致
					Log.i("DEBUG", "Debug>>manifest is not adapt to the platform ");
				}
			}
			*/
           if(null != chipMode){
				if(chipMode.contains(DtvAcrossPlatformAdaptationManager.GetPlatTypeName(mContext))){
				mPlatform.setValue(chipMode);
				}else{
					mPlatform.setValue(DtvAcrossPlatformAdaptationManager.GetPlatTypeName(mContext)); //manifest与平台不一致
					Log.i("DEBUG", "Debug>>manifest is not adapt to the platform ");
				}
			}			
			
		} catch (NameNotFoundException e) {
		// TODO Auto-generated catch block
		}
/////////////////////////////////////////////////////////////////////////////////

//		mPlatform.setValue(DtvAcrossPlatformAdaptationManager.GetPlatTypeName(mContext));
		mAndroidVersion.setValue(mDtvInterface.getAndroidVersion());
		mTVOSVersion.setValue(mDtvInterface.getTVOSversion());
	}

	public void updatePage(){
		super.updatePage();
	}
	
	public boolean onkeyDown(int keyCode,KeyEvent event){
		return super.onkeyDown(keyCode, event);
		
	}
	private Context getContext() {
		// TODO Auto-generated method stub
		return mContext;
	}
}
