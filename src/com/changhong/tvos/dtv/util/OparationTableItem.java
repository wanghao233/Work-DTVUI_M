package com.changhong.tvos.dtv.util;

import java.util.Collections;
import java.util.List;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.changhong.data.pageData.itemData.ItemRadioButtonData;
import com.changhong.menuView.itemView.ItemRadioButtonView;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvConfigManager;
import com.changhong.tvos.dtv.tvap.DtvOperatorManager;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass;
import com.changhong.tvos.dtv.tvap.baseType.DtvOperator;
import com.changhong.tvos.system.commondialog.CommonProgressInfoDialog;
import com.changhong.tvos.system.commondialog.ScanWarnDialog;

public class OparationTableItem extends RelativeLayout {
	private static final String TAG = "OparationTableItem";
	private TextView proviceText;
	private List<DtvOperator> operatorList;
	private LinearLayout table;
	private Context mContext;
	private View mView;
	private final int MAXCOLUMN = 3;
	private int rowNum;
	private int itemWidth;
	private int rowWidth;
	private DtvOperatorManager mDtvOperatorManager;
	private OnOperatonItemClickListener mOnOperatonItemClickListener;
	private DtvOperator curOpatator;
	DisplayMetrics mDisplayMetrics;

	private boolean isGuideMode;
	private String curOperatorName;

	public interface OnOperationChangeListener {
		public void onChanged(boolean hasChange, String changeData);

		public void findCurOperation(ItemRadioButtonData data, ItemRadioButtonView view);
	}

	private OnOperationChangeListener onChangeListener;

	public OparationTableItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	public OparationTableItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	public OparationTableItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		mView = LayoutInflater.from(mContext).inflate(R.layout.opration_list_item, null);
		this.addView(mView);
		proviceText = (TextView) mView.findViewById(R.id.province);
		table = (LinearLayout) mView.findViewById(R.id.oprationContainer);
		mDisplayMetrics = mContext.getResources().getDisplayMetrics();
	}

	public void initDataByProvice(String provice, List<DtvOperator> operatorList) {
		this.operatorList = operatorList;
		mDtvOperatorManager = DtvOperatorManager.getInstance();
		curOpatator = mDtvOperatorManager.getCurOperator();
		if (null == mOnOperatonItemClickListener) {
			mOnOperatonItemClickListener = new OnOperatonItemClickListener();
		}
		proviceText.setText(provice);
		Collections.sort(this.operatorList);
		initTableItems();
	}

	public void setOnOperatonItemClickListener(OnOperatonItemClickListener listener) {
		mOnOperatonItemClickListener = listener;
	}

	public void upDate(String provice, List<DtvOperator> operatorList) {
		table.removeAllViews();
		proviceText.setText(provice);
		this.operatorList = operatorList;
		Collections.sort(this.operatorList);
		curOpatator = mDtvOperatorManager.getCurOperator();
		initTableItems();
	};

	@SuppressWarnings("deprecation")
	private void initTableItems() {
		if (null == operatorList || operatorList.size() == 0) {
			return;
		}
		int size = operatorList.size();
		LinearLayout.LayoutParams params_matchParent = new LinearLayout.LayoutParams(rowWidth, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams params_wrap = new LinearLayout.LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT);
		int dividerDx = (int) (1 * mDisplayMetrics.scaledDensity);
		rowNum = (operatorList.size() - 1) / MAXCOLUMN + 1;

		for (int i = 0; i < rowNum; i++) {
			LinearLayout row = new LinearLayout(mContext);
			row.setOrientation(LinearLayout.HORIZONTAL);
			table.addView(row, params_matchParent);

			for (int j = 0; (j < MAXCOLUMN) && (i * MAXCOLUMN + j) < size; j++) {
				ItemRadioButtonView mItemView = new ItemRadioButtonView(mContext);
				mItemView.setFocusable(true);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					mItemView.setBackground(mContext.getResources().getDrawable(R.drawable.menu_item_selector));
				} else {
					mItemView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.menu_item_selector));

				}
				ItemRadioButtonData data = new ItemRadioButtonData(0, operatorList.get(i * MAXCOLUMN + j).getOperatorName(), 0, 0) {

					@Override
					public int isEnable() {
						// TODO Auto-generated method stub
						return 1;
					}
				};

				if (curOpatator.getOperatorName().equals(data.mName)) {
					data.setSelected(true);
					mItemView.requestFocus();
					onChangeListener.findCurOperation(data, mItemView);
				}

				mItemView.init(data);
				mItemView.setOnClickListener(mOnOperatonItemClickListener);

				row.addView(mItemView, params_wrap);

				if (j < MAXCOLUMN - 1) {
					LinearLayout lay = new LinearLayout(mContext);
					lay.setBackgroundColor(Color.BLACK);
					row.addView(lay, new LayoutParams(dividerDx, LayoutParams.FILL_PARENT));
				}
			}
			if (i < rowNum - 1) {

				LinearLayout lay = new LinearLayout(mContext);
				lay.setBackgroundColor(Color.BLACK);
				table.addView(lay, new LayoutParams(LayoutParams.FILL_PARENT, dividerDx));
			}
		}
		//		this.invalidate();
	}

	public class OnOperatonItemClickListener implements OnClickListener {
		private boolean isChanged;

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			final ItemRadioButtonView mItemView = (ItemRadioButtonView) arg0;
			final ItemRadioButtonData data = (ItemRadioButtonData) mItemView.getItemData();
			if (data != null) {
				if (!data.getSelected()) {
					if (isGuideMode) {
						data.setSelected(true);
						data.update();
						mItemView.update();

						curOperatorName = data.mName;

						onChangeListener.onChanged(true, curOperatorName);
						onChangeListener.findCurOperation(data, mItemView);
					} else {

						final ScanWarnDialog dialog = new ScanWarnDialog(mContext);
						dialog.setDisplayString(mContext.getResources().getString(R.string.no_string),//dtv_scan_setup_waring
								mContext.getResources().getString(R.string.dtv_scan_setup_waring_info));
						dialog.getmYesButton().setOnClickListener(new OnClickListener() {

							public void onClick(View v) {
								// TODO Auto-generated method stub
								isChanged = true;
								dialog.dismiss();
								data.setSelected(true);
								data.update();
								mItemView.update();
								curOperatorName = data.mName;
								changeOperationByName(data.mName);
							}
						});
						dialog.setOnDismissListener(new OnDismissListener() {

							public void onDismiss(DialogInterface arg0) {
								// TODO Auto-generated method stub
								if (isChanged) {

									CommonProgressInfoDialog progressDiglog = new CommonProgressInfoDialog(mContext);
									//提高菜单显示层级 2014.05.16
									progressDiglog.getWindow().setType(2003);
									progressDiglog.setDuration(1000000);
									progressDiglog.setButtonVisible(false);
									progressDiglog.setCancelable(false);
									progressDiglog.setMessage(mContext.getString(R.string.dtv_system_reboot));
									progressDiglog.show();

									DtvChannelManager.getInstance().reset();
									DtvConfigManager.getInstance().clearAll();

									DtvConfigManager.getInstance().setValue(ConstValueClass.ConstOperatorState.OPCHANGED, "true");
									MenuManager.getInstance().delAllScheduleEvents();

									new Handler().postDelayed(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
											powerManager.reboot("");

										}
									}, 5000);
								}
							}
						});
						dialog.show();
					}

				} else {
					onChangeListener.onChanged(false, curOperatorName);
				}
			}
		}
	}

	public void changeOperationByName(String chooseOperationName) {
		// TODO Auto-generated method stub

		for (DtvOperator operator : operatorList) {
			if (operator.getOperatorName().equals(chooseOperationName)) {
				mDtvOperatorManager.setCurOperator(operator);
				onChangeListener.onChanged(true, chooseOperationName);
				break;
			}
		}
	}

	public String getCurOperatorName() {
		return curOperatorName;
	}

	public void setOnChangeListener(OnOperationChangeListener onChangeListener) {
		this.onChangeListener = onChangeListener;
	}

	public OnOperationChangeListener getOnChangeListener() {
		return onChangeListener;
	}

	public void setItemWidth(int parent_width) {
		itemWidth = (parent_width) / MAXCOLUMN;
	}

	// 计算headView的width及height值  
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setItemWidthAndOffset(int width, int i) {
		// TODO Auto-generated method stub
		int dividerDx = (int) (3 * mDisplayMetrics.scaledDensity);

		rowWidth = width;
		itemWidth = (width - i - dividerDx) / MAXCOLUMN;
	}

	public void setGuideMode(boolean isGuide) {
		isGuideMode = isGuide;
	}
}