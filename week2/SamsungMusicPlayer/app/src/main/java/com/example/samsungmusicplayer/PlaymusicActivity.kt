package com.example.samsungmusicplayer

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.samsungmusicplayer.databinding.ActivityPlaymusicBinding

class PlaymusicActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPlaymusicBinding

    private var mediaPlayer: MediaPlayer? = null

    private var played:Boolean = false

    private var sbThread:Thread? = null

    inner class MyThread:Runnable{
        override fun run() {
           try{
               while(!Thread.currentThread().isInterrupted){
                   binding.sb.progress = mediaPlayer?.currentPosition!!
                   val second = (mediaPlayer?.currentPosition!! / 1000) % 60
                   val minute = (mediaPlayer?.currentPosition!! / 1000) / 60
                   runOnUiThread {
                       binding.playTime.text = String.format("%d:%02d", minute, second)
                   }
               }
           }catch(e:InterruptedException){
           }catch(e:Exception){

           }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle","PlayMusic-onCreate")

        binding = ActivityPlaymusicBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mediaPlayer = MediaPlayer.create(this,R.raw.music1)

        binding.musicTitle.text = this.intent.getStringExtra("title")
        binding.musicAuthor.text = this.intent.getStringExtra("author")
        played = this.intent.getBooleanExtra("played",false)
        mediaPlayer?.seekTo(this.intent.getIntExtra("pos",0))

        if(played){
            binding.playWhiteBtn.visibility = View.INVISIBLE
            binding.pauseWhiteBtn.visibility = View.VISIBLE
        }
        /*
        played가 혹시나 화면전환할 때 이 부분이 문제가 될까 싶었지만 그러지는 않았다.
        왜냐하면 아래에 화면전환에 대응한 부분이 있기 때문이다.
         */


        binding.sb.setOnSeekBarChangeListener(
                object: SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if(fromUser){
                            val ttt = seekBar?.progress
                            val second = (ttt!! / 1000) % 60
                            val minute = (ttt!! / 1000) / 60
                            runOnUiThread {
                                binding.playTime.text = String.format("%d:%02d", minute, second)
                            }
                        }
                        if(seekBar?.max == progress){
                            played = false
                            binding.playWhiteBtn.visibility = View.VISIBLE
                            binding.pauseWhiteBtn.visibility = View.INVISIBLE
                            mediaPlayer?.seekTo(0)
                            mediaPlayer?.pause()
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        sbThread?.interrupt()
                        sbThread = null
                        mediaPlayer?.pause()
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        val ttt = seekBar?.progress
                        if (ttt != null) {
                            mediaPlayer?.seekTo(ttt)
                            if(played)
                                mediaPlayer?.start()
                            sbThread = Thread(MyThread())
                            sbThread?.start()
                        }
                    }
                }
        )
        binding.playWhiteBtn.setOnClickListener{
            played = true
            binding.playWhiteBtn.visibility = View.INVISIBLE
            binding.pauseWhiteBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        binding.pauseWhiteBtn.setOnClickListener{
            played = false
            binding.playWhiteBtn.visibility = View.VISIBLE
            binding.pauseWhiteBtn.visibility = View.INVISIBLE
            mediaPlayer?.pause()
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
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey("pos")){
                played = true
                binding.playWhiteBtn.visibility = View.INVISIBLE
                binding.pauseWhiteBtn.visibility = View.VISIBLE
                mediaPlayer?.seekTo(savedInstanceState.getInt("pos"))
                mediaPlayer?.start()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle","PlayMusic-onStart")
        binding.sb.max = mediaPlayer?.duration!!
        sbThread = Thread(MyThread())
        sbThread?.start()

        val second = (mediaPlayer?.duration!! / 1000) % 60
        val minute = (mediaPlayer?.duration!! / 1000) / 60
        binding.endTime.text = String.format("%d:%02d",minute,second)
        /*
        사용자의 개입이 없다고 가정할 때 sbThread(Seekbar의 상태를 관리하는 쓰레드)는
        어디에서 시작해야 하고 어디에서 끝나야 할까?
        사실 시작 시점은 다소 애매한 부분이 있었다. 그러나 종료 시점은 onStop이 되야 하는 것이 확실했다.
        onStop 이 되어야 하는 이유는 onStop이 use한테 activity가 더이상 안 보일 때 불리기 때문이다.
        그래서 onStop에서 돌아가는 지점이 onStart가 되기 때문에 여기에서 시작하는 것이 적절하다 판단하였다.
        그렇다면 종료 시점이 왜 onPause가 되면 안될까?
        일단 처음으로는 onPause는 간단해야 하기 때문이다.
        그리고 두번째로는 onPause로 하면 multi window 사용시 문제가 발생할수도 있다.
        multi window를 사용하면 onPause에서 onStop을 거치지 않고 onResume으로 간다.
        그래서 만약에 onPause에 중단을 시키고 다시 돌아가기를 원한다면 onPause에 thread를 시작해야 하는데
        이는 적절하지 않다고 판단했다.
         */
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle","PlayMusic-onResume")
        mediaPlayer?.start()
        if(!played)
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
        if(played){
            outState.putInt("pos",mediaPlayer?.currentPosition!!)
        }else{
            outState.remove("pos")
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle","PlayMusic-onPause")
        mediaPlayer?.pause()
        /*
        onResume에 써서 onPause에 pause하는 것도 맞지만 그것보다도 multi window에 대해서 고려해보았다.
        multi window에서는 onStop을 거치지 않고 onPause에서 onResume으로 바로 돌아간다.
        근데 multi window 상황일 때 다른 것을 클릭하면 소리가 꺼져야만 한다.
        그래서 이를 여기에다가 실행했다.
         */
    }

    override fun onStop(){
        super.onStop()
        Log.d("LifeCycle","PlayMusic-onStop")
        sbThread?.interrupt()
        /*
        seekbar는 onStop 일때 더이상 필요하지 않으므로 여기에서 interrupt를 걸어 종료시켰다.
        근데 왜 mediaPlayer는 stop시키고 해제시키지 않았나?
        그 이유는 onStop에서 onDestroy로 가지 않고 onStart로 갈 수 있기 때문이다.
        음악 생성은 onCreate에서 실행했다.
        그래서 만약에 여기에서 mediaPlayer를 해체하면 home키를 누른 뒤에 다시 돌아가면 오류가 발생한다.
         */
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("oldPos",mediaPlayer?.currentPosition)
        setResult(RESULT_OK,intent)
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifeCycle","PlayMusic-onDestroy")
        mediaPlayer?.stop()
        mediaPlayer?.release()
        /*
        onCreate에 음악 파일을 생성했으므로
        여기에다가 release시키는 것이 가장 적절하다 판단했다.
         */
    }
}