package com.example.neteasecloudmusic.firstpagefragmentmvp.search

interface SearchContract {
    interface SearchIModel{
        fun getSearchUrlTail(keyboard: String): Pair<String, String>

    }


    interface SearchIView{


    }


    interface SearchIPresenter{
       fun beginSearch(
           keyboard: String,
           mAdapter: SearchRvAdapter
       )

    }
}