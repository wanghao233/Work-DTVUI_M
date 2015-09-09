package com.changhong.tvos.dtv.epg.normal;

import java.util.ArrayList;
import java.util.List;
import com.changhong.tvos.dtv.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TypeListdapter extends ArrayListAdapter<ChannelType> {
	@SuppressWarnings("unused")
	private EpgListView mListView;
	private Boolean isFocuse = true;
	private int mSelectPoisition = 0;

	public TypeListdapter(Activity context, List<ChannelType> dtvList, EpgListView listView) {
		super(context);
		mContext = context;
		mListView = listView;
		mList = new ArrayList<ChannelType>();
		mList.addAll(dtvList);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			convertView = inflater.inflate(R.layout.epg_normal_type_list_item, null);
			holder = new ViewHolder();
			holder.mName = (TextView) convertView.findViewById(R.id.name);
			holder.mItemView = (View) convertView.findViewById(R.id.item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mName.setText(mList.get(position).getName());

		if (isFocuse) {
			holder.mItemView.setBackgroundResource(R.drawable.epg_list_item_focuse);
			holder.mName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
			holder.mName.setAlpha(1f);
		} else if (!isFocuse) {
			holder.mItemView.setBackgroundResource(R.drawable.epg_list_item_select);
			holder.mName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			if (position == mSelectPoisition) {
				holder.mName.setAlpha(1f);
			} else {
				holder.mName.setAlpha((float) 0.4);
			}
		}

		return convertView;
	}

	public void changListState(boolean isfocuse, int poisiton, ArrayList<ChannelType> list) {//2015-3-24 YangLiu
		isFocuse = isfocuse;
		mSelectPoisition = poisiton;
		mList = list;
		notifyDataSetChanged();
	}

	public class ViewHolder {
		TextView mName;

		View mItemView;

		public ViewHolder() {
		}
	}
}
