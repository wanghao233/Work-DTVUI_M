package com.changhong.tvos.dtv.epg.normal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import com.changhong.tvos.dtv.AsyncImageLoader;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.AsyncImageLoader.ImageCallback;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.DtvScheduleEvent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderListAdpter extends ArrayListAdapter<DtvScheduleEvent> {
	private EpgListView mListView;
	private Boolean isFocuse = true;
	@SuppressWarnings("unused")
	private int mSelectPoisition = 0;
	private List<DtvProgram> mChannelList;

	public OrderListAdpter(Activity context, List<DtvScheduleEvent> dtvList, EpgListView listView) {
		super(context);
		mContext = context;
		mListView = listView;
		////////////fengy 2014-9-30 ////////////////////////////////
		mChannelList = MenuManager.getInstance().getWatchedChannelList();
		mList = new ArrayList<DtvScheduleEvent>();
		mList.addAll(dtvList);
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			convertView = inflater.inflate(R.layout.epg_normal_order_list_item, null);
			holder = new ViewHolder();
			holder.mIcon = (ImageView) convertView.findViewById(R.id.icon);
			holder.mChname = (TextView) convertView.findViewById(R.id.chname);
			holder.mTimer = (TextView) convertView.findViewById(R.id.timer);
			holder.mTitle = (TextView) convertView.findViewById(R.id.title);
			holder.mItemView = (View) convertView.findViewById(R.id.item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mTitle.setText(mList.get(position).getTitle());
		holder.mTimer.setText(setNumForm(mList.get(position).getStartTime().getHours()) + ":" + setNumForm(mList.get(position).getStartTime().getMinutes()));

		holder.mChname.setText(mList.get(position).getProgramName());

		final ImageView channelLogImageView = holder.mIcon;

		channelLogImageView.setImageDrawable(null);

		////////////////////////////////////////
		String mDtvLogo = "";

		String logo = "";

		/*		int num = mList.get(position).getProgramNum();
				for (int i = 0; i < mChannelList.size(); i++) {
					if (num == mChannelList.get(i).getProgramNum()) {
						logo = mChannelList.get(i).mDtvLogo;
						logo = mChannelList.get(i).mDtvLogo;
						break;
					}
				}*/

		int index = mList.get(position).getProgramServiceIndex();
		for (int i = 0; i < mChannelList.size(); i++) {
			if (index == mChannelList.get(i).getProgramServiceIndex()) {
				logo = mChannelList.get(i).mDtvLogo;
				break;
			}
		}/////////////////////////////////////////////////By YangLiu

		Drawable drawable = AsyncImageLoader.getInstance().loadDrawable(("/data/dtv/logo/" + logo), new ImageCallback() {

			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {

				ImageView imageViewByTag = (ImageView) mListView.findViewWithTag(imageUrl);
				if (null == imageDrawable) {
					Log.i("AsyncImageLoader", "EL--> pang imageDrawable is null line 98 = ");
				} else {
					if (null != imageViewByTag) {
						Log.i("AsyncImageLoader", "EL--> pang imageUrl = " + imageUrl);
						Log.i("AsyncImageLoader", "EL--> pang imageDrawable = " + imageDrawable);
						imageViewByTag.setImageDrawable(imageDrawable);
					}
				}
			}
		});
		if (null != drawable) {
			holder.mIcon.setImageDrawable(drawable);
		} else {
			Log.i("AsyncImageLoader", "EL--> pang null == drawable =null ");
			//holder.mIcon.setImageResource(R.drawable.app_menu_confirm_icon);

			//holder.mIcon.setImageDrawable(drawable);/**pang***/
		}
		if (isFocuse) {
			holder.mItemView.setBackgroundResource(R.drawable.epg_list_item_focuse);

		} else if (!isFocuse) {
			holder.mItemView.setBackgroundResource(R.drawable.epg_list_item_select);
		}

		return convertView;
	}

	public void changListState(boolean isfocuse, int poisiton) {
		isFocuse = isfocuse;
		mSelectPoisition = poisiton;
		notifyDataSetChanged();
	}

	public class ViewHolder {
		TextView mTitle;
		TextView mTimer;
		TextView mChname;
		ImageView mIcon;
		View mItemView;

		public ViewHolder() {
		}
	}

	private String setNumForm(int num) {
		String formNum = "";
		DecimalFormat df = new DecimalFormat("00");
		formNum = df.format(num);
		return formNum;
	}
}