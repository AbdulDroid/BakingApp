package com.example.floating.bakingapp;

import android.app.Application;
import android.text.TextUtils;

import com.example.floating.bakingapp.di.AppComponent;
import com.example.floating.bakingapp.di.AppModule;
import com.example.floating.bakingapp.di.DaggerAppComponent;
import com.example.floating.bakingapp.di.NetModule;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

public class AppController extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();


        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();
    }


    public AppComponent getAppComponent(){
        return appComponent;
    }

    /*public RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return  mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag){
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public  <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag){
        if (mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }*/
}
