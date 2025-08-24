package net.sinichi.nutritienttracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1, // We will only ever have one user profile, so the ID can be constant
    val dailyGoalKcal: Int,
    val carbPercentage: Int,
    val proteinPercentage: Int,
    val fatPercentage: Int
)