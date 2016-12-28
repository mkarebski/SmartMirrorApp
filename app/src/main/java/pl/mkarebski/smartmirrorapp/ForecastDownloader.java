package pl.mkarebski.smartmirrorapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.String.format;
import static pl.mkarebski.smartmirrorapp.MainActivity.MyPREFERENCES;

public class ForecastDownloader extends BroadcastReceiver {

    private static final String LAT = "50.8660773";
    private static final String LNG = "20.6285677";
    private static final String TAG = "WeatherService";
    private static final String CELSIUS = "\u2103";

    @Override
    public void onReceive(Context context, Intent intent) {
        final Context con = context;

        RequestBuilder weather = new RequestBuilder();

        weather.getWeather(request(), new Callback<WeatherResponse>() {
            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                double temp = weatherResponse.getCurrently().getApparentTemperature();
                Log.d(TAG, "Temp: " + temp);

                String icon = weatherResponse.getCurrently().getIcon();
                Log.d(TAG, "Icon: " + icon);

                updateSharedPreferences(temp, icon, con);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, "Error while calling: " + retrofitError.getUrl());
                Log.d(TAG, "Message: " + retrofitError.getMessage());
            }
        });
    }

    private void updateSharedPreferences(double temp, String icon, Context con) {
        Log.d("FD_SP1", String.valueOf(temp));
        Log.d("FD_SP1", icon);

        SharedPreferences sharedPreferences = con.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("temp", format("%.0f" + CELSIUS, Math.ceil(temp)));
        editor.putString("icon", icon);
        editor.apply();

        Log.d("FD_SP2", sharedPreferences.getString("temp", ""));
        Log.d("FD_SP2", sharedPreferences.getString("icon", ""));
    }

    private Request request() {
        Request request = new Request();
        request.setLat(LAT);
        request.setLng(LNG);
        request.setUnits(Request.Units.SI);
        request.setLanguage(Request.Language.POLISH);
        request.addExcludeBlock(Request.Block.HOURLY);
        request.addExcludeBlock(Request.Block.MINUTELY);
        request.addExcludeBlock(Request.Block.DAILY);
        return request;
    }

}
