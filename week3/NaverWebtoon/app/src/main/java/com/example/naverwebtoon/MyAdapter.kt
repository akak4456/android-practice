package com.example.naverwebtoon

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.example.naverwebtoon.databinding.MyItemBinding
import com.example.naverwebtoon.databinding.MyOptionBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class MyAdapter(
    private var context: Context,
    private var myList:ArrayList<MyInfo>,
    private val accessId: String,
    private val sp: SharedPreferences,
    private val editor: SharedPreferences.Editor,
    private val spId: SharedPreferences,
    private var myListFiltered:ArrayList<MyInfo>
) : BaseAdapter(), Filterable {

    private val gson: Gson = GsonBuilder().create()

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    lateinit var binding: MyItemBinding

    override fun getCount(): Int = myListFiltered.size

    override fun getItem(position: Int): Any = myListFiltered[position]

    override fun getItemId(position: Int): Long = position.toLong()

    fun remove(index: Int) {
        editor.remove(accessId + "_" + myListFiltered[index - 1].key).commit()
        myListFiltered.removeAt(index - 1)
    }

    fun changeTitle(index: Int, newTitle: String) {
        myListFiltered[index - 1].title = newTitle
        val strMyInfo: String = gson.toJson(myListFiltered[index - 1])
        editor.putString(accessId + "_" + myListFiltered[index - 1].key, strMyInfo).commit()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        binding = MyItemBinding.inflate(inflater, parent, false)
        binding.myImg.clipToOutline = true
        binding.myImg.setImageDrawable(context.getDrawable(myListFiltered[position].img))
        binding.myTitle.text = myListFiltered[position].title
        if (myListFiltered[position].isBellOn) {
            binding.bellOnBtn.visibility = View.VISIBLE
            binding.bellOffBtn.visibility = View.INVISIBLE
        } else {
            binding.bellOnBtn.visibility = View.INVISIBLE
            binding.bellOffBtn.visibility = View.VISIBLE
        }
        if (myListFiltered[position].isUp) {
            binding.showUp.visibility = View.VISIBLE
        } else {
            binding.showUp.visibility = View.INVISIBLE
        }
        if (position == count - 1) {
            binding.border.visibility = View.INVISIBLE
        }
        binding.bellOnBtn.setOnClickListener {
            myListFiltered[position].isBellOn = false
            val strMyInfo: String = gson.toJson(myListFiltered[position])
            editor.putString(accessId + "_" + myListFiltered[position].key, strMyInfo).commit()
            this.notifyDataSetChanged()
        }
        binding.bellOffBtn.setOnClickListener {
            myListFiltered[position].isBellOn = true
            val strMyInfo: String = gson.toJson(myListFiltered[position])
            editor.putString(accessId + "_" + myListFiltered[position].key, strMyInfo).commit()
            this.notifyDataSetChanged()
        }
        return binding.root
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults
            ) {
                myListFiltered = results.values as ArrayList<MyInfo>
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val results = FilterResults()
                val FilteredList: ArrayList<MyInfo> = ArrayList()
                if (constraint == null || constraint.length == 0) {
                    // No filter implemented we return all the list
                    results.values = myList
                    results.count = myList.size
                } else {
                    for (i in 0 until myList.size) {
                        val data: String = myList[i].title
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredList.add(myList[i])
                        }
                    }
                    results.values = FilteredList
                    results.count = FilteredList.size
                }
                return results
            }
        }
    }
}