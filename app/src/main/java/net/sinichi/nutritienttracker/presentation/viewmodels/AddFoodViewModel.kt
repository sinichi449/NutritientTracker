package net.sinichi.nutritienttracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.sinichi.nutritienttracker.core.data.FoodRepository
import net.sinichi.nutritienttracker.core.entities.FoodItem
import net.sinichi.nutritienttracker.presentation.states.AddFoodUiState

class AddFoodViewModel(
    private val repository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddFoodUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onCarbsChange(newCarbs: String) {
        _uiState.update { it.copy(carbs = newCarbs) }
    }

    fun onProteinChange(newProtein: String) {
        _uiState.update { it.copy(protein = newProtein) }
    }

    fun onFatChange(newFat: String) {
        _uiState.update { it.copy(fat = newFat) }
    }

    fun saveFoodItem() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val foodItem = FoodItem(
                name = currentState.name,
                carbs = currentState.carbs.toDoubleOrNull() ?: 0.0,
                protein = currentState.protein.toDoubleOrNull() ?: 0.0,
                fat = currentState.fat.toDoubleOrNull() ?: 0.0,
                timestamp = System.currentTimeMillis()
            )
            repository.insertFoodItem(foodItem)
        }
    }
}