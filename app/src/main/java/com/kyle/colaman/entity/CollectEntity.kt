package com.kyle.colaman.entity


import com.google.gson.annotations.SerializedName

data class CollectEntity(
    @SerializedName("author")
    var author: String? = "",
    @SerializedName("shareUser")
    var shareUser: String? = "",
    @SerializedName("chapterId")
    var chapterId: Int? = 0,
    @SerializedName("chapterName")
    var chapterName: String? = "",
    @SerializedName("courseId")
    var courseId: Int? = 0,
    @SerializedName("desc")
    var desc: String? = "",
    @SerializedName("envelopePic")
    var envelopePic: String? = "",
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("link")
    var link: String? = "",
    @SerializedName("niceDate")
    var niceDate: String? = "",
    @SerializedName("origin")
    var origin: String? = "",
    @SerializedName("originId")
    var originId: Int? = 0,
    @SerializedName("publishTime")
    var publishTime: Long? = 0,
    @SerializedName("title")
    var title: String? = "",
    @SerializedName("userId")
    var userId: Int? = 0,
    @SerializedName("visible")
    var visible: Int? = 0,
    @SerializedName("zan")
    var zan: Int? = 0
)