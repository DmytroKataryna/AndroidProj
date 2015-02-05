package kat.android.com.trywidget;

import kat.android.com.trywidget.POJO.WeatherResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


public interface Api {

//HTTP GET Json from http://api.openweathermap.org/data/2.5/weather?q=Lviv
    @GET("/weather")
    void getWeather(@Query("q") String cityName,
                    Callback<WeatherResponse> callback);
}
