package com.example.neteasecloudmusic.mainactivitymvp

import android.content.SharedPreferences

interface MainActivityContract {
    interface MainActivityIModel{
        fun loginAuto()
    }
    interface MainActivityIView{
        fun initFragment()
    }
    interface MainActivityPresenter{
        fun loginAuto()
    }
}