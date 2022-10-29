package com.android.sample.bonial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.domain.Brochure
import com.android.sample.bonial.usecase.FilterBrochureUseCase
import com.android.sample.bonial.usecase.GetBrochureUseCase
import com.android.sample.bonial.usecase.UseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BrochureViewModel @AssistedInject constructor(
    private val getBrochureUseCase: GetBrochureUseCase,
    private val filterBrochureUseCase: FilterBrochureUseCase
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<ViewState<List<Brochure>>>(ViewState.Loading)
    val stateFlow: StateFlow<ViewState<List<Brochure>>>
        get() = _stateFlow

    var isFilter = false

    private var job: Job? = null

    init {
        refreshBrochures()
    }

    fun enableFilter() {
        isFilter = isFilter.not()
        filter()
    }

    fun filter() {
        if (isFilter) {
            refreshFilterBrochures()
        } else {
            refreshBrochures()
        }
    }

    private fun refreshBrochures() {
        refresh(getBrochureUseCase)
    }

    private fun refreshFilterBrochures() {
        refresh(filterBrochureUseCase)
    }

    private fun refresh(useCase: UseCase) {
        job?.cancel()
        job = viewModelScope.launch {
            useCase.invoke().collect {
                _stateFlow.tryEmit(it)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): BrochureViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create() as T
            }
        }
    }
}