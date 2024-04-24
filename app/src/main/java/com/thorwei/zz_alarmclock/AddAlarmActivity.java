package com.thorwei.zz_alarmclock;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddAlarmActivity extends AppCompatActivity{

    TextView toolbarTitle;
    CardView cvRepeat;
    TextView tvRepeat;
    CardView cvRing;
    TextView tvRingtones;
    CardView cvRemind;
    TextView tvRemind;
    Switch switchVibration;

    public static TextView tvHours;
    public static TextView tvMin;

    private static AlarmClockLab alarmClockLab;

    public static Intent newIntent(Context context) {
        return new Intent(context, AddAlarmActivity.class);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        tvHours = (TextView) findViewById(R.id.alarm_time_hours);
        tvMin = (TextView) findViewById(R.id.alarm_time_min);

        int[] currentTime = getCurrentTime();
        alarmClockLab = new AlarmClockBuilder().builderLab(0);
        alarmClockLab.setEnable(true);
        alarmClockLab.setHour(currentTime[0]);
        alarmClockLab.setMinute(currentTime[1]);
        alarmClockLab.setRepeat(0);
        alarmClockLab.setTag(""+R.string.bar_name);
        alarmClockLab.setRingPosition(0);
        alarmClockLab.setRing(firstRing(this));
        alarmClockLab.setVibrate(false);
    //    alarmClockLab.setRemind(false);

        tvRingtones.setText(alarmClockLab.ring);
        switchVibration.setChecked(alarmClockLab.vibrate);
    //    cvRepeat.setOnClickListener(this);
    //    cvRing.setOnClickListener(this);
    //    cvRemind.setOnClickListener(this);

        int hour = alarmClockLab.hour;
        int minute = alarmClockLab.minute;

        String h = String.valueOf(hour);
        String m = String.valueOf(minute);

        if (minute < 10) {
            m = "0" + minute;
        }
        if (hour < 10) {
            h = "0" + hour;
        }
        tvHours.setText(h);
        tvMin.setText(m);

        switchVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarmClockLab.setVibrate(isChecked);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvRingtones.setText(alarmClockLab.ring);
        tvRepeat.setText(getRepeatString(alarmClockLab.repeat));
    }
    private String getRepeatString(int repeat) {
        String remindString = "";
        if (repeat == 0) {
            remindString = getString(R.string.alarmRepeatChoice);
        } else if (repeat == 127) {
            remindString = getString(R.string.alarmRepeatEveryDay);
        }
        return remindString;
    }
    private String firstRing(Context context) {
        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = ringtoneManager.getCursor();
        String ringName = null;
        while (cursor.moveToNext()) {
            ringName = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            if (ringName != null) {
                break;
            }
        }
        cursor.close();
        return ringName;
    }
    private int[] getCurrentTime() {
        Calendar time = Calendar.getInstance();
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int minute = time.get(Calendar.MINUTE);
        return new int[]{hour, minute};
    }
}
