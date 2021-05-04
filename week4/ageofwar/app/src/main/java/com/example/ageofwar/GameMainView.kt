package com.example.ageofwar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import java.lang.IndexOutOfBoundsException
import kotlin.math.roundToInt


class GameMainView(
    context: Context,
    attrs: AttributeSet?
) :
    View(context, attrs) {

    private val displayMetrics: DisplayMetrics = this.resources.displayMetrics
    private val homeY = dpToPx(20)
    private var homeBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.home)
    private var paint = Paint()

    @SuppressLint("ResourceType")
    private val normalSoldierStop: Movie =
        Movie.decodeStream(resources.openRawResource(R.drawable.normal_soldier_stop))

    @SuppressLint("ResourceType")
    private val normalSoldierWalk: Movie =
        Movie.decodeStream(resources.openRawResource(R.drawable.normal_soldier_walk))

    @SuppressLint("ResourceType")
    private val normalSoldierAttack: Movie =
        Movie.decodeStream(resources.openRawResource(R.drawable.normal_soldier_attack))

    @SuppressLint("ResourceType")
    private val distanceSoldierStop: Movie =
        Movie.decodeStream(resources.openRawResource(R.drawable.distance_soldier_stop))

    @SuppressLint("ResourceType")
    private val distanceSoldierWalk: Movie =
        Movie.decodeStream(resources.openRawResource(R.drawable.distance_soldier_walk))

    @SuppressLint("ResourceType")
    private val distanceSoldierAttack: Movie =
        Movie.decodeStream(resources.openRawResource(R.drawable.distance_soldier_attack))

    @SuppressLint("ResourceType")
    private val distanceSoldierAttackDistance: Movie =
        Movie.decodeStream(resources.openRawResource(R.drawable.distance_soldier_attack_distance))

    private var mySoldierList: ArrayList<SoldierInfo>? = null
    private var enemySoldierList: ArrayList<SoldierInfo>? = null
    private var played = true
    override fun onDraw(canvas: Canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas)
        canvas.save()
        //나의 편의 home을 그림
        canvas.drawBitmap(scaleBitmap(homeBitmap, 0.6f, 0.6f), homeY, 0.0f, paint)
        canvas.restore()

        canvas.save()
        //적의 편의 home을 그림
        canvas.drawBitmap(
            rotateBitmap(scaleBitmap(homeBitmap, -0.6f, 0.6f), 180.0f),
            homeY,
            canvas.height - homeBitmap.height * 0.6f,
            paint
        )
        canvas.restore()
        try {
            if (mySoldierList != null) {
                for (i in mySoldierList!!.indices) {
                    canvas.save()
                    canvas.scale(4.0f, 4.0f)
                    canvas.rotate(90.0f)
                    val now = android.os.SystemClock.uptimeMillis()
                    if(mySoldierList!![i].soldierType == "normal_soldier"){
                        if (mySoldierList!![i].soldierAnimationType == "stop") {
                            var relTime =
                                ((now - mySoldierList!![i].soldierAnimationStartTime) % normalSoldierStop.duration()).toInt()
                            if (played)
                                normalSoldierStop.setTime(relTime)
                            normalSoldierStop.draw(
                                canvas,
                                mySoldierList!![i].soldierPosition,
                                -homeY
                            )
                        } else if (mySoldierList!![i].soldierAnimationType == "walk") {
                            var relTime =
                                ((now - mySoldierList!![i].soldierAnimationStartTime) % normalSoldierWalk.duration()).toInt()
                            if (played)
                                normalSoldierWalk.setTime(relTime)
                            normalSoldierWalk.draw(
                                canvas,
                                mySoldierList!![i].soldierPosition,
                                -homeY
                            )
                        } else if (mySoldierList!![i].soldierAnimationType == "attack") {
                            var relTime =
                                ((now - mySoldierList!![i].soldierAnimationStartTime) % normalSoldierAttack.duration()).toInt()
                            if (played)
                                normalSoldierAttack.setTime(relTime)
                            normalSoldierAttack.draw(
                                canvas,
                                mySoldierList!![i].soldierPosition - 10,
                                -(homeY + 25)
                            )
                        }
                    }else if(mySoldierList!![i].soldierType == "distance_soldier"){
                        if (mySoldierList!![i].soldierAnimationType == "stop") {
                            var relTime =
                                ((now - mySoldierList!![i].soldierAnimationStartTime) % distanceSoldierStop.duration()).toInt()
                            if (played)
                                distanceSoldierStop.setTime(relTime)
                            distanceSoldierStop.draw(
                                canvas,
                                mySoldierList!![i].soldierPosition,
                                -homeY-5
                            )
                        } else if (mySoldierList!![i].soldierAnimationType == "walk") {
                            var relTime =
                                ((now - mySoldierList!![i].soldierAnimationStartTime) % distanceSoldierWalk.duration()).toInt()
                            if (played)
                                distanceSoldierWalk.setTime(relTime)
                            distanceSoldierWalk.draw(
                                canvas,
                                mySoldierList!![i].soldierPosition,
                                -homeY-5
                            )
                        } else if (mySoldierList!![i].soldierAnimationType == "attack") {
                            var relTime =
                                ((now - mySoldierList!![i].soldierAnimationStartTime) % distanceSoldierAttack.duration()).toInt()
                            if (played)
                                distanceSoldierAttack.setTime(relTime)
                            distanceSoldierAttack.draw(
                                canvas,
                                mySoldierList!![i].soldierPosition - 10,
                                -homeY-5
                            )
                        }else if (mySoldierList!![i].soldierAnimationType == "attack_distance") {
                            var relTime =
                                ((now - mySoldierList!![i].soldierAnimationStartTime) % distanceSoldierAttackDistance.duration()).toInt()
                            if (played)
                                distanceSoldierAttackDistance.setTime(relTime)
                            distanceSoldierAttackDistance.draw(
                                canvas,
                                mySoldierList!![i].soldierPosition - 10,
                                -homeY-5
                            )
                        }
                    }
                    canvas.restore()
                }
            }

            if (enemySoldierList != null) {
                for (i in enemySoldierList!!.indices) {
                    canvas.save()
                    canvas.scale(-4.0f, 4.0f)
                    canvas.rotate(-90f)
                    val now = android.os.SystemClock.uptimeMillis()
                    if(enemySoldierList!![i].soldierType == "normal_soldier"){
                        if (enemySoldierList!![i].soldierAnimationType == "stop") {
                            var relTime =
                                ((now - enemySoldierList!![i].soldierAnimationStartTime) % normalSoldierStop.duration()).toInt()
                            if (played)
                                normalSoldierStop.setTime(relTime)
                            normalSoldierStop.draw(
                                canvas,
                                enemySoldierList!![i].soldierPosition,
                                -homeY
                            )
                        } else if (enemySoldierList!![i].soldierAnimationType == "walk") {
                            var relTime =
                                ((now - enemySoldierList!![i].soldierAnimationStartTime) % normalSoldierWalk.duration()).toInt()
                            if (played)
                                normalSoldierWalk.setTime(relTime)
                            normalSoldierWalk.draw(
                                canvas,
                                enemySoldierList!![i].soldierPosition,
                                -homeY
                            )
                        } else if (enemySoldierList!![i].soldierAnimationType == "attack") {
                            var relTime =
                                ((now - enemySoldierList!![i].soldierAnimationStartTime) % normalSoldierAttack.duration()).toInt()
                            if (played)
                                normalSoldierAttack.setTime(relTime)
                            normalSoldierAttack.draw(
                                canvas,
                                enemySoldierList!![i].soldierPosition - 10,
                                -(homeY + 25)
                            )
                        }
                    }else if(enemySoldierList!![i].soldierType == "distance_soldier"){
                        if (enemySoldierList!![i].soldierAnimationType == "stop") {
                            var relTime =
                                ((now - enemySoldierList!![i].soldierAnimationStartTime) % distanceSoldierStop.duration()).toInt()
                            if (played)
                                distanceSoldierStop.setTime(relTime)
                            distanceSoldierStop.draw(
                                canvas,
                                enemySoldierList!![i].soldierPosition,
                                -homeY-5
                            )
                        } else if (enemySoldierList!![i].soldierAnimationType == "walk") {
                            var relTime =
                                ((now - enemySoldierList!![i].soldierAnimationStartTime) % distanceSoldierWalk.duration()).toInt()
                            if (played)
                                distanceSoldierWalk.setTime(relTime)
                            distanceSoldierWalk.draw(
                                canvas,
                                enemySoldierList!![i].soldierPosition,
                                -homeY-5
                            )
                        } else if (enemySoldierList!![i].soldierAnimationType == "attack") {
                            var relTime =
                                ((now - enemySoldierList!![i].soldierAnimationStartTime) % distanceSoldierAttack.duration()).toInt()
                            if (played)
                                distanceSoldierAttack.setTime(relTime)
                            distanceSoldierAttack.draw(
                                canvas,
                                enemySoldierList!![i].soldierPosition - 10,
                                -homeY -5
                            )
                        }else if (enemySoldierList!![i].soldierAnimationType == "attack_distance") {
                            var relTime =
                                ((now - enemySoldierList!![i].soldierAnimationStartTime) % distanceSoldierAttackDistance.duration()).toInt()
                            if (played)
                                distanceSoldierAttackDistance.setTime(relTime)
                            distanceSoldierAttackDistance.draw(
                                canvas,
                                enemySoldierList!![i].soldierPosition - 10,
                                -homeY -5
                            )
                        }
                    }

                    canvas.restore()
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            this.invalidate()
        }
        this.invalidate()
    }


    fun setPlayed(played: Boolean) {
        this.played = played
    }

    fun setMySoldierList(list: ArrayList<SoldierInfo>) {
        mySoldierList = list
    }

    fun setEnemySoldierList(list: ArrayList<SoldierInfo>) {
        enemySoldierList = list
    }

    fun pxToDp(px: Int): Float {
        return (px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun dpToPx(dp: Int): Float {
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix: Matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    fun scaleBitmap(source: Bitmap, scaleX: Float, scaleY: Float): Bitmap {
        val matrix: Matrix = Matrix()
        matrix.postScale(scaleX, scaleY)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, false)
    }

}