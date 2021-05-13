package com.example.movieapp

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "fe41c95bc4fee421723202ce30d8256d")
    }
}