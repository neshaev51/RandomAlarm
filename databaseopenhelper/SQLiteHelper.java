package com.dvnech.randomalarm.databaseopenhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dvnech.randomalarm.AlarmObject;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Dmitry on 21.09.2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "rootsDatabase.db";
    public static final String TABLE_NAME = "ALARMS";
    public static final String ID = "ID";
    public static final String START_TIME = "START_TIME";
    public static final String END_TIME = "END_TIME";
    public static final String REPEAT_TIMES = "REPEAT_TIMES";
    public static final String SNOOZE = "SNOOZE";
    public static final String SOUND_VOLUME = "SOUND_VOLUME";
    public static final String IS_ALARM_ACTIVE = "IS_ALARM_ACTIVE";
    public static final String ALARM_TITLE = "ALARM_TITLE";
    public static final String IS_VIBRATION_ACTIVE = "IS_VIBRATION_ACTIVE";
    public static final String REPEAT_TIME = "REPEAT_TIME";



    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( "  + ID + " TEXT , " + START_TIME + " TEXT," +
                " " + END_TIME + " TEXT, " +  REPEAT_TIMES + " INTEGER," +  SNOOZE + " INTEGER," + SOUND_VOLUME + " INTEGER,"
                + IS_ALARM_ACTIVE + " INTEGER,"+ ALARM_TITLE + " TEXT,"+ IS_VIBRATION_ACTIVE + " INTEGER," + REPEAT_TIME + " INTEGER"+")");

        Log.i("MyLOG_DATABASE", "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(AlarmObject alarmObject) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues res = new ContentValues();
        res.put(ID,alarmObject.getId());
        res.put(START_TIME,alarmObject.getDateStart());
        res.put(END_TIME,alarmObject.getDateEnd());
        res.put(REPEAT_TIMES,alarmObject.getRepeat());
        res.put(SNOOZE,alarmObject.getSnooze());
        res.put(SOUND_VOLUME,alarmObject.getSoundVolume());
        res.put(ALARM_TITLE,alarmObject.getTitle());
        res.put(REPEAT_TIME,alarmObject.getRepeatTime());
        Log.i("MY_TAG_IN",alarmObject.getTitle() + " title");
        if(alarmObject.isAlarmActive()){
            res.put(IS_ALARM_ACTIVE,1);
        }
        else
            res.put(IS_ALARM_ACTIVE,0);

        if(alarmObject.isVibrationActive()){
            res.put(IS_VIBRATION_ACTIVE,1);
        }
        else
            res.put(IS_VIBRATION_ACTIVE,0);


        Log.i("MY_TAG_DB_IN", DateFormat.getDateTimeInstance().format(new Date(alarmObject.getDateStart())) + " " + alarmObject.getDateStart());
        Log.i("MY_TAG_DB_IN", DateFormat.getDateTimeInstance().format(new Date(alarmObject.getDateEnd())) + " " + alarmObject.getDateEnd());
        long isInserted = db.insert(TABLE_NAME, null, res);
        if (isInserted == -1) {
            Log.i("MY_TAG_SQLITEHELPER","data not added");
            return false;
        } else {
            Log.i("MY_TAG_SQLITEHELPER","data added");
            return true;
        }

    }

    public boolean updateRow(AlarmObject alarmObject){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues res = new ContentValues();
        res.put(ID,alarmObject.getId());
        res.put(START_TIME,alarmObject.getDateStart());
        res.put(END_TIME,alarmObject.getDateEnd());
        res.put(REPEAT_TIMES,alarmObject.getRepeat());
        res.put(SNOOZE,alarmObject.getSnooze());
        res.put(SOUND_VOLUME,alarmObject.getSoundVolume());
        res.put(ALARM_TITLE,alarmObject.getTitle());
        res.put(REPEAT_TIME,alarmObject.getRepeatTime());
        if(alarmObject.isAlarmActive()){
            res.put(IS_ALARM_ACTIVE,1);
        }
        else
            res.put(IS_ALARM_ACTIVE,0);

        if(alarmObject.isVibrationActive()){
            res.put(IS_VIBRATION_ACTIVE,1);
        }
        else
            res.put(IS_VIBRATION_ACTIVE,0);

        db.update(TABLE_NAME, res, "ID=" + alarmObject.getId(), null);
        return true;

    }

    public void removeRow(long id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "ID=" + id, null);
    }
    public long getCountOfRows(){
        SQLiteDatabase db = getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db,TABLE_NAME);
        return count;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }
}