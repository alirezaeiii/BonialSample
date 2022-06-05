package com.android.sample.bonial.usecase

import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.domain.Brochure
import com.android.sample.bonial.repository.FilterBrochureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilterBrochureUseCase @Inject constructor(
    private val filterBrochureRepository: FilterBrochureRepository
): UseCase {
    override fun invoke(): Flow<ViewState<List<Brochure>>> =
        filterBrochureRepository.getBrochures()
}