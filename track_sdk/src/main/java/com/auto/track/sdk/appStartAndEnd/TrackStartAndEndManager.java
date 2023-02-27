package com.auto.track.sdk.appStartAndEnd;

import android.app.Activity;
import android.app.Application;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.auto.track.sdk.AutoTrackAPI;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class TrackStartAndEndManager {


    private static WeakReference<Activity> mCurrentActivity;
    private static DatabaseHelper mDatabaseHelper;
    private static CountDownTimer countDownTimer;
    private final static int SESSION_INTERVAL_TIME = 30 * 1000;

    /**
     * 注册 AppStart 的监听
     */
    public static void registerActivityStateObserver(Application application) {
        application.getContentResolver().registerContentObserver(mDatabaseHelper.getAppStartUri(),
                false, new ContentObserver(new Handler()) {
                    @Override
                    public void onChange(boolean selfChange, Uri uri) {
                        if (mDatabaseHelper.getAppStartUri().equals(uri)) {
                            countDownTimer.cancel();
                        }
                    }
                });
    }

    /**
     * Track $AppStart 事件
     */
    private static void trackAppStart(Activity activity) {
        try {
            if (activity == null) {
                return;
            }
            JSONObject properties = new JSONObject();
            properties.put("activity", activity.getClass().getCanonicalName());
            AutoTrackAPI.getInstance().track("AppStart", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Track $AppEnd 事件
     */
    private static void trackAppEnd(Activity activity) {
        try {
            if (activity == null) {
                return;
            }
            JSONObject properties = new JSONObject();
            properties.put("activity", activity.getClass().getCanonicalName());
            AutoTrackAPI.getInstance().track("AppEnd", properties);
            mDatabaseHelper.commitAppEndEventState(true);
            mCurrentActivity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerActivityLifecycleCallbacks(Application application) {

        mDatabaseHelper = new DatabaseHelper(application.getApplicationContext(), application.getPackageName());
        countDownTimer = new CountDownTimer(SESSION_INTERVAL_TIME, 10 * 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (mCurrentActivity != null) {
                    trackAppEnd(mCurrentActivity.get());
                }
            }
        };

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                mDatabaseHelper.commitAppStart(true);
                double timeDiff = System.currentTimeMillis() - mDatabaseHelper.getAppPausedTime();
                if (timeDiff > SESSION_INTERVAL_TIME) {
                    if (!mDatabaseHelper.getAppEndEventState()) {
                        trackAppEnd(activity);
                    }
                }

                if (mDatabaseHelper.getAppEndEventState()) {
                    mDatabaseHelper.commitAppEndEventState(false);
                    trackAppStart(activity);
                }
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                mCurrentActivity = new WeakReference<>(activity);
                countDownTimer.start();
                mDatabaseHelper.commitAppPausedTime(System.currentTimeMillis());
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }
}
