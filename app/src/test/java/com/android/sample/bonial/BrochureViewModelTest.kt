package com.android.sample.bonial

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.database.BrochureDao
import com.android.sample.bonial.extention.isNetworkAvailable
import com.android.sample.bonial.network.ApiService
import com.android.sample.bonial.repository.BrochureRepositoryImpl
import com.android.sample.bonial.repository.FilterBrochureRepositoryImpl
import com.android.sample.bonial.response.BrochureResponse
import com.android.sample.bonial.usecase.FilterBrochureUseCase
import com.android.sample.bonial.usecase.GetBrochureUseCase
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

    @Mock
    private lateinit var dao: BrochureDao

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
        testCoroutineRule.pauseDispatcher()

        val viewModel = getBrochureViewModel()

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
        testCoroutineRule.pauseDispatcher()

        val viewModel = getBrochureViewModel()

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
        testCoroutineRule.pauseDispatcher()

        val viewModel = getBrochureViewModel()

        assertThat(viewModel.stateFlow.value, `is`(ViewState.Loading))

        testCoroutineRule.resumeDispatcher()

        assertThat(viewModel.stateFlow.value, `is`(ViewState.Error(errorMsg)))
    }

    private fun getBrochureViewModel() : BrochureViewModel {
        val brochureRepository = BrochureRepositoryImpl(context, api, dao, Dispatchers.Main)
        val getBrochureUseCase = GetBrochureUseCase(brochureRepository)

        val filterBrochureRepository = FilterBrochureRepositoryImpl(dao, Dispatchers.Main)
        val filterBrochureUseCase = FilterBrochureUseCase(filterBrochureRepository)

        return BrochureViewModel(getBrochureUseCase, filterBrochureUseCase)
    }
}