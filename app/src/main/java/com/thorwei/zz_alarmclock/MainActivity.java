package com.thorwei.zz_alarmclock;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

import android.Manifest;

import com.thorwei.zz_alarmclock.api.RetrofitApiProvider;
import com.thorwei.zz_alarmclock.weather.Weather;
import com.thorwei.zz_alarmclock.weather.WeatherResponseModel;
import com.thorwei.zz_alarmclock.weather.WeatherTempConverter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "weitest";

    ImageView weatherIcon;
    TextView tvLocation, tvCondition, tvTemp;
    ImageView imageView;
    ListView alarmListView;
    private AlarmAdapter alarmAdapter;
    private List<AlarmModel> alarmList;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
/*
        boolean[] TabHasGone = new boolean[] {false,false,false,false,false,false,false};
        String[] TabManifest = new String[] {Manifest.permission.VIBRATE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.DISABLE_KEYGUARD,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.INTERNET};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions;
            int TabHasFalseNum = 0;
            for(int i=0;i<TabHasGone.length;i++) {
                TabHasGone[i] = checkSelfPermission(TabManifest[i]) == PackageManager.PERMISSION_GRANTED;
                if (!TabHasGone[i])
                    TabHasFalseNum++;
            }
            permissions = new String[TabHasFalseNum];
            TabHasFalseNum = 0;
            for(int i=0;i<permissions.length;i++) {
                if(!TabHasGone[i]) {
                    permissions[TabHasFalseNum] = TabManifest[i];
                    TabHasFalseNum++;
                }
            }
            Log.e(TAG,"TabHasFalseNum:"+TabHasFalseNum);
            if(TabHasFalseNum > 0)
                requestPermissions(permissions, 1);
            else
                init();
        } else {
            init();
        }
*/
        /*
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( this, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( this, Manifest.permission.DISABLE_KEYGUARD) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.VIBRATE,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WAKE_LOCK,
                    Manifest.permission.DISABLE_KEYGUARD,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.INTERNET}, 1);
        } else {
            init();
        }
        */

        if(!checkFloatPermission(this)){
            requestSettingCanDrawOverlays();
        }
        init();
    }
    private void requestSettingCanDrawOverlays() {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.O) {//8.0Up
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(intent);
        } else if (sdkInt >= Build.VERSION_CODES.M) {//6.0-8.0
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }
    public static boolean checkFloatPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                Class cls = Class.forName("android.content.Context");
                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                if (!(obj instanceof String)) {
                    return false;
                }
                String str2 = (String) obj;
                obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                cls = Class.forName("android.app.AppOpsManager");
                Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                declaredField2.setAccessible(true);
                Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                return result == declaredField2.getInt(cls);
            } catch (Exception e) {
                return false;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                if (appOpsMgr == null)
                    return false;
                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                        .getPackageName());
                return mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
            } else {
                return Settings.canDrawOverlays(context);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                for(int i=0;i<permissions.length;i++) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        finish();
                    }
                }
                init();
                break;
        }
    }

    void init() {
        weatherIcon = (ImageView)findViewById(R.id.image_weather_icon);
        tvLocation = (TextView)findViewById(R.id.tv_location);
        tvCondition = (TextView)findViewById(R.id.tv_condition);
        tvTemp = (TextView)findViewById(R.id.tv_temp);
        loadWeather("kaohsiung,tw");


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

    private void loadWeather(String city) {
        Log.d(TAG,"loadWeather_city:"+city);
        RetrofitApiProvider apiProvider = new RetrofitApiProvider();
        apiProvider.getWeather(city, new Callback<WeatherResponseModel>() {
            @Override
            public void onResponse(Call<WeatherResponseModel> call, Response<WeatherResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e(TAG,getString(R.string.weather_update));
                    populateWeather(response);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponseModel> call, Throwable t) {
                Log.e(TAG,getString(R.string.failure_network));
            }
        });
    }

    private void populateWeather(Response<WeatherResponseModel> response) {
        Log.e(TAG,getString(R.string.failure_network));
        Weather weather[] = response.body().getWeathers();
        tvLocation.setText(response.body().getName());
        tvCondition.setText(weather[0].getMain());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tFormat = preferences.getString(getString(R.string.pref_temp_format_key), getString(R.string.pref_default_temp_format));

        //Log.e(TAG, "temp:"+response.body().getMain().getTemp()+"C");
        if (tFormat.equals(getString(R.string.pref_temp_celsius))) {
            tvTemp.setText(WeatherTempConverter.convertToCelsius(response.body().getMain().getTemp()).intValue() + getString(R.string.cel));
        } else {
            tvTemp.setText(WeatherTempConverter.convertToFahrenheit(response.body().getMain().getTemp()).intValue() + getString(R.string.fahr));
        }

        switch (weather[0].getIcon()) {
            case "01d":
                weatherIcon.setImageResource(R.drawable.icon01d);
                break;
            case "01n":
                weatherIcon.setImageResource(R.drawable.icon01n);
                break;
            case "02d":
                weatherIcon.setImageResource(R.drawable.icon02d);
            case "02n":
                weatherIcon.setImageResource(R.drawable.icon02n);
                break;
            case "03d":
            case "03n":
            case "04d":
            case "04n":
                weatherIcon.setImageResource(R.drawable.icon03);
                break;
            case "09d":
            case "09n":
                weatherIcon.setImageResource(R.drawable.icon09);
                break;
            case "10d":
            case "10n":
                weatherIcon.setImageResource(R.drawable.icon10);
                break;
            case "11d":
            case "11n":
                weatherIcon.setImageResource(R.drawable.icon11);
                break;
            case "13d":
            case "13n":
                weatherIcon.setImageResource(R.drawable.icon13);
                break;
            case "50d":
            case "50n":
                weatherIcon.setImageResource(R.drawable.icon50);
                break;
        }
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

