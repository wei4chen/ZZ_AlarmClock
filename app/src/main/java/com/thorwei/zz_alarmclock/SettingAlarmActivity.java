package com.thorwei.zz_alarmclock;

import static com.thorwei.zz_alarmclock.MainActivity.TAG;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class SettingAlarmActivity extends AppCompatActivity{
    Switch switchVibration;
    EditText edittextTeg;
    Button btnCencel, btnDelete, btnSave;
    TextView RingName;
    Spinner spnRepeat;
    public static TextView tvHours;
    public static TextView tvMin;
    private static AlarmModel alarmclock;

    //private static AlarmClockLab alarmClockLab;

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
        btnDelete.setVisibility(View.VISIBLE);
        btnSave = (Button) findViewById(R.id.btn_save);
        RingName = (TextView) findViewById(R.id.ringtones_name);
        spnRepeat = (Spinner) findViewById(R.id.spinner_repeat);
        edittextTeg = (EditText) findViewById(R.id.edittext_teg);
        switchVibration = (Switch) findViewById(R.id.switch_vibration);

        alarmclock = (AlarmModel)getIntent().getSerializableExtra("AlarmModel");
        //alarmClockLab = new AlarmClockBuilder().builderLab(alarmclock.id);

        tvHours.setText(String.format("%02d",alarmclock.hour));
        tvMin.setText(String.format("%02d",alarmclock.minute));
        edittextTeg.setText(""+alarmclock.tag);
        spnRepeat.setSelection(alarmclock.repeat);
        switchVibration.setChecked(alarmclock.vibrate);
        RingName.setText(alarmclock.ring);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        if (alarmclock.enable) {
            AlarmManagerHelper.startAlarmClock(this, alarmclock);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                alarmclock.ringPosition = data.getIntExtra("position", 0);
                alarmclock.ring = data.getStringExtra("ring");
                RingName.setText(alarmclock.ring);
            }
        }
    }

    public void OnRingClick(View view) {
        Intent intent = new Intent(this, RingActivity.class);
        //startActivity(intent);
        startActivityForResult(intent, 1);
    }
    public void OnTimeClick(View view) {
        //Log.e(TAG,"OnTimeClick");
        DialogFragment dialogFragment = new TimePickerFragment();
        dialogFragment.show(getSupportFragmentManager(), "timePicker");
    }
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        public TimePickerFragment() {
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new TimePickerDialog(getActivity(), this, alarmclock.hour, alarmclock.minute, DateFormat.is24HourFormat(getActivity()));
        }
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String min = String.format("%02d",minute);
            String hour = String.format("%02d",hourOfDay);

            tvHours.setText(hour);
            tvMin.setText(min);
            alarmclock.setMinute(minute);
            alarmclock.setHour(hourOfDay);
        }
    }
}
