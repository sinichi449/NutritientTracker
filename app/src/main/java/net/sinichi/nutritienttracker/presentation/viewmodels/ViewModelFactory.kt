package net.sinichi.nutritienttracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sinichi.nutritienttracker.core.repositories.FoodRepository
import net.sinichi.nutritienttracker.core.repositories.UserProfileRepository

class ViewModelFactory(
    private val foodRepository: FoodRepository,
    private val userProfileRepository: UserProfileRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(foodRepository, userProfileRepository) as T
            }
            modelClass.isAssignableFrom(AddFoodViewModel::class.java) -> {
                AddFoodViewModel(foodRepository) as T
            }
            // We will handle EditFoodViewModel separately

            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(userProfileRepository) as T // Provide the repo
            }

            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}