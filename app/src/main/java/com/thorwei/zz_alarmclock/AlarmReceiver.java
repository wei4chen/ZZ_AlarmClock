package com.thorwei.zz_alarmclock;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    public final String TAG = "weitest";
    public static final String ALARM_CLOCK = "alarm_clock";
    public void onReceive(Context context, Intent intent) {
        Bundle bData = intent.getExtras();

        if(bData.get("title").equals("activity_app"))
        {
            //主要執行的程式
            Log.e(TAG,"AlarmReceiver_onReceive");
            Intent bootIntent = new Intent(context, BootAlarmActivity.class);
            bootIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            AlarmModel alarm = (AlarmModel)intent.getSerializableExtra(ALARM_CLOCK);
            if(alarm == null)
                Log.e(MainActivity.TAG, "alarm:null");
            else
                Log.e(MainActivity.TAG, "alarm_id:"+alarm.id);
            bootIntent.putExtra(ALARM_CLOCK, alarm);
            context.startActivity(bootIntent);

        }
    }
}
