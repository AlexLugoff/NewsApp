package com.example.newsapp.presentation.main

import com.example.newsapp.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<MainViewState, MainEvent>()