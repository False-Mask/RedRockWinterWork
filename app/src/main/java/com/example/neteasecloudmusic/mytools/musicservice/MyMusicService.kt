package com.example.neteasecloudmusic.mytools.musicservice

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.neteasecloudmusic.favoriteslist.songui.SongUiActivity
import kotlinx.coroutines.*
//存放一个歌曲的id这样就可以时刻知道正在播放
private var MySongId="NULL"
var MusicRefreshJob= Job()
var MusicRefreshThread= CoroutineScope(MusicRefreshJob)
private var isViewAlive = false
//单例了 免得到时候 重复播放
private val mediaPlayer:MediaPlayer= MediaPlayer()
class MyMusicService : Service()
        //出错的监听                    //缓冲监听
        , MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener
        //播放完成的监听                //准备完成
        , MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener
        //进度拉取完成的监听
        , MediaPlayer.OnSeekCompleteListener {

    //Binder可以看成是服务和用户的连接
    //他实现了IBinder 可以通过强转得到IBinder
    inner class MyBinder: Binder() {
        fun getService(): MyMusicService {
            return this@MyMusicService
        }
    }

    companion object {
        fun setViewStatus(boolean: Boolean){
            isViewAlive=boolean
        }
    }

    fun getMySongId(): String {
        return MySongId
    }

    val TAG="MyMusicService"
    //检测是否已经开始动态刷新seekBar(也可认为是是否正在播放)
    var isStarted = false


    //获取一些MediaPlayer基本属性
    fun getIsPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }
    fun getIsLooping(): Boolean {
        return mediaPlayer.isLooping
    }
    fun getDuration(): Int {
        return mediaPlayer.duration
    }
    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    private val binder=MyBinder()

    lateinit var songUiActivity:SongUiActivity

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //创建绑定后处于粘着状态 程序不挂我不挂
        return START_STICKY
    }
    //准备播放
    fun prepareMusicByNet(url:String,songId: String){
            try {
                mediaPlayer.apply {
                    //配置播放信息
                    setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                    //设置播放源
                    setDataSource(url)
                    //准备播放(异步准备)
                    prepareAsync()
                    //配置一堆监听
                    mediaPlayer.apply {
                        setOnPreparedListener(this@MyMusicService)
                        setOnCompletionListener(this@MyMusicService)
                        setOnErrorListener(this@MyMusicService)
                        setOnBufferingUpdateListener(this@MyMusicService)
                        setOnSeekCompleteListener(this@MyMusicService)
                    }
                    //设置播放id
                    MySongId=songId
                }
            }catch (e:Exception){
                Log.e(TAG, "MediaPlayer准备过程中出现异常",e )
            }
    }
    //开始播放音乐
    fun startMusic(songId: String){
        MySongId=songId
        if (!mediaPlayer.isPlaying){
            mediaPlayer.start()
        }
        beginToRefreshBar()
    }
    //暂停音乐
    fun pauseMusic() {
        mediaPlayer.pause()
    }
    //跳转播放
    fun seekMusicTo(progress: Int) {
        mediaPlayer.seekTo(progress)
    }
    //停止播放
    fun stopMusic(){
        mediaPlayer.stop()
    }
    //退出播放
    fun resetMusic(){
        mediaPlayer.reset()
    }




    //错误时的回调
    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        //true表示继续播放 false表示继续播放 true后调用complete
        Log.e(TAG, "onError: " )
        return true
    }
    //缓存更新的时候回调
    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        songUiActivity.setBufferedBarPercent(percent)
    }
    override fun onCompletion(mp: MediaPlayer?) {
        songUiActivity.iconChangeToPlay()
    }

    //准备完成的回调
    override fun onPrepared(mp: MediaPlayer?) {
        //设置进度条
        songUiActivity.setSeekBarMaxProgress(mediaPlayer.duration)
        mp?.start()
        beginToRefreshBar()
    }



    //同步SeekBar
    fun beginToRefreshBar() {
        //如果没开始->开始刷新
        isStarted=getIsPlaying()
        if (isStarted&&isViewAlive) {
            MusicRefreshThread.launch (Dispatchers.IO){
                while (mediaPlayer.isPlaying && isViewAlive){
                    delay(1000)
                    var mService=this@MyMusicService
                    withContext(Dispatchers.Main){
                        val current=mediaPlayer.currentPosition
                        //设置progressbar和text的内容
                        mService.songUiActivity.setCurrentTextProgressTo(current)
                        mService.songUiActivity.setCurrentSeekBarProgressTo(current)
                        mService.songUiActivity.setBufferedBarPercent(current)
                    }
                }
                //结束了可以重新开始
                isStarted=false
            }
        }
    }

    override fun onSeekComplete(mp: MediaPlayer?) {
    }

    fun addBindView(songUiActivity: SongUiActivity) {
        this.songUiActivity=songUiActivity
    }
}