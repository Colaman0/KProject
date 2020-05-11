package com.colaman.wanandroid.model

data class V2Entity(
    var content: String,
    var content_rendered: String,
    var created: Int,
    var id: Int,
    var last_modified: Int,
    var last_reply_by: String,
    var last_touched: Int,
    var member: Member,
    var node: Node,
    var replies: Int,
    var title: String,
    var url: String
)