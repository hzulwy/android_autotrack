package com.auto.track.sdk.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDBHelper extends SQLiteOpenHelper {

     EventDBHelper(Context context){
        this(context, null);
    }

    EventDBHelper(Context context,DatabaseErrorHandler handler){
        super(context, "autoTrack.db", null, 1, handler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE careList (id integer primary key autoincrement, pkgName varchar(40))");

        db.execSQL("create table if not exists tb_event(_id integer primary key autoincrement,uuid text,content text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
