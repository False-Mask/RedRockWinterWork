package com.example.neteasecloudmusic.mytools;

import java.util.ArrayList;
import java.util.List;

public class FastJsonData {

    /*
    {
    "data": {
        "admin": false,
        "chapterTops": [],
        "coinCount": 0,
        "collectIds": [],
        "email": "",
        "icon": "",
        "id": 85463,
        "nickname": "tuzhiqiang",
        "password": "",
        "publicName": "tuzhiqiang",
        "token": "",
        "type": 0,
        "username": "tuzhiqiang"
    },
    "errorCode": 0,
    "errorMsg": ""
    }
     */

    private Data data=new Data();
    private int errorCode;
    private String errorMsg;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

class Data{
    private boolean admin;
    private List<Object> chapterTops=new ArrayList<>();
    private int coinCount;
    private List<Object> collectIds=new ArrayList<>();
    private String email;
    private String icon;
    private int id;
    private String nickname;
    private String password;
    private  String publicName;
    private String token;
    private int type;
    private String username;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<Object> getChapterTops() {
        return chapterTops;
    }

    public void setChapterTops(List<Object> chapterTops) {
        this.chapterTops = chapterTops;
    }

    public int getCoinCount() {
        return coinCount;
    }

    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
    }

    public List<Object> getCollectIds() {
        return collectIds;
    }

    public void setCollectIds(List<Object> collectIds) {
        this.collectIds = collectIds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

