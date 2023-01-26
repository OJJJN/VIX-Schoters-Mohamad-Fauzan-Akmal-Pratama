package com.example.newsapp.ui.bookmark.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.newsapp.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.data.datasource.local.NewsRoomLocalDatabase
import com.example.newsapp.data.datasource.remote.NewsApi
import com.example.newsapp.data.datasource.remote.NewsRemoteDatasource
import com.example.newsapp.data.repositories.NewsRepository
import com.example.newsapp.databinding.FragmentBookmarkBinding
import com.example.newsapp.ui.adapter.NewsListAdapter
import com.example.newsapp.ui.bookmark.viewmodel.BookmarkUiState
import com.example.newsapp.ui.bookmark.viewmodel.BookmarkViewModel
import com.example.newsapp.ui.bookmark.viewmodel.BookmarkViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding: FragmentBookmarkBinding
        get() = _binding!!

    private lateinit var viewModel: BookmarkViewModel
    private lateinit var uiState: StateFlow<BookmarkUiState>
    private lateinit var bookmarkedNewsList: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val application = requireActivity().application as NewsApplication
        val database = application.database
        val newsRoomLocalDatabase =
            NewsRoomLocalDatabase(database.newsDao(), database.bookmarkedNewsDao())
        val newsApiService = NewsApi.networkService
        val newsRemoteDatasource = NewsRemoteDatasource(newsApiService)
        val newsRepository =
            NewsRepository(
                newsLocalDatasource = newsRoomLocalDatabase,
                newsRemoteDatasource = newsRemoteDatasource
            )
        viewModel = BookmarkViewModelFactory(newsRepository).create(BookmarkViewModel::class.java)
        uiState = viewModel.uiState
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            bookmarkedNewsList = NewsListAdapter(
                onBookmarkClickListener = {
                    viewModel.deleteBookmarkedNews(it)
                },
                onItemClickListener = {
                    findNavController().navigate(
                        BookmarkFragmentDirections.actionBookmarkFragmentToDetailNewsFragment(
                            it.url
                        )
                    )
                }
            )
            rvNews.adapter = bookmarkedNewsList
            tvEmptyList.setText(R.string.bookmark_empty_msg)
        }
        bindState()
    }

    private fun bindState() {
        lifecycleScope.launchWhenStarted {
            uiState.collectLatest {
                when (it) {
                    is BookmarkUiState.Success -> {
                        binding.tvEmptyList.visibility = View.GONE
                        bookmarkedNewsList.submitList(it.data)
                        if (it.data.isEmpty()) {
                            binding.tvEmptyList.visibility = View.VISIBLE
                        }
                    }
                    is BookmarkUiState.Empty -> Unit
                }
            }
        }
    }

    private fun showSnackbar(messageId: Int) {
        Snackbar.make(
            binding.root,
            resources.getString(messageId),
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}