package com.example.movieapp

import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.FragmentProfileBinding
import com.kakao.sdk.user.UserApiClient

class ProfileFragment(private val profileURL:String,private val profileNickname:String,private val profileEmail:String?) : DialogFragment(),View.OnClickListener {
    private lateinit var binding:FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        val circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable.setColorSchemeColors(Color.parseColor("#ff7f00"))
        circularProgressDrawable.strokeWidth = 6f
        circularProgressDrawable.centerRadius = 40f
        circularProgressDrawable.start()

        Glide.with(context)
            .load(profileURL)
            .placeholder(circularProgressDrawable)
            .into(binding.profileImg)
        binding.profileImg.clipToOutline = true
        binding.profileNickname.text = profileNickname
        if(profileEmail == null){
            binding.profileEmail.text = "없음"
        }else{
            binding.profileEmail.text= profileEmail
        }

        binding.xBtn.setOnClickListener{
            dismiss()
        }

        binding.logoutBtn.setOnClickListener{
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Toast.makeText(activity, "로그아웃에 실패하였습니다. 관리자에게 문의해주세요", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(activity, "로그아웃에 성공했습니다.", Toast.LENGTH_SHORT).show()
                    activity!!.finish()
                    startActivity(activity!!.intent)
                }
            }
        }
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