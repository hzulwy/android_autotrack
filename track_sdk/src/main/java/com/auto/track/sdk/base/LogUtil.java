package com.auto.track.sdk.base;

import android.util.Log;

import com.auto.track.sdk.BuildConfig;


public class LogUtil {

    private static String TAG_SDK = "Brett";
    private static boolean canShow = BuildConfig.DEBUG;
    public static final int MAX_LOG_LENGTH = 3500;


    public static boolean enable() {
        return canShow;
    }

    public static void v(String msg) {
        v(null, msg);
    }

    public static void v(String tag, String msg) {
        if (!canShow) {
            return;
        }
        for (String s : splitMsg(msg)) {
            Log.v(TAG_SDK + generateCustomTag(tag), s);
        }
    }

    public static void d(String msg) {
        d(null, msg);
    }

    public static void d(String tag, String msg) {
        if (!canShow) {
            return;
        }
        for (String s : splitMsg(msg)) {
            Log.d(TAG_SDK + generateCustomTag(tag), s);
        }
    }

    public static void i(String msg) {
        i(null, msg);
    }

    public static void i(String tag, String msg) {
        if (!canShow) {
            return;
        }
        for (String s : splitMsg(msg)) {
            Log.i(TAG_SDK + generateCustomTag(tag), s);
        }
    }


    public static void w(String msg) {
        w(null, msg);
    }

    public static void w(String tag, String msg) {
        if (!canShow) {
            return;
        }
        for (String s : splitMsg(msg)) {
            Log.w(TAG_SDK + generateCustomTag(tag), s);
        }
    }

    public static void e(String msg) {
        e(null, msg);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String msg, Throwable t) {
        e(null, msg, t);
    }

    public static void e(String tag, String msg, Throwable t) {
        if (!canShow) {
            return;
        }
        for (String s : splitMsg(msg)) {
            Log.e(TAG_SDK + generateCustomTag(tag), s, t);
        }
    }

    private static String[] splitMsg(String msg) {
        if (msg.length() <= MAX_LOG_LENGTH){
            return new String[]{msg};
        }
        String[] splitMsg = new String[msg.length() / MAX_LOG_LENGTH + 1];
        for (int i = 0; i < splitMsg.length; i++) {
            int start = i * MAX_LOG_LENGTH;
            int end = MAX_LOG_LENGTH * (i + 1);
            splitMsg[i] = msg.substring(start,end  > msg.length() ? msg.length() : end);
        }
        return splitMsg;
    }

    private static String generateCustomTag(String originTag) {
        return (originTag == null ? "" : "-" + originTag);
    }

}
