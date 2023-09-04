package com.example.koinandroid

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private fun httpInterceptor() = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private fun basicOkhttpClient() = OkHttpClient.Builder()
    .addInterceptor(httpInterceptor())
    .build()

fun createBasicAuthService(): BasicApiService {

    val retrofit = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(basicOkhttpClient())
        .baseUrl("https://api.openweathermap.org/data/2.5/forecast/")
        .build()

    return retrofit.create(BasicApiService::class.java)
}