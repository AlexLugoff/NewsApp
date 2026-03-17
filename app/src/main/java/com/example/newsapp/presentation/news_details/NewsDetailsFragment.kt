package com.example.newsapp.presentation.news_details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentNewsDetailsBinding
import com.example.newsapp.presentation.common.BaseFragment
import com.example.newsapp.presentation.common.CommonEvent
import com.example.newsapp.extensions.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailsFragment :
    BaseFragment<NewsDetailsViewState, NewsDetailsEvent, NewsDetailsViewModel, FragmentNewsDetailsBinding>() {

    override val viewModel: NewsDetailsViewModel by viewModels()

    private val args: NewsDetailsFragmentArgs by navArgs()

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentNewsDetailsBinding {
        return FragmentNewsDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadNewsDetails(args.newsLink)
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
                    val description = newsItem.description
                    if (description.isNotBlank()) {
                        descriptionTextView.isVisible = true
                        descriptionTextView.text = description
                    } else {
                        descriptionTextView.isVisible = false
                    }
                    dateTextView.text = newsItem.formattedDate

                    if (!newsItem.imageUrl.isNullOrBlank()) {
                        newsImageView.isVisible = true
                        newsImageView.load(newsItem.imageUrl) {
                            placeholder(R.drawable.placeholder_image_24)
                        }
                    } else {
                        newsImageView.isVisible = false
                    }
                }

                is NewsDetailsViewState.Error -> {
                    progressBar.isVisible = false
                    contentGroup.isVisible = false
                    CommonEvent.ShowLongToast(viewState.message.asString(requireContext()))
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