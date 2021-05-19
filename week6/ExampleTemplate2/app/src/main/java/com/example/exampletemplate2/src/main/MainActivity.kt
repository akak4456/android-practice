package com.example.exampletemplate2.src.main

import android.os.Bundle
import android.view.MenuItem
import com.example.exampletemplate2.R
import com.example.exampletemplate2.config.BaseActivity
import com.example.exampletemplate2.databinding.ActivityMainBinding
import com.example.exampletemplate2.src.main.chat.ChatFragment
import com.example.exampletemplate2.src.main.home.HomeFragment
import com.example.exampletemplate2.src.main.subscription.SubscriptionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainBottomNavigationView.setOnNavigationItemSelectedListener (this)

        supportFragmentManager.beginTransaction().add(R.id.main_frame,HomeFragment()).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.page_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frame,HomeFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.page_subscription ->{
                supportFragmentManager.beginTransaction().replace(R.id.main_frame,SubscriptionFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.page_chat->{
                supportFragmentManager.beginTransaction().replace(R.id.main_frame,ChatFragment()).commitAllowingStateLoss()
                return true
            }
            else->{
                return false
            }
        }
    }
}
