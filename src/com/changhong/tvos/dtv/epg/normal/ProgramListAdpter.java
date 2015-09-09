package com.changhong.tvos.dtv.epg.normal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvScheduleManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvEvent;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;

public class ProgramListAdpter extends ArrayListAdapter<DtvEvent> {
	// private EpgListView mListView;
	private boolean isFocuse = true;
	private int mSelectPoisition = 0;
	private DtvProgram mDtvProgram;

	public ProgramListAdpter(Activity context, List<DtvEvent> dtvList, EpgListView listView, boolean isfocuse, DtvProgram mChannel) {
		super(context);
		mContext = context;
		mListView = listView;
		mDtvProgram = mChannel;
		isFocuse = isfocuse;
		mList = new ArrayList<DtvEvent>();
		mList.addAll(dtvList);
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		try {// changed by YangLiu 2015-01-14
			if (convertView == null) {
				LayoutInflater inflater = mContext.getLayoutInflater();
				convertView = inflater.inflate(R.layout.epg_normal_program_list_item, null);
				holder = new ViewHolder();
				holder.mTime = (TextView) convertView.findViewById(R.id.time);
				holder.mName = (TextView) convertView.findViewById(R.id.name);
				holder.mState = (ImageView) convertView.findViewById(R.id.state);
				holder.mItemView = (View) convertView.findViewById(R.id.item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.mTime.setText(setNumForm(mList.get(position).getStartTime().getHours()) + ":" + setNumForm(mList.get(position).getStartTime().getMinutes()));
			holder.mName.setText(mList.get(position).getTitle());
			/**
			 * 2015-1-1 从预约列表一直按右键，到节目列表上丢焦点问题
			 */
			isFocuse = ProgramListFragment.isProgramListFragmentHasFocus;// added by YangLiu 2014-12-15
			if (isFocuse) {
				holder.mItemView.setBackgroundResource(R.drawable.epg_list_item_focuse);
				holder.mTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				holder.mTime.setAlpha(1f);

				holder.mName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				holder.mName.setAlpha(1f);
			} else {
				holder.mItemView.setBackgroundResource(R.drawable.epg_list_item_select);
				holder.mTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
				holder.mName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

				if (position == mSelectPoisition) {
					holder.mTime.setAlpha(1f);
					holder.mName.setAlpha(1f);
				} else {
					holder.mTime.setAlpha((float) 0.4);
					holder.mName.setAlpha((float) 0.4);
				}
			}
			DtvEvent mEvent = mList.get(position);

			if (isPlaying(mEvent)) {
				holder.mState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.playing));
			} else if (isOrder(mEvent)) {
				holder.mState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.timer));
			} else {
				holder.mState.setImageDrawable(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	public void changListState(boolean isfocuse, int poisiton) {
		isFocuse = isfocuse;
		mSelectPoisition = poisiton;
		notifyDataSetChanged();
	}

	public class ViewHolder {
		TextView mTime;
		TextView mName;
		ImageView mState;
		View mItemView;

		public ViewHolder() {
		}
	}

	@SuppressWarnings("unused")
	private boolean isCanOrder(DtvEvent mEvent) {
		// Date curDate = new Date(System.currentTimeMillis());
		Date curDate = MenuManager.getInstance().getCurrentDate();
		if (mEvent.getStartTime().after(curDate)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isOrder(DtvEvent mEvent) {
		boolean isHave = false;
		// isHave =DtvScheduleManager.getInstance().isOrderEvent(mEvent,mDtvProgram);
		isHave = DtvScheduleManager.getInstance().isOrderLocalEvent(mEvent, mDtvProgram);
		return isHave;
	}

	private boolean isPlaying(DtvEvent mEvent) {
		// Date curDate = new Date(System.currentTimeMillis());
		Date curDate = MenuManager.getInstance().getCurrentDate();
		if (mEvent.getStartTime().before(curDate) && mEvent.getEndTime().after(curDate)) {
			return true;
		} else {
			return false;
		}
	}

	private String setNumForm(int num) {
		String formNum = "";
		DecimalFormat df = new DecimalFormat("00");
		formNum = df.format(num);

		return formNum;
	}
}