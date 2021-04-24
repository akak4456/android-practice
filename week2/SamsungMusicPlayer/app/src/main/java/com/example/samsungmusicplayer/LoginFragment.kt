package com.example.samsungmusicplayer

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("LifeCycle","LoginFragment-onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle","LoginFragment-oCreate")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.d("LifeCycle","LoginFragment-onCreateView")
        val root =inflater.inflate(R.layout.fragment_login, container, false)
        val backBtn:ImageButton = root?.findViewById(R.id.back_btn)!!
        backBtn.setOnClickListener{
            val transaction =  fragmentManager?.beginTransaction()
            val fragment = fragmentManager?.findFragmentById(R.id.frame3)
            transaction?.remove(fragment!!)?.commit()
        }

        val join: TextView = root.findViewById(R.id.join)!!
        var spannableString = SpannableString("회원가입")
        spannableString.setSpan(UnderlineSpan(),0,spannableString.length,0)
        join.text = spannableString

        val melon: TextView = root.findViewById(R.id.melon)!!
        var melonText1 = SpannableString("멜론아이디 이용 고객님은 카카오 계정과 통합하신\n후 이용해 주시길 바랍니다.")
        melonText1.setSpan(ForegroundColorSpan(Color.parseColor("#909090")),0,melonText1.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        var melonText2 = SpannableString("자세히 보기")
        melonText2.setSpan(ForegroundColorSpan(Color.parseColor("#0000ff")),0,melonText2.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        melonText2.setSpan(UnderlineSpan(),0,melonText2.length,0)
        melon.text = TextUtils.concat(melonText1," ",melonText2)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("LifeCycle","LoginFragment-onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle","LoginFragment-onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle","LoginFragment-onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle","LoginFragment-onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifeCycle","LoginFragment-onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        super.onDestroy()
        Log.d("LifeCycle","LoginFragment-onDestoryView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifeCycle","LoginFragment-onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("LifeCycle","LoginFragment-onDetach")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                LoginFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}