package com.example.recyclerview

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.databinding.RvItemBinding

class ItemAdapter(private var context: Context, private val items:ArrayList<ItemInfo>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    lateinit var bindind: RvItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        bindind = RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bindind)
    }

    fun addData(replyInfo:ItemInfo){
        items.add(replyInfo)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(items[position],position)

    }

    inner class ViewHolder(private val binding: RvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(model: ItemInfo,position:Int) {
            binding.title.text = model.title
            binding.author.text = model.author
        }

    }
}