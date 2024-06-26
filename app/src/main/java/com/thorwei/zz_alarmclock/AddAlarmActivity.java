package com.thorwei.zz_alarmclock;

import static com.thorwei.zz_alarmclock.MainActivity.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class AddAlarmActivity extends AppCompatActivity{
    Switch switchVibration, switchRemind;
    EditText edittextTeg;
    Button btnCencel, btnDelete, btnSave;
    TextView RingName;
    Spinner spnRepeat;
    public static TextView tvHours;
    public static TextView tvMin;
    public static TextView tvRepeat;
    private static AlarmClockLab alarmClockLab;
    public String[] items;
    public boolean[] flags;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreate AddAlarmActivity");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_alarm);

        tvHours = (TextView) findViewById(R.id.alarm_time_hours);
        tvMin = (TextView) findViewById(R.id.alarm_time_min);

        btnCencel = (Button) findViewById(R.id.btn_cencel);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setVisibility(View.INVISIBLE);
        btnSave = (Button) findViewById(R.id.btn_save);
        RingName = (TextView) findViewById(R.id.ringtones_name);
        //spnRepeat = (Spinner) findViewById(R.id.spinner_repeat);
        tvRepeat = (TextView) findViewById(R.id.tv_repeat);
        edittextTeg = (EditText) findViewById(R.id.edittext_teg);
        switchVibration = (Switch) findViewById(R.id.switch_vibration);
        switchRemind = (Switch) findViewById(R.id.switch_remind);

        int[] currentTime = getCurrentTime();
        alarmClockLab = new AlarmClockBuilder().builderLab(0);
        alarmClockLab.setEnable(true);
        alarmClockLab.setHour(currentTime[0]);
        alarmClockLab.setMinute(currentTime[1]);
        alarmClockLab.setRepeat(0);
        alarmClockLab.setTag(getResources().getString(R.string.bar_name));
        alarmClockLab.setRingPosition(0);
        alarmClockLab.setRing(firstRing(this));
        alarmClockLab.setVibrate(false);
        alarmClockLab.setRemind(false);

        tvHours.setText(String.format("%02d",alarmClockLab.hour));
        tvMin.setText(String.format("%02d",alarmClockLab.minute));

        //spnRepeat.setSelection(alarmClockLab.repeat);
        tvRepeat.setText(getRepeatString(alarmClockLab.repeat));
    }

    @Override
    protected void onResume() {
        super.onResume();
        RingName.setText(alarmClockLab.ring);
    }
    private String getRepeatString(int repeat) {
        flags = new boolean[] {false,false,false,false,false,false,false};
        String remindString = "";
        if (repeat == 0) {
            remindString = getString(R.string.alarmRepeatChoice);
        } else if (repeat == 127) {
            Arrays.fill(flags, true);
            remindString = getString(R.string.alarmRepeatEveryDay);
        } else {
            int[] StrweekId = new int[] {R.string.sunday, R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday};
            for(int i=0;i<7;i++) {
                if ((repeat&(0x1<<i)) != 0) {
                    flags[i] = true;
                    if(!remindString.equals(""))
                        remindString += ",";
                    remindString += getString(StrweekId[i]);
                }
            }
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

    }
    public void AlarmSave(View view) {
        //Log.e(TAG,"AlarmSave");
        if(edittextTeg.getText() != null && edittextTeg.getText().length() != 0) {
            //Log.e(TAG,"edittextTeg["+edittextTeg.getText()+"]");
            alarmClockLab.setTag("" + edittextTeg.getText());
        }
        //alarmClockLab.setRepeat(spnRepeat.getSelectedItemPosition());
        alarmClockLab.setVibrate(switchVibration.isChecked());
        alarmClockLab.setRemind(switchRemind.isChecked());

        AlarmDBUtils.insertAlarmClock(AddAlarmActivity.this, alarmClockLab);
        AlarmModel alarm = AlarmDBUtils.queryAlarmClock(AddAlarmActivity.this).get(0);
        if (alarm.enable) {
            AlarmManagerHelper.startAlarmClock(AddAlarmActivity.this, alarm);
        }
        finish();
    }

    public void OnRepeatClick(View view) {
        items = getResources().getStringArray(R.array.weeks);
        AlertDialog.Builder alertDialog =
                new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.alarmRepeat);
        alertDialog.setMultiChoiceItems(items, flags, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                flags[which]=isChecked;
            }
        });
        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alarmClockLab.repeat = 0;
                for(int i=0; i < flags.length; i++) {
                    if(flags[i])
                        alarmClockLab.repeat = (alarmClockLab.repeat | (0x1<<i));
                }
                tvRepeat.setText(getRepeatString(alarmClockLab.repeat));
                //Toast.makeText(getBaseContext(),"確定:"+alarmClockLab.repeat,Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNeutralButton("取消",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which){
                tvRepeat.setText(getRepeatString(alarmClockLab.repeat));
                //Toast.makeText(getBaseContext(),"取消",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    public void OnRingClick(View view) {
        Intent intent = new Intent(this, RingActivity.class);
        startActivity(intent);
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
            return new TimePickerDialog(getActivity(), this, alarmClockLab.hour, alarmClockLab.minute, DateFormat.is24HourFormat(getActivity()));
        }
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String min = String.format("%02d",minute);
            String hour = String.format("%02d",hourOfDay);

            tvHours.setText(hour);
            tvMin.setText(min);
            alarmClockLab.setMinute(minute);
            alarmClockLab.setHour(hourOfDay);
        }
    }
}
