package com.changhong.tvos.dtv.channel_manager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.changhong.tvos.dtv.AsyncImageLoader;
import com.changhong.tvos.dtv.AsyncImageLoader.ImageCallback;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.menuManager.MenuManager.listState;
import com.changhong.tvos.dtv.tvap.DtvInterface;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.util.ViewGridBase;

public class ViewChannelOnOffGrid extends ViewGridBase {

	private static final String TAG = "ChanenlOpenOffGrid";

	private Button tv_open_btn;
	private Button tv_off_btn;
	PopupWindow popupWindow;
	private int pop_margin = 492;
	private int pop_width = 176;
	private int pop_height = 115;
	private DtvInterface mInterface;

	public ViewChannelOnOffGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}

	@Override
	public void init() {
		mInterface = DtvInterface.getInstance();
		popupWindow = null;
		if (mAdapter == null) {
			mAdapter = new ChannelTurnAdaper(mContext);

			setAdapter(mAdapter);
			setOnItemClickListener(new ChannelItemOnClickListener());
			setOnItemSelectedListener(new ChannelItemSelectedListener());
		}

		DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();
		pop_height = (int) (pop_height * mDisplayMetrics.scaledDensity);
		pop_width = (int) (pop_width * mDisplayMetrics.scaledDensity);
		pop_margin = (int) (pop_margin * mDisplayMetrics.scaledDensity);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (!isAnimationOk) {
			Log.v(TAG, "EL channelListGridView>>onKeyDown>> still Animation not keyAction;");
			return true;
		}
		Log.v(TAG, "EL channelListGridView>>onKeyDown>>keyCode = " + keyCode);
		lastPage = curPage;
		int curKey = revertKeyCode(keyCode, event);
		Log.v(TAG, "LL onKeyDown>>keyCode = " + keyCode + "and revert keycode is + " + curKey);
		switch (curKey) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			//			case KeyEvent.KEYCODE_DPAD_UP:
			//			case KeyEvent.KEYCODE_DPAD_DOWN:

			jugePageOrNot(curKey);
		}
		keyActionCallBack.keyActionDown(curKey, event);

		if (lastPage != curPage) {
			keyActionCallBack.animationAction(curKey);
			return true;
		}

		if (curKey != keyCode) {
			Log.i(TAG, "EL--> scanKeyCode is " + event.getScanCode());
			super.onKeyDown(curKey, event);
			return true;
		}
		return super.onKeyDown(curKey, event);

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (!isAnimationOk) {
			return true;
		}
		keyActionCallBack.keyActionUp(keyCode, event);
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void init(MenuManager listmanager, listState state) {
		setAnimationOk(true);
		manager = listmanager;
		//		manager.init(state);

		this.curProgramIndexUpdate(manager.getCurProgramListIndex());
		mAdapter.init(manager);

		mAdapter.notifyDataSetChanged();
		setSelection(getCurPosition());

	}

	private class ChannelTurnAdaper extends SimpleGridAdapter {

		public ChannelTurnAdaper(Context myContext) {
			// TODO Auto-generated constructor stub
			mContext = myContext;

		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			RelativeLayout layout = null;
			ViewHolder viewHolder = new ViewHolder();

			Log.v("getView", "position =" + position);
			if (null == convertView) {

				layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.view_channel_open_off, null);
			} else {
				layout = (RelativeLayout) convertView;
			}
			final ImageView channelLog = (ImageView) layout.findViewById(R.id.tv_log);
			ImageView stateIcon = (ImageView) layout.findViewById(R.id.open_state_icon);
			RelativeLayout container = (RelativeLayout) layout.findViewById(R.id.board_back);
			viewHolder.container = container;

			TextView tv_name = (TextView) layout.findViewById(R.id.tv_name);
			TextView tv_numble = (TextView) layout.findViewById(R.id.tv_numble);
			layout.setTag(viewHolder);

			viewHolder.tmpChannel = manager.getCurList().get((position + curPage * (columns * linage)));

			if (viewHolder.tmpChannel.isSkip()) {
				stateIcon.setImageResource(R.drawable.tv_close);
			} else {
				stateIcon.setImageResource(R.drawable.tv_open);
			}

			//			if(viewHolder.tmpChannel.getProgramNum() < 10){
			//				tv_numble.setText("D0" + viewHolder.tmpChannel.getProgramNum());
			//			}else{
			tv_numble.setText("D" + viewHolder.tmpChannel.getProgramNum());
			//			}
			tv_numble.setTextColor(Color.WHITE);

			tv_name.setText(viewHolder.tmpChannel.getProgramName());
			tv_name.setTextColor(Color.WHITE);

			//			viewHolder.channelLog.setImageResource(R.drawable.dtv_channel_mark_cctv1);
			Log.i(TAG, "EL dtvLog = " + "/data/dtv/logo/" + viewHolder.tmpChannel.mDtvLogo);
			//			if(null != viewHolder.tmpChannel.mDtvLogo){
			//				viewHolder.channelLog.setImageURI(Uri.fromFile(new File("/data/dtv/logo/" + viewHolder.tmpChannel.mDtvLogo)));
			//				
			//			}
			Drawable drawable = AsyncImageLoader.getInstance().loadDrawable(("/data/dtv/logo/" + viewHolder.tmpChannel.mDtvLogo), new ImageCallback() {

				@Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					if (null == imageDrawable) {
						Log.i(TAG, "EL--> the image is null");
					} else {
						channelLog.setImageDrawable(imageDrawable);
					}
				}

			});
			if (null != drawable) {
				Log.i(TAG, "EL--> the image drawable is not null");
				channelLog.setImageDrawable(drawable);
			}
			if ((position + curPage * (columns * linage)) == manager.getprogramSwapSelect()) {
				Log.v(TAG, "getCurProgramIndex2 =" + manager.getCurProgramListIndex());
				layout.setBackgroundResource(R.drawable.channel_edit_quick_channel_item_select_new);
			}

			return layout;
		}

	}

	private class ViewHolder {

		RelativeLayout container;
		DtvProgram tmpChannel;
	}

	public class ChannelItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View convertView, int arg2, long arg3) {
			//			convertView.setScaleY(1.05f);
			//			convertView.setScaleX(1.05f);
			convertView.setSelected(true);
			curPosition = arg2;
			Log.i(TAG, "EL--> curSelect item is " + curPosition);
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			curChooseDtvProgram = viewHolder.tmpChannel;
			startChannelChange();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	public class ChannelItemOnClickListener implements android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View convertView, int postion, long id) {
			//			convertView.setScaleY(1.05f);
			//			convertView.setScaleX(1.05f);
			keyActionCallBack.resetTimer();
			curPosition = postion;

			if (null == popupWindow) {
				popupWindow = makePopupWindow(mContext);

			}
			int xy[] = new int[2];
			convertView.getLocationOnScreen(xy);

			xy[1] += pop_width - pop_height;

			Log.v(TAG, "EL window x " + xy[0] + "and y is " + xy[1]);

			final ViewHolder viewHolder = (ViewHolder) convertView.getTag();

			viewHolder.container.setBackgroundResource(R.drawable.channel_openoff_onclick_bg);

			//���������̨��ȥ�������ӳ����뻻̨

			curChooseDtvProgram = viewHolder.tmpChannel;
			channelChangeRightNow();

			Log.v(TAG, "EL cur selected item is " + postion + " and the tv is " + viewHolder.tmpChannel.getProgramName());
			popupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {

					viewHolder.container.setBackgroundResource(R.drawable.dtv_quick_channel_selector);
				}
			});
			Log.v(TAG, "EL---->> cur type is SKIP_TYPE");
			popupWindow.showAtLocation(ViewChannelOnOffGrid.this, Gravity.LEFT | Gravity.TOP, xy[0], xy[1]);
			if (viewHolder.tmpChannel.isSkip()) {
				tv_open_btn.setEnabled(true);
				tv_open_btn.setFocusable(true);

				tv_off_btn.setEnabled(false);
				tv_off_btn.setFocusable(false);

			} else {

				tv_open_btn.setEnabled(false);
				tv_open_btn.setFocusable(false);

				tv_off_btn.setEnabled(true);
				tv_off_btn.setFocusable(true);
			}

			tv_open_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					viewHolder.tmpChannel.setSkipState(false);
					mAdapter.notifyDataSetChanged();
					popupWindow.dismiss();
					saveChannel(viewHolder.tmpChannel.mServiceIndex, false);
					keyActionCallBack.resetTimer();
				}
			});

			tv_off_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					viewHolder.tmpChannel.setSkipState(true);
					mAdapter.notifyDataSetChanged();
					popupWindow.dismiss();
					saveChannel(viewHolder.tmpChannel.mServiceIndex, true);
					keyActionCallBack.resetTimer();
				}
			});
		}
	}

	public PopupWindow makePopupWindow(Context cx) {
		final PopupWindow window = new PopupWindow(cx);

		View contentView = null;
		contentView = LayoutInflater.from(cx).inflate(R.layout.popupwindow, null);
		contentView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		//����PopupWindow��ʾ������ʱ�Ķ���
		window.setAnimationStyle(android.R.style.Animation_Dialog);
		//����PopupWindow�Ĵ�С����Ⱥ͸߶ȣ�
		window.setWidth(pop_width);
		window.setHeight(pop_height);
		//����PopupWindow������view
		window.setContentView(contentView);
		//����PopupWindow�ⲿ�����Ƿ�ɴ���
		window.setFocusable(true);
		window.setOutsideTouchable(false);
		window.setBackgroundDrawable(null);

		tv_open_btn = (Button) contentView.findViewById(R.id.channel_on_btn);
		tv_off_btn = (Button) contentView.findViewById(R.id.channel_off_btn);

		String open = mContext.getResources().getString(R.string.menu_program_open);
		String off = mContext.getResources().getString(R.string.menu_program_off);

		tv_open_btn.setText(open);
		tv_off_btn.setText(off);

		tv_open_btn.setOnFocusChangeListener(new PouupButtonFocusListener());
		tv_off_btn.setOnFocusChangeListener(new PouupButtonFocusListener());

		tv_open_btn.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (null != popupWindow && popupWindow.isShowing()) {
						popupWindow.dismiss();
					}
				}
				return false;
			}
		});
		tv_off_btn.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (null != popupWindow && popupWindow.isShowing()) {
						popupWindow.dismiss();
					}
				}
				return false;
			}
		});
		return window;
	}

	class PouupButtonFocusListener implements Button.OnFocusChangeListener {

		@Override
		public void onFocusChange(View view, boolean arg1) {
			// TODO Auto-generated method stub
			keyActionCallBack.resetTimer();
		}

	}

	@Override
	public void setChooseOneSwap(boolean isChooseOneSwap) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isChooseOneSwap() {
		// TODO Auto-generated method stub
		return false;
	}

	public void saveChannel(final int index, final boolean skip) {

		handler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mInterface.channelSkip(index, skip);
			}
		});
	}
}
