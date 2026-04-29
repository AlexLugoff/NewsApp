package io.github.alexlugoff.newsapp.core.testing

import io.github.alexlugoff.newsapp.core.common.dispatchers.AppDispatchers
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
            main = testDispatcher,
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
