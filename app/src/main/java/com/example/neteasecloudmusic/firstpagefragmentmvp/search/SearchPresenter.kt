package com.example.neteasecloudmusic.firstpagefragmentmvp.search

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.view.View
import com.example.neteasecloudmusic.mytools.filedownload.filesPath
import com.example.neteasecloudmusic.mytools.musicservice.*
import com.example.neteasecloudmusic.mytools.net.netThread
import com.example.neteasecloudmusic.mytools.net.sendPostRequest
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SearchPresenter(var view:SearchActivity): SearchContract.SearchIPresenter
,ServiceConnection{
    val model =SearchModel()

    val TAG="SearchPresenter"
    //开始寻找
    var musicService:MyMusicService?=null
    //
    private val searchSong= mutableListOf<ServiceSong>()

    override fun beginSearch(
        keyboard: String,
        mAdapter: SearchRvAdapter
    ) {

        //添加播放
        mAdapter.addItemClickListener(object : CallBack{
            override fun onItemClicked(v: View, position: Int) {
                if (!getIsPlaying() && getCurrentPosition()<=0){
                    musicService?.playMusic(searchSong,position)
                }else if (getCurrentPosition()>0 && getIsPlaying()){
                    musicService?.pauseMusic()
                }else if (getCurrentPosition()>0 && !getIsPlaying()){
                    musicService?.pauseToStart()
                }

                //之前没有播放过
                if (getCurrentPosition()<=0){
                    musicService?.playMusic(searchSong,position)
                }
                //之前播放过
                else{
                    //点击的是同一个
                    if (getPosition()==position){
                        //暂停了 继续播放
                        if (!getIsPlaying()){
                            musicService?.pauseToStart()
                        }
                        //暂停播放
                        else{
                            musicService?.pauseMusic()
                        }
                    }
                    //点击的不是同一首音乐
                    else{
                        musicService?.resetMusic()
                        musicService?.playMusic(searchSong,position)
                    }

                }

            }

        })

        //开线程搜索
        netThread.launch (IO){

            val (url,tail) = model.getSearchUrlTail(keyboard)
            val respondBody= sendPostRequest(url, tail)
            //获取到数据
            model.searchResult=Gson().fromJson(respondBody,SearchModel.SearchResult::class.java)

            //解析数据
            val songs=model.searchResult.result.songs
            //
            for (song in songs){
                //由于是下载歌曲可能存在歌曲数量比较多可能会出故障 try catch掉免得影响后续操作
                try {
                    var artist: String
                    //图片地址
                    val image: String = song.al.picUrl
                    //歌曲id
                    val songId: String = song.id.toString()
                    //歌曲名称
                    val songName: String = song.name
                    //获取歌曲对应的歌手列表
                    val artistsData = song.ar
                    //解析歌曲的内容
                    val z = StringBuilder()
                    for (x in artistsData.indices) {
                        z.append(artistsData[x].name)
                        if (x != artistsData.size - 1) {
                            z.append("/")
                        }
                    }
                    //后面是专辑的名称 - 是分隔符号
                    //i.al.name是专辑名称
                    z.append("-${song.name}")
                    artist = z.toString()
                    //更新一下数据
                    searchSong.add(ServiceSong(artist, image, songName, songId))
                    mAdapter.searchList=searchSong
                    withContext(Main){
                        mAdapter.notifyDataSetChanged()
                    }

            }catch (e:Exception){
                    Log.e(TAG, "SearchSong数据解析出问题" )
                }
            }
        }


    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.e(SearchActivity.TAG, "音乐服务器连接失败")
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.e(SearchActivity.TAG, "音乐服务器连接成功" )
        //获取音乐服务器
        musicService=(service as MyMusicService.MyBinder).getService()
    }

}