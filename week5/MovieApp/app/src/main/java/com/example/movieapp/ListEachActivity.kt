package com.example.movieapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.ActivityListEachBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class ListEachActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var binding:ActivityListEachBinding

    private lateinit var title:String
    private var isTrend:Boolean = false
    private lateinit var genre:String
    private lateinit var adapter:ListEachAdapter

    private lateinit var tmdbRetrofit: Retrofit
    private lateinit var tmdbService:TMDBRetrofitService

    private var pageNum = 3
    private var totalPageNum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListEachBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this

        title = intent.getStringExtra("title")!!
        isTrend = intent.getBooleanExtra("isTrend",false)
        genre = intent.getStringExtra("genre")?:"none"

        adapter = ListEachAdapter(context,title)

        val manager = GridLayoutManager(context,3)
        manager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    adapter.TYPE_HEADER -> 3
                    adapter.TYPE_ITEM -> 1
                    adapter.TYPE_ITEM_LOADING -> 3
                    else -> 1
                }
            }
        }
        binding.rv.layoutManager = manager
        binding.rv.adapter = adapter

        overridePendingTransition(R.anim.listeach_main_fadein, R.anim.main_listeach_fadeout)

        initRetrofit()

        CoroutineScope(Main).launch{
            //3열이어서 3으로 나누어 떨어지는 60개를 불러옴
            if(isTrend){
                val trend1Ret = async(IO){
                    tmdbService.getTodayTrend(TMDBRetrofitClient.apiKey,"ko-KR",1)
                }
                val trend2Ret = async(IO){
                    tmdbService.getTodayTrend(TMDBRetrofitClient.apiKey,"ko-KR",2)
                }
                val trend3Ret = async(IO){
                    tmdbService.getTodayTrend(TMDBRetrofitClient.apiKey,"ko-KR",3)
                }
                val trendResults = awaitAll(trend1Ret,trend2Ret,trend3Ret)
                val lists = ArrayList<List<Result>>()
                lists.add(trendResults[0].results)
                lists.add(trendResults[1].results)
                lists.add(trendResults[2].results)
                adapter.setList(lists)
                adapter.notifyDataSetChanged()

                totalPageNum = trendResults[0].total_pages
            }else{
                val genre1Ret = async(IO) {
                    tmdbService.getMovieListByGenre(TMDBRetrofitClient.apiKey,genre,"ko-KR",1)
                }
                val genre2Ret = async(IO) {
                    tmdbService.getMovieListByGenre(TMDBRetrofitClient.apiKey,genre,"ko-KR",2)
                }
                val genre3Ret = async(IO) {
                    tmdbService.getMovieListByGenre(TMDBRetrofitClient.apiKey,genre,"ko-KR",3)
                }

                val genreResults = awaitAll(genre1Ret,genre2Ret,genre3Ret)

                val lists = ArrayList<List<Result>>()
                lists.add(genreResults[0].results)
                lists.add(genreResults[1].results)
                lists.add(genreResults[2].results)
                adapter.setList(lists)
                adapter.notifyDataSetChanged()

                totalPageNum = genreResults[0].total_pages
            }

            binding.rv.addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!recyclerView.canScrollVertically(1)){
                        CoroutineScope(IO).launch {
                            val deferredList:ArrayList<Deferred<Results>> = ArrayList()
                            for(i in 1..3){
                                if(pageNum < totalPageNum){
                                    if(isTrend){
                                        deferredList.add(async {
                                            tmdbService.getTodayTrend(TMDBRetrofitClient.apiKey,"ko-KR",pageNum+i)
                                        })
                                    }else{
                                        deferredList.add(async {
                                            tmdbService.getMovieListByGenre(TMDBRetrofitClient.apiKey,genre,"ko-KR",pageNum+i)
                                        })
                                    }
                                }
                            }
                            val results = deferredList.awaitAll()
                            val lists = ArrayList<List<Result>>()
                            var totalSize = 0
                            for(i in results.indices){
                                totalSize += results[i].results.size
                                lists.add(results[i].results)
                            }
                            adapter.deleteLoading()
                            adapter.setList(lists)

                            withContext(Main){
                                adapter.notifyDataSetChanged()
                            }

                            pageNum+=3
                        }
                    }
                }
            })
        }
    }

    override fun finish() {
        super.finish()

        overridePendingTransition(R.anim.main_listeach_fadein, R.anim.listeach_main_fadeout)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.main_listeach_fadein, R.anim.listeach_main_fadeout)
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