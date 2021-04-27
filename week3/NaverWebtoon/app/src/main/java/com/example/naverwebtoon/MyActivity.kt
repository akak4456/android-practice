package com.example.naverwebtoon

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.naverwebtoon.databinding.ActivityMyBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder


class MyActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMyBinding

    private var myList:ArrayList<MyInfo> = ArrayList()

    private lateinit var myAdapter:MyAdapter

    private lateinit var context: Context

    private lateinit var accessId:String //어떤 아이디로 접근했나

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this





        val touchListener = SwipeDismissListViewTouchListener(binding.lvMy,object: SwipeDismissListViewTouchListener.DismissCallbacks{
            override fun onDismiss(listView: ListView?, reverseSortedPositions: IntArray?) {
                for(idx in reverseSortedPositions!!){
                    myAdapter.remove(idx)
                }
                myAdapter.notifyDataSetChanged()
            }

            override fun canDismiss(position: Int): Boolean {
                return true
            }
        })


        binding.lvMy.setOnTouchListener(touchListener)

        binding.lvMy.setOnItemLongClickListener{parent,view,position,id->
            Log.d("TMP",position.toString())
            val editText = EditText(context)

            val changeTitleBuilder:AlertDialog.Builder = AlertDialog.Builder(context)
            changeTitleBuilder.setTitle("관심 웹툰 제목 변경")
            changeTitleBuilder.setView(editText)
            changeTitleBuilder.setPositiveButton("변경") { dialog, which ->
                val strText:String = editText.text.toString()
                myAdapter.changeTitle(position,strText)
                myAdapter.notifyDataSetChanged()
            }
            changeTitleBuilder.setNegativeButton("취소") { dialog, which ->

            }
            changeTitleBuilder.show()
            true
        }
        binding.bottomAppbar.goToWebtoonBtn.setOnClickListener{
            val intent = Intent(this,WebtoonListActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginBtn.setOnClickListener{
            val intent = Intent(this,LoginList::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val spId:SharedPreferences = getSharedPreferences("sharedId",Context.MODE_PRIVATE)
        val spWebtoon:SharedPreferences = getSharedPreferences("sharedWebtoon", Context.MODE_PRIVATE)
        if(spId.contains("alreadyId")){
            accessId = spId.getString("alreadyId","none").toString()
        }else{
            accessId = "none"
            spId.edit().putString("alreadyId",accessId).commit()
        }
        Log.d("TMP",accessId)
        if(accessId == "none"){
            binding.loginFirst.visibility = View.VISIBLE
            binding.lvMy.visibility = View.INVISIBLE
        }else{
            binding.loginFirst.visibility = View.INVISIBLE
            binding.lvMy.visibility = View.VISIBLE
        }

        val editor:SharedPreferences.Editor = spWebtoon.edit()
        val allEntries: Map<String, *> = spWebtoon.all
        val gson: Gson = GsonBuilder().create()
        for ((key, value) in allEntries) {
            if(key.contains(accessId)){
                val myInfo:MyInfo = gson.fromJson(value.toString(),MyInfo::class.java)
                myList.add(myInfo)

            }
        }
        myList.sortBy { myInfo -> myInfo.key  }
        myAdapter = MyAdapter(this,myList,accessId,spWebtoon,editor)

        binding.lvMy.adapter = myAdapter

    }
}