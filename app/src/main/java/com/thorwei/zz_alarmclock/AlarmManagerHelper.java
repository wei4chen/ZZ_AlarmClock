package com.thorwei.zz_alarmclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;


public class AlarmManagerHelper {
    public static final String ALARM_CLOCK = "alarm_clock";
    
    public static void startAlarmClock(Context context, AlarmModel alarm) {
        /*
        Intent intent = new Intent(context, BootAlarmActivity.class);
        intent.putExtra(ALARM_CLOCK, (CharSequence) alarm);
        PendingIntent pi = PendingIntent.getActivity(context, alarm.id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long nextTime = calculateNextTime(alarm.hour,
                alarm.minute, getWeeks(alarm));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTime, pi);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pi);
        }
        */
    }
    

    private static String getWeeks(AlarmModel alarm) {
        StringBuilder weekBuilder = new StringBuilder();
        if ((alarm.repeat & 0x1<<0) != 0) {
            weekBuilder.append("1,");
        }
        if ((alarm.repeat & 0x1<<1) != 0) {
            weekBuilder.append("2,");
        }
        if ((alarm.repeat & 0x1<<2) != 0) {
            weekBuilder.append("3,");
        }
        if ((alarm.repeat & 0x1<<3) != 0) {
            weekBuilder.append("4,");
        }
        if ((alarm.repeat & 0x1<<4) != 0) {
            weekBuilder.append("5,");
        }
        if ((alarm.repeat & 0x1<<5) != 0) {
            weekBuilder.append("6,");
        }
        if ((alarm.repeat & 0x1<<6) != 0) {
            weekBuilder.append("7,");
        }
        String week = weekBuilder.toString();
        try {
            week.substring(0, week.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (week == "") {
            week = null;
        }
        return week;
    }

    public static long calculateNextTime(int hour, int minute, String weeks) {

        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long nextTime = calendar.getTimeInMillis();

        if (weeks == null) {

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
            final String[] weeksValue = weeks.split(",");
            for (String aWeeksValue : weeksValue) {
                int week = Integer.parseInt(aWeeksValue);
                calendar.set(Calendar.DAY_OF_WEEK, week);
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
    }

    public static void cancelAlarmClock(Context context, int alarmClockCode) {
        Intent intent = new Intent(context, BootAlarmActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, alarmClockCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context
                .getSystemService(Activity.ALARM_SERVICE);
        am.cancel(pi);
    }
    
}
