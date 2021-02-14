package com.example.neteasecloudmusic.favoriteslist.songui

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.mytools.changMsIntoMinutesAndSecond
import com.example.neteasecloudmusic.mytools.musicservice.*
import com.example.neteasecloudmusic.mytools.toast.MyToast
import kotlinx.android.synthetic.main.activity_song_ui.*
import kotlinx.android.synthetic.main.rv_song_list_item.*
import java.lang.Exception

class SongUiActivity : AppCompatActivity(),SongContract.SongIView
    //绑定Service实现的接口
    , IServiceBindView {
    private var presenter:SongPresenter=SongPresenter(this@SongUiActivity)
    val TAG="SongUiActivity"
    //当前点击的歌曲
    var song:ServiceSong?=null
    //当前歌曲的位置
    var position:Int=0
    //当前播放的歌单
    var songs:MutableList<ServiceSong>?=null

    //Music播放的连接
    private var connection=presenter as ServiceConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_ui)


        try {
            //初始化值
            intent.extras?.apply {
                position=getInt("position")
                song= getSerializable("song") as ServiceSong
                songs=(getSerializable("songs") as ServiceSongList).mSongs
            }
        }catch (e:Exception){
            Log.e(TAG, "空指针异常", e)
        }
        if(songs!=null&&position!=null){
            presenter.addData(songs!!,position)
        }else{
            songs=getSongList()
            position=getPosition()
            try {
                presenter.addData(songs!!, position)
            }catch (e:Exception){
                sendToast("快去寻找音乐吧")
            }
        }
        //设置点击事件的处理

        //返回键
        song_ui_back_icon.setOnClickListener(presenter)

        //  播放/暂停键
        play_pause_icon.setOnClickListener(presenter)

        //下一首
        the_next_song_icon.setOnClickListener(presenter)

        //上一首
        the_last_song_icon.setOnClickListener(presenter)

        //SeekBar的监控
        song_ui_seek_bar.setOnSeekBarChangeListener(presenter)

    }

    //图标由pause状态改为play
    override fun iconChangeToPlay() {
        play_pause_icon.setImageResource(R.drawable.play)
    }

    //图标由play改为pause
    override fun iconChangeToPause() {
        play_pause_icon.setImageResource(R.drawable.pause)
    }
    //暂时放弃
    override fun setBufferedProgress(percent: Int) {

    }

    override fun back() {
        finish()
    }

    //发送toast
    override fun sendToast(text: String) {
        MyToast().sendToast(this,text,Toast.LENGTH_SHORT)
    }

    override fun setBufferedBarPercent(percent: Int) {
    }


    override fun setMusicMaxProgress(duration: Int) {
        //设置最大值
        //SeekBar
        song_ui_seek_bar.max=duration
        //ProgressBar
        progress_bar.max=duration
        //歌曲的最大值
        total_song_progress.text= changMsIntoMinutesAndSecond(duration)
    }
    //改变SeekBar对应的位置
    override fun serviceRefresh(songName: String, singer: String, imageurl: String, duration: Int, currentTime: Int, songId: String) {
        song_ui_seek_bar.progress=currentTime
        Glide.with(this).load(imageurl).into(song_ui_song_image_view)
        song_ui_song_name.text=songName
        song_ui_singer_name.text=singer
        song_ui_seek_bar.max=duration
        total_song_progress.text= changMsIntoMinutesAndSecond(duration)
        current_song_progress.text= changMsIntoMinutesAndSecond(currentTime)
        if(getIsPlaying()){
            play_pause_icon.setImageResource(R.drawable.play)
        }else{
            play_pause_icon.setImageResource(R.drawable.pause)
        }

    }

    //开始时候添加view并连接
    override fun onStart() {
        super.onStart()
        addView(this)
        //绑定
        val intent= Intent(this, MyMusicService::class.java)
        //绑定后自动创建
        bindService(intent,connection, Context.BIND_AUTO_CREATE)
    }

    //暂停的时候关闭连接
    override fun onPause() {
        super.onPause()
        reMoveView(this)
        unbindService(connection)
    }
}