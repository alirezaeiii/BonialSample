package com.android.sample.bonial.repository

import android.content.Context
import com.android.sample.bonial.R
import com.android.sample.bonial.common.ViewState
import com.android.sample.bonial.database.BrochureDao
import com.android.sample.bonial.database.asDomainModel
import com.android.sample.bonial.di.IoDispatcher
import com.android.sample.bonial.domain.Brochure
import com.android.sample.bonial.domain.BrochureConverter
import com.android.sample.bonial.domain.asDatabaseModel
import com.android.sample.bonial.extention.isNetworkAvailable
import com.android.sample.bonial.network.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

open class BrochureRepositoryImpl @Inject constructor(
    private val context: Context,
    private val service: ApiService,
    private val dao: BrochureDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BrochureRepository {

    override fun getBrochures(): Flow<ViewState<List<Brochure>>> = flow {
        emit(ViewState.Loading)
        val cache = dao.getAllBrochures()
        if (cache.isNotEmpty()) {
            // ****** STEP 1: VIEW CACHE ******
            emit(ViewState.Success(cache.asDomainModel()))
            try {
                // ****** STEP 2: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
                refresh(true)
                // ****** STEP 3: VIEW CACHE ******
                emit(ViewState.Success(dao.getAllBrochures().asDomainModel()))
            } catch (t: Throwable) {
                Timber.e(t)
            }
        } else {
            if (context.isNetworkAvailable()) {
                try {
                    // ****** STEP 1: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
                    refresh(false)
                    // ****** STEP 2: VIEW CACHE ******
                    emit(ViewState.Success(dao.getAllBrochures().asDomainModel()))
                } catch (t: Throwable) {
                    emit(ViewState.Error(context.getString(R.string.failed_loading_msg)))
                }
            } else {
                emit(ViewState.Error(context.getString(R.string.failed_network_msg)))
            }
        }
    }.flowOn(ioDispatcher)

    private suspend fun refresh(shouldDeleteBrochures: Boolean) {
        val response = service.getBrochures()
        val brochures = response.embedded.contents.filterIsInstance<BrochureConverter>().map {
            it.convert()
        }
        if(shouldDeleteBrochures) {
            dao.deleteBrochures()
        }
        dao.insertBrochures(brochures.asDatabaseModel())
    }
}