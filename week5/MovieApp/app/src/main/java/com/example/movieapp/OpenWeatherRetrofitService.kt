package com.example.movieapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherRetrofitService {
    @GET("/data/2.5/weather")
    fun getWeatherByLoc(@Query("lat")lat:Double, @Query("lon")lon:Double, @Query("appid")api_key:String): Call<Weather>
}