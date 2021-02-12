package com.example.neteasecloudmusic.favoriteslist.songui

import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.example.neteasecloudmusic.favoriteslist.songs.Song
import com.example.neteasecloudmusic.mytools.musicservice.MyMusicService
import com.example.neteasecloudmusic.mytools.net.sendGetRequest
import com.google.gson.Gson
import kotlinx.coroutines.*

var songJob= Job()
var songThread= CoroutineScope(songJob)
class SongPresenter(activity:SongUiActivity):SongContract.SongIPresenter
        ,SeekBar.OnSeekBarChangeListener{
    val TAG="SongPresenter"
    var view=activity
    var model=SongModel()
    lateinit var song:Song
    lateinit var musicService: MyMusicService

    fun addMusicService(musicService: MyMusicService){
        this.musicService=musicService
    }

    override fun pauseOrPlay(
        it: View?,
        song: Song,
        musicService: MyMusicService
    ) {
        this.song=song
        val isPlaying=musicService.getIsPlaying()
        //第一次播放 而且以前还没有播放过
        if (!isPlaying && musicService.getCurrentPosition()==0){
            view.iconChangeToPlay()
            //获取网络请求的发送地址
            //////////////////////////////////////////////////////////////////////////////////////
            playMusic(song.songId)
        }
        //如果正在播放
        else if (isPlaying){
            //同步一下进度条 可能存在 退出和没退出播放两种情况
            musicService.beginToRefreshBar()
            //1 播放id相同 -> 暂停
            if (song.songId==musicService.getMySongId()){
                //暂停音乐
                musicService.pauseMusic()
                //同步bar最大值
                view.setSeekBarMaxProgress(musicService.getDuration())
                //改变成暂停图标
                view.iconChangeToPause()
            }
            //2 播放id不同 -> 占据music
            else{
                //使得音乐处于idle状态
                musicService.stopMusic()
                //播放
                playMusic(song.songId)
                //改变为播放
                view.iconChangeToPlay()
            }
        }
        //播放了没有退出
        else if (musicService.getCurrentPosition()!=0){
            view.iconChangeToPlay()
            musicService.startMusic(song.songId)
        }
    }

    //根据songId播放音乐
    private fun playMusic(songId:String) {
        var url=model.getUrl(songId)
        songThread.launch (Dispatchers.IO){
            //获取音乐的信息
            var songUrl= getMusicUrl(url)
            //播放网络url 3种情况的处理
            when(songUrl){
                // 1 没有找到资源
                "NULL"-> withContext(Dispatchers.Main){
                    view.sendToast("未找到该资源")
                    //把图标变回来
                    view.iconChangeToPause()
                }
                // 2 解码错误
                "ERRO"-> withContext(Dispatchers.Main){
                    view.sendToast("音乐解码错误")
                    //把图标变回来
                    view.iconChangeToPause()
                }
                // 3 成功
                else-> {
                    //设置bar的最大值
                    //view.setSeekBarMaxProgress(musicService.getDuration())
                    musicService.prepareMusicByNet(songUrl,song.songId)
                }
            }
        }
    }

    //发送并获取网络歌曲的url
    private suspend fun getMusicUrl(url: String): String {
        return try {
            val resultBody=sendGetRequest(url)
            val songData=Gson().fromJson(resultBody, SongModel.SongData::class.java)
            //表示请求成功 并且还拿到了链接
            if (songData.code==200 && songData.data[0].code!=404){
                songData.data[0].url
            }
            else{
                "NULL"
            }
        }catch (e:Exception){
            Log.e(TAG, "请求音乐链接错误",e )
            "ERRO"
        }
    }

    override fun nextSong(
        it: View?,
        song: Song,
        musicService: MyMusicService
    ) {
        this.song=song
    }

    override fun lastSong(
        it: View?,
        song: Song,
        musicService: MyMusicService
    ) {
        this.song=song
    }
    //当点开Activity的时候
    override fun initView() {
        //1 播放状态->
        if (musicService.getIsPlaying()){
            // 1 播放id与现在activity的id是一致的
            if (musicService.getMySongId()==view.song.songId){
                //开始刷新Bar
                musicService.beginToRefreshBar()
                //设置progressbar和SeekBar的最大值
                view.setSeekBarMaxProgress(musicService.getDuration())
                //设置button
                view.iconChangeToPlay()
            }
            //2 当id与现在activity不一致
            else{
                //不管就默认
            }
        }
        //2 未播放状态
        else{
            //不管
            if (musicService.getCurrentPosition()!=0){
                view.setSeekBarMaxProgress(musicService.getDuration())
                view.setCurrentSeekBarProgressTo(musicService.getCurrentPosition())
                view.setCurrentTextProgressTo(musicService.getCurrentPosition())
            }
        }
    }

    //SeekBar的监听
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        view.setCurrentTextProgressTo(progress)
    }
    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        //第一次使用
        if (!musicService.getIsPlaying() && musicService.getCurrentPosition()==0){
            musicService.startMusic(song.songId)
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        musicService.seekMusicTo(seekBar?.progress!!)
    }
}