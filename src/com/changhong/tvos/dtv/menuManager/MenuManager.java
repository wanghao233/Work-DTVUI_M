package com.changhong.tvos.dtv.menuManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import android.util.Log;
import android.view.KeyEvent;
import com.changhong.tvos.dtv.tvap.DtvChannelManager;
import com.changhong.tvos.dtv.tvap.DtvCommonManager;
import com.changhong.tvos.dtv.tvap.DtvEpgManager;
import com.changhong.tvos.dtv.tvap.DtvScheduleManager;
import com.changhong.tvos.dtv.tvap.baseType.DtvEvent;
import com.changhong.tvos.dtv.tvap.baseType.DtvProgram;
import com.changhong.tvos.dtv.tvap.baseType.DtvScheduleEvent;

/**
 * 菜单管理器
 * @author YangLiu
 *
 */
public class MenuManager {

	public enum listState {
		channel_List, channel_WatchedList, channel_Edit,
	}

	private static final String TAG = "MenuManager";
	private static MenuManager mMenuManager = null;
	private List<DtvProgram> channelListAll;
	private List<DtvProgram> channelList;
	private List<DtvProgram> cur_list;
	private int programSwapSelect;
	private listState curListState;
	private DtvScheduleManager scheduleManager = DtvScheduleManager.getInstance();
	private DtvChannelManager channelManager = DtvChannelManager.getInstance();
	private DtvCommonManager commonManager = DtvCommonManager.getInstance();
	private DtvEpgManager epgManager = DtvEpgManager.getInstance();

	private MenuManager() {
		channelListAll = channelManager.getChannelList();
		channelList = new ArrayList<DtvProgram>();
		cur_list = channelListAll;
		programSwapSelect = Integer.MAX_VALUE;
	}

	//getInstance()方法
	public static MenuManager getInstance() {
		if (mMenuManager == null) {
			mMenuManager = new MenuManager();
		}
		return mMenuManager;
	}

	//初始化各个频道列表
	public void init(listState state) {
		curListState = state;

		//////fy 2014-03-24 
		channelManager.channelListInit(channelManager.getCurChannelType());
		channelListAll = channelManager.getChannelList();

		if (listState.channel_List == state) {
			channelListInit();
			cur_list = channelList;
		} else if (listState.channel_WatchedList == state) {
			cur_list = this.getWatchedChannelList();
		} else {
			cur_list = channelListAll;
		}
		programSwapSelect = Integer.MAX_VALUE;
	}

	//根据“全部节目”标志获取节目列表
	public List<DtvProgram> getChannelListByAllchFlag() {
		/**
		 * 解决：关闭全部节目然后手动排序后，再次打开全部节目，频道列表只显示过滤后的列表的问题
		 * 				add By YangLiu 2014-12-1
		 */
		List<DtvProgram> list = null;

		if (DtvChannelManager.getInstance().GetAllchFlag()) {
			init(listState.channel_List);//取得全部列表
			list = DtvChannelManager.getInstance().getChannelList();
		} else {
			list = getWatchedChannelList();//取得搜索观看过的频道列表
		}
		return list;
	}

	//获取观看过的节目列表
	public List<DtvProgram> getWatchedChannelList() {
		List<DtvProgram> list = new ArrayList<DtvProgram>();
		List<DtvProgram> listTemp = new ArrayList<DtvProgram>();
		//	list =  channelManager.getWatchedChannelList();
		list = channelManager.getChannelList();
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).isSkip() || DtvChannelManager.getInstance().GetAllchFlag() || list.get(i).mProgramNum == channelManager.getCurProgramNum()) {
				listTemp.add(list.get(i));
			}
		}
		return listTemp;
	}

	//通过serviceIndex换台
	public boolean changeChannelByProgramServiceIndex(int serviceIndex) {
		return channelManager.channelChangeByProgramServiceIndex(serviceIndex, false);
	}

	//获得当前正在播放的节目
	public DtvProgram getCurProgram() {
		DtvProgram curProgram = null;
		curProgram = channelManager.getCurProgram();
		if (null == curProgram) {
			Log.e(TAG, "LL curProgram == null>>curProgram = new DtvProgram()");
			curProgram = new DtvProgram();
		}
		return curProgram;
	}

	//获取当前节目所在列表中的位置
	public int getCurProgramListIndex() {
		int index = 0;
		DtvProgram program = this.getCurProgram();
		if (program != null && cur_list != null && cur_list.size() > 0) {
			for (int i = 0; i < cur_list.size(); i++) {
				if (cur_list.get(i).mServiceIndex == program.mServiceIndex) {
					index = i;
					break;
				}
			}
		}
		Log.i(TAG, "LL getCurProgramListIndex()>>index = " + index);
		return index;
		//		return cur_list.indexOf(getCurProgram());
	}

	//获取当前节目的serviceIndex
	public int getCurProgramServiceIndex() {
		int serviceIndex = 0;
		DtvProgram program = getCurProgram();
		if (program != null) {
			serviceIndex = program.mServiceIndex;
		}
		return serviceIndex;
	}

	//获取所有节目的个数
	public int getProgramCount() {
		return cur_list.size();
	}

	//获取当前节目列表
	public void setCur_list(List<DtvProgram> curList) {
		this.cur_list = curList;
	}

	//节目列表初始化
	private void channelListInit() {
		channelList.clear();
		Log.v(TAG, "channelListInit(), channelListAll.size()= " + channelListAll.size());
		for (int i = 0; i < channelListAll.size(); i++) {
			if ((!channelListAll.get(i).isSkip() && !DtvChannelManager.getInstance().GetAllchFlag()) || DtvChannelManager.getInstance().GetAllchFlag()
					|| channelListAll.get(i).mProgramNum == channelManager.getCurProgramNum()) {
				channelList.add(channelListAll.get(i));
				Log.v(TAG, "channelListAll: i = " + i);
			}
		}
	}

	//获取当前节目列表
	public List<DtvProgram> getCurList() {
		return cur_list;
	}

	//通过offSetIndex换台
	public void changeChannelByListIndexOffset(int keyCode, KeyEvent event, int offSetIndex) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (curListState == listState.channel_List) {
				channelManager.channelChangeUpOffset(offSetIndex);
			} else {
				channelManager.channelChangePreOffset(offSetIndex);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (curListState == listState.channel_List) {
				channelManager.channelChangeDownOffset(offSetIndex);
			} else {
				channelManager.channelChangeNextOffset(offSetIndex);
			}
			break;
		default:
			break;
		}
	}

	//通过列表中的位置获取节目
	public DtvProgram getProgramByListIndex(int listIndex) {
		if (listIndex < 0 || listIndex >= cur_list.size()) {
			return null;
		}
		return cur_list.get(listIndex);
	}

	//onkeyDown事件
	public int onkeydown(int keyCode, KeyEvent event, int times) {
		Log.v(TAG, "LL MenuManager>>onKeyDown>>keyCode = " + keyCode);
		while (times-- > 0) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if (curListState == listState.channel_List) {
					channelManager.channelChangeUp(true);
				} else {
					channelManager.channelChangePre();
				}
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if (curListState == listState.channel_List) {
					channelManager.channelChangeDown(true);
				} else {
					channelManager.channelChangeNext();
				}
				break;
			default:
				break;
			}
		}
		return 0;
	}

	//获取当前列表状态
	public listState getListState() {
		return curListState;
	}

	//设置交换节目的位置
	public void setprogramSwapSelect(int index) {
		programSwapSelect = index;
	}

	//取得交换节目的位置
	public int getprogramSwapSelect() {
		return programSwapSelect;
	}

	//设置当前节目是否过滤
	public void setCurProgramSkipState(boolean skip) {
		DtvProgram curProgram = null;
		curProgram = channelManager.getCurProgram();
		if (curProgram != null) {
			curProgram.setSkipState(skip);
		} else {
			Log.e(TAG, "LL setCurProgramSkipState()>>curProgram==null");
		}
	}

	//交换两个位置的节目
	public void programSwap(int index1, int index2) {
		channelManager.programSwap(index1, index2);
	}

	//获取当前系统时间
	public Date getCurrentDate() {
		return commonManager.getCurrentDate();
	}

	//获取预约节目列表
	public List<DtvScheduleEvent> getScheduleEvents() {
		return scheduleManager.getScheduleEvents();
	}

	//删除预约节目
	public void delScheduleEvent(DtvScheduleEvent event) {
		scheduleManager.delScheduleEvent(event);
	}

	//删除预约节目
	public void delScheduleEvent(DtvEvent event) {
		scheduleManager.delScheduleEvent(event, getCurProgram());
	}

	//删除所有预约节目
	public void delAllScheduleEvents() {
		scheduleManager.delAllScheduleEvents();
	}

	//添加预约节目
	public void addScheduleEvent(DtvEvent event) {
		scheduleManager.addScheduleEvent(event, getCurProgram());
	}

	//是否为预约节目
	public boolean isOrderEvent(DtvEvent event) {
		return scheduleManager.isOrderEvent(event, getCurProgram());
	}

	//获取冲突的节目
	public DtvScheduleEvent getConflictEvent(DtvEvent event) {
		return scheduleManager.getConflictEvent(event, getCurProgram());
	}

	//通过时间获取节目
	public List<DtvEvent> getEventListByTime(int dayIndex, int startTime, int endTime) {
		return epgManager.getEventsByTime(getCurProgramServiceIndex(), dayIndex, startTime, endTime);
	}

	//获取节目的PF信息
	public DtvEvent[] getPFinfo() {
		return epgManager.getPFInfo(getCurProgramServiceIndex());
	}

	//设置交换的节目列表
	public void setExchangeList(List<DtvProgram> myChangeList) {
		// TODO Auto-generated method stub
		//		Collections.sort(myChangeList, new CompareDtvProgram());
		//		for (DtvProgram program : myChangeList) {
		//			Log.i(TAG, "program num " + program.mProgramNum);
		//		}

		programSwapSelect = Integer.MAX_VALUE;

		channelList.clear();
		channelList.addAll(myChangeList);
		channelManager.setChangedSortList(myChangeList);
		channelManager.channelChangeByProgramServiceIndex(channelManager.getCurPorgramServiceIndex(), true);
		cur_list = channelList;
	}
}

/**
 * 比较两个节目的差别
 * @author YangLiu
 *
 */
class CompareDtvProgram implements Comparator<DtvProgram> {

	@Override
	public int compare(DtvProgram arg0, DtvProgram arg1) {
		// TODO Auto-generated method stub
		return arg0.mProgramNum - arg1.mProgramNum;
	}
}