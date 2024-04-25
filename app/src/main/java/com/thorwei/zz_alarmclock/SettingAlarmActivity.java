package com.thorwei.zz_alarmclock;

import static com.thorwei.zz_alarmclock.MainActivity.TAG;

import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class SettingAlarmActivity extends AppCompatActivity{
    Switch switchVibration;
    EditText edittextTeg;
    Button btnCencel, btnDelete, btnSave;
    Spinner spnRepeat;
    public static TextView tvHours;
    public static TextView tvMin;
    private AlarmModel alarmclock;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreate SettingAlarmActivity");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_alarm);

        //id = getIntent().getIntExtra(ALARM_ID, 0);

        tvHours = (TextView) findViewById(R.id.alarm_time_hours);
        tvMin = (TextView) findViewById(R.id.alarm_time_min);

        btnCencel = (Button) findViewById(R.id.btn_cencel);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnSave = (Button) findViewById(R.id.btn_save);

        btnDelete.setVisibility(View.VISIBLE);

        spnRepeat = (Spinner) findViewById(R.id.spinner_repeat);
        edittextTeg = (EditText) findViewById(R.id.edittext_teg);
        switchVibration = (Switch) findViewById(R.id.switch_vibration);

        alarmclock = (AlarmModel)getIntent().getSerializableExtra("AlarmModel");
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvHours.setText(String.format("%02d",alarmclock.hour));
        tvMin.setText(String.format("%02d",alarmclock.minute));
        edittextTeg.setText(""+alarmclock.tag);
        spnRepeat.setSelection(alarmclock.repeat);
        switchVibration.setChecked(alarmclock.vibrate);
    }

    public void AlarmCencel(View view) {
        finish();
    }
    public void AlarmDelete(View view) {
        AlarmManagerHelper.cancelAlarmClock(this, alarmclock.id);
        AlarmDBUtils.deleteAlarmClock(this, alarmclock.id);
        finish();
    }
    public void AlarmSave(View view) {
        //Log.e(TAG,"AlarmSave");
        alarmclock.setTag("" + edittextTeg.getText());
        alarmclock.setRepeat(spnRepeat.getSelectedItemPosition());
        alarmclock.setVibrate(switchVibration.isChecked());
        AlarmDBUtils.updateAlarmClock(this, alarmclock);
        finish();
    }
}
