package com.changhong.tvos.dtv.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class BaseChannelProvider extends ContentProvider {
	private BaseChannelDBHelper channelDBHelper;
	private static final int ALLCHANNEL = 1;
	private static final int CHANNEL = 2;
	private static final UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sMatcher.addURI("com.changhong.tvos.dtv.basechannelprovider", "channel", ALLCHANNEL);
		sMatcher.addURI("com.changhong.tvos.dtv.basechannelprovider", "channel/#", CHANNEL);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = channelDBHelper.getWritableDatabase();
		int count = 0;
		switch (sMatcher.match(uri)) {
		case ALLCHANNEL:// com.jbridge.provider.personprovider/person
			count = db.delete(BaseChannelDBHelper.TABLE_CHANNEL, selection, selectionArgs);
			break;
		case CHANNEL:// com.jbridge.provider.personprovider/person/10
			long _id = ContentUris.parseId(uri);
			// 防止他输入时String selection, String[] selectionArgs参数为空，这样就会修改表的所有数据了
			String where = TextUtils.isEmpty(selection) ? "_id=?" : selection + " and _id=?";
			String[] params = new String[] { String.valueOf(_id) };
			if (!TextUtils.isEmpty(selection) && selectionArgs != null) {
				params = new String[selectionArgs.length + 1];
				for (int i = 0; i < selectionArgs.length; i++) {
					params[i] = selectionArgs[i];
				}
				params[selectionArgs.length] = String.valueOf(_id);
			}
			count = db.delete(BaseChannelDBHelper.TABLE_CHANNEL, where, params);
			break;
		default:
			throw new IllegalArgumentException("Unknow Uri:" + uri);
		}
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (sMatcher.match(uri)) {
		case ALLCHANNEL:// //数据集的MIME类型字符串则应该以vnd.android.cursor.dir/
			return "vnd.android.cursor.dir/basechannelprovider.channel";
		case CHANNEL:// 单一数据的MIME类型字符串应该以vnd.android.cursor.item/
			return "vnd.android.cursor.item/basechannelprovider.channel";
		default:
			throw new IllegalArgumentException("Unknow Uri:" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = channelDBHelper.getWritableDatabase();
		long id = 0;
		switch (sMatcher.match(uri)) {
		case ALLCHANNEL:// com.jbridge.provider.personprovider/person
			id = db.insert(BaseChannelDBHelper.TABLE_CHANNEL, "name", values);// 返回值是记录的行号，主键为int实际上就是主键，主键为text则为行号
			return ContentUris.withAppendedId(uri, id);
		case CHANNEL:// com.jbridge.provider.personprovider/person/10
			id = db.insert(BaseChannelDBHelper.TABLE_CHANNEL, "name", values);// 返回值是记录的行号，主键为int实际上就是主键，主键为text则为行号
			String path = uri.toString();
			return Uri.parse(path.substring(0, path.lastIndexOf("/")) + id);
		default:
			throw new IllegalArgumentException("Unknow Uri:" + uri);
		}
	}

	@Override
	public boolean onCreate() {
		channelDBHelper = new BaseChannelDBHelper(this.getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = channelDBHelper.getWritableDatabase();
		switch (sMatcher.match(uri)) {
		case ALLCHANNEL:// com.jbridge.provider.personprovider/person
			return db.query(BaseChannelDBHelper.TABLE_CHANNEL, projection, selection, selectionArgs, null, null, sortOrder);
		case CHANNEL:// com.jbridge.provider.personprovider/person/10
			long _id = ContentUris.parseId(uri);
			// 防止他输入时String selection, String[] selectionArgs参数为空，这样就会修改表的所有数据了
			String where = TextUtils.isEmpty(selection) ? "_id=?" : selection + " and _id=?";
			String[] params = new String[] { String.valueOf(_id) };
			if (!TextUtils.isEmpty(selection) && selectionArgs != null) {
				params = new String[selectionArgs.length + 1];
				for (int i = 0; i < selectionArgs.length; i++) {
					params[i] = selectionArgs[i];
				}
				params[selectionArgs.length] = String.valueOf(_id);
			}
			return db.query(BaseChannelDBHelper.TABLE_CHANNEL, projection, where, params, null, null, sortOrder);

		default:
			throw new IllegalArgumentException("Unknow Uri:" + uri);
		}
	}

	@Override
	// 返回值为影响的记录数
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = channelDBHelper.getWritableDatabase();
		int count = 0;
		switch (sMatcher.match(uri)) {
		case ALLCHANNEL:// com.jbridge.provider.personprovider/person
			count = db.update(BaseChannelDBHelper.TABLE_CHANNEL, values, selection, selectionArgs);
			break;
		case CHANNEL:// com.jbridge.provider.personprovider/person/10
			long _id = ContentUris.parseId(uri);
			// 防止他输入时String selection, String[] selectionArgs参数为空，这样就会修改表的所有数据了
			String where = TextUtils.isEmpty(selection) ? "_id=?" : selection + " and _id=?";
			String[] params = new String[] { String.valueOf(_id) };
			if (!TextUtils.isEmpty(selection) && selectionArgs != null) {
				params = new String[selectionArgs.length + 1];
				for (int i = 0; i < selectionArgs.length; i++) {
					params[i] = selectionArgs[i];
				}
				params[selectionArgs.length] = String.valueOf(_id);
			}
			count = db.update(BaseChannelDBHelper.TABLE_CHANNEL, values, where, params);
			break;
		default:
			throw new IllegalArgumentException("Unknow Uri:" + uri);
		}
		return count;
	}
}