package com.example.movieapp2.src.main.home

import android.os.Bundle
import android.view.View
import com.example.movieapp2.R
import com.example.movieapp2.config.BaseFragment
import com.example.movieapp2.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}