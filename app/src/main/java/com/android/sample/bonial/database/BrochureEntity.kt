package com.android.sample.bonial.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.sample.bonial.domain.Brochure

@Entity(tableName = "brochure")
data class BrochureEntity(
    @PrimaryKey
    val id: Long,
    val image: String,
    val retailerName: String,
    val isPremium: Boolean,
    val distance: Double
)

fun List<BrochureEntity>.asDomainModel(): List<Brochure> = this.map {
    Brochure(
        id = it.id,
        image = it.image,
        retailerName = it.retailerName,
        isPremium = it.isPremium,
        distance = it.distance
    )
}