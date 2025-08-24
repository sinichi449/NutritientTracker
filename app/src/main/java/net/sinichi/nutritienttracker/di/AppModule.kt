package net.sinichi.nutritienttracker.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.sinichi.nutritienttracker.core.data.FoodRepository
import net.sinichi.nutritienttracker.core.data.FoodRepositoryLocalImpl
import net.sinichi.nutritienttracker.data.local.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "nutrient_tracker_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFoodRepository(db: AppDatabase): FoodRepository {
        return FoodRepositoryLocalImpl(db.foodDao())
    }
}