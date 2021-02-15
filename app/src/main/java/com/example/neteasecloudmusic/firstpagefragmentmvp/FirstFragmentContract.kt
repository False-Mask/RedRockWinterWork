package com.example.neteasecloudmusic.firstpagefragmentmvp

import androidx.viewpager.widget.ViewPager
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner.NetBannerData

interface FirstFragmentContract {
    interface FirstFragmentIView{
//        fun initView()
//        fun changeBanner(now: Int)
    }
    interface FirstFragmentIModel{
        fun getBannerUrl(): String
    }
    interface FirstFragmentIPresenter{
        suspend fun getBanner(): MutableList<NetBannerData>
        // fun doBanner(myBanner: ViewPager)
    }
}