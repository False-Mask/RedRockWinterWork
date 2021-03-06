package com.example.neteasecloudmusic.favoriteslist.songui

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.mytools.animation.createRotate
import com.example.neteasecloudmusic.mytools.animation.pauseRotate
import com.example.neteasecloudmusic.mytools.animation.removeRotate
import com.example.neteasecloudmusic.mytools.animation.startRotate
import com.example.neteasecloudmusic.mytools.musicservice.*
import com.example.neteasecloudmusic.mytools.time.changMsIntoMinutesAndSecond
import com.example.neteasecloudmusic.mytools.toast.MyToast
import com.example.neteasecloudmusic.view.PlayPauseBar
import kotlinx.android.synthetic.main.activity_song_ui.*
import kotlinx.android.synthetic.main.rv_song_list_item.*

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

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor=resources.getColor(R.color.status_bar_color)

        super.onCreate(savedInstanceState)
        //进出场动画
        window.enterTransition=Slide()
        window.exitTransition=Slide()
        setContentView(R.layout.activity_song_ui)

        //添加动画
        createRotate(song_ui_song_image_view)
        //初始化转盘
        if (getIsPlaying()){
            startRotate()
        }

        try {
//            //初始化值
//            intent.extras?.apply {
//                position=getInt("position")
//                song= getSerializable("song") as ServiceSong
//                songs=(getSerializable("songs") as ServiceSongList).mSongs
//            }
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
        play_pause_icon.addOnViewClickListener(presenter)

        //下一首
        the_next_song_icon.addOnIconClickListener(presenter)

        //上一首
        the_last_song_icon.addOnIconClickListener(presenter)

        //SeekBar的监控
        song_ui_seek_bar.setOnSeekBarChangeListener(presenter)

    }

    //图标由pause状态改为play
    override fun iconChangeToPlay() {
        play_pause_icon.status=PlayPauseBar.PlayStatus.Playing
        play_pause_icon.invalidate()
        startRotate()
        //play_pause_icon.setImageResource(R.drawable.play)
    }

    //图标由play改为pause
    override fun iconChangeToPause() {
        play_pause_icon.status=PlayPauseBar.PlayStatus.Pausing
        play_pause_icon.invalidate()
        pauseRotate()
    //play_pause_icon.setImageResource(R.drawable.pause)
    }
    //暂时放弃
    override fun setBufferedProgress(percent: Int) {}

    override fun back() {
        finish()
    }

    override fun resume(percent: Float) {}

    override fun loading() {
        animator=ObjectAnimator.ofFloat(play_pause_icon,"angle",0f,360f)
        animator?.apply {
            duration=1000
            repeatCount=-1
            interpolator=LinearInterpolator()
            start()
        }
    }

    var animator: ObjectAnimator? =null
    override fun start() {
        play_pause_icon.status=PlayPauseBar.PlayStatus.Playing
        animator?.cancel()
    }

    //发送toast
    override fun sendToast(text: String) {
        MyToast().sendToast(this,text,Toast.LENGTH_SHORT)
    }

    override fun setBufferedBarPercent(percent: Int) {}


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
        //加载圆形图片
        Glide.with(this).load(imageurl).placeholder(R.drawable.music_place_holder)
                .apply(RequestOptions.bitmapTransform(CircleCrop())).into(song_ui_song_image_view)
        song_ui_song_name.text=songName
        song_ui_singer_name.text=singer
        song_ui_seek_bar.max=duration
        total_song_progress.text= changMsIntoMinutesAndSecond(duration)
        current_song_progress.text= changMsIntoMinutesAndSecond(currentTime)

        play_pause_icon.invalidate()

        if(getIsPlaying()){

            play_pause_icon.status=PlayPauseBar.PlayStatus.Playing
        }else{
            play_pause_icon.status=PlayPauseBar.PlayStatus.Pausing
        }

    }

    //开始时候添加view并连接
    override fun onStart() {
        super.onStart()
        addView(this)
        addPresenter(presenter)
        //绑定
        val intent= Intent(this, MyMusicService::class.java)
        //绑定后自动创建
        bindService(intent,connection, Context.BIND_AUTO_CREATE)
    }

    //暂停的时候关闭连接
    override fun onPause() {
        super.onPause()
        reMoveView(this)
        reMovePre(presenter)
        unbindService(connection)
        //移除动画
        removeRotate(song_ui_song_image_view)
    }
}