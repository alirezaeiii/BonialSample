package com.android.sample.bonial

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.network.ApiService
import com.android.sample.bonial.repository.BrochureRepositoryImpl
import com.android.sample.bonial.response.BrochureResponse
import com.android.sample.bonial.viewmodel.BrochureViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class BrochureViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var api: ApiService

    @Mock
    private lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
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
}