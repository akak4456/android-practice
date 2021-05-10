package com.example.movieapp

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TMDBRetrofitClient {
    private var instance:Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()
    val apiKey = "384cf9a7199fe30fe80ee45bc6d79742"
    val imageUrl = "https://image.tmdb.org/t/p/original"
    private const val BASE_URL = "https://api.themoviedb.org/"

    fun getInstance(okHttpClient: OkHttpClient): Retrofit {
        if(instance == null){
            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
        }

        return instance!!
    }
}