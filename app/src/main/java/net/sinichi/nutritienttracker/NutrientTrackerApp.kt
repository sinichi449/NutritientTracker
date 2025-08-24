package net.sinichi.nutritienttracker

import android.app.Application
import androidx.room.Room
import net.sinichi.nutritienttracker.core.repositories.FoodRepository
import net.sinichi.nutritienttracker.data.local.AppDatabase
import net.sinichi.nutritienttracker.data.local.repositories.FoodRepositoryLocalImpl

class NutrientTrackerApp : Application() {
    // These will be our singleton instances
    lateinit var database: AppDatabase
    lateinit var foodRepository: FoodRepository

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
    }
}