package com.android.sample.bonial.domain

data class Brochure(
    val id: Long,
    val image: String,
    val retailerName: String,
    val isPremium: Boolean,
    val distance: Double
)