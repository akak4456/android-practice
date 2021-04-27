package com.example.naverwebtoon

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerWebtoonAdapter(fragmentAdcitivy:FragmentActivity,private val accessId:String,private val sp:SharedPreferences,private val spWebtoon:SharedPreferences) : FragmentStateAdapter(fragmentAdcitivy) {

    companion object{
        private const val NUM_PAGES = 7
    }

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->FragmentMonWebtoon(accessId,sp,spWebtoon)
            1->FragmentTueWebtoon(accessId,sp,spWebtoon)
            2->FragmentWedWebtoon(accessId,sp,spWebtoon)
            3->FragmentThuWebtoon(accessId,sp,spWebtoon)
            4->FragmentFriWebtoon(accessId,sp,spWebtoon)
            5->FragmentSatWebtoon(accessId,sp,spWebtoon)
            6->FragmentSunWebtoon(accessId,sp,spWebtoon)
            else -> FragmentMonWebtoon(accessId,sp,spWebtoon)
        }
    }
}