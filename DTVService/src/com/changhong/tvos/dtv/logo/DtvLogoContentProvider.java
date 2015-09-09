package com.changhong.tvos.dtv.logo;

import java.util.HashMap;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.changhong.tvos.dtv.logo.DatabaseHelper;
import com.changhong.tvos.dtv.logo.DtvLogoConstant;

public class DtvLogoContentProvider extends ContentProvider {

    final static String TAG = "DtvService.DtvLogoContentProvider";
	
	protected DatabaseHelper mDatabaseHelper = null;
	private static final UriMatcher sUriMatcher;
	private static final HashMap<String, String> sDtvLogoHashMap;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(DtvLogoConstant.AUTHORITY, DtvLogoConstant.TABLE_NAME,
				DtvLogoConstant.Logos);
		sUriMatcher.addURI(DtvLogoConstant.AUTHORITY,
				DtvLogoConstant.TABLE_NAME + "/#", DtvLogoConstant.Logo_ID);
		
		sDtvLogoHashMap = new HashMap<String, String>();
		sDtvLogoHashMap.put(DtvLogoConstant.TABLE_COLUMN_ID,
				DtvLogoConstant.TABLE_COLUMN_ID);
		sDtvLogoHashMap.put(DtvLogoConstant.TABLE_COLUMN_INDEX,
				DtvLogoConstant.TABLE_COLUMN_INDEX);
		sDtvLogoHashMap.put(DtvLogoConstant.TABLE_COLUMN_NAME,
				DtvLogoConstant.TABLE_COLUMN_NAME);
		sDtvLogoHashMap.put(DtvLogoConstant.TABLE_COLUMN_SIZE,
				DtvLogoConstant.TABLE_COLUMN_SIZE);
		sDtvLogoHashMap.put(DtvLogoConstant.TABLE_COLUMN_LENGTH,
				DtvLogoConstant.TABLE_COLUMN_LENGTH);
		sDtvLogoHashMap.put(DtvLogoConstant.TABLE_COLUMN_WIDTH,
				DtvLogoConstant.TABLE_COLUMN_WIDTH);
		sDtvLogoHashMap.put(DtvLogoConstant.TABLE_COLUMN_PATH,
				DtvLogoConstant.TABLE_COLUMN_PATH);
		sDtvLogoHashMap.put(DtvLogoConstant.TABLE_COLUMN_VER,
				DtvLogoConstant.TABLE_COLUMN_VER);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mDatabaseHelper =new DatabaseHelper(getContext());
		
		Log.i("TAG", "DtvLogoContentProvider  onCreate");
		
		return true;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (sUriMatcher.match(uri)) {
        case DtvLogoConstant.Logos:
            return DtvLogoConstant.CONTENT_TYPE;
        case DtvLogoConstant.Logo_ID: 
            return DtvLogoConstant.CONTENT_ITEM_TYPE;
        default:
            throw new IllegalArgumentException("unknown  uri" + uri);
        } 
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(DtvLogoConstant.TABLE_NAME);
		
        switch (sUriMatcher.match(uri)) {
        case DtvLogoConstant.Logos:
        	qb.setProjectionMap(sDtvLogoHashMap);
            break;
        case DtvLogoConstant.Logo_ID:
        	qb.setProjectionMap(sDtvLogoHashMap);
			qb.appendWhere(DtvLogoConstant.TABLE_COLUMN_ID + " = "
					+ uri.getPathSegments().get(1));
			break;
        default:
            throw new IllegalArgumentException("unknown  uri" + uri);
        } 
        
        String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = DtvLogoConstant.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}
		
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		
		Cursor cursor = qb.query(db, projection, selection, selectionArgs,null, null, orderBy);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if (sUriMatcher.match(uri) != DtvLogoConstant.Logos) {
			throw new IllegalArgumentException("Unknown URI " + uri);

		}
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);

		} else {
			values = new ContentValues();
		}
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		long rowId = db.insert(DtvLogoConstant.TABLE_NAME, "", values);
		if (rowId > 0) {
			Uri CrmUri = ContentUris.withAppendedId(DtvLogoConstant.CONTENT_URI,
					rowId);
			getContext().getContentResolver().notifyChange(CrmUri, null);
			return CrmUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
			case DtvLogoConstant.Logos:
				count = db.update(DtvLogoConstant.TABLE_NAME, values, where,
						whereArgs);
				break;
			case DtvLogoConstant.Logo_ID:
				String crmId = uri.getPathSegments().get(1);
				count = db.update(
						DtvLogoConstant.TABLE_NAME,
						values,
						DtvLogoConstant.TABLE_COLUMN_ID
								+ "="
								+ crmId
								+ (!TextUtils.isEmpty(where) ? " AND (" + where
										+ ')' : ""), whereArgs);
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
			case DtvLogoConstant.Logos:

				count = db.delete(DtvLogoConstant.TABLE_NAME, where, whereArgs);
				break;
			case DtvLogoConstant.Logo_ID:
				String crmId = uri.getPathSegments().get(1);
				count = db.delete(
						DtvLogoConstant.TABLE_NAME,
						DtvLogoConstant.TABLE_COLUMN_ID
								+ "="
								+ crmId
								+ (!TextUtils.isEmpty(where) ? " AND (" + where
										+ ')' : ""), whereArgs);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}
