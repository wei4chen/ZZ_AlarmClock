package com.thorwei.zz_alarmclock.api;

import com.thorwei.zz_alarmclock.weather.WeatherResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherService {
    @GET("weather")
    Call<WeatherResponseModel> getWeather(@Query("q") String city, @Query("APPID") String apiKey);
}
