package com.auto.track.sdk.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.auto.track.sdk.base.Event;


public class ThreadServiceManager {

    private static HandlerThread analyzeThread = null;

    private static Handler analyzeHandler = null;

    private static final String THEADNAME = "analyzeThread";


    private ThreadServiceManager() {
        checkThread();
    }

    static class Instance {
        static ThreadServiceManager instance = new ThreadServiceManager();
    }

    public static ThreadServiceManager getInstance() {
        return Instance.instance;
    }


    public static boolean onValidateThread() {
        return Thread.currentThread().getName().contains(THEADNAME);
    }


    private void checkThread() {
        if (analyzeThread == null || !analyzeThread.isAlive()) {
            synchronized (ThreadServiceManager.class) {
                if (analyzeThread == null || !analyzeThread.isAlive()) {
                    analyzeThread = new HandlerThread(THEADNAME);
                    analyzeThread.start();
                    analyzeHandler = new Handler(analyzeThread.getLooper()) {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            if (msg.what == 101) {
                                Context obj = (Context) msg.obj;
                                AnalyzeManager.getInstance().processServiceIntent(obj);
                                return;
                            }
                            if (msg.what == 100) {
                                Event event = (Event) msg.obj;
                                StorageManager.getInstance().add(event);
                            }

                        }
                    };
                }
            }
        }
    }

    public void changeThread(final Context context) {
        checkThread();
        if (context != null) {
            analyzeHandler.sendMessage(analyzeHandler.obtainMessage(101, context));
        }
    }

    public void changeTreadAddData(Event event) {
        checkThread();
        if (event != null) {
            analyzeHandler.sendMessage(analyzeHandler.obtainMessage(100, event));
        }
    }
}
