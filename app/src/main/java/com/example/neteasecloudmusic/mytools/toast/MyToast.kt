package com.example.neteasecloudmusic.mytools.toast

import android.app.Activity
import android.content.Context
import android.widget.Toast

class MyToast {
    private object Static{
        var mToast: Toast? =null
        var count=0
    }

    //第二个参数是资源id
    fun sendToast(activity:Activity,resId:Int,time:Int){
        Static.apply {
            count++
            //将上一步的mToast取消掉
            mToast?.cancel()
            //由于cancel后的mToast不能Toast(实验结果是这样 不知道错没)
            //创建一个新的mToast
            mToast =Toast.makeText(activity,resId,time)
            mToast?.show()
            if (count >=500){
                //万一遇上了无聊的用户 疯狂点
                //这很不讲武德
                System.gc()
            }
        }

    }

    //第二个参数是string
    fun sendToast(activity:Activity,string: String,time:Int){

        Static.apply {
            count++
            //将上一步的mToast取消掉
            mToast?.cancel()
            //由于cancel后的mToast不能Toast(实验结果是这样 不知道错没)
            //创建一个新的mToast
            mToast =Toast.makeText(activity,string,time)
            mToast?.show()
            if (count >=500){
                //万一遇上了无聊的用户 疯狂点
                //这很不讲武德
                System.gc()
            }
        }

    }

    //第1个是context
    fun sendToast(context:Context, resId:Int, time:Int){
        Static.apply {
            count++
            //将上一步的mToast取消掉
            mToast?.cancel()
            //由于cancel后的mToast不能Toast(实验结果是这样 不知道错没)
            //创建一个新的mToast
            mToast =Toast.makeText(context,resId,time)
            mToast?.show()
            if (count >=500){
                //万一遇上了无聊的用户 疯狂点
                //这很不讲武德
                System.gc()
            }
        }
    }

    //第1个参数是context 2是string
    fun sendToast(context:Context, string: String, time:Int){

        Static.apply {
            count++
            //将上一步的mToast取消掉
            mToast?.cancel()
            //由于cancel后的mToast不能Toast(实验结果是这样 不知道错没)
            //创建一个新的mToast
            mToast =Toast.makeText(context,string,time)
            mToast?.show()
            if (count >=500){
                //万一遇上了无聊的用户 疯狂点
                //这很不讲武德
                System.gc()
            }
        }

    }
}