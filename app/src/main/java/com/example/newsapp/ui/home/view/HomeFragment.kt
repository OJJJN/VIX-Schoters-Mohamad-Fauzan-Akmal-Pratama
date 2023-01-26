package com.example.newsapp.ui.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.newsapp.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.data.datasource.local.NewsRoomLocalDatabase
import com.example.newsapp.data.datasource.remote.NewsApi
import com.example.newsapp.data.datasource.remote.NewsApiService
import com.example.newsapp.data.datasource.remote.NewsRemoteDatasource
import com.example.newsapp.data.repositories.NewsRepository
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.ui.adapter.NewsListAdapter
import com.example.newsapp.ui.home.viewmodel.HomeUiState
import com.example.newsapp.ui.home.viewmodel.HomeViewModel
import com.example.newsapp.ui.home.viewmodel.HomeViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"
    private lateinit var viewModel: HomeViewModel
    private lateinit var uiState: StateFlow<HomeUiState>
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    private val newsListAdapter = NewsListAdapter(
        onBookmarkClickListener = {
            Log.d(TAG, it.toString())
            if (it.isBookmarked) {
                viewModel.deleteBookmarkedNews(it)
            } else {
                viewModel.bookmarkNews(it)
            }
        },
        onItemClickListener = {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailNewsFragment(
                    it.url
                )
            )
        },
    )

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
        viewModel = HomeViewModelFactory(newsRepository).create(HomeViewModel::class.java)
        uiState = viewModel.uiState
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvNews.adapter = newsListAdapter
        }
        bindState()
    }

    private fun bindState() {
        lifecycleScope.launchWhenStarted {
            uiState.collectLatest {
                when (it) {
                    is HomeUiState.Loading -> {
                        showSnackbar(R.string.loading)
                    }
                    is HomeUiState.Error -> {
                        showSnackbar(it.messageId)
                    }
                    is HomeUiState.Success -> {
                        newsListAdapter.submitList(it.data)
                    }
                    is HomeUiState.Empty -> Unit
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