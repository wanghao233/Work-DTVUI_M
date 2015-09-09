package com.changhong.tvos.dtv.util;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.changhong.tvos.dtv.AsyncImageLoader;
import com.changhong.tvos.dtv.AsyncImageLoader.ImageCallback;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.menuManager.MenuManager.listState;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.system.commondialog.CommenInputDialog;
import com.changhong.tvos.system.commondialog.CommonInfoDialog;

public class ViewSelfOrder extends ViewGridBase implements android.widget.AdapterView.OnItemSelectedListener, android.widget.AdapterView.OnItemClickListener {
	private static final String TAG = "ViewSelfOrder";
	private static List<DtvProgram> myChangeList;
	public static boolean hasChanged = false;
	private DtvChannelManager mChannelManager = DtvChannelManager.getInstance();
	private DtvProgram preProgram;

	private CommonInfoDialog myDialog;
	private int dialog_height = 70;
	private int dialog_width = 520;
	private int dialog_margin = 480;
	private int dialog_margin_y = 30;

	public interface InputShowListner {
		public void showMenu();

		public void hideMenu();
	}

	private InputShowListner inputShowListner;
	private CommenInputDialog inputDialog;

	public ViewSelfOrder(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ViewSelfOrder(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ViewSelfOrder(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(MenuManager listmanager, listState state) {
		// TODO Auto-generated method stub
		manager = listmanager;
		manager.init(state);

		setAnimationOk(true);
		setChooseOneSwap(false);

		curProgramIndexUpdate(manager.getCurProgramListIndex());

		mAdapter.init(manager);

		if (null == myChangeList) {
			//临时备份一个list 用来处理加载UI， 原本的节目列表则用来处理交换等事件
			myChangeList = new ArrayList<DtvProgram>(manager.getCurList());
		} else {
			myChangeList.clear();
			myChangeList.addAll(manager.getCurList());
		}

		mAdapter.notifyDataSetChanged();
		setSelection(curPosition);
		preProgram = mChannelManager.getPreChannel();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		if (mAdapter == null) {
			mAdapter = new SelfOrderAdapter(mContext);
			setAdapter(mAdapter);
		}

		setOnItemSelectedListener(this);
		setOnItemClickListener(this);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v("tv", "LL onKeyDown>>keyCode = " + keyCode);
		if (!isAnimationOk()) {
			Log.v(TAG, "EL channelListGridView>>onKeyDown>> still Animation not keyAction;");
			return true;
		}
		Log.i(TAG, "EL--> curPosion is " + curPosition + " and the selction " + getSelectedItemPosition());
		lastPage = curPage;

		int curKey = revertKeyCode(keyCode, event);

		Log.v(TAG, "LL onKeyDown>>keyCode = " + keyCode + "and revert keycode is + " + curKey);

		switch (curKey) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_DOWN:

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
			Log.i(TAG, "EL--> onKeyUp not response due to still animation ");
			return true;
		}
		keyActionCallBack.keyActionUp(keyCode, event);
		return super.onKeyUp(keyCode, event);
	}

	class SelfOrderAdapter extends SimpleGridAdapter {

		public SelfOrderAdapter(Context myContext) {
			// TODO Auto-generated constructor stub
			this.mContext = myContext;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			RelativeLayout myRelativeLayout = null;
			final ImageView channelLogImageView;
			TextView channelNum;
			TextView channelName;
			viewHolder = new ViewHolder();
			myRelativeLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.view_swap_channel_sel, null);
			myRelativeLayout.setTag(viewHolder);

			channelLogImageView = (ImageView) myRelativeLayout.findViewById(R.id.imageView1);
			channelNum = (TextView) myRelativeLayout.findViewById(R.id.textView1);
			channelName = (TextView) myRelativeLayout.findViewById(R.id.textView2);

			DtvProgram tmpChannel;

			tmpChannel = myChangeList.get((position + curPage * (columns * linage)));
			viewHolder.tmpChannel = tmpChannel;
			channelNum.setText("D" + tmpChannel.mProgramNum);
			channelName.setText(tmpChannel.mProgramName);

			channelLogImageView.setScaleType(ImageView.ScaleType.FIT_XY);

			Drawable drawable = AsyncImageLoader.getInstance().loadDrawable(("/data/dtv/logo/" + tmpChannel.mDtvLogo), new ImageCallback() {

				@Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					if (null == imageDrawable) {
						Log.i(TAG, "EL--> the image is null");
					} else {
						channelLogImageView.setImageDrawable(imageDrawable);
					}
				}

			});
			if (null != drawable) {
				//					 Log.i(TAG, "EL--> the image drawable is not null");
				channelLogImageView.setImageDrawable(drawable);
			}
			return myRelativeLayout;
		}

	}

	private class ViewHolder {
		public DtvProgram tmpChannel;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.i(TAG, "LL onItemClick()>>arg2 = " + arg2 + ",arg3 = " + arg3);

		keyActionCallBack.resetTimer();
		ViewHolder viewHolder = (ViewHolder) arg1.getTag();
		curPosition = arg2;
		curChooseDtvProgram = viewHolder.tmpChannel;
		//		handler.removeCallbacks(changeChannelRunnable);
		//		if(null != curChooseDtvProgram){
		//			manager.changeChannelByProgramServiceIndex(curChooseDtvProgram.getProgramServiceIndex());
		//		}
		if (null == inputDialog) {
			inputDialog = new CommenInputDialog(mContext);
			inputDialog.setTitle(R.string.dtv_menu_self_order_info);
			inputDialog.setMessage(R.string.dtv_menu_channel_info_channel_num);
			inputDialog.setOKButtonListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					inputDialog.dismiss();
					String input = inputDialog.getInputWorld();
					if (null != input && (input.length() != 0 || !input.equals(""))) {
						exchangeProgram(Integer.parseInt(input), myChangeList.indexOf(curChooseDtvProgram));
					}

				}
			});

			inputDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					inputShowListner.showMenu();
				}
			});

		}
		inputDialog.setNumHint(String.valueOf(viewHolder.tmpChannel.getProgramNum()));
		inputShowListner.hideMenu();
		inputDialog.show();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = (ViewHolder) arg1.getTag();
		curPosition = arg2;
		arg1.setSelected(true);

		curChooseDtvProgram = viewHolder.tmpChannel;
		//		startChannelChange();
		//		if(null != curChooseDtvProgram){
		//			handler.removeCallbacks(changeChannelRunnable);
		//			handler.postDelayed(changeChannelRunnable, DELAY_TIME);
		//		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	private void exchangeProgram(int curInputNum, int curChoose) {
		Log.i(TAG, "exchangeProgram() curInputNum " + curInputNum + " curChoose " + curChoose);

		if (0 < curInputNum && curInputNum <= mChannelManager.getMaxChannelNum()) {
			if (curInputNum == curChooseDtvProgram.getProgramNum()) {
				Log.i(TAG, "input num equal curChooseDtvProgram num");
				return;
			}
			//副本只用于改变节目号的显示
			DtvProgram temChannel = myChangeList.get(curChoose);
			int desIndex = -1;
			int size = myChangeList.size();
			for (int i = 0; i < size; i++) {

				DtvProgram program = myChangeList.get(i);
				if (curInputNum <= program.mProgramNum) {
					//					program.setProgramNum(temChannel.mProgramNum);
					desIndex = i;
					break;
				}
			}
			if (desIndex == -1) {
				if (curInputNum < myChangeList.get(0).mProgramNum) {
					desIndex = 0;
				} else if (curInputNum > myChangeList.get(size - 1).mProgramNum) {
					desIndex = size - 1;
				}
			}

			if (desIndex >= 0 && desIndex < myChangeList.size()) {
				if (desIndex < curChoose) {
					temChannel.setProgramNum(curInputNum);
					for (int i = desIndex; i < curChoose; i++) {
						DtvProgram program = myChangeList.get(i);
						if (program.mServiceID != temChannel.mServiceID) {
							program.setProgramNum(++curInputNum);
						}
					}
				} else {

					temChannel.setProgramNum(curInputNum);
					for (int i = desIndex; i > curChoose; i--) {
						DtvProgram program = myChangeList.get(i);
						if (program.mServiceID != temChannel.mServiceID) {
							program.setProgramNum(--curInputNum);
						}
					}
				}

				myChangeList.remove(temChannel);
				myChangeList.add(desIndex, temChannel);

				lastPage = curPage;
				curPosition = desIndex % getPageCounts();
				curPage = desIndex / getPageCounts();
				Log.i(TAG, "prepage is : " + lastPage + " curpage is: " + curPage);
				freshView();
				preProgram = mChannelManager.getPreChannel();
				mChannelManager.setCurProgram(temChannel);
				mChannelManager.setPreChannel(preProgram);
				hasChanged = true;
			}
		} else {
			if (null == myDialog) {
				DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();
				dialog_width = (int) (dialog_width * mDisplayMetrics.scaledDensity);
				dialog_height = (int) (dialog_height * mDisplayMetrics.scaledDensity);

				myDialog = new CommonInfoDialog(mContext);
				myDialog.setGravity(Gravity.BOTTOM | Gravity.LEFT, dialog_margin, dialog_margin_y);
				myDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
				myDialog.info_layout.setLayoutParams(new FrameLayout.LayoutParams(dialog_width, dialog_height));
				myDialog.tv.setTextColor(Color.WHITE);
				myDialog.tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f);
				myDialog.setMessage("请输入正确的范围(1 ~ " + mChannelManager.getMaxChannelNum() + " ).");
			}
			myDialog.show();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//将交换好的节目设置下去

		if (inputDialog != null && inputDialog.isShowing()) {
			inputDialog.dismiss();
		}
	}

	public void setInputShowListner(InputShowListner inputShowListner) {
		this.inputShowListner = inputShowListner;
	}

	public InputShowListner getInputShowListner() {
		return inputShowListner;
	}

	public void setChangedList() {
		if (hasChanged) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					manager.setExchangeList(myChangeList);
				}
			}).start();

			hasChanged = false;
		}
	}
}
