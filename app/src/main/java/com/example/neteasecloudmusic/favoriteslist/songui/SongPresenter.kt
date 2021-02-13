package com.example.neteasecloudmusic.favoriteslist.songui

import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.example.neteasecloudmusic.favoriteslist.songs.Song
import com.example.neteasecloudmusic.favoriteslist.songs.Songs
import com.example.neteasecloudmusic.mytools.musicservice.MyMusicService
import com.example.neteasecloudmusic.mytools.musicservice.ServiceSong
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
    //当出现上一首和下一首的情况就不用重新去请求数据了
    private lateinit var songPlayList:MutableList<Song>
    var position:Int=0

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
        val isPlaying=musicService.getIsPlaying()
        //第一次播放 而且以前还没有播放过
        if (!isPlaying && musicService.getCurrentPosition()==0){
            view.iconChangeToPlay()
            //获取网络请求的发送地址
            //////////////////////////////////////////////////////////////////////////////////////
            playMusic(this.song.songId)
        }
        //如果正在播放
        else if (isPlaying){
            //同步一下进度条 可能存在 退出和没退出播放两种情况
            //musicService.beginToRefreshBar()
            //1 播放id相同 -> 暂停
            if (this.song.songId==musicService.getMySongId()){
                //暂停音乐
                musicService.pauseMusic()
                //同步bar最大值
                //view.setSeekBarMaxProgress(musicService.getDuration())
                //改变成暂停图标
                view.iconChangeToPause()
            }
            //2 播放id不同 -> 占据music
            //
            else{
                //使得音乐处于idle状态
                musicService.resetMusic()
                //播放
                playMusic(this.song.songId)
                //改变为播放
                view.iconChangeToPlay()
            }
        }
        //播放了没有退出 暂停状态
        else if (musicService.getCurrentPosition()!=0){
            view.iconChangeToPlay()
            //musicService.startMusic(this.song.songId)
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
                   // musicService.prepareMusicByNet(song.songId)
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
        //1 已经播放到最后一首了 那就播放第一首
        //由于加上了第一个view和最后一个view所以最后一首的坐标是size-2
        if (position==songPlayList.size-2){
            //播放位置和播放音乐都调整到第一首
            this.position=1
            this.song=songPlayList[1]
        }
        //还没到最后一首
        else{
            this.position++
            this.song=songPlayList[position]
        }
        //还可能当前正在播放或者没有在播放
        if (musicService.getCurrentPosition()!=0){
            //正在播先停掉
            musicService.resetMusic()
        }
        //初始化播放布局
        //ok地址和歌曲信息给view了
        view.initView(this.song, this.position, Songs(songPlayList))
        //播放歌曲
        playMusic(this.song.songId)
        //播放按键变一下
        view.iconChangeToPlay()
    }

    override fun lastSong(
        it: View?,
        song: Song,
        musicService: MyMusicService
    ) {
        //1 已经播放到最后一首了 那就播放第一首
        //由于加上了第一个view和最后一个view所以最后一首的坐标是size-2
        if (position==1){
            //播放位置和播放音乐都调整到最后一首
            this.position=songPlayList.size-2
            this.song=songPlayList[position]
        }
        //还没到最后一首
        else{
            this.position--
            this.song=songPlayList[position]
        }
        //还可能当前正在播放或者没有在播放
        if (musicService.getCurrentPosition()!=0){
            //正在播先停掉
            musicService.resetMusic()
        }
        //初始化播放布局
        //ok地址和歌曲信息给view了
        view.initView(this.song, this.position, Songs(songPlayList))
        //播放歌曲
        playMusic(this.song.songId)
        //播放按键变一下
        view.iconChangeToPlay()
    }
    //当点开Activity的时候
    override fun initView(songList: MutableList<Song>, position: Int?) {
        //初始化
        this.position=position!!
        this.songPlayList=songList
        this.song=songList[position]
        //1 现在处于 播放状态
        if (musicService.getIsPlaying()){
            // 1 播放id与现在activity的id是一致的
            if (musicService.getMySongId()==view.song.songId){
                //设置progressbar和SeekBar的最大值
                view.setMusicMaxProgress(musicService.getDuration())
                //设置button
                view.iconChangeToPlay()
            }
            //2 当id与现在activity不一致
            else{
                //不管就默认
            }
        }
        //2 现在处于 未播放状态
        else{
            //播放过 而且退出了 重新进入界面时的情况
            if (musicService.getCurrentPosition()!=0 && musicService.getMySongId()==song.songId){
                view.setMusicMaxProgress(musicService.getDuration())
                view.setCurrentSeekBarProgressTo(musicService.getCurrentPosition())
                view.setCurrentTextProgressTo(musicService.getCurrentPosition())
            }
        }
    }

    override fun onMusicCompletion() {

    }

    //SeekBar的监听
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        view.setCurrentTextProgressTo(progress)
    }
    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        //第一次使用
        if (!musicService.getIsPlaying() && musicService.getCurrentPosition()==0){
            playMusic(song.songId)
            view.iconChangeToPlay()
        }
        //之前播放过
        else if (musicService.getCurrentPosition()!=0){
            //如果播放的歌曲匹配
            if (this.song.songId==musicService.getMySongId()){
                //当停止滑动的时候 把音乐拖动播放到指定位置处
                musicService.seekMusicTo(seekBar?.progress!!)
            }
            //不匹配
            else{
                //占据该音乐的播放
                musicService.resetMusic()
                playMusic(this.song.songId)
            }
        }
    }


//    suspend fun getSong(url:String):ServiceSong{
//        var respondBody= sendGetRequest(url)
//
//    }
}