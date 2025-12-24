package com.example.newsapp.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentNewsListBinding
import com.example.newsapp.ui.common.BaseFragment
import com.example.newsapp.ui.common.CommonEvent
import dagger.hilt.android.AndroidEntryPoint
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
    }

    private fun setupUi() {
        binding.apply {
            newsAdapter = NewsListAdapter(viewModel::onNewsItemClick)

            binding.newsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = newsAdapter
            }
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.loadNews()
            }
        }
    }

    override fun handleViewState(viewState: NewsListViewState?) {
        binding.apply {
            val isListEmpty = newsAdapter.itemCount == 0

            swipeRefreshLayout.isRefreshing = viewState is NewsListViewState.Loading
                    && isListEmpty

            progressBar.isVisible =
                viewState is NewsListViewState.Loading && isListEmpty
            newsRecyclerView.isVisible =
                viewState is NewsListViewState.Success || !isListEmpty
            errorStatusTextView.isVisible = false

            when (viewState) {
                is NewsListViewState.Loading -> {
                    // Действия не требуются, логика видимости обработана выше.
                    // Если isListEmpty == false, мы видим старые данные + SwipeRefreshIndicator.
                }

                is NewsListViewState.Success -> {
                    errorStatusTextView.isVisible = false
                    newsAdapter.submitList(viewState.news)

                    if (viewState.news.isEmpty()) {
                        errorStatusTextView.text =
                            getString(R.string.empty_list_message)
                        errorStatusTextView.isVisible = true
                        newsRecyclerView.isVisible = false
                    }
                }

                is NewsListViewState.Error -> {
                    errorStatusTextView.text = viewState.message.asString(requireContext())
                    Timber.e(viewState.message.asString(requireContext()))

                    if (isListEmpty) {
                        errorStatusTextView.isVisible = true
                        newsRecyclerView.isVisible = false
                    } else {
                        CommonEvent.ShowLongToast(viewState.message.asString(requireContext()))
                    }
                }

                else -> Unit
            }
        }
    }

    override fun handleEvent(event: NewsListEvent?) {
        when (event) {
            is NewsListEvent.NavigateToNewsDetails -> {
                showDetails(event.newsLink)
            }

            else -> Unit
        }
    }

    private fun showDetails(newsLink: String) {
        val action =
            NewsListFragmentDirections.actionNewsListFragmentToNewsDetailsFragment(newsLink)
        findNavController().navigate(action)
    }
}