package com.example.neteasecloudmusic.firstpagefragmentmvp.search

interface SearchContract {
    interface SearchIModel{
        fun getSearchUrlTail(keyboard: String): Pair<String, String>

    }


    interface SearchIView{
        fun resume(percent: Float)
        fun loading()
        fun start()


    }


    interface SearchIPresenter{
       fun beginSearch(
           keyboard: String,
           mAdapter: SearchRvAdapter
       )

    }
}