package com.example.movieapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.MainInfoItemBinding
import com.example.movieapp.databinding.MainRvHeaderBinding
import com.example.movieapp.databinding.MainRvWithWeatherBinding
import com.example.movieapp.databinding.WatchlistHeaderBinding

class WatchlistAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_HEADER = 0
    val TYPE_ITEM = 1

    inner class HeaderViewHolder(private val binding: WatchlistHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_HEADER
        } else {
            return TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var holder: RecyclerView.ViewHolder? = null
        if (viewType == TYPE_HEADER) {
            val bindind =
                WatchlistHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            holder = HeaderViewHolder(bindind)
        } else if (viewType == TYPE_ITEM) {
        }

        return holder!!
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HeaderViewHolder){
            val headerViewHolder : HeaderViewHolder = holder
        }
    }
}