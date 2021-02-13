package com.example.neteasecloudmusic.mainactivitymvp

import android.content.SharedPreferences
import com.example.neteasecloudmusic.mytools.musicservice.MyMusicService

interface MainActivityContract {
    interface MainActivityIModel{
        fun  getLoginUrl(): String
        fun playList(): String
        fun getBanner(): String
    }
    interface MainActivityIView{
        fun initFragment()
    }
    interface MainActivityPresenter{
        fun loginAuto()
        fun getBanner()
        fun onUnavailable()
        fun addMusicService(musicService: MyMusicService)
    }
}