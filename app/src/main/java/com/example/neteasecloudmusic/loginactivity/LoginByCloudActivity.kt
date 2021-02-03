package com.example.neteasecloudmusic.loginactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.loginactivity.loginbyphone.ActivityController

class LoginByCloudActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_by_cloud)
        ActivityController.Static.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityController.Static.removeActivity(this)
    }
}