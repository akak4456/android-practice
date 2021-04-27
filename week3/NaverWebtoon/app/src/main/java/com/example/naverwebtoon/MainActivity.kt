package com.example.naverwebtoon

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.naverwebtoon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.loginBtn.setOnClickListener{
            val intent = Intent(this,LoginList::class.java)
            startActivity(intent)
        }

        binding.webtoonListBtn.setOnClickListener{
            val intent = Intent(this,WebtoonListActivity::class.java)
            startActivity(intent)
        }
        binding.myBtn.setOnClickListener{
            val intent = Intent(this,MyActivity::class.java)
            startActivity(intent)
        }
        binding.clearAllBtn.setOnClickListener{
            val spId: SharedPreferences = getSharedPreferences("sharedId", Context.MODE_PRIVATE)
            val editorId: SharedPreferences.Editor = spId.edit()
            val sp: SharedPreferences = getSharedPreferences("sharedWebtoon", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sp.edit()

            editorId.clear().commit()
            editor.clear().commit()
        }

    }
}