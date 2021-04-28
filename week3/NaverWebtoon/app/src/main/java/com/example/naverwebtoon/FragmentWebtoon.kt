package com.example.naverwebtoon

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FragmentWebtoon(
        private val webtoonList:ArrayList<WebtoonInfo>,
        private val accessId: String,
        private val sp: SharedPreferences,
        private val spWebtoon: SharedPreferences
) : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_webtoon, container, false)

        val re: RecyclerView = view.findViewById(R.id.webtoon_rv)
        re.adapter = WebtoonAdapter(context!!,webtoonList,accessId,sp,spWebtoon)
        val numberOfColumns = 3 // 한줄에 5개의 컬럼을 추가합니다.
        val mGridLayoutManager = GridLayoutManager(context, numberOfColumns)
        mGridLayoutManager.isAutoMeasureEnabled = true
        re.setLayoutManager(mGridLayoutManager)
        return view
    }
}