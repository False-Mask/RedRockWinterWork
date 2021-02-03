package com.example.neteasecloudmusic.mainactivitymvp

import android.content.SharedPreferences
import android.util.Log
import com.example.neteasecloudmusic.MainActivity
import com.example.neteasecloudmusic.userfragmentmvp.UserFragment

class MainActivityPresenter (activity:MainActivity): MainActivityContract.MainActivityPresenter{
    var view = activity
    var model=MainActivityModel()

}