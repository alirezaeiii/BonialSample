package com.android.sample.bonial.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BrochureEntity::class], version = 1)
abstract class BrochureDatabase : RoomDatabase() {

    abstract fun brochureDao(): BrochureDao
}