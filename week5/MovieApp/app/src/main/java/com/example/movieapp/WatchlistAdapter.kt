package com.example.movieapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.*
import com.kakao.sdk.user.UserApiClient

class WatchlistAdapter(private val context: Context,private val entities:ArrayList<WatchlistEntity>,private val watchlistDao:WatchlistDao ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_HEADER = 0
    val TYPE_ITEM = 1

    inner class HeaderViewHolder(private val binding: WatchlistHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setUp(){
            binding.logoutBtn.setOnClickListener{
                UserApiClient.instance.unlink { error ->
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

    inner class ItemViewHolder(private val binding: WatchlistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(model:WatchlistEntity,position:Int){
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.setColorSchemeColors(Color.parseColor("#ff7f00"))
            circularProgressDrawable.strokeWidth = 6f
            circularProgressDrawable.centerRadius = 40f
            circularProgressDrawable.start()

            Glide.with(context)
                .load(TMDBRetrofitClient.imageUrl+model.imageSrc)
                .placeholder(circularProgressDrawable)
                .into(binding.itemImg)
            binding.itemImg.clipToOutline = true

            binding.itemTitle.text = model.title
            binding.releaseDateAndRuntime.text = model.releaseDate + " · "+model.runtime

            binding.watchlistItemLayout.setOnClickListener{
                val intent = Intent(context,OneMovieActivity::class.java)
                intent.putExtra("movie_id",model.oneMovieId)
                context.startActivity(intent)
            }

            binding.watchlistDeleteBtn.setOnClickListener{
                watchlistDao.deleteWatchlist(model.id,model.oneMovieId)
                entities.removeAt(position)
                notifyDataSetChanged()

                Toast.makeText(context,"관심 목록에서 삭제되었습니다",Toast.LENGTH_SHORT).show()
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
            val bindind = WatchlistItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            holder = ItemViewHolder(bindind)
        }

        return holder!!
    }

    override fun getItemCount(): Int = 1 + entities.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HeaderViewHolder){
            val headerViewHolder : HeaderViewHolder = holder
            headerViewHolder.setUp()
        }else if(holder is ItemViewHolder){
            val itemViewHolder : ItemViewHolder = holder
            itemViewHolder.setData(entities[position-1],position-1)
        }
    }
}