package com.changhong.tvos.dtv.provider;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BaseChannelDBUtil {

	private BaseChannelDBHelper channelDBHelper;

	private static BaseChannelDBUtil instance;

	private BaseChannelDBUtil(Context context) {
		channelDBHelper = new BaseChannelDBHelper(context);
	}

	public synchronized static BaseChannelDBUtil getInstance(Context context) {
		if (instance == null) {
			instance = new BaseChannelDBUtil(context);
		}
		return instance;
	}

	public void addChannel(ArrayList<BaseChannel> channels) {
		// 采用事务处理，确保数据完整性
		SQLiteDatabase database = channelDBHelper.getWritableDatabase();
		channelDBHelper.onUpgrade(database, 0, 0);
		database.beginTransaction(); // 开始事务
		try {
			for (BaseChannel channel : channels) {
				database.execSQL("insert into " + BaseChannelDBHelper.TABLE_CHANNEL + " values(null, ?, ?, ?, ?)",
						new Object[] { channel.getName(), channel.getIndex(), channel.getCode(), channel.getType() });
				// 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
				// 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
				// 使用占位符有效区分了这种情况
			}

			database.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			database.endTransaction(); // 结束事务
		}
	}

	public void save(BaseChannel channel) {
		SQLiteDatabase database = channelDBHelper.getWritableDatabase();
		database.beginTransaction();

		database.execSQL("insert into " + BaseChannelDBHelper.TABLE_CHANNEL + " values(null, ?, ?, ?,?)", new Object[] { channel.getName(), channel.getIndex(), channel.getCode(), channel.getType() });

		// database.close();可以不关闭数据库，他里面会缓存一个数据库对象，如果以后还要用就直接用这个缓存的数据库对象。但通过context.openOrCreateDatabase(arg0, arg1, arg2)打开的数据库必须得关闭
		database.setTransactionSuccessful();
		database.endTransaction();
	}

	public void update(BaseChannel channel) {
		SQLiteDatabase database = channelDBHelper.getWritableDatabase();
		database.execSQL("update " + BaseChannelDBHelper.TABLE_CHANNEL + " set name=?,[index]=?,code=?,type=? where [index]=?", new Object[] { channel.getName(), channel.getIndex(),
				channel.getCode(), channel.getType(), channel.getIndex() });
	}

	public void updateIndex(BaseChannel channel) {
		Log.i("gengxin", "index");
		SQLiteDatabase database = channelDBHelper.getWritableDatabase();
		database.execSQL("update " + BaseChannelDBHelper.TABLE_CHANNEL + " set [index]=? where name=?", new Object[] { channel.getIndex(), channel.getName() });
	}

	public BaseChannel findByCode(String code) {
		SQLiteDatabase database = channelDBHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from " + BaseChannelDBHelper.TABLE_CHANNEL + " where code=?", new String[] { String.valueOf(code) });
		if (cursor.moveToNext()) {
			return new BaseChannel(cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4));
		}
		return null;
	}

	public BaseChannel findByIndex(int index) {
		SQLiteDatabase database = channelDBHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from " + BaseChannelDBHelper.TABLE_CHANNEL + " where [index]=?", new String[] { String.valueOf(index) });
		if (cursor.moveToNext()) {
			return new BaseChannel(cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4));
		}
		return null;
	}

	public void deleteByCodes(String... codes) {
		if (codes.length > 0) {
			StringBuffer sb = new StringBuffer();
			for (String id : codes) {
				sb.append('?').append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
			SQLiteDatabase database = channelDBHelper.getWritableDatabase();
			database.execSQL("delete from " + BaseChannelDBHelper.TABLE_CHANNEL + " where code in(" + sb.toString() + ")", codes);
		}
	}

	public void deleteByNames(String... names) {
		if (names.length > 0) {
			StringBuffer sb = new StringBuffer();
			for (String id : names) {
				sb.append('?').append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
			SQLiteDatabase database = channelDBHelper.getWritableDatabase();
			database.execSQL("delete from " + BaseChannelDBHelper.TABLE_CHANNEL + " where name in(" + sb.toString() + ")", names);
		}
	}

	public ArrayList<BaseChannel> getAllData() {
		ArrayList<BaseChannel> channels = new ArrayList<BaseChannel>();
		SQLiteDatabase database = channelDBHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from " + BaseChannelDBHelper.TABLE_CHANNEL, null);
		while (cursor.moveToNext()) {
			channels.add(new BaseChannel(cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4)));
		}
		return channels;
	}

	public long getCount() {
		SQLiteDatabase database = channelDBHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select count(*) from " + BaseChannelDBHelper.TABLE_CHANNEL + "", null);
		if (cursor.moveToNext()) {
			return cursor.getLong(0);
		}
		return 0;
	}
}
