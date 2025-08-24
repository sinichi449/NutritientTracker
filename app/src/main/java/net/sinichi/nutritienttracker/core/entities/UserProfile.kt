package net.sinichi.nutritienttracker.core.entities

data class UserProfile(
    val dailyGoalKcal: Int = 2200,
    val carbPercentage: Int = 40,
    val proteinPercentage: Int = 35,
    val fatPercentage: Int = 25
)
