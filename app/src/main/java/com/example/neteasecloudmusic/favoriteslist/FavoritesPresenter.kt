package com.example.neteasecloudmusic.favoriteslist

import android.util.Log
import com.example.neteasecloudmusic.mainactivitymvp.playListResult
import com.example.neteasecloudmusic.mytools.filedownload.downLoadImage
import com.example.neteasecloudmusic.mytools.filedownload.imagePath
import com.example.neteasecloudmusic.mytools.net.netThread
import com.example.neteasecloudmusic.mytools.net.sendGetRequest
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
const val BaseSongImageName="SongImage"
class FavoritesPresenter(favoritesActivity: FavoritesActivity) :FavoritesContract.FavoritesIPresenter{
    val TAG="FavoritesPresenter"
    //获取song的详细信息
    var view=favoritesActivity
    var model=FavoritesModel()
    override fun getSongs(position: Int?) {
        if (position==null){
            Log.e(TAG, "getSongs: position为空")
        }else{
            var x=playListResult.playlist?.get(position)
            var favoriteId=x?.id.toString()
            var url=model.getSongs(favoriteId)
            netThread.launch(Dispatchers.IO) {
                //网络请求
                var respondBody =sendGetRequest(url)
                playListDetailsResult=Gson().fromJson(respondBody,FavoritesModel.PlayListDetails::class.java)
                var songsData= playListDetailsResult.playlist.tracks
                //需要的数据
                var artists:MutableList<String> = mutableListOf()
                var image:MutableList<File> = mutableListOf()
                var songName:MutableList<String> = mutableListOf()
                var songid:MutableList<String> = mutableListOf()
                for (i in songsData){
                    songid.add(i.al.id.toString())
                    songName.add(i.al.name)
                    var artistsData=i.ar
                    //解析歌曲的内容
                    var z=StringBuilder()
                    for(j in artistsData){
                        z.append(j.name).append('/')
                    }
                    artists.add(z.toString().substring(0..z.length-2))
                    var currentTime=System.currentTimeMillis()
                    //下载图片
                    downLoadImage("$BaseSongImageName$currentTime",i.al.picUrl)
                    //将文件加入到file集合里面
                    image.add(File("$imagePath/$BaseSongImageName$currentTime.jpg"))
                }
            }
        }
    }

}