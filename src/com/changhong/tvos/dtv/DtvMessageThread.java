package com.changhong.tvos.dtv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.changhong.tvos.dtv.tvap.DtvCommonManager;
import com.changhong.tvos.dtv.tvap.DtvDebugManager;
import com.changhong.tvos.dtv.tvap.baseType.ConstValueClass.ConstMessageType;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class DtvMessageThread {

	private static final String TAG = "DTVmonitorThread";

	private static MyHandler mHandler = null;
	private static MyThread mThread = null;

	private static boolean isThreadStart = false;
	private static DtvCommonManager mDtvCommonManager = null;
	private static Process ps = null;
	private static Process psSu = null;

	final static String TARGETDIR = "/info";
	final static String FILE_DB_REPLACE = "/222";
	final static String FILE_PROPERTY_REPLACE = "/333";
	final static String FILE_NAME_LOG = "/111.txt";

	final static String SOURCE_PATH = "/data/dtv";
	final static String FILE_DB = "/changhong_tvos_dtv.db";
	final static String FILE_PROPERTY = "/dtv_properties";

	private final static int COPY_SYSTEM_INFO = 0x113;
	final static String FACTROY_MSTAR_LOG = "data/CH_FACTORY.txt";
	final static String FACTROY_MSTAR_LOG1 = "data/CH_FACTORY.txt.1";
	final static String FACTROY_MTK_LOG = "data/log.txt";
	final static String FACTROY_MTK_LOG1 = "data/log.txt.1 ";
	final static String ANRDIR = "data/anr/ ";
	final static String TOMBSTONE = "data/tombstones/ ";

	private final static int TIME_OUT = 0x111;
	private static TimerCallBack mTimer;
	private static int curTime;
	private static int counter;

	public interface TimerCallBack {
		public void onTimeCallBack(int curTime);
	}

	//创建目录
	private static File mkLogDir(String fileDir) {
		Log.i(TAG, "mkLogDir");
		try {
			File sdCardDir = Environment.getExternalStorageDirectory();// 获取SD卡的目录
			File targetDir = new File(sdCardDir.getCanonicalPath() + fileDir);// 创建目标目录
			if (!targetDir.exists()) {
				Log.i(TAG, "mkLogDir" + targetDir.getCanonicalPath());
				if (!targetDir.mkdirs()) {
					return null;
				}
				return targetDir;
			} else {
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	//复制文件
	private static void CopyFiles(String fileName, String desFileName) {
		Log.i(TAG, "cp " + fileName + " to " + desFileName);
		String sourcePath = fileName;
		FileInputStream input = null;
		File desFile = null;
		FileOutputStream out = null;
		byte[] buffer = new byte[1024];
		try {
			desFile = new File(desFileName);
			input = new FileInputStream(sourcePath);
			out = new FileOutputStream(desFile);
			int len = 0;
			while (-1 != (len = input.read(buffer))) {
				out.write(buffer, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != input) {
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 复制系统信息
	 * @return
	 */
	private static boolean CopySystemInfo() {
		Log.i(TAG, "LL  Enter WriteLogInfo()");
		if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			Log.i(TAG, "LL  CopySystemInfo() --> no sdcard");
			return false;
		}
		File sdCardDir = Environment.getExternalStorageDirectory();
		SimpleDateFormat sdf = new SimpleDateFormat("_MM_dd_HH_mm");
		String end = sdf.format(new Date());
		try {
			File targetDir = new File(sdCardDir.getCanonicalPath() + TARGETDIR + end);
			if (!targetDir.exists()) {
				mkLogDir(TARGETDIR + end);
			}

			String targetFileName = targetDir.getCanonicalPath() + File.separator;//	/infoend/
			//复制Log
			File log = new File(FACTROY_MTK_LOG);
			if (log.exists()) {
				CopyFiles(FACTROY_MTK_LOG, targetFileName + "log.txt");
			}

			log = new File(FACTROY_MTK_LOG1);
			if (log.exists()) {
				CopyFiles(FACTROY_MTK_LOG1, targetFileName + "log.txt1");
			}

			log = new File(FACTROY_MSTAR_LOG);
			if (log.exists()) {
				CopyFiles(FACTROY_MSTAR_LOG, targetFileName + "log.txt");
			}

			log = new File(FACTROY_MSTAR_LOG1);
			if (log.exists()) {
				CopyFiles(FACTROY_MSTAR_LOG1, targetFileName + "log.txt1");
			}

			//复制其他信息
			String exec = "cp " + FACTROY_MTK_LOG + targetFileName;
			try {
				exec = "cp -R " + ANRDIR + targetFileName;
				Log.i(TAG, "CopySystemInfo() exec : " + exec);
				Runtime.getRuntime().exec(exec);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				exec = "cp -R " + TOMBSTONE + targetFileName;
				Log.i(TAG, "CopySystemInfo() exec : " + exec);
				Runtime.getRuntime().exec(exec);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				exec = "cp -R data/core_dump " + targetFileName;
				Log.i(TAG, "CopySystemInfo() exec : " + exec);
				Runtime.getRuntime().exec(exec);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				exec = "cp -R data/core_dump.1006.gz " + targetFileName;
				Log.i(TAG, "CopySystemInfo() exec : " + exec);
				Runtime.getRuntime().exec(exec);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			Log.e(TAG, "CopySystemInfo err " + e);
			return false;
		}
		return true;
	}

	// 记录Log
	private static void WriteLog() {
		Log.i(TAG, "WriteLog");
		if (null == ps) {
			Log.e(TAG, "WriteLog>>ps=null err");
			return;
		}
		InputStream is = ps.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ((line = br.readLine()) != null) {
				sb.append("\n");
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			Log.i(TAG, "WriteLog>>MEDIA_MOUNTED ");
			File sdCardDir = Environment.getExternalStorageDirectory();// 获取SD卡的目录
			try {
				File targetDir = new File(sdCardDir.getCanonicalPath() + TARGETDIR);
				if (!targetDir.exists()) {
					mkLogDir(TARGETDIR);
				}
				File targetFile = new File(targetDir.getCanonicalPath() + FILE_NAME_LOG);
				// 以指定文件创建RandomAccessFile对象
				// 注意此处应该使用RandomAccessFile，如果使用FileOutputStream，每次文件都会清空再写进去
				RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
				// 将文件指针移动到最后
				raf.seek(targetFile.length());
				// 输出文件内容
				raf.writeBytes(sb.toString());
				raf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.e(TAG, "WriteLog>>MEDIA_UNMOUNTED ");
		}
		return;
	}

	/**
	 * 记录Log信息
	 */
	private static void WriteLogInfo() {
		Log.i(TAG, "LL  Enter WriteLogInfo()");
		if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			Log.i(TAG, "LL  WriteLogInfo() --> no sdcard");
			return;
		}
		try {
			File sdCardDir = Environment.getExternalStorageDirectory();
			File targetDir = new File(sdCardDir.getCanonicalPath() + TARGETDIR);
			if (!targetDir.exists()) {
				mkLogDir(TARGETDIR);
			}
			String targetFileName = targetDir.getCanonicalPath() + FILE_NAME_LOG;//	/info/111.txt
			String exec = "logcat -v time -f " + targetFileName;
			Log.i(TAG, "WriteLogInfo() exec : " + exec);

			if (null == ps) {
				ps = Runtime.getRuntime().exec(exec);
				WriteLog();//add by yangliu 2015-6-17
			}

			CopyFiles(SOURCE_PATH + FILE_DB, targetDir.getCanonicalPath() + FILE_DB_REPLACE);//	/data/dtv/changhong_tvos_dtv.db->/info/222
			CopyFiles(SOURCE_PATH + FILE_PROPERTY, targetDir.getCanonicalPath() + FILE_PROPERTY_REPLACE);//	/data/dtv/dtv_properties->/info/333
		} catch (Exception e) {
			Log.e(TAG, "err in WriteLogInfo() " + e);
		}
	}

	/**
	 * 监听消息处理Handler
	 * @author YangLiu
	 */
	private static class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			Log.i(TAG, "LL inThread:" + Thread.currentThread() + " handleMessage>>msg:" + msg.what + " DtvRoot.isWaitingService =" + DtvRoot.isWaitingService);

			switch (msg.what) {
			case ConstMessageType.DTV_SCREEN_SAVER_MESSAGE:// 退出屏保
				if (!DtvRoot.isWaitingService) {
					if (null == mDtvCommonManager) {
						mDtvCommonManager = DtvCommonManager.getInstance();
					}
					mDtvCommonManager.enterScreenSaver();
				}
				break;

			case ConstMessageType.DTV_LOG_OUT_MESSAGE:// 记录Log信息
				if (!DtvRoot.isWaitingService) {
					if (DtvDebugManager.getInstance().isDebug()) {
						WriteLogInfo();
						mHandler.sendEmptyMessageDelayed(ConstMessageType.DTV_LOG_OUT_MESSAGE, 5000);
					}
				}
				break;

			case TIME_OUT://定时器到时
				curTime--;
				counter++;
				if (null != mTimer) {
					mTimer.onTimeCallBack(counter);
				} else {
					Log.i(TAG, "mTimer is null");
				}
				if (curTime > 0) {
					mHandler.sendEmptyMessageDelayed(TIME_OUT, 1000);//每隔1s，执行回调onTimeCallBack(counter)
				} else {
					mHandler.removeMessages(TIME_OUT);
				}
				Log.i(TAG, "counter: " + counter + " LL inThread:" + Thread.currentThread());
				break;

			case COPY_SYSTEM_INFO://复制系统信息
				CopySystemInfo();
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 监听消息处理Thread
	 * @author YangLiu
	 */
	private static class MyThread extends Thread {
		private static final String mThreadName = "DTVmonitorThread";

		private MyThread() {
			super(mThreadName);
		}

		@Override
		public void run() {
			Looper.prepare();
			mHandler = new MyHandler();
			Looper.loop();
			Log.i(TAG, "LL MyThread.run()>>mHandler:" + mHandler);
		}
	}

	public static boolean start() {
		if (false == isThreadStart) {
			isThreadStart = true;
			mThread = new MyThread();
			Log.i(TAG, "LL start >> mThread:" + mThread);
			mThread.setDaemon(true);
			mThread.start();
		}
		return isThreadStart;
	}

	/**
	 * sendMessage的不同方法
	 * @param what
	 * @param delayMillis
	 * @return
	 */
	public static boolean sendEmptyMessageRemovedDelayed(int what, long delayMillis) {
		boolean result = false;
		if (null != mHandler) {
			mHandler.removeMessages(what);
			result = mHandler.sendEmptyMessageDelayed(what, delayMillis);
		}
		Log.i(TAG, "LL sendEmptyMessageRemovedDelayed>>msg = " + what);
		return result;
	}

	public static boolean sendEmptyMessageDelayed(int what, long delayMillis) {
		boolean result = false;
		if (null != mHandler) {
			result = mHandler.sendEmptyMessageDelayed(what, delayMillis);
		}
		Log.i(TAG, "LL sendEmptyMessageDelayed>>msg = " + what);
		return result;
	}

	public static boolean sendEmptyMessageRemoved(int what) {
		boolean result = false;
		if (null != mHandler) {
			mHandler.removeMessages(what);
			result = mHandler.sendEmptyMessage(what);
		}
		Log.i(TAG, "LL sendMessageRemoved>>msg = " + what);
		return result;
	}

	public static boolean sendEmptyMessage(int what) {
		boolean result = false;
		if (null != mHandler) {
			result = mHandler.sendEmptyMessage(what);
		}
		Log.i(TAG, "LL sendEmptyMessage>>msg = " + what);
		return result;
	}

	public static boolean sendMessageRemoved(Message msg) {
		boolean result = false;
		if (null != mHandler) {
			mHandler.removeMessages(msg.what);
			result = mHandler.sendMessage(msg);
		}
		Log.i(TAG, "LL sendMessageRemoved>>msg = " + msg.what);
		return result;
	}

	public static boolean sendMessageDelayed(Message msg, long delayMillis) {
		boolean result = false;

		if (null != mHandler) {
			result = mHandler.sendMessageDelayed(msg, delayMillis);
		}
		Log.i(TAG, "LL sendMessageDelayed>>msg = " + msg + ",delayMillis = " + delayMillis);
		return result;
	}

	public static boolean sendMessage(Message msg) {
		boolean result = false;

		if (null != mHandler) {
			result = mHandler.sendMessage(msg);
		}
		Log.i(TAG, "LL sendMessage>>result = " + result);

		return result;
	}

	public static void removeMessages(int what) {
		boolean result = false;

		if (null != mHandler) {
			mHandler.removeMessages(what);
		}
		Log.i(TAG, "LL removeMessages>>result = " + result);
	}

	public static Message obtainMessage(int what) {
		Message msg = null;

		if (null != mHandler) {
			msg = mHandler.obtainMessage(what);
		}
		Log.i(TAG, "LL obtainMessage>>mHandler = " + mHandler + ",msg = " + msg);
		return msg;
	}

	public static Message obtainMessage(int what, Object obj) {
		Message msg = null;
		if (null != mHandler) {

			if (null == obj) {
				msg = mHandler.obtainMessage(what);
			} else {
				msg = mHandler.obtainMessage(what, obj);
			}
		}
		Log.i(TAG, "LL obtainMessage>>mHandler = " + mHandler + ",msg = " + msg + ",obj = " + obj);
		return msg;
	}

	/**
	 * 设置时间并开始定时器
	 * @param seconds
	 * @return
	 */
	public static boolean setTimer(int seconds) {
		removeTimer();
		if (null != mHandler) {
			curTime = seconds;
			mHandler.sendEmptyMessageDelayed(TIME_OUT, 1000);
			return true;
		}
		return false;
	}

	public static void removeTimer() {
		if (null != mHandler) {
			mHandler.removeMessages(TIME_OUT);
		}
		counter = 0;
	}

	public static void setTimerCallBack(TimerCallBack timerCallBack) {
		mTimer = timerCallBack;
	}

	public static TimerCallBack getTimerCallBack() {
		return mTimer;
	}

	/**
	 * 开始记录Log
	 */
	public static void startLogOut() {
		Log.i(TAG, "start startLogOut");
		if (null != mHandler) {
			mHandler.removeMessages(ConstMessageType.DTV_LOG_OUT_MESSAGE);
			mHandler.sendEmptyMessage(ConstMessageType.DTV_LOG_OUT_MESSAGE);
		}
	}

	public static void stopLogOut() {
		Log.i(TAG, "stop LogOut");
		if (null != mHandler) {
			mHandler.removeMessages(ConstMessageType.DTV_LOG_OUT_MESSAGE);
		}
		if (null != ps) {
			ps.destroy();
			ps = null;
		}
	}

	/**
	 * 开始将本机信息导出到U盘
	 */
	public static void checkOutSystemErrInfo() {
		if (null != mHandler) {
			mHandler.removeMessages(COPY_SYSTEM_INFO);
			mHandler.sendEmptyMessageDelayed(COPY_SYSTEM_INFO, 1000);
		}
	}
}