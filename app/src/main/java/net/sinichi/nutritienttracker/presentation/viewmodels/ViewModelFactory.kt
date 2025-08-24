package net.sinichi.nutritienttracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sinichi.nutritienttracker.core.data.FoodRepository

class ViewModelFactory(private val repository: FoodRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        // Add other ViewModels here in the future
        // if (modelClass.isAssignableFrom(AddFoodViewModel::class.java)) { ... }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}