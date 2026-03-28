package com.example.newsapp.core.data

import com.example.newsapp.core.common.AppDispatchers
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    protected val testDispatcher get() = mainDispatcherRule.testDispatcher

    protected val testDispatchers by lazy {
        AppDispatchers(
            main = testDispatcher as MainCoroutineDispatcher,
            io = testDispatcher,
            default = testDispatcher,
            unconfined = testDispatcher
        )
    }

    @Before
    open fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    open fun tearDown() {
        clearAllMocks()
    }
}
