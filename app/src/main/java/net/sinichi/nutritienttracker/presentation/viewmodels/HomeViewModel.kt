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
import net.sinichi.nutritienttracker.core.repositories.FoodRepository
import net.sinichi.nutritienttracker.core.repositories.UserProfileRepository
import net.sinichi.nutritienttracker.presentation.states.HomeUiState

class HomeViewModel(
    private val foodRepository: FoodRepository,
    private val userProfileRepository: UserProfileRepository,
): ViewModel() {

    // Get a flow of today's food items from the repository
    private val todaysFoodFlow = foodRepository.getFoodItemsForDay(System.currentTimeMillis())

    // Get a flow of the 10 most recent food items
    private val recentFoodsFlow = foodRepository.getRecentFoodItems(10)

    // Get the user profile flow
    private val userProfileFlow = userProfileRepository.getUserProfile()

    // Combine the flows to create the final UI state
    val uiState: StateFlow<HomeUiState> = combine(
        todaysFoodFlow,
        recentFoodsFlow,
        userProfileFlow,
    ) { todaysFood, recentFoods, userProfile ->
        // Calculate totals from today's food list
        val caloriesConsumed = todaysFood.sumOf { it.calories }
        val carbsConsumed = todaysFood.sumOf { it.carbs }
        val proteinConsumed = todaysFood.sumOf { it.protein }
        val fatConsumed = todaysFood.sumOf { it.fat }

        // Calculate goals based on user profile
        val calorieGoal = userProfile.dailyGoalKcal
        val carbsGoal = (calorieGoal * (userProfile.carbPercentage.toDouble() / 100))
        val proteinGoal = (calorieGoal * (userProfile.proteinPercentage.toDouble() / 100))
        val fatGoal = (calorieGoal * (userProfile.fatPercentage.toDouble() / 100))


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
            recentFoods = recentFoods,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState() // Start with a default loading state
    )

    fun deleteFoodItem(item: FoodItem) {
        viewModelScope.launch {
            foodRepository.deleteFoodItem(item)
        }
    }
}