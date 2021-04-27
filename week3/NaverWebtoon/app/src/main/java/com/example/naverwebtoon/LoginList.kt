package com.example.naverwebtoon

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

        context = this
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle","LoginList-onStart")
        val spId:SharedPreferences = getSharedPreferences("sharedId", Context.MODE_PRIVATE)
        val allEntries: Map<String, *> = spId.all
        loginList.clear()
        for ((key, value) in allEntries) {
            if(key != "alreadyId")
            loginList.add(LoginInfo(key))
        }


        loginListAdapter = LoginListAdapter(context,loginList)

        binding.lvLoginList.adapter = loginListAdapter

        binding.lvLoginList.setOnItemClickListener{
            parent,view,position,id->
            spId.edit().putString("alreadyId",loginList[position].id).commit()
            finish()
        }

        binding.loginOtherBtn.setOnClickListener{
            val intent = Intent(context,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}