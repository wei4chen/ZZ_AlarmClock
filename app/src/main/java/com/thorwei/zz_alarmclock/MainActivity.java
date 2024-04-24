package com.thorwei.zz_alarmclock;

import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "weitest";
    public String[] TabTimes = new String[] {"01:00","02:00","03:00"};
    String[] TabTags =  new String[] {"101", "102", "103"};
    boolean[] TabEnbale =  new boolean[] {true,true,false};

    ListView alarmListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        alarmListView = (ListView)findViewById(R.id.AlarmClockListView);
        listlayoutadapter adaAlarms = new listlayoutadapter(this);
        alarmListView.setAdapter(adaAlarms);
    }


}

