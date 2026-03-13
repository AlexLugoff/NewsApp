package com.example.newsapp.presentation.source_selection

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
import com.example.newsapp.showLongToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SourceSelectionBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSourceSelectionBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SourceSelectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSourceSelectionBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NewsSourceAdapter { source ->
            viewModel.toggleSource(source)
        }
        binding.rvSources.adapter = adapter

        viewModel.sources.observe(viewLifecycleOwner) { sources ->
            adapter.submitList(sources)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorEvent.collect { errorMessage ->
                    showLongToast(errorMessage)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setFragmentResult("sources_updated", bundleOf("isChanged" to true))
        _binding = null
    }
}