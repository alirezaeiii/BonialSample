package com.android.sample.bonial.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BrochureDao {
    @Query("SELECT * FROM brochure")
    fun getAllBrochures(): List<BrochureEntity>

    @Query("SELECT * FROM brochure WHERE distance<5")
    fun getFilteredBrochures(): List<BrochureEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrochures(brochures: List<BrochureEntity>)

    @Query("DELETE FROM brochure")
    suspend fun deleteBrochures()
}