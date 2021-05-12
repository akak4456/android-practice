package com.example.movieapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.ItemLoadingBinding
import com.example.movieapp.databinding.MainInfoHorizontalItemBinding
import com.example.movieapp.databinding.OneMovieItemRelateItemBinding

class OneMovieHorizontalAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    val TYPE_ITEM = 0
    val TYPE_LOADING = 1
    private val items = ArrayList<Result>()

    inner class LoadingViewHolder(private val binding: ItemLoadingBinding):RecyclerView.ViewHolder(binding.root){

    }
    inner class ItemViewHolder(private val binding: OneMovieItemRelateItemBinding) :
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
                .into(binding.itemImg)
            binding.itemImg.clipToOutline = true

            binding.title.text = model.title
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(items[position].backdrop_path == "!"){
            return TYPE_LOADING
        }else{
            return TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_ITEM -> {
                val bindind = OneMovieItemRelateItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                ItemViewHolder(bindind)
            }
            else -> {
                val bindind = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                LoadingViewHolder(bindind)
            }
        }
    }

    override fun getItemCount(): Int =items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder){
            holder.setData(items[position],position)
        }else{

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