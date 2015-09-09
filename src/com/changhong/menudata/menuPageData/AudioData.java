package com.changhong.menudata.menuPageData;

import android.content.Context;
import android.util.Log;
import com.changhong.data.pageData.ListPageData;
import com.changhong.data.pageData.itemData.ItemOptionData;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;

public class AudioData extends ListPageData{
	private static final String TAG = AudioData.class.getSimpleName();
	Context mContext = null;
	private DtvChannelManager mChannelManager = null;
	ItemOptionData audioMode = null;
	ItemOptionData audioTrack = null; 

	String[] audioModeStr = null;
	String[] audioTrackStr = null;

	public AudioData(String strTitle, int picTitle,Context context) {
		super(strTitle,picTitle);
		mContext = context;
		mChannelManager = DtvChannelManager.getInstance();
		mType = EnumPageType.BroadListPage;
		this.init();
	}

	public void updatePage(){
		Log.i(TAG,"LL updatePage()***");
		int curMode = mChannelManager.getAudioModeSel();
		Log.i(TAG,"LL updatePage()>>curMode = " + curMode);
		audioMode.setCurValue(curMode);
		int curTrack = mChannelManager.getAudioTrackSelIndex();
		Log.i(TAG,"LL updatePage()>>curTrack = " + curTrack);
		String [] trackStr = mChannelManager.getAudioTrack(audioTrackStr);
		audioTrack.setOptionValues(trackStr);
		audioTrack.setCurValue(curTrack);
		super.updatePage();
	}
	private void init() {
		audioModeStr = getContext().getResources().getStringArray(
				R.array.menu_audio_mode);
		int curMode = mChannelManager.getAudioModeSel();
		audioTrackStr = getContext().getResources().getStringArray(
				R.array.menu_audio_track);
		String [] trackStr = mChannelManager.getAudioTrack(audioTrackStr);
		int curTrack = mChannelManager.getAudioTrackSelIndex();
		audioMode = new ItemOptionData(0, getContext()
				.getResources()
				.getString(R.string.dtv_audiosettings_audio_mode), 0, 0) {

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

			@Override
			public void onValueChange(int Value) {
				// TODO Auto-generated method stub
				mChannelManager.setAudioMode(Value);
			}
		};
		audioMode.setOptionValues(audioModeStr);
		audioMode.setCurValue(curMode);
		audioMode.IsRealTimeData = true;
		audioTrack = new ItemOptionData(0, getContext()
				.getResources().getString(R.string.dtv_audiosettings_audio_track),
				0, 0) {

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

			@Override
			public void onValueChange(int Value) {
				// TODO Auto-generated method stub
				mChannelManager.setAudioTrack(Value);
			}
		};
		audioTrack.setOptionValues(trackStr);
		audioTrack.setCurValue(curTrack);
		audioTrack.IsRealTimeData = true;
		this.add(audioMode);
		this.add(audioTrack);
	}

	public Context getContext() {
		return mContext;
	}

}
