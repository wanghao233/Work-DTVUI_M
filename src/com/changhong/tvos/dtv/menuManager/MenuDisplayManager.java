package com.changhong.tvos.dtv.menuManager;

import java.util.ArrayList;
import java.util.List;
import com.changhong.menudata.MainMenuReceiver;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;

public class MenuDisplayManager {
	private static final String TAG = MenuDisplayManager.class.getSimpleName();
	private Context mContext = null;
	private List<View> mViewList = new ArrayList<View>();
	private List<Dialog> mDialogList = new ArrayList<Dialog>();
	private static MenuDisplayManager manager = null; 
	private MenuDisplayManager(Context context){
		mContext = context;
	}
	public static MenuDisplayManager getInstance(Context context){
		if(manager==null){
			manager=new MenuDisplayManager(context);
		}
		return manager;
	}
	public void registerViewShowing(View view){
		if(view != null&&!isExists(view)){
			mViewList.add(view);
			Log.i(TAG,"LL registerViewShowing()>>view = " + view);
		}
	}
	
	public void unregisterViewShowed(View view){
		if(view != null&&isExists(view)){
			mViewList.remove(view);
			Log.i(TAG,"LL unregisterViewShowed()>>view = " + view);
		}
	}
	
	public void registerDialogShowing(Dialog dialog){
		if(dialog!=null&&!isExists(dialog)){
			mDialogList.add(dialog);
			Log.i(TAG,"LL registerDialogShowing()>>dialog = " + dialog);
		}
	}
	
	public void unregisterDialogShowed(Dialog dialog){
		if(dialog!=null&&isExists(dialog)){
			mDialogList.remove(dialog);
			Log.i(TAG,"LL unregisterDialogShowed()>>dialog = " + dialog);
		}
	}
	private boolean isExists(View view){
		for(int i=0;i<mViewList.size();i++){
			
			if(mViewList.get(i).equals(view)){
				Log.i(TAG,"LL isExists()>>find it***view="+view);
				return true;
			}
		}
		Log.i(TAG,"LL isExists()>>not find it***view="+view);
		return false;
	}
	private boolean isExists(Dialog dialog){
		for(int i=0;i<mDialogList.size();i++){
		
			if(mDialogList.get(i).equals(dialog)){
				Log.i(TAG,"LL isExists()>>find it***dialog="+dialog);
				return true;
			}
		}
		Log.i(TAG,"LL isExists()>>not find it***dialog="+dialog);
		return false;
	}
	public void dismissAllRegistered(){
		for(int i=0;i<mViewList.size();i++){
			if(mViewList.get(i).isShown()){
				
				mViewList.get(i).setVisibility(View.GONE);
			}
		}
		for(int i=0;i<mDialogList.size();i++){
			if(mDialogList.get(i).isShowing()){
				
				mDialogList.get(i).dismiss();
			}
		}
		mViewList.clear();
		mDialogList.clear();
		MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
	}
}
