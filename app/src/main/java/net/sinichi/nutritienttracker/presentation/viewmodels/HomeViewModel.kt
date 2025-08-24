package net.sinichi.nutritienttracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.sinichi.nutritienttracker.core.entities.FoodItem
import net.sinichi.nutritienttracker.core.entities.MacroNutrientInfo
import net.sinichi.nutritienttracker.core.entities.RecentFoodItems.Companion.toRecentFoodItems
import net.sinichi.nutritienttracker.core.repositories.FoodRepository
import net.sinichi.nutritienttracker.presentation.states.HomeUiState

class HomeViewModel(
    private val repository: FoodRepository
): ViewModel() {

    // Define calorie and macro goals
    private val calorieGoal = 2200
    private val carbsGoal = 220.0 // 40% of 2200 kcal
    private val proteinGoal = 192.5 // 35% of 2200 kcal
    private val fatGoal = 61.1 // 25% of 2200 kcal

    // Get a flow of today's food items from the repository
    private val todaysFoodFlow = repository.getFoodItemsForDay(System.currentTimeMillis())

    // Get a flow of the 10 most recent food items
    private val recentFoodsFlow = repository.getRecentFoodItems(10)

    // Combine the flows to create the final UI state
    val uiState: StateFlow<HomeUiState> = combine(
        todaysFoodFlow,
        recentFoodsFlow
    ) { todaysFood, recentFoods ->
        // Calculate totals from today's food list
        val caloriesConsumed = todaysFood.sumOf { it.calories }
        val carbsConsumed = todaysFood.sumOf { it.carbs }
        val proteinConsumed = todaysFood.sumOf { it.protein }
        val fatConsumed = todaysFood.sumOf { it.fat }

        // Create the list of macronutrient info
        val macros = listOf(
            MacroNutrientInfo("Carbs", carbsConsumed, carbsGoal),
            MacroNutrientInfo("Fat", fatConsumed, fatGoal),
            MacroNutrientInfo("Protein", proteinConsumed, proteinGoal)
        )

        // Return the complete UI state object
        HomeUiState(
            caloriesConsumed = caloriesConsumed.toInt(),
            calorieGoal = calorieGoal,
            dailyIntakeProgress = if (calorieGoal > 0) (caloriesConsumed.toFloat() / calorieGoal) else 0f,
            macroNutrients = macros,
            recentFoods = recentFoods.toRecentFoodItems(), // Convert FoodItem to RecentFoodItem
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState() // Start with a default loading state
    )

    fun deleteFoodItem(item: FoodItem) {
        viewModelScope.launch {
            repository.deleteFoodItem(item)
        }
    }
}