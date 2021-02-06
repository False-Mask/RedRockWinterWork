package com.example.neteasecloudmusic

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import com.example.neteasecloudmusic.mytools.toast.MyToast

//一个接口回调多个类
object NetWorkChangeImp: ConnectivityManager.NetworkCallback() {

    private var backList= ArrayList<NetCallback>()
    private var context=MyApplication.getContext()
    private val TAG="NetWorkCallback"

    fun addListener(back:NetCallback){
        backList.add(back)
    }

    //上网能力发送改变调用
    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        Log.d(TAG, "onCapabilitiesChanged: ")

        //去掉免得Toast影响用户体验
//        MyToast().sendToast(context,"网络发生改变~",Toast.LENGTH_SHORT)
        //当网络状态稳定下来的额时候
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)){
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    //WiFi
                }
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    //流量
                    MyToast().sendToast(context,"注意流量消耗哦~",Toast.LENGTH_SHORT)
                }
                else -> {
                    //我咋知道你用的啥 反正已经不重要了
                }
            }
        }
    }

    //断开连接了
    override fun onLost(network: Network) {
        super.onLost(network)
        Log.d(TAG, "onLost: ")
    }


    //不可用
    override fun onUnavailable() {
        super.onUnavailable()
        Log.d(TAG, "onUnavailable: ")
        for (x in backList){
            x.onUnavailable()
        }
    }

    //可用
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Log.d(TAG, "onAvailable: ")
        //MyToast().sendToast(context,"网络正常",Toast.LENGTH_SHORT)
        for (x in backList){
            x.onAvailable()
        }
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        Log.d(TAG, "onLosing: ")
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
        Log.d(TAG, "onLinkPropertiesChanged: ")
    }

    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
        super.onBlockedStatusChanged(network, blocked)
        Log.d(TAG, "onBlockedStatusChanged: ")
    }


    interface NetCallback{
        fun onAvailable()
        fun onUnavailable()
    }
}