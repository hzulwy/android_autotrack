package com.auto.track.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.auto.track.sdk.appStartAndEnd.TrackStartAndEndManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class AutoTrackPrivate {

    private static List<String> mIgnoredActivities;
    private static Map<String, String> mNameMap;

    static {
        mIgnoredActivities = new ArrayList<>();
        mNameMap = new HashMap<>();
    }

    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"
            + ".SSS", Locale.CHINA);

    public static void ignoreAutoTrackActivity(Class<?> activity) {
        if (activity == null) {
            return;
        }

        mIgnoredActivities.add(activity.getCanonicalName());
    }

    public static void removeIgnoredActivity(Class<?> activity) {
        if (activity == null) {
            return;
        }

        if (mIgnoredActivities.contains(activity.getCanonicalName())) {
            mIgnoredActivities.remove(activity.getCanonicalName());
        }
    }

    public static void registerActivityLifecycleCallbacks(Application application) {


        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                trackAppViewScreen(activity, null);
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
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
        TrackStartAndEndManager.registerActivityLifecycleCallbacks(application);
    }

    private static void trackAppViewScreen(Activity activity, @Nullable String name) {
        try {
            if (activity == null) {
                return;
            }
            if (mIgnoredActivities.contains(activity.getClass().getCanonicalName())) {
                return;
            }
            String pageName = name == null ? "" : name;
            JSONObject properties = new JSONObject();
            properties.put("activity", activity.getClass().getCanonicalName());
            properties.put("title", name == null ? "" : name);
            AutoTrackAPI.getInstance().track("AppViewScreen", properties);
            mNameMap.put(activity.getClass().getCanonicalName() + pageName, activity.getClass().getCanonicalName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册 AppStart 的监听
     */
    public static void registerActivityStateObserver(Application application) {
        TrackStartAndEndManager.registerActivityStateObserver(application);
    }



    public static Map<String, Object> getDeviceInfo(Context context) {
        final Map<String, Object> deviceInfo = new HashMap<>();
        deviceInfo.put("sdk_version", AutoTrackAPI.SDK_VERSION);
        deviceInfo.put("os", "Android");
        deviceInfo.put("os_version",
                Build.VERSION.RELEASE == null ? "UNKNOWN" : Build.VERSION.RELEASE);
        deviceInfo
                .put("manufacturer", Build.MANUFACTURER == null ? "UNKNOWN" : Build.MANUFACTURER);
        if (TextUtils.isEmpty(Build.MODEL)) {
            deviceInfo.put("model", "UNKNOWN");
        } else {
            deviceInfo.put("model", Build.MODEL.trim());
        }

        try {
            final PackageManager manager = context.getPackageManager();
            final PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            deviceInfo.put("app_version", packageInfo.versionName);

            int labelRes = packageInfo.applicationInfo.labelRes;
            deviceInfo.put("app_name", context.getResources().getString(labelRes));
        } catch (final Exception e) {
            e.printStackTrace();
        }

        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        deviceInfo.put("screen_height", displayMetrics.heightPixels);
        deviceInfo.put("screen_width", displayMetrics.widthPixels);

        return Collections.unmodifiableMap(deviceInfo);
    }

    public static void mergeJSONObject(final JSONObject source, JSONObject dest)
            throws JSONException {
        Iterator<String> superPropertiesIterator = source.keys();
        while (superPropertiesIterator.hasNext()) {
            String key = superPropertiesIterator.next();
            Object value = source.get(key);
            if (value instanceof Date) {
                synchronized (mDateFormat) {
                    dest.put(key, mDateFormat.format((Date) value));
                }
            } else {
                dest.put(key, value);
            }
        }
    }
}
