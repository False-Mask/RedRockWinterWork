package com.example.neteasecloudmusic.loginactivity.loginbyphone

import java.io.File
import java.io.Serializable

class LoginCathe :Serializable{
    var headShowImage:File?=null
    var nickname:String=""
    var phoneNumber:String=""
    var password:String=""
    var backgroundImage:File?=null
    var cookie:String=""

    constructor(headShowImage: File?, nickname: String,phoneNumber:String, password: String, backgroundImage: File?, cookie: String) {
        this.headShowImage = headShowImage
        this.nickname = nickname
        this.password = password
        this.backgroundImage = backgroundImage
        this.cookie = cookie
        this.phoneNumber=phoneNumber
    }

    companion object{
        const val serialVersionUID = -666666666666L
    }

}