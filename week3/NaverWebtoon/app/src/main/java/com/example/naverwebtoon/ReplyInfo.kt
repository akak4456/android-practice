package com.example.naverwebtoon

data class ReplyInfo (val replyAuthor:String, val replyDate:String, val replyMsg:String, val upCnt:Int, val downCnt:Int,var isUp:Boolean,var isDown:Boolean)