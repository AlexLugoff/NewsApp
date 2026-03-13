package com.example.newsapp.presentation.source_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.FragmentSourceSelectionBottomSheetItemBinding
import com.example.newsapp.domain.models.NewsSourceItem

class NewsSourceAdapter(
    private val onCheckedChange: (NewsSourceItem) -> Unit
) : ListAdapter<NewsSourceItem, NewsSourceAdapter.SourceViewHolder>(SourceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        val binding = FragmentSourceSelectionBottomSheetItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SourceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SourceViewHolder(private val binding: FragmentSourceSelectionBottomSheetItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(source: NewsSourceItem) {
            binding.sourceName.text = source.name
            binding.sourceCheckbox.setOnCheckedChangeListener(null) // Важно!
            binding.sourceCheckbox.isChecked = source.isEnabled

            binding.sourceCheckbox.setOnCheckedChangeListener { _, _ ->
                onCheckedChange(source)
            }

            binding.root.setOnClickListener {
                binding.sourceCheckbox.toggle()
            }
        }
    }

    class SourceDiffCallback : DiffUtil.ItemCallback<NewsSourceItem>() {
        override fun areItemsTheSame(oldItem: NewsSourceItem, newItem: NewsSourceItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NewsSourceItem, newItem: NewsSourceItem) =
            oldItem == newItem
    }
}