package com.changhong.tvos.dtv.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.http.util.EncodingUtils;
import com.changhong.tvos.dtv.vo.TimerInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TimerStorge {
    private static final String DATABASE_NAME = "timer_schedule.db";
    private static final int DATABASE_VERSION = 2015040101;
    protected static final String TIMER_INDEX = "timer_index";
    protected static final String TIMER_ID = "timer_id";
    protected static final String TIMER_TYPE = "timer_Type";
    protected static final String ORIGINAL = "original";//2015-1-21
    protected static final String START_TIME = "start_time";
    protected static final String END_NOTIRY_TIME = "end_notify_time";
    protected static final String BROADCAST_FILTER = "broadcast_filter";
    private static final String DATA_INFO = "data_info";
    private static final String SCHEDULE_TIMER_TABLE = "schedule_timer_table";
    private static final String CREATSCHEDULETABLE = "CREATE TABLE "
            + SCHEDULE_TIMER_TABLE + "(" + TIMER_INDEX + " INTEGER PRIMARY KEY,"
            + TIMER_ID + " INTEGER," + TIMER_TYPE + " INTEGER," + ORIGINAL + " INTEGER," + START_TIME
            + " INTEGER," + END_NOTIRY_TIME + " INTEGER," + BROADCAST_FILTER + " TEXT,"
            + DATA_INFO + " TEXT" + ");";//2015-1-21
    private static final String DROPSCHEDULETABLE = "DROP TABLE IF EXISTS "
            + SCHEDULE_TIMER_TABLE;

    private SQLiteDatabase timerSheduleDB = null;
    private DatabaseHelper dbHelper = null;
    private static TimerStorge timerStorgeInstance = null;
    private File timerDBInfofile = null;
    private int dataBase_version = 2012112001;

    private static final String TAG = "dtvservice.TimerSheduleStorge";


    public synchronized static TimerStorge getInstance(Context context) {
        if (timerStorgeInstance == null) {
            timerStorgeInstance = new TimerStorge(context);
            timerStorgeInstance.init(context);
        }
        return timerStorgeInstance;
    }

    /**
     * @param context
     */
    private TimerStorge(Context context) {
        if (timerDBInfofile == null) {
            timerDBInfofile = new File("/data/dtv/timerDBInfo.txt");
        }

        if (isFileExists(timerDBInfofile)) {
            dataBase_version = readFile(timerDBInfofile);
            Log.i(TAG, "文件中数据库版本:" + dataBase_version);
            if (dataBase_version < DATABASE_VERSION) {
                dataBase_version = DATABASE_VERSION;
            }
        }

        if (this.dbHelper == null) {
            context.deleteDatabase(DATABASE_NAME);
            Log.i(TAG, "新的数据库版本:" + dataBase_version);
            this.dbHelper = new DatabaseHelper(context, dataBase_version);
        }
//		this.timerSheduleDB = dbHelper.getWritableDatabase();
        this.timerSheduleDB = null;
    }

    private void init(Context context) {

        int dbVersion = this.dbHelper.getWritableDatabase().getVersion();
        Log.i(TAG, "the DB version is: " + dbVersion);
        Log.i(TAG, "file's version is: " + readFile(timerDBInfofile));

        if (isFileExists(timerDBInfofile) && (dbVersion == readFile(timerDBInfofile))) {
            Log.i(TAG, "the DBInfofile is exist and db version is equals file's version ,so return");
            return;
        } else if (!isFileExists(timerDBInfofile)) {
            try {
                Log.i(TAG, "there is no DBInfofile ,so upgradeDB");
                upGradeTimerDB();
                timerDBInfofile.createNewFile();
                writeFile(timerDBInfofile, dbVersion);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "db version is not the same as file's version, so upgradeDB");
            upGradeTimerDB();
            writeFile(timerDBInfofile, dbVersion);
        }
    }

    private int readFile(File souFile) {
        try {
            FileInputStream fin = new FileInputStream(souFile);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            String res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
            return Integer.valueOf(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void writeFile(File dirfile, int newVersion) {
        try {
            FileOutputStream fout = new FileOutputStream(dirfile);
            byte[] bytes = (newVersion + "").getBytes();
            fout.write(bytes);
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isFileExists(File file) {
        try {
            if (!file.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void flushMedia() {
//		try {
//			Runtime.getRuntime().exec("sync");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    }

    private void insertTimerRecord(TimerInfo timer) {
        ContentValues val = new ContentValues();
        val.clear();
        val.put(TIMER_TYPE, Integer.valueOf(timer.miType));
        val.put(ORIGINAL, Integer.valueOf(timer.mOriginal));//2015-1-21
        val.put(START_TIME, Long.valueOf(timer.mlStartNotifyTime));
        val.put(END_NOTIRY_TIME, Long.valueOf(timer.mlNotNotifyTime));
        val.put(BROADCAST_FILTER, timer.mstrTriggerBroadCast);
        val.put(DATA_INFO, timer.mByteDataInfo);
        if (this.timerSheduleDB != null) {
            this.timerSheduleDB.insert(SCHEDULE_TIMER_TABLE, null, val);
//			this.flushMedia();
        }
    }

    private void updateTimerRecord(TimerInfo timer) {
        ContentValues val = new ContentValues();
        val.clear();
        val.put(TIMER_TYPE, Integer.valueOf(timer.miType));
        val.put(ORIGINAL, Integer.valueOf(timer.mOriginal));//2015-1-21
        val.put(START_TIME, Long.valueOf(timer.mlStartNotifyTime));
        val.put(END_NOTIRY_TIME, Long.valueOf(timer.mlNotNotifyTime));
        val.put(BROADCAST_FILTER, timer.mstrTriggerBroadCast);
        val.put(DATA_INFO, timer.mByteDataInfo);
        if (this.timerSheduleDB != null) {
            this.timerSheduleDB.update(SCHEDULE_TIMER_TABLE, val, null, null);
//			this.flushMedia();
        }
    }

    private Cursor queryTimerRecords() {
        if (this.timerSheduleDB != null) {
            Cursor cursor = this.timerSheduleDB.query(SCHEDULE_TIMER_TABLE,
                    new String[]{TIMER_INDEX, TIMER_TYPE, ORIGINAL,
                            START_TIME, END_NOTIRY_TIME,
                            BROADCAST_FILTER, DATA_INFO}, null, null, null, null,
                    null);

            return cursor;
        }
        return null;
    }

    private List<TimerInfo> getTimerListFromDB() {
        this.timerSheduleDB = dbHelper.getWritableDatabase();
        if (null == this.timerSheduleDB) {
            return null;
        }

        List<TimerInfo> timerInfos = new ArrayList<TimerInfo>();
        Cursor cursor = queryTimerRecords();
        if (cursor == null) {
            return null;
        }

        try {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(TIMER_INDEX);
            int itype = cursor.getColumnIndex(TIMER_TYPE);
            int ioriginal = cursor.getColumnIndex(ORIGINAL);//2015-1-21
            int starTime = cursor.getColumnIndex(START_TIME);
            int endNotifyTime = cursor.getColumnIndex(END_NOTIRY_TIME);
            int broadcast = cursor.getColumnIndex(BROADCAST_FILTER);
            int datainfo = cursor.getColumnIndex(DATA_INFO);

            for (int i = 0; i < cursor.getCount(); i++) {
                TimerInfo timer = new TimerInfo();
                timer.miIndex = cursor.getInt(index);
                timer.miType = cursor.getInt(itype);
                timer.mOriginal = cursor.getInt(ioriginal);//2015-1-21
                timer.mlStartNotifyTime = cursor.getLong(starTime);
                timer.mlNotNotifyTime = cursor.getLong(endNotifyTime);
                timer.mstrTriggerBroadCast = cursor.getString(broadcast);
                timer.mByteDataInfo = cursor.getBlob(datainfo);
                timerInfos.add(timer);
                cursor.moveToNext();
            }

            Collections.sort(timerInfos, new Comparator<TimerInfo>() {
                public int compare(TimerInfo arg0, TimerInfo arg1) {
                    // TODO Auto-generated method stub
                    return (int) (arg0.mlStartNotifyTime - arg1.mlStartNotifyTime);
                }
            });

        } finally {
            if (cursor != null)
                cursor.close();
        }

        this.timerSheduleDB.close();
        this.timerSheduleDB = null;
        return timerInfos;
    }

    /**  **/
    public synchronized List<TimerInfo> getTimerRecordsFromDB() {
        return getTimerListFromDB();
    }

    private boolean isStartTimeConfilct(long startTime) {
        Cursor cur = null;
        long seconds = startTime;
        cur = timerSheduleDB.query(SCHEDULE_TIMER_TABLE,
                null,
                START_TIME + " = " + seconds,
                null,
                null,
                null,
                null);
        if (cur == null) {
            return false;
        }
        if (cur.getCount() > 0) {
            cur.close();
            return true;
        }
        cur.close();
        return false;

    }

    public synchronized boolean setTimersRecordsToDB(List<TimerInfo> timerList) {
        boolean succes = false;

        /**  **/
        List<TimerInfo> dblist = getTimerRecordsFromDB();
        TimerInfo tmpTimer = null;

        this.timerSheduleDB = dbHelper.getWritableDatabase();
        if (null == this.timerSheduleDB) {
            return false;
        }

        for (int i = 0; i < dblist.size(); i++) {
            tmpTimer = dblist.get(i);
            int j;
            for (j = 0; j < timerList.size(); j++) {
                if (tmpTimer.IsSame(timerList.get(j))) {
                    break;
                }
            }
            /** timerList.size() **/
            if (j >= timerList.size()) {
                Log.i(TAG, "delete time");
                String where = TIMER_INDEX + "=" + Integer.toString(tmpTimer.miIndex);
                Log.i(TAG, "[where]" + where);
                this.timerSheduleDB.delete(SCHEDULE_TIMER_TABLE, where, null);
                dblist.remove(tmpTimer);
            }

            if (dblist.size() == 0) {
                this.timerSheduleDB.execSQL(DROPSCHEDULETABLE);
                Log.i(TAG, "表存在");

                /** 创建数据库表 **/
                this.timerSheduleDB.execSQL(CREATSCHEDULETABLE);
            }
        }

        /** 添加预约 **/
        if (null != timerList) {
            for (int i = 0; i < timerList.size(); i++) {
                tmpTimer = timerList.get(i);
                int j;
                for (j = 0; j < dblist.size(); j++) {
                    if (tmpTimer.IsSame(dblist.get(j))) {
                        break;
                    }
                }

                if (j >= dblist.size()) {
                    Log.i(TAG, "intert timer");
                    insertTimerRecord(tmpTimer);
                }
            }
            succes = true;
        }

        this.timerSheduleDB.close();
        this.timerSheduleDB = null;

        return succes;
    }

    private synchronized void closeStorageDB() {
        if (this.timerSheduleDB != null) {
            this.timerSheduleDB.close();
            this.timerSheduleDB = null;
        }
        if (this.dbHelper != null) {
            this.dbHelper.close();
            this.dbHelper = null;
        }
    }

    private boolean upGradeTimerDB() {
        if (this.dbHelper != null) {
            this.timerSheduleDB = dbHelper.getWritableDatabase();
            this.timerSheduleDB.execSQL(DROPSCHEDULETABLE);
            this.timerSheduleDB.execSQL(CREATSCHEDULETABLE);
            Log.i("YangLiu", "updateTable succuce!");
            return true;
        } else {
            Log.i("YangLiu", "updateTable filed because dbHelper is null!");
            return false;
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

		/*DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}*/

        DatabaseHelper(Context context, int version) {
            super(context, DATABASE_NAME, null, version);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATSCHEDULETABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "数据库版本更新：" + oldVersion + "-" + newVersion);
            db.execSQL(DROPSCHEDULETABLE);
            onCreate(db);
        }
    }
}
