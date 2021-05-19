package com.example.exampletemplate2.src.main.home

import android.os.Bundle
import android.view.View
import com.example.exampletemplate2.R
import com.example.exampletemplate2.config.BaseFragment
import com.example.exampletemplate2.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy


class HomeFragment:BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home) {
    private val tabTextList = arrayListOf("Home","Popular")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPagerAdapter = HomeViewPagerAdapter(this.activity!!)
        binding.homePager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.homeTabs, binding.homePager,
            TabConfigurationStrategy { tab, position ->
                tab.text = tabTextList[position]
            }
        ).attach()
    }
}