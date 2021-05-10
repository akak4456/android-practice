package com.example.movieapp

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBRetrofitService {
    @GET("/3/trending/movie/day")
    suspend fun getTodayTrend(@Query("api_key")apiKey:String, @Query("language")language:String) : TodayTrend
    @GET("/3/trending/movie/day")
    suspend fun getTodayTrend(@Query("api_key")apiKey:String, @Query("language")language:String,@Query("page")page:Int) : TodayTrend
}