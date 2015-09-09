package com.changhong.tvos.dtv.channel_list;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;

public class ChannelList extends ListView {

	private static final String TAG = "ChannelList";

	public interface KeyActionInterface {
		public void onkeyPressedAction(int keyCode, KeyEvent event);

		public void changeChannelType(boolean isChange);

		public void showChannelInfo();

		public void refreshShowTime();
	}

	private KeyActionInterface keyActionInterface;
	public ChanelListAdapter mAdapter;
	private MenuManager manager;
	private DtvProgram curChooseDtvProgram;

	private boolean isChangeState;
	private int mCurSelectPos = 0;
	private int mCurPage;
	private int maxPage;
	private int lastPage;
	private final int delayTime = 600;
	private final int channelNumEveryPage = 6;
	private final int SHOW_CHANNALE_CHANGE = 5;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == SHOW_CHANNALE_CHANGE) {
				keyActionInterface.showChannelInfo();
			}
		}

	};
	private Runnable changeChannelRunnable = new Runnable() {
		public void run() {

			manager.changeChannelByProgramServiceIndex(curChooseDtvProgram.getProgramServiceIndex());
			handler.sendEmptyMessage(SHOW_CHANNALE_CHANGE);
		}
	};

	public ChannelList(Context context) {
		super(context);
		Drawable drawable = context.getResources().getDrawable(R.drawable.setting_picture_divider);
		setDivider(drawable);
		if (mAdapter == null) {
			mAdapter = new ChanelListAdapter(context);
			mAdapter.setChannelNumEveryPage(channelNumEveryPage);
			setAdapter(mAdapter);
			setOnItemSelectedListener(new ListItemOnSelectedListener());
			setOnItemClickListener(new ListItemClickListener());
		}
		isChangeState = false;
	}

	public ChannelList(Context context, AttributeSet attrs) {
		super(context, attrs);
		Drawable drawable = context.getResources().getDrawable(R.drawable.setting_picture_divider);
		setDivider(drawable);
		if (mAdapter == null) {
			mAdapter = new ChanelListAdapter(context);
			mAdapter.setChannelNumEveryPage(channelNumEveryPage);
			setAdapter(mAdapter);
			setOnItemSelectedListener(new ListItemOnSelectedListener());
			setOnItemClickListener(new ListItemClickListener());
		}
		isChangeState = false;
	}

	public ChannelList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Drawable drawable = context.getResources().getDrawable(R.drawable.setting_picture_divider);
		setDivider(drawable);
		if (mAdapter == null) {
			mAdapter = new ChanelListAdapter(context);
			mAdapter.setChannelNumEveryPage(channelNumEveryPage);
			setAdapter(mAdapter);
			setOnItemSelectedListener(new ListItemOnSelectedListener());
			setOnItemClickListener(new ListItemClickListener());
		}
		isChangeState = false;
	}

	public void initChannelList(MenuManager manager) {
		if (null == manager || null == manager.getCurList() || manager.getCurList().size() == 0) {
			return;
		}
		this.manager = manager;

		maxPage = (manager.getCurList().size() - 1) / channelNumEveryPage;
		if (-1 != manager.getCurProgramListIndex()) {
			mCurPage = manager.getCurProgramListIndex() / channelNumEveryPage;
		} else {
			mCurPage = 0;
		}

		Log.v(TAG, "EL-->the cur program infdex  is" + manager.getCurProgramListIndex());
		Log.v(TAG, "EL-->the curPage is + " + mCurPage + " and the maxpage is " + maxPage);
		lastPage = mCurPage;
		mAdapter.initManager(manager);
		mAdapter.setCurPage(mCurPage);
		mAdapter.setMaxPage(maxPage);
		mAdapter.notifyDataSetChanged();

		this.requestFocus();

		if (-1 != manager.getCurProgramListIndex()) {
			mCurSelectPos = manager.getCurProgramListIndex() % channelNumEveryPage;
			setSelection(mCurSelectPos);
		} else {
			setSelection(0);
			changeToCurChooseItemDtv(0);
			mCurSelectPos = 0;
		}

	}

	public void setIsChangeState(boolean isChange) {
		isChangeState = isChange;
	}

	public void pageNext() {
		if (mCurPage < maxPage) {
			mCurPage++;
		} else {
			mCurPage = 0;

		}
		if (lastPage != mCurPage) {
			lastPage = mCurPage;
			mAdapter.setCurPage(mCurPage);
			mAdapter.notifyDataSetChanged();
		}
		this.requestFocus();
		setSelection(mCurSelectPos);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		Log.v(TAG, "LL ViewChannelList>>onKeyDown KeyCode" + keyCode + "event" + event);

		keyActionInterface.refreshShowTime();
		mCurSelectPos = getSelectedItemPosition();

		handler.removeCallbacks(changeChannelRunnable);
		Log.v(TAG, "current selected position is " + mCurSelectPos);
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			if (0 == mCurSelectPos) {
				if (mCurPage > 0) {
					mCurPage--;
				} else {
					//						mCurPage = maxPage;

					keyActionInterface.changeChannelType(true);
					isChangeState = true;
					Log.v(TAG, "current selected position is " + mCurSelectPos + "\n mAdapter.getCount is " + mAdapter.getCount() + "isChangeState " + isChangeState);
					return true;
				}
				if (!isChangeState) {
					if (lastPage != mCurPage) {
						lastPage = mCurPage;
						mAdapter.setCurPage(mCurPage);
						mAdapter.notifyDataSetChanged();
					}

					setSelection(mAdapter.getCount() - 1);
					Log.v(TAG, "mAdapter.getCount is " + mAdapter.getCount());

					return true;
				}

			}
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:

			Log.v(TAG, "current selected position is " + mCurSelectPos + "\n mAdapter.getCount is " + mAdapter.getCount() + "isChangeState " + isChangeState);
			if (isChangeState) {
				isChangeState = false;
				keyActionInterface.changeChannelType(false);
				changeToCurChooseItemDtv(mCurSelectPos);
				return true;
			} else {
				if ((mAdapter.getCount() - 1) == mCurSelectPos) {
					if (mCurPage < maxPage) {
						mCurPage++;
					} else {
						mCurPage = 0;

					}
					if (lastPage != mCurPage) {
						lastPage = mCurPage;
						mAdapter.setCurPage(mCurPage);
						mAdapter.notifyDataSetChanged();
					}
					setSelection(0);
					return true;
				}

			}

			break;

		case KeyEvent.KEYCODE_GREEN:
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL:

			if (isChangeState) {
				keyActionInterface.onkeyPressedAction(KeyEvent.KEYCODE_DPAD_LEFT, event);
			} else {
				if (mCurPage > 0) {
					mCurPage--;
				} else {
					mCurPage = maxPage;

				}
				if (lastPage != mCurPage) {
					lastPage = mCurPage;
					mAdapter.setCurPage(mCurPage);
					mAdapter.notifyDataSetChanged();
				}

				if (mCurPage != maxPage) {
					setSelection(mCurSelectPos);
					changeToCurChooseItemDtv(mCurSelectPos);
				} else if (mCurSelectPos < mAdapter.getCount()) {
					setSelection(mCurSelectPos);
					changeToCurChooseItemDtv(mCurSelectPos);
				} else {
					setSelection(mAdapter.getCount() - 1);
					changeToCurChooseItemDtv(mAdapter.getCount() - 1);
				}
			}

			return true;

		case KeyEvent.KEYCODE_YELLOW:
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR:
			if (isChangeState) {
				keyActionInterface.onkeyPressedAction(KeyEvent.KEYCODE_DPAD_RIGHT, event);
			} else {
				if (mCurPage < maxPage) {
					mCurPage++;
				} else {
					mCurPage = 0;

				}
				if (lastPage != mCurPage) {
					lastPage = mCurPage;
					mAdapter.setCurPage(mCurPage);
					mAdapter.notifyDataSetChanged();
				}
				if (mCurPage != maxPage) {
					setSelection(mCurSelectPos);
					changeToCurChooseItemDtv(mCurSelectPos);
				} else if (mCurSelectPos < mAdapter.getCount()) {
					setSelection(mCurSelectPos);
					changeToCurChooseItemDtv(mCurSelectPos);
				} else {
					setSelection(mAdapter.getCount() - 1);
					changeToCurChooseItemDtv(mAdapter.getCount() - 1);
				}
			}

			return true;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			keyActionInterface.onkeyPressedAction(KeyEvent.KEYCODE_DPAD_LEFT, event);
			return true;

		case KeyEvent.KEYCODE_DPAD_RIGHT:
			keyActionInterface.onkeyPressedAction(KeyEvent.KEYCODE_DPAD_RIGHT, event);
			return true;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if (isChangeState) {
				keyActionInterface.onkeyPressedAction(KeyEvent.KEYCODE_DPAD_RIGHT, event);
				return true;
			}
			break;
		default:
			break;
		}

		if (lastPage != mCurPage) {
			lastPage = mCurPage;
			mAdapter.setCurPage(mCurPage);
			mAdapter.notifyDataSetChanged();
		}

		return super.onKeyDown(keyCode, event);

	}

	public void setKeyActionInterface(KeyActionInterface keyActionInterface) {
		this.keyActionInterface = keyActionInterface;
	}

	public KeyActionInterface getKeyActionInterface() {
		return keyActionInterface;
	}

	public void changeToCurChooseItemDtv(int position) {

		if (-1 == position) {
			position = mCurSelectPos;
		}
		curChooseDtvProgram = manager.getCurList().get(mCurPage * channelNumEveryPage + position);
		if (curChooseDtvProgram.getProgramServiceIndex() != manager.getCurProgram().getProgramServiceIndex()) {
			handler.removeCallbacks(changeChannelRunnable);
			handler.postDelayed(changeChannelRunnable, delayTime);
		}
	}

	class ListItemOnSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View convertView, int position, long arg3) {
			Log.v(TAG, "the selected item " + position);
			convertView.setSelected(true);
			changeToCurChooseItemDtv(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	class ListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View convertView, int position, long arg3) {

			keyActionInterface.refreshShowTime();
			convertView.setSelected(true);
			changeToCurChooseItemDtv(position);
		}
	}

	class ChanelListAdapter extends BaseAdapter {

		private Context mContext;
		private int curPage;
		private int maxPage;
		private int channelNumEveryPage;
		private MenuManager manager;

		public ChanelListAdapter(Context context) {
			mContext = context;
		}

		public int getCount() {
			if (null == manager || null == manager.getCurList()) {
				return 0;
			} else if (manager.getCurList().size() < channelNumEveryPage) {
				return manager.getCurList().size();
			} else if (curPage == maxPage) {
				return manager.getCurList().size() - curPage * channelNumEveryPage;
			} else {
				return channelNumEveryPage;
			}

		}

		public Object getItem(int arg0) {
			return arg0;
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int arg0, View convertView, ViewGroup arg2) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(mContext).inflate(com.changhong.tvos.dtv.R.layout.cell_channel_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.logicNumber = (TextView) convertView.findViewById(com.changhong.tvos.dtv.R.id.logic_num);

				viewHolder.channel_name = (TextView) convertView.findViewById(com.changhong.tvos.dtv.R.id.channel_name);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (manager != null && null != manager.getCurList()) {
				DtvProgram dtv = manager.getCurList().get(curPage * getChannelNumEveryPage() + arg0);
				viewHolder.dtv = dtv;
				viewHolder.logicNumber.setText(String.valueOf(dtv.getProgramNum()));
				viewHolder.channel_name.setText(dtv.getProgramName());
			}

			return convertView;
		}

		public void setCurPage(int curPage) {
			this.curPage = curPage;
		}

		public int getCurPage() {
			return curPage;
		}

		public void setMaxPage(int maxPage) {
			this.maxPage = maxPage;
		}

		public int getMaxPage() {
			return maxPage;
		}

		public void setChannelNumEveryPage(int channelNumEveryPage) {
			this.channelNumEveryPage = channelNumEveryPage;
		}

		public int getChannelNumEveryPage() {
			return this.channelNumEveryPage;
		}

		public void initManager(MenuManager manager) {
			this.manager = manager;
		}

	}

	class ViewHolder {
		private TextView logicNumber = null;
		private TextView channel_name = null;
		DtvProgram dtv;
	}

}
