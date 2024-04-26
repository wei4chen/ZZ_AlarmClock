package com.thorwei.zz_alarmclock;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class BootAlarmActivity extends AppCompatActivity {
    public static final String ALARM_CLOCK = "alarm_clock";
    RelativeLayout rvAlarmOff;
    private AlarmModel alarm;
    private MediaPlayer mediaPlayer;
    private Vibrator vibration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot_alarm);

        alarm = (AlarmModel) getIntent().getSerializableExtra(ALARM_CLOCK);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        rvAlarmOff = (RelativeLayout)findViewById(R.id.rl_boot_alarm_off);

        startPlayingRing();
        if (alarm.vibrate) {
            startVibrate();
        }
        rvAlarmOff.setOnClickListener(new View.OnClickListener() {
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
