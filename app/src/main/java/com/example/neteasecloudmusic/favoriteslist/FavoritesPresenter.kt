package com.example.neteasecloudmusic.favoriteslist

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.view.View
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.favoriteslist.songs.Song
import com.example.neteasecloudmusic.favoriteslist.songs.SongRvAdapter
import com.example.neteasecloudmusic.favoriteslist.songs.SongTitle
import com.example.neteasecloudmusic.favoriteslist.songui.SongUiActivity
import com.example.neteasecloudmusic.mainactivitymvp.mainActivitySp
import com.example.neteasecloudmusic.mainactivitymvp.playListResult
import com.example.neteasecloudmusic.mytools.filedownload.downLoadImage
import com.example.neteasecloudmusic.mytools.filedownload.downLoadObjectFile
import com.example.neteasecloudmusic.mytools.filedownload.imagePath
import com.example.neteasecloudmusic.mytools.filedownload.readObjectFile
import com.example.neteasecloudmusic.mytools.musicservice.*
import com.example.neteasecloudmusic.mytools.net.netThread
import com.example.neteasecloudmusic.mytools.net.sendGetRequest
import com.example.neteasecloudmusic.mytools.sharedpreferences.put
import com.example.neteasecloudmusic.view.PlayPauseBar
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

const val BaseSongImageName="SongImage"
const val SongsObjectFileName="SongObject"
class FavoritesPresenter(favoritesActivity: FavoritesActivity) :FavoritesContract.FavoritesIPresenter
,SongRvAdapter.OnClickListener
,ServiceConnection
,IServiceBindPresenter
,View.OnClickListener
,PlayPauseBar.Click{
    val TAG = "FavoritesPresenter"

    //获取song的详细信息
    var view = favoritesActivity
    var model = FavoritesModel()
    var listSong: MutableList<ServiceSong> = mutableListOf()
    lateinit var musicService: MyMusicService

    var extraSongs:ExtraSongs= ExtraSongs()

    override fun getSongs(position: Int?, songRvAdapter: SongRvAdapter) {
            //通知view打开progressbar
            view.progressBarOn()
            if (position == null) {
                Log.e(TAG, "getSongs: position为空")
            } else {
                val x = playListResult.playlist?.get(position)
                val favoriteId = x?.id.toString()
                val url = model.getSongs(favoriteId)
                var mSongList = mutableListOf<Song>()
                //挂起防止歌单内的歌曲数目过多导致Main线程卡顿
                netThread.launch(IO) {
                    try {
                        //加上position区分不同的歌单
                        mSongList = readObjectFile(SongsObjectFileName + position) as MutableList<Song>
                        //当songList长度大于0的时候才判断
                        if (mSongList.size > 0) {
                            for (x in mSongList) {
                                if (!x.image?.exists()!!) {
                                    //当有歌曲的封面没抓到的时候
                                    mainActivitySp.put {
                                        putBoolean("is_song_data_exists", false)
                                    }
                                    break
                                }
                            }
                        }
                        //等于0直接重新下载
                        else {
                            mainActivitySp.put {
                                putBoolean("is_song_data_exists", false)
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
                    //有image损坏-> 下载
                    if (!mainActivitySp.getBoolean("is_song_data_exists", false)) {
                        //下载歌曲文件的缓存
                        downLoadSongsCathe(url, songRvAdapter, position)
                        withContext(Main) {
                            view.progressBarOff()
                        }
                    } else {
                        try {
                            songRvAdapter.setLocalList(mSongList)
                            //关闭progressbar
                            withContext(Main) {
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

        //直接获取歌曲
        override fun getPlayList(playListId: String, songRvAdapter: SongRvAdapter, intent: Intent) {
            view.progressBarOn()
//            val avatarUrl=intent.extras?.getString("avatarUrl")
//            val nickname=intent.extras?.getString("nickname")
//            val name=intent.extras?.getString("name")
//            val description=intent.extras?.getString("description")
//            val coverImgUrl=intent.extras?.getString("coverImgUrl")
//            songRvAdapter.addTitleData(SongTitle(avatarUrl,nickname,name,description,coverImgUrl))

            netThread.launch(IO) {
                sendTheFirst(playListId,songRvAdapter)
            }
        }

    //发送第一个网络请求
    private suspend fun sendTheFirst(playListId: String, songRvAdapter: SongRvAdapter) {
        //网络请求
        val url = model.getSongs(playListId)
        try {
            //解析获取的网络数据
            val respondBody = sendGetRequest(url)
            playListDetailsResult = Gson().fromJson(respondBody, FavoritesModel.PlayListDetails::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "getSongs:  Gson解析或者网络请求失败", e)
        }

        val songsData = playListDetailsResult.playlist.tracks
        val mSongList = mutableListOf<ServiceSong>()

        val nickname= playListDetailsResult.playlist.creator.nickname
        val avatarUrl= playListDetailsResult.playlist.creator.avatarUrl
        val name= playListDetailsResult.playlist.name
        val description= playListDetailsResult.playlist.description
        val coverImgUrl= playListDetailsResult.playlist.coverImgUrl
        songRvAdapter.addTitleData(SongTitle(avatarUrl,nickname,name,description,coverImgUrl))
        withContext(Main){
            songRvAdapter.notifyDataSetChanged()
        }
        //判断是否完整
        val getSongCount= playListDetailsResult.playlist.tracks.size
        val tracksId= playListDetailsResult.playlist.trackIds
        val totalSongCount=playListDetailsResult.playlist.trackCount


        //判断是否需要半路开溜
        if (totalSongCount==0){
            withContext(Main){
                view.progressBarOff()
            }
            return
        }

        //循环解析数据
        //开始解析数据
        for (i in songsData) {
            //由于是下载歌曲可能存在歌曲数量比较多可能会出故障 try catch掉免得影响后续操作
            try {
                var artist: String
                //图片地址
                val image: String = i.al.picUrl
                //歌曲id
                val songId: String = i.id.toString()
                //歌曲名称
                val songName: String = i.name
                //获取歌曲对应的歌手列表
                val artistsData = i.ar
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
                z.append("-${i.al.name}")
                artist = z.toString()
                //更新一下数据
                listSong.add(ServiceSong(artist, image, songName, songId))
                mSongList.add(ServiceSong(artist, image, songName, songId))

                //切个Main线程更新recyclerview视图
                    withContext(Main) {
                    songRvAdapter.setNetList(mSongList)
                    //歌单列表刷新
                    songRvAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e(TAG, "歌曲列表网络解析故障", e)
            }
        }
        if (getSongCount<totalSongCount){
            sendTheSecond(getSongCount,totalSongCount,tracksId,mSongList,songRvAdapter)
        }

        withContext(Main){
            view.progressBarOff()
        }

    }

    private suspend fun sendTheSecond(songCount: Int, totalSongCount: Int, tracksId: List<FavoritesModel.TrackId>, mSongList: MutableList<ServiceSong>, songRvAdapter: SongRvAdapter) {
        //先通过append将所有的trackid拼接起来
        val stringBuilder=StringBuilder()
        for (x in tracksId){
            if (x.id!= null){
                stringBuilder.append("${x.id},")
            }
        }
        val string=stringBuilder.substring(0,stringBuilder.length-1)
        Log.e(TAG, "sendTheSecond: $string")
        //获取发送的地址
        val url=model.getTheSecondUrl(string)
        //开始发送
        val respondBody=sendGetRequest(url)
        extraSongs=Gson().fromJson(respondBody,ExtraSongs::class.java)
        for (x in songCount until extraSongs.songs.size){

            val i = extraSongs.songs[x]
            try {
                var artist: String
                //图片地址
                val image: String = i.al.picUrl
                //歌曲id
                val songId: String = i.id.toString()
                //歌曲名称
                val songName: String = i.name
                //获取歌曲对应的歌手列表
                val artistsData = i.ar
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
                z.append("-${i.al.name}")
                artist = z.toString()
                //更新一下数据
                listSong.add(ServiceSong(artist, image, songName, songId))
                mSongList.add(ServiceSong(artist, image, songName, songId))

                //切个Main线程更新recyclerview视图
                withContext(Main) {
                    songRvAdapter.setNetList(mSongList)
                    //歌单列表刷新
                    songRvAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e(TAG, "歌曲列表网络解析故障", e)
            }
        }
    }

    //下载song的缓存文件
        private suspend fun downLoadSongsCathe(url: String, songRvAdapter: SongRvAdapter, position: Int) {
            //初始化一下布局
            //网络请求
            try {
                //解析获取的网络数据
                val respondBody = sendGetRequest(url)
                playListDetailsResult = Gson().fromJson(respondBody, FavoritesModel.PlayListDetails::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "getSongs:  Gson解析或者网络请求失败", e)
            }
            val songsData = playListDetailsResult.playlist.tracks
            val mSongList = mutableListOf<Song>()
            //开始解析数据
            for (i in songsData) {
                //由于是下载歌曲可能存在歌曲数量比较多可能会出故障 try catch掉免得影响后续操作
                try {
                    var artist: String
                    var image: File
                    //歌曲id
                    val songId: String = i.id.toString()
                    //歌曲名称
                    val songName: String = i.name
                    //获取歌曲对应的歌手列表
                    val artistsData = i.ar
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
                    z.append("-${i.al.name}")
                    artist = z.toString()
                    //获取时间戳
                    val currentTime = System.currentTimeMillis()
                    //下载图片
                    downLoadImage("$BaseSongImageName$currentTime", i.al.picUrl)
                    //加上时间戳防止图片名称重复
                    image = File("$imagePath/$BaseSongImageName$currentTime.jpg")
                    //更新一下数据
                    mSongList.add(Song(artist, image, songName, songId))
                    //切个Main线程更新recyclerview视图
                    withContext(Main) {
                        songRvAdapter.setLocalList(mSongList)
                        //歌单列表刷新
                        songRvAdapter.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "getSongs: 第${mSongList.size}首歌曲解析故障", e)
                }
            }
            //下载文件
            try {
                downLoadObjectFile(SongsObjectFileName + position, mSongList)
            } catch (e: Exception) {
                Log.e(TAG, "getSongs: object下载异常文件", e)
            }
            //最后向MainActivity的sp库里面写入一点配置信息(下一次打开就不去服务端请求数据了)
            mainActivitySp.put {
                putBoolean("is_song_data_exists", true)
            }
        }

        //音乐被点击了
       override fun itemClicked(position: Int) {
        }



    //rv的listener
    override fun songClicked(view: View, position: Int) {
        if (position in 1 .. listSong.size){
//            view.song_name
//            view.singer_name
//            view.song_position
//            this.view.apply {
//                setTextColor(view.song_name,R.color.song_playing_color)
//                setTextColor(view.singer_name,R.color.song_playing_color)
//                setTextColor(view.song_position,R.color.song_playing_color)
//            }
            val clickId=listSong[position-1].songId
            //正在播放音乐
            if (getIsPlaying()){
                //播放的同一首暂停
                if (clickId== getMySongId()){
                    musicService.pauseMusic()
                }
                //否者重新播放
                else{
                    musicService.resetMusic()
                    musicService.playMusic(listSong,position-1)
                }
            }
            //没有播放音乐
            else{
                //播放后暂停
                if (getCurrentPosition()>0){
                    musicService.pauseToStart()
                }
                //单纯的没播放 那就开始播放
                else{
                    musicService.playMusic(listSong,position-1)
                }
            }
        }


    }

    override fun titleClicked(view: View) {

    }

    override fun tailClicked(view: View) {

    }







    //连接不成功
    override fun onServiceDisconnected(name: ComponentName?) {
        Log.e(TAG, "音乐服务连接失败")
    }

    //连接成功
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.e(TAG, "音乐服务连接成功")

        musicService = (service as MyMusicService.MyBinder).getService()
        //添加到视图里面
        //musicService.addBindView(view)

        Log.d(TAG, "onServiceConnected: $musicService")
    }

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
        view.preparing()
    }

    override fun onPause() {
        //musicService.pauseMusic()
        view.pause()
    }

    override fun onResume() {
        //musicService.pauseToStart()
        view.resume(getCurrentPosition().toFloat()/ getDuration())
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bottom_pause_or_play->{
                //之前有过播放
                if(getCurrentPosition()>0){
                    if (getIsPlaying()){
                        musicService.pauseMusic()
                    }
                    //之前没有播放
                    else{
                        musicService.pauseToStart()
                    }
                }
                //之前没有过播放
                else{

                }
            }
            R.id.bottom_song_image,R.id.bottom_song_name->{
                val intent=Intent(view,SongUiActivity::class.java)
                intent.putExtra("songs",ServiceSongList(listSong))
                view.loopToSongUi(intent)
            }
        }
    }

    override fun onPlayPauseViewClick(v: View) {
        val v2=v as PlayPauseBar
        if (v2.status==PlayPauseBar.PlayStatus.Playing){
            musicService.pauseMusic()
            onPause()
        }else if(v2.status==PlayPauseBar.PlayStatus.Pausing){
            musicService.pauseToStart()
            onResume()
        }
    }
}