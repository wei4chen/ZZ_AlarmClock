package com.thorwei.zz_alarmclock;

import static com.thorwei.zz_alarmclock.MainActivity.TAG;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
    Spinner spnRepeat;
    public static TextView tvHours;
    public static TextView tvMin;
    private static AlarmModel alarmclock;
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

        tvHours.setText(String.format("%02d",alarmclock.hour));
        tvMin.setText(String.format("%02d",alarmclock.minute));
        edittextTeg.setText(""+alarmclock.tag);
        spnRepeat.setSelection(alarmclock.repeat);
        switchVibration.setChecked(alarmclock.vibrate);
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
