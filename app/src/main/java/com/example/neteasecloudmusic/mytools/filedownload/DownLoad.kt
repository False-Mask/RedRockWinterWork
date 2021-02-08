package com.example.neteasecloudmusic.mytools.filedownload

import android.content.Context
import com.example.neteasecloudmusic.MyApplication
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

//存放图像文件
suspend fun downLoadImage(filename:String,url:String){
    var imageDir=File(imagePath?:"NULL")
    if (!imageDir.exists()) imageDir.mkdirs()

    var imageFile=File("$imagePath/$filename.jpg")
    if (!imageFile.exists()) imageFile.createNewFile()
    //从网络上copy文件
    DownLoad.copyFromNet(imageFile,url)
}

//存放音频缓存文件
suspend fun downLoadMusic(filename:String,url:String) {
    var musicDir = File(musicPath?:"NULL")
    if (!musicDir.exists()) musicDir.mkdirs()

    var musicFile = File("$musicPath/$filename.mp4")
    if (!musicFile.exists()) musicFile.createNewFile()
    //copy
    DownLoad.copyFromNet(musicFile, url)
}

//读取文件
fun readObjectFile(name: String):Any{
    var file=File("$filesPath/$name")
    if (!file.exists()){
        file.createNewFile()
    }
    var objIn=ObjectInputStream(FileInputStream(file))
    var obj=objIn.readObject()

    //关闭流
    objIn.close()
    return obj
}

//几个常量
val mContext: Context=MyApplication.getContext()
val imagePath:String?= mContext.cacheDir.path+"/image"
var filesPath:String?= mContext.filesDir.path
val musicPath:String?= mContext.filesDir.path+"/music"


//下载序列化文件
fun downLoadObjectFile(name:String,obj:Any){
    val file=File("$filesPath/$name")
    if (!file.parentFile.exists()){
        file.parentFile.mkdirs()
    }
    if (!file.exists()){
        file.createNewFile()
    }
    val objOut= ObjectOutputStream(FileOutputStream(file))
    objOut.writeObject(obj)

    //关闭
    objOut.close()
}

private object DownLoad{
    fun copyFromNet(imageFile: File, url: String) {

        //网络连接
        var connection= URL(url).openConnection() as HttpURLConnection
        connection.apply {
            doInput = true
            readTimeout = 5000
            connectTimeout = 5000
            defaultUseCaches = false
            requestMethod="GET"
        }
        var bufferedIn=connection.inputStream.buffered()
        var bufferedOut=FileOutputStream(imageFile).buffered()

        bufferedOut.write(bufferedIn.readBytes())

        //关闭
        bufferedOut.close()
        bufferedOut.close()
        connection.disconnect()
    }
}