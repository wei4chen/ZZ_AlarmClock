package com.thorwei.zz_alarmclock.api;

import android.util.Log;

import com.thorwei.zz_alarmclock.weather.WeatherResponseModel;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitApiProvider {
    private OpenWeatherService openWeatherService;
    private static final String API_KEY = "8247f8f2fe7ad8a3650cc448abc01666";
    //  https://api.openweathermap.org/data/2.5/forecast?id=1673820&APPID=8247f8f2fe7ad8a3650cc448abc01666
    //  https://api.openweathermap.org/data/2.5/weather?id=1673820&APPID=8247f8f2fe7ad8a3650cc448abc01666
    //  https://api.openweathermap.org/data/2.5/weather?q=kaohsiung,tw&APPID=8247f8f2fe7ad8a3650cc448abc01666
    public RetrofitApiProvider() {
        String BASE_URL = "https://api.openweathermap.org/data/2.5/";
        this.openWeatherService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherService.class);
    }
    public void getWeather(String city, Callback<WeatherResponseModel> callback) {
        openWeatherService.getWeather(city, API_KEY).enqueue(callback);
    }
}
