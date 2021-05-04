package com.example.ageofwar

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import com.example.ageofwar.databinding.ActivityMainBinding
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var context: Context

    private val myHomePosition = 20//dp단위임
    private val enemyHomePosition = -180//dp단위임
    private lateinit var displayMetrics: DisplayMetrics
    private lateinit var binding:ActivityMainBinding
    private var mySoldierList:ArrayList<SoldierInfo> = ArrayList()
    private var enemySoldierList:ArrayList<SoldierInfo> = ArrayList()
    private val soldierBetweenThreshold = -85

    private val normalSoldierAttackPoint = 1
    private val normalSoldierHp = 20
    private val distanceSoldierAttackPoint = 1
    private val distanceSoldierHp = 20

    private val wholeProgress = 25
    private val wholeHp = 200
    private var myMoney = 200
    private var myProgress = 0
    private var myQueue:BlockingQueue<String> = ArrayBlockingQueue<String>(5)
    private var myHp = wholeHp
    private var enemyMoney = 180
    private var enemyProgress = 0
    private var enemyQueue:BlockingQueue<String> = ArrayBlockingQueue<String>(5)
    private var enemyHp = wholeHp

    private var finished = false
    private var paused = false

    private var backgroundMusic: MediaPlayer? = null
    private val moneyRatio = 0.7//난이도 조절을 위해 적이 나의 병사를 죽일 때 얻는 돈의 비율 조정
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        displayMetrics = this.resources.displayMetrics
        context = this
        backgroundMusic = MediaPlayer.create(this, R.raw.background_music)
        backgroundMusic!!.isLooping = true
        backgroundMusic!!.start()
        binding.showMoney.text = myMoney.toString()
        Thread {
            //soldier 움직임을 관리한다
            while(!finished){
                while(!paused){
                    synchronized(mySoldierList) {
                        for(i in mySoldierList.indices){
                            if(i == 0){
                                if(mySoldierList[0].soldierPosition >= 560){
                                    if(mySoldierList[0].soldierAnimationType != "attack"){
                                        mySoldierList[0].soldierAnimationType = "attack"
                                        mySoldierList[0].soldierAnimationStartTime = 0L
                                    }
                                    enemyHp -= mySoldierList[0].attackPoint
                                    runOnUiThread{
                                        binding.enemyHpBar.layoutParams.width = (binding.enemyHpBarFrame.width * (enemyHp*1.0f/wholeHp)).toInt()
                                        binding.enemyHpBar.requestLayout()
                                    }
                                    if(enemyHp < 0){
                                        val intent = Intent(context,VictoryActivity::class.java)
                                        context.startActivity(intent)
                                        (context as MainActivity).finish()
                                        finished = true
                                        paused = true
                                    }
                                }else if(enemySoldierList.size > 0){
                                    if(mySoldierList[0].soldierPosition + enemySoldierList[0].soldierPosition > soldierBetweenThreshold) {
                                        if (mySoldierList[0].soldierAnimationType != "attack") {
                                            mySoldierList[0].soldierAnimationType = "attack"
                                            mySoldierList[0].soldierAnimationStartTime = 0L
                                        }
                                        enemySoldierList[0].hp -= mySoldierList[0].attackPoint
                                        if (enemySoldierList[0].hp < 0) {
                                            myMoney += enemySoldierList[0].getMoney
                                            runOnUiThread{
                                                binding.showMoney.text = myMoney.toString()
                                            }
                                            enemySoldierList.removeAt(0)
                                        }
                                    }else if(mySoldierList[0].soldierType == "distance_soldier"&&mySoldierList[0].soldierPosition + enemySoldierList[0].soldierPosition > soldierBetweenThreshold*1.4){
                                        if(mySoldierList[0].soldierAnimationType != "attack_distance"){
                                            mySoldierList[0].soldierAnimationType = "attack_distance"
                                            mySoldierList[0].soldierAnimationStartTime = 0L
                                        }
                                        enemySoldierList[0].hp -= mySoldierList[0].attackPoint
                                        if(enemySoldierList[0].hp < 0){
                                            myMoney += enemySoldierList[0].getMoney
                                            runOnUiThread{
                                                binding.showMoney.text = myMoney.toString()
                                            }
                                            enemySoldierList.removeAt(0)
                                        }
                                    }else{
                                        if(mySoldierList[0].soldierAnimationType != "walk"){
                                            mySoldierList[0].soldierAnimationType = "walk"
                                            mySoldierList[0].soldierAnimationStartTime = 0L
                                        }
                                        mySoldierList[0].soldierPosition += dpToPx(1)
                                    }
                                }else{
                                    if(mySoldierList[0].soldierAnimationType != "walk"){
                                        mySoldierList[0].soldierAnimationType = "walk"
                                        mySoldierList[0].soldierAnimationStartTime = 0L
                                    }
                                    mySoldierList[0].soldierPosition += dpToPx(1)
                                }
                            }else{
                                if(mySoldierList[i-1].soldierPosition-mySoldierList[i].soldierPosition >= 30){
                                    if(mySoldierList[i].soldierAnimationType != "walk"){
                                        mySoldierList[i].soldierAnimationType = "walk"
                                        mySoldierList[i].soldierAnimationStartTime = 0L
                                    }
                                    mySoldierList[i].soldierPosition += dpToPx(1)
                                }else{
                                    if(mySoldierList[i].soldierAnimationType != "stop"){
                                        mySoldierList[i].soldierAnimationType = "stop"
                                        mySoldierList[i].soldierAnimationStartTime = 0L
                                    }
                                }
                            }
                        }
                    }
                    synchronized(enemySoldierList){
                        for(i in enemySoldierList.indices){
                            if(i == 0){
                                if(enemySoldierList[0].soldierPosition >= -120){
                                    if(enemySoldierList[0].soldierAnimationType != "attack"){
                                        enemySoldierList[0].soldierAnimationType = "attack"
                                        enemySoldierList[0].soldierAnimationStartTime = 0L
                                    }
                                    myHp -= enemySoldierList[0].attackPoint
                                    runOnUiThread{
                                        binding.myHpBar.layoutParams.width = (binding.myHpBarFrame.width * (myHp*1.0f/wholeHp)).toInt()
                                        binding.myHpBar.requestLayout()
                                    }
                                    if(myHp < 0){
                                        val intent = Intent(context,DefeatActivity::class.java)
                                        context.startActivity(intent)
                                        (context as MainActivity).finish()
                                        finished = true
                                        paused = true
                                    }
                                }
                                else if(mySoldierList.size > 0){
                                    if(mySoldierList[0].soldierPosition + enemySoldierList[0].soldierPosition > soldierBetweenThreshold) {
                                        if (enemySoldierList[0].soldierAnimationType != "attack") {
                                            enemySoldierList[0].soldierAnimationType = "attack"
                                            enemySoldierList[0].soldierAnimationStartTime = 0L
                                        }
                                        mySoldierList[0].hp -= enemySoldierList[0].attackPoint
                                        if (mySoldierList[0].hp < 0) {
                                            enemyMoney += (mySoldierList[0].getMoney*moneyRatio).toInt()
                                            mySoldierList.removeAt(0)
                                        }
                                    }else if(enemySoldierList[0].soldierType == "distance_soldier"&&mySoldierList[0].soldierPosition + enemySoldierList[0].soldierPosition > soldierBetweenThreshold*1.4){
                                        if(enemySoldierList[0].soldierAnimationType != "attack_distance"){
                                            enemySoldierList[0].soldierAnimationType = "attack_distance"
                                            enemySoldierList[0].soldierAnimationStartTime = 0L
                                        }
                                        mySoldierList[0].hp -= enemySoldierList[0].attackPoint
                                        if(mySoldierList[0].hp < 0){
                                            enemyMoney += (mySoldierList[0].getMoney*moneyRatio).toInt()
                                            mySoldierList.removeAt(0)
                                        }
                                    }else{
                                        if(enemySoldierList[0].soldierAnimationType != "walk"){
                                            enemySoldierList[0].soldierAnimationType = "walk"
                                            enemySoldierList[0].soldierAnimationStartTime = 0L
                                        }
                                        enemySoldierList[0].soldierPosition += dpToPx(1)
                                    }
                                }else{
                                    if(enemySoldierList[0].soldierAnimationType != "walk"){
                                        enemySoldierList[0].soldierAnimationType = "walk"
                                        enemySoldierList[0].soldierAnimationStartTime = 0L
                                    }
                                    enemySoldierList[0].soldierPosition += dpToPx(1)
                                }
                            }else{
                                if(enemySoldierList[i-1].soldierPosition-enemySoldierList[i].soldierPosition >= 30){
                                    if(enemySoldierList[i].soldierAnimationType != "walk"){
                                        enemySoldierList[i].soldierAnimationType = "walk"
                                        enemySoldierList[i].soldierAnimationStartTime = 0L
                                    }
                                    enemySoldierList[i].soldierPosition += dpToPx(1)
                                }else{
                                    if(enemySoldierList[i].soldierAnimationType != "stop"){
                                        enemySoldierList[i].soldierAnimationType = "stop"
                                        enemySoldierList[i].soldierAnimationStartTime = 0L
                                    }
                                }
                            }
                        }
                    }
                    Thread.sleep(200)
                }
                Thread.sleep(1000)
            }
        }.start()
        Thread{
            //my progressbar 관리 관련
            while(!finished){
                while(!paused){
                    val soldierType = myQueue.take()
                    while(myProgress < wholeProgress) {
                        myProgress++
                        runOnUiThread {
                            binding.showProgressBar.layoutParams.width = (binding.showProgress.width * (myProgress * 1.0f / wholeProgress)).toInt()
                            binding.showProgressBar.requestLayout()
                        }
                        Thread.sleep(50)
                    }
                    myProgress = 0
                    runOnUiThread{
                        binding.showProgressBar.layoutParams.width = 0
                        binding.showProgressBar.requestLayout()
                        //Log.d("QUEUE",myQueue.size.toString())
                        if(myQueue.size == 0){
                            binding.normal1.visibility = View.INVISIBLE
                            binding.distance1.visibility = View.INVISIBLE
                        }
                        if(binding.normal2.visibility == View.VISIBLE){
                            binding.normal1.visibility = View.VISIBLE
                            binding.normal2.visibility = View.INVISIBLE
                        }else if(binding.distance2.visibility == View.VISIBLE){
                            binding.distance1.visibility = View.VISIBLE
                            binding.distance2.visibility = View.INVISIBLE
                        }
                        if(binding.normal3.visibility == View.VISIBLE){
                            binding.normal2.visibility = View.VISIBLE
                            binding.normal3.visibility = View.INVISIBLE
                        }else if(binding.distance3.visibility == View.VISIBLE){
                            binding.distance2.visibility = View.VISIBLE
                            binding.distance3.visibility = View.INVISIBLE
                        }
                        if(binding.normal4.visibility == View.VISIBLE){
                            binding.normal3.visibility = View.VISIBLE
                            binding.normal4.visibility = View.INVISIBLE
                        }else if(binding.distance4.visibility == View.VISIBLE){
                            binding.distance3.visibility = View.VISIBLE
                            binding.distance4.visibility = View.INVISIBLE
                        }
                        if(binding.normal5.visibility == View.VISIBLE){
                            binding.normal4.visibility = View.VISIBLE
                            binding.normal5.visibility = View.INVISIBLE
                        }else if(binding.distance5.visibility == View.VISIBLE){
                            binding.distance4.visibility = View.VISIBLE
                            binding.distance5.visibility = View.INVISIBLE
                        }
                    }
                    synchronized(mySoldierList){
                        val now = android.os.SystemClock.uptimeMillis()
                        if(soldierType == "normal_soldier"){
                            mySoldierList.add(SoldierInfo("normal_soldier",dpToPx(myHomePosition),"walk",now,normalSoldierAttackPoint,normalSoldierHp,15))
                        }
                        if(soldierType == "distance_soldier"){
                            mySoldierList.add(SoldierInfo("distance_soldier",dpToPx(myHomePosition),"walk",now,distanceSoldierAttackPoint,distanceSoldierHp,25))
                        }
                    }
                }
                Thread.sleep(1000)
            }
        }.start()
        Thread{
            //적의 병사 생산을 담당함
            while(!finished){
                while(!paused){
                    Thread.sleep(3000)
                    if(enemyQueue.size < 5){
                        if(enemyMoney >= 25){
                            val num = Random().nextInt(9)
                            if(num <= 5){
                                enemyMoney -= 15
                                enemyQueue.add("normal_soldier")
                            }else{
                                enemyMoney -= 25
                                enemyQueue.add("distance_soldier")
                            }
                        }else if(enemyMoney >= 15){
                            enemyMoney -= 15
                            enemyQueue.add("normal_soldier")
                        }
                    }
                }
                Thread.sleep(1000)
            }
        }.start()
        Thread{
            //적의 병사 생산 대기열 관리
            while(!finished){
                while(!paused){
                    val soldierType = enemyQueue.take()
                    while(enemyProgress < wholeProgress) {
                        enemyProgress++
                        Thread.sleep(50)
                    }
                    enemyProgress = 0
                    synchronized(enemySoldierList){
                        val now = android.os.SystemClock.uptimeMillis()
                        if(soldierType == "normal_soldier"){
                            enemySoldierList.add(SoldierInfo("normal_soldier",dpToPx(enemyHomePosition),"walk",now,normalSoldierAttackPoint,normalSoldierHp,15))
                        }
                        if(soldierType == "distance_soldier"){
                            enemySoldierList.add(SoldierInfo("distance_soldier",dpToPx(enemyHomePosition),"walk",now,distanceSoldierAttackPoint,distanceSoldierHp,25))
                        }
                    }
                }
                Thread.sleep(1000)
            }
        }.start()

        binding.myHpBar.post{
            binding.myHpBar.layoutParams.width = binding.myHpBarFrame.width
            binding.myHpBar.requestLayout()
        }
        binding.enemyHpBar.post{
            binding.enemyHpBar.layoutParams.width = binding.enemyHpBarFrame.width
            binding.enemyHpBar.requestLayout()
        }
        binding.pauseBtn.setOnClickListener{
            paused = true
            binding.gameView.setPlayed(false)
            binding.resumeLayout.visibility = View.VISIBLE
            backgroundMusic!!.pause()
        }
        binding.resumeLayout.setOnClickListener{
            paused = false
            binding.gameView.setPlayed(true)
            binding.resumeLayout.visibility = View.INVISIBLE
            backgroundMusic!!.start()
        }
        binding.gameView.setMySoldierList(mySoldierList)
        binding.gameView.setEnemySoldierList(enemySoldierList)
        binding.btnMakeNormalSoldier.setOnClickListener{
            if(myQueue.size < 5){
                if(myMoney >= 15){
                    myMoney -= 15
                    myQueue.add("normal_soldier")
                    runOnUiThread{
                        binding.showMoney.text = myMoney.toString()
                        if(isAllInvisible(1)){
                            binding.normal1.visibility = View.VISIBLE
                        }else if(isAllInvisible(2)){
                            binding.normal2.visibility = View.VISIBLE
                        }else if(isAllInvisible(3)){
                            binding.normal3.visibility = View.VISIBLE
                        }else if(isAllInvisible(4)){
                            binding.normal4.visibility = View.VISIBLE
                        }else if(isAllInvisible(5)){
                            binding.normal5.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        binding.btnMakeDistanceSoldier.setOnClickListener{
            if(myQueue.size < 5){
                if(myMoney >= 25){
                    myMoney -= 25
                    myQueue.add("distance_soldier")
                    runOnUiThread{
                        binding.showMoney.text = myMoney.toString()
                        if(isAllInvisible(1)){
                            binding.distance1.visibility = View.VISIBLE
                        }else if(isAllInvisible(2)){
                            binding.distance2.visibility = View.VISIBLE
                        }else if(isAllInvisible(3)){
                            binding.distance3.visibility = View.VISIBLE
                        }else if(isAllInvisible(4)){
                            binding.distance4.visibility = View.VISIBLE
                        }else if(isAllInvisible(5)){
                            binding.distance5.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TMP","onDestroy called")
    }

    fun pxToDp(px: Int): Float {
        return (px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun isAllInvisible(position:Int):Boolean{
        if(position == 1){
            return binding.normal1.visibility == View.INVISIBLE && binding.distance1.visibility == View.INVISIBLE
        }else if(position == 2){
            return binding.normal2.visibility == View.INVISIBLE && binding.distance2.visibility == View.INVISIBLE
        }else if(position == 3){
            return binding.normal3.visibility == View.INVISIBLE && binding.distance3.visibility == View.INVISIBLE
        }else if(position == 4){
            return binding.normal4.visibility == View.INVISIBLE && binding.distance4.visibility == View.INVISIBLE
        }else if(position == 5){
            return binding.normal5.visibility == View.INVISIBLE && binding.distance5.visibility == View.INVISIBLE
        }
        return true
    }

    fun dpToPx(dp: Int): Float {
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}