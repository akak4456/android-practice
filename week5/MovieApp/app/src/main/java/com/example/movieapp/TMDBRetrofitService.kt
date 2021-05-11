package com.example.movieapp

import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBRetrofitService {
    @GET("/3/trending/movie/day")
    suspend fun getTodayTrend(@Query("api_key")apiKey:String, @Query("language")language:String) : Results
    @GET("/3/trending/movie/day")
    suspend fun getTodayTrend(@Query("api_key")apiKey:String, @Query("language")language:String,@Query("page")page:Int) : Results

    @GET("/3/discover/movie")
    suspend fun getMovieListByGenre(@Query("api_key")apiKey:String,@Query("with_genres")withGenres:String,@Query("language")language:String):Results

    @GET("/3/discover/movie")
    suspend fun getMovieListByGenre(@Query("api_key")apiKey:String,@Query("with_genres")withGenres:String,@Query("language")language:String,@Query("page")page:Int):Results
}