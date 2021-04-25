package com.example.naverwebtoon

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.naverwebtoon.databinding.ActivityLoginListBinding

class LoginList : AppCompatActivity() {

    private lateinit var binding: ActivityLoginListBinding

    var loginList = ArrayList<LoginInfo>()

    private lateinit var loginListAdapter:LoginListAdapter

    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginList.add(LoginInfo("akak4456"))
        loginList.add(LoginInfo("joseunghyo123"))
        //일단 임시 저장. 나중에 바꿀 예정

        context = this

        loginListAdapter = LoginListAdapter(context,loginList)

        binding.lvLoginList.adapter = loginListAdapter

        binding.loginOtherBtn.setOnClickListener{
            val intent = Intent(context,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}