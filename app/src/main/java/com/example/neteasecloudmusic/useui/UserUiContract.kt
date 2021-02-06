package com.example.neteasecloudmusic.useui

interface UserUiContract {
    interface UserUiIView {
        
    }
    interface UserUiIModel{
        fun initUserDetails()

    }

    interface UserUiPresenter {
        fun initUserDetails()

    }
}