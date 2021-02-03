package com.example.neteasecloudmusic.loginactivity.loginbyphone

import android.app.Activity

//写一个Activity管理类 使得我们能过随时随地的退出activity
open class ActivityController {
    object Static{
        var list= mutableListOf<Activity>()
        fun finishAll(){
            for (activity in list){
                if (!activity.isFinishing){
                    activity.finish()
                }
            }
        }
        fun addActivity(activity: Activity){
            list.add(activity)
        }
        fun removeActivity(activity: Activity){
            list.remove(activity)
        }
    }

}