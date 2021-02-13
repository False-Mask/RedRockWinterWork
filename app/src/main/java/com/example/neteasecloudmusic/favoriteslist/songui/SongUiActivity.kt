package com.example.neteasecloudmusic.favoriteslist.songui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.favoriteslist.songs.Song
import com.example.neteasecloudmusic.favoriteslist.songs.Songs
import com.example.neteasecloudmusic.mytools.changMsIntoMinutesAndSecond
import com.example.neteasecloudmusic.mytools.musicservice.IServiceBindView
import com.example.neteasecloudmusic.mytools.musicservice.MyMusicService
import com.example.neteasecloudmusic.mytools.toast.MyToast
import kotlinx.android.synthetic.main.activity_song_ui.*

class SongUiActivity : AppCompatActivity(),SongContract.SongIView
    //绑定Service实现的接口
    , IServiceBindView {
    private var presenter:SongPresenter=SongPresenter(this@SongUiActivity)
    val TAG="SongUiActivity"

    //音乐服务的对象
    var musicService:MyMusicService?=null
    //当前点击的歌曲
    lateinit var song:Song
    //当前歌曲的位置
    var position:Int=0
    //当前播放的歌单
    lateinit var songs:Songs

    var playListId:String =""

    //Music播放的连接
    private var connection=object : ServiceConnection {
        //连接不成功
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e("TAG", "音乐服务连接失败")
        }
        //连接成功
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(TAG, "音乐服务连接成功" )
            musicService=(service as MyMusicService.MyBinder ).getService()
            //设置musicService
            presenter.addMusicService(musicService!!)
            musicService?.addBindView(this@SongUiActivity)
            Log.d(TAG, "onServiceConnected: "+musicService)

            //初始化视图了
            initView(song,position,songs)
            //设置点击事件的处理
            //返回键
            song_ui_back_icon.setOnClickListener{
                finish()
            }
            //  播放/暂停键
            play_pause_icon.setOnClickListener{
                presenter.pauseOrPlay(it, song,musicService!!)
            }
            //下一首
            the_next_song_icon.setOnClickListener{
                presenter.nextSong(it,song,musicService!!)
            }

            //上一首
            the_last_song_icon.setOnClickListener{
                presenter.lastSong(it,song,musicService!!)
            }
            //SeekBar的监控
            song_ui_seek_bar.setOnSeekBarChangeListener(presenter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_ui)

        //告诉音乐服务器 爷来了

        //获取activity的数据
        song=intent.extras?.get("song") as Song
        position= intent.extras?.getInt("position")!!
        songs=intent.extras?.getSerializable("songs")!! as Songs
     }

    fun initView(song: Song, position: Int?, songs: Songs) {
        //初始化text
        song_ui_song_name.text=song.songName
        song_ui_singer_name.text=song.artist
        //加载图片
        Glide.with(this).load(song.image).into(song_ui_song_image_view)
        //播放信息的处理
        //把activity给的歌单传给presenter
        presenter.initView(songs.songs,position)
    }

    //图标由pause状态改为play
    override fun iconChangeToPlay() {
        play_pause_icon.setImageResource(R.drawable.play)
    }
    //图标由play改为pause
    override fun iconChangeToPause() {
        play_pause_icon.setImageResource(R.drawable.pause)
    }

    override fun setBufferedProgress(percent: Int) {

    }

    //发送toast
    override fun sendToast(songUrl: String) {
        MyToast().sendToast(this,songUrl,Toast.LENGTH_SHORT)
    }

    override fun setCurrentTextProgressTo(current: Int) {

    }

    override fun setSeekBarMaxProgress(duration: Int) {
    }

    override fun setCurrentSeekBarProgressTo(current: Int) {
    }

    override fun setBufferedBarPercent(percent: Int) {
    }

    //绑定
    private fun bindMusicService() {
        val intent= Intent(this, MyMusicService::class.java)
        //绑定后自动创建
        bindService(intent,connection, Context.BIND_AUTO_CREATE)
    }

    override fun setMusicMaxProgress(duration: Int) {
        //设置SeekBar的最大值
        song_ui_seek_bar.max=duration
//        //设置SeekBar下的textView的显示
//        total_song_progress.text=changMsIntoMinutesAndSecond(duration)
        progress_bar.max=duration
        total_song_progress.text= changMsIntoMinutesAndSecond(duration)
    }

    //改变SeekBar对应的位置



    override fun serviceRefresh(songName: String, singer: String, imageurl: String, duration: Int, currentTime: Int, songId: String) {
        //Glide.with(this).load(imageurl).into()

    }

    override fun onMusicCompletion() {
        presenter.onMusicCompletion()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        //绑定
        bindMusicService()
    }

    override fun onStop() {
        super.onStop()
        //告诉音乐服务端 爷走了
        //MyMusicService.setViewStatus(false)
        //断开连接
        unbindService(connection)
    }
}