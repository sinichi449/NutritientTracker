package net.sinichi.nutritienttracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import net.sinichi.nutritienttracker.core.data.FoodRepository

class HomeViewModel(
    private val repository: FoodRepository
): ViewModel() {
}