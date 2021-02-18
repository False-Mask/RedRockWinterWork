package com.example.neteasecloudmusic.mainactivitymvp

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.neteasecloudmusic.MainActivity
import com.example.neteasecloudmusic.MyApplication
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner.LocalBannerData
import com.example.neteasecloudmusic.loginactivity.loginbyphone.ByPhoneModel
import com.example.neteasecloudmusic.loginactivity.loginbyphone.loginResult
import com.example.neteasecloudmusic.mytools.filedownload.downLoadImage
import com.example.neteasecloudmusic.mytools.filedownload.downLoadObjectFile
import com.example.neteasecloudmusic.mytools.filedownload.imagePath
import com.example.neteasecloudmusic.mytools.filedownload.readObjectFile
import com.example.neteasecloudmusic.mytools.musicservice.*
import com.example.neteasecloudmusic.mytools.net.netThread
import com.example.neteasecloudmusic.mytools.net.sendGetRequest
import com.example.neteasecloudmusic.mytools.sharedpreferences.put
import com.example.neteasecloudmusic.mytools.toast.MyToast
import com.example.neteasecloudmusic.recyclerview.favorites.Favorites
import com.example.neteasecloudmusic.recyclerview.favorites.list
import com.example.neteasecloudmusic.userfragmentmvp.rvAdapter
import com.example.neteasecloudmusic.view.PlayPauseIcon
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.io.File

//存放一些配置信息
const val MainActivitySpName="MyMainActivitySharedPreferences"
var mainActivitySp= MyApplication.getContext().getSharedPreferences(MainActivitySpName,Context.MODE_PRIVATE)!!
const val BaseFavoritesFileName="Favorites"
const val FavoritesObjectFilesName="FavoritesObject"
const val BannerDataObjectName="BannerData"
const val BaseBannerImageFileName="banner"
class MainActivityPresenter (activity:MainActivity): MainActivityContract.MainActivityPresenter
        , IServiceBindPresenter,ServiceConnection
        ,View.OnClickListener, PlayPauseIcon.Click {

    val TAG = "MainActivityPresenter"
    var view = activity
    var model = MainActivityModel()

    lateinit var musicService:MyMusicService

    //Service的接口
    override fun onMusicCompletion() {

    }

    override fun onMusicStart() {
    }

    override fun onMusicSeekComplete(mp: MediaPlayer?) {
    }

    override fun onStarted() {
        view.start()
    }

    override fun onPreparing() {
        view.preparing()
    }

    override fun onPause() {
        view.pause()
    }

    override fun onResume() {
        view.resume(getCurrentPosition().toFloat()/ getDuration())
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.e(TAG, "连接失败" )
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.e(TAG, "连接成功" )
        musicService=(service as MyMusicService.MyBinder).getService()
        musicService.addBindPresenter(this)
    }



    var loginJob:Job?=null
    //下载了歌单的封面和rv的更新 自动登陆
    override fun loginAuto() {
        //如果之前有过登陆
        if (sp.getBoolean("is_login",false)){
            //先切线程到Main 因为 netWork的线程其实在分支线程 不能进行ui更新
           loginJob = netThread.launch(Main) {
                //切到main 因为还得发送3条网络请求
                withContext(IO) {
                    //io 登陆
                    try {
                        val resultBody = sendGetRequest(model.getLoginUrl())
                        Log.d(TAG, "loginAuto: ")
                        loginResult = Gson().fromJson(resultBody, object : TypeToken<ByPhoneModel.LoginResult>() {}.type)
                    } catch (e: Exception) { }
                    //io第二条 获取用户的歌单信息(不是详细信息)
                    try {
                        val resultBody =  sendGetRequest(model.playList())
                        playListResult = Gson().fromJson<MainActivityModel.PlayListResult>(resultBody, object : TypeToken<MainActivityModel.PlayListResult>() {}.type)



                        Log.e(TAG, "loginAuto: ")
                    } catch (e: Exception) { }

                    //从sp中找找看歌单封面图片下载了没
                    try {
                        val favorites=readObjectFile(FavoritesObjectFilesName) as MutableList<Favorites>
                        for (x in favorites){
                            if (!x.images.exists()) {
                                mainActivitySp.put {
                                    putBoolean("is_picture_exists",false)
                                }
                            }
                        }
                    }catch (e:java.lang.Exception){
                        Log.e(TAG, "loginAuto: 读取歌单object文件出现异常",e)
                    }

                    val isPictureExists=mainActivitySp.getBoolean("is_picture_exists", false)
                    //获取需要下载歌单封面的个数
                    val count=playListResult.playlist?.size?:0
                    //没有下载
                    if (!isPictureExists){
                        if (count!=0){
                            for (i in 0 until count){
                                downLoadImage("$BaseFavoritesFileName$i",playListResult.playlist?.get(i)?.coverImgUrl?:"NULL")
                            }
                            //提示sp库爷已经下载下来了
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
                            //下载对象文件
                            downLoadObjectFile(FavoritesObjectFilesName,list)
                        }
//                        mainActivitySp.put {
//                            putBoolean("isPictureDownloaded",true)
//                        }
                    }
                    try {
                        //list是adapter的顶层声明
                        list=readObjectFile(FavoritesObjectFilesName) as MutableList<Favorites>
                    }catch (e:java.lang.Exception){
                        Log.e(TAG, "loginAuto: 收藏夹读取时发送异常",e )
                    }
                }
                //main线程处理
                //叫rv更新界面
                rvAdapter.notifyDataSetChanged()
                //
                MyToast().sendToast(MyApplication.getContext(), "加载完成", Toast.LENGTH_SHORT)
                //登陆是否成功的处理
                if (loginResult.code == 200) {
                    Log.d(TAG, "自动登陆成功" + Thread.currentThread().name)
                    Log.d(TAG, "歌单为$playListResult")
                    MyToast().sendToast(MyApplication.getContext(), "登陆成功", Toast.LENGTH_SHORT)
                    if(list.size==0){
                        Log.e(TAG, "loginAuto: 歌单数目为0")
                    }

                } else {
                    Log.d(TAG, "自动登陆失败" + Thread.currentThread().name + loginResult.code)
                    MyToast().sendToast(MyApplication.getContext(), "登陆失败", Toast.LENGTH_SHORT)
                }
            }
        }

    }
    override fun onUnavailable() {

    }

    override fun onClick(v: View?) {
        when(v?.id){
            //播放暂停键被点击
            R.id.bottom_play_pause_main->{
                if (getIsPlaying()){
                    musicService.pauseMusic()
                }else{
                    musicService.pauseToStart()
                }
            }
            //当bottom的图片或者文字被点击 -> 跳转到songUI界面
            R.id.bottom_song_name_main,R.id.bottom_song_image_main
                ->{
                view.loopToSongUi()
            }
        }
    }

    override fun onPlayPauseViewClick(v: View) {
        val v2=v as PlayPauseIcon
        when(v2.status){
            PlayPauseIcon.PlayStatus.Playing->{
                musicService.pauseMusic()
                onPause()
            }
            PlayPauseIcon.PlayStatus.Pausing->{
                musicService.pauseToStart()
                onResume()
            }else->{

            }
        }
    }
}