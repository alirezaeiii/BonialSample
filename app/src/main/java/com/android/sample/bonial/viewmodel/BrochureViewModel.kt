package com.android.sample.bonial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.domain.Brochure
import com.android.sample.bonial.usecase.FilterBrochureUseCase
import com.android.sample.bonial.usecase.GetBrochureUseCase
import com.android.sample.bonial.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrochureViewModel @Inject constructor(
    private val getBrochureUseCase: GetBrochureUseCase,
    private val filterBrochureUseCase: FilterBrochureUseCase
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<ViewState<List<Brochure>>>(ViewState.Loading)
    val stateFlow: StateFlow<ViewState<List<Brochure>>>
        get() = _stateFlow

    var isFilter = false

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
        viewModelScope.launch {
            useCase.invoke().collect {
                _stateFlow.tryEmit(it)
            }
        }
    }
}