package com.android.sample.bonial.network

import com.android.sample.bonial.response.BrochureResponse
import retrofit2.http.GET

interface ApiService {

    @GET("stories-test/shelf.json")
    suspend fun getBrochures(): BrochureResponse
}