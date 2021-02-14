package com.example.neteasecloudmusic.firstpagefragmentmvp

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.viewpager.widget.ViewPager
import com.example.neteasecloudmusic.myview.viewList
import kotlinx.coroutines.*

var bannerJob= Job()
var bannerThread= CoroutineScope(bannerJob)
class FragmentPresenter(firstFragment: FirstFragment) :FirstFragmentContract.FirstFragmentIPresenter
,ServiceConnection{
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

    //连接失败
    override fun onServiceDisconnected(name: ComponentName?) {

    }
    //连接成功
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

    }

}