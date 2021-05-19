package com.example.exampletemplate2.src.main.home.pager_home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exampletemplate2.databinding.RvPagerHomeHeaderBinding
import com.example.exampletemplate2.databinding.RvPagerHomeItemBinding

class PagerHomeRVAdapter(private val context: Context):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_HEADER = 0
    val TYPE_ITEM = 1

    private var pageNum = 1

    private val items:ArrayList<PagerHomeInfo> = ArrayList()

    inner class HeaderViewHolder(private val binding:RvPagerHomeHeaderBinding):RecyclerView.ViewHolder(binding.root){

    }

    inner class ItemViewHolder(private val binding:RvPagerHomeItemBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0){
            return TYPE_HEADER
        }else{
            return TYPE_ITEM
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var holder: RecyclerView.ViewHolder? = null
        if(viewType == TYPE_HEADER){
            val bindind = RvPagerHomeHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            holder = HeaderViewHolder(bindind)
        }else if(viewType == TYPE_ITEM){
            val bindind = RvPagerHomeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            holder = ItemViewHolder(bindind)
        }
        return holder!!
    }

    override fun getItemCount(): Int = items.size+1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HeaderViewHolder){
            val headerViewHolder:HeaderViewHolder = holder
        }else if(holder is ItemViewHolder){
            val itemViewHolder:ItemViewHolder = holder
        }
    }

}