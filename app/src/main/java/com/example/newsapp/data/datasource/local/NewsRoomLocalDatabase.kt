package com.example.newsapp.data.datasource.local

import android.util.Log
import com.example.newsapp.data.datasource.local.daos.BookmarkedNewsDao
import com.example.newsapp.data.datasource.local.daos.NewsDao
import com.example.newsapp.data.datasource.local.entities.BookmarkedNewsEntity
import com.example.newsapp.data.datasource.local.entities.NewsEntity
import com.example.newsapp.data.datasource.local.entities.asDomainModel
import com.example.newsapp.domain.models.NewsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map


class NewsRoomLocalDatabase(
    private val newsDao: NewsDao,
    private val bookmarkedNewsDao: BookmarkedNewsDao,
) : INewsLocalDatasource {

    private val TAG = "NewsRoomLocalDatabase"

    override suspend fun cacheNews(news: List<NewsModel>) {
        newsDao.deleteAllEntries()
        news.forEach {
            if (it.content != "Unknown") {
                Log.d(TAG, it.toString())
                newsDao.insert(NewsEntity.fromDomainModel(it))
            }
        }
    }

    override suspend fun bookmarkNews(news: NewsModel) {
        bookmarkedNewsDao.insert(BookmarkedNewsEntity.fromDomainModel(news))
    }

    override suspend fun deleteBookmarkedNews(news: NewsModel) {
        bookmarkedNewsDao.delete(news.title)
    }

    override fun getCachedNews(): Flow<List<NewsModel>> {
        return newsDao.getAllNews()
            .combine(bookmarkedNewsDao.getAllBookmarkedTitle()) { allNews, bookmarkedTitle ->
                allNews.map {
                    val isBookmarked = it.title in bookmarkedTitle
                    val news = it.asDomainModel()
                    news.isBookmarked = isBookmarked
                    news
                }
            }
    }

    override fun getAllBookmarkedNews(): Flow<List<NewsModel>> {
        return bookmarkedNewsDao.getAllNews().map {
            it.map {
                it.asDomainModel()
            }
        }
    }


}