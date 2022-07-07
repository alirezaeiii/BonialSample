package com.android.sample.bonial.domain

import com.android.sample.bonial.database.BrochureEntity

data class Brochure(
    val id: Long,
    val image: String,
    val retailerName: String,
    val isPremium: Boolean,
    val distance: Double
)

fun List<Brochure>.asDatabaseModel(): List<BrochureEntity> = this.map {
    BrochureEntity(
        id = it.id,
        image = it.image,
        retailerName = it.retailerName,
        isPremium = it.isPremium,
        distance = it.distance
    )
}