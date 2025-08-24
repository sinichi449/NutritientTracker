package net.sinichi.nutritienttracker.core.entities

data class RecentFoodItem(
    val name: String,
    val time: String,
    val carbs: Double,
    val protein: Double,
    val fat: Double
) {
    // Calories are now a calculated property
    val calories: Int
        get() = calculateCalories(carbs, protein, fat).toInt()
}