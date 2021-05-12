package com.example.movieapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.ActivityOneMovieBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class OneMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOneMovieBinding

    private lateinit var context: Context

    private lateinit var tmdbRetrofit: Retrofit
    private lateinit var tmdbService: TMDBRetrofitService
    private var movieId = 0
    private lateinit var oneMovieAdapter: OneMovieAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOneMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this
        movieId = intent.getIntExtra("movie_id", 0)
        overridePendingTransition(R.anim.onemovie_main_fadein, R.anim.main_onemovie_fadeout)

        binding.rv.layoutManager = LinearLayoutManager(context)

        initRetrofit()
        CoroutineScope(Main).launch {
            val oneRet = async(IO) {
                tmdbService.getMovieOne(movieId, TMDBRetrofitClient.apiKey, "ko-KR")
            }
            val creditsRet = async(IO) {
                tmdbService.getCredits(movieId, TMDBRetrofitClient.apiKey, "ko-KR")
            }
            val firstResultRet = async(IO) {
                tmdbService.getRecommendation(movieId, TMDBRetrofitClient.apiKey, "ko-KR", 1)
            }

            val oneMovie = oneRet.await()
            val credits = creditsRet.await()
            val firstResult = firstResultRet.await()

            val oneMovieInfo = OneMovieInfo(oneMovie, credits, firstResult.results, firstResult.total_pages, movieId)
            oneMovieAdapter = OneMovieAdapter(context, oneMovieInfo, tmdbService)
            binding.rv.adapter = oneMovieAdapter

            //배경화면 설정 아래부터
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.setColorSchemeColors(Color.parseColor("#ff7f00"))
            circularProgressDrawable.strokeWidth = 6f
            circularProgressDrawable.centerRadius = 40f
            circularProgressDrawable.start()

            Glide.with(context as OneMovieActivity)
                    .load(TMDBRetrofitClient.imageUrl + oneMovie.backdrop_path)
                    .placeholder(circularProgressDrawable)
                    .into(binding.imgBackground)
        }
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0){
                    if (binding.rvBackground.y - dy >= -binding.imgBackground.height) {
                        binding.rvBackground.y -= dy
                    } else {
                        binding.rvBackground.y = (-binding.imgBackground.height).toFloat()
                    }
                }
                if(dy < 0){
                    if (binding.rvBackground.y - dy <= 0) {
                        binding.rvBackground.y -= dy
                    }else{
                        binding.rvBackground.y = 0.0f
                    }
                }
            }
        })
    }

    override fun finish() {
        super.finish()

        overridePendingTransition(R.anim.main_onemovie_fade_in, R.anim.onemovie_main_fadeout)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.main_onemovie_fade_in, R.anim.onemovie_main_fadeout)
    }

    private fun initRetrofit() {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(logging)
        val httpClient = httpClientBuilder.build()
        tmdbRetrofit = TMDBRetrofitClient.getInstance(httpClient)
        tmdbService = tmdbRetrofit.create(TMDBRetrofitService::class.java)

    }

    fun pxToDp(px: Int): Int {
        val displayMetrics: DisplayMetrics = this.resources.displayMetrics
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun dpToPx(dp: Int): Int {
        val displayMetrics: DisplayMetrics = this.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}