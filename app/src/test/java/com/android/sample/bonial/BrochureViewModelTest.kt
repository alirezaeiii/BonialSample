package com.android.sample.bonial

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.extention.isNetworkAvailable
import com.android.sample.bonial.network.ApiService
import com.android.sample.bonial.repository.BrochureRepositoryImpl
import com.android.sample.bonial.response.BrochureResponse
import com.android.sample.bonial.viewmodel.BrochureViewModel
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BrochureViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var api: ApiService

    @Mock
    private lateinit var context: Context

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
        mockkStatic("com.android.sample.bonial.extention.ContextExtKt")
        every {
            context.isNetworkAvailable()
        } returns true
        testCoroutineRule.runBlockingTest {
            `when`(api.getBrochures()).thenReturn(BrochureResponse(
                BrochureResponse.Embedded(
                    emptyList())))
        }

        val repository = BrochureRepositoryImpl(context, api, Dispatchers.Main)

        testCoroutineRule.pauseDispatcher()

        val viewModel = BrochureViewModel(repository)

        assertThat(viewModel.stateFlow.value, `is`(ViewState.Loading))

        testCoroutineRule.resumeDispatcher()

        assertThat(viewModel.stateFlow.value, `is`(ViewState.Success(emptyList())))
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        val errorMsg = "error message"
        `when`(context.getString(anyInt())).thenReturn(errorMsg)
        mockkStatic("com.android.sample.bonial.extention.ContextExtKt")
        every {
            context.isNetworkAvailable()
        } returns true
        testCoroutineRule.runBlockingTest {
            `when`(api.getBrochures()).thenThrow(RuntimeException(""))
        }
        val repository = BrochureRepositoryImpl(context, api, Dispatchers.Main)

        testCoroutineRule.pauseDispatcher()

        val viewModel = BrochureViewModel(repository)

        assertThat(viewModel.stateFlow.value, `is`(ViewState.Loading))

        testCoroutineRule.resumeDispatcher()

        assertThat(viewModel.stateFlow.value, `is`(ViewState.Error(errorMsg)))
    }

    @Test
    fun givenNetworkUnAvailable_whenFetch_shouldReturnError() {
        val errorMsg = "error message"
        `when`(context.getString(anyInt())).thenReturn(errorMsg)
        mockkStatic("com.android.sample.bonial.extention.ContextExtKt")
        every {
            context.isNetworkAvailable()
        } returns false
        val repository = BrochureRepositoryImpl(context, api, Dispatchers.Main)

        testCoroutineRule.pauseDispatcher()

        val viewModel = BrochureViewModel(repository)

        assertThat(viewModel.stateFlow.value, `is`(ViewState.Loading))

        testCoroutineRule.resumeDispatcher()

        assertThat(viewModel.stateFlow.value, `is`(ViewState.Error(errorMsg)))
    }
}