package com.auto.track.sdk.base;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.widget.SwitchCompat;

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

    public static String getViewId(View view) {
        String idString = null;
        try {
            if (view.getId() != View.NO_ID) {
                idString = view.getContext().getResources().getResourceEntryName(view.getId());
            }
        } catch (Exception e) {

        }
        return idString;
    }

    public static String getElementType(View view) {
        if (view == null) {
            return null;
        }

        String viewType = null;
        if (view instanceof CheckBox) { // CheckBox
            viewType = "CheckBox";
        } else if (view instanceof SwitchCompat) {
            viewType = "SwitchCompat";
        } else if (view instanceof RadioButton) { // RadioButton
            viewType = "RadioButton";
        } else if (view instanceof ToggleButton) { // ToggleButton
            viewType = "ToggleButton";
        } else if (view instanceof Button) { // Button
            viewType = "Button";
        } else if (view instanceof CheckedTextView) { // CheckedTextView
            viewType = "CheckedTextView";
        } else if (view instanceof TextView) { // TextView
            viewType = "TextView";
        } else if (view instanceof ImageButton) { // ImageButton
            viewType = "ImageButton";
        } else if (view instanceof ImageView) { // ImageView
            viewType = "ImageView";
        } else if (view instanceof RatingBar) {
            viewType = "RatingBar";
        } else if (view instanceof SeekBar) {
            viewType = "SeekBar";
        }
        return viewType;
    }

    public static String traverseViewContent(StringBuilder stringBuilder, ViewGroup root) {
        try {
            if (root == null) {
                return stringBuilder.toString();
            }

            final int childCount = root.getChildCount();
            for (int i = 0; i < childCount; ++i) {
                final View child = root.getChildAt(i);

                if (child.getVisibility() != View.VISIBLE) {
                    continue;
                }

                if (child instanceof ViewGroup) {
                    traverseViewContent(stringBuilder, (ViewGroup) child);
                } else {
                    CharSequence viewText = null;
                    if (child instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) child;
                        viewText = checkBox.getText();
                    } else if (child instanceof SwitchCompat) {
                        SwitchCompat switchCompat = (SwitchCompat) child;
                        viewText = switchCompat.getTextOn();
                    } else if (child instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) child;
                        viewText = radioButton.getText();
                    } else if (child instanceof ToggleButton) {
                        ToggleButton toggleButton = (ToggleButton) child;
                        boolean isChecked = toggleButton.isChecked();
                        if (isChecked) {
                            viewText = toggleButton.getTextOn();
                        } else {
                            viewText = toggleButton.getTextOff();
                        }
                    } else if (child instanceof Button) {
                        Button button = (Button) child;
                        viewText = button.getText();
                    } else if (child instanceof CheckedTextView) {
                        CheckedTextView textView = (CheckedTextView) child;
                        viewText = textView.getText();
                    } else if (child instanceof TextView) {
                        TextView textView = (TextView) child;
                        viewText = textView.getText();
                    } else if (child instanceof ImageView) {
                        ImageView imageView = (ImageView) child;
                        if (!TextUtils.isEmpty(imageView.getContentDescription())) {
                            viewText = imageView.getContentDescription().toString();
                        }
                    }

                    if (!TextUtils.isEmpty(viewText)) {
                        stringBuilder.append(viewText.toString());
                        stringBuilder.append("-");
                    }
                }
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return stringBuilder.toString();
        }
    }

    /**
     * 获取 View 上显示的文本
     *
     * @param view View
     * @return String
     */
    public static String getElementContent(View view) {
        if (view == null) {
            return null;
        }

        CharSequence viewText = null;
        if (view instanceof CheckBox) { // CheckBox
            CheckBox checkBox = (CheckBox) view;
            viewText = checkBox.getText();
        } else if (view instanceof SwitchCompat) {
            SwitchCompat switchCompat = (SwitchCompat) view;
            viewText = switchCompat.getTextOn();
        } else if (view instanceof RadioButton) { // RadioButton
            RadioButton radioButton = (RadioButton) view;
            viewText = radioButton.getText();
        } else if (view instanceof ToggleButton) { // ToggleButton
            ToggleButton toggleButton = (ToggleButton) view;
            boolean isChecked = toggleButton.isChecked();
            if (isChecked) {
                viewText = toggleButton.getTextOn();
            } else {
                viewText = toggleButton.getTextOff();
            }
        } else if (view instanceof Button) { // Button
            Button button = (Button) view;
            viewText = button.getText();
        } else if (view instanceof CheckedTextView) { // CheckedTextView
            CheckedTextView textView = (CheckedTextView) view;
            viewText = textView.getText();
        } else if (view instanceof TextView) { // TextView
            TextView textView = (TextView) view;
            viewText = textView.getText();
        } else if (view instanceof SeekBar) {
            SeekBar seekBar = (SeekBar) view;
            viewText = String.valueOf(seekBar.getProgress());
        } else if (view instanceof RatingBar) {
            RatingBar ratingBar = (RatingBar) view;
            viewText = String.valueOf(ratingBar.getRating());
        }
        if (viewText != null) {
            return viewText.toString();
        }
        return null;
    }

    public static Activity getActivityFromView(View view) {
        Activity activity = null;
        if (view == null) {
            return null;
        }

        try {
            Context context = view.getContext();
            if (context != null) {
                if (context instanceof Activity) {
                    activity = (Activity) context;
                } else if (context instanceof ContextWrapper) {
                    while (!(context instanceof Activity) && context instanceof ContextWrapper) {
                        context = ((ContextWrapper) context).getBaseContext();
                    }
                    if (context instanceof Activity) {
                        activity = (Activity) context;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activity;
    }

    public static Activity getActivityFromContext(Context context) {
        Activity activity = null;
        try {
            if (context != null) {
                if (context instanceof Activity) {
                    activity = (Activity) context;
                } else if (context instanceof ContextWrapper) {
                    while (!(context instanceof Activity) && context instanceof ContextWrapper) {
                        context = ((ContextWrapper) context).getBaseContext();
                    }
                    if (context instanceof Activity) {
                        activity = (Activity) context;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activity;
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
