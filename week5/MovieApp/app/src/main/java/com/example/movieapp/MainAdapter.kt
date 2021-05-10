package com.example.movieapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.MainInfoItemBinding
import com.example.movieapp.databinding.MainRvHeaderBinding

class MainAdapter(private val context: Context, private val info:ArrayList<MainInfo>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    inner class HeaderViewHolder(private val binding:MainRvHeaderBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun setHeader(){
            binding.loginImage.clipToOutline = true
        }
    }

    inner class ItemViewHolder(private val binding: MainInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
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
            val itemViewHolder:ItemViewHolder = holder as ItemViewHolder
            itemViewHolder.setData(info[position-1],position-1)
        }

    }
}