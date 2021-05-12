package com.example.movieapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.ActivityOneMovieBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class OneMovieActivity:AppCompatActivity() {
    private lateinit var binding:ActivityOneMovieBinding

    private lateinit var context: Context

    private lateinit var tmdbRetrofit: Retrofit
    private lateinit var tmdbService:TMDBRetrofitService
    private var movieId = 0
    private lateinit var oneMovieAdapter:OneMovieAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOneMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this
        movieId = intent.getIntExtra("movie_id",0)
        overridePendingTransition(R.anim.onemovie_main_fadein, R.anim.main_onemovie_fadeout)

        binding.rv.layoutManager = LinearLayoutManager(context)

        initRetrofit()
        CoroutineScope(Main).launch {
            val oneRet = async(IO) {
                tmdbService.getMovieOne(movieId,TMDBRetrofitClient.apiKey,"ko-KR")
            }
            val creditsRet = async(IO) {
                tmdbService.getCredits(movieId,TMDBRetrofitClient.apiKey,"ko-KR")
            }
            val firstResultRet = async(IO){
                tmdbService.getRecommendation(movieId,TMDBRetrofitClient.apiKey,"ko-KR",1)
            }

            val oneMovie = oneRet.await()
            val credits = creditsRet.await()
            val firstResult = firstResultRet.await()

            val oneMovieInfo = OneMovieInfo(oneMovie,credits,firstResult.results,firstResult.total_pages,movieId)
            oneMovieAdapter = OneMovieAdapter(context,oneMovieInfo,tmdbService)
            binding.rv.adapter = oneMovieAdapter
        }
    }

    private fun initRetrofit(){
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(logging)
        val httpClient = httpClientBuilder.build()
        tmdbRetrofit = TMDBRetrofitClient.getInstance(httpClient)
        tmdbService = tmdbRetrofit.create(TMDBRetrofitService::class.java)

    }
}