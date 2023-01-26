package com.example.newsapp.data.datasource.remote

import com.example.newsapp.domain.models.NewsModel

interface INewsRemoteDatasource {
    suspend fun getHeadlines() : List<NewsModel>
}