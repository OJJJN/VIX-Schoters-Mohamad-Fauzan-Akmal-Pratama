package com.example.newsapp.domain.models

data class NewsModel(
    val id: Int,
    val source: String,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val imageUrl: String,
    val publishedAt: String,
    val content: String,
    var isBookmarked: Boolean,
)
