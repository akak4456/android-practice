package com.example.movieapp

import android.content.Context
import android.graphics.Color
import android.text.*
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.text.style.UpdateAppearance
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.OneMovieHeaderBinding
import com.example.movieapp.databinding.OneMovieItemExtraInfoBinding
import com.example.movieapp.databinding.OneMovieItemMainBinding
import com.example.movieapp.databinding.OneMovieItemRelateBinding
import java.lang.reflect.Method


class OneMovieAdapter(private val context: Context, private val oneMovieInfo:OneMovieInfo,private val tmdbService:TMDBRetrofitService): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_HEADER = 0
    val TYPE_ITEM_MAIN = 1
    val TYPE_ITEM_RELATE = 2
    val TYPE_ITEM_EXTRA_INFO = 3
    var pageNum = 1
    inner class HeaderViewHolder(private val binding: OneMovieHeaderBinding):RecyclerView.ViewHolder(binding.root){

    }

    inner class ItemMainViewHolder(private val binding: OneMovieItemMainBinding):RecyclerView.ViewHolder(binding.root){
        fun setData(oneMovieInfo: OneMovieInfo) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.setColorSchemeColors(Color.parseColor("#ff7f00"))
            circularProgressDrawable.strokeWidth = 6f
            circularProgressDrawable.centerRadius = 40f
            circularProgressDrawable.start()

            Glide.with(context)
                .load(TMDBRetrofitClient.imageUrl+oneMovieInfo.oneMovie.poster_path)
                .placeholder(circularProgressDrawable)
                .into(binding.posterImg)
            binding.posterImg.clipToOutline = true

            binding.title.text = oneMovieInfo.oneMovie.title
            binding.releaseDate.text = oneMovieInfo.oneMovie.release_date
            binding.timeLength.text = oneMovieInfo.oneMovie.runtime.toString()+"분"
            binding.rate.text = oneMovieInfo.oneMovie.vote_average.toString()+" ★"

            val overview = SpannableStringBuilder(oneMovieInfo.oneMovie.overview)
            overview.setSpan(ForegroundColorSpan(Color.parseColor("#9c9fa6")),0,oneMovieInfo.oneMovie.overview.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE )
            var castsText = ""
            val castEnd = (oneMovieInfo.credits.cast.size - 1).coerceAtMost(2)
            for(i in 0..castEnd){
                castsText += oneMovieInfo.credits.cast[i].name
                if(i != castEnd){
                    castsText += " · "
                }
            }
            val cast = SpannableStringBuilder("출연 "+castsText)
            cast.setSpan(ForegroundColorSpan(Color.WHITE),0,2,SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
            cast.setSpan(ForegroundColorSpan(Color.parseColor("#9c9fa6")),2,cast.length,SpannableString.SPAN_INCLUSIVE_INCLUSIVE)

            var directorText = "없음"
            for(crew in oneMovieInfo.credits.crew){
                if(crew.job == "Director"){
                    directorText = crew.name
                    break
                }
            }

            val director = SpannableStringBuilder("감독 "+directorText)
            director.setSpan(ForegroundColorSpan(Color.WHITE),0,2,SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
            director.setSpan(ForegroundColorSpan(Color.parseColor("#9c9fa6")),2,director.length,SpannableString.SPAN_INCLUSIVE_INCLUSIVE)

            var writerText = "없음"
            for(crew in oneMovieInfo.credits.crew){
                if(crew.job == "Writer"){
                    writerText = crew.name
                    break
                }
            }

            val writer = SpannableStringBuilder("각본 "+writerText)
            writer.setSpan(ForegroundColorSpan(Color.WHITE),0,2,SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
            writer.setSpan(ForegroundColorSpan(Color.parseColor("#9c9fa6")),2,writer.length,SpannableString.SPAN_INCLUSIVE_INCLUSIVE)

            var producerText = "없음"
            for(crew in oneMovieInfo.credits.crew){
                if(crew.job == "Producer"){
                    producerText = crew.name
                    break
                }
            }

            val producer = SpannableStringBuilder("제작 "+producerText)
            producer.setSpan(ForegroundColorSpan(Color.WHITE),0,2,SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
            producer.setSpan(ForegroundColorSpan(Color.parseColor("#9c9fa6")),2,producer.length,SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
            binding.overviewTv.text = TextUtils.concat(overview,"\n\n",cast,"\n\n",director,"\n\n",writer,"\n\n",producer,"\n")//간략히를 개행하기 위해서 반드시 마지막에 개행문자 넣기
            binding.overviewTv.setTrimCollapsedText("더보기")
            binding.overviewTv.setTrimExpandedText("간략히")


        }

    }
    inner class ItemRelateViewHolder(private val binding: OneMovieItemRelateBinding):RecyclerView.ViewHolder(binding.root){
        val rv = binding.rv
        fun setData(title:String) {
            binding.relateTitle.text = title
        }
    }
    inner class ItemExtraInfoViewHolder(private val binding: OneMovieItemExtraInfoBinding):RecyclerView.ViewHolder(binding.root){
        fun setUp(){
            val available = SpannableStringBuilder("지원되는 기기에서 콘텐츠를 시청할 수 있습니다")
            available.setSpan(ForegroundColorSpan(Color.WHITE),0,7,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            available.setSpan(ColoredUnderlineSpan(Color.WHITE),0,7,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            available.setSpan(ForegroundColorSpan(Color.parseColor("#9c9fa6")),7,available.length,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            binding.avaiableDetail.text = available

            val refund = SpannableStringBuilder("환불에는 Google Play 환불 정책이 적용됩니다.")
            refund.setSpan(ForegroundColorSpan(Color.parseColor("#9c9fa6")),0,5,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            refund.setSpan(ForegroundColorSpan(Color.WHITE),5,22,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            refund.setSpan(ColoredUnderlineSpan(Color.WHITE),5,22,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            refund.setSpan(ForegroundColorSpan(Color.parseColor("#9c9fa6")),22,refund.length,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            binding.refundDetail.text = refund
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0->TYPE_HEADER
            1->TYPE_ITEM_MAIN
            2->TYPE_ITEM_RELATE
            3->TYPE_ITEM_EXTRA_INFO
            else->TYPE_HEADER
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var holder:RecyclerView.ViewHolder? = null
        if(viewType == TYPE_HEADER){
            val bindind = OneMovieHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            holder = HeaderViewHolder(bindind)
        }else if(viewType == TYPE_ITEM_MAIN){
            val bindind = OneMovieItemMainBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            holder = ItemMainViewHolder(bindind)
        }else if(viewType == TYPE_ITEM_RELATE){
            val bindind = OneMovieItemRelateBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            holder = ItemRelateViewHolder(bindind)
        }else if(viewType == TYPE_ITEM_EXTRA_INFO){
            val bindind = OneMovieItemExtraInfoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            holder = ItemExtraInfoViewHolder(bindind)
        }

        return holder!!
    }

    override fun getItemCount(): Int = 4

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HeaderViewHolder){
            val headerViewHolder:HeaderViewHolder = holder
        }else if(holder is ItemMainViewHolder){
            val itemMainViewHolder:ItemMainViewHolder = holder
            itemMainViewHolder.setData(oneMovieInfo)
        }else if(holder is ItemRelateViewHolder){
            val itemRelateViewHolder:ItemRelateViewHolder = holder
            val rv:RecyclerView = itemRelateViewHolder.rv
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            val horizontalAdapter = OneMovieHorizontalAdapter(context)
            rv.adapter = horizontalAdapter
            horizontalAdapter.setList(oneMovieInfo.firstRelate)
            horizontalAdapter.deleteLoading()

            itemRelateViewHolder.setData(oneMovieInfo.oneMovie.title+"과(와) 비슷한 영화")
        }else if(holder is ItemExtraInfoViewHolder){
            val itemExtraInfoViewHolder:ItemExtraInfoViewHolder = holder
            itemExtraInfoViewHolder.setUp()
        }
    }

    internal class ColoredUnderlineSpan(private val mColor: Int) : CharacterStyle(),
        UpdateAppearance {
        override fun updateDrawState(tp: TextPaint) {
            try {
                val method: Method = TextPaint::class.java.getMethod(
                    "setUnderlineText",
                    Integer.TYPE,
                    java.lang.Float.TYPE
                )
                method.invoke(tp, mColor, 8.0f)
            } catch (e: Exception) {
                tp.isUnderlineText = true
            }
        }

    }
}