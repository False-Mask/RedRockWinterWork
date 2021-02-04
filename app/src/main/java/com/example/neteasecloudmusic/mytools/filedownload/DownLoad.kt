package com.example.kotlintext.filedownload

import android.app.Activity
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.coroutineContext

class DownLoad(activity: Activity){
    var mContext: Activity? =null
    var BASE_PATH:String?=null
    init {
        mContext=activity
        BASE_PATH=mContext!!.cacheDir.path
    }
    //存放图像文件
    suspend fun downLoadImage(filename:String,url:String){
        var imageDir=File("$BASE_PATH/image")
        if (!imageDir.exists()) imageDir.mkdirs()

        var imageFile=File("$BASE_PATH/image/$filename")
        if (!imageFile.exists()) imageFile.createNewFile()
        //从网络上copy文件
        copyFromNet(imageFile,url)
    }

    private suspend fun copyFromNet(imageFile: File, url: String) {
        //网络连接

        var job=Job()
        var coroutineScope= CoroutineScope(job)

        withContext(Dispatchers.IO){
            var connection= URL(url).openConnection() as HttpURLConnection
                    connection.apply {
                    doInput = true
                    doOutput = true
                    readTimeout = 5000
                    connectTimeout = 5000
                    defaultUseCaches = false
                    requestMethod="GET"
                }
                var bufferedIn=connection.inputStream.buffered()
                var bufferedOut=FileOutputStream(imageFile).buffered()

                bufferedOut.write(bufferedIn.readBytes())

            }

    }

    //存放音频缓存文件
    suspend fun downLoadMusic(filename:String,url:String){
        var musicDir=File("$BASE_PATH/music")
        if (!musicDir.exists()) musicDir.mkdirs()

        var musicFile=File("$BASE_PATH/music/$filename")
        if (!musicFile.exists()) musicFile.createNewFile()
        //copy
        copyFromNet(musicFile,url)
    }
}