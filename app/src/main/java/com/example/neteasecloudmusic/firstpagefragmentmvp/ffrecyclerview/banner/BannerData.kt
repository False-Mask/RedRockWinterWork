package com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.banner

import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewData
import com.example.neteasecloudmusic.firstpagefragmentmvp.ffrecyclerview.adapter.ViewHolderFactory
import java.io.File
import java.io.Serializable

data class BannerData(
        //存放url的地址
        var netList: MutableList<NetBannerData> = mutableListOf(),
        //存放本地image的地址
        var localList: MutableList<LocalBannerData> = mutableListOf(),
        //Banner的数据类
        var banners: Banners =Banners()
):ViewData{
        //返回
        override fun getType(): Int {
            return ViewHolderFactory.BANNER_VIEW
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

//本地下载数据
data class LocalBannerData(
        var pic: File?=null,
        var url:String="NULL"
): Serializable {
    companion object {
        const val serialVersionUID=123456L
    }
}

//网络数据
data class NetBannerData(
        var imageUrl:String="",
        var webUrl:String=""
): Serializable {
    companion object {
        const val serialVersionUID=123457L
    }
}
