package com.example.naverwebtoon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.naverwebtoon.databinding.ListviewLoginlistItemBinding
import com.example.naverwebtoon.databinding.WebtoonItemBinding

class WebtoonAdapter(private var context: Context, private val webtoonList:ArrayList<WebtoonInfo>) : BaseAdapter() {

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    lateinit var binding:WebtoonItemBinding

    override fun getCount(): Int = webtoonList.size

    override fun getItem(position: Int): Any = webtoonList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        binding = WebtoonItemBinding.inflate(inflater,parent,false)
        binding.webtoonImg.setImageDrawable(context.getDrawable(webtoonList[position].img))
        binding.webtoonTitle.text = webtoonList[position].title
        binding.webtoonAuthor.text = webtoonList[position].author
        binding.webtoonStar.text = webtoonList[position].star
        return binding.root
    }
}