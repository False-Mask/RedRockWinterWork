package com.example.neteasecloudmusic.firstpagefragmentmvp.search

import android.view.View

interface SearchContract {
    interface SearchIModel{
        fun getSearchUrlTail(keyboard: String): Pair<String, String>

    }


    interface SearchIView{
        fun resume(percent: Float)
        fun loading()
        fun start()
        fun loopToSongActivity(v: View?)


    }


    interface SearchIPresenter{
       fun beginSearch(
           keyboard: String,
           mAdapter: SearchRvAdapter
       )

    }
}