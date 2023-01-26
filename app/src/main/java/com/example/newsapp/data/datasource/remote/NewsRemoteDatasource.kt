package com.example.newsapp.data.datasource.remote

import com.example.newsapp.data.datasource.remote.models.asDomainModel
import com.example.newsapp.domain.models.NewsModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRemoteDatasource(
    private val newsApiService: NewsApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : INewsRemoteDatasource {

    override suspend fun getHeadlines(): List<NewsModel> {
        return withContext(dispatcher) {
            val response = newsApiService.getHeadlines()
            response.articles.map {
                it.asDomainModel()
            }
        }
    }


}