package net.sinichi.nutritienttracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import net.sinichi.nutritienttracker.core.repositories.UserProfileRepository

class SettingsViewModel(
    private val userProfileRepository: UserProfileRepository,
): ViewModel() {
}