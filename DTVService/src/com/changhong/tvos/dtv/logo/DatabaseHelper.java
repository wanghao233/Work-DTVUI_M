package com.changhong.tvos.dtv.logo;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    final static String TAG = "DtvService.DatabaseHelper";
	
	public DatabaseHelper(Context context) {
        super(context, DtvLogoConstant.DATABASE_NAME, null, DtvLogoConstant.DB_VERSION);
    }
	
	@Override
	public synchronized  SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		return super.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("CREATE TABLE ");
			sql.append(DtvLogoConstant.TABLE_NAME);
			sql.append(" (");
			sql.append(DtvLogoConstant.TABLE_COLUMN_ID);
			sql.append(" INTEGER PRIMARY KEY autoincrement,");
			sql.append(DtvLogoConstant.TABLE_COLUMN_INDEX);
			sql.append(" varchar(120),");
			sql.append(DtvLogoConstant.TABLE_COLUMN_NAME);
			sql.append(" varchar(200),");
			sql.append(DtvLogoConstant.TABLE_COLUMN_SIZE);
			sql.append(" INTEGER,");
			sql.append(DtvLogoConstant.TABLE_COLUMN_LENGTH);
			sql.append(" INTEGER,");
			sql.append(DtvLogoConstant.TABLE_COLUMN_WIDTH);
			sql.append(" INTEGER,");
			sql.append(DtvLogoConstant.TABLE_COLUMN_PATH);
			sql.append(" TEXT,");
			sql.append(DtvLogoConstant.TABLE_COLUMN_VER);
			sql.append(" INTEGER");
			sql.append(");");
			
			Log.i("TAG", sql.toString());
			
			db.execSQL(sql.toString());
		}
		catch (SQLException ex) {     
        	Log.e(TAG, "LOGO DB Create FAILD:" + ex.toString());
        } 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.i("TAG", "LOGO DB Upgrade");
		
		db.execSQL("DROP TABLE IF EXISTS " + DtvLogoConstant.TABLE_NAME);
		onCreate(db);
	}
}
