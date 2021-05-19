package com.example.exampletemplate2.src.main.home.pager_popular

import android.os.Bundle
import android.view.View
import com.example.exampletemplate2.R
import com.example.exampletemplate2.config.BaseFragment
import com.example.exampletemplate2.databinding.FragmentPagerPopularBinding

class PagerPopularFragment:BaseFragment<FragmentPagerPopularBinding>(FragmentPagerPopularBinding::bind,
    R.layout.fragment_pager_popular) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}