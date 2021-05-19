package com.example.exampletemplate2.src.main.home.pager_home

import com.example.exampletemplate2.src.domain.movies.Movies

interface PagerHomeView {
    fun onGetMovieSuccess(response: Movies)
}