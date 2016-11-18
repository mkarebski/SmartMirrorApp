package pl.mkarebski.smartmirrorapp;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

public class SmartMirrorApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}