package com.example.naverwebtoon

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import com.example.naverwebtoon.databinding.WebtoonItemBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlin.random.Random


class FragmentMonWebtoon(
    private val accessId: String,
    private val sp: SharedPreferences,
    private val spWebtoon: SharedPreferences
) : Fragment() {
    private var webtoonList:ArrayList<WebtoonInfo> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        for(i in 1..4){
            webtoonList.add(WebtoonInfo(R.drawable.mon_webtoon_1,getString(R.string.mon_webtoon_1_title),getString(R.string.mon_webtoon_1_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f)))
            webtoonList.add(WebtoonInfo(R.drawable.mon_webtoon_2,getString(R.string.mon_webtoon_2_title),getString(R.string.mon_webtoon_2_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f)))
            webtoonList.add(WebtoonInfo(R.drawable.mon_webtoon_3,getString(R.string.mon_webtoon_3_title),getString(R.string.mon_webtoon_3_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f)))
            webtoonList.add(WebtoonInfo(R.drawable.mon_webtoon_4,getString(R.string.mon_webtoon_4_title),getString(R.string.mon_webtoon_4_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f)))
            webtoonList.add(WebtoonInfo(R.drawable.mon_webtoon_5,getString(R.string.mon_webtoon_5_title),getString(R.string.mon_webtoon_5_author),"★ "+String.format("%.2f", Random.nextInt(700,1000)/100.0f)))
        }
        val view: View = inflater.inflate(R.layout.fragment_webtoon, container, false)

        val grid:GridLayout = view.findViewById(R.id.grid_main)
        grid.removeAllViews()
        grid.columnCount = 3
        val display = activity!!.windowManager.defaultDisplay
        val stageWidth = display.width
        for(webtoon in webtoonList){
            val binding:WebtoonItemBinding = WebtoonItemBinding.inflate(inflater,grid,false)
            binding.webtoonImg.setImageDrawable(context!!.getDrawable(webtoon.img))
            binding.webtoonTitle.text = webtoon.title
            binding.webtoonAuthor.text = webtoon.author
            binding.webtoonStar.text = webtoon.star
            binding.webtoonImg.setOnClickListener{
                if(accessId == "none"){
                    val dlg:AlertDialog.Builder = AlertDialog.Builder(context)
                    dlg.setTitle("관심 웹툰 추가")
                    dlg.setMessage("로그인을 먼저 해주세요!")
                    dlg.setPositiveButton("확인") { dialog, which ->
                        val intent = Intent(context,LoginList::class.java)
                        startActivity(intent)
                    }
                    dlg.show()
                }else{
                    val dlg:AlertDialog.Builder = AlertDialog.Builder(context)
                    dlg.setTitle("관심 웹툰 추가")
                    dlg.setMessage("정말 ["+binding.webtoonTitle.text+"]을 관심웹툰으로 추가하시겠습니까?")
                    dlg.setPositiveButton("추가") { dialog, which ->
                        val prevLastNumber = sp.getInt(accessId,0)
                        val savedMyInfo = MyInfo(prevLastNumber+1,webtoon.img,webtoon.title,false,false)
                        val gson:Gson = GsonBuilder().create()
                        val strMyInfo = gson.toJson(savedMyInfo)
                        spWebtoon.edit().putString(accessId+"_"+(prevLastNumber+1),strMyInfo)
                    }
                    dlg.setNegativeButton("취소") { dialog, which ->

                    }
                    dlg.show()
                }

            }
            grid.addView(binding.root,stageWidth/3,WRAP_CONTENT)
        }
        return view
    }
}