package com.changhong.tvos.dtv.epg.normal;

import java.util.ArrayList;
import com.changhong.tvos.dtv.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class WeekListFragment extends Fragment {
	private EpgListView mListView;
	private Context mContext;
	private WeekListAdpter mAAdapter;
	ArrayList<Week> mWeekList = null;

	WeekListSelectedListener mWeekListSelected;

	public interface WeekListSelectedListener {
		public void onWeekListSelected(int data);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.epg_normal_week_list_layout, container, false);
		mListView = (EpgListView) view.findViewById(R.id.weeklist);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		try {
			mWeekListSelected = (WeekListSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {//得到周信息列表
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mContext = EpgActivity.getEpgActivity();//change by YangLiu 2015-2-12

		mWeekList = getData(EpgActivity.todayOfWeek);
		Log.i("WeekListFragment", "todayOfWeek 000 = " + EpgActivity.todayOfWeek);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mAAdapter = new WeekListAdpter(getActivity(), mWeekList, mListView);
		mListView.setAdapter(mAAdapter);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
			}
		});

		/*
		 * 根据选中的周信息条目取得对应的节目信息
		 */
		mListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				mWeekListSelected.onWeekListSelected(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		/*
		 * 根据周信息列表是否获取焦点，改变节目列表
		 */
		mListView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				WeekListAdpter mAAdapter = (WeekListAdpter) mListView.getAdapter();
				mAAdapter.changListState(arg1, mListView.getSelectedItemPosition());
			}
		});

		//保存视图位置为起始项
		if (mWeekList != null && mWeekList.size() >= 2) {
			setListSelectPoisiton(0);
		}
	}

	public void setListSelectPoisiton(int p) {
		mListView.setSelection(p);
	}

	private ArrayList<Week> getData(int week) {

		ArrayList<Week> mList = new ArrayList<Week>();

		Week mon = new Week();
		//mon.setWeek("周一");
		mon.setWeek(mContext.getString(R.string.menu_EPG_week_list_Monday));
		mList.add(0, mon);
		Week tue = new Week();
		//tue.setWeek("周二");
		tue.setWeek(mContext.getString(R.string.menu_EPG_week_list_Tuesday));
		mList.add(1, tue);
		Week wed = new Week();
		//wed.setWeek("周三");
		wed.setWeek(mContext.getString(R.string.menu_EPG_week_list_Wednesday));

		mList.add(2, wed);
		Week thu = new Week();
		//thu.setWeek("周四");
		thu.setWeek(mContext.getString(R.string.menu_EPG_week_list_Thursday));

		mList.add(3, thu);
		Week fri = new Week();
		//fri.setWeek("周五");
		fri.setWeek(mContext.getString(R.string.menu_EPG_week_list_Friday));

		mList.add(4, fri);
		Week sat = new Week();
		//sat.setWeek("周六");
		sat.setWeek(mContext.getString(R.string.menu_EPG_week_list_Saturday));

		mList.add(5, sat);
		Week sun = new Week();
		//sun.setWeek("周日");
		sun.setWeek(mContext.getString(R.string.menu_EPG_week_list_Sunday));
		mList.add(6, sun);

		for (int i = 0; i < week - 1; i++) {
			/**
			 * 如果需要显示所有星期数据就取消下面一行代码的注释
			 * YangLiu
			 */
			//			mList.add(mList.get(0));   
			mList.remove(0);
		}
		return mList;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {//隐藏时失去焦点
		// TODO Auto-generated method stub
		if (!hidden) {
			mListView.setFocusable(true);
			mListView.requestFocus();
		}
		super.onHiddenChanged(hidden);
	}
}