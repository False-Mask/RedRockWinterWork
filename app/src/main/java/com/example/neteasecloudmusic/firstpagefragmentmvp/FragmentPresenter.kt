package com.example.neteasecloudmusic.firstpagefragmentmvp

import androidx.viewpager.widget.ViewPager
import com.example.neteasecloudmusic.myview.viewList
import kotlinx.coroutines.*

var bannerJob= Job()
var bannerThread= CoroutineScope(bannerJob)
class FragmentPresenter(firstFragment: FirstFragment) :FirstFragmentContract.FirstFragmentIPresenter{
    var view=firstFragment
    var model=FragmentModel()
    var isBannering=false
    override fun doBanner(myBanner: ViewPager) {
        bannerThread.launch(Dispatchers.IO) {
            while (isBannering){
                delay(3000)
                var current=myBanner.currentItem
                //无限轮播
                var now=0
                if(current!= viewList.size-1){
                    now=current+1
                }
                withContext(Dispatchers.Main){
                    view.changeBanner(now)
                }
            }
        }
    }

}