package com.example.movieapp

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.*

class ListEachAdapter(private val context: Context,private val givenTitle:String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_HEADER = 0
    val TYPE_ITEM = 1
    val TYPE_ITEM_LOADING = 2
    private val items = ArrayList<Result>()
    inner class LoadingViewHolder(private val binding:ListEachItemLoadingBinding):RecyclerView.ViewHolder(binding.root){

    }
    inner class HeaderViewHolder(private val binding: ListEachRvHeaderBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun setTitle(title:String){
            binding.title.text = title
        }
    }

    inner class ItemViewHolder(private val binding: ListEachRvItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun setData(model: Result, position: Int) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.setColorSchemeColors(Color.parseColor("#ff7f00"))
            circularProgressDrawable.strokeWidth = 6f
            circularProgressDrawable.centerRadius = 40f
            circularProgressDrawable.start()

            Glide.with(context)
                    .load(TMDBRetrofitClient.imageUrl+model.backdrop_path)
                    .placeholder(circularProgressDrawable)
                    .into(binding.showImg)
            binding.showImg.clipToOutline = true

            binding.showTitle.text = model.title
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0){
            return TYPE_HEADER
        }else if(items[position-1].backdrop_path == "!"){
            return TYPE_ITEM_LOADING
        }else{
            return TYPE_ITEM
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var holder:RecyclerView.ViewHolder? = null
        if(viewType == TYPE_HEADER){
            val bindind = ListEachRvHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            holder = HeaderViewHolder(bindind)
        }else if(viewType == TYPE_ITEM){
            val bindind = ListEachRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            holder = ItemViewHolder(bindind)
        }else if(viewType==TYPE_ITEM_LOADING){
            val bindind = ListEachItemLoadingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            holder = LoadingViewHolder(bindind)
        }

        return holder!!
    }

    override fun getItemCount(): Int = items.size+1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HeaderViewHolder){
            val headerHolder:HeaderViewHolder = holder
            headerHolder.setTitle(givenTitle)
        }else if(holder is LoadingViewHolder){
            val loadingHolder:LoadingViewHolder = holder
        }else{
            val newPos = position-1
            val itemHolder:ItemViewHolder = holder as ItemViewHolder
            itemHolder.setData(items[newPos],newPos)
        }
    }

    fun setList(lists:ArrayList<List<Result>>){
        for(i in lists.indices){
            items.addAll(lists[i])
        }
        items.add(Result(false,"!", emptyList(),0,"","","","",0.0,"","","",false,0.0,0))
        Log.d("TMP",items.size.toString())
    }

    fun deleteLoading(){
        if(items[items.lastIndex].backdrop_path == "!") {
            items.removeAt(items.lastIndex)
        }
    }
}