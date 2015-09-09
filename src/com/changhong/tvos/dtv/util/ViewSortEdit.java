package com.changhong.tvos.dtv.util;

import java.util.List;
import com.changhong.tvos.dtv.AsyncImageLoader;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.AsyncImageLoader.ImageCallback;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewSortEdit extends GridView implements android.widget.AdapterView.OnItemClickListener, android.widget.AdapterView.OnItemSelectedListener {
	private static final String TAG = "ViewSortEdit";
	private SortEditGridAdapter preViewAdapter;
	private List<DtvProgram> sortList;
	private Context mContext;
	private int[] location;
	private int curChoose;
	private int scrollHeight;
	private int curSelect;

	public interface OnDtvChooseCallBack {
		int ACTION_SHOW = 0;
		int ACTION_DISS = 1;

		public void onChooseCallBack(int[] itemLocation, DtvProgram program, int action);
	}

	public interface OnKeyCallBack {
		public void onBack();

		public void onkeyDown(int keyCode);
	}

	private OnDtvChooseCallBack dtvCallBack;
	private OnKeyCallBack keyCallBack;
	private boolean hasChooseOne;
	private DisplayMetrics mDisplayMetrics;

	public ViewSortEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		preViewAdapter = new SortEditGridAdapter();
		this.setAdapter(preViewAdapter);
		location = new int[2];
		this.setOnItemClickListener(this);
	}

	public void init(List<DtvProgram> list) {
		mDisplayMetrics = mContext.getResources().getDisplayMetrics();

		hasChooseOne = false;
		sortList = list;
		preViewAdapter.notifyDataSetChanged();
		setSelection(0);
		this.requestFocus();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//		int height = this.getHeight() / 2;
		Log.i(TAG, "onKeyDown:keyCode= " + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			keyCallBack.onBack();
			return true;
		case KeyEvent.KEYCODE_DPAD_UP:
			this.smoothScrollBy((int) (-(this.getHeight() / 2 + mDisplayMetrics.scaledDensity)), 300);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			this.smoothScrollBy((int) (this.getHeight() / 2 + mDisplayMetrics.scaledDensity), 300);
			break;
		default:
			break;
		}
		keyCallBack.onkeyDown(keyCode);
		return super.onKeyDown(keyCode, event);
	}

	private class SortEditGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null == sortList) {
				return 0;
			}
			return sortList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return sortList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup arg2) {
			// TODO Auto-generated method stub

			// TODO Auto-generated method stub
			Holder holder = null;
			if (contentView == null) {
				holder = new Holder();
				contentView = LayoutInflater.from(mContext).inflate(R.layout.view_program_sort_cell, null);
				contentView.setTag(holder);

				holder.tv_num = (TextView) contentView.findViewById(R.id.textView1);
				holder.tv_name = (TextView) contentView.findViewById(R.id.textView2);
				holder.icon = (ImageView) contentView.findViewById(R.id.imageView1);
				holder.container = (RelativeLayout) contentView.findViewById(R.id.relativeLayout2);
			} else {
				holder = (Holder) contentView.getTag();
			}

			DtvProgram tmpChannel;
			holder.container.setVisibility(View.VISIBLE);
			tmpChannel = sortList.get(position);
			holder.pro = tmpChannel;

			final ImageView channelLogImageView = holder.icon;
			Drawable drawable = AsyncImageLoader.getInstance().loadDrawable(("/data/dtv/logo/" + tmpChannel.mDtvLogo), new ImageCallback() {

				@Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					if (null == imageDrawable) {
					} else {
						channelLogImageView.setImageDrawable(imageDrawable);
					}
				}
			});
			if (null != drawable) {
				channelLogImageView.setImageDrawable(drawable);
			}

			holder.tv_num.setText("" + (position + 1));
			holder.tv_name.setText(tmpChannel.getProgramName());
			contentView.setVisibility(View.VISIBLE);
			return contentView;
		}
	}

	private class Holder {
		public DtvProgram pro;
		public ImageView icon;
		public TextView tv_num;
		public TextView tv_name;
		public RelativeLayout container;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View contentView, int postion, long arg3) {
		// TODO Auto-generated method stub
		Holder mHolder = (Holder) contentView.getTag();
		if (hasChooseOne) {
			hasChooseOne = false;
			contentView.getLocationInWindow(location);
			if (curChoose == postion) {
				contentView.setVisibility(View.VISIBLE);
				mHolder.container.setVisibility(View.VISIBLE);
				curChoose = postion;
			} else {
				insertProgramByIndex(postion);
				curChoose = postion;
				preViewAdapter.notifyDataSetChanged();
			}
			dtvCallBack.onChooseCallBack(location, mHolder.pro, dtvCallBack.ACTION_DISS);
		} else {
			hasChooseOne = true;
			contentView.setVisibility(View.INVISIBLE);
			mHolder.container.setVisibility(View.INVISIBLE);
			contentView.getLocationInWindow(location);
			curChoose = postion;
			dtvCallBack.onChooseCallBack(location, mHolder.pro, dtvCallBack.ACTION_SHOW);
		}
	}

	private void insertProgramByIndex(int postion) {
		// TODO Auto-generated method stub
		DtvProgram pro = sortList.get(curChoose);
		sortList.remove(pro);
		sortList.add(postion, pro);
	}

	public void setDtvCallBack(OnDtvChooseCallBack dtvCallBack) {
		this.dtvCallBack = dtvCallBack;
	}

	public OnDtvChooseCallBack getDtvCallBack() {
		return dtvCallBack;
	}

	public void setKeyCallBack(OnKeyCallBack keyCallBack) {
		this.keyCallBack = keyCallBack;
	}

	public OnKeyCallBack getKeyCallBack() {
		return keyCallBack;
	}

	public void setScrollHeight(int scrollHeight) {
		this.scrollHeight = scrollHeight;
	}

	public int getScrollHeight() {
		return scrollHeight;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		curSelect = arg2;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}
}