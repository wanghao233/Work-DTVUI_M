package com.changhong.tvos.dtv.epg.normal;

import java.util.ArrayList;
import java.util.List;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.epg.normal.AsyncImageLoader.ImageCallback;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class LiveHotListdapter extends ArrayListAdapter<LiveHotWiki> {
	private EpgListView mListView;
	@SuppressWarnings("unused")
	private int mSelectPoisition = 0;
	private Boolean isFocuse = true;

	public LiveHotListdapter(Activity context, List<LiveHotWiki> dtvList, EpgListView listView) {
		super(context);
		mContext = context;
		mListView = listView;
		mList = new ArrayList<LiveHotWiki>();
		mList.addAll(dtvList);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();

			convertView = inflater.inflate(R.layout.epg_normal_hot_list_item, null);
			holder = new ViewHolder();
			holder.mHot = (TextView) convertView.findViewById(R.id.hot);
			holder.mName = (TextView) convertView.findViewById(R.id.name);
			holder.mContent = (TextView) convertView.findViewById(R.id.content);
			holder.mImage = (ImageView) convertView.findViewById(R.id.image);
			holder.mItemView = (View) convertView.findViewById(R.id.item);
			holder.mRText = (RotateTextView) convertView.findViewById(R.id.rtext);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mHot.setText(mContext.getResources().getString(R.string.hotrecommend_Percentage) + mList.get(position).getHot() + "%");
		holder.mName.setText(mList.get(position).getWikiTitle());
		holder.mContent.setText(mList.get(position).getWikiContent());
		int top = position + 1;
		holder.mRText.setText("TOP" + top);
		if (isFocuse) {
			holder.mItemView.setBackgroundResource(R.drawable.epg_list_item_focuse);
		} else {
			holder.mItemView.setBackgroundResource(R.drawable.epg_list_item_select);
		}

		String poster = (String) (mList.get(position).getWikiCover());
		//		Log.i("YangLiu", mList.get(position).getWikiTitle()+"的海报地址为："+poster);
		final ImageView imageView = holder.mImage;
		imageView.setTag(poster);

		Drawable cachedImage = AsyncImageLoader.getInstance().loadDrawable(poster, new ImageCallback() {
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				//						Log.i("YangLiu", "得到的海报地址为："+imageUrl+"图片为："+imageDrawable);

				ImageView imageViewByTag = (ImageView) mListView.findViewWithTag(imageUrl);
				//						Log.i("YangLiu", "准备显示的海报图片为："+imageViewByTag);
				if (imageViewByTag != null && imageDrawable != null) {//解决显示海报为空 2015-3-25
					imageViewByTag.setImageDrawable(imageDrawable);
				}
			}
		});
		if (cachedImage == null) {
			imageView.setImageResource(R.drawable.movie_default);
		} else {
			imageView.setImageDrawable(cachedImage);
		}

		return convertView;
	}

	public void changListState(boolean isfocuse, int poisiton) {
		isFocuse = isfocuse;
		mSelectPoisition = poisiton;
		notifyDataSetChanged();
	}

	public void changFocuse(boolean isfocuse) {
		isFocuse = isfocuse;
		notifyDataSetChanged();
	}

	public class ViewHolder {
		TextView mTime;
		TextView mName;
		View mItemView;
		TextView mContent;
		TextView mHot;
		ImageView mImage;
		RotateTextView mRText;

		public ViewHolder() {
		}
	}
}