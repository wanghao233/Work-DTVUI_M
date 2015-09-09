package com.changhong.tvos.dtv.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseChannelDBHelper extends SQLiteOpenHelper {
	// 类没有实例化,是不能用作父类构造器的参数,必须声明为静态

	// 数据库版本号
	private static final int DATABASE_VERSION = 1;
	// 数据库名
	private static final String DATABASE_NAME = "basechannel.db";
	// 数据表名，一个数据库中可以有多个表
	public static final String TABLE_CHANNEL = "basechannel";

	public BaseChannelDBHelper(Context context) {
		// 第一个参数是应用的上下文
		// 第二个参数是应用的数据库名字
		// 第三个参数CursorFactory指定在执行查询时获得个游标实例的工厂,设置为null,代表使用系统默认的工厂类
		// 第四个参数是数据库版本，必须是大于0的int
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	public BaseChannelDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		creatChannelTable(db);
	}

	// onUpgrade()方法在数据库版本每次发生变化时都会把用户手机上的数据库表删除，然后再重新创建
	// 一般在实际项目中是不能这样做的，正确的做法是在更新数据库表结构时，还要考虑用户存放于数据库中的数据不会丢失,从版本几更新到版本几
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANNEL);
		onCreate(db);
	}

	private void creatChannelTable(SQLiteDatabase db) {
		// 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中）
		StringBuffer sBuffer = new StringBuffer();

		sBuffer.append("CREATE TABLE IF NOT EXISTS [" + TABLE_CHANNEL + "] (");
		sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
		sBuffer.append("[name] TEXT,");
		sBuffer.append("[index] INTEGER,");
		sBuffer.append("[code] TEXT,");
		sBuffer.append("[type] TEXT)");
		// 执行创建表的SQL语句
		db.execSQL(sBuffer.toString());
	}
}