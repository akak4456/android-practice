package com.example.naverwebtoon

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.naverwebtoon.databinding.WebtoonItemBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class WebtoonAdapter(private var context: Context, private val webtoonList:ArrayList<WebtoonInfo>, private val accessId:String, private val sp:SharedPreferences, private val spWebtoon:SharedPreferences) : RecyclerView.Adapter<WebtoonAdapter.ViewHolder>() {

    lateinit var bindind: WebtoonItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        bindind = WebtoonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bindind)
    }

    override fun getItemCount(): Int {
        return webtoonList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(webtoonList[position])

    }

    inner class ViewHolder(private val binding: WebtoonItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(model: WebtoonInfo) {
            binding.webtoonImg.setImageDrawable(context.getDrawable(model.img))
            binding.webtoonTitle.text = model.title
            binding.webtoonAuthor.text = model.author
            binding.webtoonStar.text = model.star

            if (!model.isUp) {
                binding.showUp.visibility = View.INVISIBLE
            }

            binding.webtoonImg.setOnClickListener {
                if (accessId == "none") {
                    val dlg: AlertDialog.Builder = AlertDialog.Builder(context)
                    dlg.setTitle("관심 웹툰 추가")
                    dlg.setMessage("로그인을 먼저 해주세요!")
                    dlg.setPositiveButton("확인") { dialog, which ->
                        val intent = Intent(context, LoginList::class.java)
                        context.startActivity(intent)
                    }
                    dlg.show()
                } else {
                    val dlg: AlertDialog.Builder = AlertDialog.Builder(context)
                    dlg.setTitle("관심 웹툰 추가")
                    dlg.setMessage("정말 [" + binding.webtoonTitle.text + "]을/를 관심웹툰으로 추가하시겠습니까?")
                    dlg.setPositiveButton("추가") { dialog, which ->
                        val prevLastNumber = sp.getInt(accessId, 0)
                        val savedMyInfo = MyInfo(prevLastNumber + 1, model.img, model.title, false, false)
                        val gson: Gson = GsonBuilder().create()
                        val strMyInfo = gson.toJson(savedMyInfo)
                        sp.edit().putInt(accessId, prevLastNumber + 1).commit()
                        spWebtoon.edit().putString(accessId + "_" + (prevLastNumber + 1), strMyInfo).commit()
                        val dlg: AlertDialog.Builder = AlertDialog.Builder(context)
                        dlg.setTitle("관심 웹툰 추가")
                        dlg.setMessage("관심 웹툰에 추가했습니다")
                        dlg.setPositiveButton("확인") { dialog, which ->
                        }
                        dlg.show()
                    }
                    dlg.setNegativeButton("취소") { dialog, which ->

                    }
                    dlg.show()
                }
            }
        }

    }
}