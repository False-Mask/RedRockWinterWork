package com.example.neteasecloudmusic.mainactivitymvp

import android.content.SharedPreferences
import com.example.neteasecloudmusic.mytools.musicservice.MyMusicService

interface MainActivityContract {
    interface MainActivityIModel{
        fun  getLoginUrl(): String
        fun playList(): String
        //fun getBanner(): String
    }
    interface MainActivityIView{
        fun initFragment()
        fun loopToSongUi()
        fun resume(fl: Float)
        fun pause()
        fun preparing()
        fun start()
    }
    interface MainActivityPresenter{
        fun loginAuto()
        //fun getBanner()
        fun onUnavailable()
    }
}