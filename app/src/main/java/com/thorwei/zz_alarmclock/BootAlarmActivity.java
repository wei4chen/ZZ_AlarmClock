package com.thorwei.zz_alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Objects;

public class BootAlarmActivity extends AppCompatActivity {
    public final String TAG = "weitest";
    public static final String ALARM_CLOCK = "alarm_clock";
    RelativeLayout rlAlarmOff, rlRemind;
    private AlarmModel alarm;
    private MediaPlayer mediaPlayer;
    private Vibrator vibration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG,"BootAlarmActivity_onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_boot_alarm);

        alarm = (AlarmModel) getIntent().getSerializableExtra(ALARM_CLOCK);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        TextView tv_tag = (TextView)findViewById(R.id.tv_tag);
        tv_tag.setText(alarm.tag);

        rlRemind = (RelativeLayout)findViewById(R.id.rl_boot_remind);
        rlAlarmOff = (RelativeLayout)findViewById(R.id.rl_boot_alarm_off);

        startPlayingRing();
        if (alarm.vibrate) {
            startVibrate();
        }
        if(alarm.remind) {
            rlRemind.setVisibility(View.VISIBLE);
            rlRemind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //AlarmManagerHelper.remindAlarmClock(BootAlarmActivity.this, alarm);
                    stopPlayRing();
                    if (alarm.vibrate) {
                        stopVibrate();
                    }
                    long nextTime = System.currentTimeMillis() + 1000 * 60 * 5;
                    Intent intent = new Intent(BootAlarmActivity.this, BootAlarmActivity.class);
                    intent.putExtra(ALARM_CLOCK, alarm);
                    PendingIntent pi = PendingIntent.getActivity(BootAlarmActivity.this, alarm.id, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) BootAlarmActivity.this.getSystemService(ALARM_SERVICE);

                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTime, pi);
                    Log.d("weitest", nextTime + "");
                    finish();
                }
            });
        } else {
            rlRemind.setVisibility(View.GONE);
        }
        rlAlarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarm.repeat==0) {
                    alarm.enable=false;
                    AlarmDBUtils.updateAlarmClock(BootAlarmActivity.this, alarm);
                } else {
                    AlarmManagerHelper.startAlarmClock(BootAlarmActivity.this, alarm);
                }
                finish();
            }
        });
    }

    private void startVibrate() {
        vibration = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern = new long[]{1000, 1000, 1000, 1000};
        vibration.vibrate(pattern, 2);
    }
    private void stopVibrate() {
        vibration.cancel();
    }

    private void stopPlayRing() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    private void startPlayingRing() {
        RingtoneManager ringtoneManager = new RingtoneManager(this);
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        ringtoneManager.getCursor();

        Uri ringtoneUri = ringtoneManager.getRingtoneUri(alarm.ringPosition);
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 7, AudioManager.FLAG_SHOW_UI);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, ringtoneUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setLooping(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopPlayRing();
        if (alarm.vibrate) {
            stopVibrate();
        }
    }
}
