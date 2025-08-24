package net.sinichi.nutritienttracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.sinichi.nutritienttracker.core.entities.FoodItem
import net.sinichi.nutritienttracker.core.repositories.FoodRepository
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

    fun onQuantityChange(newQuantity: String) {
        _uiState.update { it.copy(quantity = newQuantity) }
    }

    fun saveFoodItem() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val quantity = currentState.quantity.toDoubleOrNull() ?: 1.0

            val foodItem = FoodItem(
                name = currentState.name,
                // Apply the multiplier
                carbs = (currentState.carbs.toDoubleOrNull() ?: 0.0) * quantity,
                protein = (currentState.protein.toDoubleOrNull() ?: 0.0) * quantity,
                fat = (currentState.fat.toDoubleOrNull() ?: 0.0) * quantity,
                timestamp = System.currentTimeMillis()
            )
            repository.insertFoodItem(foodItem)

            // Add this line to reset the state after saving
            resetState()
        }
    }

    // Add this new function to clear the input fields
    private fun resetState() {
        _uiState.value = AddFoodUiState()
    }
}