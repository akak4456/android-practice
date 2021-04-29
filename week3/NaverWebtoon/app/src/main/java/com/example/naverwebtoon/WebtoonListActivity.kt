package com.example.naverwebtoon

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.example.naverwebtoon.databinding.ActivityWebtoonListBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*
import kotlin.concurrent.timer


class WebtoonListActivity : AppCompatActivity() {

    private lateinit var binding:ActivityWebtoonListBinding

    private val tabTextList = arrayListOf("월","화","수","목","금","토","일")

    private lateinit var accessId:String


    private var isToolbarShow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebtoonListBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.viewPagerAd.adapter = ViewPagerAdAdapter(this)

        binding.viewPagerWebtoon.offscreenPageLimit = 1

        var originalPos = 987654321
        binding.viewPagerWebtoon.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(isToolbarShow){
                    showViews()
                }else{
                    hideViews()
                }
                if(originalPos == 987654321)
                    originalPos = binding.nestedWebtoon.y.toInt()
                else
                    binding.nestedWebtoon.y = originalPos.toFloat()
                binding.ll.y = 0f
                binding.nestedWebtoon.scrollTo(0,0)



                Log.d("TMP",binding.viewPagerWebtoon.findViewById<RecyclerView>(R.id.webtoon_rv).height.toString())
                binding.nestedWebtoon.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                    Log.d("TMP",binding.ll.y.toString())
                    if (scrollY > oldScrollY) {
                        if(pxToDp((binding.ll.y + (oldScrollY - scrollY)).toInt()) > -196) {
                            binding.ll.y = binding.ll.y + (oldScrollY - scrollY).toFloat()
                            binding.nestedWebtoon.y = binding.nestedWebtoon.y + (oldScrollY - scrollY).toFloat()
                        }else {
                            binding.nestedWebtoon.y = binding.nestedWebtoon.y + (dpToPx(-196)-binding.ll.y.toInt())
                            binding.ll.y = dpToPx(-196).toFloat()
                        }
                    }
                    if (scrollY < oldScrollY) {
                        if (pxToDp((binding.ll.y + (oldScrollY - scrollY)).toInt()) < 0) {
                            binding.ll.y = binding.ll.y + (oldScrollY - scrollY).toFloat()
                            binding.nestedWebtoon.y = binding.nestedWebtoon.y + (oldScrollY - scrollY).toFloat()
                        } else {
                            binding.nestedWebtoon.y = originalPos.toFloat()
                            binding.ll.y = dpToPx(0).toFloat()
                        }
                    }
                    if (scrollY == 0) {
                        hideViews()
                    }else{
                        showViews()
                    }
                    if (scrollY == v.measuredHeight - v.getChildAt(0)
                            .measuredHeight
                    ) {
                        Log.i("FragmentActivity.TAG", "BOTTOM SCROLL")
                    }
                })
            }
        })

        binding.bottomAppbar.goToMyBtn.setOnClickListener{
            val intent = Intent(this,MyActivity::class.java)
            startActivity(intent)
            finish()
        }
        runOnUiThread { val span = SpannableString(String.format("%d / 5",(binding.viewPagerAd.currentItem)%5+1))
            span.setSpan(ForegroundColorSpan(Color.WHITE),0,1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            span.setSpan(ForegroundColorSpan(Color.parseColor("#746e6e")),2,span.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.tvAdCnt.text = span }
        val timer = timer(period=5000,initialDelay = 5000){
            binding.viewPagerAd.currentItem = binding.viewPagerAd.currentItem + 1
        }
        binding.viewPagerAd.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                runOnUiThread { val span = SpannableString(String.format("%d / 5",(binding.viewPagerAd.currentItem)%5+1))
                    span.setSpan(ForegroundColorSpan(Color.WHITE),0,1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    span.setSpan(ForegroundColorSpan(Color.parseColor("#746e6e")),2,span.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.tvAdCnt.text = span }
            }
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val sp:SharedPreferences = getSharedPreferences("sharedId", Context.MODE_PRIVATE)
        val spWebtoon:SharedPreferences = getSharedPreferences("sharedWebtoon",Context.MODE_PRIVATE)

        if(sp.contains("alreadyId")){
            accessId = sp.getString("alreadyId","none").toString()
        }else{
            accessId = "none"
            sp.edit().putString("alreadyId",accessId)
        }
        binding.viewPagerWebtoon.adapter = ViewPagerWebtoonAdapter(this,accessId,sp,spWebtoon)
        TabLayoutMediator(binding.tabLayout,binding.viewPagerWebtoon){tab,position->tab.text=tabTextList[position]}.attach()
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus){
            binding.toolbar.y = (-binding.toolbar.height).toFloat()
            binding.belowAd.y = binding.belowAd.height.toFloat()

            Log.d("TMP",Calendar.DAY_OF_WEEK.toString())

            when(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
                Calendar.MONDAY->binding.viewPagerWebtoon.currentItem=0
                Calendar.TUESDAY->binding.viewPagerWebtoon.currentItem=1
                Calendar.WEDNESDAY->binding.viewPagerWebtoon.currentItem=2
                Calendar.THURSDAY->binding.viewPagerWebtoon.currentItem=3
                Calendar.FRIDAY->binding.viewPagerWebtoon.currentItem=4
                Calendar.SATURDAY->binding.viewPagerWebtoon.currentItem=5
                Calendar.SUNDAY->binding.viewPagerWebtoon.currentItem=6
                else->binding.viewPagerWebtoon.currentItem=0
            }
        }
    }


    private fun hideViews() {
        if(isToolbarShow) {
            binding.toolbar.animate().translationY((-binding.toolbar.height).toFloat()).interpolator = AccelerateInterpolator(2F)
            binding.belowAd.animate().translationY((binding.belowAd.height).toFloat()).interpolator = AccelerateInterpolator(2F)
            isToolbarShow = false
        }
    }

    private fun showViews() {
        if(!isToolbarShow) {
            binding.toolbar.animate().translationY(0F).interpolator = DecelerateInterpolator(2F)
            binding.belowAd.animate().translationY(0F).interpolator = DecelerateInterpolator(2F)
            isToolbarShow = true
        }
    }

    fun pxToDp(px: Int): Int {
        val displayMetrics: DisplayMetrics = this.resources.displayMetrics
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun dpToPx(dp: Int): Int {
        val displayMetrics: DisplayMetrics = this.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}