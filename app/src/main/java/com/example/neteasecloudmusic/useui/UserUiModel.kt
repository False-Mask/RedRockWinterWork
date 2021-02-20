package com.example.neteasecloudmusic.useui

import android.util.Log
import com.example.neteasecloudmusic.mytools.net.SendNetRequest
import com.google.gson.Gson
import java.lang.Exception

class UserUiModel :UserUiContract.UserUiIModel{
    val TAG="UserUiModel"
    val baseUrl="http://sandyz.ink:3000"
    val sendNetRequest=SendNetRequest()
    //变量
    var detailResult=DetailResult()

    /**
     * 登陆后获取用户的详细信息
     */
    override fun initUserDetails() {
        var string="/user/account"
        sendNetRequest.sendGetRequest("$baseUrl$string"
                ,object : SendNetRequest.Back{
            override fun onResponded(resultBody: String) {
                Log.d(TAG, "UserUiModel onResponded: ")
                val gson = Gson()
                try {
                    detailResult = gson.fromJson(resultBody,DetailResult::class.java)
                }catch (e:Exception){}


            }

            override fun onFailed(respondCode: Int) {
                Log.d(TAG, "UserUiModel onFailed: ")
            }

        })
    }


    /**
     * 该类存储用户的基本信息
     */
    class DetailResult{
        var account: Account = Account()
        var code: Int = 0
        var profile: Profile = Profile()
    }
    class Account{
        var anonimousUser: Boolean = false
        var ban: Int = 0
        var baoyueVersion: Int = 0
        var createTime: Long = 0
        var donateVersion: Int = 0
        var id: Long = 0
        var paidFee: Boolean = false
        var status: Int = 0
        var tokenVersion: Int = 0
        var type: Int = 0
        var userName: String = ""
        var vipType: Int = 0
        var whitelistAuthority: Int = 0
    }

    class Profile{
        var accountStatus: Int = 0
        var accountType: Int = 0
        var anchor: Boolean = false
        var authStatus: Int = 0
        var authenticated: Boolean = false
        var authenticationTypes: Int = 0
        var authority: Int = 0
        var avatarDetail: Any? = Any()
        var avatarImgId: Long = 0
        var avatarUrl: String = ""
        var backgroundImgId: Long = 0
        var backgroundUrl: String = ""
        var birthday: Long = 0
        var city: Int = 0
        var createTime: Long = 0
        var defaultAvatar: Boolean = false
        var description: Any? = Any()
        var detailDescription: Any? = Any()
        var djStatus: Int = 0
        var expertTags: Any? = Any()
        var experts: Any? = Any()
        var followed: Boolean = false
        var gender: Int = 0
        var lastLoginIP: String = ""
        var lastLoginTime: Long = 0
        var locationStatus: Int = 0
        var mutual: Boolean = false
        var nickname: String = ""
        var province: Int = 0
        var remarkName: Any? = Any()
        var shortUserName: String = ""
        var signature: String = ""
        var userId: Int = 0
        var userName: String = ""
        var userType: Int = 0
        var vipType: Int = 0
        var viptypeVersion: Int = 0
    }

}