package com.example.exampletemplate2.src.main.home.pager_home

import com.example.exampletemplate2.src.domain.movies.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PagerHomeRetrofitInterface {
    @GET("/3/trending/movie/day")
    suspend fun getTrends(@Query("api_key")apiKey:String, @Query("language")language:String, @Query("page")page:Int): Response<Movies>
}