package com.changhong.pushoutView;

import java.util.ArrayList;
import java.util.List;
import com.changhong.pushoutView.AsyncImageLoader.ImageCallback;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.provider.BaseChannel;
import com.changhong.tvos.dtv.provider.BaseProgram;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class MagicBaseButton extends HorizontalView{

	private static final String TAG = "DTVpushview" ;
	
	protected Context mContext;
//	protected ImageView mPosterView ;
//	protected TextView mTextView;
//	protected ImageView mHalfView;
	
//	protected ColorAnimView mColorView;

	protected BaseProgram mBaseProgram;
	
	protected boolean Showable = false;
	
	//the postis OK or not when 0 not ok 1 was OK
	protected int post_image_flag;
	protected boolean business_flag = false;

	protected String postimgeurl;
	
	List<BaseChannel> mBaseChannelList;
	
	protected int  serviceId = -1;
	
	protected MagicBaseButton mMagicBaseButton = this;
	
	public MagicBaseButton(Context context) {
		super(context,230,350);
		// TODO Auto-generated constructor stub
	    mContext = context;
	    magicButtonInit();
	}

	
	public void setInfo(BaseProgram baseProgram, List<BaseChannel> baseChannelList) {
		
		mBaseProgram = baseProgram;
		mBaseChannelList = baseChannelList;
		business_flag = false;
	}
	
	
	public void magicButtonInit() {
		
		post_image_flag = MagicButtonCommon.POST__NO_NEED;
		
	}

	public void setShowable(boolean show) {
		Showable = show;
	}
	
	public boolean getShowable() {
		Log.d(TAG, "the post_image_flag is " + this.post_image_flag);
		Log.d(TAG, "the showable is " + this.Showable);
		Log.d(TAG, "the business_flag is " + business_flag);
		return this.Showable && (this.post_image_flag != MagicButtonCommon.POST_IMAGE_NOTREADY) && this.business_flag;
	}
	
	public String getBusinessName() {
		return null;
	}
	
	public int getPostImageFlag() {
		
		return post_image_flag;
		
	}
	
	protected Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case 0:
				setPostBg(postimgeurl);
				break;

			default:
				break;
			}
		}
	};

	protected void setpostBgBychild() {
		mHandler.sendEmptyMessage(0);
	}
	
	
	protected void setPostBg(String postimgeurl) {
		new Thread().start();
		final ImageView imageView = mHorizontalViewContainer.mPosterView;
		imageView.setTag(postimgeurl);
		post_image_flag = MagicButtonCommon.POST_IMAGE_NOTREADY;
		Log.d(TAG, "the url is " + postimgeurl);
		Drawable cachedImage = AsyncImageLoader.getInstance().loadDrawable(
				postimgeurl, new ImageCallback() {
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {
						
						if (imageView != null) {
							imageView.setImageDrawable(imageDrawable);
							post_image_flag = MagicButtonCommon.POST_IMAGE_READY;
						}
					}
				});
		if (cachedImage == null) {
//			imageView.setImageResource(R.drawable.mediavideo);
			if("BusinessUserAdded".equals(this.getBusinessName())) {
				imageView.setImageResource(R.drawable.pushview_default_s);
				Log.d(TAG, "the bg img posted and not ready use loaclpicture");
				post_image_flag = MagicButtonCommon.POST_IMAGE_READY;
			}
		} else {
			imageView.setImageDrawable(cachedImage);
			Log.d(TAG, "the bg img posted ready ");
			post_image_flag = MagicButtonCommon.POST_IMAGE_READY;
		}
	}

	
	protected int getBaseChannelsByHChannelname(ArrayList<ProgramInfo> Hprogram) {
		List<BaseChannel> mBaseChannelList = new ArrayList<BaseChannel>();
		ContentResolver contentResolver = mContext.getContentResolver();

		Uri uri = Uri.parse("content://com.changhong.tvos.dtv.basechannelprovider/channel");
		  Cursor cursor = contentResolver.query(uri, new String[] { "name","[index]", "type", "code" }, null, null, null);

		if (cursor==null || cursor.getCount()==0) {
			return -1;
		}

		BaseChannel mBaseChannel = null;
		while (cursor.moveToNext()) {
		mBaseChannel = new BaseChannel();
		mBaseChannel.setName(cursor.getString(0));
		mBaseChannel.setIndex(cursor.getInt(1));
		mBaseChannel.setType(cursor.getString(2));
		mBaseChannel.setCode(cursor.getString(3));

		mBaseChannelList.add(mBaseChannel);
		mBaseChannel = null;
		}
		cursor.close();
		for(ProgramInfo mprogram : Hprogram) {
			for(BaseChannel p : mBaseChannelList) {
					if(p.getCode().equals(mprogram.getChannelCode())) {
						return p.getIndex();
					}
			}
		}
		return -1;
	}
	
	protected int getBaseChannelsByHChannelname(String Hid) {
		List<BaseChannel> mBaseChannelList = new ArrayList<BaseChannel>();
		ContentResolver contentResolver = mContext.getContentResolver();
		
		Uri uri = Uri.parse("content://com.changhong.tvos.dtv.basechannelprovider/channel");
		Cursor cursor = contentResolver.query(uri, new String[] { "name","[index]", "type", "code" }, null, null, null);

		if (cursor==null || cursor.getCount()==0) {
			return -1;
		}

		BaseChannel mBaseChannel = null;
		while (cursor.moveToNext()) {
			mBaseChannel = new BaseChannel();
			mBaseChannel.setName(cursor.getString(0));
			mBaseChannel.setIndex(cursor.getInt(1));
			mBaseChannel.setType(cursor.getString(2));
			mBaseChannel.setCode(cursor.getString(3));
	
			mBaseChannelList.add(mBaseChannel);
			mBaseChannel = null;
		}
		cursor.close();
		for(BaseChannel p : mBaseChannelList ) {
			if(p.getCode().equals(Hid)) {
				return p.getIndex();
			}
		}
		return -1;
	}
	
	protected void changeChannel(int serviceId) {
	
		Log.d(TAG, "send boradcast to change program" + serviceId);
		/*Intent it = new Intent("com.changhong.tvos.Start_DTV");
		serviceId = serviceId|0x40000000;
		it.putExtra("ChannelNumber", serviceId);
		mContext.sendBroadcast(it);*/
		
		DtvChannelManager.getInstance().channelChangeByProgramServiceIndex(serviceId, false);
	}
}