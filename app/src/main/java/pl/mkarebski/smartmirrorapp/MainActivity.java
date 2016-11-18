package pl.mkarebski.smartmirrorapp;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;

public class MainActivity extends Activity {

    private TextView hour;
    private TextView date;
    private TextView temp;
    private ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hour = (TextView) findViewById(R.id.hour);
        date = (TextView) findViewById(R.id.date);
        temp = (TextView) findViewById(R.id.temp);
        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);

        LocalDateTime dateTime = LocalDateTime.now();
//        DateTimeFormatter hourFormatter =



    }
}
