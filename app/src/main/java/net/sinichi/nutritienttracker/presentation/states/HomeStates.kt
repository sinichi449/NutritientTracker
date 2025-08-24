package net.sinichi.nutritienttracker.presentation.states

import net.sinichi.nutritienttracker.core.entities.FoodCategory
import net.sinichi.nutritienttracker.core.entities.FoodItem
import net.sinichi.nutritienttracker.core.entities.MacroNutrientInfo

data class HomeUiState(
    val dailyIntakeProgress: Float = 0f,
    val caloriesConsumed: Int = 0,
    val calorieGoal: Int = 2200, // This can be dynamic later
    val macroNutrients: List<MacroNutrientInfo> = emptyList(),
    val recentFoods: List<FoodItem> = emptyList(),
    val isLoading: Boolean = true
)

// Data class to hold the state of the input fields
data class AddFoodUiState(
    val name: String = "",
    val carbs: String = "",
    val protein: String = "",
    val fat: String = "",
    val quantity: String = "1",
    val category: FoodCategory = FoodCategory.MEAL,
)
