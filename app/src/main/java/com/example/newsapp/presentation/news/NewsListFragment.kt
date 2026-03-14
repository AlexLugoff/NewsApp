package com.example.newsapp.presentation.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentNewsListBinding
import com.example.newsapp.presentation.common.BaseFragment
import com.example.newsapp.presentation.common.CommonEvent
import com.example.newsapp.presentation.source_selection.SourceSelectionBottomSheetFragment
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
        childFragmentManager.setFragmentResultListener("sources_updated", viewLifecycleOwner) { _, bundle ->
            val isChanged = bundle.getBoolean("isChanged", false)
            if (isChanged) {
                viewModel.refreshNews()
            }
        }
        setupUi()
        setupMenu()
    }

    private fun setupUi() {
        binding.apply {
            newsAdapter = NewsListAdapter(viewModel::onNewsItemClick)

            binding.newsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = newsAdapter
            }
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshNews()
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

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Очищаем меню, если нужно, чтобы не было дублей, и инфлейтим наше
                menuInflater.inflate(R.menu.menu_news_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sources -> {
                        showSourceSelection()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showSourceSelection() {
        val bottomSheet = SourceSelectionBottomSheetFragment()
        bottomSheet.show(childFragmentManager, "SourceSelection")
    }
}