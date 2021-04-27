package com.example.naverwebtoon

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import com.example.naverwebtoon.databinding.WebtoonItemBinding
import kotlin.collections.ArrayList
import kotlin.random.Random

class FragmentSatWebtoon(
    accessId: String,
    sp: SharedPreferences,
    spWebtoon: SharedPreferences
) : Fragment() {
    private var webtoonList:ArrayList<WebtoonInfo> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        for(i in 1..6){
            webtoonList.add(WebtoonInfo(R.drawable.sat_webtoon_1,getString(R.string.sat_webtoon_1_title),getString(R.string.sat_webtoon_1_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f)))
            webtoonList.add(WebtoonInfo(R.drawable.sat_webtoon_2,getString(R.string.sat_webtoon_2_title),getString(R.string.sat_webtoon_2_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f)))
            webtoonList.add(WebtoonInfo(R.drawable.sat_webtoon_3,getString(R.string.sat_webtoon_3_title),getString(R.string.sat_webtoon_3_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f)))
            webtoonList.add(WebtoonInfo(R.drawable.sat_webtoon_4,getString(R.string.sat_webtoon_4_title),getString(R.string.sat_webtoon_4_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f)))
            webtoonList.add(WebtoonInfo(R.drawable.sat_webtoon_5,getString(R.string.sat_webtoon_5_title),getString(R.string.sat_webtoon_5_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f)))
        }
        val view: View = inflater.inflate(R.layout.fragment_webtoon, container, false)

        val grid:GridLayout = view.findViewById(R.id.grid_main)
        grid.removeAllViews()
        grid.columnCount = 3
        val display = activity!!.windowManager.defaultDisplay
        val stageWidth = display.width
        for(webtoon in webtoonList){
            val binding: WebtoonItemBinding = WebtoonItemBinding.inflate(inflater,grid,false)
            binding.webtoonImg.setImageDrawable(context!!.getDrawable(webtoon.img))
            binding.webtoonTitle.text = webtoon.title
            binding.webtoonAuthor.text = webtoon.author
            binding.webtoonStar.text = webtoon.star

            grid.addView(binding.root,stageWidth/3, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        return view
    }
}