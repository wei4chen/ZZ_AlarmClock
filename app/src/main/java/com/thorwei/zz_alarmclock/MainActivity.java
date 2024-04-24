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
import android.widget.ImageView;
import android.widget.ListView;

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
    }

    public void addAlarm(View view) {
        Intent intent = new Intent(this, AddAlarmActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        alarmList = AlarmDBUtils.queryAlarmClock(this);
        alarmAdapter.setData(alarmList);
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

