package com.example.neteasecloudmusic.favoriteslist

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.favoriteslist.songs.SongRvAdapter
import com.example.neteasecloudmusic.mytools.musicservice.*
import com.example.neteasecloudmusic.mytools.toast.MyToast
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : AppCompatActivity(),FavoritesContract.FavoritesIView
,IServiceBindView{
    var presenter=FavoritesPresenter(this)

    var playListId:String=""

    val TAG="FavoritesActivity"

    lateinit var musicService:MyMusicService

    //定义连接presenter实现
    private var connection = presenter as ServiceConnection

    companion object{
        var useLocalCathe:Boolean=false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
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
        bottom_pause_or_play.setOnClickListener(presenter)
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
        startActivity(intent)
    }

    //当view刚刚开启 绑定一下service
    override fun onStart() {
        super.onStart()
        addView(this)
        val intent=Intent(this,MyMusicService::class.java)
        Log.e(TAG, "开始连接" )
        bindService(intent,connection,Context.BIND_AUTO_CREATE)
    }

    //视图不可见接触绑定 同时其他的view绑定了当前的service
    override fun onPause() {
        super.onPause()
        presenter.musicService.reMoveView()
        Log.e(TAG, "开始断开连接" )
        unbindService(connection)
        //setViewStatus(false)
        reMoveView(this)
    }


    //service的同步view的播放进度的方法
    override fun setMusicMaxProgress(duration: Int) {}

    override fun setBufferedProgress(percent: Int) {}

    override fun serviceRefresh(songName: String, singer: String, imageUrl: String, duration: Int, currentTime: Int, songId: String) {
        MyToast().sendToast(this,singer+"FavoritesActivity",Toast.LENGTH_SHORT)
            if (getIsPlaying()){
                bottom_pause_or_play.setImageResource(R.drawable.play)
            }else{
                bottom_pause_or_play.setImageResource(R.drawable.pause)
            }
            bottom_song_name.text=songName
            Glide.with(this).load(imageUrl).into(bottom_song_image)
    }

    override fun sendToast(s: String) {
        MyToast().sendToast(this,s,Toast.LENGTH_SHORT)
    }

    override fun iconChangeToPause(){}
}