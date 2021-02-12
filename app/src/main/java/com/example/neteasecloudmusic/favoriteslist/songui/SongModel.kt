package com.example.neteasecloudmusic.favoriteslist.songui

import com.example.neteasecloudmusic.mainactivitymvp.baseUrl

class SongModel :SongContract.SongIModel{
    override fun getUrl(songId: String): String {
        return "$baseUrl/song/url?id=${1329538017}"
    }


    //歌曲的详细信息
    data class SongData(
            var code: Int = 0,
            var `data`: List<Data> = listOf()
    )

    data class Data(
            var br: Int = 0,
            var canExtend: Boolean = false,
            var code: Int = 0,
            var encodeType: String = "",
            var expi: Int = 0,
            var fee: Int = 0,
            var flag: Int = 0,
            var freeTimeTrialPrivilege: FreeTimeTrialPrivilege = FreeTimeTrialPrivilege(),
            var freeTrialInfo: Any? = Any(),
            var freeTrialPrivilege: FreeTrialPrivilege = FreeTrialPrivilege(),
            var gain: Int = 0,
            var id: Int = 0,
            var level: String = "",
            var md5: String = "",
            var payed: Int = 0,
            var size: Int = 0,
            var type: String = "",
            var uf: Any? = Any(),
            var url: String = "",
            var urlSource: Int = 0
    )

    data class FreeTimeTrialPrivilege(
            var remainTime: Int = 0,
            var resConsumable: Boolean = false,
            var type: Int = 0,
            var userConsumable: Boolean = false
    )

    data class FreeTrialPrivilege(
            var resConsumable: Boolean = false,
            var userConsumable: Boolean = false
    )

}