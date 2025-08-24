package net.sinichi.nutritienttracker

import android.app.Application
import androidx.room.Room
import net.sinichi.nutritienttracker.core.repositories.FoodRepository
import net.sinichi.nutritienttracker.core.repositories.UserProfileRepository
import net.sinichi.nutritienttracker.data.local.AppDatabase
import net.sinichi.nutritienttracker.data.local.repositories.FoodRepositoryLocalImpl
import net.sinichi.nutritienttracker.data.local.repositories.UserProfileRepositoryLocalImpl

class NutrientTrackerApp : Application() {
    // These will be our singleton instances
    lateinit var database: AppDatabase
    lateinit var foodRepository: FoodRepository
    lateinit var userProfileRepository: UserProfileRepository

    override fun onCreate() {
        super.onCreate()

        // Initialize the database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "nutrient_tracker_db"
        ).build()

        // Initialize the repository
        foodRepository = FoodRepositoryLocalImpl(database.foodDao())
        userProfileRepository = UserProfileRepositoryLocalImpl(database.userProfileDao())
    }
}