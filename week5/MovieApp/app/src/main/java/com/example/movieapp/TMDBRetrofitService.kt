package com.example.movieapp

import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("/3/movie/{movie_id}")
    suspend fun getMovieOne(@Path("movie_id")movieId:Int,@Query("api_key")apiKey:String,@Query("language")language:String):OneMovie

    @GET("/3/movie/{movie_id}/credits")
    suspend fun getCredits(@Path("movie_id")movieId:Int,@Query("api_key")apiKey:String,@Query("language")language:String):Credits

    @GET("/3/movie/{movie_id}/recommendations")
    suspend fun getRecommendation(@Path("movie_id")movieId:Int,@Query("api_key")apiKey:String,@Query("language")language:String,@Query("page")page:Int):Results
}