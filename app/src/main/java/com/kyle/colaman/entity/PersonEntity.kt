package com.kyle.colaman.entity


import com.google.gson.annotations.SerializedName

data class PersonEntity(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("person")
    var person: Int = 0
)