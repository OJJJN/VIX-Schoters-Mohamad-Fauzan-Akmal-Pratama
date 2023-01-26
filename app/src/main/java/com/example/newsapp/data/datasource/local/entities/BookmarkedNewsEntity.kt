package com.example.newsapp.data.datasource.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.domain.models.NewsModel

@Entity(tableName = "news_bookmark")
data class BookmarkedNewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "source")
    val source: String?,
    @ColumnInfo(name = "author")
    val author: String?,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "url")
    val url: String?,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "published_at")
    val publishedAt: String?,
    @ColumnInfo(name = "content")
    val content: String?
) {
    companion object {
        fun fromDomainModel(news: NewsModel): BookmarkedNewsEntity {
            return BookmarkedNewsEntity(
                id = news.id,
                source = news.source,
                author = news.author,
                title = news.title,
                description = news.description,
                url = news.url,
                imageUrl = news.imageUrl,
                publishedAt = news.publishedAt,
                content = news.content,
            )
        }
    }
}

fun BookmarkedNewsEntity.asDomainModel(): NewsModel {
    return NewsModel(
        id = this.id,
        source = this.source ?: "Unknown",
        author = this.author ?: "Unknown",
        title = this.title ?: "Unknown",
        description = this.description ?: "Unknown",
        url = this.url ?: "Unknown",
        imageUrl = this.imageUrl ?: "Unknown",
        publishedAt = this.publishedAt ?: "Unknown",
        content = this.content ?: "Unknown",
        isBookmarked = true,
    )
}

