package net.sinichi.nutritienttracker.core.repositories

import kotlinx.coroutines.flow.Flow
import net.sinichi.nutritienttracker.core.entities.FoodItem

interface FoodRepository {
    suspend fun insertFoodItem(item: FoodItem)
    fun getRecentFoodItems(limit: Int): Flow<List<FoodItem>>
    fun getFoodItemsForDay(date: Long): Flow<List<FoodItem>>
    suspend fun getFoodItemById(id: String): FoodItem?
    suspend fun updateFoodItem(item: FoodItem)
    suspend fun deleteFoodItem(item: FoodItem)
}