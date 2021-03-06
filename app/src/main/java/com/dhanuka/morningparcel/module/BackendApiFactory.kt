package com.dhanuka.morningparcel.module

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.dhanuka.morningparcel.Settings
import com.dhanuka.morningparcel.service.BackendApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Factory to generate our Retrofit instance.
 */
internal class BackendApiFactory internal constructor(private val backendUrl: String) {

/*
    constructor(context: Context) : this("http://webtecnoworld.com/stripeNew/")
*/
    constructor(context: Context) : this("https://Vehicletrack.membocool.com?")

    fun create(): BackendApi {
        // Set your desired log level. Use Level.BODY for debugging errors.
        // Adding Rx so the calls can be Observable, and adding a Gson converter with
        // leniency to make parsing the results simple.
        val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(backendUrl)
            .client(httpClient)
            .build()
            .create(BackendApi::class.java)
    }

    private companion object {
        private const val TIMEOUT_SECONDS = 15L
    }
}
