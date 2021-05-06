package com.example.ageofwar

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ageofwar.databinding.ActivityMainBinding
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.ArrayList
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : AppCompatActivity() {
    private lateinit var context: Context

    private val myHomePosition = 20//dp단위임
    private val enemyHomePosition = -180//dp단위임
    private lateinit var displayMetrics: DisplayMetrics
    private lateinit var binding: ActivityMainBinding
    private var mySoldierList: ArrayList<SoldierInfo> = ArrayList()
    private var enemySoldierList: ArrayList<SoldierInfo> = ArrayList()
    private var fireStoneList: ArrayList<FireStoneInfo> = ArrayList()
    private val soldierBetweenThreshold = -85

    private val normalSoldierAttackPoint = 1
    private val normalSoldierHp = 10
    private val distanceSoldierAttackPoint = 1
    private val distanceSoldierHp = 10
    private val dinosaurSoldierAttackPoint = 2
    private val dinosaurSoldierHp = 10

    private val wholeProgress = 25
    private val wholeHp = 100
    private var myMoney:AtomicInteger = AtomicInteger(500)
    private var myProgress:AtomicInteger = AtomicInteger(0)
    private var myQueue: BlockingQueue<String> = ArrayBlockingQueue<String>(5)
    private var myHp:AtomicInteger = AtomicInteger(wholeHp)
    private var enemyMoney:AtomicInteger = AtomicInteger(450)
    private var enemyProgress:AtomicInteger = AtomicInteger(0)
    private var enemyQueue: BlockingQueue<String> = ArrayBlockingQueue<String>(5)
    private var enemyHp:AtomicInteger = AtomicInteger(wholeHp)

    private var finished:AtomicBoolean = AtomicBoolean(false)
    private var paused:AtomicBoolean = AtomicBoolean(false)

    private var backgroundMusic: MediaPlayer? = null
    private val moneyRatio = 0.7//난이도 조절을 위해 적이 나의 병사를 죽일 때 얻는 돈의 비율 조정

    private val multiAttackRemainTime = 20
    private var myMultiAttack:AtomicInteger = AtomicInteger(0)//0은 공격을 하고 있지 않은 상태로 공격이 가능하다 1~5은 공격을 하고 있는 상태 음수는 공격이 불가능한 상태
    private var myMultiAttackRemainTime:AtomicInteger = AtomicInteger(0)
    private var enemyMultiAttack:AtomicInteger = AtomicInteger(0)//0은 공격을 하고 있지 않은 상태로 공격이 가능하다 1~5은 공격을 하고 있는 상태 음수는 공격이 불가능한 상태
    private var enemyMultiAttackRemainTime:AtomicInteger = AtomicInteger(0)
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
            while (!finished.get()) {
                while (!paused.get()) {
                    synchronized(mySoldierList) {
                        synchronized(enemySoldierList) {
                            for (i in mySoldierList.indices) {
                                if (i == 0) {
                                    if ((mySoldierList[0].soldierType != "dinosaur_soldier" && mySoldierList[0].soldierPosition >= 560) ||
                                        (mySoldierList[0].soldierType == "dinosaur_soldier" && mySoldierList[0].soldierPosition >= 500)
                                    ) {
                                        if (mySoldierList[0].soldierAnimationType != "attack") {
                                            mySoldierList[0].soldierAnimationType = "attack"
                                            mySoldierList[0].soldierAnimationStartTime = 0L
                                        }
                                        enemyHp.set(enemyHp.get()-mySoldierList[0].attackPoint)
                                        runOnUiThread {
                                            binding.enemyHpBar.layoutParams.width =
                                                (binding.enemyHpBarFrame.width * (enemyHp.get() * 1.0f / wholeHp)).toInt()
                                            binding.enemyHpBar.requestLayout()
                                        }
                                        if (enemyHp.get() < 0) {
                                            val intent =
                                                Intent(context, VictoryActivity::class.java)
                                            context.startActivity(intent)
                                            (context as MainActivity).finish()
                                            finished.set(true)
                                            paused.set(true)
                                        }
                                    } else if (enemySoldierList.size > 0) {
                                        var soldierBetween = 0
                                        var distanceSoldierBetween = 0.0
                                        if (mySoldierList[0].soldierType != "dinosaur_soldier" && enemySoldierList[0].soldierType != "dinosaur_soldier") {
                                            soldierBetween = soldierBetweenThreshold
                                            distanceSoldierBetween = soldierBetweenThreshold * 1.4
                                        } else if (mySoldierList[0].soldierType == "dinosaur_soldier" && enemySoldierList[0].soldierType == "dinosaur_soldier") {
                                            soldierBetween = -230
                                        } else {
                                            soldierBetween = -150
                                            distanceSoldierBetween = soldierBetweenThreshold * 2.2
                                        }
                                        if (mySoldierList[0].soldierPosition + enemySoldierList[0].soldierPosition > soldierBetween) {
                                            if (mySoldierList[0].soldierAnimationType != "attack") {
                                                mySoldierList[0].soldierAnimationType = "attack"
                                                mySoldierList[0].soldierAnimationStartTime = 0L
                                            }
                                            enemySoldierList[0].hp -= mySoldierList[0].attackPoint
                                            if (enemySoldierList[0].hp < 0) {
                                                myMoney.set(myMoney.get()+enemySoldierList[0].getMoney)
                                                runOnUiThread {
                                                    binding.showMoney.text = myMoney.toString()
                                                }
                                                enemySoldierList.removeAt(0)
                                            }
                                        } else if (mySoldierList[0].soldierType == "distance_soldier" && mySoldierList[0].soldierPosition + enemySoldierList[0].soldierPosition > distanceSoldierBetween) {
                                            if (mySoldierList[0].soldierAnimationType != "attack_distance") {
                                                mySoldierList[0].soldierAnimationType =
                                                    "attack_distance"
                                                mySoldierList[0].soldierAnimationStartTime = 0L
                                            }
                                            enemySoldierList[0].hp -= mySoldierList[0].attackPoint
                                            if (enemySoldierList[0].hp < 0) {
                                                myMoney.set(myMoney.get()+enemySoldierList[0].getMoney)
                                                runOnUiThread {
                                                    binding.showMoney.text = myMoney.toString()
                                                }
                                                enemySoldierList.removeAt(0)
                                            }
                                        } else {
                                            if (mySoldierList[0].soldierAnimationType != "walk") {
                                                mySoldierList[0].soldierAnimationType = "walk"
                                                mySoldierList[0].soldierAnimationStartTime = 0L
                                            }
                                            mySoldierList[0].soldierPosition += dpToPx(1)
                                        }
                                    } else {
                                        if (mySoldierList[0].soldierAnimationType != "walk") {
                                            mySoldierList[0].soldierAnimationType = "walk"
                                            mySoldierList[0].soldierAnimationStartTime = 0L
                                        }
                                        mySoldierList[0].soldierPosition += dpToPx(1)
                                    }
                                } else {
                                    var soldierBetween = 0
                                    if (mySoldierList[i - 1].soldierType != "dinosaur_soldier" && mySoldierList[i].soldierType != "dinosaur_soldier") {
                                        soldierBetween = 30
                                    } else if (mySoldierList[i - 1].soldierType == "dinosaur_soldier" && mySoldierList[i].soldierType == "dinosaur_soldier") {
                                        soldierBetween = 80
                                    } else if (mySoldierList[i - 1].soldierType == "dinosaur_soldier" && mySoldierList[i].soldierType != "dinosaur_soldier") {
                                        soldierBetween = 20
                                    } else if (mySoldierList[i - 1].soldierType != "dinosaur_soldier" && mySoldierList[i].soldierType == "dinosaur_soldier") {
                                        soldierBetween = 100
                                    }
                                    if (mySoldierList[i - 1].soldierPosition - mySoldierList[i].soldierPosition >= soldierBetween) {
                                        if (mySoldierList[i].soldierAnimationType != "walk") {
                                            mySoldierList[i].soldierAnimationType = "walk"
                                            mySoldierList[i].soldierAnimationStartTime = 0L
                                        }
                                        mySoldierList[i].soldierPosition += dpToPx(1)
                                    } else {
                                        if (mySoldierList[i].soldierAnimationType != "stop") {
                                            mySoldierList[i].soldierAnimationType = "stop"
                                            mySoldierList[i].soldierAnimationStartTime = 0L
                                        }
                                    }
                                }
                            }//여기까지가 나의 움직임 및 공격을 관리함

                            for (i in enemySoldierList.indices) {
                                if (i == 0) {
                                    if ((enemySoldierList[0].soldierType != "dinosaur_soldier" && enemySoldierList[0].soldierPosition >= -120) ||
                                        (enemySoldierList[0].soldierType == "dinosaur_soldier" && enemySoldierList[0].soldierPosition >= -180)
                                    ) {
                                        if (enemySoldierList[0].soldierAnimationType != "attack") {
                                            enemySoldierList[0].soldierAnimationType = "attack"
                                            enemySoldierList[0].soldierAnimationStartTime = 0L
                                        }
                                        myHp.set(myHp.get()-enemySoldierList[0].attackPoint)
                                        runOnUiThread {
                                            binding.myHpBar.layoutParams.width =
                                                (binding.myHpBarFrame.width * (myHp.get() * 1.0f / wholeHp)).toInt()
                                            binding.myHpBar.requestLayout()
                                        }
                                        if (myHp.get() < 0) {
                                            val intent = Intent(context, DefeatActivity::class.java)
                                            context.startActivity(intent)
                                            (context as MainActivity).finish()
                                            finished.set(true)
                                            paused.set(true)
                                        }
                                    } else if (mySoldierList.size > 0) {
                                        var soldierBetween = 0
                                        var distanceSoldierBetween = 0.0
                                        if (mySoldierList[0].soldierType != "dinosaur_soldier" && enemySoldierList[0].soldierType != "dinosaur_soldier") {
                                            soldierBetween = soldierBetweenThreshold
                                            distanceSoldierBetween = soldierBetweenThreshold * 1.4
                                        } else if (mySoldierList[0].soldierType == "dinosaur_soldier" && enemySoldierList[0].soldierType == "dinosaur_soldier") {
                                            soldierBetween = -230
                                        } else {
                                            soldierBetween = -150
                                            distanceSoldierBetween = soldierBetweenThreshold * 2.2
                                        }
                                        if (mySoldierList[0].soldierPosition + enemySoldierList[0].soldierPosition > soldierBetween) {
                                            if (enemySoldierList[0].soldierAnimationType != "attack") {
                                                enemySoldierList[0].soldierAnimationType = "attack"
                                                enemySoldierList[0].soldierAnimationStartTime = 0L
                                            }
                                            mySoldierList[0].hp -= enemySoldierList[0].attackPoint
                                            if (mySoldierList[0].hp < 0) {
                                                enemyMoney.set(enemyMoney.get()+(mySoldierList[0].getMoney * moneyRatio).toInt())
                                                mySoldierList.removeAt(0)
                                            }
                                        } else if (enemySoldierList[0].soldierType == "distance_soldier" && mySoldierList[0].soldierPosition + enemySoldierList[0].soldierPosition > distanceSoldierBetween) {
                                            if (enemySoldierList[0].soldierAnimationType != "attack_distance") {
                                                enemySoldierList[0].soldierAnimationType =
                                                    "attack_distance"
                                                enemySoldierList[0].soldierAnimationStartTime = 0L
                                            }
                                            mySoldierList[0].hp -= enemySoldierList[0].attackPoint
                                            if (mySoldierList[0].hp < 0) {
                                                enemyMoney.set(enemyMoney.get()+(mySoldierList[0].getMoney * moneyRatio).toInt())
                                                mySoldierList.removeAt(0)
                                            }
                                        } else {
                                            if (enemySoldierList[0].soldierAnimationType != "walk") {
                                                enemySoldierList[0].soldierAnimationType = "walk"
                                                enemySoldierList[0].soldierAnimationStartTime = 0L
                                            }
                                            enemySoldierList[0].soldierPosition += dpToPx(1)
                                        }
                                    } else {
                                        if (enemySoldierList[0].soldierAnimationType != "walk") {
                                            enemySoldierList[0].soldierAnimationType = "walk"
                                            enemySoldierList[0].soldierAnimationStartTime = 0L
                                        }
                                        enemySoldierList[0].soldierPosition += dpToPx(1)
                                    }
                                } else {
                                    var soldierBetween = 0
                                    if (enemySoldierList[i - 1].soldierType != "dinosaur_soldier" && enemySoldierList[i].soldierType != "dinosaur_soldier") {
                                        soldierBetween = 30
                                    } else if (enemySoldierList[i - 1].soldierType == "dinosaur_soldier" && enemySoldierList[i].soldierType == "dinosaur_soldier") {
                                        soldierBetween = 80
                                    } else if (enemySoldierList[i - 1].soldierType == "dinosaur_soldier" && enemySoldierList[i].soldierType != "dinosaur_soldier") {
                                        soldierBetween = 20
                                    } else if (enemySoldierList[i - 1].soldierType != "dinosaur_soldier" && enemySoldierList[i].soldierType == "dinosaur_soldier") {
                                        soldierBetween = 100
                                    }
                                    if (enemySoldierList[i - 1].soldierPosition - enemySoldierList[i].soldierPosition >= soldierBetween) {
                                        if (enemySoldierList[i].soldierAnimationType != "walk") {
                                            enemySoldierList[i].soldierAnimationType = "walk"
                                            enemySoldierList[i].soldierAnimationStartTime = 0L
                                        }
                                        enemySoldierList[i].soldierPosition += dpToPx(1)
                                    } else {
                                        if (enemySoldierList[i].soldierAnimationType != "stop") {
                                            enemySoldierList[i].soldierAnimationType = "stop"
                                            enemySoldierList[i].soldierAnimationStartTime = 0L
                                        }
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
        Thread {
            //my progressbar 관리 관련
            while (!finished.get()) {
                while (!paused.get()) {
                    val soldierType = myQueue.take()
                    while (myProgress.get() < wholeProgress) {
                        myProgress.set(myProgress.get()+1)
                        runOnUiThread {
                            binding.showProgressBar.layoutParams.width =
                                (binding.showProgress.width * (myProgress.get() * 1.0f / wholeProgress)).toInt()
                            binding.showProgressBar.requestLayout()
                        }
                        Thread.sleep(50)
                    }
                    myProgress.set(0)
                    runOnUiThread {
                        binding.showProgressBar.layoutParams.width = 0
                        binding.showProgressBar.requestLayout()
                        //Log.d("QUEUE",myQueue.size.toString())
                        if (myQueue.size == 0) {
                            binding.normal1.visibility = View.INVISIBLE
                            binding.distance1.visibility = View.INVISIBLE
                            binding.dinosaur1.visibility = View.INVISIBLE
                        }

                        if (binding.normal2.visibility == View.VISIBLE) {
                            binding.normal1.visibility = View.VISIBLE
                            binding.normal2.visibility = View.INVISIBLE
                        } else if (binding.distance2.visibility == View.VISIBLE) {
                            binding.distance1.visibility = View.VISIBLE
                            binding.distance2.visibility = View.INVISIBLE
                        } else if (binding.dinosaur2.visibility == View.VISIBLE) {
                            binding.dinosaur1.visibility = View.VISIBLE
                            binding.dinosaur2.visibility = View.INVISIBLE
                        }

                        if (binding.normal3.visibility == View.VISIBLE) {
                            binding.normal2.visibility = View.VISIBLE
                            binding.normal3.visibility = View.INVISIBLE
                        } else if (binding.distance3.visibility == View.VISIBLE) {
                            binding.distance2.visibility = View.VISIBLE
                            binding.distance3.visibility = View.INVISIBLE
                        } else if (binding.dinosaur3.visibility == View.VISIBLE) {
                            binding.dinosaur2.visibility = View.VISIBLE
                            binding.dinosaur3.visibility = View.INVISIBLE
                        }

                        if (binding.normal4.visibility == View.VISIBLE) {
                            binding.normal3.visibility = View.VISIBLE
                            binding.normal4.visibility = View.INVISIBLE
                        } else if (binding.distance4.visibility == View.VISIBLE) {
                            binding.distance3.visibility = View.VISIBLE
                            binding.distance4.visibility = View.INVISIBLE
                        } else if (binding.dinosaur4.visibility == View.VISIBLE) {
                            binding.dinosaur3.visibility = View.VISIBLE
                            binding.dinosaur4.visibility = View.INVISIBLE
                        }

                        if (binding.normal5.visibility == View.VISIBLE) {
                            binding.normal4.visibility = View.VISIBLE
                            binding.normal5.visibility = View.INVISIBLE
                        } else if (binding.distance5.visibility == View.VISIBLE) {
                            binding.distance4.visibility = View.VISIBLE
                            binding.distance5.visibility = View.INVISIBLE
                        } else if (binding.dinosaur5.visibility == View.VISIBLE) {
                            binding.dinosaur4.visibility = View.VISIBLE
                            binding.dinosaur5.visibility = View.INVISIBLE
                        }
                    }
                    synchronized(mySoldierList) {
                        val now = android.os.SystemClock.uptimeMillis()
                        if (soldierType == "normal_soldier") {
                            mySoldierList.add(
                                SoldierInfo(
                                    "normal_soldier",
                                    dpToPx(myHomePosition),
                                    "walk",
                                    now,
                                    normalSoldierAttackPoint,
                                    normalSoldierHp,
                                    15
                                )
                            )
                        }
                        if (soldierType == "distance_soldier") {
                            mySoldierList.add(
                                SoldierInfo(
                                    "distance_soldier",
                                    dpToPx(myHomePosition),
                                    "walk",
                                    now,
                                    distanceSoldierAttackPoint,
                                    distanceSoldierHp,
                                    25
                                )
                            )
                        }
                        if (soldierType == "dinosaur_soldier") {
                            mySoldierList.add(
                                SoldierInfo(
                                    "dinosaur_soldier",
                                    dpToPx(myHomePosition),
                                    "walk",
                                    now,
                                    dinosaurSoldierAttackPoint,
                                    dinosaurSoldierHp,
                                    50
                                )
                            )
                        }
                    }
                    Thread.sleep(100)
                }
                Thread.sleep(1000)
            }
        }.start()
        Thread {
            //적의 병사 생산을 담당함
            while (!finished.get()) {
                while (!paused.get()) {
                    Thread.sleep(500)
                    if (enemyQueue.size < 5) {
                        if (enemyMoney.get() >= 50) {
                            val num = Random().nextInt(15)
                            if (num <= 7) {
                                enemyMoney.set(enemyMoney.get()-15)
                                enemyQueue.add("normal_soldier")
                            } else if (num <= 10) {
                                enemyMoney.set(enemyMoney.get()-25)
                                enemyQueue.add("distance_soldier")
                            } else {
                                enemyMoney.set(enemyMoney.get()-50)
                                enemyQueue.add("dinosaur_soldier")
                            }
                        } else if (enemyMoney.get() >= 25) {
                            val num = Random().nextInt(9)
                            if (num <= 5) {
                                enemyMoney.set(enemyMoney.get()-15)
                                enemyQueue.add("normal_soldier")
                            } else {
                                enemyMoney.set(enemyMoney.get()-25)
                                enemyQueue.add("distance_soldier")
                            }
                        } else if (enemyMoney.get() >= 15) {
                            enemyMoney.set(enemyMoney.get()-15)
                            enemyQueue.add("normal_soldier")
                        }
                    }
                }
                Thread.sleep(1000)
            }
        }.start()
        Thread {
            //적의 병사 생산 대기열 관리
            while (!finished.get()) {
                while (!paused.get()) {
                    val soldierType = enemyQueue.take()
                    while (enemyProgress.get() < wholeProgress) {
                        enemyProgress.set(enemyProgress.get()+1)
                        Thread.sleep(50)
                    }
                    enemyProgress.set(0)
                    synchronized(enemySoldierList) {
                        val now = android.os.SystemClock.uptimeMillis()
                        if (soldierType == "normal_soldier") {
                            enemySoldierList.add(
                                SoldierInfo(
                                    "normal_soldier",
                                    dpToPx(enemyHomePosition),
                                    "walk",
                                    now,
                                    normalSoldierAttackPoint,
                                    normalSoldierHp,
                                    15
                                )
                            )
                        }
                        if (soldierType == "distance_soldier") {
                            enemySoldierList.add(
                                SoldierInfo(
                                    "distance_soldier",
                                    dpToPx(enemyHomePosition),
                                    "walk",
                                    now,
                                    distanceSoldierAttackPoint,
                                    distanceSoldierHp,
                                    25
                                )
                            )
                        }
                        if (soldierType == "dinosaur_soldier") {
                            enemySoldierList.add(
                                SoldierInfo(
                                    "dinosaur_soldier",
                                    dpToPx(enemyHomePosition),
                                    "walk",
                                    now,
                                    dinosaurSoldierAttackPoint,
                                    dinosaurSoldierHp,
                                    50
                                )
                            )
                        }
                    }
                    Thread.sleep(100)
                }
                Thread.sleep(1000)
            }
        }.start()
        Thread {
            //fireStone의 움직임을 관리함
            while (!finished.get()) {
                while (!paused.get()) {
                    /*x는 0~750dp
                    y는 300dp
                    rotation(rot)은 -90~-140, -220~-270
                    로 시작해야 함
                     */
                    synchronized(fireStoneList){
                        if (fireStoneList.isNotEmpty()) {
                            for (f in fireStoneList) {
                                if (f.rot >= -180) {
                                    f.x += cos((f.rot * Math.PI / 180).toFloat()) * 100
                                    f.y += sin((f.rot * Math.PI / 180).toFloat()) * 100
                                } else {
                                    f.x -= cos((f.rot * Math.PI / 180).toFloat()) * 100
                                    f.y -= sin((f.rot * Math.PI / 180).toFloat()) * 100
                                }

                            }
                            fireStoneList.removeIf { f -> f.y <= 0 }
                        }
                    }
                    Thread.sleep(200)
                }
                Thread.sleep(1000)
            }
        }.start()
        Thread {
            //나의 멀티 공격 관련
            while (!finished.get()) {
                while (!paused.get()) {
                    /*x는 0~750dp
                    y는 300dp
                    rotation(rot)은 -90~-140, -220~-270
                    로 시작해야 함
                     */
                    if (myMultiAttack.get() in 1..5) {
                        for (i in 1..5) {
                            val xRand = dpToPx((0..750).random())
                            val y = dpToPx(300)
                            val rotCaseRand = (0..1).random()
                            var rotRand = 0
                            if (rotCaseRand == 0) {
                                rotRand = -((90..140).random())
                            } else {
                                rotRand = -((220..270).random())
                            }
                            synchronized(fireStoneList) {
                                fireStoneList.add(FireStoneInfo(y, xRand, rotRand))
                            }
                        }
                        myMultiAttack.set(myMultiAttack.get()+1)
                        synchronized(enemySoldierList) {
                            if (enemySoldierList.size < 2) {
                                enemySoldierList.clear()
                            } else {
                                for (i in 1..2) {
                                    enemySoldierList.removeAt((0 until enemySoldierList.size).random())
                                }
                            }
                        }
                    }
                    Thread.sleep(2000)
                }
                Thread.sleep(1000)
            }
        }.start()
        Thread {
            //나의 멀티 공격 대기와 관련이 있음
            while (!finished.get()) {
                while (!paused.get()) {
                    if (myMultiAttackRemainTime.get() > 0) {
                        runOnUiThread {
                            binding.shadowMultiAttack.layoutParams.height =
                                (binding.btnMultiAttack.height * (myMultiAttackRemainTime.get() * 1.0f / multiAttackRemainTime)).toInt()
                            binding.shadowMultiAttack.requestLayout()
                        }
                        myMultiAttackRemainTime.set(myMultiAttackRemainTime.get()-1)
                    }else if(myMultiAttackRemainTime.get() == 0){
                        runOnUiThread {
                            binding.shadowMultiAttack.layoutParams.height = 0
                            binding.shadowMultiAttack.requestLayout()
                        }
                    }
                    Thread.sleep(3000)
                }
                Thread.sleep(1000)
            }
        }.start()
        Thread {
            //적의 멀티 공격 관련
            while (!finished.get()) {
                while (!paused.get()) {
                    /*x는 0~750dp
                    y는 300dp
                    rotation(rot)은 -90~-140, -220~-270
                    로 시작해야 함
                     */
                    if (enemyMultiAttack.get() in 1..5) {
                        for (i in 1..5) {
                            val xRand = dpToPx((0..750).random())
                            val y = dpToPx(300)
                            val rotCaseRand = (0..1).random()
                            var rotRand = 0
                            if (rotCaseRand == 0) {
                                rotRand = -((90..140).random())
                            } else {
                                rotRand = -((220..270).random())
                            }
                            synchronized(fireStoneList) {
                                fireStoneList.add(FireStoneInfo(y, xRand, rotRand))
                            }
                        }
                        enemyMultiAttack.set(enemyMultiAttack.get()+1)
                        synchronized(mySoldierList) {
                            if (mySoldierList.size < 2) {
                                mySoldierList.clear()
                            } else {
                                for (i in 1..2) {
                                    mySoldierList.removeAt((0 until mySoldierList.size).random())
                                }
                            }
                        }
                    }
                    Thread.sleep(2000)
                }
                Thread.sleep(1000)
            }
        }.start()
        Thread {
            //적의 멀티 공격 대기와 관련이 있음
            while (!finished.get()) {
                while (!paused.get()) {
                    if (enemyMultiAttackRemainTime.get() > 0) {
                        enemyMultiAttackRemainTime.set(enemyMultiAttackRemainTime.get()-1)
                    }
                    Thread.sleep(3000)
                }
                Thread.sleep(1000)
            }
        }.start()
        Thread {
            //적의 멀티 공격할지 여부를 판단
            while (!finished.get()) {
                while (!paused.get()) {
                    if (enemyMultiAttackRemainTime.get() == 0 && (myMultiAttack.get() == 0 || myMultiAttack.get() > 5) && (1..10).random() <= 2) {
                        enemyMultiAttack.set(1)
                        enemyMultiAttackRemainTime.set(multiAttackRemainTime)
                    }
                    Thread.sleep(3000)
                }
                Thread.sleep(1000)
            }
        }.start()
        binding.myHpBar.post {
            binding.myHpBar.layoutParams.width = binding.myHpBarFrame.width
            binding.myHpBar.requestLayout()
        }
        binding.enemyHpBar.post {
            binding.enemyHpBar.layoutParams.width = binding.enemyHpBarFrame.width
            binding.enemyHpBar.requestLayout()
        }
        binding.pauseBtn.setOnClickListener {
            paused.set(true)
            binding.gameView.setPlayed(false)
            binding.resumeLayout.visibility = View.VISIBLE
            backgroundMusic!!.pause()
        }
        binding.resumeLayout.setOnClickListener {
            paused.set(false)
            binding.gameView.setPlayed(true)
            binding.resumeLayout.visibility = View.INVISIBLE
            backgroundMusic!!.start()
        }
        binding.gameView.setMySoldierList(mySoldierList)
        binding.gameView.setEnemySoldierList(enemySoldierList)
        binding.gameView.setFireStoneList(fireStoneList)
        binding.btnMakeNormalSoldier.setOnClickListener {
            if (myQueue.size < 5) {
                if (myMoney.get() >= 15) {
                    myMoney.set(myMoney.get()-15)
                    myQueue.add("normal_soldier")
                    runOnUiThread {
                        binding.showMoney.text = myMoney.toString()
                        if (isAllInvisible(1)) {
                            binding.normal1.visibility = View.VISIBLE
                        } else if (isAllInvisible(2)) {
                            binding.normal2.visibility = View.VISIBLE
                        } else if (isAllInvisible(3)) {
                            binding.normal3.visibility = View.VISIBLE
                        } else if (isAllInvisible(4)) {
                            binding.normal4.visibility = View.VISIBLE
                        } else if (isAllInvisible(5)) {
                            binding.normal5.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        binding.btnMakeDistanceSoldier.setOnClickListener {
            if (myQueue.size < 5) {
                if (myMoney.get() >= 25) {
                    myMoney.set(myMoney.get()-25)
                    myQueue.add("distance_soldier")
                    runOnUiThread {
                        binding.showMoney.text = myMoney.toString()
                        if (isAllInvisible(1)) {
                            binding.distance1.visibility = View.VISIBLE
                        } else if (isAllInvisible(2)) {
                            binding.distance2.visibility = View.VISIBLE
                        } else if (isAllInvisible(3)) {
                            binding.distance3.visibility = View.VISIBLE
                        } else if (isAllInvisible(4)) {
                            binding.distance4.visibility = View.VISIBLE
                        } else if (isAllInvisible(5)) {
                            binding.distance5.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        binding.btnMakeDinosaurSoldier.setOnClickListener {
            if (myQueue.size < 5) {
                if (myMoney.get() >= 50) {
                    myMoney.set(myMoney.get()-50)
                    myQueue.add("dinosaur_soldier")
                    runOnUiThread {
                        binding.showMoney.text = myMoney.toString()
                        if (isAllInvisible(1)) {
                            binding.dinosaur1.visibility = View.VISIBLE
                        } else if (isAllInvisible(2)) {
                            binding.dinosaur2.visibility = View.VISIBLE
                        } else if (isAllInvisible(3)) {
                            binding.dinosaur3.visibility = View.VISIBLE
                        } else if (isAllInvisible(4)) {
                            binding.dinosaur4.visibility = View.VISIBLE
                        } else if (isAllInvisible(5)) {
                            binding.dinosaur5.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        binding.shadowMultiAttack.post {
            binding.shadowMultiAttack.layoutParams.height = 0
            binding.shadowMultiAttack.requestLayout()
        }
        binding.btnMultiAttack.setOnClickListener {
            if (myMultiAttackRemainTime.get() == 0 && (enemyMultiAttack.get() == 0 || enemyMultiAttack.get() > 5)) {
                myMultiAttack.set(1)
                myMultiAttackRemainTime.set(multiAttackRemainTime)
                runOnUiThread {
                    binding.shadowMultiAttack.layoutParams.height = binding.btnMultiAttack.height
                    binding.shadowMultiAttack.requestLayout()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(paused.get()){
            binding.resumeLayout.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        paused.set(true)
        binding.gameView.setPlayed(false)
        backgroundMusic!!.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(backgroundMusic != null){
            backgroundMusic!!.stop()
            backgroundMusic!!.release()
            backgroundMusic = null
        }
        Log.d("TMP", "onDestroy called")
    }

    fun pxToDp(px: Int): Float {
        return (px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun isAllInvisible(position: Int): Boolean {
        if (position == 1) {
            return binding.normal1.visibility == View.INVISIBLE && binding.distance1.visibility == View.INVISIBLE && binding.dinosaur1.visibility == View.INVISIBLE
        } else if (position == 2) {
            return binding.normal2.visibility == View.INVISIBLE && binding.distance2.visibility == View.INVISIBLE && binding.dinosaur2.visibility == View.INVISIBLE
        } else if (position == 3) {
            return binding.normal3.visibility == View.INVISIBLE && binding.distance3.visibility == View.INVISIBLE && binding.dinosaur3.visibility == View.INVISIBLE
        } else if (position == 4) {
            return binding.normal4.visibility == View.INVISIBLE && binding.distance4.visibility == View.INVISIBLE && binding.dinosaur4.visibility == View.INVISIBLE
        } else if (position == 5) {
            return binding.normal5.visibility == View.INVISIBLE && binding.distance5.visibility == View.INVISIBLE && binding.dinosaur5.visibility == View.INVISIBLE
        }
        return true
    }

    fun dpToPx(dp: Int): Float {
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}