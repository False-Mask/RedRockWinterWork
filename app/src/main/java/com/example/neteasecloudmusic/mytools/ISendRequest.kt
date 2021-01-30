package com.example.neteasecloudmusic.mytools

import retrofit2.Call
import retrofit2.http.*

interface ISendRequest{

    //该接口方法用于放松Post请求
    @FormUrlEncoded
    @POST("user/login")
    //@Path(value = "url",encoded = true) url:String
    fun <T> addPostTail(@FieldMap hashMap: MutableMap<String,String>):Call<T>

    //该接口用于发送Get请求
    @GET("{url}")
//    @FormUrlEncoded
    fun <T> addGetTail(@Path(value = "url",encoded = true) url: String
                       ,@QueryMap hashMap: HashMap<String, String>):Call<T>
}
