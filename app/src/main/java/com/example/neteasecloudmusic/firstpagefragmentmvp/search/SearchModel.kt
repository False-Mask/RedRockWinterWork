package com.example.neteasecloudmusic.firstpagefragmentmvp.search

import com.example.neteasecloudmusic.mainactivitymvp.baseUrl

class SearchModel :SearchContract.SearchIModel {

    var searchResult=SearchResult()

    //发送获取到Search的数据
    override fun getSearchUrlTail(keyboard: String): Pair<String, String> {
        return Pair("$baseUrl/search", "keywords=$keyboard")
    }


    //搜到的数据
    data class SearchResult(
    var code: Int = 0,
    var result: Result = Result()
)

data class Result(
    var songCount: Int = 0,
    var songs: List<Song> = listOf()
)

data class Song(
    var a: Any? = Any(),
    var al: Al = Al(),
    var alia: List<String> = listOf(),
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
    var h: H? = H(),
    var id: Int = 0,
    var l: L = L(),
    var m: M? = M(),
    var mark: Long = 0,
    var mst: Int = 0,
    var mv: Int = 0,
    var name: String = "",
    var no: Int = 0,
    var noCopyrightRcmd: Any? = Any(),
    var originCoverType: Int = 0,
    var originSongSimpleData: Any? = Any(),
    var pop: Int = 0,
    var privilege: Privilege = Privilege(),
    var pst: Int = 0,
    var publishTime: Long = 0,
    var rt: String? = "",
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
    var alia: List<String> = listOf(),
    var alias: List<String> = listOf(),
    var id: Int = 0,
    var name: String = "",
    var tns: List<Any> = listOf()
)

data class H(
    var br: Int = 0,
    var fid: Int = 0,
    var size: Int = 0,
    var vd: Int = 0
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


