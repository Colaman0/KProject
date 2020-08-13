package com.kyle.colaman.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Author   : kyle
 * Date     : 2020/6/23
 * Function : 存放在数据库里的文章信息实体类
 */

@Entity(tableName = "pocket_article_table")
data class ArticleRoomEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "articleId")
    val articleId: Int,
    @ColumnInfo(name = "articleTitle")
    val title: String,
    @ColumnInfo(name = "link")
    val link: String,
    @ColumnInfo(name = "articleDesc")
    val desc: String,
    @ColumnInfo(name = "add_time")
    val addTime: Long
)