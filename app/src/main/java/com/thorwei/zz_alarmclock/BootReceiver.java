package com.thorwei.zz_alarmclock;

import static com.thorwei.zz_alarmclock.MainActivity.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras().get("title").equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.e(TAG,"onReceive");
            context.startService(new Intent(context, AlarmClockService.class));
        }
    }
}
