package com.example.neteasecloudmusic.favoriteslist.songui

import android.view.View
import com.example.neteasecloudmusic.favoriteslist.songs.Song
import com.example.neteasecloudmusic.mytools.musicservice.MyMusicService

interface SongContract {
    interface SongIModel{
        fun getUrl(songId: String): String

    }
    interface SongIView {
        fun iconChangeToPlay()
        fun iconChangeToPause()
        fun sendToast(songUrl: String)
        fun setCurrentTextProgressTo(current: Int)
        //改变进度条
        fun setSeekBarMaxProgress(duration: Int)
        fun setCurrentSeekBarProgressTo(current: Int)
        fun setBufferedBarPercent(percent: Int)
    }
    interface SongIPresenter{
        fun pauseOrPlay(
            it: View?,
            song: Song,
            musicService: MyMusicService
        )
        fun nextSong(
            it: View?,
            song: Song,
            musicService: MyMusicService
        )
        fun lastSong(
            it: View?,
            song: Song,
            musicService: MyMusicService
        )

        fun initView()

    }
}