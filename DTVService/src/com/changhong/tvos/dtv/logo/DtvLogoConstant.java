package com.changhong.tvos.dtv.logo;

import android.net.Uri;

public class DtvLogoConstant {

	public static final String AUTHORITY = "com.changhong.tvos.dtv.logo.DtvLogoContentProvider";

	public static final String DATABASE_NAME = "/data/dtv/db/changhong_tvos_LogoInfo.db"; 
	public static final String TABLE_NAME = "DtvLogoInfo"; 
	
	public static final int DB_VERSION = 1;

	public static final String TABLE_COLUMN_ID="DTV_LogImgID";
	public static final String TABLE_COLUMN_INDEX="DTV_LogImgIndex";
	public static final String TABLE_COLUMN_NAME="DTV_LogImgName";
	public static final String TABLE_COLUMN_SIZE="DTV_LogImgSize";
	public static final String TABLE_COLUMN_LENGTH="DTV_LogImgLength";
	public static final String TABLE_COLUMN_WIDTH="DTV_LogImgWidth";
	public static final String TABLE_COLUMN_PATH="DTV_LogImgPath";
	public static final String TABLE_COLUMN_VER="DTV_LogImgVer";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final String CONTENT_TYPE="vnd.android.cursor.dir/DtvLogoInfo";
	public static final String CONTENT_ITEM_TYPE="vnd.android.cursro.item/DtvLogoInfo";
	
	public static final int Logos =1;
	public static final int Logo_ID=2;
	
	public static final String DEFAULT_SORT_ORDER="DTV_LogImgID ASC";
	
	
	private DtvLogoConstant() {

	}
}
