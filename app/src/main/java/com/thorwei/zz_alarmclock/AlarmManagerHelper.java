package com.thorwei.zz_alarmclock;

import static com.thorwei.zz_alarmclock.MainActivity.TAG;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;


public class AlarmManagerHelper {
    public static final String ALARM_CLOCK = "alarm_clock";
    
    public static void startAlarmClock(Context context, AlarmModel alarm) {
        Log.e(TAG,"startAlarmClock:"+alarm.id);
        Intent intent = new Intent(context, BootAlarmActivity.class);
        intent.putExtra(ALARM_CLOCK, alarm);
        PendingIntent pi = PendingIntent.getActivity(context, alarm.id, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long nextTime = calculateNextTime(alarm.hour, alarm.minute, alarm.repeat);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTime, pi);

/*
        Intent intent = new Intent(context, BootAlarmActivity.class );
        PendingIntent pi = PendingIntent.getActivity(context, alarm.id, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        long time = System.currentTimeMillis()+5*1000;
        manager.set(AlarmManager.RTC_WAKEUP,time,pi);
 */
    }

    public static long calculateNextTime(int hour, int minute, int repeat) {
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long nextTime = calendar.getTimeInMillis();

        if (nextTime > now) {
            return nextTime;
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            nextTime = calendar.getTimeInMillis();
            return nextTime;
        }
        /*
        if(repeat == 0) {
            if (nextTime > now) {
                return nextTime;
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                nextTime = calendar.getTimeInMillis();
                return nextTime;
            }
        } else {
            nextTime = 0;
            long tempTime;
            for (int i = 0; i < 7; i++){
                if((repeat & 0x1<<i) != 0)
                    calendar.set(Calendar.DAY_OF_WEEK, i+1);
                tempTime = calendar.getTimeInMillis();

                if (tempTime <= now) {
                    tempTime += AlarmManager.INTERVAL_DAY * 7;
                }

                if (nextTime == 0) {
                    nextTime = tempTime;
                } else {
                    nextTime = Math.min(tempTime, nextTime);
                }
            }
            return nextTime;
        }
        */
    }

    public static void cancelAlarmClock(Context context, int alarmClockId) {
        Log.e(TAG,"cancelAlarmClock:"+alarmClockId);
        Intent intent = new Intent(context, BootAlarmActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, alarmClockId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
        am.cancel(pi);
    }
    
}
