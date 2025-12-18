package com.example.newsapp.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.newsapp.getRootFragment
import com.example.newsapp.showLongToast
import com.example.newsapp.showShortToast

abstract class BaseFragment<VS, E, VM : BaseViewModel<VS, E>, VB : ViewBinding> : Fragment() {

    abstract val viewModel: VM

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected open fun handleEvent(event: E?) = Unit

    protected open fun handleViewState(viewState: VS?) = Unit

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    private val viewStateObserver = Observer<VS> { vs -> vs?.let { handleViewState(it) } }
    private val eventObserver = Observer<E> { e -> e?.let { handleEvent(it) } }
    private val commonEventObserver =
        Observer<CommonEvent> { ce -> ce?.let { handleCommonEvent(it) } }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected open fun observeViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
        viewModel.event.observe(viewLifecycleOwner, eventObserver)
        viewModel.commonEvent.observe(viewLifecycleOwner, commonEventObserver)
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