package com.example.neteasecloudmusic.mytools.musicservice

import android.media.MediaPlayer

interface IServiceBindView {

    //歌曲播放时候的回调
    fun serviceRefresh(songName: String, singer: String, imageUrl: String, duration: Int, currentTime: Int, songId: String)

    //设置seekBar的最大进度
    fun setMusicMaxProgress(duration: Int)

    //发送Toast的回调
    fun sendToast(s: String)

    //改变图标的回调
    fun iconChangeToPause()

    //由于Buffered与音乐播放不一致 设置方法回调
    fun setBufferedProgress(percent: Int)
}