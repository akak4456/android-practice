package com.example.movieapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.MainInfoItemBinding
import com.example.movieapp.databinding.MainRvHeaderBinding
import com.example.movieapp.databinding.MainRvWithWeatherBinding
import com.example.movieapp.databinding.WatchlistHeaderBinding
import com.kakao.sdk.user.UserApiClient

class WatchlistAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_HEADER = 0
    val TYPE_ITEM = 1

    inner class HeaderViewHolder(private val binding: WatchlistHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setUp(){
            binding.logoutBtn.setOnClickListener{
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        Toast.makeText(context, "로그아웃에 실패하였습니다. 관리자에게 문의해주세요", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(context, "로그아웃에 성공했습니다.", Toast.LENGTH_SHORT).show()
                        (context as WatchlistActivity).finish()
                        context.startActivity(context.intent)
                    }
                }
            }
        }
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
            headerViewHolder.setUp()
        }
    }
}