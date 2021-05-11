package com.example.movieapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.MainInfoItemBinding
import com.example.movieapp.databinding.MainRvHeaderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainAdapter(private val context: Context, private val info:ArrayList<MainInfo>, private val tmdbService:TMDBRetrofitService): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    private var pageNumList = IntArray(info.size) {1}
    private var trendPageNum = 1
    inner class HeaderViewHolder(private val binding:MainRvHeaderBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun setHeader(){
            binding.loginImage.clipToOutline = true
        }
    }

    inner class ItemViewHolder(private val binding: MainInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var recyclerView:RecyclerView = binding.mainInfoHorizontalRv
        fun setData(model: MainInfo, position: Int) {
            binding.mainTitleTv.text = model.mainTitle

            if(model.subTitle.isEmpty()){
                binding.subTitleTv.visibility = View.GONE
            }else{
                binding.subTitleTv.text = model.subTitle
            }

            binding.mainInfoTop.setOnClickListener{
                val intent = Intent(context,ListEachActivity::class.java)
                intent.putExtra("title",model.mainTitle)
                if(position == 0){
                    intent.putExtra("isTrend",true)
                }else{
                    intent.putExtra("isTrend",false)
                    intent.putExtra("genre",model.genre)
                }

                context.startActivity(intent)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0){
            return TYPE_HEADER
        }else{
            return TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var holder:RecyclerView.ViewHolder? = null
        if(viewType == TYPE_HEADER){
            val bindind = MainRvHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            holder = HeaderViewHolder(bindind)
        }else if(viewType == TYPE_ITEM){
            val bindind = MainInfoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            holder = ItemViewHolder(bindind)
        }

        return holder!!
    }

    override fun getItemCount(): Int = info.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HeaderViewHolder){
            val headerViewHolder:HeaderViewHolder = holder
            headerViewHolder.setHeader()
        }else{
            val newPos = position-1
            val itemViewHolder:ItemViewHolder = holder as ItemViewHolder
            val rv:RecyclerView = itemViewHolder.recyclerView
            rv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            val horizontalAdapter = MainHorizontalAdapter(context)
            rv.adapter = horizontalAdapter
            horizontalAdapter.setList(info[newPos].results)
            rv.addOnScrollListener(object:RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition = (rv.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = horizontalAdapter.itemCount - 1
                    if(!rv.canScrollHorizontally(1)&&lastVisibleItemPosition == itemTotalCount){
                        horizontalAdapter.deleteLoading()
                        if(pageNumList[newPos] < info[newPos].total_pages) {
                            CoroutineScope(IO).launch {
                                pageNumList[newPos]++
                                var results: List<Result>? = null
                                if (newPos == 0) {
                                    results = withContext(IO) { tmdbService.getTodayTrend(TMDBRetrofitClient.apiKey, "ko-KR", pageNumList[newPos]) }.results
                                }else{
                                    results = withContext(IO) { tmdbService.getMovieListByGenre(TMDBRetrofitClient.apiKey,"28,12","ko-KR",pageNumList[newPos]) }.results
                                }
                                horizontalAdapter.setList(results!!)
                                withContext(Main){
                                    horizontalAdapter.notifyItemRangeInserted(itemTotalCount + 1, results!!.size + 1)
                                }
                            }
                        }
                    }
                }
            })

            itemViewHolder.setData(info[newPos],newPos)
        }
    }

}