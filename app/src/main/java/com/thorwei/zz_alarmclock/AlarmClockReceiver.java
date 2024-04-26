package com.thorwei.zz_alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AlarmClockReceiver extends BroadcastReceiver{
    public static final String ALARM_CLOCK = "alarm_clock";

    public AlarmClockReceiver() {
        Log.d(MainActivity.TAG, "AlarmClockReceiver");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MainActivity.TAG, "onReceive");
        Intent bootIntent = new Intent(context, BootAlarmActivity.class);
        bootIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        bootIntent.putExtra(ALARM_CLOCK, (AlarmModel)intent.getSerializableExtra(ALARM_CLOCK));
        context.startActivity(bootIntent);
    }
}
