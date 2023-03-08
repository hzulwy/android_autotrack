package com.auto.track.sdk.manager;

import android.content.Context;

import com.auto.track.sdk.base.Event;
import com.auto.track.sdk.base.LogUtil;
import com.auto.track.sdk.db.EventDAO;

import java.util.ArrayList;
import java.util.List;


public class StorageManager {

    public final static int LIMIT_NUMBER=20;

    EventDAO Dao;
    List<Event> mEvnets=new ArrayList<>();

    static class Instance{
        static StorageManager instance=new StorageManager();
    }

    public static StorageManager getInstance(){
        return Instance.instance;
    }

    public void init(Context context){
        Dao =new EventDAO(context);
        mEvnets=Dao.getAllEvent();
    }

    public synchronized int getEventNumber(){
        return mEvnets.size();
    }

    public synchronized void add(Event event){
        boolean dbResult=false;
        if(Dao!=null) {
            try {
                dbResult = Dao.add(event);
            } catch (Exception exception) {
            }
        }
        if(dbResult) {
            mEvnets.add(event);
            LogUtil.d("add event size:"+mEvnets.size());
        }
    }

    public synchronized void delete(Event event){
        boolean dbResult=false;

        if(Dao!=null) {
            try {
                dbResult = Dao.delete(event);
            } catch (Exception exception) {
            }
        }
        if(dbResult) {
            mEvnets.remove(event);
            LogUtil.d("delete event size:"+mEvnets.size());
        }
    }

    public synchronized void delete(List<Event> events){
//        if(Dao==null){
//            return;
//        }
        boolean dbResult=false;
        if(Dao!=null) {
            try {
                dbResult = Dao.delete(events);
            } catch (Exception e) {
            }
        }
        if(dbResult) {
            mEvnets.removeAll(events);
            LogUtil.d("deletes event size:"+mEvnets.size());
        }
    }

    public synchronized List<Event> getEvents(int limit, long retryTime){
        List<Event> events=new ArrayList<>();

        long currentTime=System.currentTimeMillis();

        int len=mEvnets.size();
        for(int i=0;i<len;i++){
            if(events.size()>limit)
                break;
//            if((mEvnets.get(i).getLastTryTime()+retryTime)<currentTime) {
                events.add(mEvnets.get(i));
//            }
        }

        return events;
    }

    public synchronized List<Event> getEvents(){
        return getEvents(LIMIT_NUMBER, 20000);
    }
}
