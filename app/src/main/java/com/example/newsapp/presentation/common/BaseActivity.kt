package com.example.newsapp.presentation.common

import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VS, E, VM : BaseViewModel<VS, E>, VB : ViewBinding> :
    AppCompatActivity() {

    abstract val viewModel: VM

    protected abstract fun getViewBinding(): VB

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected fun setupBindingAndContentView() {
        _binding = getViewBinding()
        setContentView(binding.root)
    }
}