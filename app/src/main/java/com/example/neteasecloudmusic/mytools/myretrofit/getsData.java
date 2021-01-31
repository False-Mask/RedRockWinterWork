package com.example.neteasecloudmusic.mytools.myretrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//实验数据
public class getsData{

    @SerializedName("errorCode")
    public int errorCode;
    @SerializedName("errorMsg")
    public String errorMsg;
    @SerializedName("data")
    public List<DataDTO> data;

    public static class DataDTO {
        @SerializedName("courseId")
        public int courseId;
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("order")
        public int order;
        @SerializedName("parentChapterId")
        public int parentChapterId;
        @SerializedName("userControlSetTop")
        public boolean userControlSetTop;
        @SerializedName("visible")
        public int visible;
        @SerializedName("children")
        public List<?> children;
    }
}