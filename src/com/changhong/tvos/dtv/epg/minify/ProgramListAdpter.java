package com.changhong.tvos.dtv.epg.minify;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvEvent;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.DtvScheduleEvent;
import android.app.Activity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgramListAdpter extends ArrayListAdapter<DtvEvent> {
	private EpgListView mListView;
	private Boolean isFocuse = true;
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		//		try {//changed by yangliu 2014-12-29 空指针异常
		if (convertView == null) {
			if (mContext != null) {
				LayoutInflater inflater = mContext.getLayoutInflater();
				convertView = inflater.inflate(R.layout.epg_minify_program_list_item, null);
				holder = new ViewHolder();
				holder.mTime = (TextView) convertView.findViewById(R.id.time);
				holder.mName = (TextView) convertView.findViewById(R.id.name);
				holder.mState = (ImageView) convertView.findViewById(R.id.state);
				holder.mItemView = (View) convertView.findViewById(R.id.item);
				convertView.setTag(holder);
			}

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//		} catch (Exception e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}

		if (holder != null) {//2015-1-6 空指针异常
			holder.mTime.setText(setNumForm(mList.get(position).getStartTime().getHours()) + ":" + setNumForm(mList.get(position).getStartTime().getMinutes()));
			holder.mName.setText(mList.get(position).getTitle());

			/**
			 * 2015-1-1 从预约列表一直按右键，到节目列表上丢焦点问题
			 */
			isFocuse = ProgramListFragment.isProgramListFragmentHasFocus;//add by YangLiu
			if (isFocuse) {
				holder.mItemView.setBackgroundResource(R.drawable.epg_list_item_focuse);
				holder.mTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				holder.mTime.setAlpha(1f);

				holder.mName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				holder.mName.setAlpha(1f);
			} else if (!isFocuse) {
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
			} else
			//} 
			if (isOrder(mEvent))

			//	holder.mState.setText("已经预约");
			//  if(MenuManager.getInstance().isOrderEvent(mEvent))
			{
				holder.mState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.timer));
			} else {
				//holder.mState.setText("");
				holder.mState.setImageDrawable(null);
			}
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

	private boolean isCanOrder(DtvEvent mEvent) {
		//		Date curDate = new Date(System.currentTimeMillis());
		Date curDate = MenuManager.getInstance().getCurrentDate();
		if (mEvent.getStartTime().after(curDate)) {
			return true;
		} else {
			return false;
		}

	}

	@SuppressWarnings({ "unused", "deprecation" })
	private boolean isOrder(DtvEvent mEvent) {

		//	menuManager.isOrderEvent()
		boolean isHave = false;

		//	return MenuManager.getInstance().isOrderEvent(mEvent)
		if (true) {
			//isHave = MenuManager.getInstance().isOrderEvent(mEvent);
			isHave = ProgramListFragment.mscheduleManager.isOrderEvent(mEvent, mDtvProgram);
		} else {
			List<DtvScheduleEvent> mOrderList = MenuManager.getInstance().getScheduleEvents();

			if (mOrderList != null && mOrderList.size() > 0) {
				int num = mOrderList.size();

				for (int i = 0; i < num; i++) {
					if (mOrderList.get(i).getProgramServiceIndex() == mDtvProgram.getProgramServiceIndex() && mOrderList.get(i).getStartTime().getDate() == mEvent.getStartTime().getDate()
					///////////////////////////////fengy 2014-10-10 ///////////////////								
							&& mOrderList.get(i).getEndTime().getDate() == mEvent.getEndTime().getDate() && mOrderList.get(i).getTitle().equals(mEvent.getTitle())) {
						isHave = true;
						break;
					}
				}

				//			if (isHave)
				//				return true;
				//			else
				//				return false;
			} else {
				return false;
			}
		}
		Log.i("isOrder", "isHave" + isHave);
		return isHave;

	}

	private boolean isPlaying(DtvEvent mEvent) {

		//		Date curDate = new Date(System.currentTimeMillis());
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