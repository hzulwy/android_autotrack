package com.auto.track.sdk.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.auto.track.sdk.base.Event;
import com.auto.track.sdk.base.LogUtil;

import java.util.ArrayList;
import java.util.List;


public class EventDAO {

    volatile EventDBHelper helper;

    public EventDAO(Context context) {
        helper = new EventDBHelper(context);
    }

    public void initHelper(Context context) {
        helper = new EventDBHelper(context);
    }

    public synchronized boolean delete(Event event) throws SQLiteException {
        if(event==null){
            LogUtil.d("EventDAO delete event null");
            return false;
        }

        boolean result = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            db.execSQL("delete from tb_event where uuid=?", new Object[]{event.getUuid()});
            db.close();
            result = true;
        }
        return result;
    }

    public synchronized boolean delete(List<Event> events) throws SQLiteException {
        if(events==null || events.size()==0){
            LogUtil.d("EventDAO delete event null");
            return false;
        }

        boolean result = false;
        SQLiteDatabase db = helper.getWritableDatabase();

        StringBuilder sql = new StringBuilder("delete from tb_event where uuid in(");
        Event event;
        int len = events.size();

        for (int i = 0; i < len; i++) {
            event = events.get(i);
            if (i != 0) {
                sql.append(",");
            }
            sql.append("'");
            sql.append(event.getUuid());
            sql.append("'");
        }
        sql.append(");");

        LogUtil.d("delete sql:" + sql.toString());

        if (db.isOpen()) {
            db.execSQL(sql.toString());
            db.close();
            result = true;
        }
        return result;
    }

    public synchronized boolean add(Event event) throws SQLiteException {
        if(event==null){
            LogUtil.d("EventDAO add event null");
            return false;
        }
        boolean result = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            db.execSQL("insert into tb_event(uuid,content) values(?,?)", new Object[]{event.getUuid(), event.getData().toString()});
            db.close();
            result = true;
        }
        return result;
    }

    public synchronized List<Event> getAllEvent() {
        List<Event> events = new ArrayList<>();
        try {

            boolean result = false;
            Event event = null;
            SQLiteDatabase db = helper.getReadableDatabase();
            int index_content = -1;
            String content;
            if (db.isOpen()) {
                Cursor cursor = db.rawQuery("select * from tb_event asc", new String[]{});

                if(cursor.getCount()>0) {
                    if(cursor.moveToFirst()) {
                        while (!cursor.isLast()) {
                            if (index_content < 0) {
                                index_content = cursor.getColumnIndex("content");
                            }
                            if (index_content > 0) {
                                content = cursor.getString(index_content);
                                event=new Event(content);
                                if(event.isValidate()) {
                                    events.add(event);
                                }
                            }
                            cursor.moveToNext();

                        }
                    }
                }
                cursor.close();
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return events;
        }
        return events;
    }

}
