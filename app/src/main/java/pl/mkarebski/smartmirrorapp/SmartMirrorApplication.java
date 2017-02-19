package pl.mkarebski.smartmirrorapp;

import android.app.Application;
import android.content.Context;

public class SmartMirrorApplication extends Application {

    public static SmartMirrorApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static SmartMirrorApplication getInstance() {
        return instance;
    }
}
