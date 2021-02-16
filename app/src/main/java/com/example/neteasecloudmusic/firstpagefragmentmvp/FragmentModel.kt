package com.example.neteasecloudmusic.firstpagefragmentmvp

import com.example.neteasecloudmusic.mainactivitymvp.baseUrl

class FragmentModel:FirstFragmentContract.FirstFragmentIModel {
        var banners=Banners()
        //相关歌单
        var relatedFavorites=RelatedFavorites()
        //网友精选歌单
        var topFavorites=TopFavorites()

        override fun getBannerUrl(): String {
            return "$baseUrl/banner?type=1"
    }

        override fun getRecommendUrl(recommendSong: String): String {
                return "$baseUrl/related/playlist?id=$recommendSong"
        }


    //获取发送的地址
    override fun getTopFavorites(toString: String): String {
        return "$baseUrl/top/playlist/highquality?before=$toString&limit=5"
    }

}

//Banner数据类
data class Banners(
        var banners: List<Banner> = listOf(),
        var code: Int = 0
)

data class Banner(
        var adDispatchJson: Any? = Any(),
        var adLocation: Any? = Any(),
        var adSource: Any? = Any(),
        var adid: Any? = Any(),
        var adurlV2: Any? = Any(),
        var alg: Any? = Any(),
        var bannerId: String = "",
        var dynamicVideoData: Any? = Any(),
        var encodeId: String = "",
        var event: Any? = Any(),
        var exclusive: Boolean = false,
        var extMonitor: Any? = Any(),
        var extMonitorInfo: Any? = Any(),
        var monitorBlackList: Any? = Any(),
        var monitorClick: Any? = Any(),
        var monitorClickList: List<Any> = listOf(),
        var monitorImpress: Any? = Any(),
        var monitorImpressList: List<Any> = listOf(),
        var monitorType: Any? = Any(),
        var pic: String = "",
        var pid: Any? = Any(),
        var program: Any? = Any(),
        var requestId: String = "",
        var scm: String = "",
        var showAdTag: Boolean = false,
        var showContext: Any? = Any(),
        var song: Any? = Any(),
        var targetId: Long = 0,
        var targetType: Int = 0,
        var titleColor: String = "",
        var typeTitle: String = "",
        var url: String? = "",
        var video: Any? = Any()
)

//推荐歌单播放列表
data class RecommendSong(
        var id:String="",
        var coverUrl:String="",
        var nama:String=""
)

//相关的歌单数据
data class RelatedFavorites(
    var code: Int = 0,
    var playlists: List<Playlists> = listOf()
)
data class Playlists(
    var coverImgUrl: String = "",
    var creator: CreatorF = CreatorF(),
    var id: String = "",
    var name: String = ""
)

data class CreatorF(
    var nickname: String = "",
    var userId: String = ""
)

//网友精选歌单数据

data class TopFavorites(
    var code: Int = 0,
    var lasttime: Long = 0,
    var more: Boolean = false,
    var playlists: List<PlaylistsTF> = listOf(),
    var total: Int = 0
)

data class PlaylistsTF(
    var adType: Int = 0,
    var anonimous: Boolean = false,
    var cloudTrackCount: Int = 0,
    var commentCount: Int = 0,
    var commentThreadId: String = "",
    var copywriter: String = "",
    var coverImgId: Long = 0,
    var coverImgId_str: String = "",
    var coverImgUrl: String = "",
    var coverStatus: Int = 0,
    var createTime: Long = 0,
    var creator: CreatorTF = CreatorTF(),
    var description: String = "",
    var highQuality: Boolean = false,
    var id: Long = 0,
    var name: String = "",
    var newImported: Boolean = false,
    var ordered: Boolean = false,
    var playCount: Int = 0,
    var privacy: Int = 0,
    var recommendInfo: Any? = Any(),
    var shareCount: Int = 0,
    var specialType: Int = 0,
    var status: Int = 0,
    var subscribed: Boolean = false,
    var subscribedCount: Int = 0,
    var subscribers: List<Subscriber> = listOf(),
    var tag: String = "",
    var tags: List<String> = listOf(),
    var totalDuration: Int = 0,
    var trackCount: Int = 0,
    var trackNumberUpdateTime: Long = 0,
    var trackUpdateTime: Long = 0,
    var tracks: Any? = Any(),
    var updateTime: Long = 0,
    var userId: Int = 0
)

data class CreatorTF(
    var accountStatus: Int = 0,
    var anchor: Boolean = false,
    var authStatus: Int = 0,
    var authenticationTypes: Int = 0,
    var authority: Int = 0,
    var avatarDetail: AvatarDetail = AvatarDetail(),
    var avatarImgId: Long = 0,
    var avatarImgIdStr: String = "",
    var avatarImgId_str: String = "",
    var avatarUrl: String = "",
    var backgroundImgId: Long = 0,
    var backgroundImgIdStr: String = "",
    var backgroundUrl: String = "",
    var birthday: Long = 0,
    var city: Int = 0,
    var defaultAvatar: Boolean = false,
    var description: String = "",
    var detailDescription: String = "",
    var djStatus: Int = 0,
    var expertTags: List<String>? = listOf(),
    var experts: Any? = Any(),
    var followed: Boolean = false,
    var gender: Int = 0,
    var mutual: Boolean = false,
    var nickname: String = "",
    var province: Int = 0,
    var remarkName: Any? = Any(),
    var signature: String = "",
    var userId: Int = 0,
    var userType: Int = 0,
    var vipType: Int = 0
)

data class Subscriber(
    var accountStatus: Int = 0,
    var anchor: Boolean = false,
    var authStatus: Int = 0,
    var authenticationTypes: Int = 0,
    var authority: Int = 0,
    var avatarDetail: Any? = Any(),
    var avatarImgId: Long = 0,
    var avatarImgIdStr: String = "",
    var avatarImgId_str: String = "",
    var avatarUrl: String = "",
    var backgroundImgId: Long = 0,
    var backgroundImgIdStr: String = "",
    var backgroundUrl: String = "",
    var birthday: Long = 0,
    var city: Int = 0,
    var defaultAvatar: Boolean = false,
    var description: String = "",
    var detailDescription: String = "",
    var djStatus: Int = 0,
    var expertTags: Any? = Any(),
    var experts: Any? = Any(),
    var followed: Boolean = false,
    var gender: Int = 0,
    var mutual: Boolean = false,
    var nickname: String = "",
    var province: Int = 0,
    var remarkName: Any? = Any(),
    var signature: String = "",
    var userId: Long = 0,
    var userType: Int = 0,
    var vipType: Int = 0
)

data class AvatarDetail(
    var identityIconUrl: String = "",
    var identityLevel: Int = 0,
    var userType: Int = 0
)
