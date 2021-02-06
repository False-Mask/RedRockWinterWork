package com.example.neteasecloudmusic.mainactivitymvp

import android.content.SharedPreferences

interface MainActivityContract {
    interface MainActivityIModel{
        fun  getLoginUrl(): String
        fun playList(): String
    }
    interface MainActivityIView{
        fun initFragment()
    }
    interface MainActivityPresenter{
        fun loginAuto()
    }
}