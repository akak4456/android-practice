package com.example.naverwebtoon

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.naverwebtoon.databinding.MyItemBinding
import com.example.naverwebtoon.databinding.MyOptionBinding
import com.example.naverwebtoon.databinding.WebtoonItemBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class MyAdapter(private var context: Context, private val myList:ArrayList<MyInfo>,private val accessId:String,private val sp:SharedPreferences,private val editor:SharedPreferences.Editor) : BaseAdapter()  {

    private val gson:Gson = GsonBuilder().create()

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    lateinit var optionBinding:MyOptionBinding

    lateinit var binding: MyItemBinding

    override fun getCount(): Int = myList.size+1

    override fun getItem(position: Int): Any = myList[position-1]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int {
        if(position == 0)
            return 0
        else
            return 1
        return super.getItemViewType(position)
    }

    override fun getViewTypeCount(): Int {
        return 2
    }

    fun remove(index:Int){
        editor.remove(accessId+"_"+myList.get(index-1).key).commit()
        myList.removeAt(index-1)
    }

    fun changeTitle(index:Int,newTitle:String){
        myList.get(index-1).title = newTitle
        val strMyInfo:String = gson.toJson(myList.get(index-1))
        editor.putString(accessId+"_"+myList.get(index-1).key,strMyInfo).commit()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val type = getItemViewType(position)
        if(type == 0) {
            optionBinding = MyOptionBinding.inflate(inflater, parent, false)
            return optionBinding.root
        }else{
            val newPosition = position-1
            binding = MyItemBinding.inflate(inflater, parent, false)
            binding.myImg.clipToOutline = true
            binding.myImg.setImageDrawable(context.getDrawable(myList[newPosition].img))
            binding.myTitle.text = myList[newPosition].title
            if (myList[newPosition].isBellOn) {
                binding.bellOnBtn.visibility = View.VISIBLE
                binding.bellOffBtn.visibility = View.INVISIBLE
            } else {
                binding.bellOnBtn.visibility = View.INVISIBLE
                binding.bellOffBtn.visibility = View.VISIBLE
            }
            if(myList[newPosition].isUp){
                binding.showUp.visibility = View.VISIBLE
            }else{
                binding.showUp.visibility = View.INVISIBLE
            }
            if(newPosition == count-2){
                binding.border.visibility = View.INVISIBLE
            }
            binding.bellOnBtn.setOnClickListener{
                myList[newPosition].isBellOn = false
                val strMyInfo:String = gson.toJson(myList.get(newPosition-1))
                editor.putString(accessId+"_"+myList.get(newPosition-1).key,strMyInfo).commit()
                this.notifyDataSetChanged()
            }
            binding.bellOffBtn.setOnClickListener{
                myList[newPosition].isBellOn = true
                val strMyInfo:String = gson.toJson(myList.get(newPosition))
                editor.putString(accessId+"_"+myList.get(newPosition).key,strMyInfo).commit()
                this.notifyDataSetChanged()
            }
            return binding.root
        }
    }
}