package com.example.newsapp.ui.common

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VS, E, VM : BaseViewModel<VS, E>, VB : ViewBinding> : AppCompatActivity() {

    abstract val viewModel: VM

    protected abstract fun getViewBinding(): VB

    protected open fun handleEvent(event: E?) = Unit

    protected open fun handleViewState(state: VS) = Unit

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    private val viewStateObserver = Observer<VS> { viewState -> viewState?.let { handleViewState(it) } }
    private val eventObserver = Observer<E> { event -> event?.let { handleEvent(it) } }

    protected fun setupBindingAndContentView() {
        _binding = getViewBinding()
        setContentView(binding.root)
    }

    protected open fun setupObservers() {
        viewModel.viewState.observe(this, viewStateObserver)
        viewModel.event.observe(this, eventObserver)
    }
}