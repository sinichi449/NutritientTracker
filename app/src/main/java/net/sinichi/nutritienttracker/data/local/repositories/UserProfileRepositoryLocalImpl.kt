package net.sinichi.nutritienttracker.data.local.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.sinichi.nutritienttracker.core.entities.UserProfile
import net.sinichi.nutritienttracker.core.repositories.UserProfileRepository
import net.sinichi.nutritienttracker.data.local.UserProfileDao
import net.sinichi.nutritienttracker.data.local.mapper.toDomain
import net.sinichi.nutritienttracker.data.local.mapper.toEntity

class UserProfileRepositoryLocalImpl(
    private val dao: UserProfileDao
): UserProfileRepository {
    override fun getUserProfile(): Flow<UserProfile> {
        return dao.getProfile().map { entity ->
            entity?.toDomain() ?: UserProfile()
        }
    }

    override suspend fun saveUserProfile(userProfile: UserProfile) {
        dao.insertProfile(userProfile.toEntity())
    }
}