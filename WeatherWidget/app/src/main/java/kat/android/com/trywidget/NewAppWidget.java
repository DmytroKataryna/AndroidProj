package kat.android.com.trywidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import kat.android.com.trywidget.POJO.WeatherResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    //Default values
    static String location = "Lviv,UA";
    static String temp = "0.0°С";
    static String wind = "wind 0.0 m/s";
    static String image = "01d";

    //start when widgets are update
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        initRest();
        for (int appWidgetId : appWidgetIds) {
            views.setTextViewText(R.id.loc_text, location);
            views.setTextViewText(R.id.temp_text, temp);
            views.setOnClickPendingIntent(R.id.refresh, getPending(context, appWidgetIds));
            views.setTextViewText(R.id.wind_text, wind);
            //Using Picasso lib , to upload image from uri
            Picasso.with(context).load("http://openweathermap.org/img/w/" + image + ".png").into(views, R.id.imageViewIcon, appWidgetIds);
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }

    //using Retrofit to get data from http://openweathermap.org/
    public void initRest() {
        RestClient.get().getWeather("Lviv", new Callback<WeatherResponse>() {
            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                location = weatherResponse.getName() + " , " + weatherResponse.getSys().getCountry();
                temp = new DecimalFormat("#.#").format(weatherResponse.getMain().getTemp() - 274d) + "°С";
                wind = "Wind " + new DecimalFormat("#.#").format(weatherResponse.getWind().getSpeed()) + "m/s";
                image = weatherResponse.getWeather().get(0).getIcon();
            }

            @Override
            public void failure(RetrofitError error) {
                //make all string default
            }
        });
    }


    //intent , which i assigns to widget button
    private PendingIntent getPending(Context context, int[] appWidgetIds) {
        Intent intent = new Intent(context, NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        return PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}


