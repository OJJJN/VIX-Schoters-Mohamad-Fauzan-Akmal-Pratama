package com.example.newsapp.data.datasource.remote.models

import com.example.newsapp.domain.models.NewsModel

data class NewsApiModel(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

data class Article(
    val source: Source?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
)

fun Article.asDomainModel(): NewsModel {
    return NewsModel(
        id = 0,
        source = this.source?.name ?: "Unknown",
        author = this.author ?: "Unknown",
        title = this.title ?: "Unknown",
        description = this.description ?: "Unknown",
        url = this.url ?: "Unknown",
        imageUrl = this.urlToImage ?: "Unknown",
        publishedAt = this.publishedAt ?: "Unknown",
        content = this.content ?: "Unknown",
        isBookmarked = false,
    )
}

data class Source(
    val id: String?,
    val name: String?,
)