package com.changhong.tvos.dtv.tvap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import android.os.Handler;
import android.util.Log;

/**
 * 读取properties文件
 */
public class DtvConfigManager {
	private static final String TAG = "DtvConfigManager";
	private static final String CONFIG_FILE_PATH = "/data/dtv/dtv_properties";
	private Properties propertie;
	private DtvInterface mDtvInterface = null;
	private static DtvConfigManager instance = null;
	private Handler mHandler = null;
	private Runnable mRunnable = null;

	/**
	 * 初始化Configuration类
	 */
	// static{
	// try {
	// File file = new File(CONFIG_FILE_PATH);
	// if(!file.exists()){
	// Log.i(TAG,"LL DtvConfigManager>>static>>createNewFile***");
	// file.createNewFile();
	// Runtime.getRuntime().exec("chmod 777 "+ file);
	// }
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// Log.e(TAG,"LL DtvConfigManager>>loadFile>>IOException***");
	// e.printStackTrace();
	// }
	// }
	private DtvConfigManager() {
		mDtvInterface = DtvInterface.getInstance();
		propertie = new Properties();
		loadFile(CONFIG_FILE_PATH);
		mHandler = new Handler();
		mRunnable = new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				saveFile(CONFIG_FILE_PATH);
			}
		};
	}

	public static DtvConfigManager getInstance() {
		if (null == instance) {
			instance = new DtvConfigManager();
		}
		return instance;
	}

	private void loadFile(String filePath) {
		File file = null;
		FileInputStream inputFile = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				Log.i(TAG, "LL DtvConfigManager>>loadFile>>createNewFile***");
				file.createNewFile();
				Runtime.getRuntime().exec("chmod 777 " + file);
			}
			inputFile = new FileInputStream(file);
			Log.i(TAG, "LL DtvConfigManager>>loadFile>>load***");
			propertie.load(inputFile);

		} catch (FileNotFoundException ex) {
			Log.e(TAG, "LL DtvConfigManager>>loadFile>>FileNotFoundException***");
			ex.printStackTrace();
		} catch (IOException ex) {
			Log.e(TAG, "LL DtvConfigManager>>loadFile>>IOException***");
			ex.printStackTrace();
		} finally {
			try {
				if (null != inputFile) {
					inputFile.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getValue(String key) {
		String value = null;
		key = key.concat(String.valueOf(mDtvInterface.getDemodType()));
		// this.loadFile(CONFIG_FILE_PATH);
		if (propertie.containsKey(key)) {
			value = propertie.getProperty(key);// 得到某一属性的值
			Log.i(TAG, "LL DtvConfigManager>>getValue***value = " + value);
			return value;
		} else {

			return value;
		}
	}

	/**
	 * 用于读取不分T和C 存储的值
	 * 
	 * @param key
	 * @return
	 */
	public String getCommenValue(String key) {
		String value = null;
		// this.loadFile(CONFIG_FILE_PATH);
		if (propertie.containsKey(key)) {
			value = propertie.getProperty(key);// 得到某一属性的值
			Log.i(TAG, "LL DtvConfigManager>>getValue***value = " + value);
			return value;
		} else {

			return value;
		}
	}

	/**
	 * 用于存储不分源存储的值
	 * 
	 * @param key
	 * @return
	 */
	public void setCommenValue(String key, String value) {
		propertie.setProperty(key, value);
		Log.i(TAG, "LL DtvConfigManager>>setValue***value = " + value);
		// this.saveFile(CONFIG_FILE_PATH);
		mHandler.post(mRunnable);
	}

	public void clearAll() {
		propertie.clear();
		// this.saveFile(CONFIG_FILE_PATH);
		mHandler.post(mRunnable);
	}

	public void setValue(String key, String value) {
		key = key.concat(String.valueOf(mDtvInterface.getDemodType()));
		propertie.setProperty(key, value);
		Log.i(TAG, "LL DtvConfigManager>>setValue***value = " + value);
		// this.saveFile(CONFIG_FILE_PATH);
		mHandler.post(mRunnable);
	}

	private synchronized void saveFile(String filePath) {
		File file = null;
		FileOutputStream outputFile = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
				Runtime.getRuntime().exec("chmod 777 " + file);
			}
			outputFile = new FileOutputStream(file);
			Log.i(TAG, "LL DtvConfigManager>>saveFile>>store***");
			propertie.store(outputFile, null);

		} catch (FileNotFoundException e) {
			Log.e(TAG, "LL DtvConfigManager>>saveFile>>FileNotFoundException***");
			e.printStackTrace();
		} catch (ClassCastException e) {
			Log.e(TAG, "LL ClassCastException>>saveFile>>ClassCastException***");
			e.printStackTrace();
		} catch (IOException ioe) {
			Log.e(TAG, "LL DtvConfigManager>>saveFile>>IOException***");
			ioe.printStackTrace();
		} finally {
			try {
				if (null != outputFile) {
					outputFile.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}