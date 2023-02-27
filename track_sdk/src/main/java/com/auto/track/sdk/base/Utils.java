package com.auto.track.sdk.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.util.UUID;

public class Utils {
    private static final String keyName = ".aset.bmju";
    private static final String fileName = ".seg.qww";

    public static String getAndroidID(Context mContext) {
        String androidID = "";
        try {
            String data = getData(mContext, keyName, fileName);
            if (TextUtils.isEmpty(data)) {
                androidID = DeviceIdUtil.getDeviceId(mContext);
            } else {
                androidID = data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveData(mContext, keyName, androidID, fileName);
        return androidID;
    }

    private static boolean saveData(Context ct, String key, String value, String prefName) {
        boolean rs = true;
        try {
            SharedPreferences sp = ct.getSharedPreferences(prefName, Context.MODE_PRIVATE);
            sp.edit().putString(key, value)
                    .commit();

        } catch (Exception e) {
            rs = false;
        }
        return rs;
    }

    private static String getData(final Context ct, final String key, String prefName) {


        try {
            SharedPreferences sp = ct.getSharedPreferences(prefName, Context.MODE_PRIVATE);
            String rs = sp.getString(key, "");
            return rs;

        } catch (Exception e) {
            return "";
        }
    }

    private static void addIndentBlank(StringBuilder sb, int indent) {
        try {
            for (int i = 0; i < indent; i++) {
                sb.append('\t');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String formatJson(String jsonStr) {
        try {
            if (null == jsonStr || "".equals(jsonStr)) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            char last;
            char current = '\0';
            int indent = 0;
            boolean isInQuotationMarks = false;
            for (int i = 0; i < jsonStr.length(); i++) {
                last = current;
                current = jsonStr.charAt(i);
                switch (current) {
                    case '"':
                        if (last != '\\') {
                            isInQuotationMarks = !isInQuotationMarks;
                        }
                        sb.append(current);
                        break;
                    case '{':
                    case '[':
                        sb.append(current);
                        if (!isInQuotationMarks) {
                            sb.append('\n');
                            indent++;
                            addIndentBlank(sb, indent);
                        }
                        break;
                    case '}':
                    case ']':
                        if (!isInQuotationMarks) {
                            sb.append('\n');
                            indent--;
                            addIndentBlank(sb, indent);
                        }
                        sb.append(current);
                        break;
                    case ',':
                        sb.append(current);
                        if (last != '\\' && !isInQuotationMarks) {
                            sb.append('\n');
                            addIndentBlank(sb, indent);
                        }
                        break;
                    default:
                        sb.append(current);
                }
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
