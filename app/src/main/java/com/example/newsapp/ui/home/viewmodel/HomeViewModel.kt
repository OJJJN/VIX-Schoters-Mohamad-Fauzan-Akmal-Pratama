package com.example.newsapp.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.newsapp.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.data.datasource.local.NewsRoomLocalDatabase
import com.example.newsapp.data.datasource.remote.NewsApi
import com.example.newsapp.data.datasource.remote.NewsRemoteDatasource
import com.example.newsapp.data.repositories.NewsRepository
import com.example.newsapp.domain.models.NewsModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class HomeUiState {
    data class Success(val data: List<NewsModel>) : HomeUiState()
    data class Error(val messageId: Int) : HomeUiState()
    object Loading : HomeUiState()
    object Empty : HomeUiState()
}


class HomeViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val TAG = "HomeViewModel"

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Empty)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        refreshNews()
    }

    fun getNews() {
        viewModelScope.launch {
            newsRepository.news.collectLatest {
                if (it.isEmpty()) {
                    refreshNews()
                    _uiState.value = HomeUiState.Error(R.string.refresh_error)
                } else {
                    _uiState.value = HomeUiState.Success(it)
                }
            }
        }
    }

    fun refreshNews() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                val news = newsRepository.refreshNews()
                _uiState.value = HomeUiState.Success(news)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(R.string.refresh_error)
                Log.e(TAG, e.toString())
                getNews()
            }
        }
    }

    fun bookmarkNews(news: NewsModel) {
        viewModelScope.launch {
            newsRepository.bookmarkNews(news)
        }
        getNews()
    }

    fun deleteBookmarkedNews(news: NewsModel) {
        viewModelScope.launch {
            newsRepository.deleteBookmarkedNews(news)
        }
        getNews()
    }

}

class HomeViewModelFactory(private val newsRepository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unable to construct HomeViewModel")
    }
}