package com.example.newsapp.presentation.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.newsapp.extensions.showLongToast
import com.example.newsapp.extensions.showShortToast
import kotlinx.coroutines.launch

abstract class BaseFragment<VS, E, VM : BaseViewModel<VS, E>, VB : ViewBinding> : Fragment() {

    abstract val viewModel: VM

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected open fun handleEvent(event: E?) = Unit

    protected open fun handleViewState(viewState: VS?) = Unit

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeStateFlow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeStateFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.uiStateFlow.collect { handleViewState(it) } }
                launch { viewModel.eventFlow.collect { handleEvent(it) } }
                launch { viewModel.commonEventFlow.collect { handleCommonEvent(it) } }
            }
        }
    }

    protected fun navigateBack() {
        findNavController().popBackStack()
    }

    private fun handleCommonEvent(commonEvent: CommonEvent) {
        when (commonEvent) {
            is CommonEvent.ShowShortToast -> {
                commonEvent.uiText?.let { showShortToast(it.asString(requireContext())) }
                commonEvent.text?.let { showShortToast(it) }
                commonEvent.textResId?.let { showShortToast(it, *commonEvent.args) }
            }

            is CommonEvent.ShowLongToast -> {
                commonEvent.uiText?.let { showLongToast(it.asString(requireContext())) }
                commonEvent.text?.let { showLongToast(it) }
                commonEvent.textResId?.let { showLongToast(it, *commonEvent.args) }
            }
        }
    }
}