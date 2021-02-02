package com.example.neteasecloudmusic.loginactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.neteasecloudmusic.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
            var intent=Intent(this,LoginByPhoneActivity::class.java)
            startActivity(intent)
        }
    }
}