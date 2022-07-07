package com.android.sample.bonial.repository

import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.database.BrochureDao
import com.android.sample.bonial.database.asDomainModel
import com.android.sample.bonial.di.IoDispatcher
import com.android.sample.bonial.domain.Brochure
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FilterBrochureRepositoryImpl @Inject constructor(
    private val dao: BrochureDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FilterBrochureRepository {

    override fun getBrochures(): Flow<ViewState<List<Brochure>>> = flow {
        emit(ViewState.Loading)
        val cache = dao.getFilteredBrochures()
        emit(ViewState.Success(cache.asDomainModel()))
    }.flowOn(ioDispatcher)
}