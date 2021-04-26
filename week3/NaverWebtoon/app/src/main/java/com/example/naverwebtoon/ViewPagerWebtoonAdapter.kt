package com.example.naverwebtoon

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerWebtoonAdapter(fragmentAdcitivy:FragmentActivity) : FragmentStateAdapter(fragmentAdcitivy) {

    companion object{
        private const val NUM_PAGES = 7
    }

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->FragmentMonWebtoon()
            1->FragmentTueWebtoon()
            2->FragmentWedWebtoon()
            3->FragmentThuWebtoon()
            4->FragmentFriWebtoon()
            5->FragmentSatWebtoon()
            6->FragmentSunWebtoon()
            else -> FragmentMonWebtoon()
        }
    }
}