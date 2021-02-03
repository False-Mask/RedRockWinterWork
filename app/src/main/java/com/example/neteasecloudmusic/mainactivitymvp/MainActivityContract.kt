package com.example.neteasecloudmusic.mainactivitymvp

import android.content.SharedPreferences

interface MainActivityContract {
    interface MainActivityIModel{

    }
    interface MainActivityIView{
        fun initFragment()
    }
    interface MainActivityPresenter{
    }
}