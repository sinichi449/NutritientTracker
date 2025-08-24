package net.sinichi.nutritienttracker.presentation.ui

import net.sinichi.nutritienttracker.core.entities.MacroNutrientInfo
import net.sinichi.nutritienttracker.core.entities.RecentFoodItems

data class HomeUiState(
    val dailyIntakeProgress: Float = 0f,
    val caloriesConsumed: Int = 0,
    val calorieGoal: Int = 2200, // This can be dynamic later
    val macroNutrients: List<MacroNutrientInfo> = emptyList(),
    val recentFoods: List<RecentFoodItems> = emptyList(),
    val isLoading: Boolean = true
)
