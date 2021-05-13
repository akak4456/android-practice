package com.example.movieapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapp.databinding.ActivityWatchlistBinding

class WatchlistActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWatchlistBinding
    private lateinit var context: Context
    private lateinit var adapter:WatchlistAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this

        adapter = WatchlistAdapter(context)
        binding
    }
}