package com.example.neteasecloudmusic.mytools.musicservice

import android.media.MediaPlayer

interface IServiceBindPresenter {

    //当音乐播放完成了
    fun onMusicCompletion()

    //当音乐开始播放的时候的回调
    fun onMusicStart()

    //拖动完成的时候的回调
    fun onMusicSeekComplete(mp: MediaPlayer?)
    fun onStarted()
    fun onPreparing()
    fun onPause()
    fun onResume()

}