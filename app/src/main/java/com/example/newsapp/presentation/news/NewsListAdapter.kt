package com.example.newsapp.presentation.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.newsapp.R
import com.example.newsapp.databinding.NewsItemBinding
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.setSafeOnClickListener

class NewsListAdapter(private val onItemClicked: (newsLink: String) -> Unit) :
    ListAdapter<NewsItem, NewsListAdapter.NewsViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = getItem(position)
        holder.bind(newsItem, onItemClicked)
    }

    class NewsViewHolder(private val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NewsItem, onItemClicked: (newsLink: String) -> Unit) {
            binding.apply {
                titleTextView.text = item.title
                val description = item.description
                if (description.isNotBlank()) {
                    descriptionTextView.isVisible = true
                    descriptionTextView.text = description
                } else {
                    descriptionTextView.isVisible = false
                }
                if (!item.imageUrl.isNullOrBlank()) {
                    newsImageView.isVisible = true
                    newsImageView.load(item.imageUrl) {
                        placeholder(R.drawable.placeholder_image_24)
                    }
                } else {
                    newsImageView.isVisible = false
                }

                root.setSafeOnClickListener {
                    onItemClicked(item.link)
                }
            }

        }
    }
}

class NewsDiffCallback : DiffUtil.ItemCallback<NewsItem>() {
    override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem == newItem
    }
}