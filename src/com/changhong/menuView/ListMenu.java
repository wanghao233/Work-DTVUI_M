package com.changhong.menuView;

import java.util.ArrayList;
import java.util.List;
import com.changhong.data.MenuData;
import com.changhong.data.pageData.ListPageData;
import com.changhong.data.pageData.PageData;
import com.changhong.data.pageData.PageData.EnumPageType;
import com.changhong.data.pageData.PageData.OnPageTurnListener;
import com.changhong.data.pageData.itemData.util.EnumDataType;
import com.changhong.data.pageData.itemData.util.ItemData;
import com.changhong.menuView.itemView.ItemBaseView.OnReturnListener;
import com.changhong.menuView.itemView.ItemOptionView;
import com.changhong.menuView.itemView.ItemProgressBarView;
import com.changhong.menuView.itemView.ItemBaseView;
import com.changhong.tvos.dtv.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListMenu extends RelativeLayout {
	protected static final String TAG = ListMenu.class.getSimpleName();
	RelativeLayout mBgRoot;
	RelativeLayout mFirstPageBg;
	RelativeLayout mListMenuBg;
	List<String> titleList = new ArrayList<String>();
	List<ImageView> dividerList = new ArrayList<ImageView>();
	List<TextView> textList = new ArrayList<TextView>();
	protected LinearLayout mRootListTitle;
	protected LinearLayout mListTitle;
	protected TextView mMenuRootListTitle;
	protected TextView mMenuListTitle1;
	protected TextView mMenuListTitle2;
	protected TextView mMenuListTitle3;
	protected TextView mMenuListTitle4;
	protected ImageView mMenuTitleDivider1;
	protected ImageView mMenuTitleDivider2;
	protected ImageView mMenuTitleDivider3;
	protected ImageView mMenuTitleDivider4;
	protected int mCurTitleColor = 0xFF565656;
	protected int mPreTitleColor = 0xFF828282;
	protected int mCurTitleSize = 30;
	protected int mPreTitleSize = 16;
	protected RelativeLayout mRootListArrow;
	protected LinearLayout mListArrow;
	public MenuListView mListMenu;
	public MenuRootListView mRootListMenu;

	protected LayoutParams mParams;
	protected Drawable divider;

	MenuData mMenuData;

	OnPageTurnListener mOnPageTurnListener = new OnPageTurnListener() {
		@Override
		public void onPageTurn(PageData nextPage) {
			// TODO Auto-generated method stub
			if (nextPage == null) {
				ItemData data = mListMenu.getItemData();
				showOnlyShowView(data);
				return;
			}

			if (nextPage.mPageId.equals("mPage2")) {
				return;
			}
			pageTurn(nextPage);
		}
	};

	public ListMenu(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		mBgRoot = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.main_menu, null);
		mParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mBgRoot.setLayoutParams(mParams);
		addView(mBgRoot);
		mRootListTitle = (LinearLayout) mBgRoot.findViewById(R.id.linearLayout3);
		mListTitle = (LinearLayout) mBgRoot.findViewById(R.id.linearLayout4);

		mMenuRootListTitle = (TextView) mBgRoot.findViewById(R.id.textView1);
		mMenuListTitle1 = (TextView) mBgRoot.findViewById(R.id.title1);
		mMenuListTitle2 = (TextView) mBgRoot.findViewById(R.id.title2);
		mMenuListTitle3 = (TextView) mBgRoot.findViewById(R.id.title3);
		mMenuListTitle4 = (TextView) mBgRoot.findViewById(R.id.title4);
		mMenuTitleDivider1 = (ImageView) mBgRoot.findViewById(R.id.titleDivider1);
		mMenuTitleDivider2 = (ImageView) mBgRoot.findViewById(R.id.titleDivider2);
		mMenuTitleDivider3 = (ImageView) mBgRoot.findViewById(R.id.titleDivider3);
		mMenuTitleDivider4 = (ImageView) mBgRoot.findViewById(R.id.titleDivider4);
		dividerList.add(mMenuTitleDivider1);
		dividerList.add(mMenuTitleDivider2);
		dividerList.add(mMenuTitleDivider3);
		dividerList.add(mMenuTitleDivider4);
		mMenuRootListTitle.setTextColor(mCurTitleColor);
		mMenuListTitle1.setTextColor(mCurTitleColor);
		mMenuListTitle2.setTextColor(mCurTitleColor);
		mMenuListTitle3.setTextColor(mCurTitleColor);
		mMenuListTitle4.setTextColor(mCurTitleColor);
		textList.add(mMenuListTitle1);
		textList.add(mMenuListTitle2);
		textList.add(mMenuListTitle3);
		textList.add(mMenuListTitle4);
		mMenuRootListTitle.setTextSize(mCurTitleSize);
		mMenuListTitle1.setTextSize(mCurTitleSize);
		mMenuListTitle2.setTextSize(mCurTitleSize);
		mMenuListTitle3.setTextSize(mCurTitleSize);
		mMenuListTitle4.setTextSize(mCurTitleSize);
		mRootListArrow = (RelativeLayout) mBgRoot.findViewById(R.id.mRootListArrow);
		mListArrow = (LinearLayout) mBgRoot.findViewById(R.id.mListArrow);
		mFirstPageBg = (RelativeLayout) mBgRoot.findViewById(R.id.mBgFirstListMenu);
		mListMenuBg = (RelativeLayout) mBgRoot.findViewById(R.id.mBgListMenu);

		mRootListMenu = (MenuRootListView) mBgRoot.findViewById(R.id.mRootListView);
		mListMenu = (MenuListView) mBgRoot.findViewById(R.id.mListView);
	}

	private void updatePageTitle(PageData pageData) {
		if (pageData != null) {
			ListPageData listPageData = (ListPageData) pageData;
			if (pageData.mType == EnumPageType.BroadListPage) {
				Log.i(TAG, "LL updatePageTitle()>>BroadPageTitle = " + listPageData.getStrTitle());
				//				mRootListTitle.setBackgroundResource(listPageData.getPicTitle());
				mMenuListTitle1.setText(listPageData.getStrTitle());
			} else if (pageData.mType == EnumPageType.NarrowListPage) {
				Log.i(TAG, "LL updatePageTitle()>>NarrowPageTitle = " + listPageData.getStrTitle());
				//				mListTitle.setBackgroundResource(listPageData.getPicTitle());
				mMenuRootListTitle.setText(listPageData.getStrTitle());
			}
		}
	}

	private void updatePageTitle(PageData pageData, EnumPageType pageType) {
		if (EnumPageType.BroadListPage == pageType) {
			if (pageData != null) {
				ListPageData listPageData = (ListPageData) pageData;
				Log.i(TAG, "LL updatePageTitle()>>title = " + listPageData.getStrTitle());
				titleList.add(listPageData.getStrTitle());
				updatePageTitle(pageData.mParent, pageType);
			} else {
				if (titleList.size() <= textList.size()) {
					for (int i = 0, titleIndex = titleList.size() - 1; i < textList.size(); i++) {

						if (i < titleList.size()) {
							if (i == titleList.size() - 1) {
								textList.get(i).setTextSize(mCurTitleSize);
								textList.get(i).setTextColor(mCurTitleColor);
								dividerList.get(i).setVisibility(View.GONE);
							} else {
								textList.get(i).setTextSize(mPreTitleSize);
								textList.get(i).setTextColor(mPreTitleColor);
								dividerList.get(i).setVisibility(View.VISIBLE);
							}
							if (titleIndex >= 0) {
								textList.get(i).setText(titleList.get(titleIndex--));
							}
						} else {
							textList.get(i).setText("");
							dividerList.get(i).setVisibility(View.GONE);
						}

					}
				} else {
					for (int i = 0, titleIndex = titleList.size() - 1; i < titleList.size(); i++) {
						if (i < titleList.size()) {
							if (i == textList.size() - 1) {
								textList.get(i).setTextSize(mCurTitleSize);
								textList.get(i).setTextColor(mCurTitleColor);
								dividerList.get(i).setVisibility(View.GONE);
							} else if (i >= 0 && i < textList.size() - 1) {
								textList.get(i).setTextSize(mPreTitleSize);
								textList.get(i).setTextColor(mPreTitleColor);
								dividerList.get(i).setVisibility(View.VISIBLE);
							}
							if (titleIndex >= 0) {
								Log.i(TAG, "LL i = " + i + ",titleIndex = " + titleIndex + ",title = " + titleList.get(titleIndex));
								if (i > 0 && i < textList.size() - 2) {
									textList.get(i).setText("...");
									titleIndex = 1;
								} else if (i == 0 || i >= textList.size() - 2 && i < textList.size()) {
									textList.get(i).setText(titleList.get(titleIndex--));
								}
							}
						}
					}
				}

				titleList.clear();
			}
		} else if (EnumPageType.NarrowListPage == pageType) {
			if (pageData != null) {
				ListPageData listPageData = (ListPageData) pageData;
				Log.i(TAG, "LL updatePageTitle()>>NarrowPageTitle = " + listPageData.getStrTitle());
				mMenuRootListTitle.setText(listPageData.getStrTitle());
			}
		}

	}

	public void init(MenuData data) {
		mMenuData = data;
		pageTurn(mMenuData.mCurPage);

	}

	private void pageTurn(PageData pageData) {
		Log.v("tv", "MenuSetting pageTurn");
		mMenuData.mCurPage = (ListPageData) pageData;
		//		updatePageTitle(pageData);
		updatePageTitle(pageData, pageData.mType);
		if (pageData.mType == EnumPageType.BroadListPage) {
			mFirstPageBg.setVisibility(View.GONE);
			mListMenuBg.setVisibility(View.VISIBLE);
			mListMenu.init(pageData);
			if (mMenuData.mCurPage.isShowMultiPageIcon()) {
				mListArrow.setVisibility(View.VISIBLE);
			} else {
				mListArrow.setVisibility(View.INVISIBLE);
			}
		} else if (pageData.mType == EnumPageType.NarrowListPage) {
			mFirstPageBg.setVisibility(View.VISIBLE);
			mListMenuBg.setVisibility(View.GONE);
			mRootListMenu.init(pageData);
			if (mMenuData.mCurPage.isShowMultiPageIcon()) {
				mRootListArrow.setVisibility(View.VISIBLE);
			} else {
				mRootListArrow.setVisibility(View.INVISIBLE);
			}
		}
		pageData.setOnPageTurnListener(mOnPageTurnListener);
	}

	private void showOnlyShowView(final ItemData data) {
		ItemBaseView mView = null;
		if (data.getDataType() == EnumDataType.EN_OPTIONVIEW) {
			mView = (ItemOptionView) data.mOnlyShowView;
			if (mView == null) {
				mView = new ItemOptionView(mContext);
			}

		} else if (data.getDataType() == EnumDataType.EN_PROGRESSBAR) {
			mView = (ItemProgressBarView) data.mOnlyShowView;
			if (mView == null) {
				mView = new ItemProgressBarView(mContext);
			}
		} else {
			return;
		}
		data.mOnlyShowView = mView;
		mView.init(data);
		RelativeLayout.LayoutParams mParam = new LayoutParams(mListMenu.getCurView().getLayoutParams());
		mParam.leftMargin = (int) mListMenu.getCurView().getX();
		mParam.topMargin = (int) (mListMenu.getCurView().getY() + mListMenu.getY());
		mView.setLayoutParams(mParam);

		this.removeView(mView);
		this.addView(mView);
		mView.setFocusable(true);
		mView.requestFocus();
		mView.setOnReturnListener(new OnReturnListener() {
			@Override
			public void OnReturn() {
				// TODO Auto-generated method stub
				ListMenu.this.removeView((ItemBaseView) data.mOnlyShowView);
				mBgRoot.setVisibility(View.VISIBLE);
			}
		});
		mBgRoot.setVisibility(View.GONE);
	}

	public void show() {
		mBgRoot.setVisibility(View.VISIBLE);
	}

	public void hide() {
		mBgRoot.setVisibility(View.GONE);

	}

	public boolean isShown() {
		return mBgRoot.isShown();
	}

	public void update() {
		if (null != mListMenu) {
			mListMenu.update();
		}
	}
}