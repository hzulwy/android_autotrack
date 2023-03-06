package com.auto.track.sdk.appOnClick;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Keep;

import com.auto.track.sdk.AutoTrackAPI;
import com.auto.track.sdk.base.Utils;

import org.json.JSONObject;

import java.util.Map;

public class SilveryTrackHelper {

    @Keep
    public static void trackViewOnClick(DialogInterface dialogInterface, int whichButton) {
        try {
            Dialog dialog = null;
            if (dialogInterface instanceof Dialog) {
                dialog = (Dialog) dialogInterface;
            }
            if (dialog == null) {
                return;
            }
            Context context = dialog.getContext();
            Activity activity = Utils.getActivityFromContext(context);

            if(activity==null){
                activity = dialog.getOwnerActivity();
            }
            JSONObject properties = new JSONObject();
            if(activity!=null){
                properties.put("activity",activity.getClass().getCanonicalName());
            }

            Button button = null;
            if (dialog instanceof android.app.AlertDialog) {
                button = ((android.app.AlertDialog) dialog).getButton(whichButton);
            } else if (dialog instanceof androidx.appcompat.app.AlertDialog) {
                button = ((androidx.appcompat.app.AlertDialog) dialog).getButton(whichButton);
            }

            if (button != null) {
                properties.put("element_content", button.getText());
            }

            properties.put("element_type", "Dialog");

            AutoTrackAPI.getInstance().track("AppClick", properties);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Keep
    public static void trackViewOnClick(View view) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("element_type", Utils.getElementType(view));
            jsonObject.put("element_id", Utils.getViewId(view));
            jsonObject.put("element_content", Utils.getElementContent(view));

            Activity activity = Utils.getActivityFromView(view);
            if (activity != null) {
                jsonObject.put("activity", activity.getClass().getCanonicalName());
            }

            AutoTrackAPI.getInstance().track("AppClick", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Keep
    public static void trackViewOnClick(View view, Map<String, Object> map) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("element_type", Utils.getElementType(view));
            jsonObject.put("element_id", Utils.getViewId(view));
            jsonObject.put("element_content", Utils.getElementContent(view));

            Activity activity = Utils.getActivityFromView(view);
            if (activity != null) {
                jsonObject.put("activity", activity.getClass().getCanonicalName());
            }
            Utils.mapToJson(jsonObject,map);

            AutoTrackAPI.getInstance().track("AppClick", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Keep
    public static void trackTabHost(String tabName) {
        try {
            JSONObject properties = new JSONObject();

            properties.put("element_type", "TabHost");
            properties.put("element_content", tabName);
            AutoTrackAPI.getInstance().track("AppClick", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
