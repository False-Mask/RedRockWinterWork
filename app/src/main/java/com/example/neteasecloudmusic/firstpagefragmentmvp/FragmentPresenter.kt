package com.example.neteasecloudmusic.firstpagefragmentmvp

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner.NetBannerData
import com.example.neteasecloudmusic.mytools.net.sendGetRequest
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

var bannerJob= Job()
var bannerThread= CoroutineScope(bannerJob)
class FragmentPresenter(firstFragment: FirstFragment) :FirstFragmentContract.FirstFragmentIPresenter
,ServiceConnection{
    var view=firstFragment
    var model=FragmentModel()
    var isBannering=false
//    override fun doBanner(myBanner: ViewPager) {
//        bannerThread.launch(Dispatchers.IO) {
//            while (isBannering){
//                delay(3000)
//                var current=myBanner.currentItem
//                //无限轮播
//                var now=0
//                if(current!= viewList.size-1){
//                    now=current+1
//                }
//                withContext(Dispatchers.Main){
//                    view.changeBanner(now)
//                }
//            }
//        }
//    }

    //连接失败
    override fun onServiceDisconnected(name: ComponentName?) {

    }
    //连接成功
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

    }

    //获取banner的视图
    override suspend fun getBanner(): MutableList<NetBannerData> {
        val bannerData= mutableListOf<NetBannerData>()
        //获取banner的地址
        val url=model.getBannerUrl()
        //开协程发送网络请求
        //发送获取banner图片的url
        try {
            val resultBody= sendGetRequest(url)
            //Gson解析
            //解析banner数据
            model.banners = Gson().fromJson(resultBody,com.example.neteasecloudmusic.firstpagefragmentmvp.Banners::class.java)
            //Banner图的个数
            val count= model.banners.banners.size
            //把需要的数据解析出来加到data里面
            for (i in 0 until count){
                bannerData.add(NetBannerData(model.banners.banners[i].pic,model.banners.banners[i].url?:"NULL"))
            }
            //解析完成就是初始化界面
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }
        return bannerData
    }

}