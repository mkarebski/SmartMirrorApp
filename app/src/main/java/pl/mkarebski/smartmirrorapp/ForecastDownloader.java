package pl.mkarebski.smartmirrorapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static pl.mkarebski.smartmirrorapp.MainActivity.MyPREFERENCES;

public class ForecastDownloader extends BroadcastReceiver {

    private static final String CELSIUS = "\u2103";
    private static final String API_KEY = "YOUR_API_KEY"; //weather.com
    private static final String URL = "http://api.wunderground.com/api/" + API_KEY + "/conditions/q/PL/Kielce.json";

    @Override
    public void onReceive(Context context, Intent intent) {
        final Context con = context;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                String responseBody = new String(response);

                JSONObject root = null;
                try {
                    root = new JSONObject(responseBody);
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }

                JSONObject currentObservation = null;
                try {
                    currentObservation = root.getJSONObject("current_observation");
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }

                String temp = null;
                try {
                    temp = currentObservation.getString("temp_c");
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }

                String icon = null;
                try {
                    icon = currentObservation.getString("icon");
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }

                updateSharedPreferences(temp, icon, con);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d("WeatherService", valueOf(statusCode));
                Log.d("WeatherService", valueOf(e.getMessage()));
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });


    }

    private void updateSharedPreferences(String temp, String icon, Context con) {
        Log.d("FD_SP1", temp);
        Log.d("FD_SP1", icon);

        SharedPreferences sharedPreferences = con.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("temp", format("%.0f" + CELSIUS, Math.ceil(new Double(temp))));
        editor.putString("icon", icon);
        editor.apply();

        Log.d("FD_SP2", sharedPreferences.getString("temp", ""));
        Log.d("FD_SP2", sharedPreferences.getString("icon", ""));
    }

}
