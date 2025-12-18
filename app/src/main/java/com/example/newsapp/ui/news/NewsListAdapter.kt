package com.example.newsapp.ui.news

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.NewsItemBinding
import com.example.newsapp.domain.models.NewsItem

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
                val description = Html.fromHtml(item.description, Html.FROM_HTML_MODE_COMPACT)
                if (description.trim().isNotEmpty()) {
                    descriptionTextView.visibility = View.VISIBLE
                    descriptionTextView.text = description
                } else {
                    descriptionTextView.visibility = View.GONE
                }
                if (!item.imageUrl.isNullOrEmpty()) {
                    Glide.with(newsImageView.context)
                        .load(item.imageUrl)
                        .placeholder(R.drawable.placeholder_image_24) // Предполагаемый ресурс
                        .into(newsImageView)
                    newsImageView.visibility = View.VISIBLE
                } else {
                    newsImageView.visibility = View.GONE
                }

                root.setOnClickListener {
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