package com.example.neteasecloudmusic.mytools

import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors


class SendRequest {
    fun <T> sendPostRequest(
        baseUrl:String
        , url:String
        , hashMap: HashMap<String,String>
        , callback: Callback<T>
    ){

        var retrofit=Retrofit.Builder()
            //添加baseUrl
            .baseUrl(baseUrl)
            //添加网络请求数据的Gson解析
            .addConverterFactory(GsonConverterFactory.create())
            //添加单线程池
            .callbackExecutor(Executors.newSingleThreadExecutor())
            //建立
            .build()













        var iSendRequest=retrofit.create(ISendRequest::class.java)
        var map= mutableMapOf<String,String>()
        map["username"]="tuzhiqiang"
        map["password"]="123456"
        var call=iSendRequest.addPostTail<T>(map)

        call.enqueue(callback)
    }


    fun <T> sendGetRequest(baseUrl:String
                           ,url:String
                           ,hashMap: HashMap<String,String>
                           ,callback:Callback<T>){

        var retrofit=Retrofit.Builder()
                //添加baseUrl
            .baseUrl(baseUrl)
                //添加网络请求数据的Gson解析
            .addConverterFactory(GsonConverterFactory.create())
                //添加单线程池
            .callbackExecutor(Executors.newSingleThreadExecutor())
                //建立
            .build()

        var iSendRequest=retrofit.create(ISendRequest::class.java)
        var call=iSendRequest.addGetTail<T>(url,hashMap)

        call.enqueue(callback)

    }
}