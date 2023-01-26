package com.example.newsapp.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsapp.data.datasource.local.daos.BookmarkedNewsDao
import com.example.newsapp.data.datasource.local.daos.NewsDao
import com.example.newsapp.data.datasource.local.entities.BookmarkedNewsEntity
import com.example.newsapp.data.datasource.local.entities.NewsEntity

@Database(
    entities = arrayOf(NewsEntity::class, BookmarkedNewsEntity::class),
    version = 1,
    exportSchema = false
)
abstract class NewsRoomDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun bookmarkedNewsDao(): BookmarkedNewsDao


    companion object {
        @Volatile
        private var INSTANCE: NewsRoomDatabase? = null
        fun getDatabase(context: Context): NewsRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsRoomDatabase::class.java,
                    "news_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}