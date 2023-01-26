package com.example.newsapp.data.repositories

import android.util.Log
import com.example.newsapp.data.datasource.local.INewsLocalDatasource
import com.example.newsapp.data.datasource.remote.INewsRemoteDatasource
import com.example.newsapp.domain.models.NewsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

class NewsRepository(
    private val newsRemoteDatasource: INewsRemoteDatasource,
    private val newsLocalDatasource: INewsLocalDatasource
) {
    private val TAG = "NewsRepository"

    val news: Flow<List<NewsModel>> = newsLocalDatasource.getCachedNews()

    suspend fun refreshNews() : List<NewsModel> {
        try {
            val response = newsRemoteDatasource.getHeadlines()
            newsLocalDatasource.cacheNews(response)
            return response
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
            throw e
        }
    }

    fun getBookmarkedNews(): Flow<List<NewsModel>> {
        return newsLocalDatasource.getAllBookmarkedNews()
    }

    suspend fun bookmarkNews(news: NewsModel) {
        newsLocalDatasource.bookmarkNews(news)
    }

    suspend fun deleteBookmarkedNews(news: NewsModel) {
        newsLocalDatasource.deleteBookmarkedNews(news)
    }
}