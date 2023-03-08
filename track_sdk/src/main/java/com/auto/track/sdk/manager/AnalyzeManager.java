package com.auto.track.sdk.manager;

import android.content.Context;
import android.util.Log;

import com.auto.track.sdk.base.Event;
import com.auto.track.sdk.base.LogUtil;

import java.util.List;

public class AnalyzeManager {
    static class Instance {
        static AnalyzeManager instance = new AnalyzeManager();
    }

    public static AnalyzeManager getInstance() {
        return Instance.instance;
    }

    public void processServiceIntent(Context service) {
        sendEventsNow(service);
    }

    private void sendEventsNow(Context context) {
        if (!ThreadServiceManager.onValidateThread()) {
            ThreadServiceManager.getInstance().changeThread(context);
            return;
        }

        List<Event> events = StorageManager.getInstance().getEvents();
        if (events.size() < 20) {
            LogUtil.e("AnalyzeManager", "数据库中存储数据不足，稍后上报");
            return;
        } else {
            LogUtil.e("AnalyzeManager", "数据库中存储数据足够，准备上报服务器");
            UploadManager.getInstance().sendEvent(context, events, new UploadManager.UploadCallback() {
                @Override
                public void onSucceed() {
                    LogUtil.e("AnalyzeManager", "数据上传成功...");
                    StorageManager.getInstance().delete(events);
                }

                @Override
                public void onFail(Exception e) {
                    LogUtil.e("AnalyzeManager", "数据上传失败...");
                }
            });

        }
    }
}
