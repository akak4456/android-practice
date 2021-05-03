package com.example.ageofwar

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import com.example.ageofwar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val myHomePosition = 20//dp단위임
    private val enemyHomePosition = -180//dp단위임
    private lateinit var displayMetrics: DisplayMetrics
    private lateinit var binding:ActivityMainBinding
    private var mySoldierList:ArrayList<SoldierInfo> = ArrayList()
    private var enemySoldierList:ArrayList<SoldierInfo> = ArrayList()
    private val soldierBetweenThreshold = -85
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        displayMetrics = this.resources.displayMetrics
        mySoldierList.add(SoldierInfo("normal_soldier",dpToPx(myHomePosition),"walk",0L))

        enemySoldierList.add(SoldierInfo("normal_soldier",dpToPx(enemyHomePosition),"walk",0L))
        Thread {
            //soldier 움직임을 관리한다
            while(true){
                synchronized(mySoldierList) {
//                    if(mySoldierList.size > 0){
//                        if(enemySoldierList.size > 0 && mySoldierList[0].soldierPosition + enemySoldierList[0].soldierPosition > soldierBetweenThreshold){
//                            //공격을 해야만 한다
//                            if(mySoldierList[0].soldierAnimationType == "walk"){
//                                mySoldierList[0].soldierAnimationType = "attack"
//                                mySoldierList[0].soldierAnimationStartTime = 0L
//                            }
//                        }else{
//                            //계속 움직여야 한다
//                            for (mySoldier in mySoldierList) {
//                                mySoldier.soldierPosition += dpToPx(1)
//                            }
//                        }
//                    }
                    for(i in mySoldierList.indices){
                        if(i == 0){
                            if(enemySoldierList.size > 0 && mySoldierList[0].soldierPosition + enemySoldierList[0].soldierPosition > soldierBetweenThreshold){
                                if(mySoldierList[0].soldierAnimationType == "walk"){
                                    mySoldierList[0].soldierAnimationType = "attack"
                                    mySoldierList[0].soldierAnimationStartTime = 0L
                                }
                            }else{
                                mySoldierList[0].soldierPosition += dpToPx(1)
                            }
                        }else{
                            if(mySoldierList[i-1].soldierPosition-mySoldierList[i].soldierPosition >= 30){
                                mySoldierList[i].soldierPosition += dpToPx(1)
                            }
                        }
                    }
                    Thread.sleep(200)
                }
                synchronized(enemySoldierList){
                    if(enemySoldierList.size > 0){
                        if(mySoldierList.size > 0 && mySoldierList[0].soldierPosition + enemySoldierList[0].soldierPosition > soldierBetweenThreshold){
                            //공격을 해야만 한다
                            if(enemySoldierList[0].soldierAnimationType == "walk"){
                                enemySoldierList[0].soldierAnimationType = "attack"
                                enemySoldierList[0].soldierAnimationStartTime = 0L
                            }
                        }else{
                            //계속 움직여야 한다
                            for(enemySoldier in enemySoldierList){
                                enemySoldier.soldierPosition += dpToPx(1)
                            }
                        }
                    }

                    Thread.sleep(200)
                }
            }
        }.start()
        binding.gameView.setMySoldierList(mySoldierList)
        binding.gameView.setEnemySoldierList(enemySoldierList)
        binding.btnMakeNormalSoldier.setOnClickListener{
            synchronized(mySoldierList){
                mySoldierList.add(SoldierInfo("normal_soldier",dpToPx(myHomePosition),"walk",0L))
            }
        }
    }

    fun pxToDp(px: Int): Float {
        return (px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun dpToPx(dp: Int): Float {
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}