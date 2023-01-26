package com.example.newsapp.ui.bookmark.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newsapp.R
import com.example.newsapp.data.repositories.NewsRepository
import com.example.newsapp.domain.models.NewsModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed class BookmarkUiState {
    data class Success(val data: List<NewsModel>) : BookmarkUiState()
    object Empty : BookmarkUiState()
}

class BookmarkViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val TAG = "BookmarkViewModel"

    private val _uiState = MutableStateFlow<BookmarkUiState>(BookmarkUiState.Empty)
    val uiState: StateFlow<BookmarkUiState> = _uiState

    init {
        getBookmarkedNews()
    }

    fun getBookmarkedNews() {
        viewModelScope.launch {
            newsRepository.getBookmarkedNews().collectLatest {
                Log.d(TAG, it.toString())
                _uiState.value = BookmarkUiState.Success(it)
            }
        }
    }

    fun deleteBookmarkedNews(news: NewsModel) {
        viewModelScope.launch {
            newsRepository.deleteBookmarkedNews(news)
        }
        getBookmarkedNews()
    }

}


class BookmarkViewModelFactory(private val newsRepository: NewsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookmarkViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unable to construct HomeViewModel")
    }
}