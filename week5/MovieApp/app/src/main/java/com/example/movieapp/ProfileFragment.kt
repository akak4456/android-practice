package com.example.movieapp

import android.app.ActionBar
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.movieapp.databinding.FragmentProfileBinding

class ProfileFragment(private val profileURL:String,private val profileNickname:String,private val profileEmail:String) : DialogFragment(),View.OnClickListener {
    private lateinit var binding:FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onClick(v: View?) {
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }
}