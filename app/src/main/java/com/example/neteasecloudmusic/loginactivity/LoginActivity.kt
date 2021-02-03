package com.example.neteasecloudmusic.loginactivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.loginactivity.loginbyphone.ActivityController
import com.example.neteasecloudmusic.loginactivity.loginbyphone.LoginByPhoneActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //将该activity加入管理器
        ActivityController.Static.addActivity(this)
        //返回
        back_image.setOnClickListener{
            finish()
        }
        //通过网易云邮箱登陆
        login_by_cloud.setOnClickListener{
            var intent=Intent(this, LoginByCloudActivity::class.java)
            startActivity(intent)
        }
        //通过手机号登陆
        login_by_phone.setOnClickListener{
            var intent=Intent(this, LoginByPhoneActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityController.Static.removeActivity(this)
    }
}