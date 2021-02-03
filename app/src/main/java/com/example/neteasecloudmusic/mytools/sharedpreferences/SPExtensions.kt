package com.example.neteasecloudmusic.mytools.sharedpreferences

import android.content.SharedPreferences

//ShardPreferences的扩展函数
fun SharedPreferences.put(putSomething:SharedPreferences.Editor.()
                        -> Unit){
    var editor=this.edit()
    editor.putSomething()
    editor.apply()
}
