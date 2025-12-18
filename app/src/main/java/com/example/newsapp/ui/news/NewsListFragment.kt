package com.example.newsapp.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentNewsListBinding
import com.example.newsapp.showLongToast
import com.example.newsapp.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class NewsListFragment : BaseFragment<
        NewsListViewState,
        NewsListEvent,
        NewsListViewModel,
        FragmentNewsListBinding>() {

    override val viewModel: NewsListViewModel by viewModels()

    private lateinit var newsAdapter: NewsListAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewsListBinding {
        return FragmentNewsListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObservers()
    }

    private fun setupUi() {
        binding.apply {
            newsAdapter = NewsListAdapter { newsLink ->
                showDetails(newsLink)
            }

            binding.newsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = newsAdapter
            }
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.loadNews()
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleUiState(state)
                }
            }
        }
    }

    private fun handleUiState(state: NewsListViewState) {
        binding.apply {
            val isListEmpty = newsAdapter.itemCount == 0

            swipeRefreshLayout.isRefreshing = state is NewsListViewState.Loading
                    && isListEmpty

            progressBar.isVisible =
                state is NewsListViewState.Loading && isListEmpty
            newsRecyclerView.isVisible =
                state is NewsListViewState.Success || !isListEmpty
            errorStatusTextView.isVisible = false

            when (state) {
                is NewsListViewState.Loading -> {
                    // Действия не требуются, логика видимости обработана выше.
                    // Если isListEmpty == false, мы видим старые данные + SwipeRefreshIndicator.
                }

                is NewsListViewState.Success -> {
                    errorStatusTextView.isVisible = false
                    newsAdapter.submitList(state.news)

                    if (state.news.isEmpty()) {
                        errorStatusTextView.text =
                            getString(R.string.empty_list_message)
                        errorStatusTextView.isVisible = true
                        newsRecyclerView.isVisible = false
                    }
                }

                is NewsListViewState.Error -> {
                    errorStatusTextView.text = state.message
                    Timber.e(state.message)

                    if (isListEmpty) {
                        binding.errorStatusTextView.isVisible = true
                        binding.newsRecyclerView.isVisible = false
                    } else {
                        showLongToast(state.message)
                    }
                }
            }
        }

    }

    private fun showDetails(newsLink: String) {
        val action =
            NewsListFragmentDirections.actionNewsListFragmentToNewsDetailsFragment(newsLink)
        findNavController().navigate(action)
    }

}