package pl.mkarebski.smartmirrorapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnhiott.darkskyandroidlib.ForecastApi;

import java.util.Calendar;

public class MainActivity extends Activity {

    private static final String FORECAST_IO_API_KEY = "YOUR_API_KEY";
    public static final String MyPREFERENCES = "MyPrefs";
    private TextView temperature;
    private ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ForecastApi.create(FORECAST_IO_API_KEY);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        temperature = (TextView) findViewById(R.id.temperature);
        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);

        setUpAlarm();

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        clearOldSharedPreferences(sharedpreferences);

        sharedpreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d("SharedprefsListener", "changed");

                String temp = sharedPreferences.getString("temp", "");
                String icon = sharedPreferences.getString("icon", "");

                temperature.setText(temp);
                weatherIcon.setImageResource(getResourceFrom(icon));
            }
        });
    }

    private void clearOldSharedPreferences(SharedPreferences sharedpreferences) {
        sharedpreferences.edit().clear().apply();
    }

    private PendingIntent initPendingIntentWithRequestCodeEqualTo(int i) {
        Intent forecastIntent = new Intent(this, ForecastDownloader.class);
        return PendingIntent.getBroadcast(this.getApplicationContext(), i, forecastIntent, 0);
    }

    private int getResourceFrom(String icon) {
        String replacedIcon = icon.replaceAll("-", "_");
        return this.getResources().getIdentifier(replacedIcon, "drawable", this.getPackageName());
    }

    private void setUpAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        PendingIntent pendingIntent = initPendingIntentWithRequestCodeEqualTo(1);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTime().getTime() + 5000, AlarmManager.INTERVAL_HOUR, pendingIntent);
    }
}
