package com.thorwei.zz_alarmclock;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AlarmClockService extends Service{
    private List<AlarmModel> alarmList;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    //    run(this, AlarmClockReceiver.class, 60);
        startTimeTask();
    }
    private void startTimeTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                alarmList = AlarmDBUtils.queryAlarmClock(AlarmClockService.this);
                for (AlarmModel alarm : alarmList) {
                    if (alarm.enable) {
                        AlarmManagerHelper.startAlarmClock(AlarmClockService.this, alarm);
                    }
                }
            }
        }).start();
    }

//=====================================================================   
    public static void run(final Context context, final Class<?> daemonServiceClazz, final int interval) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                install(context, "bin", "daemon");
                start(context, daemonServiceClazz, interval);
            }
        }).start();
    }
    public static boolean install(Context context, String destDir, String filename) {
        String abi = Build.CPU_ABI;
        if (!abi.startsWith("arm")) {
            return false;
        }

        try {
            File f = new File(context.getDir(destDir, Context.MODE_PRIVATE), filename);
            if (f.exists()) {
                return false;
            }
            copyAssets(context, filename, f, "0755");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void copyAssets(Context context, String assetsFilename, File file, String mode) throws IOException, InterruptedException {
        AssetManager manager = context.getAssets();
        final InputStream is = manager.open(assetsFilename);
        copyFile(file, is, mode);
    }

    private static void copyFile(File file, InputStream is, String mode) throws IOException, InterruptedException {
        final String abspath = file.getAbsolutePath();
        final FileOutputStream out = new FileOutputStream(file);
        byte buf[] = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        is.close();
        Runtime.getRuntime().exec("chmod " + mode + " " + abspath).waitFor();
    }

    private static void start(Context context, Class<?> daemonClazzName, int interval) {
        String cmd = context.getDir("bin", Context.MODE_PRIVATE).getAbsolutePath() + File.separator + "daemon";
        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append(cmd);
        cmdBuilder.append(" -p ");
        cmdBuilder.append(context.getPackageName());
        cmdBuilder.append(" -s ");
        cmdBuilder.append(daemonClazzName.getName());
        cmdBuilder.append(" -t ");
        cmdBuilder.append(interval);

        try {
            Runtime.getRuntime().exec(cmdBuilder.toString()).waitFor();
        } catch (IOException | InterruptedException e) {
        }
    }

    public static class DaemonInnerService extends Service {
        @Override
        public void onCreate() {
            super.onCreate();
        }
        @SuppressLint("ForegroundServiceType")
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(-1001, new Notification());
            //stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
        @Override
        public IBinder onBind(Intent intent) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }
}
