package com.example.neteasecloudmusic.firstpagefragmentmvp

import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner.BannerData
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner.NetBannerData

interface FirstFragmentContract {
    interface FirstFragmentIView{
        fun sendToast(s: String)
        fun setFreshOff()
        fun loopToSearchActivity()
//        fun initView()
//        fun changeBanner(now: Int)
    }
    interface FirstFragmentIModel{
        fun getBannerUrl(): String
        fun getRecommendUrl(recommendSong: String):String
        fun getTopFavorites(toString: String):String
    }
    interface FirstFragmentIPresenter{
        suspend fun getBanner(): BannerData
        fun initRecyclerView()
        // fun doBanner(myBanner: ViewPager)
    }
}