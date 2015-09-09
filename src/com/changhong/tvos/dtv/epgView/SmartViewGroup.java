package com.changhong.tvos.dtv.epgView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvCommonManager;
import com.changhong.tvos.dtv.tvap.DtvEpgManager;
import com.changhong.tvos.dtv.tvap.DtvSourceManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvEvent;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.DtvScheduleEvent;
import com.changhong.tvos.dtv.util.ViewScrollingText;
import com.changhong.tvos.system.commondialog.CommonInfoDialog;
import com.changhong.tvos.system.commondialog.ScanWarnDialog;

//在ANDROID中实现3D旋转直接使用animation配合camera就可以实现
/**
 * 设计思路：
 * 初始化显示的时候只显示六个。当六个加载完获取焦点，则添加剩下的items
 */
public class SmartViewGroup extends LinearLayout {

	public interface ChangeChannelListener {
		public void showChannelInfo();

		public void dissMissDialog();
	}

	private ChangeChannelListener changeChannelListener;
	private Context mContext;
	private List<SmartView> listsmart;

	private float mCenterX;
	private float mCenterY;
	private float mDepth;
	private int dialog_height = 70;
	private int dialog_width = 520;
	private int dialog_margin = 480;
	private int dialog_margin_y = 30;
	private int itemHeight = 60;
	private int divider_height = 1;

	private int curAddItem;
	private boolean isAddOver;
	private int curSlectItemID;
	private View mView;

	//用于放置epgItem的容器
	private ScrollView scrollview;
	public LinearLayout linear;

	public List<DtvEvent> eventsList;

	private List<?> tempEventList;

	private ColorStateList curDayColor;
	private ColorStateList notCurDayColor;

	private TextView dateTextView;
	private TextView curProNumText;
	private ViewScrollingText curProNameText;
	private TextView help_sureText;
	private Button reserveBtn;
	private Button channelBtn;
	private Button lastFocusButton;

	CommonInfoDialog mydialog; //toast 消息对话框
	ScanWarnDialog mConflictDialog; // 冲突消息提示框

	private RelativeLayout reserveBtnBackground;
	private RelativeLayout channelBtnBackGround;
	private ImageView ibt_dwn;

	MenuEpg_NEW_UI menuEpg_NEW_UI;

	private String[] days;
	private String[] weekDays;

	private SmartView smart_num0;
	SmartView curPlay;

	private int curWeekDayIndex;
	private int today;
	private int weekBtnIds[];
	private SimpleDateFormat dateFormatTest;
	private boolean isChannelInfo = true;
	private boolean isItemNeedRequest;
	private boolean isInitOver;
	private int curPlayPosition = -1;
	private static int epgNumEveryPage = 6;

	private static final String TAG = "Test";

	private SimpleDateFormat dateFormat;
	private SimpleDateFormat weekFormat;

	public Calendar mCalendar = Calendar.getInstance();
	public MenuManager menuManager = MenuManager.getInstance();
	private DtvCommonManager mCommonManager = DtvCommonManager.getInstance();
	private DtvEpgManager dtvEpgManager = DtvEpgManager.getInstance();
	private DtvSourceManager sourceManager = DtvSourceManager.getInstance();

	public enum Channel_info_menuState {
		reserve_menu, channel_info_menu;
	}

	private static final int FOUND_CURPLAY = -4;
	private static final int REFRESH_EPG = -3;
	private static final int UPDATE_TIME = -5;
	public static final int GET_EPG_TIMES = 60;
	public int getEpgInfoTimes = GET_EPG_TIMES;
	private int curDayIndex;

	private Runnable updateTime = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(UPDATE_TIME);
		}
	};

	//获取epg 和 预约信息的线程
	public Runnable mRunObtainEpg = new Runnable() {
		public void run() {
			if (isChannelInfo) {
				eventsList.clear();
				Log.i(TAG, "EL --> get list begin  ");
				eventsList = menuManager.getEventListByTime(curDayIndex, 0, 24);
				Log.i(TAG, "EL --> get list end  ");

				if (eventsList == null || eventsList.size() == 0) {
					Log.i(TAG, "enventList : null ");
					initSmartViews(null);

				} else if (true == MenuEpg_NEW_UI.hasCurSourceReceiveEpg(false, false)) {

					curPlayPosition = dtvEpgManager.getPlayingTVProgramPositon(eventsList);

					Log.v(TAG, "******GET THE CURRENT EVENT  ******  and the curPlayPosition is " + curPlayPosition);
					initSmartViews(eventsList);

				} else {
					initSmartViews(null);
				}

				if ((null == eventsList || eventsList.size() == 0) && getEpgInfoTimes > 0) {
					handler.removeCallbacks(mRunObtainEpg);
					handler.postDelayed(mRunObtainEpg, 1000);
					getEpgInfoTimes--;
				}
			} else {
				List<DtvScheduleEvent> scheEvents = menuManager.getScheduleEvents();
				initSmartViews(getScheduleEnventByWeek(scheEvents));
			}

		}
	};
	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == UPDATE_TIME) {
				updateCurTime();
			}
			if (msg.what >= 0 && msg.what < listsmart.size()) {

				SmartView smart = (SmartView) listsmart.get(msg.what);
				if (msg.what == 0) {
					smart_num0 = smart;
					linear.removeAllViews();
					//					Log.i(TAG, "EL -- > add item 0 at " + Calendar.getInstance().getTimeInMillis());
				}
				linear.addView(smart);
				curAddItem++;
				//				if(isChannelInfo){
				//					//节目信息状态下 ， 添加当前播放节目和后面六个的动画效果。 不是当天则添加最开始的六个的动画效果
				//					if(curWeekDayIndex == today){
				//						
				//						if(msg.what >= curPlayPosition  && 
				//								msg.what < ((curPlayPosition<0 ? 0:curPlayPosition) + epgNumEveryPage)){
				//							
				//							SmartViewAnimation(smart, 300, 0, 100);
				//							
				//						}else if(msg.what < curPlayPosition){
				//							
				//							//当天时， 先将没有动画的隐藏。
				//							smart.setVisibility(View.INVISIBLE);
				//						}
				//					}else if(msg.what < epgNumEveryPage){
				//						SmartViewAnimation(smart, 300, 0, 100);
				//						
				//					}
				//					
				//				}else if (msg.what < epgNumEveryPage) {
				//					SmartViewAnimation(smart, 300, 0, 100);
				//					
				//				}

				ImageView iv_divider = new ImageView(mContext);
				iv_divider.setId(msg.what * 2 + 1);
				iv_divider.setBackgroundResource(R.drawable.epg_divider);
				iv_divider.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
				linear.addView(iv_divider);
			}

		}

	};

	public void setItemNeedRequest(boolean isItemNeedRequest) {
		this.isItemNeedRequest = isItemNeedRequest;
	}

	public boolean isItemNeedRequest() {
		return isItemNeedRequest;
	}

	public SmartViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		setUpAndInitDatas();
	}

	public SmartViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setUpAndInitDatas();
	}

	public SmartViewGroup(Context context) {
		super(context);
		mContext = context;
		setUpAndInitDatas();
	}

	public void setParams() {

		weekBtnIds = new int[] { R.id.Btn_week0, R.id.Btn_week1, R.id.Btn_week2, R.id.Btn_week3, R.id.Btn_week4, R.id.Btn_week5, R.id.Btn_week6 };

		DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();
		dialog_width = (int) (dialog_width * mDisplayMetrics.scaledDensity);
		dialog_height = (int) (dialog_height * mDisplayMetrics.scaledDensity);
		itemHeight = (int) (itemHeight * mDisplayMetrics.scaledDensity);
		dialog_margin = (int) (dialog_margin * mDisplayMetrics.scaledDensity);

		divider_height = (int) (divider_height * mDisplayMetrics.scaledDensity);

		mCenterX = mCenterX * mDisplayMetrics.scaledDensity;
		mCenterY = mCenterY * mDisplayMetrics.scaledDensity;
		mDepth = mDepth * mDisplayMetrics.scaledDensity;
		;
	}

	public void setUpAndInitDatas() {
		//初始化数据层
		curDayIndex = 0;
		setItemNeedRequest(true);

		mCenterX = -100;
		mCenterY = 0;
		mDepth = 200;

		setParams();

		View v = LayoutInflater.from(mContext).inflate(R.layout.epg_old_smartviewgroup_layout, this);
		mView = v;

		scrollview = (ScrollView) v.findViewById(R.id.scrollview_id);
		linear = (LinearLayout) v.findViewById(R.id.linear_smartview);
		//enlong add 
		help_sureText = (TextView) v.findViewById(R.id.help_confirm);

		dateTextView = (TextView) v.findViewById(R.id.date_text);
		dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		dateFormatTest = new SimpleDateFormat("HH:mm:ss:SSS");
		weekFormat = new SimpleDateFormat("E");

		dateTextView.setText(dateFormat.format(mCalendar.getTime()));

		curProNumText = (TextView) v.findViewById(R.id.logic_num);
		curProNameText = (ViewScrollingText) v.findViewById(R.id.channel_name);

		reserveBtn = (Button) v.findViewById(R.id.reserve_info);
		channelBtn = (Button) v.findViewById(R.id.channel_info);

		reserveBtnBackground = (RelativeLayout) v.findViewById(R.id.reserve_btn_background);
		channelBtnBackGround = (RelativeLayout) v.findViewById(R.id.channel_btn_background);
		ibt_dwn = (ImageView) v.findViewById(R.id.ibt_down);

		days = mContext.getResources().getStringArray(R.array.week_seven_days);
		weekDays = mContext.getResources().getStringArray(R.array.week_7_days);

		Date mDate = mCommonManager.getCurrentDate();
		mCalendar.setTime(mDate);
		today = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;

		Log.i(TAG, "Calendar.DAY_OF_WEEK is " + today);

		XmlPullParser xml = getResources().getXml(R.drawable.btn_week_cur_day_selector);
		XmlPullParser xml2 = getResources().getXml(R.drawable.btn_week_selector_not_curday);
		try {
			curDayColor = ColorStateList.createFromXml(getResources(), xml);
			notCurDayColor = ColorStateList.createFromXml(getResources(), xml2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < days.length; i++) {
			Button btn = (Button) v.findViewById(weekBtnIds[i]);
			if (i == today) {
				curWeekDayIndex = i;
				btn.setTextColor(curDayColor);
			} else {
				btn.setTextColor(notCurDayColor);
			}

			btn.setText(days[i]);
		}

		eventsList = menuManager.getEventListByTime(curDayIndex, 0, 24);

		curPlay = null;

		mydialog = new CommonInfoDialog(mContext);
		mConflictDialog = new ScanWarnDialog(mContext);

		mydialog.setGravity(Gravity.BOTTOM | Gravity.LEFT, dialog_margin, dialog_margin_y);
		mydialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
		mydialog.info_layout.setLayoutParams(new FrameLayout.LayoutParams(dialog_width, dialog_height));
		mydialog.tv.setTextColor(Color.WHITE);
		mydialog.tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f);
		setFocusListener();

	}

	/**
	 * 获取信息（重置各个状态）
	 */
	public void resetWeekDay() {

		menuManager.init(MenuManager.listState.channel_List);
		curDayIndex = 0;

		Date mDate = mCommonManager.getCurrentDate();
		mCalendar.setTime(mDate);
		today = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
		curWeekDayIndex = today;

		linear.removeAllViews();

		DtvProgram curProgram = menuManager.getCurProgram();
		int programnum = curProgram.getProgramNum();
		//		if(programnum < 10){
		//			curProNumText.setText("0" + programnum);
		//		}else{
		curProNumText.setText(String.valueOf(programnum));
		//		}
		curProNameText.setText(curProgram.getProgramName());
		updateCurTime();

		setWeekBtnFoucusTrue();
		setItemNeedRequest(true);
		isChannelInfo = true;
		if (reserveBtn.isFocused()) {
			Log.i(TAG, "EL--> reservBtn focused");
			reserveBtn.clearFocus();
		}

		Button curBtn = (Button) mView.findViewById(weekBtnIds[curWeekDayIndex]);
		curBtn.requestFocus();
		resetTextColors();
		upDateMenu();
	}

	public void init() {
		isAddOver = false;
		isInitOver = false;
		curAddItem = 0;

		if (null != curPlay) {
			curPlay.onDestroy();
			curPlay = null;
		}
		//		smart_num0 = null;
		getEpgInfoTimes = 0;
		Log.i(TAG, "curDay is " + "星期" + curWeekDayIndex);
		linear.removeAllViews();
		timeChange();
	}

	/**
	 * 依据日期获取epg
	 * @param event
	 * @return
	 */
	private List<DtvScheduleEvent> getScheduleEnventByWeek(List<DtvScheduleEvent> event) {
		List<DtvScheduleEvent> list = new ArrayList<DtvScheduleEvent>();

		if (null == event || event.size() == 0) {
			return list;
		} else {
			int size = event.size();
			for (int i = 0; i < size; i++) {
				DtvScheduleEvent schedule = event.get(i);
				Date time = schedule.getStartTime();

				String week = weekFormat.format(time);
				String curDay = "周" + days[curWeekDayIndex];
				Log.v(TAG, "---the curShedule is " + week + "and the is " + weekDays[curWeekDayIndex]);

				if (curDay.equals(week) || weekDays[curWeekDayIndex].equals(week) || -1 != week.indexOf(days[curWeekDayIndex])) {
					if (sourceManager.getCurSourceID() == schedule.getSourceID()) {
						list.add(schedule);
					}
				}
			}
		}
		return list;
	}

	/** 
	 * 依据epg Or sheduleEvents 的  list初始化listItems。
	 * @param dtvEvents
	 */
	public void initSmartViews(List<?> dtvEvents) {

		if (null != listsmart) {
			listsmart.clear();
		} else {
			listsmart = new ArrayList<SmartView>();
		}
		if (null != tempEventList) {
			tempEventList.clear();
			tempEventList = null;
		}

		tempEventList = dtvEvents;

		int size = 0;
		if (null != dtvEvents && dtvEvents.size() > 0) {
			size = dtvEvents.size();
			Log.i(TAG, "EL --> add items begin at time " + dateFormatTest.format(new Date()));
			Log.i(TAG, "EL... the items list size is " + size);
			for (int index = 0; index < size; index++) {
				SmartView smart = new SmartView(mContext);

				//begin init epg_smart items
				smart.getGroup(SmartViewGroup.this);
				if (isChannelInfo) {

					smart.init((DtvEvent) tempEventList.get(index), null, isChannelInfo);
					if (index == curPlayPosition) {
						smart.setIconPlay(true);
						curPlay = smart;
						smart.setZipperExpand(true);

						Log.v(TAG, "***get the curplay item *** " + index);
					} else {
						smart.setIconPlay(false);
					}

				} else {
					smart.init(null, (DtvScheduleEvent) tempEventList.get(index), isChannelInfo);
					smart.setIconPlay(false);
				}
				//end  init epg_smart items
				smart.setId(index * 2);
				listsmart.add(smart);
				Log.i(TAG, "EL hander a message to add to view ");

				//当天则添加已经播放的+ 6 ， 不是当天的 就只是添加了六个
				if (isChannelInfo && curWeekDayIndex == today && index < ((curPlayPosition < 0 ? 0 : curPlayPosition) + epgNumEveryPage)) {
					smart.setAdd(true);
					handler.sendEmptyMessage(index);
				} else if (index < epgNumEveryPage) {
					smart.setAdd(true);
					handler.sendEmptyMessage(index);
				} else {
					smart.setAdd(false);
				}
			}
			Log.i(TAG, "EL --> end init items at time " + dateFormatTest.format(new Date()));
		}

		addItemHandler.sendEmptyMessage(REFRESH_EPG);

	}

	public Handler addItemHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case REFRESH_EPG:
				Log.v(TAG, "******REFRESH_EPG");
				updateCurTime();
				addSmartEpgItem(listsmart);
				break;

			case FOUND_CURPLAY:

				break;

			default:
				break;
			}
		}

	};

	/**
	 * 滚动到当前播过的节目
	 */
	private void scrollToCurPlay() {
		upDateMenu();
		if (-1 != curPlayPosition && null != curPlay) {

			int heightOff = (itemHeight + divider_height) * (curPlayPosition);
			scrollview.scrollTo(0, heightOff);
			if (isItemNeedRequest()) {
				curPlay.getReFocus();
				setItemNeedRequest(false);
			} else {
				if (null != lastFocusButton) {
					curPlay.getReFocus();
					lastFocusButton.requestFocus();
				}
			}

		} else if (null == curPlay && isItemNeedRequest() && null != smart_num0) {

			smart_num0.getReFocus();
			setItemNeedRequest(false);
			scrollview.scrollTo(0, 0);

		} else {
			scrollview.scrollTo(0, 0);

		}

	}

	/**
	 * list.size < epgNumEveryPage 时 ,添加空板补齐界面
	 * list.size = 0 | null时，添加无epg信息提示
	 * @param list
	 */
	public void addSmartEpgItem(List<SmartView> list) {

		//中间层的smartview动画,SmartView加入到本类列表
		if (null == list || list.size() < epgNumEveryPage) {
			ibt_dwn.setVisibility(View.GONE);
		} else {
			ibt_dwn.setVisibility(View.VISIBLE);
		}

		if (null == list || list.size() == 0) {
			linear.removeAllViews();
			TextView noEpg = new TextView(mContext);
			String noInfo = null;
			if (isChannelInfo) {
				noInfo = mContext.getResources().getString(R.string.menu_epg_event_null);
			} else {
				noInfo = mContext.getResources().getString(R.string.menu_epeg_shedule_none);
			}
			noEpg.setText(noInfo);
			noEpg.setTextColor(Color.WHITE);
			noEpg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f);

			noEpg.setBackgroundColor(Color.BLACK);
			noEpg.setGravity(Gravity.CENTER);
			noEpg.setLayoutParams(new AbsListView.LayoutParams(scrollview.getLayoutParams().width, scrollview.getLayoutParams().height));
			linear.addView(noEpg);

			Button btn = (Button) mView.findViewById(weekBtnIds[curWeekDayIndex]);
			btn.setNextFocusDownId(weekBtnIds[curWeekDayIndex]);
		} else {

			int size = listsmart.size();

			if (size < epgNumEveryPage) {

				//将空余补齐，美观
				for (int index = size; index < epgNumEveryPage; index++) {
					ImageView bg_board = new ImageView(mContext);
					bg_board.setBackgroundResource(R.drawable.epg_item_smartview_bg_default);
					if (null != smart_num0) {
						bg_board.setLayoutParams(smart_num0.getLayoutParams());
					}
					linear.addView(bg_board);

					ImageView iv_divider = new ImageView(mContext);
					iv_divider.setBackgroundResource(R.drawable.epg_divider);
					linear.addView(iv_divider);
				}

			}
			try {
				if (null != listsmart && listsmart.size() > 0) {

					if (listsmart.size() < epgNumEveryPage) {
						curAddItem = listsmart.size();
					} else if (today == curWeekDayIndex && isChannelInfo) {
						if (listsmart.size() < (curPlayPosition < 0 ? 0 : curPlayPosition) + epgNumEveryPage) {
							curAddItem = listsmart.size();
						} else {
							curAddItem = (curPlayPosition < 0 ? 0 : curPlayPosition) + epgNumEveryPage;
						}
					} else {
						curAddItem = epgNumEveryPage;
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, "SmartGroupView addSmartEpgItem<list> wrong");
			}
			scrollToCurPlay();
			Log.i(TAG, "EL --> add items end at time " + dateFormatTest.format(new Date()));
			Button btn = (Button) mView.findViewById(weekBtnIds[curWeekDayIndex]);
			if (curPlay != null) {
				btn.setNextFocusDownId(curPlay.linear_smart.getId());
			} else if (null != smart_num0) {
				btn.setNextFocusDownId(smart_num0.linear_smart.getId());
			}
		}

	}

	private void SmartViewAnimation(SmartView s, long duration, long delayTime, int offset) {
		//3D动画
		SmartViewAnimation uptodownAnimation = new SmartViewAnimation(-90, 0, mCenterX, mCenterY, mDepth);
		uptodownAnimation.setFillAfter(true);
		uptodownAnimation.setDuration(duration);
		uptodownAnimation.setStartTime(Animation.START_ON_FIRST_FRAME);
		uptodownAnimation.setStartOffset(delayTime);
		delayTime += offset;
		//alpha动画,渐变
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(300);
		alpha.setRepeatCount(0);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(uptodownAnimation);
		set.addAnimation(alpha);
		s.setAnimation(set);
	}

	public void setFocusListener() {
		for (int i = 0; i < weekBtnIds.length; i++) {

			final int currentFocus = i;

			final Button btn = (Button) mView.findViewById(weekBtnIds[i]);

			btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {

					if (hasFocus) {
						lastFocusButton = btn;
						setHelpInfo(R.string.menu_background_sure);
						if (curWeekDayIndex != currentFocus) {
							//								curDayIndex = (currentFocus - today + days.length)%days.length;
							curDayIndex = currentFocus - today;
							if (currentFocus == 0 && currentFocus != today) {
								//当前选中星期日 ,并且今天不是周日，算来减少了一周的时间，加上
								curDayIndex += days.length;
							}
							if (today == 0 && currentFocus != 0) {
								//今天星期日，并且没有选中周日,减去一周的时间
								curDayIndex -= days.length;
							}
							curWeekDayIndex = currentFocus;
							init();
							resetTextColors();
						}
						Log.i(TAG, "EL--> weekBtn focus, and index is " + curWeekDayIndex);
						setWeekBtnFoucusTrue();
						//set the cur_state colors

					} else {

					}
				}
			});

			btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					menuEpg_NEW_UI.refeshShowTime();
					setItemNeedRequest(true);
					if (curWeekDayIndex != currentFocus) {
						curDayIndex = currentFocus - today;
						if (currentFocus == 0 && 0 != today) {
							//星期日 ,算来减少了一周的时间，加上
							curDayIndex += days.length;
						}
						if (today == 0 && currentFocus != 0) {
							//今天星期日，并且没有选中周日,减去一周的时间
							curDayIndex -= days.length;
						}
						curWeekDayIndex = currentFocus;
						init();
					} else {
						//						addItemHandler.sendEmptyMessage(FOUND_CURPLAY);
						scrollToCurPlay();
						//						if(curPlay != null){
						//							
						//							curPlay.getReFocus();
						//							setItemNeedRequest(false);
						//							
						//						}else if(null !=smart_num0 ){
						//							
						//							smart_num0.getReFocus();
						//							setItemNeedRequest(false);
						//						}
					}
					resetTextColors();
					setWeekBtnFoucusTrue();
				}
			});
		}
		reserveBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "Onclick child item click");
				menuEpg_NEW_UI.refeshShowTime();

				setMenuState(Channel_info_menuState.reserve_menu);
				Log.v(TAG, "reserveBtn  focus");
				Button btn = (Button) mView.findViewById(weekBtnIds[curWeekDayIndex]);
				btn.requestFocus();
			}
		});

		channelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "Onclick child item click");
				menuEpg_NEW_UI.refeshShowTime();

				setMenuState(Channel_info_menuState.channel_info_menu);
				Log.v(TAG, "channelBtn lost focus");
				Button btn = (Button) mView.findViewById(weekBtnIds[curWeekDayIndex]);
				btn.requestFocus();
			}
		});

		reserveBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					setHelpInfo(R.string.menu_background_sure);
					lastFocusButton = reserveBtn;
					setMenuState(Channel_info_menuState.reserve_menu);
					Log.v(TAG, "reserveBtn  focus");
					setCurDayFocusState();
				}
			}
		});

		channelBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					setHelpInfo(R.string.menu_background_sure);
					lastFocusButton = channelBtn;
					setMenuState(Channel_info_menuState.channel_info_menu);
					Log.v(TAG, "channelBtn  focus");

					setCurDayFocusState();
				}
			}
		});

		setCurDayFocusState();

		ibt_dwn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				scrollview.scrollBy(0, itemHeight * (epgNumEveryPage - 1));
				menuEpg_NEW_UI.refeshShowTime();

				Log.i(TAG, "Onclick child item click");
			}
		});
		scrollview.setOnTouchListener(new ScrollView.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					Log.i(TAG, "touch move begin");
					menuEpg_NEW_UI.mHandler.removeCallbacks(menuEpg_NEW_UI.mRunShow);

				} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
					menuEpg_NEW_UI.refeshShowTime();
					Log.i(TAG, "touch move end");
				}
				return false;
			}

		});

	}

	/**
	 * 颜色处理
	 */
	public void resetTextColors() {

		for (int id = 0; id < weekBtnIds.length; id++) {
			Button weekday = (Button) mView.findViewById(weekBtnIds[id]);

			if (id == curWeekDayIndex) {
				weekday.setTextColor(curDayColor);
				weekday.setBackgroundResource(R.drawable.epg_btn_curday_bg);
			} else {
				weekday.setTextColor(notCurDayColor);
				weekday.setBackgroundResource(R.drawable.epg_week_btn_bg);
			}

		}

	}

	public Channel_info_menuState getCurState() {
		if (isChannelInfo) {
			return Channel_info_menuState.channel_info_menu;
		} else {
			return Channel_info_menuState.reserve_menu;
		}

	}

	public void setMenuState(Channel_info_menuState menuState) {
		boolean isNeedChange = false;
		if (menuState == Channel_info_menuState.channel_info_menu) {
			if (isChannelInfo) {
				//已经是节目信息状态了 不需要改变了
				isNeedChange = false;
			} else {
				isNeedChange = true;
			}

			isChannelInfo = true;

		} else {
			if (isChannelInfo) {
				//当前状态变了，需要改变
				isNeedChange = true;
			} else {
				//已经是预约信息状态了 不需要改变了
				isNeedChange = false;
			}
			isChannelInfo = false;
		}

		upDateMenu();
		if (isNeedChange) {
			Log.i(TAG, "EL--> the curState changed, should init agin");
			init();
		}
	}

	private void upDateMenu() {

		XmlPullParser xml1 = getResources().getXml(R.drawable.btn_text_curstate_color);
		XmlPullParser xml2 = getResources().getXml(R.drawable.btn_text_selector);

		if (isChannelInfo) {

			//设置节目信息的视图
			try {
				ColorStateList csl = ColorStateList.createFromXml(getResources(), xml1);
				channelBtn.setTextColor(csl);

				ColorStateList cs2 = ColorStateList.createFromXml(getResources(), xml2);
				reserveBtn.setTextColor(cs2);
			} catch (Exception e) {
				e.printStackTrace();
			}

			channelBtnBackGround.setBackgroundResource(R.drawable.epg_bg_choose_state);
			reserveBtnBackground.setBackgroundColor(Color.TRANSPARENT);

			curProNumText.setVisibility(View.VISIBLE);
			curProNameText.setVisibility(View.VISIBLE);

			Log.v(TAG, "EL--> set channel info ui");

		} else {
			//设置预约列表的视图

			try {
				ColorStateList csl = ColorStateList.createFromXml(getResources(), xml1);
				reserveBtn.setTextColor(csl);

				ColorStateList cs2 = ColorStateList.createFromXml(getResources(), xml2);
				channelBtn.setTextColor(cs2);
			} catch (Exception e) {
				e.printStackTrace();
			}

			reserveBtnBackground.setBackgroundResource(R.drawable.epg_bg_choose_state);
			channelBtnBackGround.setBackgroundColor(Color.TRANSPARENT);

			curProNumText.setVisibility(View.GONE);
			curProNameText.setVisibility(View.GONE);

			Log.v(TAG, "EL--> set reserve state ui");
		}

	}

	/**
	 * 加载没有被放入视图的items
	 */
	public void addNotInViewsItem() {
		if (null != listsmart && listsmart.size() > curAddItem) {
			int des = curAddItem + epgNumEveryPage;
			des = (des < listsmart.size()) ? des : listsmart.size();

			for (int i = curAddItem; i < des; i++) {
				if (!listsmart.get(i).isAdd()) {
					linear.addView(listsmart.get(i));
					listsmart.get(i).setAdd(true);
					ImageView iv_divider = new ImageView(mContext);
					iv_divider.setBackgroundResource(R.drawable.epg_divider);
					linear.addView(iv_divider);
				}
			}

			curAddItem = des;
			Log.i(TAG, "EL --> add item to curnum " + des + "and the size is " + listsmart.size());

			if (des >= listsmart.size()) {
				isAddOver = true;
				Log.i(TAG, "EL --> add item over");

			} else {
				isAddOver = false;
			}
		} else {
			Log.i(TAG, "EL --> add item over");
			isAddOver = true;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG, "the SmartView OnkeyDown is work " + keyCode);
		Button btn = (Button) mView.findViewById(weekBtnIds[curWeekDayIndex]);
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:

			if (channelBtn.isFocused() || reserveBtn.isFocused() || isWeekDayFocus()) {

				Log.i(TAG, "##buttons focus");
				setWeekBtnFoucusTrue();

			} else {
				int preDay = 0;
				if (0 == curWeekDayIndex) {

					//cur choose is Sunday, pre day should be Saturday
					preDay = curWeekDayIndex - 1 + weekBtnIds.length;

				} else if (1 == curWeekDayIndex) {
					//cur choose is Monday, pre day not change
					preDay = curWeekDayIndex;
					return false;
				} else {
					preDay = curWeekDayIndex - 1;
				}
				setItemNeedRequest(true);
				Button btnPre = (Button) mView.findViewById(weekBtnIds[preDay]);
				btnPre.setFocusable(true);

				if (preDay != curWeekDayIndex) {
					Log.v(TAG, "EL - change BtnRequest");
					btnPre.requestFocus();
				}

				return false;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:

			if (channelBtn.isFocused() || reserveBtn.isFocused() || isWeekDayFocus()) {
				Log.i(TAG, "##buttons focus");
				setWeekBtnFoucusTrue();

			} else {
				int nextDay = 0;
				if (weekBtnIds.length - 1 == curWeekDayIndex) {
					//cur choose is Saturday, next day should be Sunday
					nextDay = 0;
				} else if (0 == curWeekDayIndex) {
					//cur choose is Sunday, next day not change
					nextDay = curWeekDayIndex;
					return false;
				} else {
					nextDay = curWeekDayIndex + 1;
				}

				setItemNeedRequest(true);
				Button btnNext = (Button) mView.findViewById(weekBtnIds[nextDay]);
				btnNext.setFocusable(true);

				if (nextDay != curWeekDayIndex) {
					Log.v(TAG, "EL - change BtnRequest");
					btnNext.requestFocus();
				}
				return false;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			Log.i(TAG, "EL--> SmartView OnkeyDown ACTION_DOWN");

			if (reserveBtn.isFocused()) {
				reserveBtn.setNextFocusDownId(weekBtnIds[curWeekDayIndex]);

			} else if (channelBtn.isFocused()) {

				channelBtn.setNextFocusDownId(weekBtnIds[curWeekDayIndex]);

			} else if (null == tempEventList || tempEventList.size() == 0) {

				btn.setNextFocusDownId(weekBtnIds[curWeekDayIndex]);

			} else {
				//epg条目获得焦点
				setCurDayFocusState();
				if (curPlay != null) {
					Log.i(TAG, "EL--> SmartView curPlay is not null");
					btn.setNextFocusDownId(curPlay.linear_smart.getId());
				} else if (null != smart_num0) {
					Log.i(TAG, "EL--> SmartView curPlay is null");
					btn.setNextFocusDownId(smart_num0.linear_smart.getId());
				}

				//					int curSlect = getCurSelectItemPosition();
				//					if(!isAddOver &&  curSlect >= (epgNumEveryPage -1) ){
				//						//没有加载完成
				//						addNotInViewsItem();
				//					}else {
				//						Log.i(TAG, "EL --> add item over");
				//					}
			}

			break;
		case KeyEvent.KEYCODE_DPAD_UP:

			Log.i(TAG, "EL--> SmartView OnkeyDown ACTION_UP");
			if (null != smart_num0 && null != tempEventList && tempEventList.size() > 0) {
				if (smart_num0.getLinearSmart().isFocused()) {
					setCurDayFocusState();
				}
			}
			if (isWeekDayFocus()) {

				if (isChannelInfo) {
					btn.setNextFocusUpId(channelBtn.getId());
				} else {
					btn.setNextFocusUpId(reserveBtn.getId());
				}
			}

			if (channelBtn.isFocused()) {
				channelBtn.setNextFocusUpId(channelBtn.getId());
			}

			break;
		case KeyEvent.KEYCODE_GREEN:
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL:

			if (!reserveBtn.isFocused() && !channelBtn.isFocused() && !isWeekDayFocus()) {

				if (null != listsmart && listsmart.size() > epgNumEveryPage) {

					int curId = getCurSelectItemPosition();

					if (curId >= epgNumEveryPage) {
						curId -= epgNumEveryPage;
					}
					scrollview.smoothScrollBy(0, -(epgNumEveryPage * (itemHeight + divider_height)));
					//						scrollview.smoothScrollTo(0, ((itemHeight +divider_height) * curId) ); 
					listsmart.get(curId).getReFocus();
				}

				return false;
			}
			break;

		case KeyEvent.KEYCODE_YELLOW:
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR:

			if (!reserveBtn.isFocused() && !channelBtn.isFocused() && !isWeekDayFocus()) {
				if (!isAddOver) {
					//没有加载完成
					addNotInViewsItem();
				}
				if (listsmart.size() > epgNumEveryPage) {

					int curId = getCurSelectItemPosition();

					if (curId < listsmart.size() - epgNumEveryPage) {
						curId += epgNumEveryPage;

					}

					scrollview.smoothScrollBy(0, (epgNumEveryPage * (itemHeight + divider_height)));
					//						scrollview.smoothScrollTo(0, ((itemHeight +divider_height) * curId) );
					listsmart.get(curId).getReFocus();
				}

				return false;
			}
			break;
		}

		return true;
	}

	/**
	 * 反馈选中的item的index
	 * @return
	 */
	public int getCurSelectItemPosition() {
		Log.i(TAG, "EL--<< curselct item position is " + curSlectItemID / 2);
		return curSlectItemID / 2;
	}

	/**
	 * 除了当前选中的日期外，其他日期button都不可选
	 */
	private void setCurDayFocusState() {
		for (int i = 0; i < weekBtnIds.length; i++) {
			Button btn = (Button) mView.findViewById(weekBtnIds[i]);
			if (i == curWeekDayIndex) {
				btn.setFocusable(true);
			} else {
				btn.setFocusable(false);
			}
		}
	}

	/**
	 * 恢复日期button可选择性
	 */
	private void setWeekBtnFoucusTrue() {
		for (int i = 0; i < weekBtnIds.length; i++) {
			Button btn = (Button) mView.findViewById(weekBtnIds[i]);
			btn.setFocusable(true);
		}
	}

	/**
	 * 判断是否是日期button被选中
	 * @return
	 */
	private boolean isWeekDayFocus() {

		for (int i = 0; i < weekBtnIds.length; i++) {
			Button btn = (Button) mView.findViewById(weekBtnIds[i]);
			if (btn.isFocused()) {
				Log.v(TAG, "###weekchoose is  focused###");
				return true;
			}
		}
		Log.v(TAG, "##weekchoose is not focus##");
		return false;
	}

	/**
	 * 更新时间， 获取epg信息
	 */
	void timeChange() {

		updateCurTime();
		if (isChannelInfo) {
			getEpgInfoTimes = GET_EPG_TIMES;
		}
		handler.removeCallbacks(mRunObtainEpg);
		handler.postDelayed(mRunObtainEpg, 300);
	}

	/**
	 * 节目换台
	 */
	public void curProgramChange() {
		resetWeekDay();
	}

	/**
	 * 更新时间
	 */
	public void updateCurTime() {
		mCalendar = Calendar.getInstance();
		Log.i(TAG, "updateCurTime()-->Cur time is " + dateFormat.format(mCalendar.getTime()));
		dateTextView.setText(dateFormat.format(mCommonManager.getCurrentDate()));

		DtvProgram curProgram = menuManager.getCurProgram();
		int programnum = curProgram.getProgramNum();
		//		if(programnum < 10){
		//			curProNumText.setText("0" + programnum);
		//		}else{
		curProNumText.setText(String.valueOf(programnum));
		//		}
		curProNameText.setText(curProgram.getProgramName());

		handler.removeCallbacks(updateTime);
		handler.postDelayed(updateTime, 60 * 1000);
	}

	public void onDestroy() {

		handler.removeCallbacks(updateTime);

		if (null != curPlay) {

			curPlay.onDestroy();
			curPlay = null;
			Log.i(TAG, "curPlay.onDestroy——>onDestroy");
		}
		getEpgInfoTimes = 0;

		handler.removeCallbacks(mRunObtainEpg);

		if (null != eventsList) {
			eventsList.clear();
		}

		if (null != listsmart && listsmart.size() > 0) {
			for (int i = 0; i < listsmart.size(); i++) {
				SmartView smart = listsmart.get(i);
				smart.onDestroy();
				Log.i(TAG, "smart.onDestroy——>onDestroy");
			}
			listsmart.clear();
		}
		if (null != tempEventList) {
			tempEventList.clear();
		}
	}

	public void getEpgView(MenuEpg_NEW_UI menuEpg_NEW_UI) {
		this.menuEpg_NEW_UI = menuEpg_NEW_UI;
	}

	public void setChangeChannelListener(ChangeChannelListener changeChannelListener) {
		this.changeChannelListener = changeChannelListener;
	}

	public ChangeChannelListener getChangeChannelListener() {
		return changeChannelListener;
	}

	/**
	 * 设置帮助信息
	 * @param infoId
	 */
	public void setHelpInfo(int infoId) {
		String info = mContext.getResources().getString(infoId);
		help_sureText.setText(info);
	}

	/**
	 * 预约列表取消后更新界面
	 * @param itemId
	 */
	public void setCancelResertAction(int itemId) {
		// TODO Auto-generated method stub
		if (itemId < 0 || listsmart == null || listsmart.size() == 0 || itemId > listsmart.size()) {
			Log.i(TAG, "LL -- >> remove the item id= " + itemId + " out of index");
			return;
		}

		ImageView divider = (ImageView) linear.getChildAt(itemId * 2 + 1);
		if (null != divider) {
			divider.setVisibility(View.GONE);
		}

		if (listsmart.size() - 1 < epgNumEveryPage) {
			//当少于每页个数的时候，删掉一个 则添加一个空板
			ImageView iv_divider = new ImageView(mContext);
			iv_divider.setBackgroundResource(R.drawable.epg_divider);
			linear.addView(iv_divider);

			ImageView bg_board = new ImageView(mContext);
			bg_board.setBackgroundResource(R.drawable.epg_item_smartview_bg_default);
			bg_board.setLayoutParams(smart_num0.getLayoutParams());
			linear.addView(bg_board);
		}

		SmartView smartView = null;

		if (itemId >= 0 && itemId < listsmart.size() - 1) {
			//条目不只一个，取消的不是最后一个
			smartView = listsmart.get(itemId + 1);

		} else if ((listsmart.size() > 1) && (itemId == listsmart.size() - 1)) {
			//取消是最后一个并且不是只有一个条目
			smartView = listsmart.get(itemId - 1);
		} else if (listsmart.size() == 1) {
			//只有一个条目
			Log.i(TAG, "EL -- >> Only one item id= " + itemId * 2 + " \n and the listsmart size is 1");
			Button button = (Button) mView.findViewById(weekBtnIds[curWeekDayIndex]);
			setItemNeedRequest(false);
			button.setFocusable(true);
			button.requestFocus();
			listsmart.remove(itemId);
			addSmartEpgItem(null);
			return;
		}

		if (null != smartView) {

			Log.i(TAG, "LL -- >> remove the item id= " + itemId * 2 + " \n and the listsmart size is " + (listsmart.size() - 1));
			listsmart.remove(itemId);

			for (int id = 0; id < listsmart.size(); id++) {
				SmartView smart = listsmart.get(id);
				smart.setId(id * 2);
			}
			smartView.getReFocus();
		}

	}

	/**
	 * 设置当前选中的ＩＤ（在layout中的ID号 =  在list中的index * 2）
	 * @param id
	 */
	public void setFocusID(int id) {
		// TODO Auto-generated method stub
		curSlectItemID = id;
		if (!isAddOver) {
			//没有加载完成
			addNotInViewsItem();
			//			for(SmartView view: listsmart){
			//				view.setVisibility(View.VISIBLE);
			//		}
		}
	}

}
