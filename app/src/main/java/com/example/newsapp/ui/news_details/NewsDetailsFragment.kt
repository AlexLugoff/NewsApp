package com.example.newsapp.ui.news_details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentNewsDetailsBinding
import com.example.newsapp.showLongToast
import com.example.newsapp.showShortToast
import com.example.newsapp.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri
import com.example.newsapp.setSafeOnClickListener

@AndroidEntryPoint
class NewsDetailsFragment : BaseFragment<
        NewsDetailsViewState,
        NewsDetailsEvent,
        NewsDetailsViewModel,
        FragmentNewsDetailsBinding>() {

    override val viewModel: NewsDetailsViewModel by viewModels()

    // Получение аргументов из Navigation Component
    private val args: NewsDetailsFragmentArgs by navArgs()

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewsDetailsBinding {
        return FragmentNewsDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    override fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleUiState(state)
                }
            }
        }
    }

    private fun handleUiState(state: NewsDetailsViewState) {
        binding.apply {
            when (state) {
                is NewsDetailsViewState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    contentGroup.visibility = View.GONE
                }

                is NewsDetailsViewState.Success -> {
                    progressBar.visibility = View.GONE
                    contentGroup.visibility = View.VISIBLE

                    val newsItem = state.newsItem
                    titleTextView.text = newsItem.title
                    openInBrowserButton.setSafeOnClickListener {
                        openBrowser(newsItem.link)
                    }
                    val description = Html.fromHtml(newsItem.description, Html.FROM_HTML_MODE_COMPACT)
                    if (description.trim().isNotEmpty()) {
                        descriptionTextView.visibility = View.VISIBLE
                        descriptionTextView.text = description
                    } else {
                        descriptionTextView.visibility = View.GONE
                    }
                    descriptionTextView.text =
                        Html.fromHtml(newsItem.description, Html.FROM_HTML_MODE_COMPACT)
                    dateTextView.text = dateFormatter.format(Date(newsItem.date))

                    if (!newsItem.imageUrl.isNullOrEmpty()) {
                        Glide.with(newsImageView.context)
                            .load(newsItem.imageUrl)
                            .placeholder(R.drawable.placeholder_image_24)
                            .into(newsImageView)
                        newsImageView.visibility = View.VISIBLE
                    } else {
                        newsImageView.visibility = View.GONE
                    }
                }

                is NewsDetailsViewState.Error -> {
                    progressBar.visibility = View.GONE
                    contentGroup.visibility = View.GONE
                    showLongToast(state.message)
                }
            }
        }

    }

    private fun openBrowser(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(intent)
        } catch (e: Exception) {
            showShortToast("Не удалось открыть ссылку: ${e.message}")
        }
    }
}