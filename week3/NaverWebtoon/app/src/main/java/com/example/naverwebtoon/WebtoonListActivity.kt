package com.example.naverwebtoon

import android.content.Context
import android.content.Intent
import android.content.ReceiverCallNotAllowedException
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Layout
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.naverwebtoon.databinding.ActivityWebtoonListBinding
import com.google.android.material.tabs.TabLayoutMediator


class WebtoonListActivity : AppCompatActivity() {

    private lateinit var binding:ActivityWebtoonListBinding

    private val tabTextList = arrayListOf("월","화","수","목","금","토","일")

    private val rowCnt:ArrayList<Int> = arrayListOf(7,5,9,5,4,10,12)

    private var initialHeight:Int = 0

    private lateinit var accessId:String

    private var myLastVisiblePos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebtoonListBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.viewPagerAd.adapter = ViewPagerAdAdapter(this)

        binding.viewPagerWebtoon.offscreenPageLimit = 1


        var initialHeight = 0
        var originalPos = 987654321
        binding.viewPagerWebtoon.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
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
                        Log.i("FragmentActivity.TAG", "TOP SCROLL")
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

    private fun hideViews(view: View) {
        view.animate().translationY((-view.getHeight()).toFloat())
            .setInterpolator(AccelerateInterpolator(2F))
    }

    private fun showViews(view:View) {
        view.animate().translationY(0F)
            .setInterpolator(DecelerateInterpolator(2F))
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