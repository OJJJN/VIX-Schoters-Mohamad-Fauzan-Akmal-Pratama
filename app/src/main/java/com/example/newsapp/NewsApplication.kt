package com.example.newsapp

import android.app.Application
import com.example.newsapp.data.datasource.local.NewsRoomDatabase
import com.facebook.drawee.backends.pipeline.Fresco

class NewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }

    val database: NewsRoomDatabase by lazy {
        NewsRoomDatabase.getDatabase(this)
    }
}