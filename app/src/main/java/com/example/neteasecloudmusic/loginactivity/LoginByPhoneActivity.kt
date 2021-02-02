package com.example.neteasecloudmusic.loginactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login_by_phone.*

class LoginByPhoneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_by_phone)
        //返回
        back_image_phone.setOnClickListener{
            finish()
        }

        //注册账号
        register_user_account.setOnClickListener{
            var intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        //处理用户登陆
        login_button.setOnClickListener{
            Toast.makeText(this,"点击了登陆",Toast.LENGTH_SHORT).show()
        }

        //忘记密码
        forget_password.setOnClickListener{

        }
    }
}