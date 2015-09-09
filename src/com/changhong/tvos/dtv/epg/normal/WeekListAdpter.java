package com.changhong.tvos.dtv.epg.normal;

import java.util.ArrayList;
import java.util.List;
import com.changhong.tvos.dtv.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeekListAdpter extends ArrayListAdapter<Week> {
	private EpgListView mListView;
	private Boolean isFocuse = true;
	private int mSelectPoisition = 0;

	public WeekListAdpter(Activity context, List<Week> dtvList, EpgListView listView) {
		super(context);
		mContext = context;
		mListView = listView;
		mList = new ArrayList<Week>();
		mList.addAll(dtvList);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			convertView = inflater.inflate(R.layout.epg_normal_week_list_item, null);
			holder = new ViewHolder();
			holder.mWeek = (TextView) convertView.findViewById(R.id.week);
			holder.mItemView = (View) convertView.findViewById(R.id.item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mWeek.setText(mList.get(position).getWeek());

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
		TextView mWeek;

		View mItemView;

		public ViewHolder() {
		}
	}
}
