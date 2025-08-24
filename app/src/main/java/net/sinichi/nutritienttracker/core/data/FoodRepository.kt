package net.sinichi.nutritienttracker.core.data

import kotlinx.coroutines.flow.Flow
import net.sinichi.nutritienttracker.core.entities.FoodItem

interface FoodRepository {
    suspend fun insertFoodItem(item: FoodItem)
    fun getRecentFoodItems(limit: Int): Flow<List<FoodItem>>
    fun getFoodItemsForDay(date: Long): Flow<List<FoodItem>>
}