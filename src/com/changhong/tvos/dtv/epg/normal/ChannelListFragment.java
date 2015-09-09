package com.changhong.tvos.dtv.epg.normal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.epg.normal.AsyncImageLoader.ImageCallback;
import com.changhong.tvos.dtv.menuManager.MenuManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvInterface;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ChannelListFragment extends Fragment {

	protected static final String TAG = "ChannelListFragment";
	public boolean isChannelListFragmentFocuse = true;// add by YangLiu
	public EpgListView mListView;
	private Context mContext;
	private TextView mTitleView;

	public static List<DtvProgram> mChannelList;// 2015-3-19
	public static List<DtvProgram> mCommonList;// 2015-3-24

	public static String[] mTypes;// 2015-3-19
	public static ArrayList<LiveHotWiki> mBWiKiList;// 2015-3-19
	private List<DtvProgram> mTypeList;
	public static List<DtvProgram> mHDList;
	public static List<DtvProgram> mCCTVList;
	public static List<DtvProgram> mTVList;
	public static List<DtvProgram> mOtherList;

	private DtvProgram mCurChannel;
	private ChannelListAdpter mChanneldapter;
	private LiveHotListdapter mHotListdapter;

	@SuppressWarnings("unused")
	private ChannelType mChannelType;
	private String type = "hot";
	public int mlastItemSelectPosition = 0;
	private boolean isExit = false;
	private boolean isCanFoucse = false;
	public static boolean hdflag = false;

	private ViewGroup mRecommendView;
	private TextView mRecommendName;
	private ImageView mRecommendImage;

	ChannelListSelectedListener mChannelListSelected;

	public static Handler handler;
	public static Runnable runnable;
	public static boolean isTimerUp = false;

	public interface ChannelListSelectedListener {
		public void onChannelListSelected(DtvProgram data);
		// public void setIsHasHDChannels(boolean isHasHD); 2015-3-19
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.epg_normal_channel_list_layout, container, false);
		mListView = (EpgListView) view.findViewById(R.id.channellist);
		mTitleView = (TextView) view.findViewById(R.id.title);
		mRecommendView = (ViewGroup) view.findViewById(R.id.recommend);
		mRecommendName = (TextView) view.findViewById(R.id.recommend_content);
		mRecommendImage = (ImageView) view.findViewById(R.id.recommend_image);
		return view;// Fragment中必须被返回
	}

	@Override
	// 当fragment添加到activity中时，会调用fragment的方法onAttach()，这个方法中适合检查activity是否实现了OnArticleSelectedListener接口，检查方法就是对传入的activity的实例进行类型转换
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mChannelListSelected = (ChannelListSelectedListener) activity;// 得到监听器对象实例
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString());
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// getDataThread();// 初始化显示频道列表信息
		// getLiveHotTop();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = EpgActivity.getEpgActivity();// change by YangLiu 2015-2-12

		isExit = false;

		//全部频道列表
		if (mChannelList == null || mChannelList.size() <= 0) {
			getChannelList();
			Log.i(TAG, "getChannelList------->第一次取得频道列表长度为：" + mChannelList.size());
		}

		//当前播放频道
		mCurChannel = DtvChannelManager.getInstance().getCurProgram();// 获取当前频道信息

		//常用频道列表
		if (checkTimer() || mCommonList == null || mCommonList.size() <= 0) {
			getCommonList();
			isTimerUp = false;
			Log.i(TAG, "getCommonList------->第一次取得常用列表长度为：" + mCommonList.size());
		}

		//别名库
		if (DtvChannelManager.mBaseChannelList == null || DtvChannelManager.mBaseChannelList.size() <= 0) {
			DtvChannelManager.getBaseChannels(mContext);
			Log.i(TAG, "getBaseChannels------->第一次取得别名库长度为：" + DtvChannelManager.mBaseChannelList.size());
		}

		//频道分类
		if (mTypes == null || mTypes.length <= 0) {
			getChannelTypeList();
			Log.i(TAG, "getChannelTypeList------->第一次取得分类表长度为：" + mTypes.length);
		}
	}

	/**
	 * 取得所有频道列表
	 */
	public static void getChannelList() {
		mChannelList = new ArrayList<DtvProgram>();
		mChannelList = MenuManager.getInstance().getChannelListByAllchFlag();// change by YangLiu 2015-1-4 全部节目开关
	}

	/**
	 * 检测更新常用列表时间是否到时
	 * @return
	 */
	private boolean checkTimer() {
		if (handler == null) {
			handler = new Handler();
		}
		if (runnable == null) {
			runnable = new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//	Log.i(TAG, "测试定时器，1分钟后执行此操作");
					isTimerUp = true;
					handler.postDelayed(this, 1000 * 60);// 60秒是延时时长
				}
			};
			handler.postDelayed(runnable, 1000 * 60);// 打开定时器，执行操作 延时3秒，使其延时刷新常用节目库
		}
		return isTimerUp;
	}

	/**
	 * 取得常用频道列表
	 */
	public static void getCommonList() {
		int curChannelType = DtvChannelManager.getInstance().getCurChannelType();
		List<DtvProgram> mCommons = DtvInterface.getInstance().getWatchedChannelList(curChannelType);// 2015-3-20 修改常用频道
		mCommonList = new ArrayList<DtvProgram>();
		if (mCommons != null && mCommons.size() > 0) {
			//	Log.i(TAG, "watchedChannelList.size=" + mCommons.size());
			int num = 0;
			if (mCommons.size() >= 5) {
				num = 5;
			} else {
				num = mCommons.size();
			}

			for (int i = 0; i < num; i++) {
				mCommonList.add(mCommons.get(i));
			}
		}
	}

	/**
	 * 解决：关闭全部节目然后手动排序后，再次打开全部节目，频道列表只显示过滤后的列表的问题 
	 * add By YangLiu 2014-12-1
	 */
	/*private List<DtvProgram> getChannelListSmartly() {
		if (DtvChannelManager.getInstance().GetAllchFlag()) {
			MenuManager.getInstance().init(MenuManager.listState.channel_List);
			mChannelList = DtvChannelManager.getInstance().getChannelList();
		} else {
			mChannelList = MenuManager.getInstance().getWatchedChannelList();//取得搜索观看过的频道列表 
		}
		return mChannelList;
	}*/

	/**
	 * 取得所有频道的类型列表
	 */
	public static void getChannelTypeList() {
		mTypes = new String[mChannelList.size()];

		for (int i = 0; i < mChannelList.size(); i++) {
			String name = mChannelList.get(i).getProgramName();
			//	Log.i(TAG, "mChannelList.mProgram="+name);
			boolean flag = false;

			if (DtvChannelManager.mBaseChannelList != null && DtvChannelManager.mBaseChannelList.size() > 0) {
				//	Log.i(TAG, "mBaseChannelList.size="+mBaseChannelList.size());
				for (int j = 0; j < DtvChannelManager.mBaseChannelList.size(); j++) {
					//	Log.i(TAG, "mBaseChannelList.mProgram name="+mBaseChannelList.get(j).getName());
					if (name.equals(DtvChannelManager.mBaseChannelList.get(j).getName())) {
						String type = DtvChannelManager.mBaseChannelList.get(j).getType();
						//	Log.i(TAG, "mBaseChannelList type=" + type);

						if (!"cctv".equals(type) && !"tv".equals(type)) {
							if ((name.indexOf("高清") != -1) || (name.toLowerCase().indexOf("hd") != -1)) {
								//	Log.i(TAG, "1高清频道为：" + name);
								mTypes[i] = "hd";
								hdflag = true;
							} else {
								mTypes[i] = "other";
							}
						} else {
							if ((name.indexOf("高清") != -1) || (name.toLowerCase().indexOf("hd") != -1)) {
								//	Log.i(TAG, "2高清频道为：" + name);
								mTypes[i] = "hd";
								hdflag = true;
							} else {
								mTypes[i] = type;
							}
						}
						//	Log.i(TAG, type);

						flag = true;
						break;
					}
				}
				if (!flag) {
					if ((name.indexOf("高清") != -1) || (name.toLowerCase().indexOf("hd") != -1)) {
						//	Log.i(TAG, "3高清频道为：" + name);
						mTypes[i] = "hd";
						hdflag = true;
					} else {
						mTypes[i] = "other";
					}
					//	Log.i(TAG, mTypes[i] + "++++++++");
				}
			} else {
				if ((name.indexOf("高清") != -1) || (name.toLowerCase().indexOf("hd") != -1)) {
					//	Log.i(TAG, "4高清频道为：" + name);
					mTypes[i] = "hd";
					hdflag = true;
				} else {
					mTypes[i] = "other";
				}
			}
		}
		// mChannelListSelected.setIsHasHDChannels(hdflag); 2015-3-19
	}

	/**
	 * 取得指定类型的频道列表
	 * @param type
	 * @return
	 */
	private List<DtvProgram> getTypeChannelList(String type) {
		mTypeList = new ArrayList<DtvProgram>();
		/**分类显示**/
		if ("hd".equals(type)) {
			if (mHDList == null || mHDList.size() <= 0) {
				mHDList = new ArrayList<DtvProgram>();
				for (int i = 0; i < mTypes.length; i++) {
					if (type.equals(mTypes[i])) {
						mHDList.add(mChannelList.get(i));
					}
				}
				Log.i(TAG, "getTypeChannelList--->mHDList--->第一次取得高清列表长度为：" + mHDList.size());
			}
			mTypeList = mHDList;

		} else if ("cctv".equals(type)) {
			if (mCCTVList == null || mCCTVList.size() <= 0) {
				mCCTVList = new ArrayList<DtvProgram>();
				for (int i = 0; i < mTypes.length; i++) {
					if (type.equals(mTypes[i])) {
						mCCTVList.add(mChannelList.get(i));
					}
				}
				Log.i(TAG, "getTypeChannelList--->mCCTVList--->第一次取得CCTV列表长度为：" + mCCTVList.size());
			}
			mTypeList = mCCTVList;

		} else if ("tv".equals(type)) {
			if (mTVList == null || mTVList.size() <= 0) {
				mTVList = new ArrayList<DtvProgram>();
				for (int i = 0; i < mTypes.length; i++) {
					if (type.equals(mTypes[i])) {
						mTVList.add(mChannelList.get(i));
					}
				}
				Log.i(TAG, "getTypeChannelList--->mTVList--->第一次取得TV列表长度为：" + mTVList.size());
			}
			mTypeList = mTVList;

		} else if ("other".equals(type)) {
			if (mOtherList == null || mOtherList.size() <= 0) {
				mOtherList = new ArrayList<DtvProgram>();
				for (int i = 0; i < mTypes.length; i++) {
					if (type.equals(mTypes[i])) {
						mOtherList.add(mChannelList.get(i));
					}
				}
				Log.i(TAG, "getTypeChannelList--->mOtherList--->第一次取得其他列表长度为：" + mOtherList.size());
			}
			mTypeList = mOtherList;

		} else {
			Log.i(TAG, "can't find type's  channel");
		}

		Log.i(TAG, "mTypeList size= " + mTypeList.size());
		if (mTypes.length > mChannelList.size()) {//当分类频道列表比全部频道列表长时return全部频道列表
			return mChannelList;
		}
		return mTypeList;
	}

	/**
	 * 取得所有类型的频道列表
	 */
	public static void getAllTypeChannelList() {
		mHDList = new ArrayList<DtvProgram>();
		mCCTVList = new ArrayList<DtvProgram>();
		mTVList = new ArrayList<DtvProgram>();
		mOtherList = new ArrayList<DtvProgram>();

		for (int i = 0; i < mTypes.length; i++) {
			if ("hd".equals(mTypes[i])) {
				mHDList.add(mChannelList.get(i));

			} else if ("cctv".equals(mTypes[i])) {
				mCCTVList.add(mChannelList.get(i));

			} else if ("tv".equals(mTypes[i])) {
				mTVList.add(mChannelList.get(i));

			} else if ("other".equals(mTypes[i])) {
				mOtherList.add(mChannelList.get(i));

			} else {
				Log.i(DtvChannelManager.YL, "没有找到当前类型");
			}
		}

		Log.i(DtvChannelManager.YL, "重新刷新的高清列表长度为：" + mHDList.size());
		Log.i(DtvChannelManager.YL, "重新刷新的CCTV列表长度为：" + mCCTVList.size());
		Log.i(DtvChannelManager.YL, "重新刷新的TV列表长度为：" + mTVList.size());
		Log.i(DtvChannelManager.YL, "重新刷新的其他列表长度为：" + mOtherList.size());
	}

	/**
	 * 一、显示频道列表：
	 */
	private void display() {
		List<DtvProgram> mTypeChannelList = new ArrayList<DtvProgram>();// 2015-3-25

		if (!type.equals("common")) {
			getTypeChannelList(type);
			mChanneldapter = new ChannelListAdpter((Activity) mContext, mTypeList, mListView);
			mTypeChannelList = mTypeList;// 2015-3-25
		} else {
			mChanneldapter = new ChannelListAdpter((Activity) mContext, mCommonList, mListView);
			mTypeChannelList = mCommonList;// 2015-3-25
		}

		mListView.setAdapter(mChanneldapter);
		mListView.setCacheColorHint(Color.TRANSPARENT);

		// 选中频道列表的条目，加载频道列表数据
		mListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (type.equals("common")) {
					if (mChannelList.get(arg2).mProgramNum >= 0) {

						mChannelListSelected.onChannelListSelected(mCommonList.get(arg2));// 调用EpgActivity中的接口实现方法

						if (mChanneldapter != null)
							mChanneldapter.notifyDataSetChanged();

						mlastItemSelectPosition = arg2;
					}
				} else {
					if (mTypeList != null && mTypeList.size() >= (arg2 + 1) && mTypeList.get(arg2) != null) {
						mChannelListSelected.onChannelListSelected(mTypeList.get(arg2));

						if (mChanneldapter != null)
							mChanneldapter.notifyDataSetChanged();

						mlastItemSelectPosition = arg2;
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		// 点击频道列表的条目，触发频道跳转
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

				if (type.equals("common")) {
					MenuManager.getInstance().changeChannelByProgramServiceIndex(mCommonList.get(arg2).getProgramServiceIndex());// 更换频道
				} else {
					MenuManager.getInstance().changeChannelByProgramServiceIndex(mTypeList.get(arg2).getProgramServiceIndex());// 更换频道
				}
				mCurChannel = DtvChannelManager.getInstance().getCurProgram();
			}
		});

		// 频道列表的条目是否有焦点，触发字体大小改变
		mListView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub

				if (!type.equals("hot")) {
					isChannelListFragmentFocuse = hasFocus; // add By YangLiu
					ChannelListAdpter mAdapter = (ChannelListAdpter) mListView.getAdapter();

					//					Log.i(TAG, "position+" + mListView.getSelectedItemPosition());
					mAdapter.changListState(hasFocus, mListView.getSelectedItemPosition());
				}
				if (hasFocus) {// 有焦点时，标题布局大，字体大，透明度高

					/*
					 * RelativeLayout.LayoutParams layout = new
					 * RelativeLayout.LayoutParams(
					 * RelativeLayout.LayoutParams.WRAP_CONTENT,
					 * RelativeLayout.LayoutParams.WRAP_CONTENT);
					 * layout.setMargins(dip2px(mContext, 50), dip2px(mContext,
					 * 40), 0, 0); mTitleView.setLayoutParams(layout);
					 */

					mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
					mTitleView.setAlpha(1);
				} else {// 没焦点时，标题布局小，字体小，透明度低
					if (!type.equals("hot")) {

						/*
						 * RelativeLayout.LayoutParams layout = new
						 * RelativeLayout.LayoutParams(
						 * RelativeLayout.LayoutParams.WRAP_CONTENT,
						 * RelativeLayout.LayoutParams.WRAP_CONTENT);
						 * layout.setMargins(dip2px(mContext, 56),
						 * dip2px(mContext, 56), 0, 0);
						 * mTitleView.setLayoutParams(layout);
						 */

						mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
						mTitleView.setAlpha((float) 0.4);
					}
				}
			}
		});

		Message message = mHandler.obtainMessage(3, mTypeChannelList);
		mHandler.sendMessageDelayed(message, 500); // 延时显示会有明显的跳变，应该立即显示 2015-3-24 YangLiu
		// mHandler.sendMessage(message);
	}

	/**
	 * 二、显示热门列表
	 * @param mHotWikis
	 */
	private void displayHotList(final ArrayList<LiveHotWiki> mHotWikis) {
		mHotListdapter = new LiveHotListdapter((Activity) mContext, mHotWikis, mListView);
		mListView.setAdapter(mHotListdapter);
		getFocuse();
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				MenuManager.getInstance().changeChannelByProgramServiceIndex(mHotWikis.get(arg2).getIndex());// 更换频道
				mCurChannel = DtvChannelManager.getInstance().getCurProgram();

				// lyw add 2015年2月10日17:01:16
				DtvChannelManager.getInstance().setViewSource(DtvChannelManager.ReportCurChannelType.LocalEpg);
				Log.i("liuyuwang", "epg热门推荐换台 " + DtvChannelManager.getInstance().getViewSource());

				// 数据上报 YangLiu
				Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1_Intelligent_guide) + "|subClass:"
						+ mContext.getResources().getString(R.string.collect2_dtv) + "|reportInfo:item=" + mContext.getResources().getString(R.string.collect3_epg_hot) + ";value="
						+ mContext.getResources().getString(R.string.popular_program));
			}
		});
		mListView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					if (mHotListdapter != null)
						mHotListdapter.changFocuse(true);
				}

				if (type.equals("hot")) {
					if (hasFocus) {
						mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
						mTitleView.setAlpha(1);
					} else {
						mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
						mTitleView.setAlpha((float) 0.4);
					}
				}
			}
		});

		Message message = mHandler.obtainMessage(4, mHotWikis);
		mHandler.sendMessageDelayed(message, 500);
	}

	/**
	 * 三、显示直播关联点播
	 * @param rSource
	 */
	private void displayRecommend(final RecommendSource rSource) {
		if (rSource != null) {
			if (rSource.getName() != null && type.equals("hot")) {
				mRecommendName.setText(rSource.getName());
				mRecommendView.setVisibility(View.VISIBLE);
				mListView.setNextFocusUpId(R.id.recommend);
				Log.i(TAG, "show see full story poster");
			}

			if (rSource.getPic() != null) {
				String poster = (String) (rSource.getPic());
				final ImageView imageView = mRecommendImage;
				imageView.setTag(poster);

				Drawable cachedImage = AsyncImageLoader.getInstance().loadDrawable(poster, new ImageCallback() {
					public void imageLoaded(Drawable imageDrawable, String imageUrl) {
						// 解决全部剧情海报加载慢问题
						/*
						 * ImageView imageViewByTag = (ImageView)
						 * mListView.findViewWithTag(imageUrl); if
						 * (imageViewByTag != null && imageDrawable != null) {
						 * imageViewByTag.setImageDrawable(imageDrawable); }
						 */

						if (imageDrawable != null) {
							imageView.setImageDrawable(imageDrawable);// 2015-3-20
						}
					}
				});

				if (cachedImage == null) {
					imageView.setImageResource(R.drawable.movie_default);
				} else {
					imageView.setImageDrawable(cachedImage);
				}
			}

			mRecommendView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.d(TAG, "recommend poster is clicked");
					if (rSource != null) {

						try {
							if (!isTencentAPK()) {
								/***********************************TVMall******************************/
								Intent mIntent = new Intent();
								Log.i(TAG, "直播关联点播的有关参数=" + "/ngetApk=" + rSource.getApk() + "/ngetActivity=" + rSource.getActivity() + "/ngetId=" + rSource.getId() + "/getType=" + rSource.getType());
								ComponentName componetName = new ComponentName(rSource.getApk(), rSource.getActivity());
								mIntent.putExtra("POSTER_TAG", rSource.getId());
								mIntent.putExtra("POSTER_CODE_TAG", rSource.getType());
								mIntent.setComponent(componetName);
								mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								mContext.startActivity(mIntent);

							} else {
								/***********************************Tencent******************************/
								Intent intent = new Intent();
								String uri = rSource.getPlayUri();
								intent.setData(Uri.parse(uri + "&pull_from=100303"));
								intent.setAction("com.tencent.qqlivetv.open");
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								PackageManager packageManager = mContext.getPackageManager();
								List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
								boolean isIntentSafe = activities.size() > 0;
								Log.d(TAG, "Find if exist tencent apk, the reslut is isIntentSafe=" + isIntentSafe);
								if (isIntentSafe) {
									mContext.startActivity(intent);
								} else {
									Log.d(TAG, "There is no tencent apk, please install one");
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// 数据上报 YangLiu
						Log.d("CH_ER_COLLECT", "reportType:normal|saveType:append|sort:" + mContext.getResources().getString(R.string.collect1_Intelligent_guide) + "|subClass:"
								+ mContext.getResources().getString(R.string.collect2_dtv) + "|reportInfo:item=" + mContext.getResources().getString(R.string.collect3_epg_hot) + ";value="
								+ mContext.getResources().getString(R.string.all_show));
					}
				}
			});

			mRecommendView.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub

					if (hasFocus) {
						if (mHotListdapter != null)
							mHotListdapter.changFocuse(false);
					}

					if (type.equals("hot")) {
						if (hasFocus) {
							mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
							mTitleView.setAlpha(1);
						} else {
							mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
							mTitleView.setAlpha((float) 0.4);
						}
					}
				}
			});
		}
	}

	/**
	 * Handler
	 */
	/**********************************************************************************************/
	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case 0:
				if (!type.equals("hot")) {//显示除当前热门和常用的其他类型频道
					display();
				}
				break;
			case 1:
				if (type.equals("hot")) {//显示当前热门频道
					displayHotList((ArrayList<LiveHotWiki>) message.obj);
				}
				break;
			case 2:
				displayRecommend((RecommendSource) message.obj);//显示全部剧情
				break;

			case 3:
				initChannelListSelect((List<DtvProgram>) message.obj);//初始化其他各类频道列表
				selsectCurChannel();
				break;
			case 4:
				initHotListSelect((ArrayList<LiveHotWiki>) message.obj);//初始化当前热门频道列表
				break;
			}
		}
	};

	/**********************************************************************************************/

	// 获取当前正在播放的频道
	public void selsectCurChannel() {

		if (type.equals("common")) {
			if (mCommonList != null && mCommonList.size() > 0) {
				int num = mCurChannel.getProgramNum();
				// Log.i(TAG,"num000= "+num);//下标从0开始，当前前面的频道序号
				boolean flag = false;
				for (int i = 0; i < mCommonList.size(); i++) {
					if (mCommonList.get(i).getProgramNum() == num) {
						mListView.setSelection(i);
						//						Log.i(TAG, "select" + i);
						flag = true;
						break;
					}
				}
				if (!flag) {
					mListView.setSelection(0);
					//					Log.i(TAG, "select" + 0);
				}

				if (mChanneldapter != null)
					mChanneldapter.notifyDataSetChanged();
			}
		} else {
			if (mTypeList != null && mTypeList.size() > 0) {
				int num = mCurChannel.getProgramNum();
				// Log.i(TAG,"num000= "+num);//下标从0开始，当前前面的频道序号
				boolean flag = false;
				for (int i = 0; i < mTypeList.size(); i++) {
					if (mTypeList.get(i).getProgramNum() == num) {
						mListView.setSelection(i);
						//						Log.i(TAG, "select" + i);
						flag = true;
						break;
					}
				}
				if (!flag) {
					mListView.setSelection(0);
					//					Log.i(TAG, "select" + 0);
				}

				if (mChanneldapter != null)
					mChanneldapter.notifyDataSetChanged();
			}
		}
	}

	// mHandler 3 使频道显示第一个
	private void initChannelListSelect(List<DtvProgram> mTypeChannelList) {
		if (mTypeChannelList != null && mTypeChannelList.size() > 0) {
			mListView.setSelection(0);
			if (mChanneldapter != null)
				mChanneldapter.notifyDataSetChanged();
		}
	}

	// mHandler 4 使热门显示第一个
	private void initHotListSelect(ArrayList<LiveHotWiki> mHotWikis) {
		if (mHotWikis != null && mHotWikis.size() > 0) {
			mListView.setSelection(0);
			if (mHotListdapter != null)
				mHotListdapter.notifyDataSetChanged();
		}
	}

	// 2.获取频道业务数据
	public void getDataThread(final ChannelType cType) {
		this.mChannelType = cType;
		this.type = cType.getType();
		mTitleView.setText(cType.getName());
		mListView.setAdapter(null);
		mRecommendView.setVisibility(View.GONE);

		if (type.equals("hot")) {
			mListView.setNextFocusRightId(R.id.channellist);
			if (mBWiKiList != null && mBWiKiList.size() > 0) {
				//				Log.i(TAG, "热门个数：mBWiKiList" + mBWiKiList.size());
				Message message = mHandler.obtainMessage(1, mBWiKiList);// 1：显示热门数据
				mHandler.sendMessage(message);
			}
		} else {
			mListView.setNextFocusRightId(R.id.programlist);
		}

		if (mRecommendView.getVisibility() == View.VISIBLE) {
			mListView.setNextFocusUpId(R.id.recommend);
		} else {
			mListView.setNextFocusUpId(R.id.channellist);
		}

		new Thread() {
			public void run() {
				/*
				 * try { //DtvChannelManager.getInstance().getChannelList();
				 * mChannelList
				 * =MenuManager.getInstance().getWatchedChannelList(); } catch
				 * (Exception e) { // TODO: handle exception }
				 */
				if (!isExit) {
					if (mChannelList != null && mChannelList.size() > 0) {
						if (!type.equals("hot")) {
							Message message = mHandler.obtainMessage(0);
							mHandler.sendMessage(message);// 0：显示本地EPG数据
							// Log.i(TAG, "直接发送消息，已有频道数为:"+mChannelList.size());
						} else {
							getWikiData();
						}
					} else {
						DtvProgram mDtvProgram = new DtvProgram();
						mDtvProgram.setProgramNum(0);
						mDtvProgram.setProgramName("暂无数据");
						mChannelList.add(mDtvProgram);// 加载所有频道信息

						Message message = mHandler.obtainMessage(0);
						mHandler.sendMessage(message);// 0：显示本地EPG数据
						// Log.i(TAG,"先加载频道列表在发送消息,已有频道数为:"+mChannelList.size());
					}
				}
			}
		}.start();
	}

	public synchronized void getLiveHotTop() {

		// if (networkTest(mContext)) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				getWikiData();
			}
		}).start();
		// }
	}

	/**
	 * 1. 获取热门推荐数据
	 * 
	 * @return
	 */
	/*******************************************************************************/
	private String creatPostParam() {

		String postParame = null;
		try {
			JSONObject object = new JSONObject();
			object.put("action", "GetWikiLiveHot");

			JSONObject developer = new JSONObject();
			developer.put("apikey", "HZP9DZMA");
			developer.put("secretkey", "197d2dc3226786eb2377995f8c58e1df");
			object.put("developer", developer);

			JSONObject device = new JSONObject();
			device.put("dnum", "123456");
			object.put("device", device);

			JSONObject user = new JSONObject();
			user.put("userid", "123456");
			object.put("user", user);

			JSONObject param = new JSONObject();

			param.put("cover_type", "big");
			// param.put("showprogram", "yes");
			object.put("param", param);

			postParame = object.toString();
			Log.i("String", object.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return postParame;
	}

	private String getWikiData() {

		// mBWiKiList = new ArrayList<LiveHotWiki>();
		String url = "http://www.epg.huan.tv/json2";
		HttpPost request = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();
		String param = creatPostParam();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jsonstr", param));
		HttpResponse response;
		String errorCode = null;
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String in = EntityUtils.toString(response.getEntity(), "UTF-8");
				//	Log.i("in1", in);

				JSONObject object = new JSONObject(in);
				errorCode = getResultData(object);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errorCode;
	}

	private String getResultData(JSONObject object) {

		JSONObject error = object.optJSONObject("error");
		String errorCode = error.optString("code");
		Log.i("errorCode", errorCode);
		String errorMsg = error.optString("info");
		Log.i("errorMsg", errorMsg);

		if ("0".equals(errorCode)) {
			ArrayList<LiveHotWiki> mWiKiList = new ArrayList<LiveHotWiki>();
			LiveHotWiki mWiki = null;
			ArrayList<LiveHotProgram> mProgramList = null;
			LiveHotProgram mProgram = null;
			JSONArray wikis = object.optJSONArray("wikis");

			Log.i(TAG, "欢网取得的热门节目数：" + wikis.length());
			for (int i = 0; i < wikis.length(); i++) {
				mWiki = new LiveHotWiki();
				JSONObject wiki = (JSONObject) wikis.opt(i);
				String wiki_id = wiki.optString("wiki_id");
				String model = wiki.optString("model");
				String wiki_title = wiki.optString("wiki_title");
				String wiki_cover = wiki.optString("wiki_cover");
				String wiki_content = wiki.optString("wiki_content");
				String hot = wiki.optString("hot");
				mWiki.setWikiId(wiki_id);
				mWiki.setModel(model);
				mWiki.setWikiTitle(wiki_title);
				mWiki.setWikiCover(wiki_cover);
				mWiki.setWikiContent(wiki_content);
				mWiki.setHot(hot);
				Log.i("wiki_id", wiki_id);
				Log.i("model", model);
				Log.i("wiki_title", wiki_title);
				Log.i("wiki_cover", wiki_cover);
				Log.i("wiki_content", wiki_content);
				Log.i("hot", hot);

				JSONArray programs = (JSONArray) wiki.opt("programs");
				mProgramList = new ArrayList<LiveHotProgram>();
				boolean isCheck = true;
				boolean isExist = false;
				for (int j = 0; j < programs.length(); j++) {
					mProgram = new LiveHotProgram();
					JSONObject program = (JSONObject) programs.opt(j);
					String name = program.optString("name");
					String channel_name = program.optString("channel_name");
					String channel_code = program.optString("channel_code");
					String start_time = program.optString("start_time");
					String end_time = program.optString("end_time");
					mProgram.setName(name);
					mProgram.setChannelName(channel_name);
					mProgram.setChannelCode(channel_code);
					mProgram.setStartTime(start_time);
					mProgram.setEndTime(end_time);
					mProgramList.add(mProgram);
					if (isCheck) {
						int index = isChannelExist(channel_code);
						if (index != -1) {
							mWiki.setIndex(index);
							isExist = true;
							isCheck = false;
						}
					}

					Log.i("name", name);
					Log.i("channel_name", channel_name);
					Log.i("channel_code", channel_code);
					Log.i("start_time", start_time);
					Log.i("end_time", end_time);
					mProgram = null;
				}
				mWiki.setPrograms(mProgramList);
				mProgramList = null;
				if (isExist) {
					mWiKiList.add(mWiki);
				}
				mWiki = null;
			}
			mBWiKiList = mWiKiList;
			Message message = mHandler.obtainMessage(1, mWiKiList);// 1：显示热门数据
			mHandler.sendMessage(message);

			getEPGData();
		}
		return errorCode;
	}

	private int isChannelExist(String code) {
		int index = -1;
		if (DtvChannelManager.mBaseChannelList != null && DtvChannelManager.mBaseChannelList.size() > 0) {
			for (int i = 0; i < DtvChannelManager.mBaseChannelList.size(); i++) {
				if (code.equals(DtvChannelManager.mBaseChannelList.get(i).getCode())) {
					index = DtvChannelManager.mBaseChannelList.get(i).getIndex();
					break;
				}
			}
			return index;
		}
		return index;
	}

	/*******************************************************************************/
	/**
	 * 2. 上报本地热门数据
	 * 
	 * @return
	 */
	/*******************************************************************************/
	private String creatEPGPostParam() {
		String postParame = null;
		try {
			String channelcode = null;
			mCurChannel = DtvChannelManager.getInstance().getCurProgram();
			if (mCurChannel == null) {
				return "";
			} else {
				String channelname = mCurChannel.mProgramName;
				if (channelname != null && channelname != "" && DtvChannelManager.mBaseChannelList != null && DtvChannelManager.mBaseChannelList.size() > 0) {

					for (int i = 0; i < DtvChannelManager.mBaseChannelList.size(); i++) {
						if (channelname.equals(DtvChannelManager.mBaseChannelList.get(i).getName())) {
							channelcode = DtvChannelManager.mBaseChannelList.get(i).getCode();
							break;
						}
					}
				}
			}

			JSONObject object = new JSONObject();

			object.put("action", "GetProgramByChannels");

			JSONObject developer = new JSONObject();
			developer.put("apikey", "HZP9DZMA");
			developer.put("secretkey", "197d2dc3226786eb2377995f8c58e1df");
			object.put("developer", developer);

			JSONObject device = new JSONObject();
			device.put("dnum", "123456");
			object.put("device", device);

			JSONObject user = new JSONObject();
			user.put("userid", "123456");
			object.put("user", user);

			JSONObject param = new JSONObject();
			// param.put("cover_type", "big");
			// param.put("showprogram", "yes");

			JSONArray channel = new JSONArray();
			if (channelcode != null) {
				channel.put(channelcode);
			} else {
				channel.put("cctv1");
			}
			param.put("channel_codes", channel);

			object.put("param", param);

			postParame = object.toString();
			Log.i("String", object.toString());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return postParame;
	}

	private String getEPGData() {

		String url = "http://www.epg.huan.tv/json2";
		HttpPost request = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();
		String param = creatEPGPostParam();
		if (param == null) {
			return null;
		}
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jsonstr", param));
		HttpResponse response;
		String errorCode = null;
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				String in = EntityUtils.toString(response.getEntity(), "UTF-8");

				Log.i("in1", in);
				JSONObject object = new JSONObject(in);
				errorCode = getEPGResultData(object);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errorCode;
	}

	private String getEPGResultData(JSONObject object) {

		JSONObject error = null;
		String errorCode = null;
		try {
			error = object.optJSONObject("error");
			errorCode = error.optString("code");
			Log.i("errorCode", errorCode);
			String errorMsg = error.optString("info");
			Log.i("errorMsg", errorMsg);
		} catch (Exception e) {
			// TODO: handle exception
		}

		if ("0".equals(errorCode)) {
			ArrayList<LiveHotWiki> mWiKiList = new ArrayList<LiveHotWiki>();
			LiveHotWiki mWiki = null;
			JSONArray wikis = object.optJSONArray("programs");

			for (int i = 0; i < wikis.length(); i++) {
				mWiki = new LiveHotWiki();
				JSONObject wiki = (JSONObject) wikis.opt(i);
				String wiki_id = wiki.optString("wiki_id");
				String model = wiki.optString("model");
				String wiki_title = wiki.optString("wiki_title");

				String wiki_director = wiki.optString("wiki_director");
				String wiki_starring = wiki.optString("wiki_starring");
				String wiki_host = wiki.optString("wiki_host");
				String wiki_guest = wiki.optString("wiki_guest");
				String tags = wiki.optString("tags");

				mWiki.setWikiId(wiki_id);
				mWiki.setModel(model);
				mWiki.setWikiTitle(wiki_title);

				mWiki.setWikiDirector(wiki_director);
				mWiki.setWikiStarring(wiki_starring);
				mWiki.setWikiHost(wiki_host);
				mWiki.setWikiGuest(wiki_guest);
				mWiki.setTags(tags);

				try {
					Log.i("wiki_id", wiki_id);
					Log.i("model", model);
					Log.i("wiki_title", wiki_title);

					if (wiki_director != null)
						Log.i("wiki_director", wiki_director.toString());
					if (wiki_starring != null)
						Log.i("wiki_starring", wiki_starring.toString());
					if (wiki_host != null)
						Log.i("wiki_host", wiki_host.toString());
					if (wiki_guest != null)
						Log.i("wiki_guest", wiki_guest.toString());
					if (tags != null)
						Log.i("tags", tags.toString());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			mWiKiList.add(mWiki);
			mWiki = null;

			if (mWiKiList != null && mWiKiList.size() > 0) {
				if (!isTencentAPK()) {
					/************************TVMall*********************/
					getRecommendDatafromCloud(mWiKiList.get(0));

				} else {
					/************************Tencent*********************/
					getRecommendDatafromTencent(mWiKiList.get(0));
				}
			}
		}
		return errorCode;
	}

	/**
	 * 判断是否使用腾讯APK
	 * @return
	 */
	private boolean isTencentAPK() {
		return true;
	}

	/*******************************************************************************/
	/**
	 * 3. 获取直播关联数据	TVMall
	 * 
	 * @param liveHotWiki
	 * @return
	 */
	/*******************************************************************************/
	private String creatRecommendPostParamforCloud(LiveHotWiki liveHotWiki) {
		String postParame = null;
		try {
			JSONObject object = new JSONObject();
			JSONObject client = new JSONObject();
			client.put("agent_name", "aabbcc");
			client.put("agent_ver", "1.0.327");
			client.put("device", "TV");
			object.put("client", client);

			JSONArray programname = new JSONArray();

			JSONObject name = new JSONObject();
			name.put("name", liveHotWiki.getWikiTitle());
			name.put("type", liveHotWiki.getModel());

			programname.put(name);

			JSONObject dibingList = new JSONObject();
			dibingList.put("dibingList", programname);

			object.put("searchWord", dibingList.toString());

			object.put("version", "0");

			postParame = object.toString();
			Log.i("String", postParame);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return postParame;
	}

	private String getRecommendDatafromCloud(LiveHotWiki liveHotWiki) {
		// String url ="http://182.140.244.133:8080/cloud/services?appKey=p8q4tr&method=ch.tvmall.resource.dibbing&v=3&format=json";
		String url = "http://cloud.smart-tv.cn/cloud/services?appKey=p8q4tr&method=ch.tvmall.resource.dibbing&v=3&format=json";

		HttpPost request = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();
		String param = creatRecommendPostParamforCloud(liveHotWiki);

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jsonstr", param));
		HttpResponse response;
		String errorCode = null;
		try {
			StringEntity entity = new StringEntity(param.toString(), "utf-8");// 解决中文乱码问题
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			request.setEntity(entity);

			response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				String in = EntityUtils.toString(response.getEntity(), "GB2312");

				JSONObject object = new JSONObject(in);
				getRecommendResultDatafromCloud(object);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errorCode;
	}

	private void getRecommendResultDatafromCloud(JSONObject object) {

		RecommendSource rSource = new RecommendSource();
		try {
			if ("TVMALL_DIBBING".equals(object.optString("serviceName"))) {
				JSONArray content = object.optJSONArray("content");
				Log.i("content", content.toString());
				if (content != null && !content.equals("") && !(content.toString()).equals("[]")) {
					JSONObject player = content.optJSONObject(0).optJSONObject("player");

					String name = content.optJSONObject(0).optString("name");
					Log.i("name", name);
					String activity = player.optString("activity");
					Log.i("activity", activity);
					String apk = player.optString("apk");
					Log.i("apk", apk);

					JSONArray parameters = player.optJSONArray("parameters");

					for (int i = 0; i < parameters.length(); i++) {
						JSONObject p = (JSONObject) parameters.get(i);
						if (("POSTER_TAG").equals(p.opt("name"))) {

							String id = p.optString("value");
							Log.i("id", id);
							rSource.setId(id);
						}
						if (("POSTER_CODE_TAG").equals(p.opt("name"))) {
							String type = p.optString("value");
							Log.i("type", type);
							rSource.setType(type);
						}
					}
					String pic = content.optJSONObject(0).optString("pic");
					Log.i("pic", pic);

					rSource.setActivity(activity);
					rSource.setApk(apk);
					rSource.setName(name);
					rSource.setPlayUri("null");
					rSource.setPic(pic);

					Message message = mHandler.obtainMessage(2, rSource);
					mHandler.sendMessage(message);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/*******************************************************************************/
	/**
	 * 3. 获取直播关联数据	Tencent
	 * 
	 * @param liveHotWiki
	 * @return	根据欢网返回的节目数据 创建向腾讯请求参数
	 */
	private String creatRecommendPostParamforTencent(LiveHotWiki liveHotWik) {

		String postParame = "";
		JSONObject object = new JSONObject();

		JSONArray query_listObject = new JSONArray();
		JSONObject queryObject = new JSONObject();
		if (liveHotWik != null) {
			try {
				if (liveHotWik.getModel().endsWith("film")) {
					queryObject.put("category", "1"); //类型1：电影
				} else if (liveHotWik.getModel().endsWith("teleplay")) {
					queryObject.put("category", "2");//类型2：电视剧
				} else if (liveHotWik.getModel().endsWith("television")) {
					JSONArray typeArray = new JSONArray();
					typeArray.put("1");
					typeArray.put("2");
					queryObject.put("xcategory", typeArray);//类型非：其他（非1/2，即非电影/电视剧）
				}
				queryObject.put("name", liveHotWik.getWikiTitle());
				queryObject.put("director", liveHotWik.getWikiDirector());
				queryObject.put("cast", liveHotWik.getWikiStarring());
				queryObject.put("host", liveHotWik.getWikiHost());

				query_listObject.put(queryObject);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		try {
			object.put("format", "json");
			object.put("query_list", query_listObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		postParame = object.toString();
		Log.d(TAG, "the content of now program send to tencent is" + postParame);
		return postParame;
	}

	/**
	 * 得到腾讯返回的结果
	 * @param param
	 * @return
	 */
	private String getRecommendDatafromTencent(LiveHotWiki liveHotWiki) {
		//		String tencentURL = "http://1.tv.t002.ottcn.com/i-tvbin/qtv_video/search/get_search_live_video?Q-UA=QV%3D1%26PR%3DVIDEO%26PT%3DCH%26CHID%3D10009%26RL%3D1920*1080%26VN%3D3.0.0%26VN_CODE%3D120%26SV%3D4.4.2%26DV%3DMiBOX2%26VN_BUILD%3D0&guid=95c60a8d505a0d308b59facbe05d7bfe";//测试
		String tencentURL = "http://tv.t002.ottcn.com/i-tvbin/qtv_video/search/get_search_live_video?Q-UA=QV%3D1%26PR%3DVIDEO%26PT%3DCH%26CHID%3D10009%26RL%3D1920*1080%26VN%3D3.0.0%26VN_CODE%3D120%26SV%3D4.4.2%26DV%3DMiBOX2%26VN_BUILD%3D0&guid=95c60a8d505a0d308b59facbe05d7bfe"; //正式
		HttpPost request = new HttpPost(tencentURL);
		HttpClient client = new DefaultHttpClient();

		String param = creatRecommendPostParamforTencent(liveHotWiki);
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jsonstr", param));
		HttpResponse response;
		String tencentData = null;
		try {
			StringEntity entity = new StringEntity(param.toString(), "utf-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			request.setEntity(entity);
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String in = EntityUtils.toString(response.getEntity(), "GB2312");
				//				Log.d(TAG, in);

				JSONObject object = new JSONObject(in);
				getRecommendResultDatafromTencent(object);// 7.解析腾讯返回结果的信息，得到关联的节目
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tencentData;
	}

	/**
	 * 解析腾讯返回结果信息，得到关联的节目
	 * @param object
	 * @return
	 */
	private void getRecommendResultDatafromTencent(JSONObject jsonObject) {
		RecommendSource rSource = new RecommendSource();

		try {
			JSONObject dataObject = jsonObject.getJSONObject("data");
			if (dataObject != null) {
				JSONArray query_listArray = dataObject.getJSONArray("list");
				if (query_listArray != null && query_listArray.length() > 0) {
					//					for (int i = 0 ; i < query_listArray.length() ; i ++){
					JSONObject queryObject = (JSONObject) query_listArray.getJSONObject(0);
					String name = queryObject.optString("title");
					String playUri = queryObject.optString("target");
					String pic = queryObject.optString("pic_url");
					rSource.setActivity("com.tencent.qqlivetv.open");
					rSource.setApk("com.tencent.qqlivetv");
					rSource.setType("tencent");
					rSource.setId("2");//2 tencent
					rSource.setName(name);
					rSource.setPlayUri(playUri);
					rSource.setPic(pic);
					Log.d(TAG, "queryObject's name is " + name);
					Log.d(TAG, "queryObject's playUri is " + playUri);
					Log.d(TAG, "queryObject's pic is " + pic);
					//					}
				}

				Message message = mHandler.obtainMessage(2, rSource);
				mHandler.sendMessage(message);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*******************************************************************************/
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		mListView.setFocusable(true);
		mListView.requestFocus();
		super.onResume();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		isExit = true;
		super.onStop();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	// 根据手机的分辨率从 DP 的单位 转成为 PX(像素)
	public int dip2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	// 取得分类条目数据
	public List<DtvProgram> getTypeList() {
		return mTypeList;
	}

	public void isCanFocuse(boolean isFocuse) {
		this.isCanFoucse = isFocuse;
	}

	private void getFocuse() {
		if (isCanFoucse) {
			mListView.setFocusable(true);
			mListView.requestFocus();
		}
	}
}