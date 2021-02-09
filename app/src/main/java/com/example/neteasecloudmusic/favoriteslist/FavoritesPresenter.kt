package com.example.neteasecloudmusic.favoriteslist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.neteasecloudmusic.favoriteslist.songs.Song
import com.example.neteasecloudmusic.favoriteslist.songs.SongRvAdapter
import com.example.neteasecloudmusic.favoriteslist.songs.songList
import com.example.neteasecloudmusic.favoriteslist.songui.SongUiActivity
import com.example.neteasecloudmusic.mainactivitymvp.mainActivitySp
import com.example.neteasecloudmusic.mainactivitymvp.playListResult
import com.example.neteasecloudmusic.mytools.filedownload.*
import com.example.neteasecloudmusic.mytools.net.netThread
import com.example.neteasecloudmusic.mytools.net.sendGetRequest
import com.example.neteasecloudmusic.mytools.sharedpreferences.put
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

const val BaseSongImageName="SongImage"
const val SongsObjectFileName="SongObject"
class FavoritesPresenter(favoritesActivity: FavoritesActivity) :FavoritesContract.FavoritesIPresenter{
    val TAG="FavoritesPresenter"
    //获取song的详细信息
    var view=favoritesActivity
    var model=FavoritesModel()
    override fun getSongs(position: Int?, songRvAdapter: SongRvAdapter) {
        //通知view打开progressbar
        view.progressBarOn()
        if (position == null) {
            Log.e(TAG, "getSongs: position为空")
        } else {
            val x = playListResult.playlist?.get(position)
            val favoriteId = x?.id.toString()
            val url = model.getSongs(favoriteId)
            ///////////////////////////////////////////////////////////////////////////////
            //挂起防止歌单内的歌曲数目过多导致Main线程卡顿
            netThread.launch(Dispatchers.IO) {
                try {
                    songList = readObjectFile(SongsObjectFileName) as MutableList<Song>
                    for (x in songList) {
                        if (!x.image?.exists()!!) {
                            //当有歌曲的封面没抓到的时候
                            mainActivitySp.put {
                                putBoolean("is_song_data_exists", false)
                            }
                            break
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "getSongs: 读取歌单时发生了异常", e)
                    //当读取异常的时候
                    mainActivitySp.put {
                        putBoolean("is_song_data_exists", false)
                    }
                }
                //检擦完毕 判断是否重新缓存数据
                if (!mainActivitySp.getBoolean("is_song_data_exists", false)) {
                    downLoadSongsCathe(url, songRvAdapter)
                    withContext(Dispatchers.Main){
                        view.progressBarOff()
                    }
                } else {
                    try {
                        songRvAdapter.setList(songList)
                        //关闭progressbar
                        withContext(Dispatchers.Main){
                            songRvAdapter.notifyDataSetChanged()
                            view.progressBarOff()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "getSongs: songList读取异常", e)
                    }
                }
            }
        }
    }

    //下载song的缓存文件
    private suspend  fun downLoadSongsCathe(url: String, songRvAdapter: SongRvAdapter) {
            //初始化一下布局
            withContext(Dispatchers.Main){
                songRvAdapter.notifyDataSetChanged()
            }
            //网络请求
            try {
                //解析获取的网络数据
                val respondBody =sendGetRequest(url)
                playListDetailsResult=Gson().fromJson(respondBody,FavoritesModel.PlayListDetails::class.java)
            }catch (e:Exception){
                Log.e(TAG, "getSongs:  Gson解析或者网络请求失败",e )
            }
            val songsData= playListDetailsResult.playlist.tracks
            val mSongList= mutableListOf<Song>()
            //开始解析数据
            for (i in songsData){
                //由于是下载歌曲可能存在歌曲数量比较多可能会出故障 try catch掉免得影响后续操作
                try {
                    var artist:String
                    var image:File
                    //歌曲id
                    val songId:String = i.al.id.toString()
                    //歌曲名称
                    val songName:String = i.al.name
                    //获取歌曲列表
                    val artistsData=i.ar
                    //解析歌曲的内容
                    val z=StringBuilder()
                    for(j in artistsData){
                        z.append(j.name).append('/')
                    }
                    artist=z.toString().substring(0..z.length-2)
                    //获取时间戳
                    val currentTime=System.currentTimeMillis()
                    //下载图片
                    downLoadImage("$BaseSongImageName$currentTime",i.al.picUrl)
                    //加上时间戳防止图片名称重复
                    image=File("$imagePath/$BaseSongImageName$currentTime.jpg")
                    //更新一下数据
                    mSongList.add(Song(artist, image, songName, songId))
                    //切个Main线程更新recyclerview视图
                    withContext(Dispatchers.Main){
                        songRvAdapter.setList(mSongList)
                        //歌单列表刷新
                        songRvAdapter.notifyDataSetChanged()
                    }
                }catch (e:Exception){
                    Log.e(TAG, "getSongs: 第${mSongList.size}首歌曲解析故障", e)
                }
            }
            //下载文件
            try{
                downLoadObjectFile(SongsObjectFileName, songList)
            }catch (e:Exception){
                Log.e(TAG, "getSongs: object下载异常文件",e )
            }
            //最后向MainActivity的sp库里面写入一点配置信息(下一次打开就不去服务端请求数据了)
            mainActivitySp.put {
                putBoolean("is_song_data_exists",true)
            }
    }
}