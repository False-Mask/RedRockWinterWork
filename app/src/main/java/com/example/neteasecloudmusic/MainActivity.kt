package com.example.neteasecloudmusic
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.neteasecloudmusic.firstpagefragmentmvp.FirstFragment
import com.example.neteasecloudmusic.mainactivitymvp.BannerDataObjectName
import com.example.neteasecloudmusic.mainactivitymvp.MainActivityContract
import com.example.neteasecloudmusic.mainactivitymvp.MainActivityPresenter
import com.example.neteasecloudmusic.mytools.filedownload.readObjectFile
import com.example.neteasecloudmusic.mytools.net.netJob
import com.example.neteasecloudmusic.mytools.toast.MyToast
import com.example.neteasecloudmusic.myview.BannerData
import com.example.neteasecloudmusic.myview.viewList
import com.example.neteasecloudmusic.userfragmentmvp.UserFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pagerview.view.*
import java.io.File
import kotlinx.android.synthetic.main.activity_main.user_text as user_text1

//NetWorkChangeImp是网络变化的监听
var context:MainActivity?=null
class MainActivity : AppCompatActivity() , MainActivityContract.MainActivityIView
        ,NetWorkChangeImp.NetCallback {

    //网络连接管理器
    private lateinit var connectivityManager:ConnectivityManager
    //连接变化的实现类
    private var myListener=NetWorkChangeImp

    val TAG="MainActivity"
    //初始化presenter
    var presenter=MainActivityPresenter(this)
    //Fragment定义并初始化
    companion object MyFragment{
        lateinit var secondFragment:UserFragment
        lateinit var firstFragment:FirstFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //程序开始 初始化View视图
        initFragment()
        //注册网络连接变化的监听
        registerNetListener()
        //添加监听器
        NetWorkChangeImp.addListener(this)
        context=this
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
        var request=NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request,myListener)
    }

    fun changeUserIcon(view: View) {
        //初始化颜色
        user_text1.setTextColor(Color.BLACK)
        music_text.setTextColor(Color.BLACK)
        bottom_user.setImageResource(R.drawable.user_icon_1)
        bottom_music.setImageResource(R.drawable.music_icon_1)

        var manager=supportFragmentManager
        var transaction=manager.beginTransaction()
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

        var manager=supportFragmentManager
        var transaction=manager.beginTransaction()
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


    //隐藏所有的Fragment
    //由于Fragment在replace以后实例不变 但是界面会刷新 这样体验不太好
    //暂时是我能想到的最好的解决方案了
    private fun hideAll(transaction:FragmentTransaction) {
        transaction.hide(firstFragment)
        transaction.hide(secondFragment)
    }


    //当有网络的时候自动登陆
    override fun onAvailable() {
        presenter.loginAuto()
        presenter.getBanner()
    }
    //无网络发一个Toast(暂时还不知道干什么)
    override fun onUnavailable() {
        MyToast().sendToast(this,"网络出了亿点小问题~",Toast.LENGTH_SHORT)
        presenter.onUnavailable()
    }

}