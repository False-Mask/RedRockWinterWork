package com.example.neteasecloudmusic.favoriteslist

import android.content.Intent
import android.widget.TextView
import com.example.neteasecloudmusic.favoriteslist.songs.SongRvAdapter

interface FavoritesContract {
    interface FavoritesIView {
        fun progressBarOn()
        fun progressBarOff()
        fun setTextColor(songName: TextView?, songPlayingColor: Int)
        fun loopToSongUi(intent: Intent)

    }

    interface  FavoritesIModel{
        fun getSongs(favoriteId: String):String
        fun getTheSecondUrl(string: String):String

    }
    interface FavoritesIPresenter {
        fun getSongs(position: Int?, songRvAdapter: SongRvAdapter)
        fun getPlayList(playListId: String, songRvAdapter: SongRvAdapter, intent: Intent)
        fun itemClicked(position: Int)
    }
}