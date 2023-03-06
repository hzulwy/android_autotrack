package com.auto.track.sdk.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.auto.track.sdk.base.LogUtil;

public class TimerManager {
    Thread schedulerThread;
    long startTime=0;

    Handler handler;

    final static long TIME_DELAY_INIT=10000;

    final static long TIME_DELAY_NORMAL=60000;

    final static long TIME_CHANGE_DELAY=1000*60*2;

    volatile long delayTime=TIME_DELAY_INIT;

    // @set
    private static long globalDelayTime =  -1;

    public static void setGlobalDelayTime(long time){
        globalDelayTime = time;
    }
    volatile Runnable timerTask;

    static class Instance{
        static TimerManager instance=new TimerManager();
    }

    public static TimerManager getInstance(){
        return Instance.instance;
    }

    public void init(){
        startTime=System.currentTimeMillis();
        schedulerThread=new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        LogUtil.d("schedulerThread run:"+System.currentTimeMillis()+" startTime:"+startTime);
                        try {
                            if(timerTask!=null){
                                timerTask.run();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        if(TIME_DELAY_INIT==delayTime && System.currentTimeMillis()-startTime>TIME_CHANGE_DELAY){
                            delayTime=TIME_DELAY_NORMAL;
                        }
                        if(globalDelayTime != -1){
                            handler.sendEmptyMessageDelayed(1,globalDelayTime);
                        }else{
                            handler.sendEmptyMessageDelayed(1,delayTime);
                        }
                    }
                };
                handler.sendEmptyMessageDelayed(1,delayTime);
                Looper.loop();
            }
        });
        schedulerThread.start();
    }

    public Runnable getTimerTask() {
        return timerTask;
    }

    public void setTimerTask(Runnable timerTask) {
        this.timerTask = timerTask;
    }
}
