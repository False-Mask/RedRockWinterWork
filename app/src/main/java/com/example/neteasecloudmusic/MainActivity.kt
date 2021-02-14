package com.example.neteasecloudmusic
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.neteasecloudmusic.favoriteslist.songui.SongUiActivity
import com.example.neteasecloudmusic.firstpagefragmentmvp.FirstFragment
import com.example.neteasecloudmusic.mainactivitymvp.MainActivityContract
import com.example.neteasecloudmusic.mainactivitymvp.MainActivityPresenter
import com.example.neteasecloudmusic.mytools.musicservice.*
import com.example.neteasecloudmusic.mytools.net.netJob
import com.example.neteasecloudmusic.mytools.toast.MyToast
import com.example.neteasecloudmusic.userfragmentmvp.UserFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.rv_song_first.*
import kotlinx.android.synthetic.main.second_fragment_layout.*
import kotlinx.android.synthetic.main.activity_main.user_text as user_text1

//NetWorkChangeImp是网络变化的监听
var context:MainActivity?=null
class MainActivity : AppCompatActivity() , MainActivityContract.MainActivityIView
        ,NetWorkChangeImp.NetCallback, IServiceBindView {
    //网络连接管理器
    private lateinit var connectivityManager:ConnectivityManager
    //连接变化的实现类
    private var myListener=NetWorkChangeImp

    private lateinit var connection:ServiceConnection

    val TAG="MainActivity"
    //初始化presenter
    //var presenter=MainActivityPresenter(this)
    //由于MainActivity里面有通用的刷新视图的方法 presenter可能会被其他activity喊去帮忙干活
    //Fragment定义并初始化
    companion object MyFragment{
        lateinit var secondFragment:UserFragment
        lateinit var firstFragment:FirstFragment
        lateinit var presenter:MainActivityPresenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //初始化connection和presenter
        presenter=MainActivityPresenter(this)
        connection= presenter
        //程序开始 初始化View视图
        initFragment()
        //注册网络连接变化的监听
        //初始化点击事件的处理
        initClick()
        registerNetListener()
        //添加监听器
        NetWorkChangeImp.addListener(this)
        context=this
    }

    private fun initClick() {
        bottom_play_pause_main.setOnClickListener(presenter)
        bottom_song_image_main.setOnClickListener(presenter)
        bottom_song_name_main.setOnClickListener(presenter)
        bottom_next_song.setOnClickListener(presenter)
        bottom_last_song.setOnClickListener(presenter)
    }

    //activity销毁的时候取消job以免发生内存泄漏
    //同时撤回网络状态的监听(为系统减负)
    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(myListener)
        netJob.cancel()
    }
    //网络监听的初始化
    private fun registerNetListener() {
        //val intendFilter=IntentFilter()
        connectivityManager=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request=NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request,myListener)
    }

    fun changeUserIcon(view: View) {
        //初始化颜色
        user_text1.setTextColor(Color.BLACK)
        music_text.setTextColor(Color.BLACK)
        bottom_user.setImageResource(R.drawable.user_icon_1)
        bottom_music.setImageResource(R.drawable.music_icon_1)


        val manager=supportFragmentManager
        val transaction=manager.beginTransaction()
        //变色
        when(view.id){
            //用户点击  “用户”  选项
            R.id.user_text,R.id.bottom_user-> {
                //图标和Fragment响应
                user_text1.setTextColor(resources.getColor(R.color.bottom_text_color))
                bottom_user.setImageResource(R.drawable.user_icon_2)
                hideAll(transaction)
                //替换FrameLayout内容
                transaction.show(secondFragment)
                //视图初始化(这里有网络请求)
                secondFragment.initView()
                //关闭banner
                firstFragment.presenter.isBannering=false
            }
            //用户点击 “首页”  选项
            R.id.bottom_music,R.id.music_text->{
                bottom_music.setImageResource(R.drawable.music_icon_2)
                music_text.setTextColor(resources.getColor(R.color.bottom_text_color))
                hideAll(transaction)
                transaction.show(firstFragment)
                //这里也得初始化视图
                firstFragment.initView()
            }
        }
        //提交
        transaction.commit()
    }
    //初始化Fragment
    override fun initFragment() {
        //初始化 Fragment传入activity实例方便以后干点啥
        firstFragment=FirstFragment(this)
        secondFragment=UserFragment(this)

        val manager=supportFragmentManager
        val transaction=manager.beginTransaction()
        //添加所有的Fragment
        transaction.add(R.id.frameLayout,firstFragment)
        transaction.add(R.id.frameLayout,secondFragment)
        hideAll(transaction)
        transaction.show(firstFragment)
        //底部导航栏初始化
        bottom_music.setImageResource(R.drawable.music_icon_2)
        music_text.setTextColor(resources.getColor(R.color.bottom_text_color))

        //提交申请
        transaction.commit()
        //window
    }
//跳转至 songUiActivity
    override fun loopToSongUi() {
        val intent=Intent(this,SongUiActivity::class.java)
        startActivity(intent)
    }


    //隐藏所有的Fragment
    //由于Fragment在replace以后实例不变 但是界面会刷新 这样体验不太好
    //暂时是我能想到的最好的解决方案了
    private fun hideAll(transaction:FragmentTransaction) {
        transaction.hide(firstFragment)
        transaction.hide(secondFragment)
    }


    //绑定布局
    override fun onStart() {
        super.onStart()
        val intent=Intent(this,MyMusicService::class.java)
        bindService(intent,connection, Context.BIND_AUTO_CREATE)
        addView(this)
    }

    override fun onPause() {
        super.onPause()
        //移除免得调用
        presenter.musicService.reMoveView()
        unbindService(connection)
        Log.d(TAG, "断开连接")
        reMoveView(this)
    }

    //service的接口
    override fun serviceRefresh(songName: String, singer: String, imageUrl: String, duration: Int, currentTime: Int, songId: String) {
        if (getIsPlaying()){
            bottom_play_pause_main.setImageResource(R.drawable.play)
        }else{
            bottom_play_pause_main.setImageResource(R.drawable.pause)
        }
        Glide.with(this).load(imageUrl).into(bottom_song_image_main)
        bottom_song_name_main.text=songName
    }

    override fun setMusicMaxProgress(duration: Int) {

    }

    override fun sendToast(s: String) {
        MyToast().sendToast(this,s,Toast.LENGTH_SHORT)
    }

    override fun iconChangeToPause() {
    }

    override fun setBufferedProgress(percent: Int) {

    }


    //当有网络 两个是异步进行的
    override fun onAvailable() {
        //下载了歌单的封面和rv的更新 自动登陆
        presenter.loginAuto()
        presenter.getBanner()
    }
    //无网络发一个Toast(暂时还不知道干什么)
    override fun onUnavailable() {
        MyToast().sendToast(this,"网络出了亿点小问题~",Toast.LENGTH_SHORT)
        presenter.onUnavailable()
    }
}