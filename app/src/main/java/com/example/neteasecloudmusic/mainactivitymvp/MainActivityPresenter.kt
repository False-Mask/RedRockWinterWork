package com.example.neteasecloudmusic.mainactivitymvp

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.neteasecloudmusic.MainActivity
import com.example.neteasecloudmusic.MyApplication
import com.example.neteasecloudmusic.loginactivity.loginbyphone.ByPhoneModel
import com.example.neteasecloudmusic.mytools.filedownload.downLoadImage
import com.example.neteasecloudmusic.mytools.filedownload.downLoadObjectFile
import com.example.neteasecloudmusic.mytools.filedownload.imagePath
import com.example.neteasecloudmusic.mytools.filedownload.readObjectFile
import com.example.neteasecloudmusic.mytools.net.netThread
import com.example.neteasecloudmusic.mytools.net.sendGetRequest
import com.example.neteasecloudmusic.mytools.sharedpreferences.put
import com.example.neteasecloudmusic.mytools.toast.MyToast
import com.example.neteasecloudmusic.recyclerview.favorites.Favorites
import com.example.neteasecloudmusic.recyclerview.favorites.list
import com.example.neteasecloudmusic.userfragmentmvp.Mlist
import com.example.neteasecloudmusic.userfragmentmvp.rvAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.log

//存放一些配置信息
const val MainActivitySpName="MyMainActivitySharedPreferences"
var mainActivitySp= MyApplication.getContext().getSharedPreferences(MainActivitySpName,Context.MODE_PRIVATE)!!
const val BaseFavoritesFileName="Favorites"

const val FavoritesObjectFilesName="FavoritesObject"
class MainActivityPresenter (activity:MainActivity): MainActivityContract.MainActivityPresenter {
    val TAG = "MainActivityPresenter"
    var view = activity
    var model = MainActivityModel()

    var p: Any? = null
    override fun loginAuto() {
        netThread.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                //io第一条
                try {
                    var resultBody = async { sendGetRequest(model.getLoginUrl()) }
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
                        for (i in 0 until count-1){
                            downLoadImage("$BaseFavoritesFileName$i",playListResult.playlist?.get(i)?.coverImgUrl?:"NULL")
                        }
                        mainActivitySp.put {
                            putBoolean("isPictureDownloaded",true)
                        }
                    }
                    //然后吧文件加载到adapter里面
                    if (count!=0){
                        for (i in 0 until count-1){
                            var play=playListResult.playlist?.get(i)
                            Mlist.add(
                                    Favorites(play?.name?:"NULL",play?.trackCount?:0,File(imagePath+ BaseFavoritesFileName+i))
                            )
                            downLoadObjectFile(FavoritesObjectFilesName,Mlist)
                        }
                    }
                }else{
                    Mlist=readObjectFile(FavoritesObjectFilesName) as MutableList<Favorites>
                }
                withContext(Dispatchers.Main){
                    rvAdapter.notifyDataSetChanged()
                }
            }

            //main线程处理
            if (loginResult.code == 200) {
                Log.d(TAG, "自动登陆成功" + Thread.currentThread().name)
                Log.d(TAG, "歌单为$playListResult")
                MyToast().sendToast(MyApplication.getContext(), "登陆成功", Toast.LENGTH_SHORT)

            } else {
                Log.d(TAG, "自动登陆失败" + Thread.currentThread().name + loginResult.code)
                MyToast().sendToast(MyApplication.getContext(), "登陆失败", Toast.LENGTH_SHORT)
            }
        }

    }
}