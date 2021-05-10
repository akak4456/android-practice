package com.example.movieapp

data class TodayTrend(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)