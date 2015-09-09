package com.changhong.menudata.menuPageData;

import java.util.List;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import com.changhong.data.pageData.ListPageData;
import com.changhong.data.pageData.itemData.ItemHaveSubData;
import com.changhong.data.pageData.itemData.ItemOptionData;
import com.changhong.menudata.MainMenuReceiver;
import com.changhong.tvos.dtv.R;
import com.changhong.tvos.dtv.channel_manager.FilterChannels;
import com.changhong.tvos.dtv.channel_manager.MenuChannelOnOffDialog;
import com.changhong.tvos.dtv.menuManager.MenuDisplayManager;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvConfigManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstDefautUserValue;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstStringKey;
import com.changhong.tvos.dtv.userMenu.MenuSortChannel;
import com.changhong.tvos.dtv.userMenu.MenuSwapChannel;
import com.changhong.tvos.dtv.vo.DTVConstant.ConstServiceType;
import com.changhong.tvos.system.commondialog.CommonInfoDialog;

public class ChannelManagerData extends ListPageData {
	protected static final String TAG = ChannelManagerData.class.getSimpleName();

	Context mContext = null;
	CommonInfoDialog mDialog = null;
	ItemHaveSubData mChannelOnAndOff = null;
	ItemHaveSubData mChannelSwap = null;
	ItemOptionData mFavSortSwitch = null;
	String[] mSwitchStr = null;

	ItemHaveSubData mChannelFilterData = null;

	FilterChannels mFilterDialog = null;
	MenuChannelOnOffDialog mMenuChannelOnOffDialog = null;
	MenuSwapChannel mMenuSwapChannel = null;
	//	MenuSortChannel menuSelfSortDialog = null;
	ItemHaveSubData mSelfSortData = null;

	public ChannelManagerData(String strTitle, int picTitle, Context context) {
		super(strTitle, picTitle);
		this.mContext = context;
		//		this.mType = EnumPageType.NarrowListPage;
		this.mType = EnumPageType.BroadListPage;
		this.init();
	}

	private void init() {
		mSwitchStr = getContext().getResources().getStringArray(R.array.str_dtv_usrset_fav_switch);
		String tmp = DtvConfigManager.getInstance().getValue(ConstStringKey.USER_SET_FAV_SORT);
		int curMode = tmp == null ? ConstDefautUserValue.FAV_SORT_DEFAULT_VALUE : Integer.valueOf(tmp);
		mDialog = new CommonInfoDialog(mContext);
		mChannelOnAndOff = new ItemHaveSubData(0, getContext().getResources().getString(R.string.dtv_channelmanager_channel_on_and_off), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public void onNextPage() {
				// TODO Auto-generated method stub
				List<DtvProgram> channelList = DtvChannelManager.getInstance().getChannelList();
				if (channelList == null || channelList.size() <= 0) {
					Log.e(TAG, "LL there is no any channel***");
					mDialog.setMessage(R.string.dtv_menu_no_channel_prompt);
					mDialog.show();
					return;
				}
				if (null == mMenuChannelOnOffDialog) {
					mMenuChannelOnOffDialog = new MenuChannelOnOffDialog(mContext);
					mMenuChannelOnOffDialog.setOnShowListener(new DialogInterface.OnShowListener() {

						@Override
						public void onShow(DialogInterface arg0) {
							MenuDisplayManager.getInstance(mContext).registerDialogShowing(mMenuChannelOnOffDialog);
							MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_INVISIBLE);
							mMenuChannelOnOffDialog.setShowTV(false);
						}
					});
					mMenuChannelOnOffDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface arg0) {
							MenuDisplayManager.getInstance(mContext).unregisterDialogShowed(mMenuChannelOnOffDialog);
							//							new Handler().postDelayed(new Runnable() {
							//								
							//								@Override
							//								public void run() {
							//									// TODO Auto-generated method stub
							if (mMenuChannelOnOffDialog.isShowTV()) {
								mMenuChannelOnOffDialog.setShowTV(false);
								MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
							} else {
								MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_VISIBLE);
							}
							//								}
							//							}, 200);
						}
					});
				}

				mMenuChannelOnOffDialog.show();

			}
		};
		mChannelOnAndOff.isOnlyShow = true;
		mChannelSwap = new ItemHaveSubData(0, getContext().getResources().getString(R.string.dtv_channelmanager_channel_swap), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public void onNextPage() {
				// TODO Auto-generated method stub
				List<DtvProgram> channelList = DtvChannelManager.getInstance().getChannelList();
				if (channelList == null || channelList.size() <= 0) {
					Log.e(TAG, "LL there is no any channel###");
					mDialog.setMessage(R.string.dtv_menu_no_channel_prompt);
					mDialog.show();
					return;
				}
				if (null == mMenuSwapChannel) {
					mMenuSwapChannel = new MenuSwapChannel(mContext);
					mMenuSwapChannel.setOnShowListener(new DialogInterface.OnShowListener() {

						@Override
						public void onShow(DialogInterface arg0) {
							MenuDisplayManager.getInstance(mContext).registerDialogShowing(mMenuSwapChannel);
							MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_INVISIBLE);
							mMenuSwapChannel.setShowTV(false);
						}
					});
					mMenuSwapChannel.setOnDismissListener(new DialogInterface.OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface arg0) {
							MenuDisplayManager.getInstance(mContext).unregisterDialogShowed(mMenuSwapChannel);
							//							new Handler().postDelayed(new Runnable() {
							//								
							//								@Override
							//								public void run() {
							//									// TODO Auto-generated method stub
							if (mMenuSwapChannel.isShowTV()) {
								MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
								mMenuSwapChannel.setShowTV(false);
							} else {
								MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_VISIBLE);
							}
							//								
							//							}, 200);
						}
					});

				}

				mMenuSwapChannel.show();

			}
		};
		mChannelSwap.isOnlyShow = true;
		mFavSortSwitch = new ItemOptionData(0, getContext().getResources().getString(R.string.dtv_channelmanager_fav_sort), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public boolean initData() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onValueChange(int Value) {
				// TODO Auto-generated method stub
				DtvConfigManager.getInstance().setValue(ConstStringKey.USER_SET_FAV_SORT, String.valueOf(Value));
			}
		};
		mFavSortSwitch.setOptionValues(mSwitchStr);
		mFavSortSwitch.setCurValue(curMode);
		mFavSortSwitch.IsRealTimeData = true;
		this.add(mChannelOnAndOff);
		this.add(mChannelSwap);
		this.add(mFavSortSwitch);

		mChannelFilterData = new ItemHaveSubData(0, getContext().getResources().getString(R.string.dtv_channelmanager_channel_filter), 0, 0) {

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				if (DtvChannelManager.getInstance().getCurChannelType() == ConstServiceType.SERVICE_TYPE_RADIO) {
					return 0;
				}
				return 1;
			}

			@Override
			public void onNextPage() {
				// TODO Auto-generated method stub
				List<DtvProgram> channelList = DtvChannelManager.getInstance().getChannelList();
				if (channelList == null || channelList.size() <= 0) {
					Log.e(TAG, "LL there is no any channel###");
					mDialog.setMessage(R.string.dtv_menu_no_channel_prompt);
					mDialog.show();
					return;
				}
				if (null == mFilterDialog) {
					mFilterDialog = FilterChannels.getInstance(mContext);
					mFilterDialog.setOnShowListener(new DialogInterface.OnShowListener() {

						@Override
						public void onShow(DialogInterface arg0) {
							MenuDisplayManager.getInstance(mContext).registerDialogShowing(mFilterDialog);
							MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_INVISIBLE);
							mFilterDialog.setShowTv(false);
						}
					});
					mFilterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface arg0) {
							MenuDisplayManager.getInstance(mContext).unregisterDialogShowed(mFilterDialog);

							if (mFilterDialog.isShowTv()) {
								MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
								mFilterDialog.setShowTv(false);
							} else {
								MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_VISIBLE);
							}

						}
					});
				}

				mFilterDialog.show();
			}

		};

		mChannelFilterData.isOnlyShow = true;
		this.add(mChannelFilterData);

		mSelfSortData = new ItemHaveSubData(0, getContext().getResources().getString(R.string.dtv_menu_diy_sort), 0, 0) {

			@Override
			public void onNextPage() {
				// TODO Auto-generated method stub
				List<DtvProgram> channelList = DtvChannelManager.getInstance().getChannelList();
				if (channelList == null || channelList.size() <= 0) {
					Log.e(TAG, "LL there is no any channel###");
					mDialog.setMessage(R.string.dtv_menu_no_channel_prompt);
					mDialog.show();
					return;
				}

				//				if(null == menuSelfSortDialog){
				final MenuSortChannel menuSelfSortDialog = new MenuSortChannel(mContext);
				menuSelfSortDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						if (menuSelfSortDialog.isShowTV()) {
							MenuDisplayManager.getInstance(mContext).unregisterDialogShowed(menuSelfSortDialog);
							MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_EXIT);
							menuSelfSortDialog.setShowTV(false);
						} else {
							MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_VISIBLE);
						}
					}
				});
				menuSelfSortDialog.setOnShowListener(new DialogInterface.OnShowListener() {

					@Override
					public void onShow(DialogInterface dialog) {
						// TODO Auto-generated method stub
						MenuDisplayManager.getInstance(mContext).registerDialogShowing(menuSelfSortDialog);
						MainMenuReceiver.menuVisibilityControl(MainMenuReceiver.INTEND_EXTRA_INVISIBLE);
						menuSelfSortDialog.setShowTV(false);
					}
				});
				//				}
				menuSelfSortDialog.show();

			}

			@Override
			public int isEnable() {
				// TODO Auto-generated method stub
				return 1;
			}

		};

		mSelfSortData.isOnlyShow = true;
		this.add(mSelfSortData);
	}

	public Context getContext() {
		return mContext;
	}

	public void updatePage() {
		this.removeItem(mChannelFilterData);
		this.add(mChannelFilterData);
		super.updatePage();
	}
}
