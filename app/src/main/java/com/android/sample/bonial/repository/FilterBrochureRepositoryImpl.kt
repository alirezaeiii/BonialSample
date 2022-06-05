package com.android.sample.bonial.repository

import android.content.Context
import com.android.sample.bonial.di.IoDispatcher
import com.android.sample.bonial.domain.Brochure
import com.android.sample.bonial.network.ApiService
import com.android.sample.bonial.response.Content
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FilterBrochureRepositoryImpl @Inject constructor(
    context: Context,
    service: ApiService,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FilterBrochureRepository, BrochureRepositoryImpl(context, service, ioDispatcher) {

    override fun filter(content: List<Content>): List<Brochure> =
        super.filter(content).filter { it.distance < 5 }

}