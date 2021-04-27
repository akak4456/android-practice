package com.example.naverwebtoon

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.naverwebtoon.databinding.ActivityLoginBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder

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

        binding.loginBtn.setOnClickListener{
            val input_id = binding.inputId.text.toString()
            val spId:SharedPreferences = getSharedPreferences("sharedId", Context.MODE_PRIVATE)
            val editorId:SharedPreferences.Editor = spId.edit()
            val sp:SharedPreferences = getSharedPreferences("sharedWebtoon", Context.MODE_PRIVATE)
            val editor:SharedPreferences.Editor = sp.edit()
            if(!spId.contains(input_id)){
                var lastNumber = 1
                val gson: Gson = GsonBuilder().create()
                for(i in 1..3){
                    var up = false
                    if(i == 1)
                        up = true
                    var strMyInfo = gson.toJson(MyInfo(lastNumber,R.drawable.mon_webtoon_1,getString(R.string.mon_webtoon_1_title),false,up))
                    editor.putString(input_id+"_"+lastNumber,strMyInfo)
                    lastNumber++
                    strMyInfo = gson.toJson(MyInfo(lastNumber,R.drawable.mon_webtoon_2,getString(R.string.mon_webtoon_2_title),true,up))
                    editor.putString(input_id+"_"+lastNumber,strMyInfo)
                    lastNumber++
                    strMyInfo = gson.toJson(MyInfo(lastNumber,R.drawable.mon_webtoon_3,getString(R.string.mon_webtoon_3_title),false,up))
                    editor.putString(input_id+"_"+lastNumber,strMyInfo)
                    lastNumber++
                    strMyInfo = gson.toJson(MyInfo(lastNumber,R.drawable.mon_webtoon_4,getString(R.string.mon_webtoon_4_title),true,up))
                    editor.putString(input_id+"_"+lastNumber,strMyInfo)
                    lastNumber++
                    strMyInfo = gson.toJson(MyInfo(lastNumber,R.drawable.mon_webtoon_5,getString(R.string.mon_webtoon_5_title),false,up))
                    editor.putString(input_id+"_"+lastNumber,strMyInfo)
                    lastNumber++
                }
                editor.commit()
                editorId.putInt(input_id,1)
                editorId.commit()
            }
            finish()
        }
    }
}