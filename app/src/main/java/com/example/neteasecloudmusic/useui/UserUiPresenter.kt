package com.example.neteasecloudmusic.useui

class UserUiPresenter(userUiActivity: UserUiActivity) :UserUiContract.UserUiPresenter{
    var view=userUiActivity
    var model=UserUiModel()
    override fun initUserDetails() {
        model.initUserDetails()
    }
}