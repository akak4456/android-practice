package com.example.naverwebtoon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.naverwebtoon.databinding.ActivityWebtoonListBinding
import com.google.android.material.tabs.TabLayoutMediator

class WebtoonListActivity : AppCompatActivity() {

    private lateinit var binding:ActivityWebtoonListBinding

    private val tabTextList = arrayListOf("월","화","수","목","금","토","일")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebtoonListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPagerAd.adapter = ViewPagerAdAdapter(this)
        binding.viewPagerWebtoon.adapter = ViewPagerWebtoonAdapter(this)
        TabLayoutMediator(binding.tabLayout,binding.viewPagerWebtoon){tab,position->tab.text=tabTextList[position]}.attach()
    }
}