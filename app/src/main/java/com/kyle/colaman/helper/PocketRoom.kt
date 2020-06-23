package com.kyle.colaman.helper

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kyle.colaman.api.PocketDao
import com.kyle.colaman.entity.ArticleRoomEntity

/**
 * Author   : kyle
 * Date     : 2020/6/23
 * Function : pocket功能实现，加入到数据库中
 */

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(ArticleRoomEntity::class), version = 1, exportSchema = false)
abstract class PocketRoom : RoomDatabase() {

    abstract fun pocketDao(): PocketDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PocketRoom? = null

        fun getDatabase(context: Context): PocketRoom {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PocketRoom::class.java,
                    "pocket"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}