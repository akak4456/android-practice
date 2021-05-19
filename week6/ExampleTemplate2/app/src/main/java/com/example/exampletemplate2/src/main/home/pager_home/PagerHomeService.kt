package com.example.exampletemplate2.src.main.home.pager_home

import android.util.Log
import com.example.exampletemplate2.config.ApplicationClass
import com.example.exampletemplate2.src.domain.movies.Movies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response

class PagerHomeService(val view:PagerHomeView) {
    fun tryGetMovies(){
        val pagerHomeRetrofitInterface = ApplicationClass.sRetrofit.create(PagerHomeRetrofitInterface::class.java)
        CoroutineScope(IO).launch {
            val response:Response<Movies> = pagerHomeRetrofitInterface.getTrends(ApplicationClass.TMDB_API_KEY,"ko-KR",1)
            if(response.isSuccessful){
                //성공 대응
                view.onGetMovieSuccess(response.body()!!)
            }else{
                //실패 대응
            }
        }
    }
}