package com.example.neteasecloudmusic.favoriteslist

import com.example.neteasecloudmusic.mainactivitymvp.baseUrl
var playListDetailsResult= FavoritesModel.PlayListDetails()
class FavoritesModel :FavoritesContract.FavoritesIModel{
    override fun getSongs(favoriteId: String): String {
        return "$baseUrl/playlist/detail?id=$favoriteId"
    }

    //歌单的详细信息 歌曲的地址 歌手的背景啥的
    data class PlayListDetails(
        var code: Int = 0,
        var playlist: Playlist = Playlist(),
        var privileges: List<Privilege> = listOf(),
        var relatedVideos: Any? = Any(),
        var urls: Any? = Any()
    )

    data class Playlist(
        var adType: Int = 0,
        var backgroundCoverId: Int = 0,
        var backgroundCoverUrl: Any? = Any(),
        var cloudTrackCount: Int = 0,
        var commentCount: Int = 0,
        var commentThreadId: String = "",
        var coverImgId: Long = 0,
        var coverImgId_str: String = "",
        var coverImgUrl: String = "",
        var createTime: Long = 0,
        var creator: Creator = Creator(),
        var description: Any? = Any(),
        var englishTitle: Any? = Any(),
        var highQuality: Boolean = false,
        var id: Long = 0,
        var name: String = "",
        var newImported: Boolean = false,
        var opRecommend: Boolean = false,
        var ordered: Boolean = false,
        var playCount: Int = 0,
        var privacy: Int = 0,
        var shareCount: Int = 0,
        var specialType: Int = 0,
        var status: Int = 0,
        var subscribed: Boolean = false,
        var subscribedCount: Int = 0,
        var subscribers: List<Any> = listOf(),
        var tags: List<Any> = listOf(),
        var titleImage: Int = 0,
        var titleImageUrl: Any? = Any(),
        var trackCount: Int = 0,
        var trackIds: List<TrackId> = listOf(),
        var trackNumberUpdateTime: Long = 0,
        var trackUpdateTime: Long = 0,
        var tracks: List<Track> = listOf(),
        var updateFrequency: Any? = Any(),
        var updateTime: Long = 0,
        var userId: Long = 0,
        var videoIds: Any? = Any(),
        var videos: Any? = Any()
    )

    data class Privilege(
        var chargeInfoList: List<ChargeInfo> = listOf(),
        var cp: Int = 0,
        var cs: Boolean = false,
        var dl: Int = 0,
        var downloadMaxbr: Int = 0,
        var fee: Int = 0,
        var fl: Int = 0,
        var flag: Int = 0,
        var freeTrialPrivilege: FreeTrialPrivilege = FreeTrialPrivilege(),
        var id: Int = 0,
        var maxbr: Int = 0,
        var payed: Int = 0,
        var pl: Int = 0,
        var playMaxbr: Int = 0,
        var preSell: Boolean = false,
        var sp: Int = 0,
        var st: Int = 0,
        var subp: Int = 0,
        var toast: Boolean = false
    )

    data class Creator(
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

    data class TrackId(
        var alg: Any? = Any(),
        var at: Long = 0,
        var id: Int = 0,
        var v: Int = 0
    )

    data class Track(
        var a: Any? = Any(),
        var al: Al = Al(),
        var alia: List<Any> = listOf(),
        var ar: List<Ar> = listOf(),
        var cd: String = "",
        var cf: String = "",
        var copyright: Int = 0,
        var cp: Int = 0,
        var crbt: Any? = Any(),
        var djId: Int = 0,
        var dt: Int = 0,
        var fee: Int = 0,
        var ftype: Int = 0,
        var h: Any? = Any(),
        var id: Int = 0,
        var l: L = L(),
        var m: M = M(),
        var mark: Int = 0,
        var mst: Int = 0,
        var mv: Int = 0,
        var name: String = "",
        var no: Int = 0,
        var noCopyrightRcmd: Any? = Any(),
        var originCoverType: Int = 0,
        var originSongSimpleData: Any? = Any(),
        var pop: Int = 0,
        var pst: Int = 0,
        var publishTime: Long = 0,
        var rt: Any? = Any(),
        var rtUrl: Any? = Any(),
        var rtUrls: List<Any> = listOf(),
        var rtype: Int = 0,
        var rurl: Any? = Any(),
        var s_id: Int = 0,
        var single: Int = 0,
        var st: Int = 0,
        var t: Int = 0,
        var v: Int = 0
    )

    data class Al(
        var id: Int = 0,
        var name: String = "",
        var pic: Long = 0,
        var picUrl: String = "",
        var pic_str: String = "",
        var tns: List<Any> = listOf()
    )

    data class Ar(
        var alias: List<Any> = listOf(),
        var id: Int = 0,
        var name: String = "",
        var tns: List<Any> = listOf()
    )

    data class L(
        var br: Int = 0,
        var fid: Int = 0,
        var size: Int = 0,
        var vd: Int = 0
    )

    data class M(
        var br: Int = 0,
        var fid: Int = 0,
        var size: Int = 0,
        var vd: Int = 0
    )

    data class ChargeInfo(
        var chargeMessage: Any? = Any(),
        var chargeType: Int = 0,
        var chargeUrl: Any? = Any(),
        var rate: Int = 0
    )

    data class FreeTrialPrivilege(
        var resConsumable: Boolean = false,
        var userConsumable: Boolean = false
    )
}