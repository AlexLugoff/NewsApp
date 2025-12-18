package com.example.newsapp.ui.news_details

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.newsapp.DATE_FORMAT_PATTERN
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentNewsDetailsBinding
import com.example.newsapp.setSafeOnClickListener
import com.example.newsapp.ui.common.BaseFragment
import com.example.newsapp.ui.common.CommonEvent
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class NewsDetailsFragment :
    BaseFragment<NewsDetailsViewState, NewsDetailsEvent, NewsDetailsViewModel, FragmentNewsDetailsBinding>() {

    override val viewModel: NewsDetailsViewModel by viewModels()

    private val dateFormatter = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentNewsDetailsBinding {
        return FragmentNewsDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    override fun handleViewState(viewState: NewsDetailsViewState?) {
        binding.apply {
            when (viewState) {
                is NewsDetailsViewState.Loading -> {
                    progressBar.isVisible = true
                    contentGroup.isVisible = false
                }

                is NewsDetailsViewState.Success -> {
                    progressBar.isVisible = false
                    contentGroup.isVisible = true

                    val newsItem = viewState.newsItem
                    titleTextView.text = newsItem.title
                    openInBrowserButton.setSafeOnClickListener {
                        viewModel.goToBrowser(newsItem.link)
                    }
                    val description =
                        Html.fromHtml(newsItem.description, Html.FROM_HTML_MODE_COMPACT)
                    if (description.trim().isNotEmpty()) {
                        descriptionTextView.isVisible = true
                        descriptionTextView.text = description
                    } else {
                        descriptionTextView.isVisible = false
                    }
                    descriptionTextView.text =
                        Html.fromHtml(newsItem.description, Html.FROM_HTML_MODE_COMPACT)
                    dateTextView.text = dateFormatter.format(Date(newsItem.date))

                    if (!newsItem.imageUrl.isNullOrEmpty()) {
                        Glide.with(newsImageView.context).load(newsItem.imageUrl)
                            .placeholder(R.drawable.placeholder_image_24).into(newsImageView)
                        newsImageView.isVisible = true
                    } else {
                        newsImageView.isVisible = false
                    }
                }

                is NewsDetailsViewState.Error -> {
                    progressBar.isVisible = false
                    contentGroup.isVisible = false
                    CommonEvent.ShowLongToast(viewState.message)
                }

                else -> Unit
            }
        }
    }

    override fun handleEvent(event: NewsDetailsEvent?) {
        when (event) {
            is NewsDetailsEvent.GoToBrowser -> {
                openBrowser(event.url)
            }

            else -> Unit
        }
    }

    private fun openBrowser(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(intent)
        } catch (e: Exception) {
            CommonEvent.ShowShortToast(getString(R.string.failed_to_open_link, e.message))
        }
    }
}