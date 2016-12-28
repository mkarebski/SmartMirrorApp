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
import android.widget.Toast;

import com.johnhiott.darkskyandroidlib.ForecastApi;

import java.util.Calendar;

public class MainActivity extends Activity {

    private static final String FORECAST_IO_API_KEY = "API_KEY";
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


        sharedpreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                String temp = sharedPreferences.getString("temp", "");
                String icon = sharedPreferences.getString("icon", "");

                Log.d("SharedprefsListener", "temp");
                Log.d("SharedprefsListener", "icon");

                temperature.setText(temp);
                weatherIcon.setImageResource(getResourceFrom(icon));
            }
        });
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

        Calendar morning = getInitiatedCalendar(7, 30);
        Calendar noon = getInitiatedCalendar(12, 30);
        Calendar afternoon = getInitiatedCalendar(16, 30);
        Calendar evening = getInitiatedCalendar(20, 30);

        PendingIntent pendingIntent1 = initPendingIntentWithRequestCodeEqualTo(1);
        PendingIntent pendingIntent2 = initPendingIntentWithRequestCodeEqualTo(2);
        PendingIntent pendingIntent3 = initPendingIntentWithRequestCodeEqualTo(3);
        PendingIntent pendingIntent4 = initPendingIntentWithRequestCodeEqualTo(4);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, morning.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, noon.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent2);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, afternoon.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent3);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, evening.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent4);

        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    private Calendar getInitiatedCalendar(int h, int m) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        return calendar;
    }


}
