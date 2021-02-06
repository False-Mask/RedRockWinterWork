package com.example.neteasecloudmusic.loginactivity.loginbyphone

import android.util.Log
import com.example.neteasecloudmusic.mytools.net.SendNetRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ByPhoneModel : ByPhoneContract.ByPhoneIModel {

    private val TAG = "ByPhoneModel"

    //数据对象
    var loginResult = LoginResult()

    //主机名 除去了末尾的 "/"
    private val baseUrl = "http://sandyz.ink:3000"

    //网络请求工具类
    private var sendNetRequest = SendNetRequest()

    //登陆
    override fun login(phoneNumber: String, passwordText: String): LoginResult {
        //http请求貌似不太安全(配了啥我也不懂)
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3")
        var string: String = "/login/cellphone"

        sendNetRequest.sendGetRequest("$baseUrl$string?phone=$phoneNumber&password=$passwordText"
                , object : SendNetRequest.Back {
            override fun onResponded(resultBody: String) {
                Log.d(TAG, "login onResponded: ")
                var gson = Gson()
                var type=object : TypeToken<LoginResult>(){}.type
                loginResult = gson.fromJson(resultBody, type)
                Log.d(TAG, "onResponded: "+loginResult.toString())
            }

            override fun onFailed(respondCode: Int) {
                Log.d(TAG, "login onFailed: ")
            }

        })
         return loginResult
    }
    class LoginResult {
        var account: Account?=null
        var bindings: List<Binding>? =null
        var code: Int = 0
        var cookie: String = ""
        var loginType: Int = 0
        var profile:Profile? = null
        var token: String = ""
        override fun toString(): String {
            return "LoginResult(account=$account, bindings=${bindings?.toString()}, code=$code, cookie='$cookie', loginType=$loginType, profile=$profile, token='$token')"
        }

    }

    class Account {
        var anonimousUser: Boolean = false
        var ban: Int = 0
        var baoyueVersion: Int = 0
        var createTime: Long = 0
        var donateVersion: Int = 0
        var id: Long = 0
        var salt: String = ""
        var status: Int = 0
        var tokenVersion: Int = 0
        var type: Int = 0
        var userName: String = ""
        var vipType: Int = 0
        var viptypeVersion: Int = 0
        var whitelistAuthority: Int = 0
        override fun toString(): String {
            return "Account(anonimousUser=$anonimousUser, ban=$ban, baoyueVersion=$baoyueVersion, createTime=$createTime, donateVersion=$donateVersion, id=$id, salt='$salt', status=$status, tokenVersion=$tokenVersion, type=$type, userName='$userName', vipType=$vipType, viptypeVersion=$viptypeVersion, whitelistAuthority=$whitelistAuthority)"
        }
    }

        class Binding {

            var bindingTime: Long = 0
            var expired: Boolean = false
            var expiresIn: Int = 0
            var id: Long = 0
            var refreshTime: Int = 0
            var tokenJsonStr: String = ""
            var type: Int = 0
            var url: String = ""
            var userId: Long = 0
            override fun toString(): String {
                return "Binding(bindingTime=$bindingTime, expired=$expired, expiresIn=$expiresIn, id=$id, refreshTime=$refreshTime, tokenJsonStr='$tokenJsonStr', type=$type, url='$url', userId=$userId)"
            }

        }

    class Profile{
        var accountStatus: Int = 0
        var authStatus: Int = 0
        var authority: Int = 0
        var avatarDetail: Any ?= null
        var avatarImgId: Long = 0
        var avatarImgIdStr: String = ""
        var avatarImgId_str: String = ""
        var avatarUrl: String = ""
        var backgroundImgId: Long = 0
        var backgroundImgIdStr: String = ""
        var backgroundUrl: String = ""
        //
        var birthday: Long = 0
        var city: Int = 0
        var defaultAvatar: Boolean = false
        var description: String = ""
        var detailDescription: String = ""
        var djStatus: Int = 0
        var eventCount: Int = 0
        var expertTags: Any? = null
        var experts: Any? = null
        var followed: Boolean = false
        var followeds: Int = 0
        var follows: Int = 0
        var gender: Int = 0
        var mutual: Boolean = false
        var nickname: String = ""
        var playlistBeSubscribedCount: Int = 0
        var playlistCount: Int = 0
        var province: Int = 0
        var remarkName: Any? = null
        var signature: String = ""
        var userId: Long = 0
        var userType: Int = 0
        var vipType: Int = 0
        override fun toString(): String {
            return "Profile(accountStatus=$accountStatus, authStatus=$authStatus, authority=$authority, avatarDetail=$avatarDetail, avatarImgId=$avatarImgId, avatarImgIdStr='$avatarImgIdStr', avatarImgId_str='$avatarImgId_str', avatarUrl='$avatarUrl', backgroundImgId=$backgroundImgId, backgroundImgIdStr='$backgroundImgIdStr', backgroundUrl='$backgroundUrl', birthday=$birthday, city=$city, defaultAvatar=$defaultAvatar, description='$description', detailDescription='$detailDescription', djStatus=$djStatus, eventCount=$eventCount, expertTags=$expertTags, experts=$experts, followed=$followed, followeds=$followeds, follows=$follows, gender=$gender, mutual=$mutual, nickname='$nickname', playlistBeSubscribedCount=$playlistBeSubscribedCount, playlistCount=$playlistCount, province=$province, remarkName=$remarkName, signature='$signature', userId=$userId, userType=$userType, vipType=$vipType)"
        }
    }
}