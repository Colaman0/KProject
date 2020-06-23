package com.kyle.colaman.api

import androidx.room.*
import com.kyle.colaman.entity.ArticleRoomEntity

/**
 * Author   : kyle
 * Date     : 2020/6/23
 * Function : pocket功能查询数据库接口
 */

@Dao
interface PocketDao {

    @Query("SELECT * from pocket_article_table")
    fun getPocketArticles(): List<ArticleRoomEntity>

    @Delete
    fun removePocketArticle(entity: ArticleRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPocketArticle(entity: ArticleRoomEntity)
}