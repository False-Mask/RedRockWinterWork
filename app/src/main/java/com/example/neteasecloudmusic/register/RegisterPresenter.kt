package com.example.neteasecloudmusic.register

import android.widget.Toast
import com.example.neteasecloudmusic.mytools.net.SendNetRequest

class RegisterPresenter(registerActivity: RegisterActivity) :RegisterContract.RegisterIPresenter{

    var view=registerActivity
    var model=RegisterModel()

    override fun isRegisted(phoneNumber: String): Boolean {
            return false
    }

    override fun sendRegisterRequest(
        captcha: String,
        phone: String,
        password: String,
        nickname: String
    ) {
    }

    override fun sendCaptcha(phoneNumber: String) {
        model.sendCaptcha(phoneNumber)
    }

}