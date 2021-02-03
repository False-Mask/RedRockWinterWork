package com.example.neteasecloudmusic.useui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.neteasecloudmusic.R

class UserUiActivity : AppCompatActivity(),UserUiContract.UserUiIView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_ui)
    }
}