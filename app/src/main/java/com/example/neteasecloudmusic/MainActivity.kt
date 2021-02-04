package com.example.neteasecloudmusic
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.neteasecloudmusic.firstpagefragmentmvp.FirstFragment
import com.example.neteasecloudmusic.mainactivitymvp.MainActivityContract
import com.example.neteasecloudmusic.mainactivitymvp.MainActivityPresenter
import com.example.neteasecloudmusic.userfragmentmvp.UserFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , MainActivityContract.MainActivityIView {

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

        registerNetListener()

    }

    private fun registerNetListener() {
        //val intendFilter=IntentFilter()
        var connectivityManager=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var myListener=NetWorkCallback()
        var request=NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request,myListener)

    }

    fun changeUserIcon(view: View) {
        user_text.setTextColor(Color.BLACK)
        music_text.setTextColor(Color.BLACK)
        bottom_user.setImageResource(R.drawable.user_icon_1)
        bottom_music.setImageResource(R.drawable.music_icon_1)


        var manager=supportFragmentManager
        var transaction=manager.beginTransaction()
        //变色
        when(view.id){

            //用户点击  “用户”  选项
            R.id.user_text,R.id.bottom_user-> {
                user_text.setTextColor(resources.getColor(R.color.bottom_text_color))
                bottom_user.setImageResource(R.drawable.user_icon_2)
                hideAll(transaction)
                //替换FrameLayout内容
                transaction.show(secondFragment)
                secondFragment.initView()
                secondFragment.login()
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

}