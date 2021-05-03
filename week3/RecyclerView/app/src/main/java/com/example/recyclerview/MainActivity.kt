package com.example.recyclerview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataSet:ArrayList<ItemInfo> = ArrayList()

        for(i in 1..10){
            dataSet.add(ItemInfo("titleA","authorA"))
            dataSet.add(ItemInfo("titleB","authorB"))
            dataSet.add(ItemInfo("titleC","authorC"))
        }

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = ItemAdapter(this,dataSet)

        dataSet.add(ItemInfo("titleD","authorD"))

        binding.rv.scrollTo(0,2000)
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d("TMP",(binding.rv.computeVerticalScrollOffset()*1.0f/binding.rv.computeVerticalScrollRange()).toString())
                //추측하기로 이걸로 recycler view의 스크롤 변화 정도를 감지할 수 있지 않을까?
                //dy	int: The amount of vertical scroll.
            }
        })
    }
}