package net.sinichi.nutritienttracker.presentation.viewmodels

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.sinichi.nutritienttracker.core.entities.FoodItem
import net.sinichi.nutritienttracker.core.entities.MacroNutrientInfo
import net.sinichi.nutritienttracker.core.repositories.FoodRepository
import net.sinichi.nutritienttracker.core.repositories.UserProfileRepository
import net.sinichi.nutritienttracker.presentation.states.HomeUiState

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val foodRepository: FoodRepository,
    private val userProfileRepository: UserProfileRepository,
): ViewModel() {

    // 1. State to hold the selected date, defaulting to today
    private val _selectedDate = MutableStateFlow(System.currentTimeMillis())
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()


    // 2. The food flow now transforms based on the selected date
    private val foodForSelectedDateFlow = _selectedDate.flatMapLatest { date ->
        foodRepository.getFoodItemsForDay(date)
    }

    // Get the user profile flow
    private val userProfileFlow = userProfileRepository.getUserProfile()

    // Combine the flows to create the final UI state
    val uiState: StateFlow<HomeUiState> = combine(
        foodForSelectedDateFlow,
        userProfileFlow,
    ) { dailyFood, userProfile ->
        // Calculate totals from today's food list
        val caloriesConsumed = dailyFood.sumOf { it.calories }
        val carbsConsumed = dailyFood.sumOf { it.carbs }
        val proteinConsumed = dailyFood.sumOf { it.protein }
        val fatConsumed = dailyFood.sumOf { it.fat }

        // Calculate goals based on user profile
        val calorieGoal = userProfile.dailyGoalKcal

        // --- CORRECTED GOAL CALCULATIONS ---
        val targetCaloriesFromCarbs = calorieGoal * (userProfile.carbPercentage.toDouble() / 100)
        val carbsGoal = if (calorieGoal > 0) targetCaloriesFromCarbs / 4.0 else 0.0 // 4 kcal/gram for carbs

        val targetCaloriesFromProtein = calorieGoal * (userProfile.proteinPercentage.toDouble() / 100)
        val proteinGoal = if (calorieGoal > 0) targetCaloriesFromProtein / 4.0 else 0.0 // 4 kcal/gram for protein

        val targetCaloriesFromFat = calorieGoal * (userProfile.fatPercentage.toDouble() / 100)
        val fatGoal = if (calorieGoal > 0) targetCaloriesFromFat / 9.0 else 0.0 // 9 kcal/gram for fat
        // --- END OF CORRECTIONS ---

        // Create the list of macronutrient info
        val macros = listOf(
            MacroNutrientInfo("Kabohidrat", carbsConsumed, carbsGoal),
            MacroNutrientInfo("Protein", proteinConsumed, proteinGoal),
            MacroNutrientInfo("Lemak", fatConsumed, fatGoal),
        )

        // Return the complete UI state object
        HomeUiState(
            caloriesConsumed = caloriesConsumed.toInt(),
            calorieGoal = calorieGoal,
            dailyIntakeProgress = if (calorieGoal > 0) (caloriesConsumed.toFloat() / calorieGoal) else 0f,
            macroNutrients = macros,
            // The list now directly uses the food from the selected date
            recentFoods = dailyFood,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState() // Start with a default loading state
    )

    // 3. Function to update the selected date from the UI
    fun onDateSelected(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        _selectedDate.value = calendar.timeInMillis
    }

    fun deleteFoodItem(item: FoodItem) {
        viewModelScope.launch {
            foodRepository.deleteFoodItem(item)
        }
    }
}