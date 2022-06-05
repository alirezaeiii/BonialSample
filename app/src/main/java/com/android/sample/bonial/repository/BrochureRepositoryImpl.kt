package com.android.sample.bonial.repository

import android.content.Context
import com.android.sample.bonial.R
import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.di.IoDispatcher
import com.android.sample.bonial.domain.Brochure
import com.android.sample.bonial.domain.BrochureConverter
import com.android.sample.bonial.extention.isNetworkAvailable
import com.android.sample.bonial.network.ApiService
import com.android.sample.bonial.response.Content
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

open class BrochureRepositoryImpl @Inject constructor(
    private val context: Context,
    private val service: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BrochureRepository {

    override fun getBrochures(): Flow<ViewState<List<Brochure>>> = flow {
        emit(ViewState.Loading)
        if (context.isNetworkAvailable()) {
            try {
                val response = service.getBrochures()
                val brochures = filter(response.embedded.contents)

                emit(ViewState.Success(brochures))
            } catch (t: Throwable) {
                emit(ViewState.Error(context.getString(R.string.failed_loading_msg)))
            }
        } else {
            emit(ViewState.Error(context.getString(R.string.failed_network_msg)))
        }
    }.flowOn(ioDispatcher)

    open fun filter(content: List<Content>) =
        content.filterIsInstance<BrochureConverter>().map {
            it.convert()
        }
}