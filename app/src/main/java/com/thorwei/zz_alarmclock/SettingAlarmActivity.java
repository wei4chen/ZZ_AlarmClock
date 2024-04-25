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

    TextView toolbarTitle;
    CardView cvRepeat;
    TextView tvRepeat;
    CardView cvRing;
    TextView tvRingtones;
    CardView cvRemind;
    TextView tvRemind;
    Switch switchVibration;
    EditText edittextTeg;
    Button btnCencel, btnDelete, btnSave;

    Spinner spnRepeat;
    public static TextView tvHours;
    public static TextView tvMin;

    private static AlarmClockLab alarmClockLab;

    private static final String ALARM_ID = "id";
    private int position, id;
    private List<AlarmModel> alarmList;
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

        Bundle bundle = getIntent().getBundleExtra("alarmclock");
        position = bundle.getInt("position");
        id = bundle.getInt("id");
        alarmclock = new AlarmClockBuilder().builder(id);
        alarmclock.id = bundle.getInt("id");
        alarmclock.enable = bundle.getBoolean("enable");
        alarmclock.hour = bundle.getInt("hour");
        alarmclock.minute = bundle.getInt("minute");
        alarmclock.repeat = bundle.getInt("repeat");
        alarmclock.tag = bundle.getString("tag");
        alarmclock.ringPosition = bundle.getInt("ringPosition");
        alarmclock.ring = bundle.getString("ring");
        alarmclock.vibrate = bundle.getBoolean("vibrate");
        alarmclock.remind = bundle.getBoolean("remind");


/*
        alarmClockLab = new AlarmClockBuilder().builderLab(id);
        alarmClockLab.setHour(alarmClockLab.hour);
        alarmClockLab.setMinute(alarmClockLab.minute);
        alarmClockLab.setRepeat(alarmClockLab.repeat);
        alarmClockLab.setTag(alarmClockLab.tag);
        alarmClockLab.setRingPosition(alarmClockLab.ringPosition);
        alarmClockLab.setRing(alarmClockLab.ring);
        alarmClockLab.setVibrate(alarmClockLab.vibrate);
*/
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

    public void AlarmCencel(View view) {
        finish();
        //onBackPressed();
    }
    public void AlarmDelete(View view) {
        AlarmManagerHelper.cancelAlarmClock(this, id);
        AlarmDBUtils.deleteAlarmClock(this, id);
        finish();
    }
    public void AlarmSave(View view) {
        Log.e(TAG,"AlarmSave");
        /*
        if(edittextTeg.getText() != null && edittextTeg.getText().length() != 0) {
            //Log.e(TAG,"edittextTeg["+edittextTeg.getText()+"]");
            alarmClockLab.setTag("" + edittextTeg.getText());
        }
        AlarmDBUtils.updateAlarmClock(SettingAlarmActivity.this, alarmClockLab);
        AlarmModel alarm = AlarmDBUtils.queryAlarmClock(SettingAlarmActivity.this).get(0);
        if (alarm.enable) {
            AlarmManagerHelper.startAlarmClock(SettingAlarmActivity.this, alarm);
        }
        */
        alarmclock.setTag("" + edittextTeg.getText());
        alarmclock.setRepeat(spnRepeat.getSelectedItemPosition());
        alarmclock.setVibrate(switchVibration.isChecked());

        getIntent().putExtra("position", position);
        getIntent().putExtra("id", id);
        getIntent().putExtra("tag", alarmclock.tag);
        getIntent().putExtra("repeat", alarmclock.repeat);
        getIntent().putExtra("vibrate", alarmclock.vibrate);
        setResult(RESULT_OK,getIntent());
        finish();
    }
}
