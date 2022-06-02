package com.android.sample.bonial.response

import com.squareup.moshi.Json

data class BrochureResponse(
    @Json(name = "_embedded")
    val embedded: Embedded,
) {
    data class Embedded(val contents: List<Content>)
}

