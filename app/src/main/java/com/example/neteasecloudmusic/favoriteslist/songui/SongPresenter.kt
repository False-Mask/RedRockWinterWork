package com.example.neteasecloudmusic.favoriteslist.songui

import android.content.ComponentName
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.favoriteslist.songs.Song
import com.example.neteasecloudmusic.mytools.musicservice.*
import com.example.neteasecloudmusic.mytools.net.sendGetRequest
import com.example.neteasecloudmusic.view.NextLastSongIcon
import com.example.neteasecloudmusic.view.PlayPauseBar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_song_ui.*
import kotlinx.coroutines.*

var songJob= Job()
var songThread= CoroutineScope(songJob)

class SongPresenter(activity:SongUiActivity):SongContract.SongIPresenter
        ,SeekBar.OnSeekBarChangeListener
        ,ServiceConnection
        ,View.OnClickListener, IServiceBindPresenter
,PlayPauseBar.Click, NextLastSongIcon.Click {
    val TAG="SongPresenter"
    var view=activity
    var model=SongModel()




    //当出现上一首和下一首的情况就不用重新去请求数据了
    private lateinit var songPlayList:MutableList<ServiceSong>
    var position:Int=0

    lateinit var song:ServiceSong
    lateinit var musicService: MyMusicService

    override fun pauseOrPlay(
        it: View?,
        song: Song,
        musicService: MyMusicService
    ) {
        val isPlaying= getIsPlaying()
        //第一次播放 而且以前还没有播放过
        if (!isPlaying && getCurrentPosition()==0){
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
            if (this.song.songId== getMySongId()){
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
        else if (getCurrentPosition()!=0){
            view.iconChangeToPlay()
            //musicService.startMusic(this.song.songId)
        }
    }

    //根据songId播放音乐
    private fun playMusic(songId:String) {
        val url=model.getUrl(songId)
        songThread.launch (Dispatchers.IO){
            //获取音乐的信息
            val songUrl= getMusicUrl(url)
            //播放网络url 3种情况的处理
            when(songUrl){
                // 1 没有找到资源
                "NULL"-> withContext(Dispatchers.Main) {
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
        if (getCurrentPosition()!=0){
            //正在播先停掉
            musicService.resetMusic()
        }
        //初始化播放布局
        //ok地址和歌曲信息给view了
       // view.initView(this.song, this.position, Songs(songPlayList))
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
        if (getCurrentPosition()!=0){
            //正在播先停掉
            musicService.resetMusic()
        }
        //初始化播放布局
        //ok地址和歌曲信息给view了
//        view.initView(this.song, this.position, Songs(songPlayList))
        //播放歌曲
        playMusic(this.song.songId)
        //播放按键变一下
        view.iconChangeToPlay()
    }

    override fun initView(songList: MutableList<Song>, position: Int?) {
    }
    //当点开Activity的时候
//    override fun initView(songList: MutableList<Song>, position: Int?) {
//        //初始化
//        this.position=position!!
//        this.songPlayList=songList
//        this.song=songList[position]
//        //1 现在处于 播放状态
//        if (getIsPlaying()){
//            // 1 播放id与现在activity的id是一致的
//            if (getMySongId()==view.song.songId){
//                //设置progressbar和SeekBar的最大值
//                view.setMusicMaxProgress(getDuration())
//                //设置button
//                view.iconChangeToPlay()
//            }
//            //2 当id与现在activity不一致
//            else{
//                //不管就默认
//            }
//        }
//        //2 现在处于 未播放状态
//        else{
//            //播放过 而且退出了 重新进入界面时的情况
//            if (getCurrentPosition()!=0 && getMySongId()==song.songId){
//                view.setMusicMaxProgress(getDuration())
////                view.setCurrentSeekBarProgressTo(getCurrentPosition())
////                view.setCurrentTextProgressTo(getCurrentPosition())
//            }
//        }
//    }

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
        view.loading()
    }

    override fun onPause() {
        view.iconChangeToPause()
    }

    override fun onResume() {
        val percent= getCurrentPosition().toFloat()/getDuration()
        view.resume(percent)
    }

    override fun onPlayPauseViewClick(v: View) {
        val view=v as PlayPauseBar
        if (view.status== PlayPauseBar.PlayStatus.Pausing){
            musicService.pauseToStart()
            this.view.iconChangeToPlay()
            this.view.resume(getCurrentPosition().toFloat()/ getDuration())
        }else if (view.status== PlayPauseBar.PlayStatus.Playing){
            musicService.pauseMusic()
            this.view.iconChangeToPause()
        }
    }

    //SeekBar的监听
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//        view.setCurrentTextProgressTo(progress)
    }
    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        //第一次使用
        if (!getIsPlaying()&& getCurrentPosition()==0){
            try {
                musicService.playMusic(songPlayList,position)
                view.iconChangeToPlay()
            }catch (e:java.lang.Exception){
                view.sendToast("快去寻找音乐吧~")
            }
        }
        //之前播放过
        else if (getCurrentPosition()!=0){
            //如果播放的歌曲匹配
            if (this.song.songId== getMySongId()){
                //当停止滑动的时候 把音乐拖动播放到指定位置处
                musicService.seekMusicTo(seekBar?.progress!!)
            }
            //不匹配
            else{
                //占据该音乐的播放
                musicService.resetMusic()
                //playMusic(getSongList(), getPosition())
                musicService.playMusic(songPlayList,position)
            }
        }
    }





    //连接不成功
    override fun onServiceDisconnected(name: ComponentName?) {
        Log.e("TAG", "音乐服务连接失败")
    }



    //连接成功
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.e(TAG, "音乐服务连接成功" )
        musicService=(service as MyMusicService.MyBinder ).getService()
    }

    override fun onClick(v: View?) {
        when(v?.id){
           R.id.song_ui_back_icon->{
                view.back()
            }
            R.id.play_pause_icon->{
                //当没有播放过
                if (getCurrentPosition()==0){
                    try {
                        musicService.playMusic(songPlayList,position)
                    }catch (e:java.lang.Exception){
                        view.sendToast("快去寻找音乐吧~")
                    }
                }
                //当播放过
                else{
                    if (getIsPlaying()){
                        musicService.pauseMusic()
                        view.iconChangeToPause()
                    }else{
                        musicService.pauseToStart()
                        view.iconChangeToPlay()
                    }
                }
            }

        }
    }

    fun addData(songs: MutableList<ServiceSong>, position: Int) {
        this.songPlayList=songs
        this.position=position
        this.song=songPlayList[position]
    }

    //但上一首或者下一首按钮被点击了
    override fun onIconClicked(v: View) {
        when(v.id){
            R.id.the_next_song_icon->{
                if (getSongList()!=null){
                    musicService.playNextSong()
                    //更新一下song
                    this.song= getSong()?: ServiceSong()
                }else{
                    view.sendToast("暂无音乐播放列表~")
                }
            }
            R.id.the_last_song_icon->{
                if (getSongList()!=null){
                    musicService.playLastSong()
                    //更新一下song
                    this.song= getSong()?:ServiceSong()
                }else{
                    view.sendToast("暂无音乐播放列表~")
                }
            }
        }
    }

}