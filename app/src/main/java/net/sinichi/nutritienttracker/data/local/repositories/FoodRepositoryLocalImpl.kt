package net.sinichi.nutritienttracker.data.local.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.sinichi.nutritienttracker.core.entities.FoodItem
import net.sinichi.nutritienttracker.core.repositories.FoodRepository
import net.sinichi.nutritienttracker.data.local.FoodDao
import net.sinichi.nutritienttracker.data.local.mapper.toDomain
import net.sinichi.nutritienttracker.data.local.mapper.toEntity
import java.util.Calendar

class FoodRepositoryLocalImpl(
    private val dao: FoodDao
): FoodRepository {
    override suspend fun insertFoodItem(item: FoodItem) {
        // Convert core entity to Room entity
        dao.insertFoodItem(item.toEntity())
    }

    override fun getRecentFoodItems(limit: Int): Flow<List<FoodItem>> {
        return dao.getAllFoodItems().map { list ->
            // Map to the core FoodItem entity here
            list.take(limit).map { it.toDomain() }
        }
    }

    override fun getFoodItemsForDay(date: Long): Flow<List<FoodItem>> {
        val calendar = Calendar.getInstance().apply { timeInMillis = date }
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        val startOfDay = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val endOfDay = calendar.timeInMillis

        return dao.getFoodItemsForDay(startOfDay, endOfDay).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getFoodItemById(id: String): FoodItem? {
        return dao.getFoodItemById(id)?.toDomain()
    }

    override suspend fun updateFoodItem(item: FoodItem) {
        dao.updateFoodItem(item.toEntity())
    }

    override suspend fun deleteFoodItem(item: FoodItem) {
        dao.deleteFoodItem(item.toEntity())
    }
}