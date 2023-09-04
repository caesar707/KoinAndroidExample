package com.example.koinandroid

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface BasicApiService {

    @GET("daily")
    fun getDaysForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") days: Int,
        @Query("appid") appID: String,
        @Query("units") units: String,
    ): Single<ForecastResponse>
}