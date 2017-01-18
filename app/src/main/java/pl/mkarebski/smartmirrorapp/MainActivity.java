package pl.mkarebski.smartmirrorapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import static android.app.AlarmManager.INTERVAL_HALF_HOUR;
import static java.util.Calendar.*;

public class MainActivity extends Activity {

    public static final String MyPREFERENCES = "MyPrefs";
    private TextView temperature;
    private ImageView weatherIcon;
    private OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        temperature = (TextView) findViewById(R.id.temperature);
        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);

        setUpAlarm();

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        clearOldSharedPreferences(sharedpreferences);

        onSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d("SharedprefsListener", "changed");

                String temp = sharedPreferences.getString("temp", "");
                String icon = sharedPreferences.getString("icon", "");

                temperature.setText(temp);
                weatherIcon.setImageResource(getResourceFrom(icon));
            }
        };
        sharedpreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    private void clearOldSharedPreferences(SharedPreferences sharedpreferences) {
        sharedpreferences.edit().clear().apply();
    }

    private PendingIntent initPendingIntentWithRequestCodeEqualTo(int i) {
        Intent forecastIntent = new Intent(this, ForecastDownloader.class);
        return PendingIntent.getBroadcast(this.getApplicationContext(), i, forecastIntent, 0);
    }

    private int getResourceFrom(String icon) {
        switch (icon) {
            case "clear":
            case "sunny":
                return getResourceByName("clear_day");
            case "nt_clear":
            case "nt_sunny":
                return getResourceByName("clear_night");
            case "fog":
            case "hazy":
            case "nt_fog":
            case "nt_hazy":
                return getResourceByName("fog");
            case "cloudy":
            case "nt_cloudy":
                return getResourceByName("cloudy");
            case "mostlycloudy":
            case "mostlysunny":
            case "partlysunny":
            case "partlycloudy":
                return getResourceByName("partly_cloudy_day");
            case "nt_mostlycloudy":
            case "nt_mostlysunny":
            case "nt_partlysunny":
            case "nt_partlycloudy":
                return getResourceByName("partly_cloudy_night");
            case "chanceflurries":
            case "flurries":
            case "nt_chanceflurries":
            case "nt_flurries":
                return getResourceByName("flurries");
            case "nt_snow":
            case "nt_chancesnow":
            case "snow":
            case "chancesnow":
                return getResourceByName("snow");
            case "chancerain":
            case "chancesleet":
            case "sleet":
            case "rain":
            case "nt_chancerain":
            case "nt_chancesleet":
            case "nt_sleet":
            case "nt_rain":
                return getResourceByName("rain");
            case "chancetstorms":
            case "tstorms":
            case "nt_chancetstorms":
            case "nt_tstorms":
                return getResourceByName("stormy");
        }
        return -1;
    }

    private int getResourceByName(String name) {
        return this.getResources().getIdentifier(name, "drawable", this.getPackageName());
    }

    private void setUpAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = getInstance();

        PendingIntent pendingIntent = initPendingIntentWithRequestCodeEqualTo(1);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTime().getTime() + 5000, INTERVAL_HALF_HOUR, pendingIntent);
    }
}
