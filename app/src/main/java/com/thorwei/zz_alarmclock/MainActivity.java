package com.thorwei.zz_alarmclock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "weitest";
    public String[] TabTimes = new String[] {"01:00","02:00","03:00"};
    String[] TabTags =  new String[] {"101", "102", "103"};
    boolean[] TabEnbale =  new boolean[] {true,true,false};

    ListView alarmListView;
    private AlarmAdapter alarmAdapter;
    private List<AlarmModel> alarmList;

    ImageView imageView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    Log.e(TAG,"Touch+ Down");
                    addAlarm(v);
                    return true;
                }
                return false;
            }
        });


        alarmListView = (ListView)findViewById(R.id.AlarmClockListView);
        alarmAdapter = new AlarmAdapter(this, alarmList);
        //listlayoutadapter adaAlarms = new listlayoutadapter(this);
        alarmListView.setAdapter(alarmAdapter);

        alarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, ""+alarmList.get(position).id,Toast.LENGTH_SHORT).show();

                settingAlarm(position);
            }
        });

    }

    public void addAlarm(View view) {
        Intent intent = new Intent(this, AddAlarmActivity.class);
        startActivity(intent);
/*
        Log.e(TAG,"addAlarm");
        Intent intent = new Intent(MainActivity.this, ZZTestActivity.class);
        Log.e(TAG,"addAlarm1");
        startActivity(intent);
        Log.e(TAG,"addAlarm2");
*/
    }

    public void settingAlarm(int position) {
        Intent intent = new Intent(this, SettingAlarmActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("AlarmModel", alarmList.get(position));
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if (resultCode == RESULT_OK){
                int position = data.getIntExtra("position", -1);
                if(position >= 0) {

                    final AlarmModel bufAlarm = (AlarmModel)data.getSerializableExtra("AlarmModel");
                    AlarmDBUtils.updateAlarmClock(this, bufAlarm);
                    alarmList = AlarmDBUtils.queryAlarmClock(this);
                    alarmAdapter.setData(alarmList);

                    /*
                    AlarmModel bufAlarmM = (AlarmModel)data.getSerializableExtra("AlarmModel");
                    Log.e(TAG,"id["+bufAlarmM.id+"] tag["+bufAlarmM.tag+"]repeat["+bufAlarmM.repeat+"]vibrate["+bufAlarmM.vibrate+"]");
                    final AlarmModel bufAlarm = alarmList.get(position);
                    bufAlarm.tag = bufAlarmM.tag;
                    bufAlarm.repeat = bufAlarmM.repeat;
                    bufAlarm.vibrate = bufAlarmM.vibrate;
                    AlarmDBUtils.updateAlarmClock(this, bufAlarm);
                    alarmList = AlarmDBUtils.queryAlarmClock(this);
                    alarmAdapter.setData(alarmList);
*/
                }
/*
                int id = data.getIntExtra("id", 0);
                String tag = data.getStringExtra("tag");
                int repeat = data.getIntExtra("repeat", 0);
                boolean vibrate = data.getBooleanExtra("vibrate", false);
                Log.e(TAG,"position["+position+"]id["+id+"] tag["+tag+"]repeat["+repeat+"]vibrate["+vibrate+"]");

                if(position >= 0) {
                    Log.e(TAG,"updateAlarmClock");
                    final AlarmModel bufAlarm = alarmList.get(position);
                    bufAlarm.tag = tag;
                    bufAlarm.repeat = repeat;
                    bufAlarm.vibrate = vibrate;
                    AlarmDBUtils.updateAlarmClock(this, bufAlarm);
                    alarmList = AlarmDBUtils.queryAlarmClock(this);
                    alarmAdapter.setData(alarmList);
                }

 */
            }
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG,"onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onResume");
        super.onResume();
        alarmList = AlarmDBUtils.queryAlarmClock(this);
        alarmAdapter.setData(alarmList);
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG,"onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
    }

    private void initDB() {
        AlarmClockBuilder clockBuilder = new AlarmClockBuilder();
        AlarmModel alarmM = clockBuilder.enable(true)
                .hour(7)
                .minute(0)
                .repeat(0)
                .tag(""+R.string.bar_name)
                .ringPosition(0)
                .ring(firstRing(this))
                .vibrate(true)
                .builder(0);

        AlarmDBUtils.insertAlarmClock(this, alarmM);
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
        return ringName;
    }


}

