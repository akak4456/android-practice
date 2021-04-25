package com.example.naverwebtoon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.naverwebtoon.databinding.ListviewLoginlistItemBinding

class LoginListAdapter(context: Context, private val loginList:ArrayList<LoginInfo>) : BaseAdapter() {

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    lateinit var binding:ListviewLoginlistItemBinding

    override fun getCount(): Int = loginList.size

    override fun getItem(position: Int): Any = loginList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        binding = ListviewLoginlistItemBinding.inflate(inflater,parent,false)
        binding.showId.text = loginList[position].id

        return binding.root
    }
}