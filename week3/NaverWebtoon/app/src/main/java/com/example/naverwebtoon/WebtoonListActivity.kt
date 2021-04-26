package com.example.naverwebtoon

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.naverwebtoon.databinding.ActivityWebtoonListBinding
import com.google.android.material.tabs.TabLayoutMediator


class WebtoonListActivity : AppCompatActivity() {

    private lateinit var binding:ActivityWebtoonListBinding

    private val tabTextList = arrayListOf("월","화","수","목","금","토","일")

    private val rowCnt:ArrayList<Int> = arrayListOf(7,5,9,5,4,10,12)

    private var initialHeight:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebtoonListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPagerAd.adapter = ViewPagerAdAdapter(this)
        binding.viewPagerWebtoon.adapter = ViewPagerWebtoonAdapter(this)
        binding.viewPagerWebtoon.offscreenPageLimit = 7

        binding.viewPagerWebtoon.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(initialHeight == 0)
                    initialHeight = binding.viewPagerWebtoon.findViewById<GridLayout>(R.id.grid_main).height / rowCnt[0]
                binding.viewPagerWebtoon.layoutParams.height = initialHeight* rowCnt[position]
                binding.viewPagerWebtoon.requestLayout()
            }

        })
        TabLayoutMediator(binding.tabLayout,binding.viewPagerWebtoon){tab,position->tab.text=tabTextList[position]}.attach()
    }

    override fun onResume() {
        super.onResume()

    }
}