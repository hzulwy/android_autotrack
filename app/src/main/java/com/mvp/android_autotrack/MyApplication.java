package com.mvp.android_autotrack;

import android.app.Application;

import com.auto.track.sdk.AutoTrackAPI;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AutoTrackAPI.init(this);
    }
}
