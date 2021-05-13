package com.example.movieapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.*

class MainWithWeatherAdapter(private val context: Context):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_ITEM_1 = 0
    private val TYPE_ITEM_2 = 1
    private val TYPE_LOADING = 2
    private val items = ArrayList<Result>()
    inner class LoadingViewHolder(private val binding:ItemLoading2Binding):RecyclerView.ViewHolder(binding.root){

    }
    inner class Item1ViewHolder(private val binding: MainRvWithWeatherItem1Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(model: Result, position: Int) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.setColorSchemeColors(Color.parseColor("#ff7f00"))
            circularProgressDrawable.strokeWidth = 6f
            circularProgressDrawable.centerRadius = 40f
            circularProgressDrawable.start()

            Glide.with(context)
                .load(TMDBRetrofitClient.imageUrl+model.poster_path)
                .placeholder(circularProgressDrawable)
                .into(binding.showImg)
            binding.showImg.clipToOutline = true

            binding.showImg.setOnClickListener{
                val intent = Intent(context,OneMovieActivity::class.java)
                intent.putExtra("movie_id",model.id)
                context.startActivity(intent)
            }
        }
    }

    inner class Item2ViewHolder(private val binding: MainRvWithWeatherItem2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(model: Result, position: Int) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.setColorSchemeColors(Color.parseColor("#ff7f00"))
            circularProgressDrawable.strokeWidth = 6f
            circularProgressDrawable.centerRadius = 40f
            circularProgressDrawable.start()

            Glide.with(context)
                .load(TMDBRetrofitClient.imageUrl+model.poster_path)
                .placeholder(circularProgressDrawable)
                .into(binding.showImg)
            binding.showImg.clipToOutline = true

            binding.showImg.setOnClickListener{
                val intent = Intent(context,OneMovieActivity::class.java)
                intent.putExtra("movie_id",model.id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(items[position].backdrop_path == "!"){
            return TYPE_LOADING
        }else if((position+1)%7 == 1){
            return TYPE_ITEM_2
        }else{
            return TYPE_ITEM_1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_ITEM_1 -> {
                val bindind = MainRvWithWeatherItem1Binding.inflate(LayoutInflater.from(parent.context),parent,false)
                Item1ViewHolder(bindind)
            }
            TYPE_ITEM_2 -> {
                val bindind = MainRvWithWeatherItem2Binding.inflate(LayoutInflater.from(parent.context),parent,false)
                Item2ViewHolder(bindind)
            }
            else -> {
                val bindind = ItemLoading2Binding.inflate(LayoutInflater.from(parent.context),parent,false)
                LoadingViewHolder(bindind)
            }
        }
    }

    override fun getItemCount(): Int =items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is Item1ViewHolder){
            val item1ViewHolder:Item1ViewHolder = holder
            item1ViewHolder.setData(items[position],position)
        }else if(holder is Item2ViewHolder){
            val item2ViewHolder:Item2ViewHolder = holder
            val layoutParams:StaggeredGridLayoutManager.LayoutParams = StaggeredGridLayoutManager.LayoutParams(item2ViewHolder.itemView.layoutParams)
            layoutParams.isFullSpan = true
            item2ViewHolder.itemView.layoutParams = layoutParams
            item2ViewHolder.setData(items[position],position)
        }else if(holder is LoadingViewHolder){
            val loadingViewHolder : LoadingViewHolder = holder
            val layoutParams:StaggeredGridLayoutManager.LayoutParams = StaggeredGridLayoutManager.LayoutParams(loadingViewHolder.itemView.layoutParams)
            layoutParams.isFullSpan = true
            loadingViewHolder.itemView.layoutParams = layoutParams
        }
    }

    fun setList(i:List<Result>){
        items.addAll(i)
        items.add(Result(false,"!", emptyList(),0,"","","","",0.0,"","","",false,0.0,0))
    }

    fun deleteLoading(){
        if(items[items.lastIndex].backdrop_path == "!") {
            items.removeAt(items.lastIndex)
        }
    }
}