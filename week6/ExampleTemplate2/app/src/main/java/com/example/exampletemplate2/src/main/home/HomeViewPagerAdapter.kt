package com.example.exampletemplate2.src.main.home

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.exampletemplate2.src.main.home.pager_home.PagerHomeFragment
import com.example.exampletemplate2.src.main.home.pager_popular.PagerPopularFragment

class HomeViewPagerAdapter(fa: FragmentActivity):FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->PagerHomeFragment()
            1->PagerPopularFragment()
            else->PagerHomeFragment()
        }
    }
}