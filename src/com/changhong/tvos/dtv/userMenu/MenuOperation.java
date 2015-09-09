package com.changhong.tvos.dtv.userMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.changhong.data.pageData.itemData.ItemRadioButtonData;
import com.changhong.menuView.itemView.ItemRadioButtonView;
import com.changhong.menudata.MainMenuReceiver;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvConfigManager;
import com.changhong.tvos.dtv.tvap.DtvDialogManager;
import com.changhong.tvos.dtv.tvap.DtvOperatorManager;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass;
import com.changhong.tvos.dtv.tvap.baseType.DtvOperator;
import com.changhong.tvos.dtv.util.OparationTableItem;
import com.changhong.tvos.dtv.util.OparationTableItem.OnOperationChangeListener;
import com.changhong.tvos.system.commondialog.VchCommonToastDialog;

public class MenuOperation extends Dialog {

	private static final String TAG = "MenuOperation";
	private View mContentView;
	private LinearLayout oprationContainer;
	private TextView curOpration;
	private DtvOperatorManager mDtvOperatorManager;
	private List<DtvOperator> operatorList;

	private Map<String, List<DtvOperator>> data;
	private int lastSourceId = -1;
	private int curSourceId = -1;
	private Context mContext;
	private OnOperationChangeListener mOnOperationChangeListener;
	private ItemRadioButtonData curChooseData;
	private ItemRadioButtonView curChooseView;
	private boolean isShowTv;
	private Runnable dissMissRunnable;
	private Handler mHandler;
	private int duration = 30000;
	private ScrollView mScrollView;

	private boolean isGuideMode;
	private Button btnNext;
	private String curChooseOprator;
	private String tmpOp;
	private ImageView arrow_down;
	@SuppressWarnings("unused")
	private ImageView searchGuide;

	private LinearLayout buttomDivider;
	public final static int ACTION_REBOOT = 0;
	public final static int ACTION_SCAN = 1;
	private DtvSourceManager mSourceManager;

	public interface ActionCallBack {
		public int actionCallBack(int action);
	}

	private ActionCallBack mActionCallBack;

	public MenuOperation(Context context) {
		super(context, R.style.Theme_ActivityTransparent);
		mContext = context;
		// TODO Auto-generated constructor stub
		// RelativeLayout mBg =(RelativeLayout)
		// LayoutInflater.from(mContext).inflate(R.layout.menu_null, null);
		mContentView = LayoutInflater.from(context).inflate(R.layout.menu_operation_detail, null);
		setContentView(mContentView);
		// mBg.addView(mContentView);
		mScrollView = (ScrollView) findViewById(R.id.opration_scrollView);
		buttomDivider = (LinearLayout) findViewById(R.id.buttomDivider);
		arrow_down = (ImageView) findViewById(R.id.arrow_down);
		oprationContainer = (LinearLayout) mContentView.findViewById(R.id.opration_layout);
		curOpration = (TextView) mContentView.findViewById(R.id.curOperation);
		mSourceManager = DtvSourceManager.getInstance();
		mDtvOperatorManager = DtvOperatorManager.getInstance();
		lastSourceId = -1;
		operatorList = mDtvOperatorManager.getOperatorList();
		mOnOperationChangeListener = new OnOperationChangeListener() {

			@Override
			public void onChanged(boolean hasChange, String curOpratorName) {
				// TODO Auto-generated method stub
				tmpOp = curOpratorName;
				Log.i(TAG, "onChanged:" + curOpratorName);
				if (null != curChooseData && hasChange) {

					curChooseData.setSelected(false);
					curChooseData.update();
					if (null != curChooseView) {
						curChooseView.update();
					}
				} else {
					if (!isGuideMode) {
						dismiss();
					} else {
						btnNext.requestFocus();
					}
				}
			}

			@Override
			public void findCurOperation(ItemRadioButtonData data, ItemRadioButtonView view) {
				// TODO Auto-generated method stub
				curChooseData = data;
				curChooseView = view;
			}
		};

		dissMissRunnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				dismiss();
			}
		};

		mHandler = new Handler();

		btnNext = (Button) mContentView.findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i(TAG, "curSelectOprator:" + tmpOp + " curChooseOprator:" + curChooseOprator);

				if (curSourceId != mSourceManager.getCurSourceID()) {
					Log.i(TAG, "curSource not equal system");

					if (null == tmpOp) { // �л����ź�Դ�� ����û��ѡ����Ӫ��
						VchCommonToastDialog myDialog = new VchCommonToastDialog(mContext);
						myDialog.setMessage(R.string.dtv_search_guide_noOporator);
						myDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
						myDialog.getWindow().setType(2003);
						myDialog.show();
						return;
					}

					mSourceManager.setCurSource(curSourceId);

					DtvChannelManager.getInstance().reset();
					DtvConfigManager.getInstance().clearAll();

					DtvConfigManager.getInstance().setValue(ConstValueClass.ConstOperatorState.OP_GUIDE, "true");
					MenuManager.getInstance().delAllScheduleEvents();

					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							changeOperationByName(curChooseOprator);
							PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
							powerManager.reboot("");
						}
					}, 5000);

					if (null != mActionCallBack) {
						mActionCallBack.actionCallBack(ACTION_REBOOT);
					}
				} else { //û���л��ź�Դ
					if (null != tmpOp && !tmpOp.equals(curChooseOprator)) {
						DtvChannelManager.getInstance().reset();
						DtvConfigManager.getInstance().clearAll();

						DtvConfigManager.getInstance().setValue(ConstValueClass.ConstOperatorState.OP_GUIDE, "true");
						MenuManager.getInstance().delAllScheduleEvents();

						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								changeOperationByName(tmpOp);
								PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
								powerManager.reboot("");
							}
						}, 5000);

						if (null != mActionCallBack) {
							mActionCallBack.actionCallBack(ACTION_REBOOT);
						}
					} else if (null == tmpOp || tmpOp.equals(curChooseOprator)) {
						if (null != mActionCallBack) {
							mActionCallBack.actionCallBack(ACTION_SCAN);
						}
					}
				}
				dismiss();
			}
		});
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		super.cancel();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		if (isShowTv) {
			MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
		}
		mHandler.removeCallbacks(dissMissRunnable);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		curSourceId = (-1 == curSourceId) ? mSourceManager.getCurSourceID() : curSourceId;
		operatorList = mDtvOperatorManager.getOperatorListBySourceID(curSourceId);

		curChooseOprator = mDtvOperatorManager.getCurOperator().getOperatorName();
		isShowTv = false;
		DtvDialogManager.AddShowDialog(this);
		super.show();
		mContentView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.wave_scale));
		if (isGuideMode) {
			btnNext.setVisibility(View.VISIBLE);
		}
		if (null == operatorList || operatorList.size() < 22) {
			buttomDivider.setVisibility(View.GONE);
			arrow_down.setVisibility(View.GONE);

		} else {
			buttomDivider.setVisibility(View.VISIBLE);
			arrow_down.setVisibility(View.VISIBLE);
		}
	}

	public void setShowTv(boolean b) {
		// TODO Auto-generated method stub
		isShowTv = b;
	}

	public boolean isShowTv() {
		return isShowTv;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {

			curSourceId = (-1 == curSourceId) ? mSourceManager.getCurSourceID() : curSourceId;
			curOpration.setText(mDtvOperatorManager.getCurOperator().getOperatorName());
			if (lastSourceId != curSourceId) {
				lastSourceId = curSourceId;
				int width = oprationContainer.getWidth();
				int padding = oprationContainer.getPaddingLeft();
				// Log.i(TAG, "oprationContainer width is " + width +
				// " panding is " + padding);
				data = mDtvOperatorManager.getProvincesBySourceId(curSourceId, mContext);
				if (null != data && data.size() > 0) {
					String tmp = mContext.getString(R.string.dtv_scansettings_operator_general);
					Set<String> provinces = data.keySet();
					List<String> proList = new ArrayList<String>(provinces);
					Collections.sort(proList);
					if (proList.contains(tmp)) {
						proList.remove(tmp);
						proList.add(0, tmp);
					}
					for (String province : proList) {
						Log.i(TAG, "province:" + province);
						OparationTableItem item = new OparationTableItem(mContext);
						item.setGuideMode(isGuideMode);
						item.setItemWidthAndOffset(width, padding * 4);
						item.setOnChangeListener(mOnOperationChangeListener);
						item.initDataByProvice(province, data.get(province));
						oprationContainer.addView(item, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
					}
				}
			}
			if (isGuideMode) {
				// btnNext.requestFocus();
			}
			mHandler.removeCallbacks(dissMissRunnable);
			mHandler.postDelayed(dissMissRunnable, duration);
		} else {
			mHandler.removeCallbacks(dissMissRunnable);
		}
	}

	//////////////////////////FENGY 2014-3-25 /////////////////////////////////////
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onKeyUp>> keyCode = " + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_SOURCE:
			// case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBAR:
			
		case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBOX://4126:// KEYCODE_CHANGHONGIR_TOOLBAR
			
		case 170:// KEYCODE_CHANGHONGIR_TV
			
		case KeyEvent.KEYCODE_CHANGHONGIR_TV://4135:
			isShowTv = true;
			
		case KeyEvent.KEYCODE_MENU:
		case KeyEvent.KEYCODE_BACK:
			dismiss();
			return true;
			
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL:
			mScrollView.scrollBy(0, -mScrollView.getHeight());
			break;
			
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR:
			mScrollView.scrollBy(0, mScrollView.getHeight());
			break;

		case KeyEvent.KEYCODE_DPAD_DOWN:
			int messaureHeight = mScrollView.getMeasuredHeight();
			int scrollY = mScrollView.getScrollY();
			Log.i(TAG, "messaureHeight:" + messaureHeight + " scrollY: " + scrollY);
			if (messaureHeight == (scrollY + mScrollView.getHeight())) {
				arrow_down.setVisibility(View.GONE);
			}

			mHandler.removeCallbacks(dissMissRunnable);
			mHandler.postDelayed(dissMissRunnable, duration);
			break;
			
		default:
			Log.i(TAG, "111111 onKeyDown>> dissMissRunnable = " + duration);
			mHandler.removeCallbacks(dissMissRunnable);
			mHandler.postDelayed(dissMissRunnable, duration);
			break;
		}

		Log.i(TAG, "onKeyDown>> 888888= " + keyCode);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onKeyDown>> keyCode = " + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_SOURCE:
			// case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBAR:
			
		case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBOX://4126:// KEYCODE_CHANGHONGIR_TOOLBAR
			
		case 170:// KEYCODE_CHANGHONGIR_TV
			
		case KeyEvent.KEYCODE_CHANGHONGIR_TV://4135:
			isShowTv = true;
			
		case KeyEvent.KEYCODE_MENU:
		case KeyEvent.KEYCODE_BACK:
			dismiss();
			return true;
			
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL:
			mScrollView.scrollBy(0, -mScrollView.getHeight());
			break;
			
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR:
			mScrollView.scrollBy(0, mScrollView.getHeight());
			break;

		case KeyEvent.KEYCODE_DPAD_DOWN:
			int messaureHeight = mScrollView.getMeasuredHeight();
			int scrollY = mScrollView.getScrollY();
			Log.i(TAG, "messaureHeight:" + messaureHeight + " scrollY: " + scrollY);
			if (messaureHeight == (scrollY + mScrollView.getHeight())) {
				arrow_down.setVisibility(View.GONE);
			}

			mHandler.removeCallbacks(dissMissRunnable);
			mHandler.postDelayed(dissMissRunnable, duration);
			break;
			
		default:
			mHandler.removeCallbacks(dissMissRunnable);
			mHandler.postDelayed(dissMissRunnable, duration);
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public void setGuideMode(boolean isGuideMode) {
		this.isGuideMode = isGuideMode;
	}

	public boolean isGuideMode() {
		return isGuideMode;
	}

	public void setSourceId(int sourceId) {
		curSourceId = sourceId;
	}

	public String getCurChooseOpratorname() {
		return curChooseOprator;
	}

	public void changeOperationByName(String chooseOperationName) {
		// TODO Auto-generated method stub
		for (DtvOperator operator : operatorList) {
			if (operator.getOperatorName().equals(chooseOperationName)) {
				mDtvOperatorManager.setCurOperator(operator);
				break;
			}
		}
	}

	public void setActionCallBack(ActionCallBack mActionCallBack) {
		this.mActionCallBack = mActionCallBack;
	}

	public ActionCallBack getActionCallBack() {
		return mActionCallBack;
	}
}