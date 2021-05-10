package com.example.movieapp

import androidx.recyclerview.widget.RecyclerView

data class MainInfo(
    val mainTitle:String,
    val subTitle:String,
    var results:MutableList<Result>
)