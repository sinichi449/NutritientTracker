package net.sinichi.nutritienttracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FoodItemEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun foodDao(): FoodDao
}