package com.auto.track.sdk.base;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Event implements Parcelable {

    JSONObject data = new JSONObject();
    public Event(String jsonStr) {
        try {
            data = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Event(JSONObject jsonObject){
        data = jsonObject;
    }


    protected Event(Parcel in) {
        try {
            data=new JSONObject(in.readString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(data.toString());
    }

    public String getUuid() {
        try {
            return data.getString("log_id");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public JSONObject getData() {
        return data;
    }

    public boolean isValidate(){
        return !TextUtils.isEmpty(getUuid());
    }
}
