package com.android.sample.bonial.repository

import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.domain.Brochure
import kotlinx.coroutines.flow.Flow

interface BrochureRepository {
    fun getBrochures(): Flow<ViewState<List<Brochure>>>
}