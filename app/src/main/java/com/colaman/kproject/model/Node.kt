package com.colaman.kproject.model

data class Node(
    var avatar_large: String,
    var avatar_mini: String,
    var avatar_normal: String,
    var footer: String,
    var header: String,
    var id: Int,
    var name: String,
    var parent_node_name: String,
    var root: Boolean,
    var stars: Int,
    var title: String,
    var title_alternative: String,
    var topics: Int,
    var url: String
)