package com.changhong.tvos.dtv.for3rd;

import android.os.Parcel;
import android.os.Parcelable;
import com.changhong.tvos.dtv.for3rd.InterChannelInfo;
import com.changhong.tvos.dtv.for3rd.InterTunerStatus;
import com.changhong.tvos.dtv.for3rd.InterAudioTrack;
import com.changhong.tvos.dtv.for3rd.InterEPGEvent;
import com.changhong.tvos.dtv.for3rd.InterVersionInfo;

interface IDTV3rdService {
	IBinder createDTVManager(String uuid, IBinder playerListerner);
}