package com.example.neteasecloudmusic.useui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.neteasecloudmusic.R
import com.example.neteasecloudmusic.userfragmentmvp.UserPresenter

class UserUiActivity : AppCompatActivity(),UserUiContract.UserUiIView {
    var presenter=UserUiPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_ui)

        presenter.initUserDetails()

    }
}