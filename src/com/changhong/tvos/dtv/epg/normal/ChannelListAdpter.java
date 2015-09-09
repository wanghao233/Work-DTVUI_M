package com.changhong.tvos.dtv.epg.normal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import com.changhong.tvos.dtv.AsyncImageLoader;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.AsyncImageLoader.ImageCallback;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class ChannelListAdpter extends ArrayListAdapter<DtvProgram> {
	private EpgListView mListView;
	private boolean isFocuse = true;
	private int mSelectPoisition = 0;
	protected static final String TAG = "ChannelListAdpter";

	public ChannelListAdpter(Activity context, List<DtvProgram> dtvList, EpgListView listView) {
		super(context);
		mContext = context;
		mListView = listView;
		mList = new ArrayList<DtvProgram>();
		mList.addAll(dtvList);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// 利用ViewHolder来提高列表的重用率和效率
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			convertView = inflater.inflate(R.layout.epg_normal_channel_list_item, null);
			holder = new ViewHolder();
			holder.mTitle = (TextView) convertView.findViewById(R.id.title);
			holder.mNum = (TextView) convertView.findViewById(R.id.num);
			holder.mIcon = (ImageView) convertView.findViewById(R.id.icon);
			holder.mItemView = (View) convertView.findViewById(R.id.item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 设置频道标题和序号
		holder.mTitle.setText(mList.get(position).getProgramName());

		holder.mNum.setText(setNumForm(mList.get(position).getProgramNum()));

		// 设置频道图片的加载
		Drawable drawable = AsyncImageLoader.getInstance().loadDrawable(("/data/dtv/logo/" + mList.get(position).mDtvLogo), new ImageCallback() {

			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {

				ImageView imageViewByTag = (ImageView) mListView.findViewWithTag(imageUrl);
				if (null == imageDrawable) {

				} else {
					if (null != imageViewByTag) {

						imageViewByTag.setImageDrawable(imageDrawable);
					}
				}
			}
		});
		if (null != drawable) {
			holder.mIcon.setImageDrawable(drawable);
		}

		/**
		 * change By YangLiu 2014-12-17
		 * 解决当快速在频道列表中按上下键时，再按右键到节目列表，焦点回到节目列表，但是没有突出显示，反而显示在频道列表
		 */
		// 设置频道焦点变换的效果
		if (isFocuse && mListView.isFocusable()) {// 频道列表获得焦点字体大
			holder.mItemView.setBackgroundResource(R.drawable.epg_list_item_focuse);
			holder.mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			holder.mTitle.setAlpha(1f);
			holder.mNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			holder.mNum.setAlpha(1f);
			LayoutParams para = holder.mIcon.getLayoutParams();
			para.height = dip2px(mContext, 30);
			para.width = dip2px(mContext, 50);
			holder.mIcon.setLayoutParams(para);
			holder.mIcon.setAlpha(1f);
		} else if (!mListView.isFocusable() || !isFocuse) {// 频道列表获得焦点字体小
			holder.mItemView.setBackgroundResource(R.drawable.epg_list_item_select);
			holder.mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			holder.mNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			holder.mNum.setAlpha(1);

			if (position == mSelectPoisition) {//当前选中高亮
				holder.mTitle.setAlpha(1f);
				holder.mNum.setAlpha(1f);
				holder.mIcon.setAlpha(1f);
			} else { // 未选中暗色
				holder.mTitle.setAlpha((float) 0.4);
				holder.mNum.setAlpha((float) 0.4);
				holder.mIcon.setAlpha((float) 0.4);
			}

			LayoutParams para = holder.mIcon.getLayoutParams();
			para.height = dip2px(mContext, 23);
			para.width = dip2px(mContext, 30);
			holder.mIcon.setLayoutParams(para);
		}
		return convertView;
	}

	// 当当前位置改变时更新信息
	public void changListState(boolean isfocuse, int poisiton) {
		isFocuse = isfocuse;
		mSelectPoisition = poisiton;
		notifyDataSetChanged();
	}

	// 提高列表效率，利用Holder加载条目信息
	public class ViewHolder {
		TextView mTitle;
		TextView mNum;
		ImageView mIcon;
		View mItemView;

		public ViewHolder() {
		}
	}

	// 设置dip与px之间的转化关系
	public int dip2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	// 设置数字的格式
	private String setNumForm(int num) {
		String formNum = "";
		DecimalFormat df = new DecimalFormat("000");
		formNum = df.format(num);

		return formNum;
	}
}