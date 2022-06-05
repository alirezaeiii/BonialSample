package com.android.sample.bonial.repository

import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.domain.Brochure
import kotlinx.coroutines.flow.Flow

interface FilterBrochureRepository {
    fun getBrochures(): Flow<ViewState<List<Brochure>>>
}