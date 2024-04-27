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

    private static AlarmModel alarmclock;
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent bootIntent = new Intent(context, BootAlarmActivity.class);
        bootIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        bootIntent.putExtra(ALARM_CLOCK, intent.getSerializableExtra(ALARM_CLOCK));
        Log.d("TAG", "receiver");
        context .startActivity(bootIntent);


/*
        Log.d(MainActivity.TAG, "onReceive");
        if(intent.getAction().equals("AlarmClock")) {
            Intent bootIntent = new Intent(context, BootAlarmActivity.class);
            bootIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            AlarmModel alarm = (AlarmModel) intent.getSerializableExtra(ALARM_CLOCK);
            if(alarm == null)
                Log.e(MainActivity.TAG, "alarm:null");
            else
                Log.e(MainActivity.TAG, "alarm_id:"+alarm.id);
            bootIntent.putExtra(ALARM_CLOCK, (AlarmModel)intent.getSerializableExtra(ALARM_CLOCK));
            context.startActivity(bootIntent);
        }
*/



    }
}
