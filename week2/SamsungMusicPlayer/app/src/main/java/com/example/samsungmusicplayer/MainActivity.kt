package com.example.samsungmusicplayer

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.text.Html
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.example.samsungmusicplayer.databinding.ActivityMainBinding
import com.example.samsungmusicplayer.databinding.ActivityPlaymusicBinding
import java.io.File


class MainActivity : FragmentActivity() {

    private val context: Context = this

    private lateinit var binding: ActivityMainBinding

    private var mediaPlayer: MediaPlayer? = null

    private var played: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle", "Main-onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.playTitle.isSelected = true

        mediaPlayer = MediaPlayer.create(this, R.raw.music1)

        if (played) {
            binding.playThisMusicBtn.visibility = View.INVISIBLE
            binding.stopThisMusicBtn.visibility = View.VISIBLE
        }

        binding.playThisMusicBtn.setOnClickListener {
            played = true
            binding.playThisMusicBtn.visibility = View.INVISIBLE
            binding.stopThisMusicBtn.visibility = View.VISIBLE
            mediaPlayer?.start()

            val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            builder.setView(layoutInflater.inflate(R.layout.activity_go_to_login, null))

            val dialog: AlertDialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            val wmlp: WindowManager.LayoutParams? = dialog.window?.attributes;

            wmlp?.gravity = Gravity.BOTTOM;
            wmlp?.y = binding.bottom.height;
            wmlp?.horizontalMargin = -100f




            dialog.show()

            val loginLink:TextView = dialog.findViewById(R.id.login_link)!!
            var spannableString = SpannableString("로그인")
            spannableString.setSpan(UnderlineSpan(),0,spannableString.length,0)
            loginLink.text = spannableString
            loginLink.setOnClickListener{
                val fragment = LoginFragment()

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame3,fragment).commit()
                transaction.addToBackStack(null)
                dialog.dismiss()
            }
            dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)

        }
        binding.stopThisMusicBtn.setOnClickListener {
            played = false
            binding.playThisMusicBtn.visibility = View.VISIBLE
            binding.stopThisMusicBtn.visibility = View.INVISIBLE
            mediaPlayer?.pause()
        }
        binding.playMusicDraw.setOnClickListener {
            val intent = Intent(this, PlaymusicActivity::class.java)
            intent.putExtra("title", "Ellonica")
            intent.putExtra("author", "Rinne")
            intent.putExtra("played", played)
            intent.putExtra("pos", mediaPlayer?.currentPosition)
            startActivityForResult(intent, 1)
        }
        binding.shareBtn.setOnClickListener {
            val shareIntent: Intent = Intent(Intent.ACTION_SEND).apply {
                type = "audio/*"
                putExtra(Intent.EXTRA_STREAM, Uri.parse("android.resource://" + context.packageName + "/" + R.raw.music1))
            }
            //경로 설정을 android:resource://[패키지이름]/resId와 같이 해야 함
            startActivity(Intent.createChooser(shareIntent, null))
        }
        /*
       view 설정
       음악 파일 불러오기
       이벤트 설정
       등 초기화 부분은 여기가 가장 적절하다 판단했다.
        */

        /*
        화면 전환에 대응하기 위함
         */
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("pos")) {
                played = true
                binding.playThisMusicBtn.visibility = View.INVISIBLE
                binding.stopThisMusicBtn.visibility = View.VISIBLE
                mediaPlayer?.seekTo(savedInstanceState.getInt("pos"))
                mediaPlayer?.start()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle", "Main-onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle", "Main-onResume")
        mediaPlayer?.start()
        if (!played)
            mediaPlayer?.pause()
        /*
        onResume은 activity가 user랑 상호작용을 하기 시작할 때에 호출된다.
        그래서 음악 플레이가 되는 것이 상호작용 하는 것이라 판단해 여기에 썼다.
         */
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        /*
        화면 전환 시에 대응하기 위해 존재함
         */
        if (played) {
            outState.putInt("pos", mediaPlayer?.currentPosition!!)
        } else {
            outState.remove("pos")
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle", "Main-onPause")
        mediaPlayer?.pause()
        /*
        onResume에 써서 onPause에 pause하는 것도 맞지만 그것보다도 multi window에 대해서 고려해보았다.
        multi window에서는 onStop을 거치지 않고 onPause에서 onResume으로 바로 돌아간다.
        근데 multi window 상황일 때 다른 것을 클릭하면 소리가 꺼져야만 한다.
        그래서 이를 여기에다가 실행했다.

        또 Main에서 PlayMusic으로 이동할 시에 Main-onStop이 다소 늦게 호출이 되는 점도 확인함.
         */
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifeCycle", "Main-onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifeCycle", "Main-onDestroy")
        mediaPlayer?.stop()
        mediaPlayer?.release()
        /*
        onCreate에 음악 파일을 생성했으므로
        여기에다가 release시키는 것이 가장 적절하다 판단했다.
         */
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("LifeCycle", "Main-onActivityResult")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val oldPos = data?.getIntExtra("oldPos", 0)
                mediaPlayer?.seekTo(oldPos ?: 0)
            }
        }
    }

}