package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    private lateinit var mainAdapter: MainAdapter

    private val mainTitleList: Array<String> = arrayOf("최신영화 전용관","애니메이션 판타지 영화","액션/어드벤처")
    private val subTitleList:Array<String> = arrayOf("따끈한 신작 영화들을 즐기는 시간","","액션과 모험의 세계로 떠납시다!")
    /*
    주의 main과 sub는 1:1대응이 되어야 함
    ex) 최신영화 전용관 은 따끈한 신작 영화들을 즐기는 시간와 같이
    index가 대응이 되어야 함
     */

    private val mainInfoList:ArrayList<MainInfo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for(idx in mainTitleList.indices){
            mainInfoList.add(MainInfo(mainTitleList[idx],subTitleList[idx]))
        }

        binding.mainRv.layoutManager = LinearLayoutManager(this)
        mainAdapter = MainAdapter(this,mainInfoList)
        binding.mainRv.adapter = mainAdapter
    }
}