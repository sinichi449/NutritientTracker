package net.sinichi.nutritienttracker.core.repositories

import kotlinx.coroutines.flow.Flow
import net.sinichi.nutritienttracker.core.entities.UserProfile

interface UserProfileRepository {
    fun getUserProfile(): Flow<UserProfile>
    suspend fun saveUserProfile(userProfile: UserProfile)
    suspend fun updateUserProfile(userProfile: UserProfile)
}