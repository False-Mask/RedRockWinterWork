package com.example.neteasecloudmusic
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.neteasecloudmusic.favoriteslist.songui.SongUiActivity
import com.example.neteasecloudmusic.firstpagefragmentmvp.FirstFragment
import com.example.neteasecloudmusic.mainactivitymvp.MainActivityContract
import com.example.neteasecloudmusic.mainactivitymvp.MainActivityPresenter
import com.example.neteasecloudmusic.mytools.animation.createRotate
import com.example.neteasecloudmusic.mytools.animation.pauseRotate
import com.example.neteasecloudmusic.mytools.animation.removeRotate
import com.example.neteasecloudmusic.mytools.animation.startRotate
import com.example.neteasecloudmusic.mytools.musicservice.*
import com.example.neteasecloudmusic.mytools.net.netJob
import com.example.neteasecloudmusic.mytools.toast.MyToast
import com.example.neteasecloudmusic.userfragmentmvp.UserFragment
import com.example.neteasecloudmusic.view.PlayPauseBar
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
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

    var animator:ObjectAnimator?=null


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
        //改变顶部状态栏的颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor=resources.getColor(R.color.status_bar_color)


        setContentView(R.layout.activity_main)
        //初始化connection和presenter
        presenter=MainActivityPresenter(this)

//        presenter.loading(image_view)


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

        createRotate(bottom_song_image_main)
    }

    private fun initClick() {
        bottom_play_pause_main.addOnViewClickListener(presenter)

        bottom_song_image_main.setOnClickListener(presenter)
        bottom_song_name_main.setOnClickListener(presenter)

        bottom_next_song.addOnIconClickListener(presenter)
        bottom_last_song.addOnIconClickListener(presenter)

    }

    //activity销毁的时候取消job以免发生内存泄漏
    //同时撤回网络状态的监听(为系统减负)
    override fun onDestroy() {
        super.onDestroy()
        //移除旋转
        removeRotate(bottom_song_image_main)

        connectivityManager.unregisterNetworkCallback(myListener)
        netJob.cancel()

        //移除免得调用
        reMoveView(this)
        reMovePre(presenter)
        try{
            unbindService(connection)
        }catch (e:Exception){
            Log.e(TAG, "服务器解绑故障", e)
        }

        Log.d(TAG, "断开连接")
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
        bottom_user.setImageResource(R.drawable.user_icon_2)
        bottom_music.setImageResource(R.drawable.music_icon_2)


        val manager=supportFragmentManager
        val transaction=manager.beginTransaction()
        //变色
        when(view.id){
            //用户点击  “用户”  选项
            R.id.user_text,R.id.bottom_user-> {
                //图标和Fragment响应
                user_text1.setTextColor(resources.getColor(R.color.bottom_text_color))
                bottom_user.setImageResource(R.drawable.user_icon_1)
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
                bottom_music.setImageResource(R.drawable.music_icon_1)
                music_text.setTextColor(resources.getColor(R.color.bottom_text_color))
                hideAll(transaction)
                transaction.show(firstFragment)
                //这里也得初始化视图
               // firstFragment.initView()
            }
        }
        //提交
        transaction.commit()
        //初始化转动
        if (getIsPlaying()) startRotate()
        else pauseRotate()
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
        bottom_music.setImageResource(R.drawable.music_icon_1)
        music_text.setTextColor(resources.getColor(R.color.bottom_text_color))

        //提交申请
        transaction.commit()
        //window
    }
    //跳转至 songUiActivity
    override fun loopToSongUi() {
        val intent=Intent(this,SongUiActivity::class.java)
        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    override fun resume(fl: Float) {
        bottom_play_pause_main.status=PlayPauseBar.PlayStatus.Playing
        bottom_play_pause_main.progressPercent=fl
    }

    override fun pause() {
        animator?.cancel()
        bottom_play_pause_main.status=PlayPauseBar.PlayStatus.Pausing
    }

    override fun preparing() {
        bottom_play_pause_main.status=PlayPauseBar.PlayStatus.Loading
        animator?.apply {
            duration=1000
            repeatCount=-1
            interpolator=LinearInterpolator()
            start()
        }
    }

    override fun start() {
        bottom_play_pause_main.status=PlayPauseBar.PlayStatus.Playing
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
        try {
            bindService(intent,connection, Context.BIND_AUTO_CREATE)
        }catch (e:Exception){
            Log.e(TAG, "绑定服务故障",e )
        }

        createRotate(bottom_song_image_main)
        if (getIsPlaying()) startRotate()
        else pauseRotate()

        addView(this)
        addPresenter(presenter)
    }

//    override fun onPause() {
//        super.onPause()
//        //移除免得调用
//        reMoveView(this)
//        reMovePre(presenter)
//        try{
//            unbindService(connection)
//        }catch (e:Exception){
//            Log.e(TAG, "服务器解绑故障", e)
//        }
//
//        Log.d(TAG, "断开连接")
//    }

    var circleCrop=RequestOptions.bitmapTransform(CircleCrop())
    //service的接口
    override fun serviceRefresh(songName: String, singer: String, imageUrl: String, duration: Int, currentTime: Int, songId: String) {
        if (getIsPlaying()){
            resume(currentTime.toFloat()/duration)
        }else{
            pause()
        }
        Glide.with(this).load(imageUrl).circleCrop()
                .error(R.drawable.music_place_holder)
                .placeholder(R.drawable.music_place_holder).into(bottom_song_image_main)
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
        //presenter.getBanner()
    }
    //无网络发一个Toast(暂时还不知道干什么)
    override fun onUnavailable() {
        MyToast().sendToast(this,"网络出了亿点小问题~",Toast.LENGTH_SHORT)
        presenter.onUnavailable()
    }


}