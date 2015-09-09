package com.changhong.tvos.dtv.epgView;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvEvent;
import com.changhong.tvos.dtv.tvap.baseType.DtvScheduleEvent;
import com.changhong.tvos.dtv.vo.DTVConstant;
import com.changhong.tvos.system.commondialog.CommonInfoDialog;
import com.changhong.tvos.system.commondialog.ScanWarnDialog;

//每一个条目
public class SmartView extends RelativeLayout {

	private static final String TAG = "SmartView";
	private Context mContext;
	LinearLayout linear_smart;
	private ImageView iv_icon, reserveState;
	private TextView tv_time, tv_program;
	private TextView tv_title;
	public RelativeLayout linear_zipper;
	public LinearLayout zipper;

	List<DtvEvent> eventList;
	CoverEpgView coverView;
	public MenuManager menuManager = MenuManager.getInstance();
	public final Calendar mCalendar = Calendar.getInstance();
	Date curDate;
	CommonInfoDialog mydialog;
	ScanWarnDialog mConflictDialog;

	SimpleDateFormat dateFormat;
	public int id = -1;//SmartView在ViewGroup中的位置
	public int mState = -1;//SmartView在ViewGroup中是否被选中，0为否，1为是
	private SmartViewGroup vgroup;//SmartView所在的Group	
	private final Rect mRect = new Rect();//切片区域
	private BitmapRegionDecoder mDecoder;//切片工具
	private List<View> list;

	private Button btnReserve;
	private Button btnRecord;

	private boolean isChannelInfoState;
	private boolean isNeedKeepState;
	private boolean isToZipperExpand;
	private boolean isReserved;
	private boolean isAdd;

	private DtvEvent cur_tv_info;
	private DtvScheduleEvent mscheduleEvent;

	private int pop_margin = 470;
	private int pop_width = 140;
	private int pop_height = 90;
	private int pop_singile = 55;
	private int time_icon_margin = 5;
	private float mdepth;
	//    private int length;
	private PopupWindow mwindow;

	public int bgResid = R.drawable.epg_expand_bg;//背景资源ID

	public SmartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		isToZipperExpand = false;
		viewInit();
	}

	public SmartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		isToZipperExpand = false;
		viewInit();
	}

	public SmartView(Context context) {
		super(context);
		mContext = context;
		isToZipperExpand = false;
		viewInit();
	}

	private void viewInit() {
		mdepth = 400;
		isNeedKeepState = false;
		isReserved = false;
		setAdd(false);
		View v = LayoutInflater.from(mContext).inflate(R.layout.epg_old_smart_layout, this);
		linear_smart = (LinearLayout) findViewById(R.id.linear_smart);
		iv_icon = (ImageView) v.findViewById(R.id.iv_icon);
		tv_time = (TextView) v.findViewById(R.id.tv_time);

		tv_title = (TextView) v.findViewById(R.id.tv_title);

		tv_program = (TextView) v.findViewById(R.id.tv_channelProgram);

		reserveState = (ImageView) v.findViewById(R.id.stateIcon);

		linear_zipper = (RelativeLayout) v.findViewById(R.id.linear_zipper);
		zipper = (LinearLayout) v.findViewById(R.id.zipper);
		coverView = (CoverEpgView) v.findViewById(R.id.cover);

		DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();
		pop_height = (int) (pop_height * mDisplayMetrics.scaledDensity);
		pop_width = (int) (pop_width * mDisplayMetrics.scaledDensity);
		pop_margin = (int) (pop_margin * mDisplayMetrics.scaledDensity);
		pop_singile = (int) (pop_singile * mDisplayMetrics.scaledDensity);
		time_icon_margin = (int) (time_icon_margin * mDisplayMetrics.scaledDensity);
	}

	/**
	 * epg Item的初始化
	 * @param tv_info  epg信息
	 * @param scheduleEvent  预约信息
	 * @param isChnanelInfo  true： epg信息   false：预约信息
	 */
	public void init(DtvEvent tv_info, DtvScheduleEvent scheduleEvent, boolean isChnanelInfo) {

		cur_tv_info = tv_info;
		mscheduleEvent = scheduleEvent;

		linear_smart.setOnFocusChangeListener(new myOnFocusChangeListener());
		linear_smart.setOnClickListener(new PopUpMenuListener());

		isChannelInfoState = isChnanelInfo;
		dateFormat = new SimpleDateFormat("HH:mm");

		curDate = menuManager.getCurrentDate();

		eventList = vgroup.eventsList;
		mydialog = vgroup.mydialog;
		mConflictDialog = vgroup.mConflictDialog;

		if (isChnanelInfo) {
			// 在节目信息下显示是否预约
			coverView.init(tv_info);

			String tv_time_string = dateFormat.format(tv_info.getStartTime());
			tv_time.setText(tv_time_string);
			tv_title.setText(tv_info.getTitle());

			String curTime = dateFormat.format(mCalendar.getTime());
			Log.i(TAG, "curTime is " + curTime + "and the DTVEventTime is " + tv_time_string);

			if (menuManager.isOrderEvent(cur_tv_info)) {
				tv_time.setLeft(time_icon_margin);
				isReserved = true;
				Drawable drawable = getContext().getResources().getDrawable(R.drawable.epg_reserved);
				reserveState.setImageDrawable(drawable);
				//				reserveState.setImageResource(R.drawable.epg_reserved);
				reserveState.setVisibility(View.VISIBLE);
			} else {
				isReserved = false;
				reserveState.setVisibility(View.GONE);
			}
			tv_program.setVisibility(View.GONE);

		} else {
			//在预约列表显示当前节目
			tv_title.setText(scheduleEvent.getTitle());
			tv_time.setText(dateFormat.format(scheduleEvent.getStartTime()));
			reserveState.setVisibility(View.GONE);
			tv_program.setText(scheduleEvent.getProgramName());

		}

	}

	public void setZipperExpand(boolean isExpand) {
		isToZipperExpand = isExpand;
		if (isToZipperExpand) {
			linear_zipper.setVisibility(View.VISIBLE);
			coverView.startProgress();

		} else {
			linear_zipper.setVisibility(View.GONE);
		}
	}

	/**
	 * 隐藏当前播放的节目的进度条信息
	 * @return
	 */
	public boolean removeCoverView() {

		//		isToZipperExpand = false;
		if (null != coverView) {
			coverView.onDestroy();
			Log.i(TAG, "coverView.onDestroy——>onDestroy");
			return true;
		}

		return false;
	}

	@Override
	public void setId(int id) {
		linear_smart.setId(id);
		super.setId(id);
	}

	public void getReFocus() {
		linear_smart.requestFocus();
	}

	public void removeFocus() {
		linear_smart.clearFocus();
	}

	//本身的扩展动画
	public void SmartViewExpandAnimation() {
		//计算zipper的宽和高

		//		int h =linear_zipper.getLayoutParams().height;
		//折叠动画
		if (isToZipperExpand) {
			//			GetTwoViewCombin();
			if (linear_zipper.getVisibility() == View.GONE) {
				//				zipper.removeAllViews();
				//				ZipperExpandAnimation(list.get(0),list.get(1));
				//				
				//				//添加折叠动画
				//				for(int i =0;i<list.size();i++){
				//					zipper.addView(list.get(i));
				//				}
				try {
					coverView.startProgress();
				} catch (Exception e) {
					e.printStackTrace();
				}

				linear_zipper.setVisibility(View.VISIBLE);

			}

		}

	}

	private List<View> GetTwoViewCombin() {
		//图片2分切片
		try {
			InputStream is = getResources().openRawResource(bgResid);
			mDecoder = BitmapRegionDecoder.newInstance(is, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final int imgWidth = mDecoder.getWidth();//图片宽
		final int imgHeight = mDecoder.getHeight();//图片高       
		mRect.set(0, 0, imgWidth, imgHeight / 2);
		final Bitmap bitmap1 = mDecoder.decodeRegion(mRect, null);//切片1
		mRect.set(0, imgHeight / 2, imgWidth, imgHeight);
		final Bitmap bitmap2 = mDecoder.decodeRegion(mRect, null);//切片2
		BitmapDrawable bd1 = new BitmapDrawable(bitmap1);
		BitmapDrawable bd2 = new BitmapDrawable(bitmap2);

		ImageView iv1 = new ImageView(mContext);
		iv1.setLayoutParams(new LinearLayout.LayoutParams(600, 100));
		iv1.setBackgroundDrawable(bd1);

		ImageView iv2 = new ImageView(mContext);
		iv2.setLayoutParams(new LinearLayout.LayoutParams(600, 100));
		iv2.setBackgroundDrawable(bd2);
		list = new ArrayList<View>();
		list.add(iv1);
		list.add(iv2);
		return list;
	}

	//对两个切片(iv1和iv2)实施动画,展开动画
	private void ZipperExpandAnimation(View v1, View v2) {
		//上部旋转动画+Z深度变化
		SmartViewZipperAnimation v1anim = new SmartViewZipperAnimation(-60, 0, 0, 100, mdepth, true);
		v1anim.setFillAfter(true);
		v1anim.setDuration(500);
		v1anim.setInterpolator(new AccelerateInterpolator());
		v1.startAnimation(v1anim);

		//下部旋转动+Z深度变化
		SmartViewZipperAnimation v2anim = new SmartViewZipperAnimation(60, 0, 0, 0, mdepth, true);
		v2anim.setFillAfter(true);
		v2anim.setDuration(500);
		v2anim.setInterpolator(new AccelerateInterpolator());
		v2.startAnimation(v2anim);

	}

	public void getGroup(ViewGroup group) {
		vgroup = (SmartViewGroup) group;

	}

	public class myOnFocusChangeListener implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {

			if (hasFocus) {

				vgroup.menuEpg_NEW_UI.refeshShowTime();

				vgroup.setFocusID(getId());
				//					v.setBackgroundResource(R.drawable.epg_item_smartview_bg_focuse);	
				v.setFocusable(true);
				((TextView) ((LinearLayout) v).getChildAt(1)).setTextColor(Color.BLACK);
				TextView t2 = ((TextView) ((LinearLayout) v).getChildAt(2));
				t2.setTextColor(Color.BLACK);

				t2.setMarqueeRepeatLimit(-1);
				v.setSelected(true);

				SmartView.this.SmartViewExpandAnimation();
				if (isChannelInfoState) {
					if (isReserved) {
						reserveState.setImageResource(R.drawable.epg_reserved);
						reserveState.setVisibility(View.VISIBLE);
						vgroup.setHelpInfo(R.string.menu_help_cancell);
					} else {
						vgroup.setHelpInfo(R.string.menu_help_reserve);
					}
				} else {
					tv_program.setTextColor(Color.BLACK);
					tv_program.setMarqueeRepeatLimit(-1);
					vgroup.setHelpInfo(R.string.menu_background_sure);
				}

			} else {
				if (!isNeedKeepState) {

					v.setSelected(false);

					linear_zipper.setVisibility(View.GONE);
					//并且取消被选择的背景   xml已经完成该功能
					//						linear_smart.setBackgroundColor(Color.BLACK);		
					//并且还原字体颜色为白色					
					TextView t1 = (TextView) (linear_smart.getChildAt(1));
					t1.setTextColor(Color.WHITE);
					TextView t2 = (TextView) (linear_smart.getChildAt(2));
					t2.setTextColor(Color.WHITE);
					t2.setMarqueeRepeatLimit(1);
					if (isChannelInfoState) {
						if (isReserved) {
							reserveState.setImageResource(R.drawable.epg_reserved);
							reserveState.setVisibility(View.VISIBLE);
						}
					} else {
						tv_program.setTextColor(Color.WHITE);
						tv_program.setMarqueeRepeatLimit(1);
					}

					if (isToZipperExpand) {
						removeCoverView();
					}
				}
			}
		}
	}

	//	public void keepState(){
	//		if(isToZipperExpand){
	//			isNeedKeepState = true;
	//			linear_smart.setBackgroundResource(R.drawable.epg_item_smartview_bg_focuse);	
	//			linear_smart.setFocusable(true);		
	//			((TextView)linear_smart.getChildAt(1)).setTextColor(Color.BLACK);
	//			((TextView)linear_smart.getChildAt(2)).setTextColor(Color.BLACK);
	//			this.SmartViewExpandAnimation();
	//		}
	//	
	//	}

	public LinearLayout getLinearSmart() {
		return this.linear_smart;
	}

	public void setIconPlay(boolean isShow) {
		if (isShow) {
			iv_icon.setVisibility(View.VISIBLE);
			iv_icon.setImageResource(R.drawable.tv_icon_test);
		} else {
			iv_icon.setVisibility(View.GONE);
		}
	}

	public DtvEvent getDtvEvent() {
		return cur_tv_info;

	}

	/**
	 * 预约和取消epg
	 */
	public void reserveAction() {

		if (eventList != null && eventList.size() > 0) {
			if (null == cur_tv_info)
				return;

			long eventStartTime = cur_tv_info.getStartTime().getTime();
			if (eventStartTime < curDate.getTime()) {
				Log.v("tv", "eventStartTime =" + eventStartTime);
				Log.v("tv", "eventStartTime curDate.getTime() =" + curDate.getTime());

				mydialog.setHintIcon(CommonInfoDialog.TYPE_WARN);
				mydialog.setMessage(mContext.getString(R.string.menu_epg_program_outdated));
				mydialog.setDuration(3);
				mydialog.show();
			} else {
				List<DtvScheduleEvent> scheEvents = menuManager.getScheduleEvents();
				if (scheEvents != null && scheEvents.size() >= 36 && !menuManager.isOrderEvent(cur_tv_info)) {
					mydialog.setHintIcon(CommonInfoDialog.TYPE_WARN);
					mydialog.setMessage(mContext.getString(R.string.menu_epg_schedule_count));
					mydialog.setDuration(3);
					mydialog.show();
					return;
				}
				final DtvScheduleEvent scheduleEvent = menuManager.getConflictEvent(cur_tv_info);
				if (null != scheduleEvent) {

					Log.v(TAG, "scheduleEvent conflict");
					StringBuffer strBuffer = new StringBuffer();
					strBuffer.append(dateFormat.format(scheduleEvent.getStartTime()));
					strBuffer.append("\n");
					strBuffer.append(scheduleEvent.getTitle());
					mConflictDialog.setDisplayString(mContext.getResources().getString(R.string.menu_epg_schedule_conflict_title),
							mContext.getResources().getString(R.string.menu_epg_schedule_conflict_message, strBuffer));

					//					mConflictDialog.setDialogMeasurement(400, 200, 0, 0);
					mConflictDialog.show();
					mConflictDialog.getmYesButton().setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Log.i(TAG, "LL delete conflict schedule event***");
							menuManager.delScheduleEvent(scheduleEvent);
							menuManager.addScheduleEvent(cur_tv_info);
							mConflictDialog.dismiss();
							if (null != mwindow && mwindow.isShowing()) {
								mwindow.dismiss();
							}

							reserveState.setImageResource(R.drawable.epg_reserved);
							reserveState.setVisibility(View.VISIBLE);
							isReserved = true;

						}
					});
				} else {

					if (menuManager.isOrderEvent(cur_tv_info)) {
						menuManager.delScheduleEvent(cur_tv_info);
						reserveState.setVisibility(View.GONE);
						isReserved = false;
						vgroup.setHelpInfo(R.string.menu_help_reserve);
						Log.i(TAG, "LL end add del schedule event*** " + dateFormat.format(cur_tv_info.getStartTime()) + " and the end " + dateFormat.format(cur_tv_info.getEndTime()));
						Log.i(TAG, "LL del_event _reserve " + cur_tv_info.getTitle());

						mydialog.setHintIcon(CommonInfoDialog.TYPE_WARN);
						mydialog.setMessage(mContext.getString(R.string.menu_epg_unschedule_success));
						mydialog.setDuration(3);
						mydialog.show();

					} else {
						Log.i(TAG, "LL start add new schedule event***");
						menuManager.addScheduleEvent(cur_tv_info);
						reserveState.setImageResource(R.drawable.epg_reserved);
						reserveState.setVisibility(View.VISIBLE);
						isReserved = true;
						vgroup.setHelpInfo(R.string.menu_help_cancell);
						Log.i(TAG, "LL end add new addScheduleEvent() " + cur_tv_info.getTitle() + "curSerIndex = " + menuManager.getCurProgramServiceIndex());

						mydialog.setHintIcon(CommonInfoDialog.TYPE_WARN);
						mydialog.setMessage(mContext.getString(R.string.menu_epg_schedule_success));
						mydialog.setDuration(3);
						mydialog.show();
					}

				}

			}
		}

	}

	/**
	 * 生成一个ＰＯＰＵＰＷｉｎｄｏｗ实例，响应预约列表下的取消和播放事件
	 * @param cx :context
	 * @return popupwindow
	 */
	public PopupWindow makePopupWindow(Context cx) {
		final PopupWindow window = new PopupWindow(cx);

		View contentView = null;
		Log.v(TAG, "the popup window height and width is " + pop_height + " " + pop_width);
		contentView = LayoutInflater.from(cx).inflate(R.layout.epg_old_menu_popup, null);
		contentView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		//设置PopupWindow显示和隐藏时的动画
		window.setAnimationStyle(android.R.style.Animation_Dialog);
		//设置PopupWindow的大小（宽度和高度）

		window.setWidth(pop_width);
		window.setHeight(pop_height);
		//设置PopupWindow的内容view
		window.setContentView(contentView);
		//设置PopupWindow外部区域是否可触摸
		window.setFocusable(true);
		window.setOutsideTouchable(false);

		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				//				vgroup.resetTextColors();

				//change linear_smart to the white background red board and text black_color 
				linear_smart.setBackgroundResource(R.drawable.epg_item_smartview_bg_selector);
				linear_smart.setFocusable(true);
				((TextView) linear_smart.getChildAt(1)).setTextColor(Color.BLACK);
				((TextView) linear_smart.getChildAt(2)).setTextColor(Color.BLACK);
				SmartView.this.SmartViewExpandAnimation();
				if (isChannelInfoState) {
					if (isReserved) {
						reserveState.setImageResource(R.drawable.epg_reserved);
						reserveState.setVisibility(View.VISIBLE);
					}
				} else {
					tv_program.setTextColor(Color.BLACK);
				}
			}

		});

		btnReserve = (Button) contentView.findViewById(R.id.channel_reserve_btn);
		btnRecord = (Button) contentView.findViewById(R.id.chanenl_rercord_btn);

		if (isChannelInfoState) {
			btnReserve.setFocusable(true);
			btnRecord.setVisibility(View.GONE);
			window.setHeight(pop_singile);
		} else {
			btnReserve.setFocusable(true);
			btnRecord.setFocusable(true);
		}

		btnReserve.setOnFocusChangeListener(new PouupButtonFocusListener());
		btnRecord.setOnFocusChangeListener(new PouupButtonFocusListener());

		btnReserve.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				vgroup.menuEpg_NEW_UI.refeshShowTime();

				if (isChannelInfoState) {
					reserveAction();
				} else {
					//play...
					DtvChannelManager mChannelManager = DtvChannelManager.getInstance();
					DtvSourceManager mSourceManager = DtvSourceManager.getInstance();

					if (mSourceManager.getCurSourceID() != mscheduleEvent.getSourceID()) {
						mSourceManager.setCurSource(mscheduleEvent.getSourceID());
						mChannelManager.setCurChannelType(mChannelManager.getBootChannelType());
					}

					if (DTVConstant.ConstServiceType.SERVICE_TYPE_TV != DtvChannelManager.getInstance().getCurChannelType()) {
						//如果当前状态不是电视状态，切换回电视状态后 再换台
						DtvChannelManager.getInstance().changeChannelType();
					}
					MenuManager manager = MenuManager.getInstance();
					manager.changeChannelByProgramServiceIndex(mscheduleEvent.getProgramServiceIndex());

					Log.i(TAG,
							"LL play shedule event " + mscheduleEvent.getProgramName() + "service index " + mscheduleEvent.getProgramServiceIndex() + "program number "
									+ mscheduleEvent.getProgramNum());

					vgroup.getChangeChannelListener().showChannelInfo();
					vgroup.getChangeChannelListener().dissMissDialog();
				}

				mwindow.dismiss();
			}

		});

		btnRecord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				vgroup.menuEpg_NEW_UI.refeshShowTime();
				btnRecord.setTextColor(R.color.selected);

				if (isChannelInfoState) {
					//recorder....
				} else {
					mwindow.dismiss();
					menuManager.delScheduleEvent(mscheduleEvent);
					vgroup.setCancelResertAction(linear_smart.getId() / 2);
					//refresh view
					SmartView.this.setVisibility(View.GONE);
					Log.i(TAG, "LL delShedule " + mscheduleEvent.getProgramName() + "service index " + mscheduleEvent.getProgramServiceIndex() + "program number " + mscheduleEvent.getProgramNum());

					mydialog.setHintIcon(CommonInfoDialog.TYPE_WARN);
					mydialog.setMessage(mContext.getString(R.string.menu_epg_unschedule_success));
					mydialog.setDuration(3);
					mydialog.show();
				}
				//				mwindow.dismiss();
			}
		});
		return window;
	}

	class PopUpMenuListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			vgroup.menuEpg_NEW_UI.refeshShowTime();
			if (isChannelInfoState) {
				reserveAction();
			} else {
				mwindow = null;
				mwindow = makePopupWindow(mContext);
				if (isChannelInfoState) {
					btnReserve.setText(R.string.menu_popup_reserve);
					btnRecord.setText(R.string.menu_popup_record);

					if (reserveState.getVisibility() == View.VISIBLE) {
						btnReserve.setText(R.string.menu_popup_cancell);
					}
				} else {
					btnReserve.setText(R.string.menu_popup_play);
					btnRecord.setText(R.string.menu_popup_cancell);
				}

				int xy[] = new int[2];

				v.getLocationInWindow(xy);

				mwindow.showAtLocation(vgroup.menuEpg_NEW_UI.mView, Gravity.LEFT | Gravity.TOP, pop_margin, xy[1]);
				mwindow.setFocusable(true);

				//change linear_smart to red background and white text
				linear_smart.setBackgroundColor(getResources().getColor(R.color.selected));
				//并且还原字体颜色为白色					
				TextView t1 = (TextView) (linear_smart.getChildAt(1));
				t1.setTextColor(Color.WHITE);
				TextView t2 = (TextView) (linear_smart.getChildAt(2));
				t2.setTextColor(Color.WHITE);

				if (isChannelInfoState) {
					if (isReserved) {
						reserveState.setImageResource(R.drawable.epg_reserved);
						reserveState.setVisibility(View.VISIBLE);
					}
				} else {
					tv_program.setTextColor(Color.WHITE);
				}
			}

		}

	}

	class PouupButtonFocusListener implements Button.OnFocusChangeListener {

		@Override
		public void onFocusChange(View view, boolean arg1) {
			// TODO Auto-generated method stub
			vgroup.menuEpg_NEW_UI.refeshShowTime();
		}

	}

	public void onDestroy() {
		if (null != mwindow && mwindow.isShowing()) {
			mwindow.dismiss();
		}
		if (null != coverView) {
			coverView.onDestroy();
			coverView = null;
		}
		Log.i(TAG, "SmartView——>onDestroy");

	}

	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}

	public boolean isAdd() {
		return isAdd;
	}

}
