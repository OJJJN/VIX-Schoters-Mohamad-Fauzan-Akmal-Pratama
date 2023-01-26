package com.example.newsapp.data.datasource.local

import com.example.newsapp.domain.models.NewsModel
import kotlinx.coroutines.flow.Flow

interface INewsLocalDatasource {
    suspend fun cacheNews(news: List<NewsModel>)
    suspend fun bookmarkNews(news: NewsModel)
    suspend fun deleteBookmarkedNews(news: NewsModel)
    fun getCachedNews() : Flow<List<NewsModel>>
    fun getAllBookmarkedNews() : Flow<List<NewsModel>>
}