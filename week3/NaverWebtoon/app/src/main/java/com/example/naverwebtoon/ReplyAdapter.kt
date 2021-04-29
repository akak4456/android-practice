package com.example.naverwebtoon

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
import com.example.naverwebtoon.databinding.ReplyItemBinding
import com.example.naverwebtoon.databinding.WebtoonItemBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class ReplyAdapter(private var context: Context, private val reply:ArrayList<ReplyInfo>) : RecyclerView.Adapter<ReplyAdapter.ViewHolder>() {

    lateinit var bindind: ReplyItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        bindind = ReplyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bindind)
    }

    fun addData(replyInfo:ReplyInfo){
        reply.add(replyInfo)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return reply.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(reply[position],position)

    }

    fun changeIsUp(position:Int, targetValue:Boolean){
        reply[position].isUp = targetValue
        notifyDataSetChanged()
    }
    fun changeIsDown(position:Int, targetValue:Boolean){
        reply[position].isDown = targetValue
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ReplyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(model: ReplyInfo,position:Int) {
            binding.replyAuthor.text = model.replyAuthor
            binding.replyTime.text = model.replyDate
            var ss = SpannableString("    "+model.replyMsg)
            ss.setSpan(CenteredImageSpan(context,R.drawable.best),0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            ss.setSpan(ForegroundColorSpan(Color.BLACK),2,ss.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.replyMsg.text = ss

            binding.upBtn.text = model.upCnt.toString()
            binding.downBtn.text = model.downCnt.toString()

            if(model.isUp){
                binding.upBtn.setBackgroundResource(R.drawable.up_btn_checked)
                binding.upBtn.setTextColor(Color.parseColor("#fb5859"))

            }else{
                binding.upBtn.setBackgroundResource(R.drawable.button_unchecked)
                binding.upBtn.setTextColor(Color.parseColor("#333333"))
            }

            if(model.isDown){
                binding.downBtn.setBackgroundResource(R.drawable.down_btn_checked)
                binding.downBtn.setTextColor(Color.parseColor("#06a5e7"))
            }else{
                binding.downBtn.setBackgroundResource(R.drawable.button_unchecked)
                binding.downBtn.setTextColor(Color.parseColor("#333333"))
            }

            binding.upBtn.setOnClickListener{
                if(reply[position].isUp){
                    changeIsUp(position,false)
                }else{
                    changeIsUp(position,true)
                    changeIsDown(position,false)
                }
            }
            binding.downBtn.setOnClickListener{
                if(reply[position].isDown){
                    changeIsDown(position,false)
                }else{
                    changeIsDown(position,true)
                    changeIsUp(position,false)
                }
            }
        }

    }
}