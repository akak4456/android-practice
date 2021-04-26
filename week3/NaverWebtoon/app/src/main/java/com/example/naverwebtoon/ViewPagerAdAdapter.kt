package com.example.naverwebtoon

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdAdapter(fragmentAdcitivy:FragmentActivity) : FragmentStateAdapter(fragmentAdcitivy) {

    companion object{
        private const val NUM_PAGES = 5
    }

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        if(position == 0){
            return FragmentAd1()
        }else if(position == 1){
            return FragmentAd2()
        }else if(position == 2){
            return FragmentAd3()
        }else if(position == 3){
            return FragmentAd4()
        }else if(position == 4){
            return FragmentAd5()
        }
        return FragmentAd1()
    }
}