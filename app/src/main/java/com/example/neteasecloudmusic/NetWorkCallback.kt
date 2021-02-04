package com.example.neteasecloudmusic

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import com.example.neteasecloudmusic.mytools.toast.MyToast

class NetWorkCallback : ConnectivityManager.NetworkCallback() {
    var context=MyApplication.getContext()
    val TAG="NetWorkCallback"
    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
        super.onBlockedStatusChanged(network, blocked)
        Log.d(TAG, "onBlockedStatusChanged: ")
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        Log.d(TAG, "onCapabilitiesChanged: ")
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)){
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    MyToast().sendToast(context,"WiFi",Toast.LENGTH_SHORT)
                }
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    MyToast().sendToast(context,"流量欸",Toast.LENGTH_SHORT)
                }
                else -> {
                    MyToast().sendToast(context,"不知道你用的啥",Toast.LENGTH_SHORT)
                }
            }
        }
    }

    //断开连接了
    override fun onLost(network: Network) {
        super.onLost(network)
        Log.d(TAG, "onLost: ")
//        MyToast().sendToast(context,"你咋没网络了",Toast.LENGTH_SHORT)
        Toast.makeText(context,"onLost",Toast.LENGTH_LONG).show()

    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
        Log.d(TAG, "onLinkPropertiesChanged: ")
        Toast.makeText(context,"onLinkPropertiesChanged",Toast.LENGTH_LONG).show()

    }
    //不可用
    override fun onUnavailable() {
        super.onUnavailable()
        Log.d(TAG, "onUnavailable: ")
        Toast.makeText(context,"onUnavailable",Toast.LENGTH_LONG).show()
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        Log.d(TAG, "onLosing: ")
    }
    //可用
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Log.d(TAG, "onAvailable: ")
//        MyToast().sendToast(context,"网络正常",Toast.LENGTH_SHORT)
        Toast.makeText(context,"onAvailable",Toast.LENGTH_LONG).show()
    }
}