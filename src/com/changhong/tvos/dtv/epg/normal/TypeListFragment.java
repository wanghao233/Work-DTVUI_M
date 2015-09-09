package com.changhong.tvos.dtv.epg.normal;

import java.util.ArrayList;
import com.changhong.tvos.dtv.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class TypeListFragment extends Fragment {

	private EpgListView mListView;
	private Context mContext;
	private TextView mTitleView;
	private TypeListdapter mTypedapter;
	private ArrayList<ChannelType> mList;
	TypeListSelectedListener mTypeListSelected;

	//	private boolean isHasHDChannels = false;

	public interface TypeListSelectedListener {
		public void onTypeListSelected(ChannelType data);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("onCreateView", "onCreateView");
		View view = inflater.inflate(R.layout.epg_normal_type_list_layout, container, false);
		mListView = (EpgListView) view.findViewById(R.id.typelist);
		mTitleView = (TextView) view.findViewById(R.id.title);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mTypeListSelected = (TypeListSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("onCreate", "onCreate");

		mContext = EpgActivity.getEpgActivity();//change by YangLiu 2015-2-12
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mList = getData();
		display();
	}

	/*public void setIsHasHD(boolean flag){//会在EPG第一次创建时执行回调 2015-3-19
		isHasHDChannels = flag;
		mList = getData();
		display();
	}*/

	private void display() {
		mTypedapter = new TypeListdapter(getActivity(), mList, mListView);
		mListView.setAdapter(mTypedapter);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

			}
		});

		mListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

				mTypeListSelected.onTypeListSelected(mList.get(arg2));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		mListView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				Log.i("view", "" + hasFocus);

				TypeListdapter mAdapter = (TypeListdapter) mListView.getAdapter();
				mList = getData();//动态更新分类的类型  2015-3-24  YangLiu
				mAdapter.changListState(hasFocus, mListView.getSelectedItemPosition(), mList);

				Log.i("D", mListView.getSelectedItemPosition() + "");

				if (hasFocus) {
					/*RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.MATCH_PARENT);
					
					layout.setMargins(dip2px(mContext, 70),
							dip2px(mContext, 30), 0, 0);
					mTitleView.setLayoutParams(layout);*/

					mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
					mTitleView.setAlpha(1);

				} else {
					/*RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					layout.setMargins(dip2px(mContext, 82),
							dip2px(mContext, 60), 0, 0);
					mTitleView.setLayoutParams(layout);*/

					mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
					mTitleView.setAlpha((float) 0.4);
				}
			}
		});

		Log.i("S", mListView.getSelectedItemPosition() + "");
	}

	private ArrayList<ChannelType> getData() {
		ArrayList<ChannelType> mList = new ArrayList<ChannelType>();

		ChannelType mChannelType1 = new ChannelType();
		mChannelType1.setName(mContext.getString(R.string.menu_channel_list_Current_Popular));
		mChannelType1.setType("hot");
		mList.add(mChannelType1);
		ChannelType mChannelType2 = new ChannelType();
		mChannelType2.setName(mContext.getString(R.string.menu_channel_list_Common_Channel));
		mChannelType2.setType("common");
		mList.add(mChannelType2);
		Log.i("YL", "isHasHDChannels=" + ChannelListFragment.hdflag);
		if (ChannelListFragment.hdflag) {
			ChannelType mChannelType3 = new ChannelType();
			mChannelType3.setName(mContext.getString(R.string.menu_channel_list_HD_Channel));
			mChannelType3.setType("hd");
			mList.add(mChannelType3);
		}
		ChannelType mChannelType4 = new ChannelType();
		mChannelType4.setName(mContext.getString(R.string.menu_channel_list_CCTV_Channel));
		mChannelType4.setType("cctv");
		mList.add(mChannelType4);
		ChannelType mChannelType5 = new ChannelType();
		mChannelType5.setName(mContext.getString(R.string.menu_channel_list_Satellite_Channels));
		mChannelType5.setType("tv");
		mList.add(mChannelType5);
		ChannelType mChannelType6 = new ChannelType();
		mChannelType6.setName(mContext.getString(R.string.menu_channel_list_Other_Channels));
		mChannelType6.setType("other");
		mList.add(mChannelType6);

		return mList;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		mListView.setFocusable(true);
		mListView.requestFocus();
		Log.i("onResume", "onResume");
		super.onResume();
	}

	public void clearFocuse() {
		mListView.clearFocus();
	}

	public int dip2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}