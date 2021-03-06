package com.example.neteasecloudmusic.mytools.net

import android.util.Log
import kotlinx.coroutines.*
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
//发送网络
class SendNetRequest{

    var TAG="SendNetRequest"


    /**
     * 发送post网络请求
     * url -> 对应的网址
     * back -> 回调的接口
     */

    fun sendPostRequest(url:String, tail:String, back: Back){

        GlobalScope.launch {
            withContext(Dispatchers.IO){
                var httpURLConnection:HttpURLConnection= URL(url).openConnection() as HttpURLConnection

                Log.d(TAG, "sendPostRequest: $tail")
                httpURLConnection.apply {
                    doInput=true
                    doOutput=true
                    requestMethod="POST"
                    readTimeout=5000
                    connectTimeout=5000
                    defaultUseCaches=false
                }
//                httpURLConnection.doInput=true
//                httpURLConnection.doOutput=true
//                httpURLConnection.requestMethod="POST"
//                //httpURLConnection.setRequestProperty("Content-Type", "application/json")
//                httpURLConnection.readTimeout=5000
//                httpURLConnection.connectTimeout=5000
//                httpURLConnection.defaultUseCaches=false

                var outputStream=httpURLConnection.outputStream
                var writer=BufferedWriter(OutputStreamWriter(outputStream))

                writer.write(tail)
                writer.flush()
                getResult(httpURLConnection,back)

                //关闭
                httpURLConnection.disconnect()
                outputStream.close()
                writer.close()
            }
        }




//        Thread{
//            var httpURLConnection:HttpURLConnection= URL(url).openConnection() as HttpURLConnection
//
//            httpURLConnection.doInput=true
//            httpURLConnection.doOutput=true
//            httpURLConnection.requestMethod="POST"
//            //httpURLConnection.setRequestProperty("Content-Type", "application/json")
//            httpURLConnection.readTimeout=5000
//            httpURLConnection.connectTimeout=5000
//            httpURLConnection.defaultUseCaches=false
//
//            var outputStream=httpURLConnection.outputStream
//            var writer=BufferedWriter(OutputStreamWriter(outputStream))
//
//            writer.write(tail)
//            writer.flush()
//            getResult(httpURLConnection,back)
//
//            //关闭
//            httpURLConnection.disconnect()
//            outputStream.close()
//            writer.close()
//        }.start()

    }

    /**中间代码（通过reader读取返回的内容）(减少重复代码)
     * connection -> 传入的网络连接
     * back -> 回调接口
     */
    fun getResult(connection: HttpURLConnection,back: Back){
        var stringBuilder=StringBuilder()
        if (connection.responseCode==HttpURLConnection.HTTP_OK){
            //代表连接成功

            var inputStream=connection.inputStream
            inputStream.bufferedReader().forEachLine {
                stringBuilder.append(it)
                Log.d(TAG, "getResult: "+it)
            }
            back.onResponded(stringBuilder.toString())
            Log.d(TAG, "网络请求成功")

            //关闭流
            inputStream.close()

        }else {
            back.onFailed(connection.responseCode)
            Log.d(TAG, "网络请求失败")
        }
    }

    /**发送Get请求
     * url -> 对应的网站地址
     * back -> 网络请求结束的回调接口
     */
    fun sendGetRequest(url:String,back: Back){

        GlobalScope.launch {
            withContext(Dispatchers.IO){
                var connection=URL(url).openConnection() as HttpURLConnection

                connection.connectTimeout=5000
                connection.readTimeout=5000
                connection.defaultUseCaches=false
                connection.doOutput=true
                connection.doOutput=true
                connection.requestMethod="GET"

                getResult(connection,back)

                //关闭连接
                connection.disconnect()
            }
        }



//        Thread{
//            var connection=URL(url).openConnection() as HttpURLConnection
//
//            connection.connectTimeout=5000
//            connection.readTimeout=5000
//            connection.defaultUseCaches=false
//            connection.doOutput=true
//            connection.doOutput=true
//            connection.requestMethod="GET"
//
//            getResult(connection,back)
//
//            //关闭连接
//            connection.disconnect()
//
//        }.start()

    }

    interface Back {
        fun onResponded(resultBody:String)
        fun onFailed(respondCode:Int)
    }
}
