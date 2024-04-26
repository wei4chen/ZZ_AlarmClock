package com.thorwei.zz_alarmclock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
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
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    addAlarm(v);
                    return true;
                }
                return false;
            }
        });

        alarmListView = (ListView)findViewById(R.id.AlarmClockListView);
        alarmAdapter = new AlarmAdapter(this, alarmList);
        alarmListView.setAdapter(alarmAdapter);
        alarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, ""+alarmList.get(position).id,Toast.LENGTH_SHORT).show();
                settingAlarm(position);
            }
        });

        startService(new Intent(this, AlarmClockService.class));
    }

    public void addAlarm(View view) {
        Intent intent = new Intent(this, AddAlarmActivity.class);
        startActivity(intent);
    }

    public void settingAlarm(int position) {
        Intent intent = new Intent(this, SettingAlarmActivity.class);
        intent.putExtra("AlarmModel", alarmList.get(position));
        startActivity(intent);
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
}

