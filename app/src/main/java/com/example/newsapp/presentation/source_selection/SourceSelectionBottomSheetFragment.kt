package com.example.newsapp.presentation.source_selection

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.newsapp.databinding.FragmentSourceSelectionBottomSheetBinding
import com.example.newsapp.presentation.common.BaseBottomSheetDialogFragment
import com.example.newsapp.showLongToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SourceSelectionBottomSheetFragment : BaseBottomSheetDialogFragment<
        SourceSelectionViewState,
        SourceSelectionEvent,
        SourceSelectionViewModel,
        FragmentSourceSelectionBottomSheetBinding>() {

    override val viewModel: SourceSelectionViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSourceSelectionBottomSheetBinding {
        return FragmentSourceSelectionBottomSheetBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NewsSourceAdapter { source ->
            viewModel.toggleSource(source)
        }
        binding.rvSources.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sourcesState.collect { sources ->
                    adapter.submitList(sources)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorEvent.collect { errorMessage ->
                    showLongToast(errorMessage)
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        setFragmentResult("sources_updated", bundleOf("isChanged" to true))
        super.onDismiss(dialog)
    }
}