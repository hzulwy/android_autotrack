package com.auto.track.sdk;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.auto.track.sdk.base.Utils;

import org.json.JSONObject;

import java.util.Map;
import android.os.Process;

@Keep
public class AutoTrackAPI {
    private final String TAG = this.getClass().getSimpleName();
    public static final String SDK_VERSION = "1.0.0";
    private static AutoTrackAPI INSTANCE;
    private static final Object mLock = new Object();
    private static Map<String, Object> mDeviceInfo;
    private String mDeviceId;
    private boolean flag = true;

    @Keep
    public static AutoTrackAPI init(Application application) {
        synchronized (mLock) {
            if (null == INSTANCE) {
                INSTANCE = new AutoTrackAPI(application);
            }
            return INSTANCE;
        }
    }

    @Keep
    public static AutoTrackAPI getInstance() {
        return INSTANCE;
    }

    private AutoTrackAPI(Application application) {
        mDeviceId = Utils.getAndroidID(application.getApplicationContext());
        mDeviceInfo = AutoTrackPrivate.getDeviceInfo(application.getApplicationContext());
        if (flag) {
            AutoTrackPrivate.registerActivityLifecycleCallbacks(application);
            AutoTrackPrivate.registerActivityStateObserver(application);
        }
    }

    @Keep
    public boolean isAutoTrack(boolean isAutoTrack) {
        flag = isAutoTrack;
        return flag;
    }

    /**
     * 指定不采集哪个 Activity 的页面浏览事件
     *
     * @param activity Activity
     */
    public void ignoreAutoTrackActivity(Class<?> activity) {
        AutoTrackPrivate.ignoreAutoTrackActivity(activity);
    }

    /**
     * 恢复采集某个 Activity 的页面浏览事件
     *
     * @param activity Activity
     */
    public void removeIgnoredActivity(Class<?> activity) {
        AutoTrackPrivate.removeIgnoredActivity(activity);
    }

    @Keep
    public void track(@NonNull String eventName, @Nullable JSONObject properties) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("event", eventName);
            jsonObject.put("device_id", mDeviceId);
            jsonObject.put("process_id",Process.myPid());

            JSONObject sendProperties = new JSONObject(mDeviceInfo);

            if (properties != null) {
                AutoTrackPrivate.mergeJSONObject(properties, sendProperties);
            }

            jsonObject.put("properties", sendProperties);
            jsonObject.put("time", System.currentTimeMillis());

            Log.i(TAG, Utils.formatJson(jsonObject.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
