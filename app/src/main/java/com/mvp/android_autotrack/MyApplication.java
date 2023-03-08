package com.mvp.android_autotrack;

import android.app.Application;

import com.auto.track.sdk.AutoTrackAPI;
import com.auto.track.sdk.base.LogUtil;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AutoTrackAPI.setServerUrl("http://10.0.2.2:3000/posts");
        AutoTrackAPI.init(this);
    }
}
