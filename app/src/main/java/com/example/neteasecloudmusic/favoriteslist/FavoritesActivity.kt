package com.example.neteasecloudmusic.favoriteslist

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.transition.Explode
import android.transition.Fade
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.favoriteslist.songs.SongRvAdapter
import com.example.neteasecloudmusic.mytools.animation.createRotate
import com.example.neteasecloudmusic.mytools.animation.pauseRotate
import com.example.neteasecloudmusic.mytools.animation.removeRotate
import com.example.neteasecloudmusic.mytools.animation.startRotate
import com.example.neteasecloudmusic.mytools.musicservice.*
import com.example.neteasecloudmusic.mytools.toast.MyToast
import com.example.neteasecloudmusic.view.PlayPauseBar
import kotlinx.android.synthetic.main.activity_favorites.*
import java.io.FileDescriptor
import java.lang.Exception

class FavoritesActivity : AppCompatActivity(),FavoritesContract.FavoritesIView
,IServiceBindView{
    var presenter=FavoritesPresenter(this)

    var playListId:String=""

    val TAG="FavoritesActivity"

    lateinit var musicService:MyMusicService

    var animator:ObjectAnimator?=null

    //定义连接presenter实现
    private var connection = presenter as ServiceConnection

    companion object{
        var useLocalCathe:Boolean=false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //设置window的内容
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor=resources.getColor(R.color.status_bar_color)

        window.enterTransition=Explode()
        window.exitTransition=Explode()

        setContentView(R.layout.activity_favorites)

        createRotate(bottom_song_image)
        if (getIsPlaying()){
            startRotate()
        }

        //初始化
        //发送网络请求啥的啊(获取点击的position)
        val position=intent.extras?.getInt("position")

//
//        val isSendUserId=intent.extras?.getBoolean("is_send_user_id")?:false
//
//
//        //如果需要发送userId获取歌单的id
//        if (isSendUserId){
//
//        }
        //获取所点击的歌单的id
        playListId= intent.extras?.getString("playListId").toString()
        //判断是否是使用本地存储
        useLocalCathe= intent.extras?.getBoolean("useLocalCathe")!!
        //初始化视图
        initView(intent,position)
    }

    //初始化view视图
    private fun initView(intent: Intent,position: Int?) {
        //设置recyclerview的基本属性
        val songRvAdapter=SongRvAdapter()
        //设置一下属性
        if (useLocalCathe){
            songRvAdapter.setLocalList(mutableListOf())
        }else{
            songRvAdapter.setNetList(mutableListOf())
        }
        //设置一下adapter
        song_list_rv.adapter=songRvAdapter
        song_list_rv.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        //提示一下adapter改变视图
        songRvAdapter.notifyDataSetChanged()

        //是否要发一个请求重新获取歌曲
        if (useLocalCathe){
            presenter.getSongs(position,songRvAdapter)
        }else{
            presenter.getPlayList(playListId,songRvAdapter,intent)
        }
        //设置recyclerview字item点击监听 presenter实现
        songRvAdapter.setOnItemClickListener(presenter)
        //底部视图的点击监听
        bottom_pause_or_play.addOnViewClickListener(presenter)
        bottom_song_name.setOnClickListener(presenter)
        bottom_song_image.setOnClickListener(presenter)
    }
    //进度条开启
    override fun progressBarOn() {
        song_loading.visibility= View.VISIBLE
    }

    //progressbar关闭
    override fun progressBarOff() {
        song_loading.visibility=View.GONE
    }

    override fun setTextColor(textView: TextView?, color: Int) {
        textView?.setTextColor(resources.getColor(color))
    }

    override fun loopToSongUi(intent: Intent) {
        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    override fun resume(fl: Float) {
        bottom_pause_or_play.status=PlayPauseBar.PlayStatus.Playing
        bottom_pause_or_play.invalidate()
        bottom_pause_or_play.progressPercent=fl
        start()
    }

    override fun pause() {
        animator?.cancel()
        bottom_pause_or_play.status=PlayPauseBar.PlayStatus.Pausing
    }

    override fun preparing() {
        bottom_pause_or_play.status=PlayPauseBar.PlayStatus.Loading
        animator=ObjectAnimator.ofFloat(bottom_pause_or_play,"angle",0f,360f)
        animator?.apply {
            duration=1000
            repeatCount=-1
            interpolator=LinearInterpolator()
            start()
        }
    }

    override fun start() {
        bottom_pause_or_play.status=PlayPauseBar.PlayStatus.Playing
    }


    //当view刚刚开启 绑定一下service
    override fun onStart() {
        super.onStart()
        addView(this)
        addPresenter(presenter)
        val intent=Intent(this,MyMusicService::class.java)
        Log.e(TAG, "开始连接" )
        bindService(intent,connection,Context.BIND_AUTO_CREATE)

        //创建绑定初始化
        createRotate(bottom_song_image)
        if (getIsPlaying()){
            startRotate()
        }
    }

    //视图不可见接触绑定 同时其他的view绑定了当前的service
    override fun onPause() {
        super.onPause()
        reMovePre(presenter)
        reMoveView(this)
        Log.e(TAG, "开始断开连接" )
        try {
            unbindService(connection)
        }catch (e:Exception){
            Log.e(TAG, "断开连接故障",e )
        }

        //setViewStatus(false)
        reMoveView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        //移除动画
        removeRotate(bottom_song_image)
    }

    //service的同步view的播放进度的方法
    override fun setMusicMaxProgress(duration: Int) {}

    override fun setBufferedProgress(percent: Int) {}

    override fun serviceRefresh(songName: String, singer: String, imageUrl: String, duration: Int, currentTime: Int, songId: String) {
            if (getIsPlaying()){
                resume(currentTime.toFloat()/duration)
            }else{
                pause()
            }
            bottom_song_name.text=songName
            Glide.with(this).load(imageUrl).placeholder(R.drawable.music_place_holder)
                    .error(R.drawable.music_place_holder).circleCrop().into(bottom_song_image)
    }

    override fun sendToast(s: String) {
        MyToast().sendToast(this,s,Toast.LENGTH_SHORT)
    }

    override fun iconChangeToPause(){
        bottom_pause_or_play.status=PlayPauseBar.PlayStatus.Pausing
        bottom_pause_or_play.invalidate()
    }
}