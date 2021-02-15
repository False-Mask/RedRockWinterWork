package com.example.neteasecloudmusic.mainactivitymvp

import android.content.Context
import android.content.SharedPreferences
import com.example.neteasecloudmusic.MyApplication
import com.example.neteasecloudmusic.loginactivity.loginbyphone.LoginCathe
import com.example.neteasecloudmusic.loginactivity.loginbyphone.MyLoginCatheObjectFileName
import com.example.neteasecloudmusic.loginactivity.loginbyphone.loginResult
import com.example.neteasecloudmusic.mytools.filedownload.readObjectFile
import com.example.neteasecloudmusic.userfragmentmvp.UserFragment

var playListResult=MainActivityModel.PlayListResult()
val spName=UserFragment.USER_BASIC_SP_NAME
val sp: SharedPreferences =MyApplication.getContext().getSharedPreferences(spName,Context.MODE_PRIVATE)
const val baseUrl = "http://sandyz.ink:3000"

class MainActivityModel : MainActivityContract.MainActivityIModel{
    //数据对象

    val TAG="MainActivityModel"
    override fun getLoginUrl(): String {
        //登陆地址
        val string: String = "/login/cellphone"

        var phoneNumber:String?=null
        var password:String?=null
        var isLogin=sp.getBoolean("is_login",false)
        if (isLogin){
            var mData= readObjectFile(MyLoginCatheObjectFileName) as LoginCathe
            phoneNumber=mData.phoneNumber
            password=mData.password
        }
        return "$baseUrl$string?phone=$phoneNumber&password=$password"
    }

    override fun playList(): String {
        val playListUrl="/user/playlist?uid="
        return baseUrl+playListUrl+loginResult.account?.id
    }


    class PlayListResult {
    var code: Int = 0
    var more: Boolean = false
    var playlist: List<Playlist>? = null
    var version: String = ""
        override fun toString(): String {
            return "PlayListResult(code=$code, more=$more, playlist=$playlist, version='$version')"
        }

    }

class Playlist {
    var adType: Int = 0
    var anonimous: Boolean = false
    var artists: Any? = Any()
    var backgroundCoverId: Int = 0
    var backgroundCoverUrl: Any? = Any()
    var cloudTrackCount: Int = 0
    var commentThreadId: String = ""
    var coverImgId: Long = 0
    var coverImgId_str: String = ""
    var coverImgUrl: String = ""
    var createTime: Long = 0
    var creator: Creator = Creator()
    var description: String? = ""
    var englishTitle: Any? = Any()
    var highQuality: Boolean = false
    var id: Long = 0
    var name: String = ""
    var newImported: Boolean = false
    var opRecommend: Boolean = false
    var ordered: Boolean = false
    var playCount: Int = 0
    var privacy: Int = 0
    var recommendInfo: Any? = Any()
    var specialType: Int = 0
    var status: Int = 0
    var subscribed: Boolean = false
    var subscribedCount: Int = 0
    var subscribers: List<Any> = ArrayList()
    var tags: List<Any> = ArrayList()
    var titleImage: Int = 0
    var titleImageUrl: Any? = Any()
    var totalDuration: Int = 0
    var trackCount: Int = 0
    var trackNumberUpdateTime: Long = 0
    var trackUpdateTime: Long = 0
    var tracks: Any? = Any()
    var updateFrequency: Any? = Any()
    var updateTime: Long = 0
    var userId: Long = 0
    override fun toString(): String {
        return "Playlist(adType=$adType, anonimous=$anonimous, artists=$artists, backgroundCoverId=$backgroundCoverId, backgroundCoverUrl=$backgroundCoverUrl, cloudTrackCount=$cloudTrackCount, commentThreadId='$commentThreadId', coverImgId=$coverImgId, coverImgId_str='$coverImgId_str', coverImgUrl='$coverImgUrl', createTime=$createTime, creator=$creator, description=$description, englishTitle=$englishTitle, highQuality=$highQuality, id=$id, name='$name', newImported=$newImported, opRecommend=$opRecommend, ordered=$ordered, playCount=$playCount, privacy=$privacy, recommendInfo=$recommendInfo, specialType=$specialType, status=$status, subscribed=$subscribed, subscribedCount=$subscribedCount, subscribers=$subscribers, tags=$tags, titleImage=$titleImage, titleImageUrl=$titleImageUrl, totalDuration=$totalDuration, trackCount=$trackCount, trackNumberUpdateTime=$trackNumberUpdateTime, trackUpdateTime=$trackUpdateTime, tracks=$tracks, updateFrequency=$updateFrequency, updateTime=$updateTime, userId=$userId)"
    }

}

class Creator {
    var accountStatus: Int = 0
    var anchor: Boolean = false
    var authStatus: Int = 0
    var authenticationTypes: Int = 0
    var authority: Int = 0
    var avatarDetail: Any? = Any()
    var avatarImgId: Long = 0
    var avatarImgIdStr: String = ""
    var avatarImgId_str: String = ""
    var avatarUrl: String = ""
    var backgroundImgId: Long = 0
    var backgroundImgIdStr: String = ""
    var backgroundUrl: String = ""
    var birthday: Long= 0
    var city: Int = 0
    var defaultAvatar: Boolean = false
    var description: String = ""
    var detailDescription: String = ""
    var djStatus: Int = 0
    var expertTags: Any? = Any()
    var experts: Any? = Any()
    var followed: Boolean = false
    var gender: Int = 0
    var mutual: Boolean = false
    var nickname: String = ""
    var province: Int = 0
    var remarkName: String? = null
    var signature: String = ""
    var userId: Long = 0
    var userType: Int = 0
    var vipType: Int = 0
    override fun toString(): String {
        return "Creator(accountStatus=$accountStatus, anchor=$anchor, authStatus=$authStatus, authenticationTypes=$authenticationTypes, authority=$authority, avatarDetail=$avatarDetail, avatarImgId=$avatarImgId, avatarImgIdStr='$avatarImgIdStr', avatarImgId_str='$avatarImgId_str', avatarUrl='$avatarUrl', backgroundImgId=$backgroundImgId, backgroundImgIdStr='$backgroundImgIdStr', backgroundUrl='$backgroundUrl', birthday=$birthday, city=$city, defaultAvatar=$defaultAvatar, description='$description', detailDescription='$detailDescription', djStatus=$djStatus, expertTags=$expertTags, experts=$experts, followed=$followed, gender=$gender, mutual=$mutual, nickname='$nickname', province=$province, remarkName=$remarkName, signature='$signature', userId=$userId, userType=$userType, vipType=$vipType)"
    }

}
}