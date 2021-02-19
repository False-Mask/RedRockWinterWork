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
    var hasMore: Boolean = false,
    var songCount: Int = 0,
    var songs: List<Song> = listOf()
)

data class Song(
    var album: Album = Album(),
    var alias: List<String> = listOf(),
    var artists: List<ArtistX> = listOf(),
    var copyrightId: Int = 0,
    var duration: Int = 0,
    var fee: Int = 0,
    var ftype: Int = 0,
    var id: Int = 0,
    var mark: Int = 0,
    var mvid: Int = 0,
    var name: String = "",
    var rUrl: Any? = Any(),
    var rtype: Int = 0,
    var status: Int = 0
)

data class Album(
    var alia: List<String> = listOf(),
    var artist: Artist = Artist(),
    var copyrightId: Int = 0,
    var id: Int = 0,
    var mark: Int = 0,
    var name: String = "",
    var picId: Long = 0,
    var publishTime: Long = 0,
    var size: Int = 0,
    var status: Int = 0
)

data class ArtistX(
    var albumSize: Int = 0,
    var alias: List<Any> = listOf(),
    var id: Int = 0,
    var img1v1: Int = 0,
    var img1v1Url: String = "",
    var name: String = "",
    var picId: Int = 0,
    var picUrl: Any? = Any(),
    var trans: Any? = Any()
)

data class Artist(
    var albumSize: Int = 0,
    var alias: List<Any> = listOf(),
    var id: Int = 0,
    var img1v1: Int = 0,
    var img1v1Url: String = "",
    var name: String = "",
    var picId: Int = 0,
    var picUrl: Any? = Any(),
    var trans: Any? = Any()
)


}


