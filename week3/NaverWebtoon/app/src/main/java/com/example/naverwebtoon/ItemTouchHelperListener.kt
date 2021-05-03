package com.example.naverwebtoon

interface ItemTouchHelperListener {
    fun onItemMove(fromPosition:Int,toPosition:Int):Boolean
    fun onItemSwipe(position:Int)
}