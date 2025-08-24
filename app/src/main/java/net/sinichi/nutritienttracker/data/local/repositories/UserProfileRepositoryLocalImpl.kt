package net.sinichi.nutritienttracker.data.local.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import net.sinichi.nutritienttracker.core.entities.UserProfile
import net.sinichi.nutritienttracker.core.repositories.UserProfileRepository
import net.sinichi.nutritienttracker.data.local.UserProfileDao
import net.sinichi.nutritienttracker.data.local.mapper.toDomain
import net.sinichi.nutritienttracker.data.local.mapper.toEntity

class UserProfileRepositoryLocalImpl(
    private val dao: UserProfileDao
): UserProfileRepository {

    // Define the default profile
    private val defaultProfile = UserProfile(
        dailyGoalKcal = 2200,
        carbPercentage = 40,
        proteinPercentage = 35,
        fatPercentage = 25
    )

    init {
        // This is a simple way to ensure the default profile exists.
        // runBlocking is used here for simplicity during initialization.
        // In a more complex app, you might handle this in a database callback.
        runBlocking {
            dao.insertOrUpdateProfile(defaultProfile.toEntity())
        }
    }

    override fun getUserProfile(): Flow<UserProfile> {
        return dao.getProfile()
            .distinctUntilChanged()
            .map { entity ->
                // If the entity is null, it means the DB is empty, so we return the default.
                // The `init` block should prevent this, but it's a good safeguard.
                entity?.toDomain() ?: defaultProfile
            }
    }

    override suspend fun saveUserProfile(userProfile: UserProfile) {
        dao.insertOrUpdateProfile(userProfile.toEntity())
    }

    override suspend fun updateUserProfile(userProfile: UserProfile) {
        dao.insertOrUpdateProfile(userProfile.toEntity())
    }
}