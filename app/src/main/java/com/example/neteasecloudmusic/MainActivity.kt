package com.example.neteasecloudmusic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.neteasecloudmusic.mytools.FastJsonData
import com.example.neteasecloudmusic.mytools.SendRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    
    var TAG="MainActivity"
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sendRequest=SendRequest()

        var hashMap=HashMap<String,String>()
        hashMap.put("username","tuzhiqiang")
        hashMap.put("password","123456")

        sendRequest.sendPostRequest("https://www.wanandroid.com/"
            ,"user/login"
            ,hashMap
            ,object: Callback<FastJsonData>{
                override fun onFailure(call: Call<FastJsonData>
                                       , t: Throwable)
                {
                    Log.d(TAG, "onFailure: ")
                }

                override fun onResponse(
                    call: Call<FastJsonData>
                    , response: Response<FastJsonData>
                )
                {
                    Log.d(TAG, "onResponse: ")
                }

            })
    }
}