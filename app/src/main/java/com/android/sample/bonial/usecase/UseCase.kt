package com.android.sample.bonial.usecase

import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.domain.Brochure
import kotlinx.coroutines.flow.Flow

interface UseCase {
    operator fun invoke(): Flow<ViewState<List<Brochure>>>
}