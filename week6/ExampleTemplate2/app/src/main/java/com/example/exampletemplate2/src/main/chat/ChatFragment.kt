package com.example.exampletemplate2.src.main.chat

import android.os.Bundle
import android.view.View
import com.example.exampletemplate2.R
import com.example.exampletemplate2.config.BaseFragment
import com.example.exampletemplate2.databinding.FragmentChatBinding

class ChatFragment:BaseFragment<FragmentChatBinding>(FragmentChatBinding::bind, R.layout.fragment_chat) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}