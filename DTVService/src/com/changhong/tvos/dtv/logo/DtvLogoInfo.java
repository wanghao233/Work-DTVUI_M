package com.changhong.tvos.dtv.logo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class DtvLogoInfo {

    final static String TAG = "DtvService.Get DTVLogoImageInfo";

    private Context mContext = null;

    /**
     * <br>
     */
    public DtvLogoInfo(Context context) {

        mContext = context;
        if (mContext == null) {
            Log.e(TAG, "DTVLogoImageInfo    context == null");
        } else {
            Log.d(TAG, "DTVLogoImageInfo");
        }
    }

    /**
     *
     */
    public byte[] GetLogoImageByName(String strLogoName) {

        byte[] bytes = null;

        ContentResolver resolver = mContext.getContentResolver();

        String selection = DtvLogoConstant.TABLE_COLUMN_NAME + " like '%" + strLogoName + "%'";

        Cursor cursor = resolver.query(DtvLogoConstant.CONTENT_URI, null, selection, null, DtvLogoConstant.DEFAULT_SORT_ORDER);

        if (cursor.moveToFirst()) {
            String strImagePath = null;
            do {
                strImagePath = cursor.getString(cursor.getColumnIndex("DTV_LogImgPath")).trim();

                //id = cursor.getString(cursor.getColumnIndex("DTV_LogImgID"));
                //String packname = cursor.getString(cursor.getColumnIndex("DTV_LogImgName")).trim();
                //Log.d(TAG, "Id:"+id + "   ImagePath:" + strImagePath + "   name:" + packname);

                bytes = readImage(strImagePath);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return bytes;
    }

    /**
     *
     */
    public byte[] GetLogoImageByIndex(String strLogoIndex) {

        byte[] bytes = null;

        ContentResolver resolver = mContext.getContentResolver();

        String selection = DtvLogoConstant.TABLE_COLUMN_INDEX + " like '%" + strLogoIndex + "%'";

        Cursor cursor = resolver.query(DtvLogoConstant.CONTENT_URI, null, selection, null, DtvLogoConstant.DEFAULT_SORT_ORDER);

        if (cursor.moveToFirst()) {
            String strImagePath = null;
            do {
                //id = cursor.getString(cursor.getColumnIndex("DTV_LogImgID"));
                strImagePath = cursor.getString(cursor.getColumnIndex("DTV_LogImgPath")).trim();

                //String packname = cursor.getString(cursor.getColumnIndex("DTV_LogImgName")).trim();
                //Log.d(TAG, "Id:"+id + "   ImagePath:" + strImagePath + "   name:" + packname);

                bytes = readImage(strImagePath);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return bytes;
    }

    /**
     *
     */
    public byte[] GetLogoImageByID(int nLogoID) {

        byte[] bytes = null;

        ContentResolver resolver = mContext.getContentResolver();
        Uri uri = ContentUris.withAppendedId(DtvLogoConstant.CONTENT_URI, nLogoID);
        Cursor c = resolver.query(uri, null, null, null, DtvLogoConstant.DEFAULT_SORT_ORDER);

        if (c.moveToFirst()) {
            String strImagePath = null;
            do {
                //id = c.getString(c.getColumnIndex("DTV_LogImgID"));
                strImagePath = c.getString(c.getColumnIndex("DTV_LogImgPath")).trim();

                //Log.d(TAG, "Id:"+id + "   ImagePath:" + strImagePath);

                bytes = readImage(strImagePath);
            } while (c.moveToNext());
        }

        c.close();

        return bytes;
    }

    /**
     *
     */
    public byte[] readImage(String imagePath) {
        BufferedInputStream bis = null;
        byte[] bytes = null;

        try {
            try {
                bis = new BufferedInputStream(new FileInputStream(imagePath));
                bytes = new byte[bis.available()];
                bis.read(bytes);
            } finally {
                if (bis != null) {
                    bis.close();
                }
            }
        } catch (FileNotFoundException ex) {

            Log.e(TAG, "readImage Exception:" + ex.toString());
        } catch (IOException e) {
            Log.e(TAG, "readImage Exception:" + e.toString());
        }

        return bytes;
    }
}
