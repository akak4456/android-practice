package com.example.naverwebtoon

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.random.Random

class ViewPagerWebtoonAdapter(private val fragmentAdcitivy:FragmentActivity,private val accessId:String,private val sp:SharedPreferences,private val spWebtoon:SharedPreferences) : FragmentStateAdapter(fragmentAdcitivy) {

    companion object{
        private const val NUM_PAGES = 7
    }

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        val monWebtoonList = ArrayList<WebtoonInfo>()
        val tueWebtoonList = ArrayList<WebtoonInfo>()
        val wedWebtoonList = ArrayList<WebtoonInfo>()
        val thuWebtoonList = ArrayList<WebtoonInfo>()
        val friWebtoonList = ArrayList<WebtoonInfo>()
        val satWebtoonList = ArrayList<WebtoonInfo>()
        val sunWebtoonList = ArrayList<WebtoonInfo>()
        var isUp = false
        for(i in 1..4){
            monWebtoonList.add(WebtoonInfo(R.drawable.mon_webtoon_1,fragmentAdcitivy.getString(R.string.mon_webtoon_1_title),fragmentAdcitivy.getString(R.string.mon_webtoon_1_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            monWebtoonList.add(WebtoonInfo(R.drawable.mon_webtoon_2,fragmentAdcitivy.getString(R.string.mon_webtoon_2_title),fragmentAdcitivy.getString(R.string.mon_webtoon_2_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            monWebtoonList.add(WebtoonInfo(R.drawable.mon_webtoon_3,fragmentAdcitivy.getString(R.string.mon_webtoon_3_title),fragmentAdcitivy.getString(R.string.mon_webtoon_3_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            monWebtoonList.add(WebtoonInfo(R.drawable.mon_webtoon_4,fragmentAdcitivy.getString(R.string.mon_webtoon_4_title),fragmentAdcitivy.getString(R.string.mon_webtoon_4_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            monWebtoonList.add(WebtoonInfo(R.drawable.mon_webtoon_5,fragmentAdcitivy.getString(R.string.mon_webtoon_5_title),fragmentAdcitivy.getString(R.string.mon_webtoon_5_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
        }
        if(monWebtoonList.size%3 != 0) {
            val remainCnt = 3 - monWebtoonList.size % 3
            for (i in 1..remainCnt) {
                monWebtoonList.add(WebtoonInfo(R.drawable.blank_webtoon, "", "", "", false))
            }
        }

        isUp = false
        for(i in 1..4){
            tueWebtoonList.add(WebtoonInfo(R.drawable.tue_webtoon_1,fragmentAdcitivy.getString(R.string.tue_webtoon_1_title),fragmentAdcitivy.getString(R.string.tue_webtoon_1_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            tueWebtoonList.add(WebtoonInfo(R.drawable.tue_webtoon_2,fragmentAdcitivy.getString(R.string.tue_webtoon_2_title),fragmentAdcitivy.getString(R.string.tue_webtoon_2_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            tueWebtoonList.add(WebtoonInfo(R.drawable.tue_webtoon_3,fragmentAdcitivy.getString(R.string.tue_webtoon_3_title),fragmentAdcitivy.getString(R.string.tue_webtoon_3_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            tueWebtoonList.add(WebtoonInfo(R.drawable.tue_webtoon_4,fragmentAdcitivy.getString(R.string.tue_webtoon_4_title),fragmentAdcitivy.getString(R.string.tue_webtoon_4_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            tueWebtoonList.add(WebtoonInfo(R.drawable.tue_webtoon_5,fragmentAdcitivy.getString(R.string.tue_webtoon_5_title),fragmentAdcitivy.getString(R.string.tue_webtoon_5_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
        }
        if(tueWebtoonList.size%3 != 0) {
            val remainCnt = 3 - tueWebtoonList.size % 3
            for (i in 1..remainCnt) {
                tueWebtoonList.add(WebtoonInfo(R.drawable.blank_webtoon, "", "", "", false))
            }
        }
        isUp = false
        for(i in 1..4){
            wedWebtoonList.add(WebtoonInfo(R.drawable.wed_webtoon_1,fragmentAdcitivy.getString(R.string.wed_webtoon_1_title),fragmentAdcitivy.getString(R.string.wed_webtoon_1_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            wedWebtoonList.add(WebtoonInfo(R.drawable.wed_webtoon_2,fragmentAdcitivy.getString(R.string.wed_webtoon_2_title),fragmentAdcitivy.getString(R.string.wed_webtoon_2_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            wedWebtoonList.add(WebtoonInfo(R.drawable.wed_webtoon_3,fragmentAdcitivy.getString(R.string.wed_webtoon_3_title),fragmentAdcitivy.getString(R.string.wed_webtoon_3_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            wedWebtoonList.add(WebtoonInfo(R.drawable.wed_webtoon_4,fragmentAdcitivy.getString(R.string.wed_webtoon_4_title),fragmentAdcitivy.getString(R.string.wed_webtoon_4_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            wedWebtoonList.add(WebtoonInfo(R.drawable.wed_webtoon_5,fragmentAdcitivy.getString(R.string.wed_webtoon_5_title),fragmentAdcitivy.getString(R.string.wed_webtoon_5_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
        }
        if(wedWebtoonList.size%3 != 0) {
            val remainCnt = 3 - wedWebtoonList.size % 3
            for (i in 1..remainCnt) {
                wedWebtoonList.add(WebtoonInfo(R.drawable.blank_webtoon, "", "", "", false))
            }
        }
        isUp = false
        for(i in 1..4){
            thuWebtoonList.add(WebtoonInfo(R.drawable.thu_webtoon_1,fragmentAdcitivy.getString(R.string.thu_webtoon_1_title),fragmentAdcitivy.getString(R.string.thu_webtoon_1_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            thuWebtoonList.add(WebtoonInfo(R.drawable.thu_webtoon_2,fragmentAdcitivy.getString(R.string.thu_webtoon_2_title),fragmentAdcitivy.getString(R.string.thu_webtoon_2_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            thuWebtoonList.add(WebtoonInfo(R.drawable.thu_webtoon_3,fragmentAdcitivy.getString(R.string.thu_webtoon_3_title),fragmentAdcitivy.getString(R.string.thu_webtoon_3_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            thuWebtoonList.add(WebtoonInfo(R.drawable.thu_webtoon_4,fragmentAdcitivy.getString(R.string.thu_webtoon_4_title),fragmentAdcitivy.getString(R.string.thu_webtoon_4_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            thuWebtoonList.add(WebtoonInfo(R.drawable.thu_webtoon_5,fragmentAdcitivy.getString(R.string.thu_webtoon_5_title),fragmentAdcitivy.getString(R.string.thu_webtoon_5_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
        }
        if(thuWebtoonList.size%3 != 0) {
            val remainCnt = 3 - thuWebtoonList.size % 3
            for (i in 1..remainCnt) {
                thuWebtoonList.add(WebtoonInfo(R.drawable.blank_webtoon, "", "", "", false))
            }
        }
        isUp = false
        for(i in 1..4){
            friWebtoonList.add(WebtoonInfo(R.drawable.fri_webtoon_1,fragmentAdcitivy.getString(R.string.fri_webtoon_1_title),fragmentAdcitivy.getString(R.string.fri_webtoon_1_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            friWebtoonList.add(WebtoonInfo(R.drawable.fri_webtoon_2,fragmentAdcitivy.getString(R.string.fri_webtoon_2_title),fragmentAdcitivy.getString(R.string.fri_webtoon_2_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            friWebtoonList.add(WebtoonInfo(R.drawable.fri_webtoon_3,fragmentAdcitivy.getString(R.string.fri_webtoon_3_title),fragmentAdcitivy.getString(R.string.fri_webtoon_3_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            friWebtoonList.add(WebtoonInfo(R.drawable.fri_webtoon_4,fragmentAdcitivy.getString(R.string.fri_webtoon_4_title),fragmentAdcitivy.getString(R.string.fri_webtoon_4_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            friWebtoonList.add(WebtoonInfo(R.drawable.fri_webtoon_5,fragmentAdcitivy.getString(R.string.fri_webtoon_5_title),fragmentAdcitivy.getString(R.string.fri_webtoon_5_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
        }
        if(friWebtoonList.size%3 != 0) {
            val remainCnt = 3 - friWebtoonList.size % 3
            for (i in 1..remainCnt) {
                friWebtoonList.add(WebtoonInfo(R.drawable.blank_webtoon, "", "", "", false))
            }
        }
        isUp = false
        for(i in 1..4){
            satWebtoonList.add(WebtoonInfo(R.drawable.sat_webtoon_1,fragmentAdcitivy.getString(R.string.sat_webtoon_1_title),fragmentAdcitivy.getString(R.string.sat_webtoon_1_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            satWebtoonList.add(WebtoonInfo(R.drawable.sat_webtoon_2,fragmentAdcitivy.getString(R.string.sat_webtoon_2_title),fragmentAdcitivy.getString(R.string.sat_webtoon_2_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            satWebtoonList.add(WebtoonInfo(R.drawable.sat_webtoon_3,fragmentAdcitivy.getString(R.string.sat_webtoon_3_title),fragmentAdcitivy.getString(R.string.sat_webtoon_3_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            satWebtoonList.add(WebtoonInfo(R.drawable.sat_webtoon_4,fragmentAdcitivy.getString(R.string.sat_webtoon_4_title),fragmentAdcitivy.getString(R.string.sat_webtoon_4_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            satWebtoonList.add(WebtoonInfo(R.drawable.sat_webtoon_5,fragmentAdcitivy.getString(R.string.sat_webtoon_5_title),fragmentAdcitivy.getString(R.string.sat_webtoon_5_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
        }
        if(satWebtoonList.size%3 != 0) {
            val remainCnt = 3 - satWebtoonList.size % 3
            for (i in 1..remainCnt) {
                satWebtoonList.add(WebtoonInfo(R.drawable.blank_webtoon, "", "", "", false))
            }
        }
        isUp = false
        for(i in 1..4){
            sunWebtoonList.add(WebtoonInfo(R.drawable.sun_webtoon_1,fragmentAdcitivy.getString(R.string.sun_webtoon_1_title),fragmentAdcitivy.getString(R.string.sun_webtoon_1_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            sunWebtoonList.add(WebtoonInfo(R.drawable.sun_webtoon_2,fragmentAdcitivy.getString(R.string.sun_webtoon_1_title),fragmentAdcitivy.getString(R.string.sun_webtoon_2_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            sunWebtoonList.add(WebtoonInfo(R.drawable.sun_webtoon_3,fragmentAdcitivy.getString(R.string.sun_webtoon_1_title),fragmentAdcitivy.getString(R.string.sun_webtoon_3_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            sunWebtoonList.add(WebtoonInfo(R.drawable.sun_webtoon_4,fragmentAdcitivy.getString(R.string.sun_webtoon_1_title),fragmentAdcitivy.getString(R.string.sun_webtoon_4_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
            sunWebtoonList.add(WebtoonInfo(R.drawable.sun_webtoon_5,fragmentAdcitivy.getString(R.string.sun_webtoon_1_title),fragmentAdcitivy.getString(R.string.sun_webtoon_5_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f),isUp))
        }
        if(sunWebtoonList.size%3 != 0) {
            val remainCnt = 3 - sunWebtoonList.size % 3
            for (i in 1..remainCnt) {
                sunWebtoonList.add(WebtoonInfo(R.drawable.blank_webtoon, "", "", "", false))
            }
        }
        return when(position){
            0->FragmentWebtoon(monWebtoonList,accessId,sp,spWebtoon)
            1->FragmentWebtoon(tueWebtoonList,accessId,sp,spWebtoon)
            2->FragmentWebtoon(wedWebtoonList,accessId,sp,spWebtoon)
            3->FragmentWebtoon(thuWebtoonList,accessId,sp,spWebtoon)
            4->FragmentWebtoon(friWebtoonList,accessId,sp,spWebtoon)
            5->FragmentWebtoon(satWebtoonList,accessId,sp,spWebtoon)
            6->FragmentWebtoon(sunWebtoonList,accessId,sp,spWebtoon)
            else -> FragmentWebtoon(monWebtoonList,accessId,sp,spWebtoon)
        }
    }
}