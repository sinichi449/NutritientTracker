package net.sinichi.nutritienttracker.presentation.states

import net.sinichi.nutritienttracker.core.entities.UserProfile

data class SettingsUiState(
    val userProfile: UserProfile = UserProfile(),
    // TODO: Another settings here
)