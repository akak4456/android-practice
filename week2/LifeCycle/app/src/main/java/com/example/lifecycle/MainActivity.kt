package com.example.lifecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("TMP","onCreate")
    }

    override fun onStart(){
        super.onStart()
        Log.i("TMP","onStart")
    }

    override fun onResume(){
        super.onResume()
        Log.i("TMP","onResume")
    }

    override fun onPause(){
        super.onPause()
        Log.i("TMP","onPause")
    }

    override fun onStop(){
        super.onStop()
        Log.i("TMP","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TMP","onDestroy")
    }
}