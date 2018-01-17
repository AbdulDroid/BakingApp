package com.example.floating.bakingapp

import android.app.Application

import com.example.floating.bakingapp.di.AppComponent
import com.example.floating.bakingapp.di.AppModule
import com.example.floating.bakingapp.di.DaggerAppComponent
import com.example.floating.bakingapp.di.NetModule

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

class AppController : Application() {

    var appComponent: AppComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()


        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule())
                .build()
    }
}
