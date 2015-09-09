package com.changhong.tvos.dtv.scan;

import java.util.List;
import com.changhong.menudata.menuPageData.MainMenuRootData;
import com.changhong.tvos.dtv.channel_manager.FilterChannels;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import android.content.Context;
import android.util.Log;

public class MenuFilter {

	private static final String TAG = "MenuFilter";
	private Context mContext;
	private static MenuFilter mInstance;
	private boolean isShowFilter = false;
	private FilterChannels filterChannels;
	private List<DtvProgram> channList;

	private MenuFilter(Context context) {
		super();
		this.mContext = context;

		filterChannels = FilterChannels.getInstance(mContext);
		channList = DtvChannelManager.getInstance().getChannelList();
	}

	public synchronized static MenuFilter getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new MenuFilter(context);
		}
		return mInstance;
	}

	public void startFilter() {
		Log.i(TAG, "-------------------------startFilter");
		//已经过滤掉的节目是否需要再次过滤
		/*for(DtvProgram program: channList){
			if(program.isSkip()){
				Log.i(TAG, "program: " + program.mProgramName + " num:"+program.mProgramNum + " serviceIndex: " + program.mServiceIndex + " skipped");
			}
		}*/

		if (channList != null && channList.size() > 0) {// 节目列表不为空，开始过滤
			Log.i(TAG, "channel List.size=" + channList.size());

			filterChannels.show();
			setShowFilter(true);
			if (filterChannels.isFilter()) {// 正在过滤时则继续
				Log.i(TAG, "SmartSkip is already on");
			} else {// 还没有开始过滤则准备过滤
				Log.i(TAG, "start SmartSkip");
				filterChannels.startSmartSkip();
			}
		} else {// 节目列表为空，隐藏过滤细节
			//			MainMenuRootData.HideFilterMenuDetail();
			MainMenuRootData.HideFilterMenu(); //2015-4-17
			setShowFilter(false);
		}
		//		}
	}

	public void cancelFilter() {
		Log.i(TAG, "cancelFilter");
		filterChannels.hide();

		Log.i(TAG, "filterChannels.isFilter()=" + filterChannels.isFilter());
		if (filterChannels.isFilter()) {
			Log.i(TAG, "cancelSmartSkip");
			filterChannels.cancelSmartSkip();
		}
	}

	public void setShowFilter(boolean isShowFilter) {
		this.isShowFilter = isShowFilter;
	}

	public boolean isShowFilter() {
		return isShowFilter;
	}
}