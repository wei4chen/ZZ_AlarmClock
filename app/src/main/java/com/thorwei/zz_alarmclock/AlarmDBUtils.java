package com.thorwei.zz_alarmclock;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//import com.softmiracle.weatheralarmclock.alarm.AlarmClockBuilder;

import java.util.ArrayList;
import java.util.List;

public class AlarmDBUtils {
    public static final String DB_NAME = "AlarmClock.db";
    public static final int DB_VERSION = 1;
    
    public static SQLiteDatabase openAlarmClock(Context context) {
        AlarmDBHelper alarmDBHelper = new AlarmDBHelper(context, DB_NAME, null, DB_VERSION);
        return alarmDBHelper.getWritableDatabase();
    }

    public static ContentValues getContentValues(AlarmModel alarm) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ENABLE", alarm.enable);
        contentValues.put("MINUTE", alarm.minute);
        contentValues.put("HOUR", alarm.hour);
        contentValues.put("REPEAT", alarm.repeat);
        contentValues.put("TAG", alarm.tag);
        contentValues.put("RING_POSITION", alarm.ringPosition);
        contentValues.put("RING", alarm.ring);
        contentValues.put("VIBRATE", alarm.vibrate);
        return contentValues;
    }

    public static void insertAlarmClock(Context context, AlarmModel alarm) {
        ContentValues contentValues = getContentValues(alarm);

        openAlarmClock(context).insert(AlarmDBHelper.ALARM_TABLE, null, contentValues);
        contentValues.clear();
    }
    
    public static void updateAlarmClock(Context context, AlarmModel alarm) {
        ContentValues contentValues = getContentValues(alarm);
        openAlarmClock(context).update(AlarmDBHelper.ALARM_TABLE, contentValues, "ID=" + alarm.id, null);
        contentValues.clear();
    }

    public static void deleteAlarmClock(Context context, int id) {
        openAlarmClock(context).delete(AlarmDBHelper.ALARM_TABLE, "ID=" + id, null);
    }
    
    @SuppressLint("Range")
    public static List<AlarmModel> queryAlarmClock(Context context) {
        List<AlarmModel> alarmList = new ArrayList<>();
        Cursor cursor = openAlarmClock(context).query(AlarmDBHelper.ALARM_TABLE, null, null, null, null, null, "ID DESC");
        while (cursor.moveToNext()) {
            AlarmModel alarm;
            AlarmClockBuilder builder = new AlarmClockBuilder();
            alarm = builder.enable(valueOf(cursor.getInt(cursor.getColumnIndex("ENABLE"))))
                    .minute(cursor.getInt(cursor.getColumnIndex("MINUTE")))
                    .hour(cursor.getInt(cursor.getColumnIndex("HOUR")))
                    .repeat(cursor.getInt(cursor.getColumnIndex("REPEAT")))
                    .tag(cursor.getString(cursor.getColumnIndex("TAG")))
                    .ringPosition(cursor.getInt(cursor.getColumnIndex("RING_POSITION")))
                    .ring(cursor.getString(cursor.getColumnIndex("RING")))
                    .vibrate(valueOf(cursor.getInt(cursor.getColumnIndex("VIBRATE"))))
                    .builder(cursor.getInt(cursor.getColumnIndex("ID")));
            alarmList.add(alarm);
        }
        cursor.close();
        return alarmList;
    }

    public static boolean valueOf(int i) {
        return i == 1;
    }
}
