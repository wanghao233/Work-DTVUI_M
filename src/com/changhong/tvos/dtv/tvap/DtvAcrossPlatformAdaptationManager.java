package com.changhong.tvos.dtv.tvap;

import com.changhong.softkeyboard.CHSoftKeyboardManager;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * @author Administrator
 *
 */
public class DtvAcrossPlatformAdaptationManager {
	public class PlatformType {
		public static final String PLATFORM_TYPE_MTK= "MTK" ;
		public static final String PLATFORM_TYPE_MSTAR= "MSTAR" ;

	}
	private static final String TAG = DtvAcrossPlatformAdaptationManager.class.getSimpleName();
	private static  String PLATFORM_TYPE = PlatformType.PLATFORM_TYPE_MTK;
	private static DtvInterface mInterface = DtvInterface.getInstance();
	private DtvAcrossPlatformAdaptationManager(){}
	
	public static String GetPlatTypeName(Context context){
		if(AdjustCHSoftKeyboardManager.mAdjustCHSKMInstance == null){
			AdjustCHSoftKeyboardManager.mAdjustCHSKMInstance = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(context);
		}
		Log.i(TAG, "curPlatType is " + PLATFORM_TYPE);
		return PLATFORM_TYPE;
	}
	
	public static class AdjustCHSoftKeyboardManager{
		private static AdjustCHSoftKeyboardManager mAdjustCHSKMInstance = null;
		private Context mContext = null;
		private CHSoftKeyboardManager mCHSKM = null;
		private AdjustCHSoftKeyboardManager(Context context){
			mContext = context;
			if(PlatformType.PLATFORM_TYPE_MTK.equals(PLATFORM_TYPE)){
				Log.i(TAG, "AdjustCHSoftKeyboardManager>>1;");

				mCHSKM = (CHSoftKeyboardManager)context.getSystemService(Context.SOFT_KEYBOARD_SERVICE);	
				if(null == mCHSKM ){
					Log.i(TAG, "AdjustCHSoftKeyboardManager>>have to choose 2;");
					
					mCHSKM = CHSoftKeyboardManager.getInstance(context); 
					PLATFORM_TYPE = PlatformType.PLATFORM_TYPE_MSTAR;
				}
			}else if(PlatformType.PLATFORM_TYPE_MSTAR.equals(PLATFORM_TYPE)){
				
				Log.i(TAG, "AdjustCHSoftKeyboardManager>>2;");
	
				mCHSKM = CHSoftKeyboardManager.getInstance(context); 	
			}else{
				Log.i(TAG, "AdjustCHSoftKeyboardManager>>3;");
		
				
				mCHSKM = (CHSoftKeyboardManager)context.getSystemService(Context.SOFT_KEYBOARD_SERVICE);
			}
			
			Log.i(TAG, "AdjustCHSoftKeyboardManager>>mCHSKM="+mCHSKM);
		}
		
		public static AdjustCHSoftKeyboardManager getAdjustSoftKeyboardInstance(Context context){
			if(mAdjustCHSKMInstance==null){
				String name = mInterface.getPlatformName();
				if(null != name){
					try {
						String subString = name.substring(0, 3);
						if (subString.contains("S")) {
							PLATFORM_TYPE = PlatformType.PLATFORM_TYPE_MSTAR;
						} else if (subString.contains("M")) {
							PLATFORM_TYPE = PlatformType.PLATFORM_TYPE_MTK;
						}
					} catch (Exception e) {
						
					}
					
				}else{
					PLATFORM_TYPE = PlatformType.PLATFORM_TYPE_MTK;
				}
				
         //rectify by fengyang
//					try {
//					ApplicationInfo pkg;
//					pkg = context.getPackageManager().getApplicationInfo(
//							context.getPackageName(),  
//							PackageManager.GET_META_DATA);
//					String chipMode =  pkg.metaData.getString("chipmodel");
//					if(chipMode.startsWith("MSTAR")){
//							PLATFORM_TYPE = PlatformType.PLATFORM_TYPE_MSTAR;
//					}else{
//							PLATFORM_TYPE = PlatformType.PLATFORM_TYPE_MTK;
//						}
//				} catch (NameNotFoundException e) {
//				// TODO Auto-generated catch block
//					Log.i(TAG, "configOperator()-->" + e);
//					}
        //fengyang rectify end
				mAdjustCHSKMInstance = new AdjustCHSoftKeyboardManager(context);
			}
			return mAdjustCHSKMInstance;
		}
		public boolean isSoftKeyPanelOnShow(){
			if(null == mCHSKM){
				Log.e(TAG, "isSoftKeyPanelOnShow>>(null == mCHSKM)");
				return false;
			}
			return mCHSKM.isPanelOnShow();
		}
		public void processNumberSoftKeyPanel(int keyCode, int position){
			if(null != mCHSKM){
			mCHSKM.processNumberKeyPanel(keyCode, position);
			}else{
				Log.e(TAG, "processNumberSoftKeyPanel()>>(null == mCHSKM)");
			}
		}
	}
	public static class AdjustAudioManager{
		private static AdjustAudioManager mAdjustAudioManager = null;
		private Context mContext = null;
		private AudioManager mAudioManager = null;
		private AdjustAudioManager(Context context){
			mContext = context;
			mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		}
		public static AdjustAudioManager getAdjustAudioManagerInstance(Context context){
			if(mAdjustAudioManager==null){
				mAdjustAudioManager = new AdjustAudioManager(context);
			}
			
			if(AdjustCHSoftKeyboardManager.mAdjustCHSKMInstance == null){
				AdjustCHSoftKeyboardManager.mAdjustCHSKMInstance = AdjustCHSoftKeyboardManager.getAdjustSoftKeyboardInstance(context);
			}
			return mAdjustAudioManager;
		}
		
		public void adjustStreamVolume(int adjustType){
			if(PlatformType.PLATFORM_TYPE_MTK.equals(PLATFORM_TYPE)){
				Log.i(TAG, "adjustStreamVolume()>> from MTK");
				mAudioManager.adjustVolume(adjustType, AudioManager.FLAG_SHOW_UI);
				
			}else if(PlatformType.PLATFORM_TYPE_MSTAR.equals(PLATFORM_TYPE)){
				Log.i(TAG, "adjustStreamVolume()>> from MSTAR");
				mAudioManager.adjustStreamVolume(AudioManager.STREAM_TV,adjustType, AudioManager.FLAG_SHOW_UI);
				
			}else{
				
				mAudioManager.adjustVolume(adjustType, AudioManager.FLAG_SHOW_UI);
			}
		}
		
		public void setStreamVolume(int value){
			if(PlatformType.PLATFORM_TYPE_MTK.equals(PLATFORM_TYPE)){
				Log.i(TAG, "setStreamVolume()>>  MTK");
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value, AudioManager.FLAG_SHOW_UI);
				
			}else if(PlatformType.PLATFORM_TYPE_MSTAR.equals(PLATFORM_TYPE)){
				Log.i(TAG, "setStreamVolume()>>  MSTAR");
				mAudioManager.setStreamVolume(AudioManager.STREAM_TV, value, AudioManager.FLAG_SHOW_UI);
			}else{
				
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value, AudioManager.FLAG_SHOW_UI);
			}
		}
		
		public int getStreamVolume(){
			int temVolume = 0;
			if(PlatformType.PLATFORM_TYPE_MTK.equals(PLATFORM_TYPE)){
				Log.i(TAG, "getStreamVolume()>>  MTK");
				temVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
				
			}else if(PlatformType.PLATFORM_TYPE_MSTAR.equals(PLATFORM_TYPE)){
				Log.i(TAG, "getStreamVolume()>>  MSTAR");
				temVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_TV);
			}else{
				temVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			}
			
			return temVolume;
		}
		
		public boolean isStreamMute(int streamType){
			boolean bMute = false;
			bMute = mAudioManager.isStreamMute(streamType);
			return bMute;
		}
		
		public void setStreamMute(int streamType, boolean state, boolean needUI){
			mAudioManager.setStreamMute(streamType, state, needUI);
		}
	}	
}