package com.example.newsapp.data.datasource.local.daos

import androidx.room.*
import com.example.newsapp.data.datasource.local.entities.BookmarkedNewsEntity
import com.example.newsapp.domain.models.NewsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkedNewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: BookmarkedNewsEntity)
    @Query("DELETE FROM news_bookmark WHERE title = :title")
    suspend fun delete(title: String)
    @Query("SELECT * FROM news_bookmark")
    fun getAllNews() : Flow<List<BookmarkedNewsEntity>>
    @Query("SELECT title FROM news_bookmark")
    fun getAllBookmarkedTitle() : Flow<List<String?>>
}