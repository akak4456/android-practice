package com.example.movieapp

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.MainInfoItemBinding
import com.example.movieapp.databinding.MainRvHeaderBinding
import kotlinx.coroutines.*


class MainAdapter(private val context: Context, private val info:ArrayList<MainInfo>, private val tmdbService:TMDBRetrofitService): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
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
            val mainAdapter = this
            val newPos = position-1
            val itemViewHolder:ItemViewHolder = holder as ItemViewHolder
            itemViewHolder.recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            val horizontalAdapter = MainHorizontalAdapter(context)
            itemViewHolder.recyclerView.adapter = horizontalAdapter

            (itemViewHolder.recyclerView.adapter as MainHorizontalAdapter).setList(info[newPos].results)
            itemViewHolder.recyclerView.addOnScrollListener(object:RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition = (itemViewHolder.recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = horizontalAdapter.itemCount - 1
                    if(!itemViewHolder.recyclerView.canScrollHorizontally(1)&&lastVisibleItemPosition == itemTotalCount){
                        horizontalAdapter.deleteLoading()
                        if(newPos == 0){
                            CoroutineScope(Dispatchers.Main).launch {
                                trendPageNum++
                                val trendRet = async(Dispatchers.IO){
                                    tmdbService.getTodayTrend(TMDBRetrofitClient.apiKey,"ko-KR",trendPageNum)
                                }
                                val trend = trendRet.await()
                                horizontalAdapter.setList(trend.results)
                                Log.d("TMP",horizontalAdapter.getList().size.toString())
                                horizontalAdapter.notifyItemRangeInserted(itemTotalCount+1,trend.results.size+1)
                            }
                        }
                    }
                }
            })
            itemViewHolder.setData(info[newPos],newPos)
        }
    }

}