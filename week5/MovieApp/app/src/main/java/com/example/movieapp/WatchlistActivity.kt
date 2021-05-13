package com.example.movieapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.adapter = adapter

        binding.bottomAppbar.watchlistBtnOffImg.visibility = View.INVISIBLE
        binding.bottomAppbar.watchlistTv.setTextColor(Color.WHITE)

        binding.bottomAppbar.homeLayout.setOnClickListener{
            val intent = Intent(context,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}