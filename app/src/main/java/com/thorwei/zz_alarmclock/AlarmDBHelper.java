package com.thorwei.zz_alarmclock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AlarmDBHelper extends SQLiteOpenHelper {

    public static final String ALARM_TABLE = "ALARM_CLOCK";
    public static final String CREATE_ALARM = "CREATE TABLE " + ALARM_TABLE + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ENABLE BOOLEAN, " +
            "MINUTE INTEGER, " +
            "HOUR INTEGER, " +
            "REPEAT INTEGER, " +
            "TAG TEXT, " +
            "RING_POSITION INTEGER, " +
            "RING TEXT, " +
            "VIBRATE BOOLEAN " +
//            "WEATHER BOOLEAN " +
            ")";

    public AlarmDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}
