package com.example.samsungmusicplayer

import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager

class GoToLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle","GoToLogin-onCreate")
        setContentView(R.layout.activity_go_to_login)
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle","GoToLogin-onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle","GoToLogin-onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle","GoToLogin-onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifeCycle","GoToLogin-onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifeCycle","GoToLogin-onDestroy")
    }
}