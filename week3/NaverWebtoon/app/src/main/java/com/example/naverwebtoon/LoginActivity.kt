package com.example.naverwebtoon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.naverwebtoon.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.inputId.setOnFocusChangeListener{
            view,hasFocus->
            if(hasFocus) view.setBackgroundResource(R.drawable.green_edittext)
            else view.setBackgroundResource(R.drawable.gray_edittext)
        }

        binding.inputPw.setOnFocusChangeListener{
                view,hasFocus->
            if(hasFocus) view.setBackgroundResource(R.drawable.green_edittext)
            else view.setBackgroundResource(R.drawable.gray_edittext)
        }
    }
}