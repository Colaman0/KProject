package com.kyle.colaman.entity

data class UserInfoEntity(
    val admin: Boolean = false,
    val chapterTops: List<Any> = emptyList(),
    val collectIds: List<Any> = emptyList(),
    val email: String = "",
    val icon: String = "",
    val id: Int = -1,
    val nickname: String = "",
    val password: String = "",
    val publicName: String = "",
    val token: String = "",
    val type: Int = -1,
    val username: String = ""
)