package com.auto.track.sdk.manager;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.auto.track.sdk.base.Event;
import com.auto.track.sdk.base.GzipUtil;
import com.auto.track.sdk.base.LogUtil;
import com.auto.track.sdk.base.ThreadUtil;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;

public class UploadManager {

    final int CONNECT_TIME_OUT = 8000;

    boolean isSendding = false;

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static boolean isZip = false;
    private static boolean isEncrypt = true;

    private String finalUrl = "";

    static class Instance {
        static UploadManager instance = new UploadManager();
    }

    public static UploadManager getInstance() {
        return Instance.instance;
    }

    public void setServerUrl(String url){
        this.finalUrl = url;
    }

    public void sendEvent(final Context context, List<Event> events, final UploadCallback callback) {
        final JSONArray jar = new JSONArray();

        for (Event event : events) {
            jar.put(event.getData());
        }
        if (ThreadUtil.isOnMainThread()) {
            ThreadUtil.runInBackground(new Runnable() {
                @Override
                public void run() {
                    sendEvent(context, jar.toString(), callback);
                }
            });
        } else {
            sendEvent(context, jar.toString(), callback);
        }
    }

    public void sendEvent(final Context context, final String jsonStr, final UploadCallback callback) {
        synchronized (UploadManager.class) {
            if (isSendding || TextUtils.isEmpty(jsonStr)) {
                return;
            }

            Boolean successful = false;
            HttpURLConnection connection = null;
            BufferedWriter writer = null;
            OutputStream outputStream = null;
            try {

                int zipJsonLength = 0;
                byte[] zipJson = null;
//                if (!DebugInfoUtil.isBebugEnable(context)) {
                    if (isZip) {
                        try {
                            String sourceJson = jsonStr;
//                            if (isEncrypt) {
//                                sourceJson = new AESencryptUtil("ivqH3zTRdOULfKKC", "jQhQQc9P9IP8B8g3").encryptZeroPaddingForHexString(jsonStr);
//                                LogUtil.d("encrypt sendEvent: encryptJson=" + sourceJson);
//                            }
                            zipJson = GzipUtil.compressForGzip(sourceJson);
                            isZip = true;
                        } catch (Exception ex) {
                            isZip = false;
                        }
                    }
                    if (zipJson != null) {
                        zipJsonLength = zipJson.length;
                    }
//                    if (zipJsonLength == 0 && isFirst) {
//                        isFirst = false;
//                        isZip = false;
//                    }
//                }

                URL url = new URL(finalUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-agent", "Mozilla/5.0 (Linux; Android 4.4; Nexus 4 Build/KRT16S) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.36");
                connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Accept-Encoding", "deflate, sdch");
                connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");

                //如果压缩了数据,添加Content-Type:application/octet-stream
                if (zipJsonLength > 0) {
                    if (isEncrypt){
                        connection.setRequestProperty("Content-Type", "application/secret-stream");
                    }else {
                        connection.setRequestProperty("Content-Type", "application/octet-stream");
                    }
                    connection.setRequestProperty("Content-length", String.valueOf(zipJsonLength));
                } else {
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Content-length", String.valueOf(jsonStr.getBytes().length));
                }
                connection.setConnectTimeout(CONNECT_TIME_OUT);
                connection.setReadTimeout(CONNECT_TIME_OUT);

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
                    connection.setRequestProperty("Connection", "close");
                }
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);


                if (!TextUtils.isEmpty(jsonStr)) {
                    outputStream = connection.getOutputStream();
                    //如果是压缩数据,直接写入outputStream
                    if (zipJsonLength > 0) {
                        outputStream.write(zipJson);
                        outputStream.flush();
                        outputStream.close();
                    } else {
                        writer = new BufferedWriter(new OutputStreamWriter(outputStream, DEFAULT_CHARSET));
                        writer.write(jsonStr);
                        writer.flush();
                        writer.close();
                    }
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder strbuilder = new StringBuilder("utf-8");

                String strline = null;
                while ((strline = in.readLine()) != null) {
                    strbuilder.append(strline);
                }

//                LogUtil.d(" return: " + strbuilder.toString());

                int responseCode = connection.getResponseCode();


                successful = (responseCode == HttpURLConnection.HTTP_ACCEPTED || responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK);

                if (successful) {
                    LogUtil.d("sendEvent succeed response:" + strbuilder.toString());
                    callback.onSucceed();
                } else {
                    LogUtil.d("sendEvent fail response:" + strbuilder.toString());
                    callback.onFail(new Exception(strbuilder.toString()));
                }

            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.d("sendEvent fail " + e.getMessage());
                callback.onFail(e);
            } finally {

                if (writer != null) {
                    try {
                        writer.flush();
                    } catch (IOException e) {
//                        e.printStackTrace();
                    }
                    try {
                        writer.close();
                    } catch (IOException e) {
//                        e.printStackTrace();
                    }
                }


                if (outputStream != null) {
                    try {
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

    }


    public interface UploadCallback {
        void onSucceed();

        void onFail(Exception e);
    }
}
