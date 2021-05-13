package com.example.movieapp

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class WithWeatherDecoration(private val size:Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right += size
        outRect.left += size
        outRect.top += size
        outRect.bottom += size
    }
}