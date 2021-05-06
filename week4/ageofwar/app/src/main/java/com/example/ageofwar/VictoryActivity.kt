package com.example.ageofwar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ageofwar.databinding.ActivityVictoryBinding

class VictoryActivity : AppCompatActivity() {

    private lateinit var binding:ActivityVictoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVictoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRestart.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}