package com.changhong.tvos.dtv.userMenu;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.changhong.menudata.menuPageData.MainMenuRootData;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvInterface;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.util.PageIndexView;
import com.changhong.tvos.dtv.util.ViewChannelInfo;
import com.changhong.tvos.dtv.util.ViewChannelSort;
import com.changhong.tvos.dtv.util.ViewDtvProgram;
import com.changhong.tvos.dtv.util.ViewDtvProgramPreview;
import com.changhong.tvos.dtv.util.ViewGridBase.OnKeyActionCallBack;
import com.changhong.tvos.dtv.util.ViewSelfOrder;
import com.changhong.tvos.dtv.util.ViewSortEdit;
import com.changhong.tvos.dtv.util.ViewSortEdit.OnDtvChooseCallBack;
import com.changhong.tvos.dtv.util.ViewSortEdit.OnKeyCallBack;
import com.changhong.tvos.system.commondialog.CommonAcionDialog;
import com.changhong.tvos.system.commondialog.VchCommonToastDialog;

public class MenuSortChannel extends BaseGridMenu implements
		android.view.View.OnClickListener, ViewChannelSort.OnSortChangeCallBack{
	private Handler mHandler = new Handler();
	private ViewChannelSort mSortView;
	private ViewChannelSort mSortView2;
	private List<DtvProgram> sortList;
	private ProgressBar mProgressBar;
	private Button channelEditBtn;
	private Button cancelLastBtn;
	private Button saveBtn;
	private Animation removeAnimation;
	private Animation addAnimation;
	private Animation animationFadeIn;
	private RelativeLayout mView;
	
	private RelativeLayout sortContainer;
	PopupWindow window;
	private MenuManager.listState mListState = null;
	VchCommonToastDialog mToastDialog = null;
	private static final String TAG = "MenuSortChannel";
	private static final int numPreviewList = 9;
	private static final double AnimationSpeed = 3;
	private ScrollView scroll1;
	private ScrollView scroll2;
	private DtvInterface mInterface = DtvInterface.getInstance();
	
	private LinearLayout preList1;
	private LinearLayout preList2;
	
	private int lastListLine;
	private int curListLine;
	private boolean hasChanged;
	private int curListPage;
	private int lastListPage;
	
	private int dtvCellHeight;
	private ViewDtvProgram animView;
	private RelativeLayout preViewLayout;
	private ViewSortEdit editChannelView;
	
	private ViewDtvProgramPreview curSelctView;
	private LayoutInflater appInfView = null;
	private DisplayMetrics mDisplayMetrics;
	private int mShowTime = 60000;
//	private ScrollView editScrollView;
//	private LinearLayout editLinearLayout;
	private Runnable mRunShow;
	private int[] desLocation;
	private boolean isFirst = true;
	private boolean isFirstPre = true;
	private boolean isKeyboardProcessed = false;
	private Runnable indicateWindowShow =  new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			window.dismiss();
		}
		
	};
	
	public MenuSortChannel(Context context) {
		// TODO Auto-generated constructor stub
		super(context, R.style.Theme_ActivityTransparent);
		if(appInfView==null){
			 appInfView = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		mView = (RelativeLayout) appInfView.inflate(R.layout.menu_channel_order, null, false);
//		mView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.menu_channel_order, null);
		setContentView(mView);

		mToastDialog = new VchCommonToastDialog(mContext);

		sortList = new ArrayList<DtvProgram>();
		desLocation = new int[2];
		sortContainer = (RelativeLayout)mView.findViewById(R.id.sort_view_container);//整个排序容器

		this.mListState = MenuManager.listState.channel_List;
		filpper = (ViewFlipper) mView.findViewById(R.id.fliperView);//已有节目列表的flipper
		layout = (RelativeLayout) mView.findViewById(R.id.mainLayout);//已有节目列表的相对布局
		layout2 = (RelativeLayout) mView.findViewById(R.id.mainLayout2);//已有节目列表的相对布局（第二页）
		mSortView = (ViewChannelSort) mView.findViewById(R.id.channel_sort_view);//已有节目列表的视图
		mSortView2 = (ViewChannelSort) mView.findViewById(R.id.channel_sort_view1);//已有节目列表的视图（第二页）
		editChannelView = (ViewSortEdit) mView.findViewById(R.id.sort_edit_Grid);//编辑节目视图 
		
		animView = (ViewDtvProgram) mView.findViewById(R.id.preEditChooseDtv);//预先编辑当前节目动画。。。。。。
//		editScrollView = (ScrollView) findViewById(R.id.sorteditScroll);
//		editLinearLayout = (LinearLayout) findViewById(R.id.sort_vertival_container);
		mSortView.setKeyActionCallBack(new OnKeyActionCallBack() {

			@Override
			public void resetTimer() {

				mHandler.removeCallbacks(mRunShow);
				mHandler.postDelayed(mRunShow, mShowTime);
			}

			@Override
			public void animationAction(int direct) {
				// TODO Auto-generated method stub
				Animation inAnimation = null;
				switch (direct) {
				case KeyEvent.KEYCODE_DPAD_LEFT:
					filpper.setInAnimation(mContext, R.anim.anim_right_in);
					filpper.setOutAnimation(mContext, R.anim.anim_left_out);
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					filpper.setInAnimation(mContext, R.anim.anim_left_in);
					filpper.setOutAnimation(mContext, R.anim.anim_right_out);
					break;
				case KeyEvent.KEYCODE_DPAD_UP:
					filpper.setInAnimation(mContext, R.anim.anim_down_in);
					filpper.setOutAnimation(mContext, R.anim.anim_up_out);
					break;
				case KeyEvent.KEYCODE_DPAD_DOWN:
					filpper.setInAnimation(mContext, R.anim.anim_up_in);
					filpper.setOutAnimation(mContext, R.anim.anim_down_out);

					break;
				default:
					break;
				}

				//为已有节目列表添加动画跳转
				inAnimation = filpper.getInAnimation();
				if (null != inAnimation) {
					inAnimation.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							if (SHOW_LAYOUT1 == witchViewShouldSee) {

								setWitchViewShow(SHOW_LAYOUT2);
//								mSortView2.startChannelChange();

							} else if (SHOW_LAYOUT2 == witchViewShouldSee) {

								setWitchViewShow(SHOW_LAYOUT1);
//								mSortView.startChannelChange();

							}

							mSortView2.setAnimationOk(true);
							mSortView.setAnimationOk(true);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub

							if (SHOW_LAYOUT1 == witchViewShouldSee) {
								layout2.requestFocus();
							} else if (SHOW_LAYOUT2 == witchViewShouldSee) {
								layout.requestFocus();
							}
							setWitchViewShow(SHOW_ALL);
						}
					});
				} else {
					Log.i(TAG, "The out animation is null");
				}

				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						filpper.showNext();
					}
				}, 150);
			}

			@Override
			public void keyActionUp(int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				keyUpAction(keyCode, event);
			}

			@Override
			public void keyActionDown(int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				keyDownAction(keyCode, event);
			}

		});
		mSortView.setSortChangeCallBack(this);

		mSortView2.setSortChangeCallBack(this);
		mSortView2.setKeyActionCallBack(mSortView.getKeyActionCallBack());

		leftArrow = (ImageView) mView.findViewById(R.id.page_pre);
		rightArrow = (ImageView) mView.findViewById(R.id.page_next);
		pageDot = (PageIndexView)mView.findViewById(R.id.page_dot);

		leftArrow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == leftArrow.getId()) {
					mHandler.removeCallbacks(mRunShow);
					mHandler.postDelayed(mRunShow, mShowTime);
					Log.i(TAG, "EL leftArrow.onClick()***");
					keyUpAction(KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL, null);
				}
			}
		});

		rightArrow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "EL leftArrow.onClick()***");
				mHandler.removeCallbacks(mRunShow);
				mHandler.postDelayed(mRunShow, mShowTime);
				keyUpAction(KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR, null);
			}
		});
		setWitchViewShow(SHOW_LAYOUT1);

		//竖线
		mProgressBar = (ProgressBar) mView.findViewById(R.id.sort_progress);
		//编辑，撤销，保存
		channelEditBtn = (Button) mView.findViewById(R.id.channelManager);
		cancelLastBtn = (Button) mView.findViewById(R.id.channelSortQuit);
		saveBtn = (Button) mView.findViewById(R.id.channelSave);
		saveBtn.setNextFocusDownId(saveBtn.getId());
		
		preList1 = (LinearLayout) mView.findViewById(R.id.prelist1);//预览排序第一行布局
		preList2 = (LinearLayout) mView.findViewById(R.id.prelist2);//预览排序第二行布局
		
		scroll1 = (ScrollView) mView.findViewById(R.id.sort_list_scroll1);//预览排序第一行
		scroll2 = (ScrollView) mView.findViewById(R.id.sort_list_scroll2);//预览排序第二行
		
		//点击编辑后的预览布局
		preViewLayout = (RelativeLayout) mView.findViewById(R.id.preShowLayout);
		curSelctView = (ViewDtvProgramPreview) mView.findViewById(R.id.curChooseDtv);
		
		addAnimation = AnimationUtils.loadAnimation(mContext,R.anim.wave_scale);//缩小效果
		removeAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);//淡出效果
		
		initPreList();

		saveBtn.setOnClickListener(this);
		cancelLastBtn.setOnClickListener(this);
		channelEditBtn.setOnClickListener(this);
		
//		channelEditBtn.setEnabled(false);
//		cancelLastBtn.setEnabled(false);
		/**
		 * 编辑视图
		 */
		editChannelView.setKeyCallBack(new OnKeyCallBack() {
			
			@Override
			public void onBack() {
				// TODO Auto-generated method stub
				isFirstPre = false;
//				setCompatBackGround(mView, mContext.getResources().getDrawable(R.drawable.sort_bg));
				sortContainer.setVisibility(View.VISIBLE);
				preViewLayout.setVisibility(View.GONE);
				channelEditBtn.requestFocus();
				
				updatePrelist();
			}

			@Override
			public void onkeyDown(int keyCode) {
				// TODO Auto-generated method stub
				
				mHandler.removeCallbacks(mRunShow);
				mHandler.postDelayed(mRunShow, mShowTime);
			}

		});
		
		//编辑视图中的动画
		editChannelView.setDtvCallBack(new OnDtvChooseCallBack() {
			
			@Override
			public void onChooseCallBack(int[] itemLocation,final DtvProgram program,int action) {
				// TODO Auto-generated method stub
				switch(action){
					case ACTION_SHOW:{
						int[] des = getLocationByCurChooseItemPosition(itemLocation);
						curSelctView.init(program);
						curSelctView.setLocation(des);
						animView.init(program);
						curSelctView.setVisibility(View.VISIBLE);
						Animation move = getAnimationShow(itemLocation, des, new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation arg0) {
								// TODO Auto-generated method stub
								animView.setVisibility(View.VISIBLE);
							}
							
							@Override
							public void onAnimationRepeat(Animation arg0) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationEnd(Animation arg0) {
								// TODO Auto-generated method stub
								animView.setVisibility(View.GONE);
							}
						});
						animView.startAnimation(move);
						animationFadeIn = new AlphaAnimation(0.0f,1f);
						animationFadeIn.setDuration((long) (move.getDuration() * 1.5));
						animationFadeIn.setAnimationListener(new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation arg0) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationRepeat(Animation arg0) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationEnd(Animation arg0) {
								// TODO Auto-generated method stub
								curSelctView.setVisibility(View.VISIBLE);
							}
						});
						curSelctView.startAnimation(animationFadeIn);
					}
						break;
					case ACTION_DISS:{
						int[] src = new int[2];
						curSelctView.getLocationInWindow(src);
//						animView.init(program);
						Animation move = getAnimationShow(src, itemLocation, new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation arg0) {
								// TODO Auto-generated method stub
								animView.setVisibility(View.VISIBLE);
							}
							
							@Override
							public void onAnimationRepeat(Animation arg0) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationEnd(Animation arg0) {
								// TODO Auto-generated method stub
								animView.setVisibility(View.GONE);
							}
						});
						animView.startAnimation(move);
						curSelctView.setVisibility(View.GONE);
					}
						break;
				}
			}
		});
		
		mRunShow = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//不需要自动退出 刘珊珊
//				onCancel();
			}
			
		};

		//编辑视图中频道焦点改变触发
		editChannelView.setOnFocusChangeListener(new OnFocusChangeListener() {
			boolean isFirst = true;
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1 && isFirst){
					isFirst = false;
					int[] location = new int[2];
					editChannelView.getLocationInWindow(location);
					location[1] = (int) (mDisplayMetrics.heightPixels / 2 - 125 * mDisplayMetrics.scaledDensity);
					Log.d(TAG, "show indication at y:" + location[1]);
					showIndicate(editChannelView, location, R.string.dtv_channel_sort_edit);
					//按OK键选中节目，移动光标，将选择的节目插入到光标所在的位置
				}
			}
		});
		
		//编辑按钮焦点改变触发
		channelEditBtn.setOnFocusChangeListener(new OnFocusChangeListener() {
			boolean isFirst = true;
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1 && isFirst){
					isFirst = false;
					showIndicate(arg0, null, R.string.dtv_channel_sort_modify);//微调节目顺序
				}
			}
		});
		//保存按钮焦点改变触发
		saveBtn.setOnFocusChangeListener(new OnFocusChangeListener() {
			boolean isFirst = true;
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1 && isFirst){
					isFirst = false;
					showIndicate(arg0, null, R.string.dtv_channel_sort_save);//保存退出
				}
			}
		});
		//撤销按钮焦点改变触发
		cancelLastBtn.setOnFocusChangeListener(new OnFocusChangeListener() {
			boolean isFirst = true;
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1 && isFirst){
					isFirst = false;
					showIndicate(arg0, null, R.string.dtv_channel_sort_cancel);//依次取消已排节目
				}
			}
		});
	}

	/**
	 * 排序预览界面获取选中微调节目的位置
	 * @param itemLocation
	 * @return
	 */
	protected int[] getLocationByCurChooseItemPosition(int[] itemLocation) {
		// TODO Auto-generated method stub
		
		int[] des = new int[2];
		int[] origin = new int[2];
		editChannelView.getLocationInWindow(origin);
		/*
		 *  2 | 1
		 *  3 | 4
		 */
		if(itemLocation[0] <= origin[0] + editChannelView.getWidth() / 2
				&& itemLocation[1] >= origin[1] + editChannelView.getHeight()/ 3 ){
			
			//第3象限
			des[0] = itemLocation[0] + 5 * animView.getWidth() / 4;
			des[1] = origin[1] + editChannelView.getHeight()  +   animView.getHeight() / 3;
			curSelctView.setBackGround(mContext.getResources().getDrawable(R.drawable.menu_sort_edit_board_3));
			Log.i(TAG, "getLocationByCurChooseItemPosition>> third section");
		}else if(itemLocation[0] <= origin[0] + editChannelView.getWidth() / 2
				&& itemLocation[1] < origin[1] + editChannelView.getHeight()/ 3 ){
			//第2象限
			des[0] = itemLocation[0] + 5 * animView.getWidth() / 4;
			des[1] = origin[1]  -  4 * animView.getHeight() / 3;
			curSelctView.setBackGround(mContext.getResources().getDrawable(R.drawable.menu_sort_edit_board_1));
			Log.i(TAG, "getLocationByCurChooseItemPosition>> second section");

		}else if(itemLocation[0] > origin[0] + editChannelView.getWidth() / 2
				&& itemLocation[1] >= origin[1] + editChannelView.getHeight()/ 3 ){
			//第4象限
			des[0] = itemLocation[0] - 5 * animView.getWidth() / 4;
			des[1] = origin[1] + editChannelView.getHeight()  +    animView.getHeight() / 3;
			curSelctView.setBackGround(mContext.getResources().getDrawable(R.drawable.menu_sort_edit_board_4));
			Log.i(TAG, "getLocationByCurChooseItemPosition>> fourth section");

		}else{//第1象限
			des[0] = itemLocation[0] - 5 * animView.getWidth() / 4;
			des[1] = origin[1]  -  4 * animView.getHeight() / 3;
			curSelctView.setBackGround(mContext.getResources().getDrawable(R.drawable.menu_sort_edit_board_2));
			Log.i(TAG, "getLocationByCurChooseItemPosition>> first section");

		}
		
		return des;
	}


	protected void showHelpInfoById(View arg0, int alpha0) {
		// TODO Auto-generated method stub
		
	}

	//初始化底部预约列表
	private void initPreList(){
		mDisplayMetrics = mContext.getResources().getDisplayMetrics();
		LayoutParams layoutparam = new LayoutParams(mDisplayMetrics.widthPixels/numPreviewList, LayoutParams.WRAP_CONTENT);
		for(int i = 0; i < numPreviewList; i++){
			ViewDtvProgram dtvCell = new ViewDtvProgram(mContext);
			dtvCell.setSortNum(i + 1);
			dtvCell.setBackGround(null);
			preList1.addView(dtvCell,layoutparam);
		}
		
		for(int i = 0; i < numPreviewList; i++){
			ViewDtvProgram dtvCell = new ViewDtvProgram(mContext);
			dtvCell.setSortNum(i + numPreviewList + 1);
			preList2.addView(dtvCell,layoutparam);
			dtvCell.setBackGround(null);
		}
		animView.setBackGround(null);
		curSelctView.setBackGround(null);
	}

	/**
	 * 显示结果
	 */
	@Override
	public void show() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
		mMenuManager.init(mListState);
		sortList.clear();
		ViewChannelInfo.isNeedShow = true;
		setShowTV(false);
		if (getWitchViewShouldSee() == SHOW_LAYOUT1) {

			mSortView.init(mMenuManager, mListState);
			mSortView.requestFocus();
			super.show();
			mSortView2.init(mMenuManager, mListState);
			setPreShowAndNextShow(mSortView2, mSortView);
			this.updatePageView();
		} else {

			mSortView2.init(mMenuManager, mListState);
			super.show();
			mSortView.init(mMenuManager, mListState);
			setPreShowAndNextShow(mSortView, mSortView2);
			this.updatePageView();
			mSortView2.requestFocus();
		}
		
	}

	/**
	 * 检测按键弹起事件
	 */
	@Override
	public void keyUpAction(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_GREEN:
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SL:
			if (getWitchViewShouldSee() == SHOW_LAYOUT1) {
				setPreShowAndNextShow(mSortView, mSortView2);
				slidShowNext(ViewSelfOrder.DIRECTION_LEFT);
			} else {
				setPreShowAndNextShow(mSortView2, mSortView);
				slidShowNext(ViewSelfOrder.DIRECTION_LEFT);
			}

			break;
		case KeyEvent.KEYCODE_YELLOW:
		case KeyEvent.KEYCODE_CHANGHONGIR_FLCK_SR:
			if (getWitchViewShouldSee() == SHOW_LAYOUT1) {
				setPreShowAndNextShow(mSortView, mSortView2);
				slidShowNext(ViewSelfOrder.DIRECTION_RIGHT);
			} else {
				setPreShowAndNextShow(mSortView2, mSortView);
				slidShowNext(ViewSelfOrder.DIRECTION_RIGHT);
			}

			break;
		default:
			break;

		}
	}

	/**
	 * 检测按键按下事件
	 */
	@Override
	public void keyDownAction(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
//////////////////////////////////////cuixiaoyan 2014-10-20 //////////////////
	isKeyboardProcessed = false;
		switch (event.getScanCode()) {
		case 231://keyboard Menu
		case 233://keyboard Channel Up
		case 234://keyboard Channel Down
			break;
		case 232://keyboard Source 
			keyCode = KeyEvent.KEYCODE_DPAD_CENTER;
			isKeyboardProcessed = true;
			Log.i(TAG, "keyDownAction convert source_key to enter!");
			super.onKeyDown(keyCode, event);
			break;
		case 235://keyboard Volume Down
			keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
			Log.i(TAG, "keyDownAction convert volume_up_key to left!");
			super.onKeyDown(keyCode, event);
			isKeyboardProcessed = true;
			break;
		case 236://keyboard Volume Up
			keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
			Log.i(TAG, "keyDownAction convert volume_up_key to right!");
			isKeyboardProcessed = true;
			super.onKeyDown(keyCode, event);
			break;
		default:
			break;
		}
/////////////////////////////////////////////////////////////////////////////////		
		switch (keyCode) {

		// case KeyEvent.KEYCODE_DPAD_LEFT:
		// case KeyEvent.KEYCODE_DPAD_RIGHT:
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (getWitchViewShouldSee() == SHOW_LAYOUT1) {
				//////////////////////add by YangLiu 2014-12-02/////////////////////
				/**
				 * 解决按住向下方向键不动，排序列表焦点丢失问题
				 */
				channelEditBtn.setFocusable(false);
				cancelLastBtn.setFocusable(false);
				saveBtn.setFocusable(false);
				scroll1.setFocusable(false);
				scroll2.setFocusable(false);
				if (mSortView.getCurPage() != mSortView.getLastPage()) {
					setPreShowAndNextShow(mSortView, mSortView2);
					showNext(keyCode);
				}
			} else {
				/**
				 * 解决按住向下方向键不动，排序列表焦点丢失问题
				 */
				channelEditBtn.setFocusable(false);
				cancelLastBtn.setFocusable(false);
				saveBtn.setFocusable(false);
				scroll1.setFocusable(false);
				scroll2.setFocusable(false);
				if (mSortView2.getCurPage() != mSortView2.getLastPage()) {
					setPreShowAndNextShow(mSortView2, mSortView);
					showNext(keyCode);
				}
			}

			break;

//		case KeyEvent.KEYCODE_SOURCE:    ////cuixiaoyan 2014-10-20 ////////
			// case KeyEvent.KEYCODE_CHANGHONGIR_TOOLBAR:
		case 4126:// KEYCODE_CHANGHONGIR_TOOLBAR
		case 170:// KEYCODE_CHANGHONGIR_TV
		case 4135:
			setShowTV(true);
		case KeyEvent.KEYCODE_MENU:
		case KeyEvent.KEYCODE_BACK:
//			break;
		default:
			break;
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	/**
	 * 监听右侧功能键
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		/*
		 * cancelBtn = (Button) findViewById(R.id.channelSortQuit); saveBtn =
		 * (Button) findViewById(R.id.channelSortExit); previewBtn = (Button)
		 * findViewById(R.id.channelSortSer);
		 */
		switch (arg0.getId()) {
		case R.id.channelSortQuit: // 取消上一个
			if(null == sortList || sortList.size() == 0){
				Log.i(TAG, "NO more dtv in sortlist");
//				CommonmToastDialog toast = new CommonmToastDialog(mContext);
//				toast.setMessage(R.string.dtv_channel_sort_none);
//				toast.show();
				mToastDialog.setMessage(R.string.dtv_channel_sort_none);
				mToastDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
				mToastDialog.getWindow().setType(2003);
				mToastDialog.show();
				return;
			}
			onSortCancel(sortList.get((sortList.size() - 1)));//取消最后一个新加入的节目
			/**
			 * 解决让撤销按钮能够一直聚焦问题		
			 */			
			mSortView.setFocusable(false);
			////////////////////////add By YangLiu  2014-11-27//////////////////
			
			break;
		case R.id.channelSave: // 保存并退出
			save();
			dismiss();
			
			break;
		case R.id.channelManager: // 启动编辑界面

			if(null == sortList || sortList.size() == 0){
				Log.i(TAG, "NO more dtv in sortlist");
//				CommonmToastDialog toast = new CommonmToastDialog(mContext);
//				toast.setMessage(R.string.dtv_channel_sort_none);
//				toast.show();
				mToastDialog.setMessage(R.string.dtv_channel_sort_none);
				mToastDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
				mToastDialog.getWindow().setType(2003);
				mToastDialog.show();
				return;
			}
			showPreView();
			break;
		default:
			break;
		}
	}

	/**
	 * 显示预览编辑界面
	 */
	public void showPreView(){
		
		curSelctView.setVisibility(View.GONE);//当前选中的节目视图隐藏curSelctView
		dtvCellHeight = preList1.getChildAt(0).getHeight();
		preViewLayout.setVisibility(View.VISIBLE);//预览布局显现preViewLayout
		sortContainer.setVisibility(View.GONE);//节目列表容器隐藏sortContainer
		
		int height = dtvCellHeight * ((sortList.size() -1) /  numPreviewList + 1);
//		editChannelView.setLayoutParams(new LayoutParams(
//				LayoutParams.MATCH_PARENT, 
//				height));
		Log.i(TAG, "dtvCellHeight= " + dtvCellHeight + "  height= " + height);
//		setCompatBackGround(mView, mContext.getResources().getDrawable(R.drawable.sort_ser_bg));
//		mView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.sort_ser_bg));
		preViewLayout.requestFocus();
//		editScrollView.requestFocus();
		editChannelView.requestFocus();
		editChannelView.init(sortList);
	}
	
	/**
	 * 将要排序的节目添加到下方的列表中
	 */
	@Override
	public void onSortAdd(int[] sourceLocation,final DtvProgram program) {
		// TODO Auto-generated method stub
		Log.i(TAG, "on sortAdd:" + program);
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
		int addPosition = sortList.size();
		sortList.add(program);
		
		if(sortList.size() == 1){
			showIndicate(preList2.getChildAt(2), null, R.string.dtv_channel_sort_list);//显示提示信息
		}
		updatePreviewListPageIndex();
		
		 ViewDtvProgram dtvcell = null;
		int addLine = getOpertatorLineByPosition(addPosition);//表示是偶数还是奇数行
		Log.i(TAG, "add postion is " + addPosition + " and line:" + addLine);
		
			switch(addLine){
			case 0:
				dtvcell = (ViewDtvProgram)preList1.getChildAt(addPosition % (numPreviewList << 1));
				for(int i = 0; i< numPreviewList; i++){//numPreviewList=9
					((ViewDtvProgram)preList2.getChildAt(i)).reset();
				}
				break;
			case 1:
				dtvcell = (ViewDtvProgram)preList2.getChildAt(addPosition % (numPreviewList));
				dtvcell.setImageAble(true);
				break;
			}
		
		dtvcell.getLocationInWindow(desLocation);
		final ViewDtvProgram cell = dtvcell;
		Animation trans = getAnimationShow(sourceLocation, desLocation, new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				animView.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				animView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}});
		animView.init(program);
		animView.startAnimation(trans);
		
		if(lastListPage != curListPage){ //18的倍数时候,需要翻页了
			Log.i(TAG, "update pageviews");
			resetAllPreViews();
		}
		
		
		
		animationFadeIn = new AlphaAnimation(0.8f,1f);
		animationFadeIn.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				cell.init(program);
			}
		});
		animationFadeIn.setDuration(trans.getDuration());
		cell.startAnimation(animationFadeIn);//创建跳下动画
		
		if(mSortView.getChannelList().size() == 0){//当将所有节目排序后，直接使保存焦点聚焦
			saveBtn.requestFocus();
		}
	}

	/**
	 * 根据选择节目的位置和将要添加排序的位置来创建一个动画
	 * @param sourceLocation
	 * @param desLocation
	 * @param listener
	 * @return
	 */
	private Animation getAnimationShow(int[] sourceLocation, int[] desLocation, AnimationListener listener) {
		// TODO Auto-generated method stub
		Animation anim = new TranslateAnimation(sourceLocation[0], desLocation[0],sourceLocation[1], desLocation[1]);
		double distance = Math.pow((desLocation[0] - sourceLocation[0]), 2) +  Math.pow((desLocation[1] - sourceLocation[1]), 2);
		int duration = (int) (Math.sqrt(distance) / AnimationSpeed);
		anim.setInterpolator(new AccelerateDecelerateInterpolator());
		anim.setDuration(duration);
		anim.setAnimationListener(listener);
		
		return anim;
 	}


	/**
	 * 翻页处理，重置排序列表所有节目
	 */
	private void resetAllPreViews() {
		// TODO Auto-generated method stub
		Log.d(TAG, "Enter resetAllPreViews");
		for(int i = 0; i< numPreviewList; i++){
			((ViewDtvProgram)preList1.getChildAt(i)).reset();
			((ViewDtvProgram)preList2.getChildAt(i)).reset();
			((ViewDtvProgram)preList2.getChildAt(i)).setImageAble(false);
			((ViewDtvProgram)preList1.getChildAt(i)).setSortNum(i + curListLine * numPreviewList + 1);
			((ViewDtvProgram)preList2.getChildAt(i)).setSortNum(i + (curListLine+ 1) * numPreviewList +1);
		}
	}

	/**
	 * 更新退出预览编辑后的排序视图
	 */
	private void updatePrelist(){
		int lastPageSortCounts = sortList.size() % (numPreviewList << 1);
		for(int i = 0; i < lastPageSortCounts && i < numPreviewList; i++){
			((ViewDtvProgram)preList1.getChildAt(i)).init(sortList.get(i + curListPage * (numPreviewList << 1)));
		}
		
		if(lastPageSortCounts > numPreviewList){
			for(int i = numPreviewList; i < lastPageSortCounts; i++){
				((ViewDtvProgram)preList2.getChildAt(i - numPreviewList)).init(sortList.get(i + curListPage * (numPreviewList << 1)));
			}
		}
		
	}
	
	/**
	 * 更新排序视图的行、页
	 */
	private void updatePreviewListPageIndex(){
		lastListLine = curListLine;
		lastListPage = curListPage;
		curListLine = (sortList.size() - 1) / numPreviewList;
		curListPage = (sortList.size() - 1) / (numPreviewList << 1);
	}
	
	/**
	 * 获取排序视图的所在行
	 * @param position
	 * @return
	 */
	private int getOpertatorLineByPosition(int position){
		return  (((position / numPreviewList) %  2) == 0) ? 0 : 1;
	}

	@Override
	public void onSortCancel(DtvProgram program) {
		// TODO Auto-generated method stub
		if (null == program) {
			return;
		}
		
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
		
		Log.i(TAG, "on sortremove:" + program);
		sortList.remove(program);
		
        updatePreviewListPageIndex();
        int removePosition = sortList.size();
        int line = getOpertatorLineByPosition(removePosition);
        
        Log.i(TAG, "onSortCancel dtvCell in line:" + line);
        ViewDtvProgram dtvcell = null;
        switch(line){
        case 0:
        	dtvcell = (ViewDtvProgram)preList1.getChildAt(removePosition % (numPreviewList  << 1));

        	break;
        case 1:
        	dtvcell = (ViewDtvProgram)preList2.getChildAt(removePosition % (numPreviewList));

        	break;
        }
        
        dtvcell.reset();
        Log.i(TAG, "onSortCancel dtvCell reset");
        if(lastListPage != curListPage){//需要翻页了
        	for(int i = 0; i< numPreviewList; i++){
    			((ViewDtvProgram)preList1.getChildAt(i)).init(sortList.get(i + curListPage * (numPreviewList << 1)));
    			((ViewDtvProgram)preList1.getChildAt(i)).setSortNum(i + curListPage * (numPreviewList << 1) + 1);
    			((ViewDtvProgram)preList2.getChildAt(i)).init(sortList.get(i + curListPage * (numPreviewList << 1)+  numPreviewList) );
    			((ViewDtvProgram)preList2.getChildAt(i)).setSortNum(i + curListPage * (numPreviewList << 1) + 1 + numPreviewList);
    		}
        }
        dtvcell.startAnimation(removeAnimation);
		if (getWitchViewShouldSee() == SHOW_LAYOUT1) {
			mSortView.addProgram(program);
		} else {
			mSortView2.addProgram(program);
		}
	}

	public ViewChannelSort getCurShowView() {
		if (getWitchViewShouldSee() == SHOW_LAYOUT1) {
			return mSortView;
		}
		if (getWitchViewShouldSee() == SHOW_LAYOUT2) {
			return mSortView2;
		}

		return null;
	}

	@Override
	public void updatePageView() {
		if(0 == getCurShowView().getMaxPage()){
			mProgressBar.setProgress(100);
		}else{
			
			mProgressBar.setProgress(getCurShowView().getCurPage() * 100
					/ getCurShowView().getMaxPage());
		}
	}
	
	
	/**
	 * 保存节目编辑
	 */
	public void save(){
		if(null != sortList && sortList.size() !=0){
			Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:"+mContext.getResources().getString(R.string.collect1)+"|subClass:"+mContext.getResources().getString(R.string.collect2)+"|reportInfo:menu="+mContext.getResources().getString(R.string.collect3)+";item1="+mContext.getResources().getString(R.string.dtv_Mainmenu_ProgramManager)+";item2="+mContext.getResources().getString(R.string.dtv_ProgramManager_ManualSort));
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
					sortList.addAll(mSortView.getChannelList());
				
					for(int i=0; i< sortList.size(); i++){
						sortList.get(i).setProgramNum(i+1);
					}
					
					Log.i(TAG, "sortList.size() is" + sortList.size());
					mMenuManager.setExchangeList(sortList);
//				}
//			}).start();
			
			lastListPage = curListPage = 0;
			lastListLine = curListLine = 0;
			resetAllPreViews();
			
//			CommonmToastDialog toast = new CommonmToastDialog(mContext);
//			toast.setMessage(R.string.dtv_channel_sort_save_ok);
//			toast.show();
			mToastDialog.setMessage(R.string.dtv_channel_sort_save_ok);
			mToastDialog.info_layout.setBackgroundResource(R.drawable.epg_prompt_bg);
			mToastDialog.getWindow().setType(2003);
			mToastDialog.show();
		}
		
	}


	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		Log.i(TAG, "MenuSort onKeyDown: keycode" + keyCode);
		
		mHandler.removeCallbacks(mRunShow);
		mHandler.postDelayed(mRunShow, mShowTime);
/////////////////////////////////////cuixiaoyan 2014-10-20 ////////////
	isKeyboardProcessed = false;
		switch (event.getScanCode()) {
		case 231://keyboard Menu
			/*添加当在手动排序界面按本机和遥控器的菜单键时一次退出菜单*/
			onCancel();			
			MainMenuRootData.mMainMenu.setMenukeyUseless(false);
			MainMenuRootData.mMainMenu.destroy();			
			break;
			//////////////add By YangLiu 2014-12-1//////////////
		case 233://keyboard Channel Up
		case 234://keyboard Channel Down
			break;
		case 232://keyboard Source 
			keyCode = KeyEvent.KEYCODE_DPAD_CENTER;
			isKeyboardProcessed = true;
			Log.i(TAG, "onKeyDown convert source_key to enter!");
			break;
		case 235://keyboard Volume Down
			keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
			Log.i(TAG, "onKeyDown convert volume_up_key to left!");
			isKeyboardProcessed = true;
			break;
		case 236://keyboard Volume Up
			keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
			Log.i(TAG, "onKeyDown convert volume_up_key to right!");
			isKeyboardProcessed = true;
			break;
		default:
			break;
		}
/////////////////////////////////////////////////////////////////////////
		switch(keyCode){
		/**
		 * 解决让撤销按钮能够一直聚焦问题
		 */
		case KeyEvent.KEYCODE_DPAD_LEFT://键值为21  左键
			/////////////////////add By YangLiu 2014-11-27////////////////
			mSortView.setFocusable(true);
			break;
		
		case KeyEvent.KEYCODE_DPAD_RIGHT://键值为22  左键
			/////////////////add by YangLiu 2014-12-02////////////////
			/**
			 * 解决按住向下方向键不动，排序列表焦点丢失问题
			 */
			channelEditBtn.setFocusable(true);
			cancelLastBtn.setFocusable(true);
			saveBtn.setFocusable(true);
			scroll1.setFocusable(true);
			scroll2.setFocusable(true);
			break;
		case KeyEvent.KEYCODE_BACK:
			if(preViewLayout.isShown()){
				preViewLayout.setVisibility(View.GONE);
				return true;
			}else{
				onCancel();
				return true;
			}
		case KeyEvent.KEYCODE_MENU:
			onCancel();
			/*添加当在手动排序界面按本机和遥控器的菜单键时一次退出菜单*/
			MainMenuRootData.mMainMenu.setMenukeyUseless(false);
			MainMenuRootData.mMainMenu.destroy();
			//////////////add By YangLiu 2014-12-1//////////////
			break;
			
		case 170:// KEYCODE_CHANGHONGIR_TV
		case KeyEvent.KEYCODE_CHANGHONGIR_TV:
			setShowTV(true);
			onCancel();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	public void dismiss(){
		
		mHandler.removeCallbacks(indicateWindowShow);
		mHandler.removeCallbacks(mRunShow);
		super.dismiss();
		
	}
	
	
	/**
	 * 重构节目列表的长度，因为有节目节目列表长度有加减操作
	 */
	@Override
	public int getCurListSize(){
		return mSortView.getChannelList().size();
	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#cancel()
	 */
	
	public void onCancel() {
		// TODO Auto-generated method stub
		if(sortList == null || sortList.size() == 0) 
			{
				dismiss();
				return;
			}
		final CommonAcionDialog  confirmDialog = new CommonAcionDialog(mContext, R.string.no_string,0,0,30);//dtv_scan_setup_waring
		confirmDialog.getWindow().setType(2003);
		confirmDialog.setMessage(R.string.dtv_channel_sort_saveornot);
		confirmDialog.setShowTV(true);
		confirmDialog.setCancelButtonListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				confirmDialog.setShowTV(false);
				confirmDialog.dismiss();
				sortContainer.setVisibility(View.VISIBLE);
				preViewLayout.setVisibility(View.GONE);
				MenuSortChannel.this.dismiss();
			}
		});
	
		confirmDialog.setOKButtonListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				confirmDialog.dismiss();
			}
		});
		
		confirmDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				if(confirmDialog.isShowTV()){
					sortContainer.setVisibility(View.VISIBLE);
					preViewLayout.setVisibility(View.GONE);
					save();
					MenuSortChannel.this.dismiss();
				}
			}
		});
		confirmDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
				confirmDialog.setShowTV(false);
			}
		});
//		confirmDialog.setIcons(drawableL, drawableT, drawableR, drawabled);
//		confirmDialog.setBackGround(mContext.getResources().getDrawable(R.drawable.dialog_sort_confirm_bg));
		confirmDialog.setCancelable(true);
		confirmDialog.setDefaultFocusButton(CommonAcionDialog.FOCUS_BUTTON_OK);
		confirmDialog.setDuration(30);
		confirmDialog.setOkButtonText(R.string.yes);
		confirmDialog.setCancelButtonText(R.string.no);
		confirmDialog.show();
		
	}
	
	
	public void setCompatBackGround(View view,Drawable drawable) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackground(drawable);
		} else {
			view.setBackgroundDrawable(drawable);

		}
	}
	
	public void showIndicate(View anchor,int[] location, int msgId){
		showIndicate(anchor,location,mContext.getResources().getString(msgId));
	}
	
	public void showIndicate(View anchor,int[] location, String msg){
		if(null == window){
			window = new PopupWindow(mContext);
	    
			window.setBackgroundDrawable(null);
	    View contentView = null;
	    contentView= LayoutInflater.from(mContext).inflate(R.layout.channel_sort_indicate_bg, null);
	    contentView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
	            LayoutParams.WRAP_CONTENT));
	    //设置PopupWindow显示和隐藏时的动画
	    window.setAnimationStyle(android.R.style.Animation_Dialog);
	    //设置PopupWindow的大小（宽度和高度）
	    window.setHeight((int) (96 * mDisplayMetrics.scaledDensity));
	    window.setWidth((int) (215 * mDisplayMetrics.scaledDensity));
	    //设置PopupWindow的内容view
	    window.setContentView(contentView);
	    //设置PopupWindow外部区域是否可触摸
	    window.setFocusable(false);
	    window.setOutsideTouchable(true);
		}
		
		if(window.isShowing()){
			window.dismiss();
		}
		
		if(null == location){
			location = new int[2];
			anchor.getLocationInWindow(location);
		}
		TextView msgText = (TextView) window.getContentView().findViewById(R.id.msg_indicate);
		msgText.setText(msg);
		window.showAtLocation(mView, Gravity.LEFT|Gravity.TOP, location[0], location[1] - window.getContentView().getHeight());
//		window.showAsDropDown(anchor, 0, -window.getContentView().getHeight()- anchor.getHeight());
		mHandler.removeCallbacks(indicateWindowShow);
		if(anchor.getId() == R.id.sort_edit_Grid){
			mHandler.postDelayed(indicateWindowShow, 6000);
		}else{
			
		mHandler.postDelayed(indicateWindowShow, 3000);
	}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		
		if(isFirst  && hasFocus){
			isFirst = false;
			showIndicate(mSortView, null, R.string.dtv_channel_sort_add);
		}
	}
}
