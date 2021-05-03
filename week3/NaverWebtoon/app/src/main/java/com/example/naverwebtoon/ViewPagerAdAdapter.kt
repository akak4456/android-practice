package com.example.naverwebtoon

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdAdapter(
    fragmentAdcitivy: FragmentActivity
) : FragmentStateAdapter(fragmentAdcitivy) {

    companion object{
        private const val NUM_PAGES = 5
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        if(position.rem(5) == 0){
            return FragmentAd1()
        }else if(position.rem(5) == 1){
            return FragmentAd2()
        }else if(position.rem(5) == 2){
            return FragmentAd3()
        }else if(position.rem(5) == 3){
            return FragmentAd4()
        }else if(position.rem(5) == 4){
            return FragmentAd5()
        }
        return FragmentAd1()
    }
}