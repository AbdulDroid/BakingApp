package com.example.floating.bakingapp.di

import android.app.Application

import com.example.floating.bakingapp.data.ApiService
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.io.IOException

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by apple on 14/01/2018.
 */
@Module
class NetModule {

    @Provides
    @Singleton
    internal fun providesHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    internal fun providesGson(): Gson {
        val builder = GsonBuilder()
        builder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        return builder.create()
    }

    @Provides
    @Singleton
    internal fun providesOkhttp(cache: Cache): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val builder = chain.request().newBuilder()
                    .header("Accept", "application/json")
                    .header("X-Requested-With", "XMLHttpRequest")
            chain.proceed(builder.build())
        }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
        builder.cache(cache)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(loggingInterceptor)
        return builder.build()

    }

    @Provides
    @Singleton
    internal fun providesRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiService.BASE_URL)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    internal fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
