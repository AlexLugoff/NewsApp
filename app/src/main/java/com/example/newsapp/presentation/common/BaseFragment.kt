package com.example.newsapp.presentation.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.newsapp.extensions.showLongToast
import com.example.newsapp.extensions.showShortToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<VS, E, VM : BaseViewModel<VS, E>, VB : ViewBinding> : Fragment() {

    abstract val viewModel: VM

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected open fun handleEvent(event: E?) = Unit

    protected open fun handleViewState(viewState: VS?) = Unit

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    private val eventObserver = Observer<E> { e -> e?.let { handleEvent(it) } }
    private val commonEventObserver =
        Observer<CommonEvent> { ce -> handleCommonEvent(ce) }

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
        observeViewModel()
        observeStateFlow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected open fun observeViewModel() {
        viewModel.event.observe(viewLifecycleOwner, eventObserver)
        viewModel.commonEvent.observe(viewLifecycleOwner, commonEventObserver)
    }

    private fun observeStateFlow() {
        collectLatestLifecycleFlow(viewModel.uiStateFlow) { state ->
            state?.let { handleViewState(it) }
        }
    }

    protected fun <T> collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(collect)
            }
        }
    }

//    protected fun setupToolbar(
//        toolbar: Toolbar,
//        title: UniversalText,
//        @DrawableRes navigationIconResId: Int? = R.drawable.ic_back,
//        onNavigationIconClick: () -> Unit = ::navigateBack
//    ) {
//        toolbar.apply {
//            setTitle(title.asString(requireContext()))
//            navigationIconResId?.let { setNavigationIcon(navigationIconResId) }
//            setNavigationOnClickListener { onNavigationIconClick() }
//        }
//    }

    protected fun navigateBack() {
        findNavController().popBackStack()
    }

    private fun handleCommonEvent(commonEvent: CommonEvent) {
        when (commonEvent) {
            is CommonEvent.ShowShortToast -> {
                commonEvent.text?.let { showShortToast(it) }
                commonEvent.textResId?.let { showShortToast(it, *commonEvent.args) }
            }

            is CommonEvent.ShowLongToast -> {
                commonEvent.text?.let { showLongToast(it) }
                commonEvent.textResId?.let { showLongToast(it, *commonEvent.args) }
            }
        }
    }
}