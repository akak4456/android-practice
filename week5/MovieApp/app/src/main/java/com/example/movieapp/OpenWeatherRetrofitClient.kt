package com.example.movieapp

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenWeatherRetrofitClient {
    private var instance: Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()
    val apiKey = "5c535bb2f67000545ff32ee119343a96"
    private const val BASE_URL = "https://api.openweathermap.org/"

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