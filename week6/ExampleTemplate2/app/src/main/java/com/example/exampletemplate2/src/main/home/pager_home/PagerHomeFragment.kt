package com.example.exampletemplate2.src.main.home.pager_home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exampletemplate2.R
import com.example.exampletemplate2.config.BaseFragment
import com.example.exampletemplate2.databinding.FragmentPagerHomeBinding
import com.example.exampletemplate2.src.domain.movies.Movies

class PagerHomeFragment:BaseFragment<FragmentPagerHomeBinding>(FragmentPagerHomeBinding::bind, R.layout.fragment_pager_home),PagerHomeView {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pagerHomeRv.layoutManager = LinearLayoutManager(context!!)
        binding.pagerHomeRv.adapter = PagerHomeRVAdapter(context!!)
        PagerHomeService(this).tryGetMovies()
    }

    override fun onGetMovieSuccess(response: Movies) {

    }
}