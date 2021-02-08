package com.example.neteasecloudmusic.mainactivitymvp

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.neteasecloudmusic.MainActivity
import com.example.neteasecloudmusic.MyApplication
import com.example.neteasecloudmusic.loginactivity.loginbyphone.ByPhoneModel
import com.example.neteasecloudmusic.loginactivity.loginbyphone.loginResult
import com.example.neteasecloudmusic.mytools.filedownload.downLoadImage
import com.example.neteasecloudmusic.mytools.filedownload.downLoadObjectFile
import com.example.neteasecloudmusic.mytools.filedownload.imagePath
import com.example.neteasecloudmusic.mytools.filedownload.readObjectFile
import com.example.neteasecloudmusic.mytools.net.netThread
import com.example.neteasecloudmusic.mytools.net.sendGetRequest
import com.example.neteasecloudmusic.mytools.sharedpreferences.put
import com.example.neteasecloudmusic.mytools.toast.MyToast
import com.example.neteasecloudmusic.myview.BannerData
import com.example.neteasecloudmusic.recyclerview.favorites.Favorites
import com.example.neteasecloudmusic.recyclerview.favorites.list
import com.example.neteasecloudmusic.userfragmentmvp.rvAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

//存放一些配置信息
const val MainActivitySpName="MyMainActivitySharedPreferences"
var mainActivitySp= MyApplication.getContext().getSharedPreferences(MainActivitySpName,Context.MODE_PRIVATE)!!
const val BaseFavoritesFileName="Favorites"
const val FavoritesObjectFilesName="FavoritesObject"
const val BannerDataObjectName="BannerData"
const val BaseBannerImageFileName="banner"
class MainActivityPresenter (activity:MainActivity): MainActivityContract.MainActivityPresenter {
    val TAG = "MainActivityPresenter"
    var view = activity
    var model = MainActivityModel()

    override fun loginAuto() {
        netThread.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                //io第一条
                try {
                    var resultBody = async { sendGetRequest(model.getLoginUrl()) }
                    Log.d(TAG, "loginAuto: ")
                    loginResult = Gson().fromJson(resultBody.await(), object : TypeToken<ByPhoneModel.LoginResult>() {}.type)
                } catch (e: Exception) { }
                //io第二条
                try {
                    var resultBody = async { sendGetRequest(model.playList()) }
                    playListResult = Gson().fromJson<MainActivityModel.PlayListResult>(resultBody.await(), object : TypeToken<MainActivityModel.PlayListResult>() {}.type)
                    Log.e(TAG, "loginAuto: ")
                } catch (e: Exception) { }

                //从sp中找找看图片下载了没
                var isPictureDownloaded=mainActivitySp.getBoolean("isPictureDownloaded", false)
                //没有下载
                var count=playListResult.playlist?.size?:0
                if (!isPictureDownloaded){
                    if (count!=0){
                        for (i in 0 until count){
                            downLoadImage("$BaseFavoritesFileName$i",playListResult.playlist?.get(i)?.coverImgUrl?:"NULL")
                        }
                        mainActivitySp.put {
                            putBoolean("isPictureDownloaded",true)
                        }
                        //然后吧文件加载到adapter里面
                        for (i in 0 until count){
                            var play=playListResult.playlist?.get(i)
                            list.add(
                                    Favorites(play?.name?:"NULL",play?.trackCount?:0,File("$imagePath/$BaseFavoritesFileName$i.jpg"))
                            )
                        }

                        //不知道为什么会爆EOF异常
                        //下载文件
                        downLoadObjectFile(FavoritesObjectFilesName,list)
                    }
                }else{
                    for (i in 0..count){

                    }
                    list=readObjectFile(FavoritesObjectFilesName) as MutableList<Favorites>
                }
                //切线程发送消息
                withContext(Dispatchers.Main){
                    rvAdapter.notifyDataSetChanged()
                    MyToast().sendToast(MyApplication.getContext(), "加载完成", Toast.LENGTH_SHORT)
                }
            }

            //main线程处理
            if (loginResult.code == 200) {
                Log.d(TAG, "自动登陆成功" + Thread.currentThread().name)
                Log.d(TAG, "歌单为$playListResult")
                MyToast().sendToast(MyApplication.getContext(), "登陆成功", Toast.LENGTH_SHORT)
                if(list.size==0){
                    MyToast().sendToast(MyApplication.getContext(), "出现错误", Toast.LENGTH_SHORT)
                }

            } else {
                Log.d(TAG, "自动登陆失败" + Thread.currentThread().name + loginResult.code)
                MyToast().sendToast(MyApplication.getContext(), "登陆失败", Toast.LENGTH_SHORT)
            }
        }

    }
    //获取banner的视图
    override fun getBanner() {
        var url=model.getBanner()
        netThread.launch (Dispatchers.IO){
            //发送获取banner图片的url
            try {
                val resultBody=sendGetRequest(url)
                bannerResult=Gson().fromJson(resultBody,MainActivityModel.Banners::class.java)
                val count=bannerResult.banners.size
                val bannerList= mutableListOf<BannerData>()

                //下载图片
                for (i in 0 until count){
                    downLoadImage("banner$i", bannerResult.banners[i].pic)
                    bannerList.add(BannerData(File("$imagePath/banner$i.jpg") ,bannerResult.banners[i].url?:"NULL"))
                }
                //下载banner对象方便下次登陆时候的寻找
                downLoadObjectFile(BannerDataObjectName,bannerList)
                //下载完成
                mainActivitySp.put {
                    putBoolean("is_banner_cathe",true)
                }
            }catch (e:java.lang.Exception){
                e.printStackTrace()
            }
            //切换Main线程更新Ui
            withContext(Dispatchers.Main){
                MainActivity.firstFragment.initView()
            }
        }
    }

    override fun onUnavailable() {

    }
}