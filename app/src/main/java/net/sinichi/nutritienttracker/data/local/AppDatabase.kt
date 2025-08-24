package net.sinichi.nutritienttracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        FoodItemEntity::class,
        UserProfileEntity::class
   ],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun userProfileDao(): UserProfileDao
}