package kat.android.com.trywidget.POJO;


import com.google.gson.annotations.SerializedName;

import java.util.List;

//Using GSON to convert json to java object
public class WeatherResponse {
    @SerializedName("sys")
    private Sys sys;

    @SerializedName("wind")
    private Wind wind;

    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("name")
    private String name;

    @SerializedName("main")
    private Main main;

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public WeatherResponse() {
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
