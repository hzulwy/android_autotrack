package com.auto.track.sdk.base;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public final class ThreadUtil {

    private static Thread mainThread;

    private static Handler mainHandler;

    static {
        Looper mainLooper = Looper.getMainLooper();
        mainThread = mainLooper.getThread();
        mainHandler = new Handler(mainLooper);
    }

    public static boolean isOnMainThread() {
        return mainThread == Thread.currentThread();
    }

    /**
     * 在当前线程运行
     *
     * @param r
     */
    public static void run(Runnable r) {
        Handler currentHandler = new Handler(Looper.myLooper());
        currentHandler.post(r);
    }

    /**
     * 在当前线程延时运行
     *
     * @param r
     * @param delayMillis
     */
    public static void run(Runnable r, long delayMillis) {
        Handler currentHandler = new Handler(Looper.myLooper());
        currentHandler.postDelayed(r, delayMillis);
    }

    /**
     * 在指定的thread 的 handler 运行
     *
     * @param handler
     * @param r
     */
    public static void run(Handler handler, Runnable r) {
        handler.post(r);
    }

    /**
     * 在指定的thread 的 handler 延时运行
     *
     * @param handler
     * @param r
     */
    public static void run(Handler handler, Runnable r, long delayMillis) {
        handler.postDelayed(r, delayMillis);
    }

    /**
     * 在main thread运行
     *
     * @param r
     */
    public static void runOnMainThread(Runnable r) {
        if (isOnMainThread()) {
            r.run();
        } else {
            mainHandler.post(r);
        }
    }

    /**
     * 在main thread延时运行
     *
     * @param r
     */
    public static void runOnMainThread(Runnable r, long delayMillis) {
        mainHandler.postDelayed(r, delayMillis);
    }

    public static void runInBackground(Runnable r) {
        new Thread(r).start();
    }

    public static void runInBackground(final Runnable r, final Runnable callback, final boolean callbackOnMainThread) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                r.run();
                if (callbackOnMainThread) {
                    ThreadUtil.runOnMainThread(callback);
                } else {
                    callback.run();
                }
            }
        }).start();
    }
}
