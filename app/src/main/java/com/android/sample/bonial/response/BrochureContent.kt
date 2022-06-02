package com.android.sample.bonial.response

data class BrochureContent(
    val id: Long,
    val retailer: Retailer?,
    val brochureImage: String,
    val distance: Double,
) {
    data class Retailer(
        val name: String
    )
}