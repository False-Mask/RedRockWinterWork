package com.example.neteasecloudmusic
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.neteasecloudmusic.firstpagefragmentmvp.FirstFragment
import com.example.neteasecloudmusic.mainactivitymvp.MainActivityContract
import com.example.neteasecloudmusic.mainactivitymvp.MainActivityPresenter
import com.example.neteasecloudmusic.mytools.net.netJob
import com.example.neteasecloudmusic.mytools.toast.MyToast
import com.example.neteasecloudmusic.userfragmentmvp.UserFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.user_text as user_text1

class MainActivity : AppCompatActivity() , MainActivityContract.MainActivityIView
        ,NetWorkChangeImp.NetCallback {

    //网络连接管理器
    private lateinit var connectivityManager:ConnectivityManager
    //连接变化的实现类
    private var myListener=NetWorkChangeImp

    var TAG="MainActivity"

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
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(myListener)
        netJob.cancel()
    }

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
                user_text1.setTextColor(resources.getColor(R.color.bottom_text_color))
                bottom_user.setImageResource(R.drawable.user_icon_2)
                hideAll(transaction)
                //替换FrameLayout内容
                transaction.show(secondFragment)
                secondFragment.initView()
            }
            //用户点击 “首页”  选项
            R.id.bottom_music,R.id.music_text->{
                bottom_music.setImageResource(R.drawable.music_icon_2)
                music_text.setTextColor(resources.getColor(R.color.bottom_text_color))
                hideAll(transaction)
                transaction.show(firstFragment)
            }

        }
        //提交
        transaction.commit()
    }
    //初始化
    override fun initFragment() {
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
        window
    }


    //隐藏所有的Fragment
    private fun hideAll(transaction:FragmentTransaction) {
        transaction.hide(firstFragment)
        transaction.hide(secondFragment)
    }


    //当有网络的时候自动登陆
    override fun onAvailable() {
        presenter.loginAuto()
    }

    override fun onUnavailable() {
        MyToast().sendToast(this,"网络出了小问题~",Toast.LENGTH_SHORT)
    }

}