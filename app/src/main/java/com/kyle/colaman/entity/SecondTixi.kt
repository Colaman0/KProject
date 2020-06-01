package com.kyle.colaman.entity


import com.google.gson.annotations.SerializedName

data class SecondTixi(
    @SerializedName("children")
    var children: List<Any>? = listOf(),
    @SerializedName("courseId")
    var courseId: Int? = 0,
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("order")
    var order: Int? = 0,
    @SerializedName("parentChapterId")
    var parentChapterId: Int? = 0,
    @SerializedName("userControlSetTop")
    var userControlSetTop: Boolean? = false,
    @SerializedName("visible")
    var visible: Int? = 0
)