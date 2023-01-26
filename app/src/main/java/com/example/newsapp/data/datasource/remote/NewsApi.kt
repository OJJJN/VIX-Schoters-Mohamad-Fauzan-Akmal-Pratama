package com.example.newsapp.data.datasource.remote

import com.example.newsapp.data.datasource.remote.models.NewsApiModel
import retrofit2.http.GET
import retrofit2.http.Query

private val baseUrl = "https://newsapi.org/v2/"

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getHeadlines(
        @Query("apiKey") apiKey: String = "f22df70d807849cc89f54744cdd37709",
        @Query("country") country: String = "id",
    ) : NewsApiModel
}

object NewsApi {
    val networkService: NewsApiService by lazy {
        RetrofitProvider.getRetrofit(baseUrl).create(NewsApiService::class.java)
    }
}