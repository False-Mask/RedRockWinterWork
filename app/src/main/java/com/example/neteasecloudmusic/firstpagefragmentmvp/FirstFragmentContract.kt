package com.example.neteasecloudmusic.firstpagefragmentmvp

import androidx.viewpager.widget.ViewPager

interface FirstFragmentContract {
    interface FirstFragmentIView{
        fun initView()
        fun changeBanner(now: Int)
    }
    interface FirstFragmentIModel{}
    interface FirstFragmentIPresenter{
        fun doBanner(myBanner: ViewPager)
    }
}