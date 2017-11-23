package com.gsoft.gcweather.app;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * Created by edison on 2017/11/23 0023.
 */

public class MyApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        LitePal.initialize(sContext);
    }

    public static Context getContext(){
        return sContext;
    }
}
