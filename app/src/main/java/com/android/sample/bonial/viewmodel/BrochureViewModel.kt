package com.android.sample.bonial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.domain.Brochure
import com.android.sample.bonial.repository.BrochureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrochureViewModel @Inject constructor(
    private val repository: BrochureRepository,
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<ViewState<List<Brochure>>>(ViewState.Loading)
    val stateFlow: StateFlow<ViewState<List<Brochure>>>
        get() = _stateFlow

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            repository.getBrochures().collect {
                _stateFlow.tryEmit(it)
            }
        }
    }
}