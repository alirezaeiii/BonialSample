package com.android.sample.bonial.repository

import android.content.Context
import com.android.sample.bonial.R
import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.di.IoDispatcher
import com.android.sample.bonial.domain.Brochure
import com.android.sample.bonial.domain.BrochureConverter
import com.android.sample.bonial.network.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BrochureRepositoryImpl @Inject constructor(
    private val context: Context,
    private val service: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BrochureRepository {

    override fun getBrochures(): Flow<ViewState<List<Brochure>>> = flow {
        emit(ViewState.Loading)
        try {
            val response = service.getBrochures()
            val brochures =
                response.embedded.contents.filterIsInstance<BrochureConverter>().map {
                    it.convert()
                }.filter {
                    it.distance < 5
                }
            emit(ViewState.Success(brochures))
        } catch (t: Throwable) {
            emit(ViewState.Error(context.getString(R.string.failed_loading_msg)))
        }
    }.flowOn(ioDispatcher)
}