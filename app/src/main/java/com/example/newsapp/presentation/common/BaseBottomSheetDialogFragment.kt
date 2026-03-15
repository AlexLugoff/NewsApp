package com.example.newsapp.presentation.common

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<VS, E, VM : BaseViewModel<VS, E>, VB : ViewBinding> :
    BottomSheetDialogFragment() {

    abstract val viewModel: VM

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected open fun handleEvent(event: E) = Unit

    protected open fun handleViewState(viewState: VS) = Unit

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    private val viewStateObserver = Observer<VS> { vs -> vs?.let { handleViewState(it) } }
    private val eventObserver = Observer<E> { e -> e?.let { handleEvent(it) } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val themedInflater =
            inflater.cloneInContext(ContextThemeWrapper(requireContext(), requireContext().theme))
        _binding = getViewBinding(themedInflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected open fun observeViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
        viewModel.event.observe(viewLifecycleOwner, eventObserver)
    }
}