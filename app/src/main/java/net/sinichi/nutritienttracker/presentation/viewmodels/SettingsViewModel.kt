package net.sinichi.nutritienttracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.sinichi.nutritienttracker.core.entities.UserProfile
import net.sinichi.nutritienttracker.core.repositories.UserProfileRepository
import net.sinichi.nutritienttracker.presentation.states.SettingsUiState

class SettingsViewModel(
    private val userProfileRepository: UserProfileRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    // Expose the user profile as a StateFlow so the UI can observe it
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState()
        )

    init {
        viewModelScope.launch {
            userProfileRepository.getUserProfile().let {
                _uiState.value = SettingsUiState(
                    userProfile = it.first()
                )
            }
        }
    }

    fun onDailyKcalGoalChange(newDailyKcalGoal: String) {
        _uiState.value = _uiState.value.copy(
            userProfile = _uiState.value.userProfile.copy(
                dailyGoalKcal = newDailyKcalGoal.toIntOrNull() ?: 0
            )
        )
    }

    fun onCarbPercentageChange(newCarbPercentage: String) {
        _uiState.value = _uiState.value.copy(
            userProfile = _uiState.value.userProfile.copy(
                carbPercentage = newCarbPercentage.toIntOrNull() ?: 0
            )
        )
    }

    fun onProteinPercentageChange(newProteinPercentage: String) {
        _uiState.value = _uiState.value.copy(
            userProfile = _uiState.value.userProfile.copy(
                proteinPercentage = newProteinPercentage.toIntOrNull() ?: 0
            )
        )
    }

    fun onFatPercentageChange(newFatPercentage: String) {
        _uiState.value = _uiState.value.copy(
            userProfile = _uiState.value.userProfile.copy(
                fatPercentage = newFatPercentage.toIntOrNull() ?: 0
            )
        )
    }

    fun saveSettings() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val currentValue = currentState.userProfile

            val updatedUserProfile = UserProfile(
                dailyGoalKcal = currentValue.dailyGoalKcal,
                carbPercentage = currentValue.carbPercentage,
                proteinPercentage = currentValue.proteinPercentage,
                fatPercentage = currentValue.fatPercentage
            )

            userProfileRepository.updateUserProfile(updatedUserProfile)
        }
    }
}