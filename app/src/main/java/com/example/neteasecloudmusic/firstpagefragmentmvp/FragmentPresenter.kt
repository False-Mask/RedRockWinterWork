package com.example.neteasecloudmusic.firstpagefragmentmvp

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.neteasecloudmusic.MainActivity
import com.example.neteasecloudmusic.context
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.MultiRvAdapter
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewData
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner.BannerData
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner.NetBannerData
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.music.MusicBigData
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.music.MusicSmallData
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.music.TopData
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.title.TitleData
import com.example.neteasecloudmusic.mainactivitymvp.playListResult
import com.example.neteasecloudmusic.mainactivitymvp.sp
import com.example.neteasecloudmusic.mytools.net.netThread
import com.example.neteasecloudmusic.mytools.net.sendGetRequest
import com.google.gson.Gson
import kotlinx.android.synthetic.main.first_fragment_layout.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

var bannerJob= Job()
var bannerThread= CoroutineScope(bannerJob)
class FragmentPresenter(firstFragment: FirstFragment) :FirstFragmentContract.FirstFragmentIPresenter
,ServiceConnection, SwipeRefreshLayout.OnRefreshListener {
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
    override suspend fun getBanner(): BannerData {
        val bannerData= mutableListOf<NetBannerData>()
        //获取banner的地址
        val url=model.getBannerUrl()
        //开协程发送网络请求
        //发送获取banner图片的url
        try {
            val resultBody= sendGetRequest(url)
            //Gson解析
            //解析banner数据
            model.banners = Gson().fromJson(resultBody, Banners::class.java)
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

        val banner=BannerData()
        banner.netList=bannerData
        return banner
    }


    //添加ViewData的数据
    var multiList= mutableListOf<ViewData>()
    //Adapter
    var multiRvAdapter:MultiRvAdapter=MultiRvAdapter(multiList)//这时候multiList里面还是空的
    //Banner的数据
    private val bannerData= BannerData()


    //创建布局管理器
    val layoutManager= GridLayoutManager(context,6)

    override fun initRecyclerView() {

        //发送获取其他的数据
        netThread.launch (Dispatchers.IO){
            //第一步获取 banner数据
            val step1=async { getBanner() }
            //第二步获取 小大列表对应的数据
            val step2=async { getRecommendFavorites() }
            //第三步获取 网友精选歌单
            val step3=async { getTopFavorites() }

            //获取数据
            val data1=step1.await()
            val (data2,data3)=step2.await()
            val data4=step3.await()

            //加入banner
            multiList.add(data1)
//            multiRvAdapter?.setMultiList(multiList)
//            multiRvAdapter?.notifyDataSetChanged()

            //添加标题
            multiList.add(TitleData("推荐歌单"))
//            multiRvAdapter?.setMultiList(multiList)
//            multiRvAdapter?.notifyDataSetChanged()

            //加入small
            for (x in data2) multiList.add(x)
//            multiRvAdapter?.setMultiList(multiList)
//            multiRvAdapter?.notifyDataSetChanged()
            //加入big
            for (x in data3) multiList.add(x)
//            multiRvAdapter?.setMultiList(multiList)
//            multiRvAdapter?.notifyDataSetChanged()

            //加入title
            multiList.add(TitleData("精选歌单"))

            //加入Top歌单
            for(i in data4){
                multiList.add(i)
            }

//            获取Banner数据
//            bannerData.netList=getBanner()
//            添加banner的数据
//            multiList.add(bannerData)


            //更新一下界面
            withContext(Main){

                multiRvAdapter?.setMultiList(multiList)
                //设置布局管理器
                multiRvAdapter?.setGridSpan(layoutManager)
                //初始化adapter

                //设置adapter和layoutManager
                view.recycler_view_ff.adapter=multiRvAdapter
                view.recycler_view_ff.layoutManager=layoutManager
                //提示rv更新界面
                multiRvAdapter?.notifyDataSetChanged()
            }

            //获取推荐的歌曲列表
            //getRecommendFavorites()


            //标题栏

            //请求获取到推荐歌单

        }
    }

    //发送并获取歌单
    private suspend fun getTopFavorites(): MutableList<TopData> {
        val url=model.getTopFavorites(System.currentTimeMillis().toString())
        val respondBody= sendGetRequest(url)
        model.topFavorites=Gson().fromJson(respondBody,TopFavorites::class.java)
        val data=model.topFavorites
        val topList= mutableListOf<TopData>()
        for (i in data.playlists){
            topList.add(TopData(i.name,i.coverImgUrl,i.id.toString()))
        }
        return topList
    }


    //准备好发送请求的list
    private var preparedList= mutableListOf<RecommendSong>()
    //还没完全准备好发送的list
    var preparingList= mutableListOf<RecommendSong>()
    //展示的5个长度的list
    private var showedList= mutableListOf<RecommendSong>()





    private suspend fun getRecommendFavorites(): Pair<MutableList<MusicSmallData>, MutableList<MusicBigData>> {

        if (sp.getBoolean("is_login",false)){
                MainActivity.presenter.loginJob?.join()
        //等待登陆工作完成
            val favoritesCount=playListResult.playlist?.size
            if (favoritesCount==0){
                withContext(Main){
                    view.sendToast("无收藏歌单无法进行相似匹配")
                }
                return Pair(mutableListOf(), mutableListOf())
            }
            else{
                if(playListResult.playlist!=null){
                    //加载到准备完成的列表中去
                    for (i in playListResult.playlist!!){
                        preparedList.add(RecommendSong(i.id.toString(),i.coverImgUrl,i.name))
                    }
                    //获取到准备展示的list
                    getRelativeSongs()
                    //把showedList的内容copy 5个进去
                    return  copyIntoMultiList()
//                    //更新界面
//                    withContext(Main){
//                        multiRvAdapter?.notifyDataSetChanged()
//                    }
                }else{
                    Log.e("TAG", "getRecommendFavorites: $playListResult")
                }

                }
        }
        return Pair(mutableListOf(), mutableListOf())
    }

    private fun copyIntoMultiList(): Pair<MutableList<MusicSmallData>, MutableList<MusicBigData>> {
        //创建一个临时的list
        val list= mutableListOf<RecommendSong>()
        for (i in 0 until 5) {
            //复制一份
            list.add(showedList[0])
            showedList.removeAt(0)
        }

        val bigList= mutableListOf<MusicBigData>()
        val smallList= mutableListOf<MusicSmallData>()

        //添加3个小型的song表单
        for (i in 0 until 3){
            smallList.add(MusicSmallData(list[i].nama,list[i].coverUrl,list[i].id))
        }

        //添加两个稍微大一点的song表单
        for (i in 3 until 5){
            bigList.add(MusicBigData(list[i].nama,list[i].coverUrl,list[i].id))
        }

        return Pair(smallList,bigList)
    }

    private suspend fun refreshRecommendData(): Pair<MutableList<MusicSmallData>, MutableList<MusicBigData>> {
        if (preparedList.size!=0){
            if (showedList.size<5){
                getRelativeSongs()
            }

            return copyIntoMultiList()
        }else{
            //先把东西复制过去
            for (x in preparingList){
                preparedList.add(x)
            }
            //然后把preparing清空 方便下一次存储
            preparingList.clear()

            if (showedList.size<5){
               getRelativeSongs()
            }

            return copyIntoMultiList()
        }
    }


    private suspend fun getRelativeSongs() {
        //发送网络请求了
        val x=(0 until preparedList.size).random()
        val url=model.getRecommendUrl(preparedList[x].id)
        val respondBody= sendGetRequest(url)
        model.relatedFavorites=Gson().fromJson(respondBody,RelatedFavorites::class.java)

        val data=model.relatedFavorites
        for (i in data.playlists){
            showedList.add(RecommendSong(i.id,i.coverImgUrl,i.name))
            //同时preparingList也要开始复制item
            preparingList.add(RecommendSong(i.id,i.coverImgUrl,i.name))
        }
        //这个id已经发送过请求了没什么用了可以remove掉
        preparedList.removeAt(x)
        //如果showedList长度低于5再发一次
        if (showedList.size<5){
            getRelativeSongs()
        }
        //大于等于5就开溜了
    }

    override fun onRefresh() {
        if (preparedList.size==0&&preparingList.size==0){
            view.sendToast("正在解析数据请稍后")
            view.setFreshOff()
        }else{
            netThread.launch(IO) {
                //
                val job1=async { getTopFavorites() }
                //刷新相关音乐面板
                val (refresh1,refresh2)=refreshRecommendData()

                //await
                val refresh3=job1.await()

                //刷新小的音乐页面
                for (x in 2..4)  multiList[x] = refresh1[x-2]
                //刷新大的音乐面板
                for (x in 5..6)  multiList[x] = refresh2[x-5]
                //刷新网友精选歌单
                for (x in 8..12) multiList[x] = refresh3[x-8]
                //设置list
                multiRvAdapter?.setMultiList(multiList)
                //提示adapter改变数据
                withContext(Main){
                    multiRvAdapter?.notifyDataSetChanged()
                }

                view.setFreshOff()
            }
        }
    }


}