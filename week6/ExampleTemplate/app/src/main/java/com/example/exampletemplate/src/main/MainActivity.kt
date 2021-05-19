package com.example.exampletemplate.src.main

import android.os.Bundle
import com.example.exampletemplate.config.BaseActivity
import com.example.exampletemplate.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}