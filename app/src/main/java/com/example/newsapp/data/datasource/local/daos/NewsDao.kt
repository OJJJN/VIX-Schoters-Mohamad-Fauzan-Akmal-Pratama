package com.example.newsapp.data.datasource.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newsapp.data.datasource.local.entities.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: NewsEntity)
    @Delete
    suspend fun delete(news: NewsEntity)
    @Update
    suspend fun update(news: NewsEntity)
    @Query("SELECT * FROM news")
    fun getAllNews() : Flow<List<NewsEntity>>
    @Query("DELETE FROM news")
    suspend fun deleteAllEntries()
}