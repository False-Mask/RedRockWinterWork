package com.example.neteasecloudmusic.mytools.musicservice

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.neteasecloudmusic.favoriteslist.songui.songThread
import com.example.neteasecloudmusic.mytools.net.sendGetRequest
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.io.Serializable

//刷新音乐的协程
private var musicService=MyMusicService()
var MusicRefreshJob= Job()
var MusicRefreshThread= CoroutineScope(MusicRefreshJob)
private var songUiList= mutableListOf<IServiceBindView>()
private var position:Int=0
private var songList:MutableList<ServiceSong>?=null
private var song:ServiceSong?=null

fun getSongList(): MutableList<ServiceSong>? {
    return songList
}
fun getSong(): ServiceSong? {
    return song
}
fun getPosition(): Int {
    return position
}

fun addView(view:IServiceBindView){
    songUiList.add(view)
}
fun reMoveView(view: IServiceBindView){
    songUiList.remove(view)
}

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

fun getMySongId(): String {
    return MySongId
}

//顶层方法 主要是告诉Service View还活着没 死了就不要调用刷新他丫的视图
//免得闪退

//存放一个歌曲的id这样就可以时刻知道正在播放
private var MySongId="NULL"

//单例了 免得到时候 重复播放
private val mediaPlayer:MediaPlayer= MediaPlayer()

class MyMusicService : Service()
        //出错的监听                    //缓冲监听
        , MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener
        //播放完成的监听                //准备完成
        , MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener
        //进度拉取完成的监听
        , MediaPlayer.OnSeekCompleteListener {

    //默认playType为正序播放下一首
    var playType:PlayType=PlayType.PlayInOrderNext
    //防止多次刷新
    var isRefreshing=false

    //变量
    val TAG="MyMusicService"

    private val binder=MyBinder

    //外部加入的song
//    private var songList:MutableList<ServiceSong>?=null
//
//    private var song:ServiceSong?=null
    //绑定的songPre

    private lateinit var songPre: IServiceBindPresenter


    var songUi:IServiceBindView?=null

    //service放回的给view的data

    private lateinit var serviceData:ServiceBackData

    //是否刷新

    private var doRefreshing=false

    private val baseUrl="http://sandyz.ink:3000"

    private val url="/song/url?id="


    //service自己发送获得的song的数据

    private lateinit var serviceSongData:SongData


    //Service的几个生命周期所对应的回调方法
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

    //Binder可以看成是服务和用户的连接
    //他实现了IBinder 可以通过强转得到IBinder

    //确保从始至终只有一个service的实例服务activity
    //这里通过单例实现了musicService的单例(防止重复播放)
    object MyBinder: Binder() {
        fun getService(): MyMusicService {
            return musicService
        }
    }

    enum class PlayType{
        Loopering,PlayInOrderNext,PlayInOrderLast
    }

    //添加绑定的视图
    fun addBindView(songUiActivity:IServiceBindView){
        this.songUi=songUiActivity
    }

    fun addBindPresenter(songPresenter:IServiceBindPresenter){
        this.songPre=songPresenter
    }

    fun doForAll(block:IServiceBindView.()->Unit){
        for (x in songUiList){
            x.block()
        }
    }

    //发送并获取网络歌曲的url
    private suspend fun getMusicUrl(url: String): String {
        return try {
            val resultBody=sendGetRequest(url)
            serviceSongData=Gson().fromJson(resultBody, SongData::class.java)
            //表示请求成功 并且还拿到了链接
            if (serviceSongData.code==200 && serviceSongData.data[0].code!=404){
                serviceSongData.data[0].url
            }
            else{
                "NULL"
            }
        }catch (e:Exception){
            Log.e(TAG, "请求音乐链接错误",e )
            "ERRO"
        }
    }



    //准备并播放音乐播放
    private fun prepareMusicByNet(songUrl: String){
                /**
                 *  var songName: String,
                    var singer: String,
                    var Imageurl: String,
                    var duration: Int,
                    var currentTime: Int,
                    var songId: String
                 */

            try {
                mediaPlayer.apply {
                    //配置播放信息
                    setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                    //设置播放源
                    setDataSource(songUrl)
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
                }
            }catch (e:Exception){
                Log.e(TAG, "MediaPlayer准备过程中出现异常",e )
            }
    }
    //开始播放音乐
   private fun startMusic(){
        if (!mediaPlayer.isPlaying){
            mediaPlayer.start()
            //开始刷新 每1秒刷新一次
            //允许刷新
            doRefreshing=true
            refreshMusicPlayerStatus()
        }
    }
    //start
     fun pauseToStart(){
        mediaPlayer.start()
    }

    //根据songId播放音乐 播放单曲不具有 自动切换下一曲的功能
    fun playMusic(msong: ServiceSong) {
        //自动初始song
        song=msong
        //同步当前播放的id
        MySongId= msong.songId
        val url1=baseUrl+url+msong.songId
        songThread.launch (IO){
            //获取音乐的信息
            val songUrl= getMusicUrl(url1)
            //播放网络url 3种情况的处理
            serviceData= ServiceBackData()
            serviceData.apply {
                Imageurl= msong.image
                this.songId= msong.songId
                singer= msong.artist
                songName= msong.songName
            }
            when(songUrl){
                // 1 没有找到资源
                "NULL"-> withContext(Main){
                    //songUi?.sendToast("未找到该资源")
                    //把图标变回来
                    //songUi?.iconChangeToPause()
                    doForAll {
                        sendToast("未找到该资源")
                        iconChangeToPause()
                    }
                    comeAcrossError()
                }
                // 2 解码错误
                "ERRO"-> withContext(Main){
                    //songUi?.sendToast("音乐解码错误")
                    //把图标变回来
                    //songUi?.iconChangeToPause()
                    doForAll {
                        iconChangeToPause()
                    }
                    comeAcrossError()
                }
                // 3 成功
                else-> {
                    withContext(Main){
                        prepareMusicByNet(songUrl)
                    }
                }
            }

        }
    }

    //自动关联list播放完以后自动依照模式播放
    fun playMusic(msongList: MutableList<ServiceSong>?, mposition: Int){
        songList=msongList
        position=mposition
        playMusic(msongList!![mposition])
    }


    private fun refreshMusicPlayerStatus() {
        if (!isRefreshing){
            MusicRefreshThread.launch (IO){

                Log.e(TAG, "${Thread.currentThread()}"+"while 外部")
                isRefreshing=true

                while (true){
                    Log.e(TAG, "${Thread.currentThread()}"+"while 内部")
                    if (!doRefreshing){
                        break
                    }
                    //每一秒刷新一下
                    delay(1000)
                    if (doRefreshing){
                        withContext(Main){
                            doForAll {
                                serviceRefresh  (serviceData.songName,
                                        serviceData.singer,
                                        serviceData.Imageurl,
                                        getDuration(),
                                        getCurrentPosition(),
                                        serviceData.songId)
                            }
                        }
                    }
                }
                isRefreshing=false
            }
        }
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
    //退出播放状态 进入reset状态
    fun resetMusic(){
        //停止下来
        doRefreshing=false
        mediaPlayer.reset()
    }


    //错误时的回调
    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        //true表示继续播放 false表示继续播放 true后调用complete
        comeAcrossError()
        return true
    }

   private fun comeAcrossError(){
        when (playType) {
            PlayType.PlayInOrderLast -> {
                //播放下一个
                playNextSong()
            }
            PlayType.PlayInOrderLast -> {
                //播放上一个
                playLastSong()
            }
            PlayType.Loopering -> {
                //停止播放
                resetMusic()
            }
            else->{}
        }
    }

    //缓存更新的时候回调
    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        songUi?.setBufferedProgress(percent)
    }
    override fun onCompletion(mp: MediaPlayer?) {
        //音乐播放完成
        //表明添加的是songList
        //那就自动播放内容
        //songPre.onMusicCompletion()
        when(playType){
            PlayType.PlayInOrderNext-> playNextSong()
            PlayType.PlayInOrderLast-> playLastSong()
            PlayType.Loopering-> playLoop()
            else->{}
        }
    }

    fun playLoop() {
        if (song!=null){
            playMusic(song!!)
        }
    }

    //播放下一首
    fun playLastSong() {
        if(songList!=null){
            if (position==0){
                position=songList!!.size-1
            }else{
                position--
            }
            if (getCurrentPosition()!=0){
                resetMusic()
            }
            //准备并播放歌曲
            playMusic(songList!!,position)
        }
    }

    //播放下一首
    fun playNextSong() {
        if (songList!=null){
            if(position<songList!!.size-1){
                position++
            }else{
                //最后一首
                position=0
            }
            //播放过的
            if (getCurrentPosition()!=0){
                resetMusic()
            }
            //准备并播放音乐
            playMusic(songList!!,position)
        }
    }

    //准备完成的回调
    override fun onPrepared(mp: MediaPlayer?) {

        startMusic()
        doForAll {
            setMusicMaxProgress(getDuration())
        }
    }

    //拖动完成
    override fun onSeekComplete(mp: MediaPlayer?) {
        songPre.onMusicSeekComplete(mp)
    }

    fun reMoveView() {
        songUi= null
    }

    //返回的数据
    data class ServiceBackData(var songName: String="",
                               var singer: String="",
                               var Imageurl: String="",
                               var duration: Int=0,
                               var currentTime: Int=0,
                               var songId: String=""){

    }
}

//服务端的song
//由于在线播放的内容太多了
//本地下载多半是行不通的只能是播放哪Glide动态加载哪里
data class ServiceSong(var artist:String="",
                       var image:String="",
                       var songName:String="",
                       var songId:String=""): Serializable {
    companion object{
        const val serialVersionUID = 1000L
    }
}

data class ServiceSongList(
        var mSongs:MutableList<ServiceSong>
):Serializable{
    companion object{
        const val serialVersionUID = 1001L
    }
}
//歌曲的详细信息
data class SongData(
        var code: Int = 0,
        var `data`: List<Data> = listOf()
)

data class Data(
        var br: Int = 0,
        var canExtend: Boolean = false,
        var code: Int = 0,
        var encodeType: String = "",
        var expi: Int = 0,
        var fee: Int = 0,
        var flag: Int = 0,
        var freeTimeTrialPrivilege: FreeTimeTrialPrivilege = FreeTimeTrialPrivilege(),
        var freeTrialInfo: Any? = Any(),
        var freeTrialPrivilege: FreeTrialPrivilege = FreeTrialPrivilege(),
        var gain: Int = 0,
        var id: Int = 0,
        var level: String = "",
        var md5: String = "",
        var payed: Int = 0,
        var size: Int = 0,
        var type: String = "",
        var uf: Any? = Any(),
        var url: String = "",
        var urlSource: Int = 0
)

data class FreeTimeTrialPrivilege(
        var remainTime: Int = 0,
        var resConsumable: Boolean = false,
        var type: Int = 0,
        var userConsumable: Boolean = false
)

data class FreeTrialPrivilege(
        var resConsumable: Boolean = false,
        var userConsumable: Boolean = false
)