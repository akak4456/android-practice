package com.example.movieapp

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var context: Context

    private lateinit var tmdbRetrofit: Retrofit
    private lateinit var tmdbService:TMDBRetrofitService

    private lateinit var binding:ActivityMainBinding

    private lateinit var mainAdapter: MainAdapter

    private val mainTitleList: Array<String> = arrayOf("최신영화 전용관","애니메이션 판타지 영화","액션/어드벤처")
    private val subTitleList:Array<String> = arrayOf("따끈한 신작 영화들을 즐기는 시간","","액션과 모험의 세계로 떠납시다!")
    /*
    주의 main과 sub는 1:1대응이 되어야 함
    ex) 최신영화 전용관 은 따끈한 신작 영화들을 즐기는 시간와 같이
    index가 대응이 되어야 함
     */

    private val mainInfoList:ArrayList<MainInfo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        binding.mainRv.layoutManager = LinearLayoutManager(this)

        initRetrofit()
        CoroutineScope(Main).launch {
            //아마 여기에 waiting bar를 그리는 것이 낫지 않을까?
            val trendRet = async(IO) {
                tmdbService.getTodayTrend(TMDBRetrofitClient.apiKey,"ko-KR")
            }
            val trend = trendRet.await()
            mainInfoList.add(MainInfo(mainTitleList[0],subTitleList[0],
                trend.results as MutableList<Result>))
            mainAdapter = MainAdapter(context,mainInfoList,tmdbService)
            binding.mainRv.adapter = mainAdapter
        }

    }

    private fun initRetrofit(){
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClientBuilder:OkHttpClient.Builder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(logging)
        val httpClient = httpClientBuilder.build()
        tmdbRetrofit = TMDBRetrofitClient.getInstance(httpClient)
        tmdbService = tmdbRetrofit.create(TMDBRetrofitService::class.java)
    }
}